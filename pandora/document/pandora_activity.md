# Activity基类
我为大家提供了4个可继承的Activity基类，分别是：AbsActivity、BaseActivity、BaseRefreshActivity、BaseSandwichActivity，后三个都继承自AbsActivity，
使用方法可以参考[MvcDemoActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/mvc/MvcDemoActivity.kt)。

## 一、AbsActivity
该Activity是Pandora里所有Activity封装类的基础，内部实现了Rx的生命周期绑定，EventBus解注册以及AnkoLayout的使用。

### 1. 可重写方法
我对onCreate()方法进行了细分，回调顺序按文档描述顺序，大家可以根据自己的需要重写对应的方法，并在内部实现业务逻辑。

方法名称|描述|备注
:---|:---|:---
startCreate()|onCreate()中首先回调的方法|在setContentView()方法之前回调，一般可以在这个方法内去获取Intent带过来的参数
getAbsLayoutId()|获取布局layoutId|如果使用AnkoLayout这里可以传0
afterSetContentView()|配置ContentView里的控件|在setContentView()方法之后回调，一般由二级封装类使用，在内部对控件进行处理
findViews(savedInstanceState: Bundle?)|获取控件|可以在该方法内获取控件对象或者进行控件方法设置
setListeners()|设置监听器|在findViews()之后回调，可以在该方法内统一存放控件的监听器实现方法
initData()|设置数据|在setListeners()之后回调，可以在该方法内统一存放初始数据的配置逻辑
endCreate()|onCreate()中最后回调的方法|如果你还有业务逻辑不适合放在之前任何一个回调方法当中，辣么可以把它放在这里

除了onCreate()的细分方法外，还有以下常用方法可以重写

方法名称|描述|备注
:---|:---|:---
onPressBack()|拦截物理返回按钮|返回true表示拦截物理返回键点击自行处理，返回false表示不拦截交由内部来处理
getSaveBundle()|保存持久化数据|当页面被回收时调用，你可以将需要持久化的数据保存在bundle里返回给内部保存
onRestore(bundle: Bundle)|获取持久化数据|当被回收的页面重新显示时回调，传入的bundle为之前保存的数据

### 2. 可调用方法
我封装了一些常用的内部调用方法，方便大家使用

方法名称|描述|备注
:---|:---|:---
getContext()|获取上下文|无
addFragment(@IdRes containerViewId: Int, fragment: Fragment, tag: String)|添加Fragment|无
replaceFragment(@IdRes containerViewId: Int, fragment: Fragment, tag: String)|替换Fragment|无
showFragment(fragment: Fragment)|显示Fragment|无
hideFragment(fragment: Fragment)|隐藏Fragment|无
getFragments()|获取Fragment列表|无
findFragmentByTag(tag: String)|通过tag找到对应的Fragment|无
isUseAnkoLayout()|是否使用AnkoLayout|无

### 3. 使用EventBus
我已经在AbsActivity里注册和解注册EventBus，有用到EventBus的可以直接写订阅方法接收即可，例如：
```
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onXxxxEvent(event: XxxxEvent) {

    }
```

### 4. 使用AnkoLayout
 - AbsActivity支持AnkoLayout，你只需要加上 **@UseAnkoLayout** 注解并且 **override fun getAbsLayoutId(): Int = 0** 即可
 - 了解官方 **Anko Layouts ([wiki](https://github.com/Kotlin/anko/wiki/Anko-Layouts))**
 - 了解如何继承AbsActivity使用AnkoLayout可以参考[AnkoLayoutActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/anko/AnkoLayoutActivity.kt)
 - 除了AbsActivity以外的Activity基类均不支持AnkoLayout

### 5. 使用Rx绑定生命周期
如果在AbsActivity的继承类里使用RxJava调用可以直接链上生命周期方法来指定何时停止订阅，例如：
```
    Observable.interval(1, TimeUnit.SECONDS)
        .compose(bindUntilEvent(ActivityEvent.DESTROY))//当Activity销毁时停止订阅
        .subscribe(BaseObserver.empty())
```

## 二、BaseActivity
BaseActivity继承自AbsActivity。
该Activity封装了常用的基础控件，包括：标题栏，加载页、加载失败页和无数据页。
每个页面都可以在Activity单独配置，
也可以在Application里对所有的BaseActivity继承类里的基础控件进行配置，统一配置请参考[Application基类](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_application.md)。

### 1. 可重写方法
除了AbsActivity里可以重写的方法外，BaseActivity新增或者变更了一些方法

 - 变更后不可重写的方法如下：

方法名称|描述
:---|:---
getAbsLayoutId()|已由内部实现，用getLayoutId()替代
afterSetContentView()|已由内部实现

 - 新增的可重写的方法

方法名称|描述|备注
:---|:---|:---
getLayoutId()|获取布局layoutId|用来替代getAbsLayoutId()
onClickBackBtn()|点击标题栏返回按钮回调|默认不实现，你可以根据自己的需要实现逻辑
onClickReload()|点击失败页面重试按钮回调|默认不实现，你可以根据自己的需要实现逻辑

### 2. 可调用方法
除了AbsActivity里的方法外，这里新增了一些方法

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

## 三、BaseRefreshActivity
BaseRefreshActivity继承自AbsActivity。除了和BaseActivity一样封装了基础控件外，还增加了下拉刷新控件

### 1. 可重写方法
BaseRefreshActivity比BaseActivity多了1个重写方法

方法名称|描述|备注
:---|:---|:---
onDataRefresh()|刷新回调|用户下拉刷新时会触发该回调方法

### 2. 可调用方法
比起BaseActivity多了4个可以调用的方法

方法名称|描述|备注
:---|:---|:---
setSwipeRefreshColorScheme(@ColorRes vararg colorResIds: Int)|设置下拉进度的切换颜色|无
setSwipeRefreshBackgroundColor(@ColorRes colorResId: Int)|设置下拉进度的背景颜色|无
setSwipeRefreshFinish()|设置刷新结束（隐藏刷新进度条）|无
setSwipeRefreshEnabled(enabled: Boolean)|设置刷新控件是否启用|true为启用，false为不启用

## 四、BaseSandwichActivity
BaseSandwichActivity继承自AbsActivity。与BaseRefreshActivity相比去掉了标题栏控件和相关方法，增加了顶部和底部的扩展布局。

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
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_activity.md#activity基类)