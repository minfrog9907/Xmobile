package com.example.hp.xmoblie.Utill;

import android.os.Handler;
import android.os.Message;

import com.example.hp.xmoblie.Service.ServiceControlCenter;

/**
 * Created by HP on 2017-11-06.
 */

public class NotificationHandler extends Handler {
    String filename="";
    String path="";
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case  1://?
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"다운로드중",0,0,true);
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case 100://다운중
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"다운로드중",msg.arg2,msg.arg1,false);
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case 200://맥스 설정
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"다운로드중",0,msg.arg1,true);
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case  222:// 다운완료
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"다운로드 완료",filename,path);
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case 333://실패
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"다운로드 실패");
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;

            case 900:
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"업로드중",0,msg.arg1,true);
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case 990:
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"업로드중",msg.arg2,msg.arg1,false);
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case 999:
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"업로드 실패");
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
            case 9999:
                ServiceControlCenter.getInstance().getNotificationBarService().makeNotification(filename,"업로드 성공");
                ServiceControlCenter.getInstance().getNotificationBarService().pushNotification(1);
                break;
        }
    }
    public void setName(String name){
       filename =name;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
