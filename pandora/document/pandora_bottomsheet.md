# BottomSheet封装
我为大家提供了2个可继承的BottomSheet相关基类，分别是：[BaseBottomSheetDialog](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_bottomsheet.md#一basebottomsheetdialog)、
[BaseBottomSheetDialogFragment](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_bottomsheet.md#二basebottomsheetdialogfragment)，
使用方法可以参考[BottomSheetsActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/bottomsheet/BottomSheetsActivity.kt)。

## 一、BaseBottomSheetDialog
BaseBottomSheetDialog继承BottomSheetDialog，支持你获取并配置BottomSheetBehavior，方便你使用位移偏移量来完成动画特效。

### 1. 可重写方法
我对onCreate()方法进行了细分，回调顺序按文档描述顺序，大家可以根据自己的需要重写对应的方法，并在内部实现业务逻辑。

方法名称|描述|备注
:---|:---|:---
startCreate()|onCreate()中首先回调的方法|一般可以在这个方法内去处理构造函数带进来的参数
getLayoutId()|获取布局layoutId|无
findViews()|获取控件|可以在该方法内获取控件对象或者进行控件方法设置
setListeners()|设置监听器|在findViews()之后回调，可以在该方法内统一存放控件的监听器实现方法
initData()|设置数据|在setListeners()之后回调，可以在该方法内统一存放初始数据的配置逻辑
endCreate()|onCreate()中最后回调的方法|如果你还有业务逻辑不适合放在之前任何一个回调方法当中，辣么可以把它放在这里

获取BottomSheetBehavior对象回调

方法名称|描述|备注
:---|:---|:---
onBehaviorInit(behavior: BottomSheetBehavior<*>)|回调BottomSheetBehavior对象|无

### 2. 可配置方法
你可以配置是否状态栏透明已经弹出高度偏移量

方法名称|描述|备注
:---|:---|:---
configTransparentStatusBar()|配置状态栏是否透明|默认true，不透明false
configTopOffsetPx()|配置布局高度偏移量|默认0，返回像素值，弹框高度会向下偏移你指定的数值

### 3. 可调用方法
我封装两个常用调用方法方便大家调用

方法名称|描述|备注
:---|:---|:---
getDialogInterface()|获取DialogInterface对象|无
setDim(@FloatRange(from = 0.0, to = 1.0) value: Float)|设置背景蒙版透明度|0透明，1不透明

## 二、BaseBottomSheetDialogFragment
BaseBottomSheetDialogFragment继承自BottomSheetDialogFragment，
我对内部进行了一些处理，让用法和BaseBottomSheetDialog保持一致，参考[BaseBottomSheetDialog]()即可。

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_bottomsheet.md#bottomsheet封装)