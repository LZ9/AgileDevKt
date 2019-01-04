# PopupWindow封装
我封装了一个[BasePopupWindow]()基类，
可以继承它来快速实现一个PopupWindow，
继承用法可以参考[OrientationPopupWindow.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/rv/popup/OrientationPopupWindow.kt)。

### 1. 可重写方法
按照惯例对初始化方法进行了细分，回调顺序按文档描述顺序，大家可以根据自己的需要重写对应的方法，并在内部实现业务逻辑。

方法名称|描述|备注
:---|:---|:---
getLayoutId()|获取布局layoutId|无
startCreate()|首先回调的方法|一般可以在这个方法内去处理构造函数带进来的参数
findViews(view: View)|获取控件|可以在该方法内获取控件对象或者进行控件方法设置
setListeners()|设置监听器|在findViews()之后回调，可以在该方法内统一存放控件的监听器实现方法
initData()|设置数据|在setListeners()之后回调，可以在该方法内统一存放初始数据的配置逻辑
endCreate()|最后回调的方法|如果你还有业务逻辑不适合放在之前任何一个回调方法当中，辣么可以把它放在这里

### 2. 可配置方法
可快速配置PopupWindow的高度、宽度和阴影

方法名称|描述|备注
:---|:---|:---
getWidthPx()|配置宽度|默认ViewGroup.LayoutParams.WRAP_CONTENT，单位像素
getHeightPx()|配置高度|默认ViewGroup.LayoutParams.WRAP_CONTENT，单位像素
getElevationValue()|配置阴影值|无

### 3. 可调用方法
我封装3个常用调用方法方便大家调用

方法名称|描述|备注
:---|:---|:---
create()|创建PopupWindow|必须在非构造函数内调用，建议在获得继承类对象时调用该方法
getPopup()|获取内部PopupWindow对象|无
getContext()|获取上下文|无

### 4. 调用方式
XxxxPopupWindow为BasePopupWindow的继承类，在创建得到popupWindow对象时，必须调用create()进行创建，
显示时调用getPopup()获取内部的PopupWindow对象来设置显示位置。
```
    val popupWindow = XxxxPopupWindow(getContext())
    popupWindow.create()//得到对象后立即调用
    popupWindow.getPopup().showAsDropDown(view, -50, 20)
```

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部]()