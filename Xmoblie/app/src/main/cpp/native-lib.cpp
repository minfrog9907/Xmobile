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

JNIEXPORT void JNICALL
Java_com_example_hp_xmoblie_Activity_ImageGrayScaleActivity_loadImage(
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

JNIEXPORT void JNICALL
Java_com_example_hp_xmoblie_Activity_ImageGrayScaleActivity_imageprocessing(
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

<<<<<<< HEAD
    y1 = a.y;
    y2 = b.y;
    y3 = c.y;
    y4 = d.y;
    return abs((x1 * y2 + x2 * y4 + x4 * y1) - (x2 * y1 + x4 * y2+ x1 * y4)) +
           abs((x1 * y4 + x4 * y3 + x3 * y1) - (x4 * y1 + x3 * y4 + x1 * y3)) +
           abs((x4 * y2 + x2 * y3 + x3 * y4) - (x2 * y4 + x3 * y2 + x4 * y3));
=======
typedef struct
{
    bool	bVisitedFlag;
    CvPoint ptReturnPoint;
} Visited;
IplImage*	m_Image= NULL;
int			m_nThreshold= 0;
Visited*	m_vPoint;
int			m_nBlobs= _DEF_MAX_BLOBS;
CvRect*		m_recBlobs= NULL;
int*		m_intBlobs= NULL;

JNIEXPORT void JNICALL
DoLabeling();
JNIEXPORT int JNICALL
Labeling(IplImage* image, int nThreshold);
JNIEXPORT void JNICALL
InitvPoint(int nWidth, int nHeight);// m_vPoint 초기화 함수
JNIEXPORT void JNICALL
DeletevPoint();
JNIEXPORT int JNICALL
_Labeling(unsigned char *DataBuf, int nWidth, int nHeight, int nThreshold);// Size가 nWidth이고 nHeight인 DataBuf에서 nThreshold보다 작은 영역을 제외한 나머지를 blob으로 획득
JNIEXPORT void JNICALL
DetectLabelingRegion(int nLabelNumber, unsigned char *DataBuf, int nWidth, int nHeight);
JNIEXPORT int JNICALL
__NRFIndNeighbor(unsigned char *DataBuf, int nWidth, int nHeight, int nPosX, int nPosY, int *StartX, int *StartY, int *EndX, int *EndY );
JNIEXPORT int JNICALL
__Area(unsigned char *DataBuf, int StartX, int StartY, int EndX, int EndY, int nWidth, int nLevel);
JNIEXPORT void JNICALL
BlobSmallSizeConstraint(int nWidth, int nHeight);
JNIEXPORT void JNICALL
_BlobSmallSizeConstraint(int nWidth, int nHeight, CvRect* rect, int* label, int *nRecNumber);
// nWidth와 nHeight보다 큰 rec을 모두 삭제
JNIEXPORT void JNICALL
BlobBigSizeConstraint(int nWidth, int nHeight);
JNIEXPORT void JNICALL
_BlobBigSizeConstraint(int nWidth, int nHeight, CvRect* rect, int* label, int* nRecNumber);



JNIEXPORT void JNICALL
Java_com_example_hp_xmoblie_Activity_ImageGrayScaleActivity_setParam(
        JNIEnv *env,
        jobject,
        jstring imageFileName,
        jlong addrImage,
        jint nThreshold) {

    Mat &img_input = *(Mat *) addrImage;

    const char *nativeFileNameString = env->GetStringUTFChars(imageFileName, JNI_FALSE);

    string baseDir("/storage/emulated/0/");
    baseDir.append(nativeFileNameString);
    const char *pathDir = baseDir.c_str();

    img_input = imread(pathDir, IMREAD_COLOR);

    if( m_recBlobs != NULL )
    {
        delete m_recBlobs;

        m_recBlobs	= NULL;
        m_nBlobs	= _DEF_MAX_BLOBS;
    }

    if( m_intBlobs != NULL )
    {
        delete m_intBlobs;

        m_intBlobs	= NULL;
        m_nBlobs	= _DEF_MAX_BLOBS;
    }

    if( m_Image != NULL )	cvReleaseImage( &m_Image );

    m_Image			= cvCloneImage( new IplImage(img_input) );

    m_nThreshold	= nThreshold;

    DoLabeling();
}

JNIEXPORT void JNICALL
DoLabeling()
{
    m_nBlobs = Labeling(m_Image, m_nThreshold);
}

JNIEXPORT int JNICALL
Labeling(IplImage* image, int nThreshold)
{
    if( image->nChannels != 1 ) 	return 0;

    int nNumber;

    int nWidth	= image->width;
    int nHeight = image->height;

    unsigned char* tmpBuf = new unsigned char [nWidth * nHeight];

    int i,j;

    for(j=0;j<nHeight;j++)
        for(i=0;i<nWidth ;i++)
            tmpBuf[j*nWidth+i] = (unsigned char)image->imageData[j*image->widthStep+i];

    // 레이블링을 위한 포인트 초기화
    InitvPoint(nWidth, nHeight);

    // 레이블링
    nNumber = _Labeling(tmpBuf, nWidth, nHeight, nThreshold);

    // 포인트 메모리 해제
    DeletevPoint();

    if( nNumber != _DEF_MAX_BLOBS )		m_recBlobs = new CvRect [nNumber];

    if( nNumber != _DEF_MAX_BLOBS )		m_intBlobs = new int [nNumber];

    if( nNumber != 0 )	DetectLabelingRegion(nNumber, tmpBuf, nWidth, nHeight);

    for(j=0;j<nHeight;j++)
        for(i=0;i<nWidth ;i++)
            image->imageData[j*image->widthStep+i] = tmpBuf[j*nWidth+i];

    delete tmpBuf;
    return nNumber;
}

JNIEXPORT void JNICALL
InitvPoint(int nWidth, int nHeight)
{
    int nX, nY;

    m_vPoint = new Visited [nWidth * nHeight];

    for(nY = 0; nY < nHeight; nY++)
    {
        for(nX = 0; nX < nWidth; nX++)
        {
            m_vPoint[nY * nWidth + nX].bVisitedFlag		= false;
            m_vPoint[nY * nWidth + nX].ptReturnPoint.x	= nX;
            m_vPoint[nY * nWidth + nX].ptReturnPoint.y	= nY;
        }
    }
}

JNIEXPORT void JNICALL
DeletevPoint()
{
    delete m_vPoint;
}

// Size가 nWidth이고 nHeight인 DataBuf에서
// nThreshold보다 작은 영역을 제외한 나머지를 blob으로 획득
JNIEXPORT int JNICALL
_Labeling(unsigned char *DataBuf, int nWidth, int nHeight, int nThreshold)
{
    int Index = 0, num = 0;
    int nX, nY, k, l;
    int StartX , StartY, EndX , EndY;

    // Find connected components
    for(nY = 0; nY < nHeight; nY++)
    {
        for(nX = 0; nX < nWidth; nX++)
        {
            if(DataBuf[nY * nWidth + nX] == 255)		// Is this a new component?, 255 == Object
            {
                num++;

                DataBuf[nY * nWidth + nX] = num;

                StartX = nX, StartY = nY, EndX = nX, EndY= nY;

                __NRFIndNeighbor(DataBuf, nWidth, nHeight, nX, nY, &StartX, &StartY, &EndX, &EndY);

                if(__Area(DataBuf, StartX, StartY, EndX, EndY, nWidth, num) < nThreshold)
                {
                    for(k = StartY; k <= EndY; k++)
                    {
                        for(l = StartX; l <= EndX; l++)
                        {
                            if(DataBuf[k * nWidth + l] == num)
                                DataBuf[k * nWidth + l] = 0;
                        }
                    }
                    --num;

                    if(num > 250)
                        return  0;
                }
            }
        }
    }

    return num;
}

// Blob labeling해서 얻어진 결과의 rec을 얻어냄
JNIEXPORT void JNICALL
DetectLabelingRegion(int nLabelNumber, unsigned char *DataBuf, int nWidth, int nHeight)
{
    int nX, nY;
    int nLabelIndex ;

    bool bFirstFlag[255] = {false,};

    for(nY = 1; nY < nHeight - 1; nY++)
    {
        for(nX = 1; nX < nWidth - 1; nX++)
        {
            nLabelIndex = DataBuf[nY * nWidth + nX];

            if(nLabelIndex != 0)	// Is this a new component?, 255 == Object
            {
                if(bFirstFlag[nLabelIndex] == false)
                {
                    m_recBlobs[nLabelIndex-1].x			= nX;
                    m_recBlobs[nLabelIndex-1].y			= nY;
                    m_recBlobs[nLabelIndex-1].width		= 0;
                    m_recBlobs[nLabelIndex-1].height	= 0;

                    bFirstFlag[nLabelIndex] = true;
                }
                else
                {
                    int left	= m_recBlobs[nLabelIndex-1].x;
                    int right	= left + m_recBlobs[nLabelIndex-1].width;
                    int top		= m_recBlobs[nLabelIndex-1].y;
                    int bottom	= top + m_recBlobs[nLabelIndex-1].height;

                    if( left   >= nX )	left	= nX;
                    if( right  <= nX )	right	= nX;
                    if( top    >= nY )	top		= nY;
                    if( bottom <= nY )	bottom	= nY;

                    m_recBlobs[nLabelIndex-1].x			= left;
                    m_recBlobs[nLabelIndex-1].y			= top;
                    m_recBlobs[nLabelIndex-1].width		= right - left;
                    m_recBlobs[nLabelIndex-1].height	= bottom - top;

                    m_intBlobs[nLabelIndex-1]			= nLabelIndex;
                }
            }

        }
    }

}

// Blob Labeling을 실제 행하는 function
// 2000년 정보처리학회에 실린 논문 참조
JNIEXPORT int JNICALL
__NRFIndNeighbor(unsigned char *DataBuf, int nWidth, int nHeight, int nPosX, int nPosY, int *StartX, int *StartY, int *EndX, int *EndY )
{
    CvPoint CurrentPoint;

    CurrentPoint.x = nPosX;
    CurrentPoint.y = nPosY;

    m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x].bVisitedFlag    = true;
    m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x].ptReturnPoint.x = nPosX;
    m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x].ptReturnPoint.y = nPosY;

    while(1)
    {
        if( (CurrentPoint.x != 0) && (DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x - 1] == 255) )   // -X 방향
        {
            if( m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x - 1].bVisitedFlag == false )
            {
                DataBuf[CurrentPoint.y  * nWidth + CurrentPoint.x  - 1]					= DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x];	// If so, mark it
                m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x - 1].bVisitedFlag	= true;
                m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x - 1].ptReturnPoint	= CurrentPoint;
                CurrentPoint.x--;

                if(CurrentPoint.x <= 0)
                    CurrentPoint.x = 0;

                if(*StartX >= CurrentPoint.x)
                    *StartX = CurrentPoint.x;

                continue;
            }
        }

        if( (CurrentPoint.x != nWidth - 1) && (DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x + 1] == 255) )   // -X 방향
        {
            if( m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x + 1].bVisitedFlag == false )
            {
                DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x + 1]					= DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x];	// If so, mark it
                m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x + 1].bVisitedFlag	= true;
                m_vPoint[CurrentPoint.y * nWidth +  CurrentPoint.x + 1].ptReturnPoint	= CurrentPoint;
                CurrentPoint.x++;

                if(CurrentPoint.x >= nWidth - 1)
                    CurrentPoint.x = nWidth - 1;

                if(*EndX <= CurrentPoint.x)
                    *EndX = CurrentPoint.x;

                continue;
            }
        }

        if( (CurrentPoint.y != 0) && (DataBuf[(CurrentPoint.y - 1) * nWidth + CurrentPoint.x] == 255) )   // -X 방향
        {
            if( m_vPoint[(CurrentPoint.y - 1) * nWidth +  CurrentPoint.x].bVisitedFlag == false )
            {
                DataBuf[(CurrentPoint.y - 1) * nWidth + CurrentPoint.x]					= DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x];	// If so, mark it
                m_vPoint[(CurrentPoint.y - 1) * nWidth +  CurrentPoint.x].bVisitedFlag	= true;
                m_vPoint[(CurrentPoint.y - 1) * nWidth +  CurrentPoint.x].ptReturnPoint = CurrentPoint;
                CurrentPoint.y--;

                if(CurrentPoint.y <= 0)
                    CurrentPoint.y = 0;

                if(*StartY >= CurrentPoint.y)
                    *StartY = CurrentPoint.y;

                continue;
            }
        }

        if( (CurrentPoint.y != nHeight - 1) && (DataBuf[(CurrentPoint.y + 1) * nWidth + CurrentPoint.x] == 255) )   // -X 방향
        {
            if( m_vPoint[(CurrentPoint.y + 1) * nWidth +  CurrentPoint.x].bVisitedFlag == false )
            {
                DataBuf[(CurrentPoint.y + 1) * nWidth + CurrentPoint.x]					= DataBuf[CurrentPoint.y * nWidth + CurrentPoint.x];	// If so, mark it
                m_vPoint[(CurrentPoint.y + 1) * nWidth +  CurrentPoint.x].bVisitedFlag	= true;
                m_vPoint[(CurrentPoint.y + 1) * nWidth +  CurrentPoint.x].ptReturnPoint = CurrentPoint;
                CurrentPoint.y++;

                if(CurrentPoint.y >= nHeight - 1)
                    CurrentPoint.y = nHeight - 1;

                if(*EndY <= CurrentPoint.y)
                    *EndY = CurrentPoint.y;

                continue;
            }
        }

        if(		(CurrentPoint.x == m_vPoint[CurrentPoint.y * nWidth + CurrentPoint.x].ptReturnPoint.x)
                   &&	(CurrentPoint.y == m_vPoint[CurrentPoint.y * nWidth + CurrentPoint.x].ptReturnPoint.y) )
        {
            break;
        }
        else
        {
            CurrentPoint = m_vPoint[CurrentPoint.y * nWidth + CurrentPoint.x].ptReturnPoint;
        }
    }

    return 0;
}

// 영역중 실제 blob의 칼라를 가진 영역의 크기를 획득
JNIEXPORT int JNICALL
__Area(unsigned char *DataBuf, int StartX, int StartY, int EndX, int EndY, int nWidth, int nLevel)
{
    int nArea = 0;
    int nX, nY;

    for (nY = StartY; nY < EndY; nY++)
        for (nX = StartX; nX < EndX; nX++)
            if (DataBuf[nY * nWidth + nX] == nLevel)
                ++nArea;

    return nArea;
}

/******************************************************************************************************/

// nWidth와 nHeight보다 작은 rec을 모두 삭제
JNIEXPORT void JNICALL
BlobSmallSizeConstraint(int nWidth, int nHeight)
{
    _BlobSmallSizeConstraint(nWidth, nHeight, m_recBlobs, m_intBlobs, &m_nBlobs);
}

JNIEXPORT void JNICALL
_BlobSmallSizeConstraint(int nWidth, int nHeight, CvRect* rect, int* label, int *nRecNumber)
{
    if(*nRecNumber == 0)	return;

    int nX;
    int ntempRec = 0;

    CvRect* temp = new CvRect[*nRecNumber];
    int* labeled = new int[*nRecNumber];

    for(nX = 0; nX < *nRecNumber; nX++)
    {
        temp[nX]	= rect[nX];
        labeled[nX] = label[nX];
    }

    for(nX = 0; nX < *nRecNumber; nX++)
    {
        if( (rect[nX].width > nWidth) && (rect[nX].height > nHeight) )
        {
            temp[ntempRec] = rect[nX];
            labeled[ntempRec] = label[nX];

            ntempRec++;
        }
    }

    *nRecNumber = ntempRec;

    for(nX = 0; nX < *nRecNumber; nX++)
    {
        rect[nX] = temp[nX];
        label[nX] = labeled[nX];
    }

    delete temp;
    delete labeled;
}


JNIEXPORT void JNICALL
BlobBigSizeConstraint(int nWidth, int nHeight)
{
    _BlobBigSizeConstraint(nWidth, nHeight, m_recBlobs, m_intBlobs, &m_nBlobs);
}

JNIEXPORT void JNICALL
_BlobBigSizeConstraint(int nWidth, int nHeight, CvRect* rect, int* label, int* nRecNumber)
{
    if(*nRecNumber == 0)	return;

    int nX;
    int ntempRec = 0;

    CvRect* temp = new CvRect [*nRecNumber];
    int* labeled = new int [*nRecNumber];

    for(nX = 0; nX < *nRecNumber; nX++)
    {
        temp[nX] = rect[nX];
        labeled[nX] = label[nX];
    }

    for(nX = 0; nX < *nRecNumber; nX++)
    {
        if( (rect[nX].width < nWidth) && (rect[nX].height < nHeight) )
        {
            temp[ntempRec] = rect[nX];
            labeled[ntempRec] = label[nX];

            ntempRec++;
        }
    }

    *nRecNumber = ntempRec;

    for(nX = 0; nX < *nRecNumber; nX++)
    {
        rect[nX] = temp[nX];
        label[nX] = labeled[nX];
    }

    delete temp;
    delete labeled;
}

///////////////////////////////////////////////////////////////////////////////////
// 원하는 블롭의 이미지 정보만 가져온다.

JNIEXPORT void JNICALL
Java_com_example_hp_xmoblie_Activity_ImageGrayScaleActivity_getBlobImage(
        JNIEnv *env,
        jobject,
        jlong* outputImg,
        int nLabel)
{
    IplImage* dest;
    cvSetZero( dest );

    CvRect rect = m_recBlobs[ nLabel ];
    int nNum	= m_intBlobs[ nLabel ];

    for( int j = 0; j < rect.height; j++ )
        for( int i = 0; i < rect.width;  i++ )
        {
            unsigned char val = (unsigned char)m_Image->imageData[ (j + rect.y) * m_Image->widthStep + (i + rect.x) ];

            if( val == nNum )	dest->imageData[ j * dest->widthStep + i ] = (unsigned char)255;
        }

}


>>>>>>> 0b7340924949252c477d4ea6ad71c96f0832c7d5
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

