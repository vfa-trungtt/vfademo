#ifndef _SNBITMAP_H_
#define _SNBITMAP_H_

#include <jni.h>
#include <stdio.h>

typedef struct _SNBitmap {
    uint32_t *address;
    int width;
    int height;
} SNBitmap;

inline void java2Native(JNIEnv *env, jobject jbitmap, SNBitmap *native)
{
    jfieldID id;
    jclass clazz = (*env)->GetObjectClass(env, jbitmap);

    id = (*env)->GetFieldID(env, clazz, "address", "I");
    native->address = (uint32_t *)(*env)->GetIntField(env, jbitmap, id);
    id = (*env)->GetFieldID(env, clazz, "width", "I");
    native->width = (*env)->GetIntField(env, jbitmap, id);
    id = (*env)->GetFieldID(env, clazz, "height", "I");
    native->height = (*env)->GetIntField(env, jbitmap, id);
}

inline void set2Java(JNIEnv *env, jobject jbitmap, uint32_t *address, int width, int height)
{
    jfieldID id;
    jclass clazz = (*env)->GetObjectClass(env, jbitmap);
    
    id = (*env)->GetFieldID(env, clazz, "address", "I");
    (*env)->SetIntField(env, jbitmap, id, (int)address);
    id = (*env)->GetFieldID(env, clazz, "width", "I");
    (*env)->SetIntField(env, jbitmap, id, width);
    id = (*env)->GetFieldID(env, clazz, "height", "I");
    (*env)->SetIntField(env, jbitmap, id, height);
}

extern int scaleWithFilter(SNBitmap *srcBitmap, SNBitmap *dstBitmap);

#endif /* _SNBITMAP_H_ */

