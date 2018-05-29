package com.example.dingjinwen01.recyclerweblistdemo.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.dingjinwen01.recyclerweblistdemo.R
import org.jetbrains.anko.find
/**
 * 底部弹出的带取消按钮的单选框
 * 适用添加商品套餐，選項卡列表的弹框
 * Created by dingjinwen01 on 2018/5/29.
 */
class BottomSingleSelectDialogFragment : BaseDialogFragment() {

    lateinit var recycleView: RecyclerView
    private lateinit var textCancel: TextView
    private lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>
    var mData: MutableList<String> = ArrayList()

    companion object {
        fun newInstance(data: Array<String>): BottomSingleSelectDialogFragment {
            val args = Bundle()
            args.putStringArray("data", data)
            val fragment = BottomSingleSelectDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getResLayoutId() = R.layout.layout_dialog_fragment_goods_tabs_list

    override fun initStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomFragmentDialogWithAnim)
    }

    override fun initViews(view: View) {
        recycleView = view.find(R.id.recyclerView)
        textCancel = view.find(R.id.text_cancel)
    }

    override fun initData() {

        mData.clear()
        mData.addAll(arguments?.getStringArray("data")!!.toMutableList())

        textCancel.setOnClickListener {
            dismiss()
        }

        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)
        recycleView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_goods_tabs_select_list, mData) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.setText(R.id.text_tab, item)
            }
        }
        mAdapter.setOnItemClickListener { adapter, _, position ->
            onItemClickListener?.onItemClick(position, adapter.data[position].toString())
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        recycleView.adapter = mAdapter
    }

    override fun setDialogParams(dialog: Dialog) {
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        // 设置底部弹出显示的DialogFragment窗口属性。
        val window = dialog.window
        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, content: String)
    }

}