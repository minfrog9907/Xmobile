package com.example.hp.xmoblie.Items;

import android.util.Log;

import java.util.StringTokenizer;

/**
 * Created by HP on 2017-10-25.
 */

public class BoundingBoxItem {
    int top;
    int bottom;
    int left;
    int right;
    int i,j,z;
    float xScope,yScope;
    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getWidth(){
        return right-left;
    }
    public int getHeight(){
        return (bottom-top)*2;
    }
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getZ() {
        return z;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }



    public void setZ(int z) {
        this.z = z;
    }

    public void setBounding(String bounding, int i, int j, int z){
        StringTokenizer st = new StringTokenizer(bounding,",");
        setLeft(Integer.parseInt(st.nextToken()));
        setTop(Integer.parseInt(st.nextToken()));
        setRight(getLeft()+Integer.parseInt(st.nextToken()));
        setBottom(getTop()+Integer.parseInt(st.nextToken()));
        setI(i);
        setJ(j);
        setZ(z);
        Log.e("boundingdata",top+" "+left+" "+bottom+" "+right);
    }
    public boolean crushY(int y){
        if(top-20<=y&&top+getHeight()*2>=y)
            return true;
        else
            return false;
    }
    public boolean crushX(int x){
        if(left-20<=x&&right+20>=x)
            return true;
        else
            return false;
    }
    public void setScope(float x,float y){
        xScope=x;
        yScope =y;
    }
}
