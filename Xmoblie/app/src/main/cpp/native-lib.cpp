#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/asset_manager_jni.h>
#include <android/log.h>

int _DEF_MAX_BLOBS=10000;
using namespace cv;
using namespace std;

extern "C" {
Point corner[4];
JNIEXPORT double JNICALL
PointDistance(Point a, Point b);
JNIEXPORT double JNICALL
PointSize(Point a, Point b, Point c, Point d);
JNIEXPORT void JNICALL
FindFirstCorner(vector<vector<Point>> contours);
JNIEXPORT void JNICALL
FindForthCorner(vector<vector<Point>> contours);
JNIEXPORT void JNICALL
FindThirdCorner(vector<vector<Point>> contours);
JNIEXPORT void JNICALL
FindSecondCorner(vector<vector<Point>> contours);
JNIEXPORT Mat JNICALL
RotateImage(const Mat src, double degree, Point base);
JNIEXPORT double JNICALL
GetAngle(Point a, Point b, Point c);
JNIEXPORT string JNICALL
CurrentDateTime();

JNIEXPORT void JNICALL
Java_com_example_hp_xmoblie_Activity_CameraResultActivity_loadImage(
        JNIEnv *env,
        jobject,
        jstring imageFileName,
        jlong addrImage) {

    Mat &img_input = *(Mat *) addrImage;

    const char *nativeFileNameString = env->GetStringUTFChars(imageFileName, JNI_FALSE);

    string baseDir("/storage/emulated/0/");
    baseDir.append(nativeFileNameString);
    const char *pathDir = baseDir.c_str();

    img_input = imread(pathDir, IMREAD_COLOR);

}

JNIEXPORT String JNICALL
Java_com_example_hp_xmoblie_Activity_CameraResultActivity_imageprocessing(
        JNIEnv *env,
        jobject,
        jlong addrInputImage,
        jlong addrOutputImage) {

    Mat &img_input = *(Mat *) addrInputImage;
    Mat &img_output = *(Mat *) addrOutputImage;

    cvtColor(img_input, img_input, CV_BGR2RGB);
    cvtColor(img_input, img_output, CV_RGB2GRAY);
    blur(img_output, img_output, Size(17, 17));
    Canny(img_output, img_output, 150, 300, 5);

    vector<vector<Point>> contours;
    vector<Vec4i> hierarchy;

    Mat contour_Img = img_output;
    findContours(contour_Img, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,
                 Point(0, 0));

    FindFirstCorner(contours);
    FindSecondCorner(contours);
    FindThirdCorner(contours);
    FindForthCorner(contours);

    Point limitedPoint[2]={corner[0],corner[0]};
    for(int i=0; i<4; ++i ){
        limitedPoint[0].x = limitedPoint[0].x < corner[i].x ? limitedPoint[0].x : corner[i].x;
        limitedPoint[1].x = limitedPoint[1].x < corner[i].x ? corner[i].x : limitedPoint[1].x;
        limitedPoint[0].y = limitedPoint[0].y < corner[i].y ? limitedPoint[0].y : corner[i].y;
        limitedPoint[1].y = limitedPoint[1].y < corner[i].y ? corner[i].y : limitedPoint[1].y;

    }

    double w,h;
    w = limitedPoint[1].x- limitedPoint[0].x;
    h = limitedPoint[1].y- limitedPoint[0].y;
    __android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "%d %d %d %d ",limitedPoint[0].x+(int)w/2, limitedPoint[0].y+(int)h/2, (int)w, (int)h );
    Rect rectCrop =  Rect(limitedPoint[0].x, limitedPoint[0].y, (int)w, (int)h);
    Mat croppedImage =  Mat(img_input, rectCrop);
    img_output = croppedImage;

    string node = "/storage/emulated/0/cropedBills/"+CurrentDateTime()+".jpg";
    //imwrite(node, img_output);

    return node;
}
}
JNIEXPORT void JNICALL
FindFirstCorner(vector<vector<Point>> contours) {
    double maxlen = 100000000;

    for (int i = 0; i < contours.size(); ++i) {
        if (PointDistance(Point(0, 0), contours[i][0]) < maxlen) {
            maxlen = PointDistance(Point(0, 0), contours[i][0]);
            corner[0] = contours[i][0];
        }
    }
}

JNIEXPORT void JNICALL
FindSecondCorner(vector<vector<Point>> contours) {
    double maxlen = 0;
    for (int i = 0; i < contours.size(); ++i) {
        double tmp = PointDistance(corner[0], contours[i][0]);
        if (maxlen < tmp) {
            maxlen = tmp;
            corner[1] = contours[i][0];
        }

    }
}
JNIEXPORT void JNICALL
FindThirdCorner(vector<vector<Point>> contours) {
    double maxlen = 0;
    for (int i = 0; i < contours.size(); ++i) {
        double tmp =
                PointDistance(corner[0], contours[i][0]) + PointDistance(corner[1], contours[i][0]);
        if (maxlen < tmp) {
            maxlen = tmp;
            corner[2] = contours[i][0];
        }

    }
}
JNIEXPORT void JNICALL
FindForthCorner(vector<vector<Point>> contours) {
    double maxlen = 0;
    for (int i = 0; i < contours.size(); ++i) {
        double tmp = PointSize(corner[0],corner[1],corner[2],contours[i][0]);
        if (maxlen < tmp) {
            maxlen = tmp;
            corner[3] = contours[i][0];
        }

    }
}
JNIEXPORT double JNICALL
PointDistance(Point a, Point b) {
    return sqrt(pow(a.x - b.x, 2.0) + pow(a.y - b.y, 2));
}

JNIEXPORT double JNICALL
PointSize(Point a, Point b, Point c, Point d) {
    double x1, x2, x3, x4, y1, y2, y3, y4;
    x1 = a.x;
    x2 = b.x;
    x3 = c.x;
    x4 = d.x;

    y1 = a.y;
    y2 = b.y;
    y3 = c.y;
    y4 = d.y;
    return abs((x1 * y2 + x2 * y4 + x4 * y1) - (x2 * y1 + x4 * y2 + x1 * y4)) +
           abs((x1 * y4 + x4 * y3 + x3 * y1) - (x4 * y1 + x3 * y4 + x1 * y3)) +
           abs((x4 * y2 + x2 * y3 + x3 * y4) - (x2 * y4 + x3 * y2 + x4 * y3));

}
JNIEXPORT Mat JNICALL
RotateImage(const Mat src, double degree, Point base)
{
    Mat dst = src.clone();
    Mat rot_mat = getRotationMatrix2D(base,degree,1);
    warpAffine(src, dst, rot_mat, src.size());
    return dst;
}
JNIEXPORT double JNICALL
GetAngle(Point a, Point b, Point c){
    double p1 = sqrt(pow(a.x - c.x, 2) + pow(a.y - c.y, 2));
    double p2 = sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2));
    double p3 = sqrt(pow(b.x - c.x, 2) + pow(b.y - c.y, 2));

    double temp = (pow(p2,2) + pow(p3,2) - pow(p1,2)) / (2*p2*p3) ;


    return  temp*(90/3.14);

}
JNIEXPORT string JNICALL
CurrentDateTime() {
    time_t     now = time(0);
    struct tm  tstruct;
    char       buf[80];
    tstruct = *localtime(&now);
    // Visit http://en.cppreference.com/w/cpp/chrono/c/strftime
    // for more information about date/time format
    strftime(buf, sizeof(buf), "%Y%m%d.%X", &tstruct);

    return buf;
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