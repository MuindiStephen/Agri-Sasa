#include <jni.h>
#include <string>




extern "C"
JNIEXPORT jstring JNICALL
Java_com_steve_1md_smartmkulima_utils_Constants_getStringBaseUrlDevelopment(JNIEnv *env,jclass clazz) {
    return env->NewStringUTF("https://testapi.io/api/stevemd/");
}