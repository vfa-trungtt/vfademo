#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "MarkFinderMS.h"
#include "SNBitmap.h"
#include "SNJNICommon.h"

#define LOG_TAG "libsnphototransformerhigh"


#define SUCCESS       0
#define FAILED        1
#define OUT_OF_MEMORY 2
#define PAPER_NG      3

#define SN_IMAGE_BLOCK_SIZE 2

#define SN_SIDE_LENGTH_MAX 2048
#define SNPHOTO_OUTPUT_IMAGE_CUSTOM_SCALE 1.0f

#define SNPHOTO_OUTPUT_IMAGE_WIDTH 768 * SNPHOTO_OUTPUT_IMAGE_CUSTOM_SCALE
#define SNPHOTO_OUTPUT_IMAGE_HEIGHT 1024 * SNPHOTO_OUTPUT_IMAGE_CUSTOM_SCALE

#define SNPHOTO_OUTPUT_IMAGE_WIDTH_HIGH_RESOLUTION 1200//768//1200//
#define SNPHOTO_OUTPUT_IMAGE_HEIGHT_HIGH_RESOLUTION 1600//1024//1600//

#define CHECKDATA_WIDTH  92
#define CHECKDATA_HEIGHT 21

#define CHECK_IMAGE_WIDTH 1200
#define CHECK_IMAGE_HEIGHT 1600

#define MAX(a,b) ((a) > (b) ? (a) : (b))
#define MIN(a,b) ((a) < (b) ? (a) : (b))

#define ROUND(f) ((int)(f + 0.5f))

typedef struct _PointF {
    float x;
    float y;
} PointF;

typedef struct _Rect {
    int x;
    int y;
    int width;
    int height;
} Rect;

typedef struct _GlayImage {
    unsigned char *data;
    unsigned int  width;
    unsigned int  height;
} GlayImage;

typedef struct _LogoImage {
    uint32_t *data;
    int  width;
    int  height;
} LogoImage;

typedef struct _OCRCharDefine {
    char          result;
    unsigned char value[7];
} OCRCharDefine;

static unsigned char SHOTNOTE_PAPER_CHECK_MASTER[CHECKDATA_WIDTH * CHECKDATA_HEIGHT] = {
0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,0,0,0,0,
0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,
0,0,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,
0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,
0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,1,0,1,1,0,1,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,    
};

static GLuint gProgram = 0;

static int ootuThreshold(const unsigned char *grayScale, int width, int height)
{
    LOGD("called %s\n", __func__);

    int i, j;

    int l_min = 0, l_max = 0;
    int histgram[256] = {0};

    for (i = 0; i < height; ++i) {
        for (j = 0; j < width; ++j) {
            ++histgram[grayScale[i * width + j]];
        }
    }

    for (i = 0; i < sizeof(histgram) / sizeof(histgram[0]); ++i) {
        if (0 != histgram[i]) {
            l_min = i;
            break;
        }
    }

    for (i = sizeof(histgram) / sizeof(histgram[0]) - 1; i >= 0 ; --i) {
        if (0 != histgram[i]) {
            l_max = i;
            break;
        }
    }

    int   l  = l_min - 1;
    float n1 = 0, n2 = width * height;
    float s1 = 0.0f, s2 = 0.0f;
    float t1 = 0.0f, t2 = 0.0f;
    for (i = l_min; i <= l_max; ++i) {
        s2 += i * histgram[i];
        t2 += i * i * histgram[i];
    }
    float lambda_dash = 0.0f, lambda;
    float z, z_dash, z_double_dash;
    float my1, my2, sigma_square_1, sigma_square_2;  
    float sigma_square_b, sigma_square_w;  

    while (l != l_max) {
        ++l;
        if (0 == histgram[l])
            continue;
        z             = histgram[l];
        z_dash        = histgram[l] * l;
        z_double_dash = histgram[l] * l * l;
        n1 += z;
        s1 += z_dash;
        t1 += z_double_dash;
        n2 -= z;
        s2 -= z_dash;
        t2 -= z_double_dash;
        my1 = s1 / n1;
        sigma_square_1 = t1 / n1 - pow(my1, 2);
        my2 = s2 / n2;
        sigma_square_2 = t2 / n2 - pow(my2, 2);
        sigma_square_b = n1 * n2 * pow(my1 - my2, 2);
        sigma_square_w = n1 * sigma_square_1 + n2 * sigma_square_2;
        lambda = sigma_square_b / sigma_square_w;

        if (lambda < lambda_dash)
            break;
        else
            lambda_dash = lambda;
    }

    LOGD("%s threshold=%d\n", __func__, l);

    return l;
}

static void convertGrayScale(uint32_t *pixels, int width, int height, unsigned char *outGrayScale)
{
    LOGD("called %s\n", __func__);

    int i, size;
    for (i = 0, size = width * height; i < size; ++i) {
        /*
         * 色つき付箋紙でのマーカー認識率に大きな差が出ないように、輝度算出でのRGB係数を 1:1:1 にする。
         * 本来はRGB係数を 約2:4:1 にするが、これは人間の目に合わせたもの。
         */
        uint8_t *rgba = (uint8_t *)&pixels[i];
        uint8_t y = (rgba[0] + rgba[1] + rgba[2]) / 3;
        outGrayScale[i] = y;
    }
}

static void convertBinary(const unsigned char *glayImage, int width, const height, unsigned char *outBinary)
{
    LOGD("called %s\n", __func__);

    int threshold = ootuThreshold(glayImage, width, height);
    int i, size;
    for (i = 0, size = width * height; i < size; ++i) {
        if (threshold < glayImage[i]) {
            outBinary[i] = 255;
        }
        else {
            outBinary[i] = 0;
        }
    }
}

static GlayImage *clipAndConvertGlayScale(uint32_t *pixels, int width, int height, const Rect *rect)
{
    LOGD("called %s w=%d h=%d rect=%d,%d,%d,%d\n", __func__, width, height, rect->x, rect->y, rect->width, rect->height);

    GlayImage *ret = malloc(sizeof(GlayImage));
    if (!ret)
        return NULL;

    ret->data = malloc(rect->width * rect->height);
    if (!ret->data) {
        free(ret);
        return NULL;
    }

    int i, j;
    for (i = 0; i < rect->height; ++i) {
        for (j = 0; j < rect->width; ++j) {

            int idxG = i * rect->width + j;
            int idxC = (rect->y + i) * width + (rect->x + j);

            /*
             * ここでも人の目に合わせる必要はないのでRGB係数を 1:1:1 にする。
             */
            uint8_t *rgba = (uint8_t *)&pixels[idxC];
            uint8_t y = (rgba[0] + rgba[1] + rgba[2]) / 3;
            ret->data[idxG] = y;
        }
    }


    ret->width  = rect->width;
    ret->height = rect->height;

    return ret;
}

static GlayImage *clipGlayScale(unsigned char *glayImage, int width, int height, const Rect *rect)
{
    LOGD("called %s\n", __func__);

    GlayImage *ret = malloc(sizeof(GlayImage));
    if (!ret)
        return NULL;

    ret->data = malloc(rect->width * rect->height);
    if (!ret->data) {
        free(ret);
        return NULL;
    }

    int i, j;
    for (i = 0; i < rect->height; ++i) {
        for (j = 0; j < rect->width; ++j) {

            int idxOut = i * rect->width + j;
            int idxIn = (rect->y + i) * width + (rect->x + j);

            ret->data[idxOut] = glayImage[idxIn];
        }
    }


    ret->width  = rect->width;
    ret->height = rect->height;

    return ret;
}

static GlayImage *resizeGlayImage(unsigned char *glayImage, int width, int height, int outWidth, int outHeight)
{
    GlayImage *ret = malloc(sizeof(GlayImage));
    if (!ret)
        return NULL;

    ret->data = malloc(outWidth * outHeight);
    if (!ret->data) {
        free(ret);
        return NULL;
    }

    int i, j;
    for (i = 0; i < outHeight; ++i) {
        for (j = 0; j < outWidth; ++j) {

            int idxOut = i * outWidth + j;

            int x = (int)((float)width  / (float)outWidth  * (float)j);
            int y = (int)((float)height / (float)outHeight * (float)i);
            
            int idxIn = y * width + x;

            ret->data[idxOut] = glayImage[idxIn];
        }
    }


    ret->width  = outWidth;
    ret->height = outHeight;

    return ret;
}

static void releaseGlayImage(GlayImage *image)
{
    free(image->data);
    free(image);
}

static void releaseLogoImage(LogoImage *image)
{
    free(image->data);
    free(image);
}

static void applyHighContrast(unsigned char *grayScale, int width, int height, float contrast)
{
    unsigned char convertTable[256];

    int i, j;
    int min = 255, max = 0;

    for (i = 0; i < height; ++i) {
        for (j = 0; j < width; ++j) {
            int idx = i * width + j;
            if (max < grayScale[idx])
                max = grayScale[idx];
            else if (min > grayScale[idx])
                min = grayScale[idx];
        }
    }

    int center = (max + min + min + min) / 4;
    int adjustBrightness = 0;
    if (16 > max - min) {
        adjustBrightness = 112;
        center = 127;
    }
    if (32 > max - min) {
        adjustBrightness = 96;
        center = 127;
    }
    else if (48 > max - min) {
        adjustBrightness = 80;
        center = 127;
    }
    else if (64 > max - min) {
        adjustBrightness = 64;
        center = 127;
    }

    for (i = 0; i < sizeof(convertTable) / sizeof(convertTable[0]); ++i) {
        int brightness = tan(contrast * M_PI / 180.0f) * (i - (center - adjustBrightness)) + center;
        if (255 < brightness)
            convertTable[i] = 255;
        else if (0 > brightness)
            convertTable[i] = 0;
        else
            convertTable[i] = (unsigned char)brightness;
    }

    for (i = 0; i < height; ++i) {
        for (j = 0; j < width; ++j) {
            grayScale[i * width + j] = convertTable[grayScale[i * width + j]];
        }
    }
}

static int findMarker(uint32_t *pixels, int width, int height, PointF *points)
{
    LOGI("called %s w=%d h=%d \n", __func__, width, height);

    unsigned char *grayScale = malloc(width * height);
    if (!grayScale) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, width * height);
        return OUT_OF_MEMORY;
    }

    convertGrayScale(pixels, width, height, grayScale);

    SMF_IMAGE smfImage = {
        sizeof(SMF_IMAGE),
        width,
        height,
        8,
        SMF_BI_RGB,
        grayScale
    };

    SMF_PARAM smfParam = {
        sizeof(SMF_PARAM),
        SMF_LARGEIMAGE_MODE
    };

    SMF_MARKER_INFO smfMarkerInfo = {
        sizeof(SMF_MARKER_INFO),
        {{0, 0,}, {0, 0,}, {0, 0,}, {0, 0,}},
        SMF_RIGHT_TOP
    };

    SMF_RESULT ret = FindShotNoteMarks(&smfImage, &smfParam, &smfMarkerInfo);
    LOGE("error FindShotNoteMarks() ret='%s'\n",
            SMF_SUCCESS         == ret ? "SMF_SUCCESS"         :
            SMF_ERROR_BASE      == ret ? "SMF_ERROR_BASE"      :
            SMF_ERROR_BAD_IMG   == ret ? "SMF_ERROR_BAD_IMG"   :
            SMF_ERROR_BAD_PARAM == ret ? "SMF_ERROR_BAD_PARAM" :
            SMF_FAILED          == ret ? "SMF_FAILED"          :
                                         "UNKNOWN"
            );

    points[0].x = (float)smfMarkerInfo.miPos[0].x;
    points[0].y = (float)smfMarkerInfo.miPos[0].y;
    points[1].x = (float)smfMarkerInfo.miPos[1].x;
    points[1].y = (float)smfMarkerInfo.miPos[1].y;
    points[2].x = (float)smfMarkerInfo.miPos[2].x;
    points[2].y = (float)smfMarkerInfo.miPos[2].y;
    points[3].x = (float)smfMarkerInfo.miPos[3].x;
    points[3].y = (float)smfMarkerInfo.miPos[3].y;

    free(grayScale);

    return SMF_SUCCESS == ret ? SUCCESS : FAILED;
}


static int checkLogo(GlayImage *image, float offsetY_f)
{
    int i, length = CHECKDATA_WIDTH * CHECKDATA_HEIGHT;

    int offsetY_ZERO = image->height / 2 - CHECKDATA_HEIGHT / 2;
    int offsetY_MIN = 0;
    int offsetY_MAX = image->height - CHECKDATA_HEIGHT;

    int offsetY;
    if (0.0f > offsetY_f)
        offsetY = (int)(offsetY_MIN * -offsetY_f + offsetY_ZERO * (1.0f - -offsetY_f));
    else
        offsetY = (int)(offsetY_MAX * offsetY_f + offsetY_ZERO * (1.0f - offsetY_f));

    int offset = CHECKDATA_WIDTH * offsetY;
    
    float th = 0.6f;
    float matchCount[9] = {0};

#ifdef DEBUG
    char log[200];
    strcpy(log,"");
#endif

    for (i = 0; i < length; ++i) {
#ifdef DEBUG
        if (i && i % CHECKDATA_WIDTH == 0) {
            LOGD("%s\n", log);
            strcpy(log,"");
        }
#endif
        int target = !(image->data[offset + i]);
#ifdef DEBUG
        strcat(log, target ? "1," : "0,");
#endif
        matchCount[0] += (SHOTNOTE_PAPER_CHECK_MASTER[i] == target);
        matchCount[1] += i % CHECKDATA_WIDTH > 2 ? (SHOTNOTE_PAPER_CHECK_MASTER[i-2] == target) : th;
        matchCount[2] += i % CHECKDATA_WIDTH > 4 ? (SHOTNOTE_PAPER_CHECK_MASTER[i-4] == target) : th;
        matchCount[3] += i % CHECKDATA_WIDTH > 6 ? (SHOTNOTE_PAPER_CHECK_MASTER[i-6] == target) : th;
        matchCount[4] += i % CHECKDATA_WIDTH < CHECKDATA_WIDTH - 2 ? (SHOTNOTE_PAPER_CHECK_MASTER[i+2] == target) : th;
        matchCount[5] += i % CHECKDATA_WIDTH < CHECKDATA_WIDTH - 4 ? (SHOTNOTE_PAPER_CHECK_MASTER[i+4] == target) : th;
        matchCount[6] += i % CHECKDATA_WIDTH < CHECKDATA_WIDTH - 6 ? (SHOTNOTE_PAPER_CHECK_MASTER[i+6] == target) : th;
        matchCount[7] += i > CHECKDATA_WIDTH * 2 ? (SHOTNOTE_PAPER_CHECK_MASTER[i-CHECKDATA_WIDTH * 2] == target) : th;
        matchCount[8] += i < length - CHECKDATA_WIDTH * 2 ? (SHOTNOTE_PAPER_CHECK_MASTER[i+CHECKDATA_WIDTH * 2] == target) : th;
    }
    LOGD("\n}\n");

    float maxMatchCount = 0;
    for (i = 0; i < 9; i++) {
        LOGD("matchCount[%d]:%f", i, matchCount[i]);
        if (maxMatchCount < matchCount[i])
            maxMatchCount = matchCount[i];
    }
    LOGD("checkLogo matchCount:%f / %f", maxMatchCount, (float)length * th);

    return (maxMatchCount > (length * th)) ? SUCCESS : PAPER_NG;
}

static LogoImage *imageForPaperCheck(
        uint32_t *deptBuffer, int deptWidth, int deptHeight,PointF *points)
{
    float calcTable[8];

    Rect rect = {0.1 * CHECK_IMAGE_WIDTH
            , 0.963 * CHECK_IMAGE_HEIGHT
            , 0.118 * CHECK_IMAGE_WIDTH
            , 0.0193 * CHECK_IMAGE_HEIGHT
    };

    LogoImage *ret = malloc(sizeof(LogoImage));
        ret->data = malloc(rect.width * rect.height * sizeof(uint32_t));
        ret->width = rect.width;
        ret->height = rect.height;

    float sx = (points[0].x - points[1].x) + (points[2].x - points[3].x);
    float sy = (points[0].y - points[1].y) + (points[2].y - points[3].y);

    float dx1 = points[1].x - points[2].x;
    float dx2 = points[3].x - points[2].x;
    float dy1 = points[1].y - points[2].y;
    float dy2 = points[3].y - points[2].y;

    float z = (dx1 * dy2)-(dy1 * dx2);
    float g = ((sx * dy2)-(sy * dx2)) / z;
    float h = ((sy * dx1)-(sx * dy1)) / z;

    calcTable[0] = points[1].x - points[0].x + g * points[1].x;
    calcTable[1] = points[3].x - points[0].x + h * points[3].x;
    calcTable[2] = points[0].x;
    calcTable[3] = points[1].y - points[0].y + g * points[1].y;
    calcTable[4] = points[3].y - points[0].y + h * points[3].y;
    calcTable[5] = points[0].y;
    calcTable[6] = g;
    calcTable[7] = h;

    unsigned int x, y,outX,outY;
    for (y = rect.y, outY = 0; y < (rect.y + rect.height); y++, outY++) {
        for (x = rect.x, outX = 0; x < (rect.x + rect.width); x++, outX++){
            float xRate = (float) x / CHECK_IMAGE_WIDTH;
            float yRate = (float) y / CHECK_IMAGE_HEIGHT;

            float deptX_f = (calcTable[0] * xRate + calcTable[1] * yRate + calcTable[2]) / (calcTable[6] * xRate + calcTable[7] * yRate + 1.0);
            float deptY_f = (calcTable[3] * xRate + calcTable[4] * yRate + calcTable[5]) / (calcTable[6] * xRate + calcTable[7] * yRate + 1.0);

            uint32_t deptX = (uint32_t)deptX_f;
            uint32_t deptY = (uint32_t)deptY_f;

            if (deptX >= 0 && deptX < deptWidth && deptY >= 0 && deptY < deptHeight) {
                ret->data[outY * rect.width + outX] = deptBuffer[deptY * deptWidth + deptX];
            }
        }
    }
    return ret;
}

static int paperCheck(uint32_t *pixels, int width, int height)
{
    LOGI("called %s w=%d h=%d \n", __func__, width, height);

    Rect rect = {
        0,
        0,
        width,
        height
    };

    GlayImage *image = clipAndConvertGlayScale(pixels, width, height, &rect);
    convertBinary(image->data, image->width, image->height, image->data);
    GlayImage *resized = resizeGlayImage(image->data, image->width, image->height, CHECKDATA_WIDTH, CHECKDATA_HEIGHT * 2);
    releaseGlayImage(image);
    int i, ret;
    ret = checkLogo(resized,0);
    releaseGlayImage(resized);

    return ret;
}

static char detectResult(unsigned char *segments)
{
    OCRCharDefine ocrTable[] = {
        {'0', {1,1,1,0,1,1,1}},
        {'1', {0,0,1,0,0,1,0}},
        {'2', {1,0,1,1,1,0,1}},
        {'3', {1,0,1,1,0,1,1}},
        {'4', {0,1,1,1,0,1,0}},
        {'5', {1,1,0,1,0,1,1}},
        {'6', {1,1,0,1,1,1,1}},
        {'7', {1,0,1,0,0,1,0}},
        {'8', {1,1,1,1,1,1,1}},
        {'9', {1,1,1,1,0,1,1}},
        {'1', {0,1,0,0,1,0,0}},
        {'6', {0,1,0,1,1,1,1}},
        {'7', {1,1,1,0,0,1,0}},
        {'9', {1,1,1,1,0,1,0}}
    };

    int i, j;
    char ret = '#';

    for (i = sizeof(ocrTable) / sizeof(ocrTable[0]) - 1; i >= 0; --i) {
        int matchCount = 0;
        for (j = 0; j < 7; ++j) {
            if (ocrTable[i].value[j] == segments[j]) {
                ++matchCount;
            }
        }

        if (5 < matchCount) {
            ret = ocrTable[i].result;
        }

        if (7 == matchCount) {
            break;
        }
    }

    return ret;
}

static int isAllBlack(unsigned char *grayScale, int width, int height)
{
    int i, j;
    for (j = 0; j < width; ++j) {
        for (i = 0; i < height; ++i) {
            if (0x00 != grayScale[i * width + j])
                return 0;
        }
    }

    return 1;
}

static void detectVerticalRange(unsigned char *grayScale, int width,  int height, int left, int right, int *top, int *bottom)
{
    int i, j;

    int topTest = height - 1;
    int bottomTest = 0;

    *top = height - 1;
    *bottom = 0;

    for (i = 0; i < height; ++i) {
        int blackFound = 0;
        for (j = left; j <= right; ++j) {
            if (0x00 == grayScale[i * width + j]) {
                int nextDoor = 0;

                if (i != 0 && 0x00 == grayScale[(i - 1) * width + j])
                    ++nextDoor;
                if (i != height - 1 && 0x00 == grayScale[(i + 1) * width + j])
                    ++nextDoor;
                if (j != 0 && 0x00 == grayScale[i * width + (j - 1)])
                    ++nextDoor;
                if (i != width - 1 && 0x00 == grayScale[i * width + (j + 1)])
                    ++nextDoor;

                if (2 > nextDoor)
                    continue;

                blackFound = 1;
            }
        }

        if (blackFound) {
            if (topTest > i)
                topTest = i;
            if (bottomTest < i)
                bottomTest = i;
        }
        else {
            topTest = height;
            bottomTest = 0;
        }

        if ((*bottom - *top) < (bottomTest - topTest)) {
            *top = topTest;
            *bottom = bottomTest;
        }
    }
}

static int reviseHorizonalRange(unsigned char *grayScale, int width, int height, int top, int bottom, int *left, int *right)
{
    LOGD("called %s\n", __func__);

    int leftArray[10] = {0};
    int rightArray[10] = {0};

    int i, j, k = 0;

    int leftTest = width - 1;
    int rightTest = 0;

    int blackDetected = 0;

    for (j = 0; j < width; ++j) {
        int blackFound = 0;
        for (i = top; i < bottom; ++i) {
            if (0x00 == grayScale[i * width + j]) {
                int nextDoor = 0;

                if (i != 0 && 0x00 == grayScale[(i - 1) * width + j])
                    ++nextDoor;
                if (i != height - 1 && 0x00 == grayScale[(i + 1) * width + j])
                    ++nextDoor;
                if (j != 0 && 0x00 == grayScale[i * width + (j - 1)])
                    ++nextDoor;
                if (i != width - 1 && 0x00 == grayScale[i * width + (j + 1)])
                    ++nextDoor;

                if (2 > nextDoor)
                    continue;

                blackFound = 1;
                blackDetected = 1;
            }
        }

        if (blackFound) {
            if (leftTest > j)
                leftTest = j;
            if (rightTest < j)
                rightTest = j;
        }

        if (blackDetected && !blackFound) {
            leftArray[k] = leftTest;
            rightArray[k] = rightTest;

            leftTest = width - 1;
            rightTest = 0;

            blackDetected = 0;
        
            ++k;
            if (10 <= k)
                break;
        }
    }

    for (i = 0; i < (k - 1); ++i) {
        int range = rightArray[i+1] - leftArray[i];
        if (width / 2 < range && range < width / 5 * 4) {
            *left = leftArray[i];
            *right = rightArray[i+1];
            return SUCCESS;
        }
    }

    if (width / 2 < *left)
        *left -= width / 2;
    if (width / 2 > *right)
        *right += width / 2;

    if ((*right - *left) < (width / 2))
        return FAILED;

    return SUCCESS;
}

static int detectArea(unsigned char *grayScale, int width, int height, int *left, int *right, int *top, int *bottom)
{
    LOGD("called %s\n", __func__);

    int i, j;

    int leftTest = width - 1;
    int rightTest = 0;

    *left  = width - 1;
    *right = 0;

    *top = height - 1;
    *bottom = 0;

    for (j = 0; j < width; ++j) {
        int blackFound = 0;
        for (i = 0; i < height; ++i) {
            if (0x00 == grayScale[i * width + j]) {
                int nextDoor = 0;

                if (i != 0 && 0x00 == grayScale[(i - 1) * width + j])
                    ++nextDoor;
                if (i != height - 1 && 0x00 == grayScale[(i + 1) * width + j])
                    ++nextDoor;
                if (j != 0 && 0x00 == grayScale[i * width + (j - 1)])
                    ++nextDoor;
                if (i != width - 1 && 0x00 == grayScale[i * width + (j + 1)])
                    ++nextDoor;

                if (2 > nextDoor)
                    continue;

                blackFound = 1;
            }
        }

        if (blackFound) {
            if (leftTest > j)
                leftTest = j;
            if (rightTest < j)
                rightTest = j;
        }
        else {
            leftTest = width - 1;
            rightTest = 0;
        }

        if ((*right - *left) < (rightTest - leftTest)) {
            int topTest, bottomTest;
            detectVerticalRange(grayScale, width, height, leftTest, rightTest, &topTest, &bottomTest);
            if ((height / 2) <= bottomTest - topTest) {
                *left = leftTest;
                *right = rightTest;
                *top = topTest;
                *bottom = bottomTest;
            }
        }
    }

    int ret;

    if ((*bottom - *top) < (height / 2)) {
        ret = FAILED;
    }
    else if ((*right - *left) < (width / 2)) {
        ret = reviseHorizonalRange(grayScale, width, height, *top, *bottom, left, right);
    }
    else {
        ret = SUCCESS;
    }

    LOGD("end %s ret=%s\n", __func__, SUCCESS == ret ? "SUCCESS" : "FAILED");
    return ret;
}

#ifdef DEBUG
static char ocrDigit(unsigned char *grayScale, int width, int height, uint32_t *pixels)
#else
static char ocrDigit(unsigned char *grayScale, int width, int height)
#endif
{
    LOGD("called %s w=%d h=%d \n", __func__, width, height);

    int i, j, k;

    int top;
    int bottom;
    int left;
    int right;

    applyHighContrast(grayScale, width, height, 60.0f);
    convertBinary(grayScale, width, height, grayScale);

#ifdef DEBUG
    {
        int l, m;
        for (l = 0; l < height; ++l) {
            for (m = 0; m < width; ++m) {
                uint8_t *rgba = (uint8_t *)&pixels[l * width + m];
                rgba[0] = grayScale[l * width + m];
                rgba[1] = grayScale[l * width + m];
                rgba[2] = grayScale[l * width + m];
                rgba[3] = 0xFF;
            }
        }
    }
#endif

    LOGD("#### Trimed area #### - width=%d height=%d top=%d bottom=%d\n", width, height);

    if (isAllBlack(grayScale, width, height))
        return '#';
    if (SUCCESS != detectArea(grayScale, width, height, &left, &right, &top, &bottom))
        return '#';

    LOGD("#### Digit area #### - left=%d right=%d top=%d bottom=%d - (right-left)=%d (bottom-top)=%d\n", left, right, top, bottom, right - left, bottom - top);

    Rect rect = {left, top, right - left + 1, bottom - top + 1};
    GlayImage *image = clipGlayScale(grayScale, width, height, &rect);

    float blockWidth  = (float)image->width;
    float blockHeight = (float)image->height / 2.0f;

    Rect rectTable[] = {
        {ROUND(blockWidth * 0.5) - 2, 0,                            4,                       ROUND(blockHeight * 0.3)},
        {0,                           ROUND(blockHeight * 0.5) - 2, ROUND(blockWidth * 0.3), 4                       },
        {ROUND(blockWidth * 0.7),     ROUND(blockHeight * 0.5) - 2, ROUND(blockWidth * 0.3), 4                       },
        {ROUND(blockWidth * 0.5) - 2, ROUND(blockHeight * 0.85),    4,                       ROUND(blockHeight * 0.3)},
        {0,                           ROUND(blockHeight * 1.5) - 2, ROUND(blockWidth * 0.3), 4                       },
        {ROUND(blockWidth * 0.7),     ROUND(blockHeight * 1.5) - 2, ROUND(blockWidth * 0.3), 4                       },
        {ROUND(blockWidth * 0.5) - 2, ROUND(blockHeight * 1.7),     4,                       ROUND(blockHeight * 0.3)},
    };

    unsigned char result[7] = {0};

    for (k = 0; k < 7; ++k) {
        int x_max = rectTable[k].x + rectTable[k].width;
        int y_max = rectTable[k].y + rectTable[k].height;
        int blackCount = 0;
        for (i = rectTable[k].y; i < y_max; ++i) {
            for (j = rectTable[k].x; j < x_max; ++j) {
                if (0x00 == image->data[i * image->width + j]) {
                    ++blackCount;
                }
            }
        }

        if (4 <= blackCount) {
            result[k] = 1;
        }
#ifdef DEBUG
        {
            int l, m;
            for (l = rectTable[k].y; l < rectTable[k].y + rectTable[k].height; ++l) {
                for (m = rectTable[k].x; m < rectTable[k].x + rectTable[k].width; ++m) {
                    uint8_t *rgba = (uint8_t *)&pixels[(l + top) * width + m + left];
                    rgba[0] = 0xFF;
                    rgba[1] = 0x20;
                    rgba[2] = 0x20;
                    rgba[3] = 0xFF;
                }
            }
        }
#endif
    }

    releaseGlayImage(image);

    LOGD("ocrDidit End");
    return detectResult(result);
}

static int ocrNumber(uint32_t *pixels, int width, int height, char *outStr)
{
    LOGI("called %s w=%d h=%d \n", __func__, width, height);

    int i;

    for (i = 0; i < 4; ++i) {
        Rect rect = {
            (int)((0.4000 + i * 0.0407) * width),
            (int)(0.0040 * height),
            (int)(0.0460 * width),
            (int)(0.0660 * height)
        };

        GlayImage *image = clipAndConvertGlayScale(pixels, width, height, &rect);
#ifdef DEBUG
        uint32_t *pixels_d = malloc(image->width * image->height * sizeof(uint32_t));
        char c = ocrDigit(image->data, image->width, image->height, pixels_d);
#else
        char c = ocrDigit(image->data, image->width, image->height);
#endif
        if ('#' == c) {
            c = '0';
        }
        outStr[i] = c;

#ifdef DEBUG
        {
            int j, k;
            for (k = 0; k < image->height; ++k) {
                for (j = 0; j < image->width; ++j) {
                    pixels[(200 + k) * width + ((i+1)*60 + j)] = pixels_d[k * image->width + j];
                }
            }
        }
        free(pixels_d);
#endif
        releaseGlayImage(image);
    }

    outStr[4] = '\0';

    return SUCCESS;
}

static int ocrDate(uint32_t *pixels, int width, int height, char *outStr)
{
    LOGI("called %s w=%d h=%d \n", __func__, width, height);

    int i, idx;

    float startX = 0.6525;

    outStr[2]  = '/';
    outStr[5]  = '/';
    outStr[8] = '\0';

    for (i = 0, idx = 0; i < 6; ++i) {
        if (0 < i && i % 2 == 0) {
            startX += 0.0069;
            ++idx;
        }

        Rect rect = {
            (int)(startX * width),
            (int)(0.0040 * height),
            (int)(0.0460 * width),
            (int)(0.0660 * height)
        };

        startX += 0.0407;

        GlayImage *image = clipAndConvertGlayScale(pixels, width, height, &rect);
#ifdef DEBUG
        uint32_t *pixels_d = malloc(image->width * image->height * sizeof(uint32_t));
        char c = ocrDigit(image->data, image->width, image->height, pixels_d);
#else
        char c = ocrDigit(image->data, image->width, image->height);
#endif
        if ('#' == c)
            c = '0';

        outStr[idx++] = c;

#ifdef DEBUG
        {
            int j, k;
            for (k = 0; k < image->height; ++k) {
                for (j = 0; j < image->width; ++j) {
                    pixels[(300 + k) * width + ((i+1)*60 + j)] = pixels_d[k * image->width + j];
                }
            }
        }
        free(pixels_d);
#endif
        releaseGlayImage(image);
    }

    return SUCCESS;
}

static void intersectPoint(PointF *points, float *x, float *y)
{
    float a1 = (points[2].y - points[0].y) / (points[2].x - points[0].x);
    float a2 = (points[3].y - points[1].y) / (points[3].x - points[1].x);
    float b1 = -a1 * points[0].x + points[0].y;
    float b2 = -a2 * points[1].x + points[1].y;

    *x = (b2 - b1) / (a1 - a2);
    *y = a1 * (*x) + b1;
}

static float aspectRatioFrom(PointF *positions)
{
    float topWidth =  positions[1].x - positions[0].x;
    float bottomWidth = positions[2].x - positions[3].x;

    float bigWidth = MAX(topWidth, bottomWidth);
    float smallWidth = MIN(topWidth, bottomWidth);

    float wy =bigWidth - smallWidth;
    float threshold = (wy)/bigWidth * 4;

    float rightHeight = positions[2].y - positions[1].y;
    float leftHeight = positions[3].y - positions[0].y;

    float smallHeight = MIN(rightHeight ,leftHeight);

    float height = (threshold > 1) ? (smallHeight + wy)*threshold : smallHeight + wy;
    LOGI("called %s topWidth=%f bottomWidth=%f leftHeight=%f rightHeight=%f aspectRatio=%f \n", __func__, topWidth, topWidth, leftHeight, rightHeight,height / bigWidth);

    return height / bigWidth;
}

static void adjustMarker(PointF *points,float aspectRatio)
{
    int i;
    float centerX, centerY;
    intersectPoint(points, &centerX, &centerY);
    float ratioY = 1.067, ratioX = 1.090;
    float baseRatio = (float)4/3;

    if(aspectRatio != baseRatio) {
        if(aspectRatio < baseRatio) {
            ratioX = 1+ 0.090 / baseRatio * aspectRatio;
        }
        else {
            ratioY = 1+ 0.067 * baseRatio / aspectRatio;
        }
    }

    for(i = 0; i < 4; i++){
        points[i].x = (points[i].x - centerX) * ratioX + centerX;
        points[i].y = (points[i].y - centerY) * ratioY + centerY;
    }

}

static GLuint createTexture(uint32_t *inputImage, int inputImageWidth, int inputImageHeight, int blockSizeX, int blockSizeY, int row, int column, uint32_t *texture)
{
    DECLARE_IMAGE_REPRESENTATION(srcImage, inputImage, inputImageWidth, inputImageHeight);
    DECLARE_IMAGE_REPRESENTATION(dstImage, texture, blockSizeX, blockSizeY);

    int xOffset = blockSizeX * column;
    int yOffset = blockSizeY * row;

    int i, j;
    for (j = 0; j < HEIGHT(dstImage); ++j) {
        int inputY = yOffset + j;
        if (HEIGHT(srcImage) <= inputY)
            break;

        /*
         * 上下反転
         */
        int outputY = HEIGHT(dstImage) - 1 - j;

        for (i = 0; i < WIDTH(dstImage); ++i) {
            int inputX = xOffset + i;
            if (WIDTH(srcImage) <= inputX)
                break;

            PIXEL(dstImage, i, outputY) = PIXEL(srcImage, inputX, inputY);
        }
    }

    GLuint texNum;
    glGenTextures(1, &texNum);
    glBindTexture(GL_TEXTURE_2D, texNum);

    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, blockSizeX, blockSizeY, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);

    return texNum;
}

static void transformCoefficient(float calcTable[8], PointF *pointsArg, RectF rect)
{
    PointF points[4];

    int i;
    for (i = 0; i < 4; i++) {
        points[i].x = (pointsArg[i].x - rect.x) / rect.width;
        points[i].y = (pointsArg[i].y - rect.y) / rect.height;
    }

    float sx = (points[0].x - points[1].x) + (points[2].x - points[3].x);
    float sy = (points[0].y - points[1].y) + (points[2].y - points[3].y);

    float dx1 = points[1].x - points[2].x;
    float dx2 = points[3].x - points[2].x;
    float dy1 = points[1].y - points[2].y;
    float dy2 = points[3].y - points[2].y;

    float z = (dx1 * dy2)-(dy1 * dx2);
    float g = ((sx * dy2)-(sy * dx2)) / z;
    float h = ((sy * dx1)-(sx * dy1)) / z;

    calcTable[0] = points[1].x - points[0].x + g * points[1].x;
    calcTable[1] = points[3].x - points[0].x + h * points[3].x;
    calcTable[2] = points[0].x;
    calcTable[3] = points[1].y - points[0].y + g * points[1].y;
    calcTable[4] = points[3].y - points[0].y + h * points[3].y;
    calcTable[5] = points[0].y;
    calcTable[6] = g;
    calcTable[7] = h;

#ifdef DEBUG
    for (i = 0; i < 8; i++) {
        LOGI("calcTable[%d] : %.4f\n", i, calcTable[i]);
    }
#endif
}

static void reverseTransformCoefficient(float reverseTable[9], PointF *points)
{
    float sx = (points[0].x - points[1].x) + (points[2].x - points[3].x);
    float sy = (points[0].y - points[1].y) + (points[2].y - points[3].y);

    float dx1 = points[1].x - points[2].x;
    float dx2 = points[3].x - points[2].x;
    float dy1 = points[1].y - points[2].y;
    float dy2 = points[3].y - points[2].y;

    float z = (dx1 * dy2)-(dy1 * dx2);
    float g = ((sx * dy2)-(sy * dx2)) / z;
    float h = ((sy * dx1)-(sx * dy1)) / z;

    float calcTable[8];
    calcTable[0] = points[1].x - points[0].x + g * points[1].x;
    calcTable[1] = points[3].x - points[0].x + h * points[3].x;
    calcTable[2] = points[0].x;
    calcTable[3] = points[1].y - points[0].y + g * points[1].y;
    calcTable[4] = points[3].y - points[0].y + h * points[3].y;
    calcTable[5] = points[0].y;
    calcTable[6] = g;
    calcTable[7] = h;

    reverseTable[0] = calcTable[3] * calcTable[7] - calcTable[4] * calcTable[6];
    reverseTable[1] = calcTable[1] * calcTable[6] - calcTable[0] * calcTable[7];
    reverseTable[2] = calcTable[0] * calcTable[4] - calcTable[1] * calcTable[3];

    reverseTable[3] = calcTable[4] - calcTable[5] * calcTable[7];
    reverseTable[4] = calcTable[2] * calcTable[7] - calcTable[1];
    reverseTable[5] = calcTable[1] * calcTable[5] - calcTable[2] * calcTable[4];

    reverseTable[6] = calcTable[3] - calcTable[5] * calcTable[6];
    reverseTable[7] = calcTable[2] * calcTable[6] - calcTable[0];
    reverseTable[8] = calcTable[0] * calcTable[5] - calcTable[2] * calcTable[3];

#ifdef DEBUG
    int i;
    for (i = 0; i < 9; i++) {
        LOGI("reverseTable[%d] : %.4f\n", i, reverseTable[i]);
    }
#endif
}

static int fixLength(float val) {
    int base = (int)val;
    int fraction = base % SN_IMAGE_BLOCK_SIZE;
    return (fraction < (SN_IMAGE_BLOCK_SIZE/2) )? base - fraction : base + (SN_IMAGE_BLOCK_SIZE - fraction);
}

static void sizeWithAspectRatio(float aspectRatio,SNBitmap *bmp){
    bmp->height = fixLength(bmp->width * aspectRatio);
    LOGD("変更前outBmp.height=%d",bmp->height);
    float longer = (bmp->width > bmp->height) ? bmp->width : bmp->height;
    if( longer > SN_SIDE_LENGTH_MAX ){
        if( aspectRatio > 1){
            bmp->width = fixLength(SN_SIDE_LENGTH_MAX / aspectRatio);
            bmp->height = SN_SIDE_LENGTH_MAX;
        } else {
            bmp->width = SN_SIDE_LENGTH_MAX;
            bmp->height =fixLength(SN_SIDE_LENGTH_MAX * aspectRatio);
        }
    }

    LOGD("outBmp.height=%d outBmp.width=%d",bmp->height,bmp->width);

    return;
}


static void projectiveTransform(
        uint32_t *deptBuffer, int deptWidth, int deptHeight,
        uint32_t *destBuffer, int destWidth, int destHeight,
        PointF *points)
{
    int blockSizeX = MAX_TEXTURE_SIZE;
    int blockSizeY = MAX_TEXTURE_SIZE;

    int divideTimesX = calcBlockCount(deptWidth, blockSizeX);
    int divideTimesY = calcBlockCount(deptHeight, blockSizeY);

    uint32_t *texture = (uint32_t *)malloc(MAX_TEXTURE_SIZE * MAX_TEXTURE_SIZE * sizeof(uint32_t));

    int count = (int)divideTimesX * (int)divideTimesY;
    GLuint textureIDs[count];

    int i, j;
    for (j = 0 ; j < divideTimesY; ++j) {
        for (i = 0 ; i < divideTimesX; ++i) {
            textureIDs[j * divideTimesY + i] = createTexture(deptBuffer, deptWidth, deptHeight, blockSizeX, blockSizeY, j, i, texture);
        }
    }

    free(texture);

    GLuint vertexAttributeLocationVertexXYZ = glGetAttribLocation(gProgram, "myVertexXYZ");
    GLuint tramsformCoefficientLocation = glGetUniformLocation(gProgram, "calcTable");
    GLuint reverseTramsformCoefficientLocation = glGetUniformLocation(gProgram, "reverseTable");

    LOGI("vertexAttributeLocationVertexXYZ=%d", vertexAttributeLocationVertexXYZ);
    LOGI("tramsformCoefficientLocation=%d", tramsformCoefficientLocation);
    LOGI("reverseTramsformCoefficientLocation=%d", reverseTramsformCoefficientLocation);
  
    float reverseTable[9];
    reverseTransformCoefficient(reverseTable, points);
  
    glViewport(0, 0, destWidth, destHeight);
  
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
  
    glUseProgram(gProgram);
  
    for (j = 0; j < divideTimesY; ++j) {
        for (i = 0; i < divideTimesX; ++i) {
            float offsetX = (float)(i * blockSizeX);
            float offsetY = (float)(j * blockSizeY);

            RectF rect = {offsetX, offsetY, (float)blockSizeX, (float)blockSizeY};
            float calcTable[8];
            transformCoefficient(calcTable, points, rect);

            glUniform1fv(reverseTramsformCoefficientLocation, 9, reverseTable);
            glUniform1fv(tramsformCoefficientLocation, 8, calcTable);

            GLfloat verticesXYZ[] = {
                rect.x,              rect.y,               0.0f, 1.0,
                rect.x + rect.width, rect.y,               0.0f, 1.0,
                rect.x,              rect.y + rect.height, 0.0f, 1.0,
                rect.x + rect.width, rect.y + rect.height, 0.0f, 1.0
            };

            glVertexAttribPointer(vertexAttributeLocationVertexXYZ, 4, GL_FLOAT, 0, 0, verticesXYZ);
            glEnableVertexAttribArray(vertexAttributeLocationVertexXYZ);

            GLuint texture = textureIDs[j * divideTimesY + i];
            glBindTexture(GL_TEXTURE_2D, texture);
            GLuint location = glGetUniformLocation(gProgram, "myTexture");
            glUniform1i(location, 0);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            glDisableVertexAttribArray(vertexAttributeLocationVertexXYZ);
        }
    }

    glDeleteTextures(count, textureIDs);

    /*
     * 上下反転
     */
    for (i = 0; i < destHeight; ++i) {
        glReadPixels(0, i, destWidth, 1, GL_RGBA, GL_UNSIGNED_BYTE, (uint32_t *)&destBuffer[(destHeight - 1 - i) * destWidth]);
    }
  
    return;
}

static void scaleMin(uint32_t *input, int inputWidth, int inputHeight, uint32_t *output, int outputWidth, int outputHeight)
{
    SNBitmap srcBitmap = {input, inputWidth, inputHeight};
    SNBitmap dstBitmap = {output, outputWidth, outputHeight};
    scaleWithFilter(&srcBitmap, &dstBitmap);
}

JNIEXPORT jint JNICALL Java_com_kingjim_shotnote_SNPhotoTransformer_transform(
        JNIEnv     *env,
        jobject    obj,
        jobject    inBitmap,
        jobject    outBitmap,
        int        outputWidth,
        jboolean   isOCR,
        jbyteArray vertexShader,
        jbyteArray fragmentShader)
{
    char str[strlen("--/--/--") + 1];

    SNBitmap inBmp;
    SNBitmap outBmp;

    java2Native(env, inBitmap, &inBmp);
    java2Native(env, outBitmap, &outBmp);
    outBmp.width = outputWidth;

    LOGI("called %s inputWidth=%d inputHeight=%d outputWidth=%d \n", __func__, inBmp.width, inBmp.height, outBmp.width);

    float aspectRatio;
    PointF positions[4];
    int ret = findMarker(inBmp.address, inBmp.width, inBmp.height, positions);
    if (SUCCESS != ret) {
        goto SCALE_MIN;
    }

    gProgram = createProgram(env, vertexShader, fragmentShader);
    if (!gProgram) {
        LOGE("Could not create program.");
        goto SCALE_MIN;
    }

    LOGD("[0].x=%f [0].y=%f \n", positions[0].x, positions[0].y);
    LOGD("[1].x=%f [1].y=%f \n", positions[1].x, positions[1].y);
    LOGD("[2].x=%f [2].y=%f \n", positions[2].x, positions[2].y);
    LOGD("[3].x=%f [3].y=%f \n", positions[3].x, positions[3].y);

    adjustMarker(positions,(float)4/3);
    LogoImage *logoImage = imageForPaperCheck(inBmp.address, inBmp.width, inBmp.height,positions);
    ret = paperCheck(logoImage->data, logoImage->width, logoImage->height);
    releaseLogoImage(logoImage);
    if (SUCCESS != ret) {
        findMarker(inBmp.address, inBmp.width, inBmp.height, positions);
        aspectRatio = aspectRatioFrom(positions);
        adjustMarker(positions,aspectRatio);
        isOCR = FALSE;
    }
    else{
        aspectRatio = (float)4/3;
    }
    sizeWithAspectRatio(aspectRatio,&outBmp);
    outBmp.address = (uint32_t *)malloc(outBmp.width * outBmp.height * sizeof(uint32_t));
    set2Java(env,outBitmap,outBmp.address,outBmp.width,outBmp.height);
    projectiveTransform(inBmp.address, inBmp.width, inBmp.height, outBmp.address, outBmp.width, outBmp.height, positions);
    glDeleteProgram(gProgram);
    if (!isOCR)
        return SUCCESS;

    jstring jstrBuf;
    jfieldID id;
    jclass clazz = (*env)->GetObjectClass(env, obj);

    ret = ocrNumber(outBmp.address, outBmp.width, outBmp.height, str);
    if (SUCCESS == ret) {
        jstrBuf = (*env)->NewStringUTF(env, str);
        id      = (*env)->GetFieldID(env, clazz, "mOCRNumber", "Ljava/lang/String;");
        (*env)->SetObjectField(env, obj, id, jstrBuf);
        (*env)->DeleteLocalRef(env, jstrBuf);
    }

    ret = ocrDate(outBmp.address, outBmp.width, outBmp.height, str);
    if (SUCCESS == ret) {
        jstrBuf = (*env)->NewStringUTF(env, str);
        id      = (*env)->GetFieldID(env, clazz, "mOCRDate", "Ljava/lang/String;");
        (*env)->SetObjectField(env, obj, id, jstrBuf);
        (*env)->DeleteLocalRef(env, jstrBuf);
    }
    return SUCCESS;

SCALE_MIN:
    sizeWithAspectRatio((float)4/3 ,&outBmp);
    outBmp.address = (uint32_t *)malloc(outBmp.width * outBmp.height * sizeof(uint32_t));
    set2Java(env,outBitmap,outBmp.address,outBmp.width,outBmp.height);
    scaleMin(inBmp.address, inBmp.width, inBmp.height, outBmp.address, outBmp.width, outBmp.height);
    return FAILED;
}

