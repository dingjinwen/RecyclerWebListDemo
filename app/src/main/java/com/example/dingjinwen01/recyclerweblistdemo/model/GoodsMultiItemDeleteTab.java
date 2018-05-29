package com.example.dingjinwen01.recyclerweblistdemo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dingjinwen01 on 2018/4/16.
 */

public class GoodsMultiItemDeleteTab implements MultiItemEntity {
    public static final int TABS = 2;

    private GoodsPackageModel bigItem;
    private GoodsPackageTagModel smallItem;

    public GoodsMultiItemDeleteTab(GoodsPackageModel bigItem, GoodsPackageTagModel smallItem) {
        this.bigItem = bigItem;
        this.smallItem = smallItem;
    }

    public String getFoodId() {
        return smallItem.getFood_id();
    }

    public void setFoodId(String food_id) {
        smallItem.setFood_id(food_id);
    }

    public String getFoodNo() {
        return smallItem.getFood_no();
    }

    public void setFoodNo(String food_no) {
        smallItem.setFood_no(food_no);
    }

    public String getTagName() {
        return smallItem.getTag_name();
    }

    public void setTagName(String tag_name) {
        smallItem.setTag_name(tag_name);
    }

    public String getTagPrice() {
        return smallItem.getTag_price();
    }

    public void setTagPrice(String tag_price) {
        smallItem.setTag_price(tag_price);
    }

    @Override
    public int getItemType() {
        return TABS;
    }

    public GoodsPackageTagModel getGoodsPackageTagModel() {
        return smallItem;
    }

    public GoodsPackageModel getGoodsPackageModel() {
        return bigItem;
    }
}
