# Dialog封装
我为大家提供了6个可继承的Dialog基类，分别是：[BaseDialog]()、
[BaseCenterDialog]()、
[BaseBottomDialog]()、
[BaseTopDialog]()、
[BaseLeftDialog]()、
[BaseRightDialog]()，
后5个都继承自BaseDialog，
使用方法可以参考[DialogActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/dialog/DialogActivity.kt)。

## 一、BaseDialog
该Dialog是Pandora里所有Dialog封装类的基础，支持配置弹出动画以及阴影设置。
它的弹出位置由系统默认配置决定（一般是正中，也有的系统是默认底部），如果希望指定它的弹出位置，可以使用它的二级继承类。

### 1. 可重写方法
我对onCreate()方法进行了细分，回调顺序按文档描述顺序，大家可以根据自己的需要重写对应的方法，并在内部实现业务逻辑。

方法名称|描述|备注
:---|:---|:---
getLayoutId()|获取布局layoutId|如果使用AnkoLayout这里可以传0
startCreate()|onCreate()中首先回调的方法|一般可以在这个方法内去处理构造函数带进来的参数
findViews()|获取控件|可以在该方法内获取控件对象或者进行控件方法设置
setListeners()|设置监听器|在findViews()之后回调，可以在该方法内统一存放控件的监听器实现方法
initData()|设置数据|在setListeners()之后回调，可以在该方法内统一存放初始数据的配置逻辑
endCreate()|onCreate()中最后回调的方法|如果你还有业务逻辑不适合放在之前任何一个回调方法当中，辣么可以把它放在这里

除此之外，你还可以重写配置弹出动画方法

方法名称|描述|备注
:---|:---|:---
configAnimations()|配置弹出动画|重写该方法，返回你指定的弹框动画


### 2. 可调用方法
我封装了一些常用的内部调用方法，方便大家使用

方法名称|描述|备注
:---|:---|:---
getDialogInterface()|获取DialogInterface对象|无
setElevation(elevation: Float, background: Drawable)|设置阴影|阴影必须有背景色才会生效，因此使用时务必传入背景

## 二、BaseCenterDialog
BaseCenterDialog继承自BaseDialog，限定了Dialog由正中弹出，弹出伴随缩放动画，可配置打开或关闭。

 - 除了BaseDialog可以重写的方法外还新增了配置缩放动画开关的方法

方法名称|描述|备注
:---|:---|:---
hasAnimations()|是否有缩放动画|默认true，关闭false

## 三、BaseBottomDialog
BaseBottomDialog继承自BaseDialog，限定了Dialog由底部弹出，你可以配置是否横向占满屏幕宽度

 - 除了BaseDialog可以重写的方法外，你还可以配置Dialog的宽度

方法名称|描述|备注
:---|:---|:---
isMatchWidth()|是否需要填满宽度|默认true，不填满false

## 四、BaseTopDialog
BaseTopDialog继承自BaseDialog，限定了Dialog由顶部弹出，其他使用方式和BaseBottomDialog一致

## 五、BaseLeftDialog
BaseLeftDialog继承自BaseDialog，限定了Dialog由左侧弹出，你可以配置是否纵向占满屏幕高度

 - 除了BaseDialog可以重写的方法外，你还可以配置Dialog的高度

方法名称|描述|备注
:---|:---|:---
isMatchHeight()|是否需要填满高度|默认true，不填满false

## 六、BaseRightDialog
BaseRightDialog继承自BaseDialog，限定了Dialog由右侧弹出，其他使用方式和BaseLeftDialog一致

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部]()