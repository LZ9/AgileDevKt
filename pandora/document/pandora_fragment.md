# Fragment基类
我为大家提供了4个可继承的Fragment基类，分别是：[LazyFragment](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_fragment.md#一lazyfragment)、
[BaseFragment](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_fragment.md#二basefragment)、
[BaseRefreshFragment](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_fragment.md#三baserefreshfragment)、
[BaseSandwichFragment](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_fragment.md#四basesandwichfragment)，
后三个都继承自LazyFragment，
使用方法可以参考[MvcFragmentActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/mvc/MvcFragmentActivity.kt)。

## 一、LazyFragment
该Fragment是Pandora里所有Fragment封装类的基础，内部实现了Rx的生命周期绑定，懒加载以及AnkoLayout的使用。

### 1. 可重写方法
为了实现fragment的懒加载，我对内部的回调机制进行了重新梳理，建议大家使用时重写我提供的方法。
回调顺序按文档描述顺序，大家可以在对应的方法内去实现业务逻辑。

方法名称|描述|备注
:---|:---|:---
getAbsLayoutId()|获取布局layoutId|如果使用AnkoLayout这里可以传0
startCreate()|懒加载首先回调的方法|一般可以在这个方法内去获取Arguments带过来的Bundle参数
beforeFindViews(view: View)|配置ContentView里的控件|一般由二级封装类使用，在内部对控件进行处理
findViews(view: View, savedInstanceState: Bundle?)|获取控件|可以在该方法内获取控件对象或者进行控件方法设置
setListeners(view: View)|设置监听器|在findViews()之后回调，可以在该方法内统一存放控件的监听器实现方法
initData(view: View)|设置数据|在setListeners()之后回调，可以在该方法内统一存放初始数据的配置逻辑
endCreate()|懒加载中最后回调的方法|如果你还有业务逻辑不适合放在之前任何一个回调方法当中，辣么可以把它放在这里

除了上面的方法外，你还可以重写下面几个我定义的常用方法

方法名称|描述|备注
:---|:---|:---
onPressBack()|拦截物理返回按钮|返回true表示拦截物理返回键点击自行处理，返回false表示不拦截交由内部来处理
configIsLazyLoad()|配置是否进行懒加载|默认true进行懒加载，false不使用懒加载
onFragmentResume()|替代onResume()|Fragment Resume时调用，与activity生命周期保持一致
onFragmentPause()|替代onPause()|Fragment Pause时调用，与activity生命周期保持一致

### 2. 可调用方法
我封装了一些常用的内部调用方法，方便大家使用

方法名称|描述|备注
:---|:---|:---
getContext()|获取上下文|如果Context为空会抛出IllegalStateException异常
isUseAnkoLayout()|是否使用AnkoLayout|无

### 3. 使用AnkoLayout
 - LazyFragment支持AnkoLayout，你只需要加上 **@UseAnkoLayout** 注解并且 **override fun getAbsLayoutId(): Int = 0** 然后重写 **getAnkoLayoutView()** 方法，比如：
```
    override fun getAnkoLayoutView(): View? {
        return UI {
            verticalLayout {
                padding = dip(30)
                mAccountTv = textView {
                    textSize = 16f
                }
                mNameTv = textView {
                    textSize = 16f
                    text = getString(R.string.al_name) + "  Jack"
                }
                mChangeBtn = button {
                    setText(R.string.al_change_detail)
                    setOnClickListener { view ->
                        mListener?.invoke(view)
                    }
                }
            }
        }.view
    }
```
 - 了解官方 **Anko Layouts ([wiki](https://github.com/Kotlin/anko/wiki/Anko-Layouts))**
 - 了解如何继承LazyFragment使用AnkoLayout可以参考[AnkoAccountFragment.kt](https://github.com/LZ9/AgileDevKt/blob/7ce5bcfcf3796567106f6539c355b15d42130c40/app/src/main/java/com/lodz/android/agiledevkt/modules/anko/fragment/AnkoAccountFragment.kt)
 或[AnkoDetailFragment.kt](https://github.com/LZ9/AgileDevKt/blob/7ce5bcfcf3796567106f6539c355b15d42130c40/app/src/main/java/com/lodz/android/agiledevkt/modules/anko/fragment/AnkoDetailFragment.kt)
 - 除了LazyFragment以外的Fragment基类**均不支持**AnkoLayout

### 4. 使用Rx绑定生命周期
如果在LazyFragment的继承类里使用RxJava调用可以直接链上生命周期方法来指定何时停止订阅，例如：
```
    Observable.interval(1, TimeUnit.SECONDS)
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))//当Fragment销毁时停止订阅
        .subscribe(BaseObserver.empty())
```

## 二、BaseFragment
BaseFragment继承自LazyFragment。
该Fragment封装了常用的基础控件，包括：标题栏（默认不显示），加载页、加载失败页和无数据页。
每个页面都可以在Fragment单独配置，
也可以在Application里对所有的BaseFragment继承类里的基础控件进行配置，统一配置请参考[Application基类](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_application.md)。

### 1. 可重写方法
除了LazyFragment里可以重写的方法外，BaseFragment新增或者变更了一些方法

 - 变更后不可重写的方法如下：

方法名称|描述
:---|:---
getAbsLayoutId()|已由内部实现，用getLayoutId()替代
beforeFindViews()|已由内部实现
getAnkoLayoutView()|不支持AnkoLayout不再让子类重写该方法

 - 新增的可重写的方法

方法名称|描述|备注
:---|:---|:---
getLayoutId()|获取布局layoutId|用来替代getAbsLayoutId()
onClickBackBtn()|点击标题栏返回按钮回调|默认不实现，你可以根据自己的需要实现逻辑
onClickReload()|点击失败页面重试按钮回调|默认不实现，你可以根据自己的需要实现逻辑

### 2. 可调用方法
除了LazyFragment里的方法外，这里新增了一些方法

方法名称|描述|备注
:---|:---|:---
showStatusLoading()|显示加载页|无
showStatusNoData()|显示无数据页|没有设置LayoutId时默认显示此页面
showStatusError()|显示加载失败页|用户点击重试按钮会回调onClickReload()
showStatusCompleted()|显示加载完成|显示出getLayoutId()里的UI页面
goneTitleBar()|隐藏标题栏|无
showTitleBar()|显示标题栏|无
getTitleBarLayout()|获取标题栏对象|可对标题栏进行配置
getLoadingLayout()|获取加载页对象|可对加载页进行配置
getNoDataLayout()|获取无数据页对象|可对无数据页进行配置
getErrorLayout()|获取加载失败页对象|可对加载失败页进行配置

## 三、BaseRefreshFragment
BaseRefreshFragment继承自LazyFragment。除了和BaseFragment一样封装了基础控件外，还增加了下拉刷新控件

### 1. 可重写方法
BaseRefreshFragment比BaseFragment多了1个重写方法

方法名称|描述|备注
:---|:---|:---
onDataRefresh()|刷新回调|用户下拉刷新时会触发该回调方法

### 2. 可调用方法
比起BaseFragment多了4个可以调用的方法

方法名称|描述|备注
:---|:---|:---
setSwipeRefreshColorScheme(@ColorRes vararg colorResIds: Int)|设置下拉进度的切换颜色|无
setSwipeRefreshBackgroundColor(@ColorRes colorResId: Int)|设置下拉进度的背景颜色|无
setSwipeRefreshFinish()|设置刷新结束（隐藏刷新进度条）|无
setSwipeRefreshEnabled(enabled: Boolean)|设置刷新控件是否启用|true为启用，false为不启用

## 四、BaseSandwichFragment
BaseSandwichFragment继承自LazyFragment。与BaseRefreshFragment相比去掉了标题栏控件和相关方法，增加了顶部和底部的扩展布局。

### 1. 可重写方法
新增两个可重写的方法

方法名称|描述|备注
:---|:---|:---
getTopLayoutId()|获取顶部布局layoutId|无
getBottomLayoutId()|获取底部布局layoutId|无

### 2. 可调用方法
新增两个可调用方法

方法名称|描述|备注
:---|:---|:---
getTopView()|获取顶部布局对象|无
getBottomView()|获取底部布局对象|无


## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_fragment.md#fragment基类)