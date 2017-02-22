package com.wardrobe.www.base.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商品
 * Created by admin on 2016/9/20.
 */
public class Clothes implements Parcelable {
    private Integer id;
    private String name;//照片名称
    private String division;//所属部件分类，如上衣
    private String imgUrl;//照片路径
    private String time;//收藏时间
    private String date;//收藏日期
    private boolean chosen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(division);
        parcel.writeString(imgUrl);
        parcel.writeString(time);
        parcel.writeString(date);
    }

    public static final Parcelable.Creator<Clothes> CREATOR = new Creator<Clothes>() {
        @Override
        public Clothes[] newArray(int size) {
            return new Clothes[size];
        }

        @Override
        public Clothes createFromParcel(Parcel in) {
            return new Clothes(in);
        }
    };

    private Clothes(Parcel in) {
        id = in.readInt();
        name = in.readString();
        division = in.readString();
        imgUrl = in.readString();
        time = in.readString();
        date = in.readString();
    }

    public Clothes() {
        super();
    }
}
