#include <stdio.h>
#include <stdlib.h>

#include "SNJNICommon.h"
#include "SNBitmap.h"

#define LOG_TAG "libsnes2renderer"

enum {
    IDBasicAdjust,
    IDRGBCurve,
    IDAdjustmentsCount
};

static GLuint adjustments[IDAdjustmentsCount];

static GLuint gProgram;
static GLuint gvPositionHandle;
static GLuint gVertexSTAttributeHandle;
static GLuint gUniformLocation;

static int gBlockSizeX;
static int gBlockSizeY;
static int gDivideTimesX;
static int gDivideTimesY;
static int gProcessX;
static int gProcessY;
static uint32_t *gTexture = NULL;
static SNBitmap gInputImage;

static int gCount;
static GLuint *textureIDs = NULL;

typedef struct _Vertex {
    float x;
    float y;
} Vertex;

const static Vertex verticesST[] = {
    {0.0f, 0.0f},
    {1.0f, 0.0f},
    {0.0f, 1.0f},
    {1.0f, 1.0f}
};


JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNES2Renderer_initShader(JNIEnv * env, jobject obj, jbyteArray vertexShader, jbyteArray fragmentShader)
{
    LOGI("%s start\n", __func__);

    gProgram = createProgram(env, vertexShader, fragmentShader);
    if (!gProgram) {
        LOGE("Could not create program.");
        return;
    }
  
    gvPositionHandle = glGetAttribLocation(gProgram, "myVertexXYZ");
    gVertexSTAttributeHandle = glGetAttribLocation(gProgram, "myVertexST");
    adjustments[IDBasicAdjust] = glGetAttribLocation(gProgram, "myBasicAdjust");
    adjustments[IDRGBCurve] = glGetAttribLocation(gProgram, "myRGBCurve");

    LOGI("glGetAttribLocation(\"myVertexXYZ\") = %d\n", gvPositionHandle);
    LOGI("glGetAttribLocation(\"myVertexST\") = %d\n", gVertexSTAttributeHandle);
    LOGI("glGetAttribLocation(\"adjustments[IDBasicAdjust]\") = %d\n", adjustments[IDBasicAdjust]);
    LOGI("glGetAttribLocation(\"adjustments[IDRGBCurve]\") = %d\n", adjustments[IDRGBCurve]);

    gUniformLocation = glGetUniformLocation(gProgram, "myTexture");

    LOGI("%s end\n", __func__);
}

JNIEXPORT int JNICALL Java_com_kingjim_shotnote_SNES2Renderer_prepareChangeTexture(JNIEnv * env, jobject obj, jobject jbitmap)
{
    LOGI("%s start\n", __func__);

    java2Native(env, jbitmap, &gInputImage);

    glClearColor(1.0, 1.0, 1.0, 1.0);
    glViewport(0, 0, gInputImage.width, gInputImage.height);

    gBlockSizeX = MAX_TEXTURE_SIZE;
    gBlockSizeY = MAX_TEXTURE_SIZE;

    gDivideTimesX = calcBlockCount(gInputImage.width, gBlockSizeX);
    gDivideTimesY = calcBlockCount(gInputImage.height, gBlockSizeY);

    gProcessX = 0;
    gProcessY = 0;

    gTexture = (uint32_t *)malloc(MAX_TEXTURE_SIZE * MAX_TEXTURE_SIZE * sizeof(uint32_t));

    if (textureIDs) {
        glDeleteTextures(gCount, textureIDs);
        free(textureIDs);
    }

    gCount = gDivideTimesX * gDivideTimesY;
    textureIDs = (GLuint *)malloc(gCount * sizeof(GLuint));
    glGenTextures(gCount, textureIDs);

    LOGI("%s end\n", __func__);
    return gCount;
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNES2Renderer_processChangeTexture(JNIEnv * env, jobject obj)
{
    LOGI("%s start stage=%d\n", __func__, gProcessY * gDivideTimesY + gProcessX);

    DECLARE_IMAGE_REPRESENTATION(srcImage, gInputImage.address, gInputImage.width, gInputImage.height);
    DECLARE_IMAGE_REPRESENTATION(dstImage, gTexture, gBlockSizeX, gBlockSizeY);

    int xOffset = gBlockSizeX * gProcessX;
    int yOffset = gBlockSizeY * gProcessY;

    int i, j;
    for (j = 0; j < HEIGHT(dstImage); ++j) {
        int inputY = yOffset + j;
        if (HEIGHT(srcImage) <= inputY)
            break;

        for (i = 0; i < WIDTH(dstImage); ++i) {
            int inputX = xOffset + i;
            if (WIDTH(srcImage) <= inputX)
                break;

            PIXEL(dstImage, i, j) = PIXEL(srcImage, inputX, inputY);
        }
    }

    glBindTexture(GL_TEXTURE_2D, textureIDs[gProcessY * gDivideTimesY + gProcessX]);

    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, gBlockSizeX, gBlockSizeY, 0, GL_RGBA, GL_UNSIGNED_BYTE, gTexture);

    ++gProcessX;
    if (gDivideTimesX <= gProcessX) {
        gProcessX = 0;
        ++gProcessY;
    }

    LOGI("%s end\n", __func__);
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNES2Renderer_finishChangeTexture(JNIEnv * env, jobject obj)
{
    LOGI("%s start\n", __func__);
    free(gTexture);
    gTexture = NULL;
    LOGI("%s end\n", __func__);
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNES2Renderer_cancelChangeTexture(JNIEnv * env, jobject obj)
{
    LOGI("%s start\n", __func__);

    if (gTexture) {
        free(gTexture);
        gTexture = NULL;
    }

    if (textureIDs) {
        glDeleteTextures(gCount, textureIDs);
        free(textureIDs);
        textureIDs = NULL;
    }

    LOGI("%s end\n", __func__);
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNES2Renderer_draw(JNIEnv * env, jobject obj,
        float hue, float saturation, float contrast, float brightness, float rCurve, float gCurve, float bCurve, jobject jbitmap)
{
    LOGI("%s start\n", __func__);

    if (!textureIDs) {
        LOGI("Textures is not prepared.\n");
        return;
    }

    SNBitmap outputImage;
    java2Native(env, jbitmap, &outputImage);
    outputImage.width = gInputImage.width;
    outputImage.height = gInputImage.height;
    outputImage.address = (uint32_t *)malloc(outputImage.width * outputImage.height * sizeof(uint32_t));
    if (!outputImage.address) {
        LOGI("%s : cannot memory allocate -> %d byte\n", __func__, outputImage.width * outputImage.height * sizeof(uint32_t));
        return;
    }

    set2Java(env, jbitmap, outputImage.address, outputImage.width, outputImage.height);

    glClear(GL_COLOR_BUFFER_BIT);

    glUseProgram(gProgram);

    glVertexAttrib4f(adjustments[IDBasicAdjust], hue, saturation, contrast, brightness);
    glVertexAttrib3f(adjustments[IDRGBCurve], rCurve, gCurve, bCurve);

    glVertexAttribPointer(gVertexSTAttributeHandle, 2, GL_FLOAT, 0, 0, verticesST);
    glEnableVertexAttribArray(gVertexSTAttributeHandle);

    int i, j;
    for (j = 0; j < gDivideTimesY; ++j) {
        for (i = 0; i < gDivideTimesX; ++i) {
            float offsetX = (float)(i * gBlockSizeX);
            float offsetY = (float)(j * gBlockSizeY);

            RectF rect = {offsetX, offsetY, (float)gBlockSizeX, (float)gBlockSizeY};
            LOGD("texture rect. i=%d j=%d rect.x=%f rect.y=%f rect.w=%f rect.h=%f\n", i, j, rect.x, rect.y, rect.width, rect.height);

            GLfloat verticesXY[] = {
                -1.0 + (rect.x                / (float)outputImage.width) * 2.0f, -1.0 + (rect.y                 / (float)outputImage.height) * 2.0f,
                -1.0 + ((rect.x + rect.width) / (float)outputImage.width) * 2.0f, -1.0 + (rect.y                 / (float)outputImage.height) * 2.0f,
                -1.0 + (rect.x                / (float)outputImage.width) * 2.0f, -1.0 + ((rect.y + rect.height) / (float)outputImage.height) * 2.0f,
                -1.0 + ((rect.x + rect.width) / (float)outputImage.width) * 2.0f, -1.0 + ((rect.y + rect.height) / (float)outputImage.height) * 2.0f,
            };

            glBindTexture(GL_TEXTURE_2D, textureIDs[j * gDivideTimesY + i]);
            glUniform1i(gUniformLocation, 0);

            glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, GL_FALSE, 0, verticesXY);
            glEnableVertexAttribArray(gvPositionHandle);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            glDisableVertexAttribArray(gvPositionHandle);
        }
    }

    glDisableVertexAttribArray(gVertexSTAttributeHandle);

    glReadPixels(0, 0, outputImage.width, outputImage.height, GL_RGBA, GL_UNSIGNED_BYTE, outputImage.address);

    LOGI("%s end\n", __func__);
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNES2Renderer_release(JNIEnv * env, jobject obj)
{
    LOGI("%s\n", __func__);

    if (textureIDs) {
        glDeleteTextures(gCount, textureIDs);
        free(textureIDs);
        textureIDs = NULL;
    }

    if (gProgram) {
        glDeleteProgram(gProgram);
    }
}

