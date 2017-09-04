#include "SNBitmap.h"

#include <android/log.h>
#include <stdlib.h>
#include <setjmp.h>

#include "jpeg-8c/jpeglib.h"
#include "jpeg-8c/jerror.h"

#include "SNJNICommon.h"

#define LOG_TAG "SNBitmap(native)"

#ifdef DEBUG
#define LOGD LOGI
#else
#define LOGD 1?0:
#endif

#define MAKE8888(r, g, b) (uint32_t)((((uint32_t)r)) | (((uint32_t)g) << 8) | (((uint32_t)b) << 16) | 0xFF000000)

#define SUCCESS 0
#define FAILED  -1

/* -- for libjpeg -- */

struct JpegErrorManager {
    struct jpeg_error_mgr jError;
    jmp_buf jmpBuf;
};

static void _JpegError(j_common_ptr cinfo)
{
    char pszMessage[JMSG_LENGTH_MAX];

    (*cinfo->err->format_message)(cinfo, pszMessage);
    LOGI("error!  %s", pszMessage);

    longjmp(((struct JpegErrorManager *)cinfo->err)->jmpBuf, 1);
}

typedef struct {
    struct jpeg_source_mgr pub; /* public fields */

    JOCTET * buffer;
    unsigned long buffer_length;
} memory_source_mgr;
typedef memory_source_mgr *memory_src_ptr;

METHODDEF(void) memory_init_source (j_decompress_ptr cinfo)
{
}

METHODDEF(boolean) memory_fill_input_buffer (j_decompress_ptr cinfo)
{
    memory_src_ptr src = (memory_src_ptr) cinfo->src;

    src->buffer[0] = (JOCTET) 0xFF;
    src->buffer[1] = (JOCTET) JPEG_EOI;
    src->pub.next_input_byte = src->buffer;
    src->pub.bytes_in_buffer = 2;
    return TRUE;
}

METHODDEF(void) memory_skip_input_data (j_decompress_ptr cinfo, long num_bytes)
{
    memory_src_ptr src = (memory_src_ptr) cinfo->src;

    if (num_bytes > 0) {
        src->pub.next_input_byte += (size_t) num_bytes;
        src->pub.bytes_in_buffer -= (size_t) num_bytes;
    }
}

METHODDEF(void) memory_term_source (j_decompress_ptr cinfo)
{
}

GLOBAL(void)
jpeg_memory_src (j_decompress_ptr cinfo, void* data, unsigned long len)
{
    memory_src_ptr src;

    if (cinfo->src == NULL) {   /* first time for this JPEG object? */
        cinfo->src = (struct jpeg_source_mgr *)
        (*cinfo->mem->alloc_small) ((j_common_ptr) cinfo, JPOOL_PERMANENT,
          sizeof(memory_source_mgr));
        src = (memory_src_ptr) cinfo->src;
        src->buffer = (JOCTET *)
        (*cinfo->mem->alloc_small) ((j_common_ptr) cinfo, JPOOL_PERMANENT,
          len * sizeof(JOCTET));
    }

    src = (memory_src_ptr) cinfo->src;

    src->pub.init_source = memory_init_source;
    src->pub.fill_input_buffer = memory_fill_input_buffer;
    src->pub.skip_input_data = memory_skip_input_data;
    src->pub.resync_to_restart = jpeg_resync_to_restart; /* use default method */
    src->pub.term_source = memory_term_source;

    src->pub.bytes_in_buffer = len;
    src->pub.next_input_byte = (JOCTET*)data;
}

/* -- for libjpeg end -- */

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeWriteJPEG(JNIEnv *env, jobject obj, jstring filePath, int quality)
{
    LOGI("%s start\n", __func__);

    jint ret = FAILED;

    SNBitmap bitmap;
    java2Native(env, obj, &bitmap);

    FILE *fp;
    const char *filePath_c;
    filePath_c = (*env)->GetStringUTFChars(env, filePath, NULL);
    fp = fopen(filePath_c, "wb");
    if(!fp) {
        LOGI("failed fopen filePath=%s", filePath_c);
        goto ERROR_1;
    }

    struct jpeg_compress_struct cinfo;
    struct JpegErrorManager error;

    cinfo.err = jpeg_std_error(&error.jError);
    error.jError.error_exit = _JpegError;
    if (setjmp(error.jmpBuf))
        goto ERROR_2;

    jpeg_create_compress(&cinfo);

    jpeg_stdio_dest(&cinfo, fp);

    cinfo.image_width = bitmap.width;
    cinfo.image_height = bitmap.height;
    cinfo.input_components = 3;
    cinfo.in_color_space = JCS_RGB;
    jpeg_set_defaults(&cinfo);
    jpeg_set_quality(&cinfo, quality, 1);
    cinfo.dct_method = JDCT_IFAST;

    jpeg_start_compress(&cinfo, 1);

    JSAMPROW line = NULL;
    line = (JSAMPROW)malloc(sizeof(JSAMPLE) * 3 * bitmap.width);
    if (!line) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, sizeof(JSAMPLE) * 3 * bitmap.width);
        goto ERROR_3;
    }

    {
        DECLARE_IMAGE_REPRESENTATION(srcImage, bitmap.address, bitmap.width, bitmap.height);
        int i, j;
        for (j = 0; j < HEIGHT(srcImage); ++j) {
            for (i = 0; i < WIDTH(srcImage); ++i) {
                int idx = i * 3;
                Pixel *pixel = (Pixel *)&PIXEL(srcImage, i, j);
                line[idx + 0] = pixel->r;
                line[idx + 1] = pixel->g;
                line[idx + 2] = pixel->b;
            }
            jpeg_write_scanlines(&cinfo, &line, 1);
        }
    }

    free(line);
    ret = SUCCESS;

ERROR_3:
    jpeg_finish_compress(&cinfo);
ERROR_2:
    jpeg_destroy_compress(&cinfo);

    fclose(fp);

ERROR_1:
    (*env)->ReleaseStringUTFChars(env, filePath, filePath_c);

    LOGI("%s end\n", __func__);
    return ret;
}

static uint32_t *decodeJPEG(struct jpeg_decompress_struct *cinfo)
{
    int nJpegLineBytes;
    JSAMPROW row[1];
    JSAMPLE *pSample;

    cinfo->two_pass_quantize = 0;
    cinfo->dither_mode = JDITHER_ORDERED;
    cinfo->dct_method = JDCT_FASTEST;
    cinfo->do_fancy_upsampling = 0;

    jpeg_start_decompress(cinfo);

    LOGI("output w=%d h=%d", cinfo->output_width, cinfo->output_height);

    nJpegLineBytes = cinfo->output_width * cinfo->output_components;

    pSample = (JSAMPLE *)malloc((nJpegLineBytes + 10) * sizeof(JSAMPLE));
    if (!pSample) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, (nJpegLineBytes + 10) * sizeof(JSAMPLE));
        goto ERROR_1;
    }

    uint32_t *buffer = (uint32_t *)malloc(cinfo->output_width * cinfo->output_height * sizeof(uint32_t));
    if (!buffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, cinfo->output_width * cinfo->output_height * sizeof(uint32_t));
        goto ERROR_2;
    }

    row[0] = pSample;

    {
        DECLARE_IMAGE_REPRESENTATION(dstImage, buffer, cinfo->output_width, cinfo->output_height);

        int j = 0;
        for (j = 0; cinfo->output_scanline < cinfo->output_height; ++j) {
            jpeg_read_scanlines(cinfo, row, 1);
            int i, x;
            for (i = 0, x = 0; i < cinfo->output_width; ++i, x += 3) {
                PIXEL(dstImage, i, j) = MAKE8888(row[0][x + 0], row[0][x + 1], row[0][x + 2]);
            }
        }
    }

ERROR_2:
    free(pSample);

ERROR_1:
    jpeg_finish_decompress(cinfo);

    return buffer;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeDecodeJPEG(JNIEnv *env, jobject obj, jbyteArray data)
{
    LOGI("%s start\n", __func__);

    jint ret = FAILED;

    int length = (*env)->GetArrayLength(env, data);
    jbyte *jpegData = (*env)->GetByteArrayElements(env, data, 0 );

    int nJpegLineBytes;
    JSAMPROW row[1];
    JSAMPLE *pSample;
    struct jpeg_decompress_struct cinfo;
    struct JpegErrorManager error;

    cinfo.err = jpeg_std_error(&error.jError);
    error.jError.error_exit = _JpegError;
    if (setjmp(error.jmpBuf))
        goto ERROR;

    jpeg_create_decompress(&cinfo);
    jpeg_memory_src(&cinfo, jpegData, length);
    jpeg_read_header(&cinfo, TRUE);

    uint32_t *buffer = decodeJPEG(&cinfo);
    if (!buffer)
        goto ERROR;

    set2Java(env, obj, buffer, cinfo.output_width, cinfo.output_height);
    ret = SUCCESS;

ERROR:
    jpeg_destroy_decompress(&cinfo);

    (*env)->ReleaseByteArrayElements(env, data, jpegData, 0);

    LOGI("%s end\n", __func__);
    return ret;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeRotate90(JNIEnv *env, jobject obj)
{
    LOGI("%s start\n", __func__);

    SNBitmap bitmap;
    java2Native(env, obj, &bitmap);

    uint32_t *buffer = malloc(bitmap.width * bitmap.height * sizeof(uint32_t));
    if (!buffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, bitmap.width * bitmap.height * sizeof(uint32_t));
        return FAILED;
    }

    DECLARE_IMAGE_REPRESENTATION(srcImage, bitmap.address, bitmap.width, bitmap.height);
    DECLARE_IMAGE_REPRESENTATION(dstImage, buffer, bitmap.height, bitmap.width);

    int i, j;
    for (j = 0; j < HEIGHT(srcImage); ++j) {
        for (i = 0; i < WIDTH(srcImage); ++i) {
            PIXEL(dstImage, HEIGHT(srcImage) - 1 - j, i) = PIXEL(srcImage, i, j);
        } 
    }

    memcpy(bitmap.address, buffer, bitmap.width * bitmap.height * sizeof(uint32_t));
    free(buffer);

    set2Java(env, obj, bitmap.address, bitmap.height, bitmap.width);

    LOGI("%s end\n", __func__);
    return SUCCESS;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeCreateVoid(JNIEnv *env, jobject obj)
{
    LOGI("%s start\n", __func__);

    SNBitmap bitmap;
    java2Native(env, obj, &bitmap);

    uint32_t *buffer = calloc(1, bitmap.width * bitmap.height * sizeof(uint32_t));
    if (!buffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, bitmap.width * bitmap.height * sizeof(uint32_t));
        return FAILED;
    }

    set2Java(env, obj, buffer, bitmap.height, bitmap.width);

    LOGI("%s end\n", __func__);
    return SUCCESS;
}

JNIEXPORT jint Java_com_kingjim_shotnote_SNBitmap_nativeReadJPEG(JNIEnv * env, jobject obj, jstring filePath, int maxWidth, int maxHeight)
{
    LOGI("%s start\n", __func__);

    jint ret = FAILED;

    int nJpegLineBytes;
    JSAMPROW row[1];
    JSAMPLE *pSample;
    FILE *fp;
    struct jpeg_decompress_struct cinfo;
    struct JpegErrorManager error;

    const char *filename_c;
    filename_c = (*env)->GetStringUTFChars(env, filePath, NULL);
    fp = fopen(filename_c, "rb");
    if(!fp) {
        LOGI("failed fopen filename=%s", filename_c);
        goto ERROR_1;
    }

    cinfo.err = jpeg_std_error(&error.jError);
    error.jError.error_exit = _JpegError;
    if (setjmp(error.jmpBuf))
        goto ERROR_2;

    jpeg_create_decompress(&cinfo);
    jpeg_stdio_src(&cinfo,fp);
    jpeg_read_header(&cinfo,TRUE);

    if (maxWidth < cinfo.image_width || maxHeight < cinfo.image_height)
        goto ERROR_2;

    uint32_t *buffer = decodeJPEG(&cinfo);
    if (!buffer)
        goto ERROR_2;

    set2Java(env, obj, buffer, cinfo.output_width, cinfo.output_height);
    ret = SUCCESS;

ERROR_2:
    jpeg_destroy_decompress(&cinfo);
    fclose(fp);

ERROR_1:
    (*env)->ReleaseStringUTFChars(env, filePath, filename_c);

    LOGI("%s end\n", __func__);
    return ret;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeCreateScaled(JNIEnv *env, jobject obj, jobject src, int dstWidth, int dstHeight)
{
    LOGI("%s start\n", __func__);

    uint32_t *dstBuffer = (uint32_t *)malloc(dstWidth * dstHeight * sizeof(uint32_t));
    if (!dstBuffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, dstWidth * dstHeight * sizeof(uint32_t));
        return FAILED;
    }

    SNBitmap srcBitmap;
    java2Native(env, src, &srcBitmap);

    DECLARE_IMAGE_REPRESENTATION(srcImage, srcBitmap.address, srcBitmap.width, srcBitmap.height);
    DECLARE_IMAGE_REPRESENTATION(dstImage, dstBuffer, dstWidth, dstHeight);

    fixed fx = FLOAT2FIXED((float)srcBitmap.width / (float)dstWidth);
    fixed fy = FLOAT2FIXED((float)srcBitmap.height / (float)dstHeight);

    int i, j;
    fixed y = FIXED_ZERO;
    for (j = 0; j < HEIGHT(dstImage); ++j) {
        int idxY = FIXED2INT(y);

        fixed x = FIXED_ZERO;
        for (i = 0; i < WIDTH(dstImage); ++i) {
            int idxX = FIXED2INT(x);
            PIXEL(dstImage, i, j) = PIXEL(srcImage, idxX, idxY);
            x += fx;
        }

        y += fy;
    }

    set2Java(env, obj, dstBuffer, dstWidth, dstHeight);

    LOGI("%s end\n", __func__);
    return SUCCESS;
}

int scaleWithFilter(SNBitmap *srcBitmap, SNBitmap *dstBitmap)
{
    START_TIME_TEST(__func__);

    uint32_t *intermediateBuffer = (uint32_t *)malloc(srcBitmap->height * dstBitmap->width * sizeof(uint32_t));
    if (!intermediateBuffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, srcBitmap->height * dstBitmap->width * sizeof(uint32_t));
        return FAILED;
    }

    float fxFloat = (float)srcBitmap->width / (float)dstBitmap->width;
    float fyFloat = (float)srcBitmap->height / (float)dstBitmap->height;
    fixed fx = FLOAT2FIXED(fxFloat);
    fixed fy = FLOAT2FIXED(fyFloat);

    DECLARE_IMAGE_REPRESENTATION(srcImage, srcBitmap->address, srcBitmap->width, srcBitmap->height);
    DECLARE_IMAGE_REPRESENTATION(dstImage, dstBitmap->address, dstBitmap->width, dstBitmap->height);
    DECLARE_IMAGE_REPRESENTATION(intImage, intermediateBuffer, dstBitmap->width, srcBitmap->height);

    fixed length = FLOAT2FIXED(1.0f / fxFloat);
    int i, j;
    for (j = 0; j < HEIGHT(srcImage); ++j) {
        int num = 0;
        uint32_t r = 0;
        uint32_t g = 0;
        uint32_t b = 0;
        int xIdx = 0;
        fixed l = FIXED_HALF;

        for (i = 0; i < WIDTH(srcImage); ++i) {
            if (xIdx < FIXED2INT(l)) {
                Pixel *intP = (Pixel *)&PIXEL(intImage, xIdx, j);
                intP->r = r / num;
                intP->g = g / num;
                intP->b = b / num;

                num = 0;
                r = 0;
                g = 0;
                b = 0;
                ++xIdx;
            }

            Pixel *srcP = (Pixel *)&PIXEL(srcImage, i, j);
            r += srcP->r;
            g += srcP->g;
            b += srcP->b;
            l += length;
            ++num;
        }

        if (xIdx < WIDTH(intImage)) {
            Pixel *intP = (Pixel *)&PIXEL(intImage, xIdx, j);
            intP->r = r / num;
            intP->g = g / num;
            intP->b = b / num;
        }
    }

    length = FLOAT2FIXED(1.0f / fyFloat);
    for (i = 0; i < WIDTH(intImage); ++i) {
        int num = 0;
        uint32_t r = 0;
        uint32_t g = 0;
        uint32_t b = 0;
        int yIdx = 0;
        fixed l = FIXED_HALF;

        for (j = 0; j < HEIGHT(intImage); ++j) {
            if (yIdx < FIXED2INT(l)) {
                Pixel *dstP = (Pixel *)&PIXEL(dstImage, i, yIdx);
                dstP->r = r / num;
                dstP->g = g / num;
                dstP->b = b / num;
                dstP->a = 0xFF;

                num = 0;
                r = 0;
                g = 0;
                b = 0;
                ++yIdx;
            }

            Pixel *intP = (Pixel *)&PIXEL(intImage, i, j);
            r += intP->r;
            g += intP->g;
            b += intP->b;
            l += length;
            ++num;
        }

        if (yIdx < HEIGHT(dstImage)) {
            Pixel *dstP = (Pixel *)&PIXEL(dstImage, i, yIdx);
            dstP->r = r / num;
            dstP->g = g / num;
            dstP->b = b / num;
            dstP->a = 0xFF;
        }
    }

    free(intermediateBuffer);

    FINISH_TIME_TEST();

    return SUCCESS;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeCreateScaledWithFilter(JNIEnv *env, jobject obj, jobject src, int dstWidth, int dstHeight)
{
    LOGI("%s start\n", __func__);

    uint32_t *dstBuffer = (uint32_t *)malloc(dstWidth * dstHeight * sizeof(uint32_t));
    if (!dstBuffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, dstWidth * dstHeight * sizeof(uint32_t));
        return FAILED;
    }

    SNBitmap srcBitmap;
    java2Native(env, src, &srcBitmap);

    SNBitmap dstBitmap = {dstBuffer, dstWidth, dstHeight};
    if (SUCCESS != scaleWithFilter(&srcBitmap, &dstBitmap))
        return FAILED;

    set2Java(env, obj, dstBitmap.address, dstBitmap.width, dstBitmap.height);
    LOGI("%s end\n", __func__);
    return SUCCESS;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeCreateCropped(JNIEnv *env, jobject obj, jobject src, int xOffset, int yOffset, int dstWidth, int dstHeight)
{
    LOGI("%s start\n", __func__);

    uint32_t *dstBuffer = (uint32_t *)malloc(dstWidth * dstHeight * sizeof(uint32_t));
    if (!dstBuffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, dstWidth * dstHeight * sizeof(uint32_t));
        return FAILED;
    }

    SNBitmap srcBitmap;
    java2Native(env, src, &srcBitmap);

    DECLARE_IMAGE_REPRESENTATION(srcImage, srcBitmap.address, srcBitmap.width, srcBitmap.height);
    DECLARE_IMAGE_REPRESENTATION(dstImage, dstBuffer, dstWidth, dstHeight);

    int i, j;
    int y = yOffset;
    for (j = 0; j < HEIGHT(dstImage); ++j, ++y) {
        int x = xOffset;
        for (i = 0; i < WIDTH(dstImage); ++i, ++x) {
            PIXEL(dstImage, i, j) = PIXEL(srcImage, x, y);
        }
    }

    set2Java(env, obj, dstBuffer, dstWidth, dstHeight);

    LOGI("%s end\n", __func__);
    return SUCCESS;
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeAllocate(JNIEnv *env, jobject obj)
{
    LOGI("%s start\n", __func__);

    SNBitmap bitmap;
    java2Native(env, obj, &bitmap);

    uint32_t *buffer = (uint32_t *)malloc(bitmap.width * bitmap.height * sizeof(uint32_t));
    if (!buffer) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__,  bitmap.width * bitmap.height * sizeof(uint32_t));
        return FAILED;
    }

    set2Java(env, obj, buffer, bitmap.width, bitmap.height);

    LOGI("%s end\n", __func__);
    return SUCCESS;
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNBitmap_nativeRecycle(JNIEnv *env, jobject obj)
{
    jclass clazz = (*env)->GetObjectClass(env, obj);
    jfieldID id = (*env)->GetFieldID(env, clazz, "address", "I");
    uint32_t *addr = (uint32_t *)(*env)->GetIntField(env, obj, id);
    free(addr);
}

