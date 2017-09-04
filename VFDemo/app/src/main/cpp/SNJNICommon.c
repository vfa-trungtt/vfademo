#include "SNJNICommon.h"

#ifdef ALLOCATION_CHECK

#undef malloc
#undef calloc
#undef free

#include <semaphore.h>

typedef struct _AllocInfo AllocInfo;
struct _AllocInfo {
    int seq;
    void *address;
    const char *func;
    const char *method;
    int size;
    int line;
    AllocInfo *next;
    AllocInfo *prev;
};

static AllocInfo allocInfoPool[128] = {0};

static AllocInfo *firstNode = NULL;
static AllocInfo *lastNode = NULL;
static AllocInfo *freeList = NULL;

static int seq = 0;
static int isInitialized = 0;

static sem_t semaphore;

#endif

#include <stdlib.h>

#define LOG_TAG   "libsnglcommon"

int __LogEanable__;

extern void printGLString(const char *name, GLenum s)
{
    const char *v = (const char *) glGetString(s);
    LOGI("GL %s = %s\n", name, v);
}

extern void checkGlError(const char* op)
{
    GLint error;
    for (error = glGetError(); error; error = glGetError()) {
        LOGI("after %s() glError (0x%x)\n", op, error);
    }
}

static GLuint loadShader(JNIEnv *env, GLenum shaderType, jbyteArray array)
{

    LOGI("%s\n", __func__);

    GLuint shader = glCreateShader(shaderType);
    if (!shader) {
        LOGI("Could not create shader.\n");
        return 0;
    }

    char *source   = (*env)->GetByteArrayElements(env, array, NULL);
    GLsizei length = (*env)->GetArrayLength(env, array);

    glShaderSource(shader, 1, (const char **)&source, &length);

    (*env)->ReleaseByteArrayElements(env, array, source, 0);

    glCompileShader(shader);

    GLint compiled = 0;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
    if (!compiled) {
        GLint infoLen = 0;
        glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
        if (infoLen) {
            char* buf = (char*) malloc(infoLen);
            if (buf) {
                glGetShaderInfoLog(shader, infoLen, NULL, buf);
                LOGE("Could not compile shader %d:\n%s\n",
                        shaderType, buf);
                free(buf);
            }
            glDeleteShader(shader);
            shader = 0;
        }
    }
    
    return shader;
}

extern GLuint createProgram(JNIEnv *env, jbyteArray vertexSource, jbyteArray fragmentSource)
{
    LOGI("%s\n", __func__);

    GLuint vertexShader = loadShader(env, GL_VERTEX_SHADER, vertexSource);
    if (!vertexShader)
        return 0;

    GLuint pixelShader = loadShader(env, GL_FRAGMENT_SHADER, fragmentSource);
    if (!pixelShader)
        return 0;

    GLuint program = glCreateProgram();
    if (!program)
        return 0;
    
    glAttachShader(program, vertexShader);
    checkGlError("glAttachShader");
    glAttachShader(program, pixelShader);
    checkGlError("glAttachShader");

    glDeleteShader(vertexShader);
    glDeleteShader(pixelShader);

    GLint linkStatus = GL_FALSE;

    glLinkProgram(program);
    glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
    if (linkStatus != GL_TRUE) {
        GLint bufLength = 0;
        glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
        if (bufLength) {
            char* buf = (char*) malloc(bufLength);
            if (buf) {
                glGetProgramInfoLog(program, bufLength, NULL, buf);
                LOGE("Could not link program:\n%s\n", buf);
                free(buf);
            }
        }
        glDeleteProgram(program);
        program = 0;
    }

    return program;
}

extern int calcBlockCount(int imageSize, int blockSize)
{
    if (0 == imageSize % blockSize)
        return imageSize / blockSize;
    else
        return imageSize / blockSize + 1;
}

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_Log_setLogEnable(JNIEnv *env, jobject obj, jboolean enable)
{
    __LogEanable__ = enable;
}

#ifdef ALLOCATION_CHECK

static void initializeAllocPool()
{
    int i;
    for (i = 0; i < 128; ++i) {
        AllocInfo *willBeFirst = &allocInfoPool[i];
        willBeFirst->next = freeList;
        freeList = willBeFirst;
    }

    sem_init(&semaphore, 0, 1);

    isInitialized = 1;
}

static AllocInfo *getAllocInfo()
{
    if (!freeList) {
        LOGI("Allocation pool is empty. (>_<)\n");
        return NULL;
    }

    AllocInfo *ret = freeList;
    freeList = freeList->next;

    ret->next = NULL;

    if (!firstNode) {
        ret->prev = NULL;
        firstNode = ret;
    }

    if (lastNode) {
        ret->prev = lastNode;
        lastNode->next = ret;
    }

    lastNode = ret;

    return ret;
}

static void releaseAllocInfo(AllocInfo *info)
{
    if (info->prev) {
        info->prev->next = info->next;
    }
    else {
        firstNode = info->next;
    }

    if (info->next) {
        info->next->prev = info->prev;
    }
    else {
        lastNode = info->prev;
    }

    memset(info, 0, sizeof(AllocInfo));

    info->next = freeList;
    freeList = info;

    if (!firstNode)
        LOGI("-- alive memory is nothing. v(^^)\n");
}

void *__malloc__(int size, const char *func, int line)
{
    void *ret = malloc(size);

    if (!isInitialized)
        initializeAllocPool();

    sem_wait(&semaphore);

    AllocInfo *node = getAllocInfo();
    node->address = ret;
    node->seq = seq++;
    node->func = func;
    node->method = "malloc";
    node->size = size;
    node->line = line;
    LOGI("-- allocated seq=%d address=%08X size=%d -- alocated by %s() - %d : %s\n", node->seq, node->address, node->size, node->func, node->line, node->method);

    sem_post(&semaphore);
    return ret;
}

void *__calloc__(int num, int size, const char *func, int line)
{
    void *ret = calloc(num, size);

    if (!isInitialized)
        initializeAllocPool();

    sem_wait(&semaphore);

    AllocInfo *node = getAllocInfo();
    node->address = ret;
    node->seq = seq++;
    node->func = func;
    node->method = "calloc";
    node->size = size;
    node->line = line;
    LOGI("-- allocated seq=%d address=%08X size=%d -- alocated by %s() - %d : %s\n", node->seq, node->address, node->size, node->func, node->line, node->method);

    sem_post(&semaphore);
    return ret;
}

void __free__(void *p, const char *func, int line)
{
    free(p);

    sem_wait(&semaphore);

    AllocInfo *willBeReleased = NULL;

    AllocInfo *node;
    for (node = firstNode; NULL != node; node = node->next) {
        if (p == node->address) {
            LOGI("-- freed seq=%d address=%08X size=%d -- alocated by %s() - %d : %s\n", node->seq, node->address, node->size, node->func, node->line, node->method);
            willBeReleased = node;
        }
        else {
            LOGI("-- alive memory seq=%d address=%08X size=%d -- alocated by %s() - %d : %s\n", node->seq, node->address, node->size, node->func, node->line, node->method);
        }
    }

    if (willBeReleased)
        releaseAllocInfo(willBeReleased);

    sem_post(&semaphore);
}

#endif

