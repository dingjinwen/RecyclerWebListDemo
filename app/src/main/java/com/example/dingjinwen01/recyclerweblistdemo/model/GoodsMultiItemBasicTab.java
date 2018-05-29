package com.example.dingjinwen01.recyclerweblistdemo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dingjinwen01 on 2018/4/16.
 */

public class GoodsMultiItemBasicTab implements MultiItemEntity {
    public static final int BASIC = 1;

    private GoodsPackageModel item;

    public GoodsMultiItemBasicTab(GoodsPackageModel item) {
        this.item = item;
    }

    public String getName() {
        return item.getPackage_name();
    }

    public void setName(String name) {
        item.setPackage_name(name);
    }

    public String getType() {
        return item.getPackage_type();
    }

    public void setType(String type) {
        item.setPackage_type(type);
    }

    @Override
    public int getItemType() {
        return BASIC;
    }

    public GoodsPackageModel getGoodsPackageModel() {
        return item;
    }
}
