package com.example.dingjinwen01.recyclerviewweblistdemo.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.dingjinwen01.recyclerweblistdemo.R
import com.example.dingjinwen01.recyclerweblistdemo.model.GoodsMultiItemAddTab
import com.example.dingjinwen01.recyclerweblistdemo.model.GoodsMultiItemBasicTab
import com.example.dingjinwen01.recyclerweblistdemo.model.GoodsMultiItemDeleteTab
import com.example.dingjinwen01.recyclerweblistdemo.ui.fragment.BottomSingleSelectDialogFragment
import com.example.dingjinwen01.recyclerweblistdemo.ui.fragment.showDialogFragment
import com.example.dingjinwen01.recyclerweblistdemo.util.MoneyPointFilter

/**
 * Created by dingjinwen01 on 2018/5/28.
 */
class GoodsPackageAdapter(mContext: Context, mData: MutableList<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(mData) {

    init {
        addItemType(GoodsMultiItemBasicTab.BASIC, R.layout.item_list_tab_basic)
        addItemType(GoodsMultiItemDeleteTab.TABS, R.layout.item_list_tab_delete)
        addItemType(GoodsMultiItemAddTab.ADD, R.layout.item_list_tab_add)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
        //名字和类型
            GoodsMultiItemBasicTab.BASIC -> {
                val model = item as GoodsMultiItemBasicTab
                val editTabName = helper.getView<EditText>(R.id.edit_tab_name)
                var textTabType = helper.getView<TextView>(R.id.text_tab_type)

                if (!TextUtils.isEmpty(model.name)) {
                    editTabName.setText(model.name)
                } else {
                    editTabName.setText("")
                }

                if (!TextUtils.isEmpty(model.type)) {
                    textTabType.text = mContext.resources.getStringArray(R.array.goods_package_tabs)[model.type.toInt() - 1]
                } else {
                    textTabType.text = ""
                }

                helper.addOnClickListener(R.id.text_delete_package)

                //選項卡類型
                textTabType.setOnClickListener {
                    //1.非必选 ，2.单选  3.多选 4.必选2项 5.必选3项 6.必选4项
                    val dialogFragment = BottomSingleSelectDialogFragment.newInstance(mContext.resources.getStringArray(R.array.goods_package_tabs))
                    dialogFragment.onItemClickListener = object : BottomSingleSelectDialogFragment.OnItemClickListener {
                        override fun onItemClick(position: Int, content: String) {
                            textTabType.text = content
                            model.type = "" + (position + 1)
                        }
                    }
                    showDialogFragment(mContext as AppCompatActivity, dialogFragment, BottomSingleSelectDialogFragment::class.java.simpleName)
                }


                val nameWatcher = object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        if (helper.getView<View>(R.id.edit_tab_name).hasFocus()) {//判断当前EditText是否有焦点在
                            model.name = editable.toString()
                        }
                    }
                }

                editTabName.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        (view as EditText).addTextChangedListener(nameWatcher)
                    } else {
                        (view as EditText).removeTextChangedListener(nameWatcher)
                    }
                }
            }
        //选项卡类型
            GoodsMultiItemDeleteTab.TABS -> {
                val model = item as GoodsMultiItemDeleteTab
                val editNum = helper.getView<EditText>(R.id.edit_label_number)
                val editName = helper.getView<EditText>(R.id.edit_label_name)
                val editPrice = helper.getView<EditText>(R.id.edit_label_price)

                //限制只能输两位小数
                editPrice.filters = arrayOf<InputFilter>(MoneyPointFilter())

                if (!TextUtils.isEmpty(model.foodNo)) {
                    editNum.setText(model.foodNo)
                    editNum.setSelection(model.foodNo.length)
                } else {
                    editNum.setText("")
                }

                if (!TextUtils.isEmpty(model.tagName)) {
                    editName.setText(model.tagName)
                } else {
                    editName.setText("")
                }

                if (!TextUtils.isEmpty(model.tagPrice)) {
                    editPrice.setText(model.tagPrice)
                } else {
                    editPrice.setText("")
                }

                helper.addOnClickListener(R.id.img_delete_tab)

                val tagNumWatcher = object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        if (editNum.hasFocus()) {//判断当前EditText是否有焦点在
                            model.foodNo = editable.toString()
                            if (!TextUtils.isEmpty(editable.toString().trim())) {
                                //搜索操作
                                listener?.onSearchNameByNum(model.foodNo)
                            }
                        }
                    }
                }

                editNum.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        (view as EditText).addTextChangedListener(tagNumWatcher)
                    } else {
                        (view as EditText).removeTextChangedListener(tagNumWatcher)
                    }
                }

                val tagNameWatcher = object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        if (editName.hasFocus()) {//判断当前EditText是否有焦点在
                            model.tagName = editable.toString()
                        }
                    }
                }

                editName.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        (view as EditText).addTextChangedListener(tagNameWatcher)
                    } else {
                        (view as EditText).removeTextChangedListener(tagNameWatcher)
                    }
                }


                val priceWatcher = object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun afterTextChanged(editable: Editable) {
                        if (editPrice.hasFocus()) {//判断当前EditText是否有焦点在
                            model.tagPrice = editable.toString()
                        }
                    }
                }

                editPrice.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                    if (hasFocus) {
                        (view as EditText).addTextChangedListener(priceWatcher)
                    } else {
                        (view as EditText).removeTextChangedListener(priceWatcher)
                    }
                }
            }
        //添加选项卡
            GoodsMultiItemAddTab.ADD -> {
                helper.addOnClickListener(R.id.layout_add_tab)
            }
        }
    }

    interface OnSearchNameListener {
        /**
         * 根据商品编号搜索商品名字
         */
        fun onSearchNameByNum(num: String)
    }

    private var listener: OnSearchNameListener? = null

    fun setOnSearchNameListener(listener: OnSearchNameListener) {
        this.listener = listener
    }
}