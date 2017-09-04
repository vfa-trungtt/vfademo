#ifndef _SN_GL_COMMON_H_
#define _SN_GL_COMMON_H_

#include <jni.h>
#include <android/log.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <time.h>

#define  LOGI(...)  __LogEanable__ ? __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__) : 0
#define  LOGE(...)  __LogEanable__ ? __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) : 0

#ifdef DEBUG
#define LOGD LOGI
#else
#define LOGD 1?0:
#endif

#define TRUE 1
#define FALSE 0

#define MAX_TEXTURE_SIZE 1024

#define FIXED_QUANTIZATION_BIT_RATE 10
#define FLOAT2FIXED(f) ((int)((f) * (float)(1 << FIXED_QUANTIZATION_BIT_RATE)))
#define FIXED_HALF (1 << (FIXED_QUANTIZATION_BIT_RATE - 1))
#define FIXED_ZERO 0
#define FIXED2INT(fix) ((fix) >> FIXED_QUANTIZATION_BIT_RATE)
#define FIXED2FLOAT(fix) ((fix) / (float)(1 <<FIXED_QUANTIZATION_BIT_RATE))

#define DECLARE_IMAGE_REPRESENTATION(name, address, width, height) \
    typedef struct _##name##Image { \
        uint32_t pixels[height][width]; \
    } name##Image; \
    name##Image *name = (name##Image *)address; \
    int __##name##_width__ = width; \
    int __##name##_height__ = height;
#define PIXEL(image, x, y) (image->pixels[y][x])
#define WIDTH(image) (__##image##_width__)
#define HEIGHT(image) (__##image##_height__)

#define START_TIME_TEST(tag) \
    clock_t __time__ = clock(); \
    const char *__tag__ = tag
#define FINISH_TIME_TEST() \
    LOGI("%s taked %dmsec\n", __tag__, ((clock() - __time__) / (CLOCKS_PER_SEC / 1000)))

typedef int fixed;

typedef struct _RectF {
    float x;
    float y;
    float width;
    float height;
} RectF;

typedef struct _Pixel {
    unsigned char r;
    unsigned char g;
    unsigned char b;
    unsigned char a;
} Pixel;

extern void printGLString(const char *name, GLenum s);
extern void checkGlError(const char *op);

extern GLuint createProgram(JNIEnv *env, jbyteArray vertexSource, jbyteArray fragmentSource);

extern int calcBlockCount(int imageSize, int blockSize);

extern int __LogEanable__;

#ifdef ALLOCATION_CHECK

extern void *__malloc__(int size, const char *func, int line);
extern void *__calloc__(int num, int size, const char *func, int line);
extern void __free__(void *p, const char *func, int line);

#define malloc(size) __malloc__((size), __func__, __LINE__)
#define calloc(num, size) __calloc__((num), (size), __func__, __LINE__)
#define free(p) __free__((p), __func__, __LINE__)

#endif

#endif /* _SN_GL_COMMON_H_ */
