package com.example.dingjinwen01.recyclerweblistdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 商品套餐
 * Created by dingjinwen01 on 2018/3/20.
 */
public class GoodsPackageModel implements Parcelable {
    private String package_id;
    private String package_name;
    private String package_type;
    private int package_remove;
    private List<GoodsPackageTagModel> package_tags;

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public int getPackage_remove() {
        return package_remove;
    }

    public void setPackage_remove(int package_remove) {
        this.package_remove = package_remove;
    }

    public List<GoodsPackageTagModel> getPackage_tags() {
        return package_tags;
    }

    public void setPackage_tags(List<GoodsPackageTagModel> package_tags) {
        this.package_tags = package_tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.package_id);
        dest.writeString(this.package_name);
        dest.writeString(this.package_type);
        dest.writeInt(this.package_remove);
        dest.writeTypedList(this.package_tags);
    }

    public GoodsPackageModel() {
    }

    protected GoodsPackageModel(Parcel in) {
        this.package_id = in.readString();
        this.package_name = in.readString();
        this.package_type = in.readString();
        this.package_remove = in.readInt();
        this.package_tags = in.createTypedArrayList(GoodsPackageTagModel.CREATOR);
    }

    public static final Creator<GoodsPackageModel> CREATOR = new Creator<GoodsPackageModel>() {
        @Override
        public GoodsPackageModel createFromParcel(Parcel source) {
            return new GoodsPackageModel(source);
        }

        @Override
        public GoodsPackageModel[] newArray(int size) {
            return new GoodsPackageModel[size];
        }
    };

    @Override
    public String toString() {
        return "GoodsPackageModel{" +
                "package_id='" + package_id + '\'' +
                ", package_name='" + package_name + '\'' +
                ", package_type='" + package_type + '\'' +
                ", package_remove=" + package_remove +
                ", package_tags=" + package_tags +
                '}';
    }
}
