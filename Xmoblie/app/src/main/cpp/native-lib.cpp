#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/asset_manager_jni.h>
#include <android/log.h>

int _DEF_MAX_BLOBS=10000;
using namespace cv;
using namespace std;

extern "C" {

JNIEXPORT void JNICALL
Java_com_example_hp_xmoblie_Activity_CameraResultActivity_loadImage(
        JNIEnv *env,
        jobject,
        jstring imageFileName,
        jlong addrImage) {


}

JNIEXPORT String JNICALL
Java_com_example_hp_xmoblie_Activity_CameraResultActivity_imageprocessing(
        JNIEnv *env,
        jobject,
        jlong addrInputImage,
        jlong addrOutputImage) {
    return "";
}
}
/*
 *
      for (int i = 0; i < contours.size(); ++i) {

        RotatedRect rect = minAreaRect(contours[i]);
        double areaRatio = abs(contourArea(contours[i])) / (rect.size.width * rect.size.height);
        drawContours(img_input, corner[0], i, CV_RGB(0, 0, 255), 10);
    int x4 = contours[i].data()->x;
    int y4 = contours[i].data()->y;
    double tmp = sqrt(pow(x4 - x, 2) + pow(y4 - y, 2));
    if (maxlen > tmp) {
        maxlen = tmp;
corner[1] = contours[i];
}

}
 */
