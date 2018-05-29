package com.example.dingjinwen01.recyclerweblistdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 添加商品时用的
 * Created by dingjinwen01 on 2018/4/2.
 */

public class GoodsModel implements Parcelable {
    private String food_id;
    private String type;//商品类型id
    private String name;
    private String num;
    private String description;
    private String images;
    private double price;
    private int has_package;
    private List<GoodsPackageModel> packages;

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getHas_package() {
        return has_package;
    }

    public void setHas_package(int has_package) {
        this.has_package = has_package;
    }

    public List<GoodsPackageModel> getPackages() {
        return packages;
    }

    public void setPackages(List<GoodsPackageModel> packages) {
        this.packages = packages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.food_id);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.num);
        dest.writeString(this.description);
        dest.writeString(this.images);
        dest.writeDouble(this.price);
        dest.writeInt(this.has_package);
        dest.writeTypedList(this.packages);
    }

    public GoodsModel() {
    }

    protected GoodsModel(Parcel in) {
        this.food_id = in.readString();
        this.type = in.readString();
        this.name = in.readString();
        this.num = in.readString();
        this.description = in.readString();
        this.images = in.readString();
        this.price = in.readDouble();
        this.has_package = in.readInt();
        this.packages = in.createTypedArrayList(GoodsPackageModel.CREATOR);
    }

    public static final Creator<GoodsModel> CREATOR = new Creator<GoodsModel>() {
        @Override
        public GoodsModel createFromParcel(Parcel source) {
            return new GoodsModel(source);
        }

        @Override
        public GoodsModel[] newArray(int size) {
            return new GoodsModel[size];
        }
    };

    @Override
    public String toString() {
        return "GoodsModel{" +
                "food_id='" + food_id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", description='" + description + '\'' +
                ", images='" + images + '\'' +
                ", price=" + price +
                ", has_package=" + has_package +
                ", packages=" + packages +
                '}';
    }
}
