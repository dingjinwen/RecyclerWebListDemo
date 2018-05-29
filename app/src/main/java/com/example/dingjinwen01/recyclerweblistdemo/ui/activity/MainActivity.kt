package com.example.dingjinwen01.recyclerweblistdemo.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.dingjinwen01.recyclerviewweblistdemo.ui.GoodsPackageAdapter
import com.example.dingjinwen01.recyclerweblistdemo.R
import com.example.dingjinwen01.recyclerweblistdemo.model.*
import com.example.dingjinwen01.recyclerweblistdemo.ui.fragment.BottomSingleSelectDialogFragment
import com.example.dingjinwen01.recyclerweblistdemo.ui.fragment.showDialogFragment
import com.example.dingjinwen01.recyclerweblistdemo.util.LogUtil
import com.example.dingjinwen01.recyclerweblistdemo.util.MoneyPointFilter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import java.util.ArrayList

const val KEY_PACKAGE_TYPE_SINGLE: Int = 0//单品
const val KEY_PACKAGE_TYPE_PACKAGE: Int = 1//套餐
const val KEY_PACKAGE_TYPE_ONLY_PACKAGE: Int = 2//仅套餐

/**
 * 添加商品，类似web表单提交的功能
 */
class MainActivity : AppCompatActivity(), View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    //header_view
    private lateinit var headerView: View
    private lateinit var textSubmit: TextView
    private lateinit var textGoodsCategory: TextView
    private lateinit var layoutAddPhoto: LinearLayout
    private lateinit var imageAddPhoto: ImageView
    private lateinit var textGoodsPackage: TextView
    private lateinit var editGoodsName: EditText
    private lateinit var editGoodsNumber: EditText
    private lateinit var editGoodsPrice: EditText
    private lateinit var editGoodsDetail: EditText
    private lateinit var textGoodsTip: TextView
    //footer_view
    private lateinit var footerView: View
    private lateinit var layoutAddPackage: RelativeLayout

    private var mBottomSingleSelectDialogFragment: BottomSingleSelectDialogFragment? = null

    private lateinit var mAdapter: GoodsPackageAdapter
    private var mData: MutableList<GoodsPackageModel> = ArrayList()//真实的数据源
    private val mShowData = ArrayList<MultiItemEntity>()//显示的数据源
    private var mPackageType: Int = KEY_PACKAGE_TYPE_SINGLE//0.單品 ，1.套餐  2.僅套餐選項

    companion object {
        //1.非必选 ，2.单选  3.多选 4.必选2项 5.必选3项 6.必选4项
        var PACKAGE_TYPE_MUST_TWO = "4"
        var PACKAGE_TYPE_MUST_THREE = "5"
        var PACKAGE_TYPE_MUST_FOUR = "6"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initListener()
    }

    /**
     * 添加一个默认的选项卡
     */
    private fun addDefaultTabs(): GoodsPackageModel {
        //真实
        val packageModel = GoodsPackageModel()
        packageModel.package_remove = 0
        val data: MutableList<GoodsPackageTagModel> = ArrayList()
        data.add(GoodsPackageTagModel())
        packageModel.package_tags = data
        mData.add(packageModel)
        //显示
        mShowData.add(GoodsMultiItemBasicTab(packageModel))
        data.forEach {
            mShowData.add(GoodsMultiItemDeleteTab(packageModel, it))
        }
        mShowData.add(GoodsMultiItemAddTab(packageModel))
        return packageModel
    }

    private fun initView() {
        headerView = getHeaderView()
        footerView = getFooterView()

        recycleView.layoutManager = LinearLayoutManager(this)
        mAdapter = GoodsPackageAdapter(this, mShowData)
        mAdapter.addHeaderView(headerView)
        mAdapter.onItemChildClickListener = this
        recycleView.adapter = mAdapter

        mBottomSingleSelectDialogFragment = BottomSingleSelectDialogFragment.newInstance(this.resources.getStringArray(R.array.goods_package_type))
    }

    private fun initListener() {
        mBottomSingleSelectDialogFragment?.onItemClickListener = object : BottomSingleSelectDialogFragment.OnItemClickListener {
            override fun onItemClick(position: Int, content: String) {
                textGoodsPackage.text = content
                onSelectedPackageType(position)
            }
        }
    }

    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.headerview_add_goods_recycler, recycleView.parent as ViewGroup, false)
        textSubmit = view.find(R.id.text_submit)
        textGoodsPackage = view.find(R.id.text_goods_package)
        textGoodsCategory = view.find(R.id.text_goods_category)
        imageAddPhoto = view.find(R.id.image_add_photo)
        layoutAddPhoto = view.find(R.id.layout_add_photo)
        editGoodsName = view.find(R.id.edit_goods_name)
        editGoodsNumber = view.find(R.id.edit_goods_number)
        editGoodsPrice = view.find(R.id.edit_goods_price)
        editGoodsDetail = view.find(R.id.edit_goods_detail)
        textGoodsTip = view.find(R.id.text_goods_tip)
        textSubmit.setOnClickListener(this)
        textGoodsPackage.setOnClickListener(this)
        textGoodsCategory.setOnClickListener(this)
        imageAddPhoto.setOnClickListener(this)
        layoutAddPhoto.setOnClickListener(this)
        //限制只能输两位小数
        editGoodsPrice.filters = arrayOf<InputFilter>(MoneyPointFilter())
        //套餐类型默认单品
        textGoodsPackage.text = this.resources.getStringArray(R.array.goods_package_type)[0]

        return view
    }

    private fun getFooterView(): View {
        val view = layoutInflater.inflate(R.layout.footerview_add_goods_recycler, recycleView.parent as ViewGroup, false)
        layoutAddPackage = view.find(R.id.layout_add_package)
        layoutAddPackage.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_submit -> {//提交
                submit()
            }
            R.id.layout_add_package -> {//添加套餐
                addPackage()
            }
            R.id.text_goods_package -> {//选择套餐类型
                showDialogFragment(this, mBottomSingleSelectDialogFragment!!, BottomSingleSelectDialogFragment::class.java.simpleName)
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.text_delete_package -> {//删除套餐
                deletePackage(position)
            }
            R.id.img_delete_tab -> {//删除选项
                deletePackageTab(position)
            }
            R.id.layout_add_tab -> {//添加选项
                addPackageTab(position)
            }
        }
    }

    /**
     * 选择套餐类型
     * 0.单品 1.套餐 2.仅套餐选项
     */
    fun onSelectedPackageType(position: Int) {
        LogUtil.d("--是否有套餐on------$position")
        mPackageType = position
        when (position) {
            KEY_PACKAGE_TYPE_PACKAGE -> {//有套餐
                LogUtil.d("--開--${mData.size}----")
                if (mShowData.size == 0) {
                    addPackage()
                }
                if (mAdapter.footerLayoutCount == 0) {
                    mAdapter.addFooterView(footerView)
                }
            }
            KEY_PACKAGE_TYPE_SINGLE, KEY_PACKAGE_TYPE_ONLY_PACKAGE//单品和仅套餐选项
            -> {
                LogUtil.d("--関--$mData.size----")
                mData.clear()

                //显示
                mShowData.clear()
                if (mAdapter.footerLayoutCount == 1) {
                    mAdapter.removeFooterView(footerView)
                }
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 添加套餐
     */
    private fun addPackage() {
        LogUtil.d("添加整个套餐---")
        val packageModel = addDefaultTabs()
        val count = packageModel.package_tags.size + 2
        mAdapter.notifyItemRangeInserted(mShowData.size, count)
        recycleView.scrollToPosition(mShowData.size)
    }

    /**
     * 删除套餐
     */
    private fun deletePackage(position: Int) {
        LogUtil.d("删除整个套餐---" + position)
        val basicModel = mAdapter.data[position] as GoodsMultiItemBasicTab
        //真实
        val goodsPackageModel = basicModel.goodsPackageModel
        goodsPackageModel.package_remove = 1
        //显示
        mShowData.remove(basicModel)
        // 要用这种迭代器的方式，不然可能会报异常
        val iterator = mShowData.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            if (entity is GoodsMultiItemDeleteTab) {
                if (entity.goodsPackageModel === goodsPackageModel) {
                    iterator.remove()
                }
            }
            if (entity is GoodsMultiItemAddTab) {
                if (entity.goodsPackageModel === goodsPackageModel) {
                    iterator.remove()
                }
            }
        }
        val count = goodsPackageModel.package_tags.size + 2
        mAdapter.notifyItemRangeRemoved(position + 1, count)

    }

    /**
     * 添加套餐选项
     */
    private fun addPackageTab(position: Int) {
        LogUtil.d("添加一个选项卡---" + position)
        //真实
        val itemAddModel = mAdapter.data[position] as GoodsMultiItemAddTab
        val bigItem = itemAddModel.goodsPackageModel
        val smallItem = GoodsPackageTagModel()
        bigItem.package_tags.add(smallItem)
        //显示
        val itemSmallModel = GoodsMultiItemDeleteTab(bigItem, smallItem)
        mShowData.add(position, itemSmallModel)
        mAdapter.notifyItemInserted(position + 1)
    }

    /**
     * 删除套餐选项
     */
    private fun deletePackageTab(position: Int) {
        LogUtil.d("删除一个选项卡---" + position)
        //真实
        val smallModel = mAdapter.data[position] as GoodsMultiItemDeleteTab
        val bigItem = smallModel.goodsPackageModel
        val smallItem = smallModel.goodsPackageTagModel
        bigItem.package_tags.remove(smallItem)
        //显示
        mShowData.removeAt(position)
        mAdapter.notifyItemRemoved(position + 1)
    }

    /**
     * 检测是否有套餐没有选项
     */
    private fun checkEmptyTab(): Boolean {
        mData.forEach {
            if (it.package_remove == 0) {//已经删除了的==1，不用判断
                if (it.package_tags == null || it.package_tags.size == 0) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 检测选项卡類型為必選N項時，是否滿足條件
     * return true不滿足條件，false滿足條件
     */
    private fun checkMustSelect(): Boolean {
        mData.forEachIndexed { index, packageModel ->
            if (packageModel.package_remove == 0) {//已经删除了的==1，不用判断
                when (packageModel.package_type) {
                    PACKAGE_TYPE_MUST_TWO -> {//必选2项
                        if (packageModel.package_tags.size < 2) {
                            return true
                        }
                    }
                    PACKAGE_TYPE_MUST_THREE -> {//必选3项
                        if (packageModel.package_tags.size < 3) {
                            return true
                        }
                    }
                    PACKAGE_TYPE_MUST_FOUR -> {//必选4项
                        if (packageModel.package_tags.size < 4) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    /**
     * 检查套餐里面是否有内容为空的
     */
    private fun checkPackageContainEmptyContent(): Boolean {
        mData.forEachIndexed { _, goodsPackageModel ->
            if (goodsPackageModel.package_remove == 0) {//已经删除了的==1，不用判断
                if (TextUtils.isEmpty(goodsPackageModel.package_name)) {
                    showToast(R.string.goods_input_package_name)
                    return true
                }
                if (TextUtils.isEmpty(goodsPackageModel.package_type)) {
                    showToast(R.string.goods_input_package_type)
                    return true
                }
                goodsPackageModel.package_tags.forEachIndexed { _, goodsPackageTagModel ->
                    if (TextUtils.isEmpty(goodsPackageTagModel.tag_name)) {
                        showToast(R.string.goods_input_package_tab_name)
                        return true
                    }
                    if (TextUtils.isEmpty(goodsPackageTagModel.tag_price)) {
                        showToast(R.string.goods_input_package_tab_price)
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 提交商品信息
     */
    private fun submit() {
        val goodsNumber = editGoodsNumber.text.toString()
        val goodsCategory = textGoodsCategory.text.toString()
        val goodsName = editGoodsName.text.toString()
        val goodsDetail = editGoodsDetail.text.toString()
        val goodsPrice = editGoodsPrice.text.toString()
        LogUtil.d("---$goodsNumber---$goodsCategory---$goodsDetail---$goodsPrice")

        if (goodsNumber.isEmpty()) {
            showToast(R.string.goods_input_num)
            return
        }

//        if (goodsCategory.isEmpty()) {
//            showToast(R.string.goods_input_category)
//            return
//        }

        if (goodsName.isEmpty()) {
            showToast(R.string.goods_input_name)
            return
        }

        if (goodsPrice.isEmpty()) {
            showToast(R.string.goods_input_price)
            return
        }

        try {
            if (goodsPrice.toDouble() == 0.0) {
                showToast(R.string.goods_price_zero)
                return
            }
        } catch (e: Exception) {
        }

        if (checkEmptyTab()) {
            showToast(R.string.store_goods_tab_empty)
            return
        }

        if (checkMustSelect()) {
            showToast(R.string.store_goods_tab_not_enough)
            return
        }

        if (checkPackageContainEmptyContent()) {
            return
        }

        val goodsModel = GoodsModel()
        goodsModel.num = goodsNumber
//        goodsModel.type = goodsCategoryModel?.id//商品类型ID
        goodsModel.name = goodsName
        goodsModel.description = goodsDetail
        try {
            goodsModel.price = goodsPrice.toDouble()
        } catch (e: Exception) {
        }

        goodsModel.has_package = mPackageType

        if (mPackageType == KEY_PACKAGE_TYPE_PACKAGE) {
            goodsModel.packages = mData//这里必须用个list转一下，不然会出现package_tag第一个数据丢失的情况
        }


        LogUtil.dd("djw", goodsModel)
    }
}

fun AppCompatActivity.showToast(resId: Int) {
    Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show()
}

