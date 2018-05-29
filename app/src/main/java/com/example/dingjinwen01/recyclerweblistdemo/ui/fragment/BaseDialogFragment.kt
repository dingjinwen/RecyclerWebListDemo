package com.example.dingjinwen01.recyclerweblistdemo.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
/**
 * DialogFragment基類
 * Created by dingjinwen01 on 2018/5/29.
 */
abstract class BaseDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStyle()
    }

    //重寫該方法的，寬度能占全屏，通过layout下的自定义布局来创建对话框
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null != dialog) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        var view = inflater.inflate(getResLayoutId(), container, false)
        initViews(view)
        initData()
        return view
    }

    //重寫該方法的，寬度不能占全屏，适用于创建简单的对话框。用AlertDialog或者Dialog创建出Dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (null != dialog) {
            setDialogParams(dialog)
        }
    }

    abstract fun getResLayoutId(): Int
    abstract fun initStyle()
    abstract fun initViews(view: View)
    abstract fun initData()
    abstract fun setDialogParams(dialog: Dialog)
}

//防止重复弹出
fun showDialogFragment(activity: AppCompatActivity, dialogFragment: BaseDialogFragment, tag: String): BaseDialogFragment? {
    val fragmentManager = activity.supportFragmentManager
    var baseDialogFragment = fragmentManager.findFragmentByTag(tag)
    if (null == baseDialogFragment) {
        baseDialogFragment = dialogFragment
    }

    if (!activity.isFinishing && null != baseDialogFragment && !baseDialogFragment.isAdded) {
        fragmentManager.beginTransaction()
                .add(baseDialogFragment, tag)
                .commitAllowingStateLoss()
    }

    return baseDialogFragment as BaseDialogFragment
}
