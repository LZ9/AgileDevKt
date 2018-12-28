# Application基类
你可以继承BaseApplication来订制你的Application。我在内部集成了对基类UI的风格配置以及Activity的统一管控，
使用方法可以参考[App.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/App.kt)。

## 内部重写方法描述
方法名称|描述|备注
:---|:---|:---
onStartCreate()|替代onCreate()方法|可在内部对放入需要执行的初始化逻辑
onExit()|开发者在代码中调用App.exit()方法时会回调该方法|可以在内部放入应用关闭时需要释放的资源等

## 配置方法描述
方法名称|描述|备注
:---|:---|:---
getBaseLayoutConfig()|获取基类UI风格配置器|无
getBaseLayoutConfig().getTitleBarLayoutConfig()|获取标题栏配置器，对标题栏进行定制|可配置标题颜色、背景颜色等等
getBaseLayoutConfig().getErrorLayoutConfig()|获取异常页面配置器，对异常页面进行定制|可配置图标、提示语和背景色等等
getBaseLayoutConfig().getLoadingLayoutConfig()|获取加载页面配置器，对加载页面进行定制|可配置图标、提示语和背景色等等
getBaseLayoutConfig().getNoDataLayoutConfig()|获取无数据页面配置器，对无数据页面进行定制|可配置图标、提示语和背景色等等

## 外部调用方法描述
方法名称|描述|备注
:---|:---|:---
get()|获取Application单例|该方法获取到的是BaseApplication，如果需要获取自己定义的App，可以在App里写一个静态方法把BaseApplication.get()转型成App即可
exit()|需要整个退出应用时调用该方法|调用时内部会关闭你所有继承了AbsActivity的页面，并且回调onExit()方法执行你需要释放资源的逻辑

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_application.md#application基类)
