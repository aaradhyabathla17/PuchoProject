package com.puchoInc.diya.TextSpeakData;

import android.os.Parcel;
import android.os.Parcelable;

public class SpeakData implements Parcelable {
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    private  int image;
    public SpeakData(int image,int serial)
    {
        this.image=image;
        this.serial_num=serial;
    }

    public int getSerial_num() {
        return serial_num;
    }

    public void setSerial_num(int serial_num) {
        this.serial_num = serial_num;
    }

    private  int serial_num;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
