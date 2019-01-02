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
我已经在AbsActivity里注册和解注册EventBus，有用到EventBus的可以直接写订阅方法接收即可
```
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onXxxxEvent(event: XxxxEvent) {

    }
```

### 4. 使用AnkoLayout
 - AbsActivity支持AnkoLayout，你只需要加上 **@UseAnkoLayout** 注解并且 **override fun getAbsLayoutId(): Int = 0** 即可
 - 了解官方 **Anko Layouts ([wiki](https://github.com/Kotlin/anko/wiki/Anko-Layouts))**
 - 了解如何继承AbsActivity使用AnkoLayout可以参考[AnkoLayoutActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/anko/AnkoLayoutActivity.kt)



## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部]()
