package com.example.dingjinwen01.recyclerweblistdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dingjinwen01 on 2018/4/3.
 */

public class GoodsPackageTagModel implements Parcelable {

    private String tag_id;
    private String tag_name;
    private String tag_price;
    private String food_id;
    private String food_no;

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_price() {
        return tag_price;
    }

    public void setTag_price(String tag_price) {
        this.tag_price = tag_price;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getFood_no() {
        return food_no;
    }

    public void setFood_no(String food_no) {
        this.food_no = food_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag_id);
        dest.writeString(this.tag_name);
        dest.writeString(this.tag_price);
        dest.writeString(this.food_id);
        dest.writeString(this.food_no);
    }

    public GoodsPackageTagModel() {
    }

    protected GoodsPackageTagModel(Parcel in) {
        this.tag_id = in.readString();
        this.tag_name = in.readString();
        this.tag_price = in.readString();
        this.food_id = in.readString();
        this.food_no = in.readString();
    }

    public static final Creator<GoodsPackageTagModel> CREATOR = new Creator<GoodsPackageTagModel>() {
        @Override
        public GoodsPackageTagModel createFromParcel(Parcel source) {
            return new GoodsPackageTagModel(source);
        }

        @Override
        public GoodsPackageTagModel[] newArray(int size) {
            return new GoodsPackageTagModel[size];
        }
    };

    @Override
    public String toString() {
        return "GoodsPackageTagModel{" +
                "tag_id='" + tag_id + '\'' +
                ", tag_name='" + tag_name + '\'' +
                ", tag_price='" + tag_price + '\'' +
                ", food_id='" + food_id + '\'' +
                ", food_no='" + food_no + '\'' +
                '}';
    }
}
