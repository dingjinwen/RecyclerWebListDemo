package com.example.dingjinwen01.recyclerweblistdemo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dingjinwen01 on 2018/4/16.
 */

public class GoodsMultiItemAddTab implements MultiItemEntity {
    public static final int ADD = 3;

    private GoodsPackageModel item;

    public GoodsMultiItemAddTab(GoodsPackageModel item) {
        this.item = item;
    }

    @Override
    public int getItemType() {
        return ADD;
    }

    public GoodsPackageModel getGoodsPackageModel() {
        return item;
    }
}
