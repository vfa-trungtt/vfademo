#include <jni.h>
#include <GLES/gl.h>
#include "SNBitmap.h"

JNIEXPORT void JNICALL Java_com_kingjim_shotnote_SNGLImageView_glTexImage2D(JNIEnv *env, jobject obj, jobject jbitmap)
{
    SNBitmap bitmap;
    java2Native(env, jbitmap, &bitmap);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap.width, bitmap.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bitmap.address);
}

