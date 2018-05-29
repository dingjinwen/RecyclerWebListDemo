# RecyclerView实现表单列表

>最近公司有个外卖项目，里面有个添加商品的功能，类似web表单那种，添加商品时可添加套餐选项，页面如下图

![添加商品](/screenshot/add_goods.png)

### 首先说一下这个页面要完成的基本功能：
* 选择商品类型，如果是套餐，展开列表，如果是非套餐类型的，收起列表；
* 可动态添加和删除整个配选项目；
* 可动态添加和删除配选项目里的选项；
* 删除配选项目时，不是真正把对应的数据删掉，而是把数据源里的package_remove字段变为1，因为这个商品在服务器数据库里还是存在的，只是在这个套餐里没有了而已，所以肯定不是直接就把数据库数据删除掉；

### 之前踩过的坑
* 刚拿到这个页面的时候，用了ScrollView里面嵌套一个Fragment，Fragment里面是个RecyclerView列表，RecyclerView的Item里面又嵌套一个RecyclerView，最终功能是现实了，但是感觉很难拓展和维护，也有一堆嵌套滑动带来的问题，最后还是重构了代码，换了一种实现方案。

### 新的实现方案
* 整个页面用一个RecyclerView完成，商品的基本信息是HeaderView，套餐信息对应List，有3种类型,最下面的新增配选项目是FooterView,如下图
  <br/>
  1. HeaderView ![HeaderView](/screenshot/header_view.jpg)
  <br/>

  2. item_type1 ![item_type1](/screenshot/item_type1.jpg)
  <br/>

  3. item_type2 ![item_type2](/screenshot/item_type2.jpg)
  <br/>

  4. item_type3 ![item_type3](/screenshot/item_type3.jpg)
  <br/>

  5. FooterView ![FooterView](/screenshot/footer_view.jpg)
  <br/>

* 用代理模式来实现Bean，维护两份数据，一份真正的数据源，提交到后台的，一份显示在界面上的数据源,只要维护好两份数据对应的关系就可以了，在界面输入的时候，已经同步修改了两处的数据
``` java
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
```

``` java
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
```
<br/>

* 用`BaseRecyclerViewAdapterHelper`来实现多类型的item，用起来特别简单

``` java
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

            }
            //选项卡类型
            GoodsMultiItemDeleteTab.TABS -> {

            }
            //添加选项卡
            GoodsMultiItemAddTab.ADD -> {

            }
        }
    }
}
```
<br/>

* 维护两份数据的关系，比如删除一个配选项目，真实的数据源对应的package_remove置1，显示的数据源直接删除对应项。
``` java
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
```
<br/>

* 最后分享一下，踩过的一个坑，列表的item里面如果有EditText，输入的内容，滑动列表的时候可能会数据错乱，要怎么有效的避免呢？查阅大量资料之后发现是因为焦点导致，解决方案如下：
``` java
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
```

最后献上源码地址：
[RecyclerWebListDemo](https://github.com/dingjinwen/RecyclerWebListDemo)