# Pandora
Pandora是基于Kotlin实现的敏捷开发解决方案，里面包括了APP基础开发需要用到的大部分基类、控件和工具。
你可以直接引用Pandora进行上层UI和业务逻辑开发或者从中抽取你需要模块代码支援你的工程。
（如果你需要Java的敏捷开发解决方案可以参考[AgileDev](https://github.com/LZ9/AgileDev)）

## 内部依赖
Pandora支持Androidx和Kotlin开发插件，内部集成了包括
[Rxjava2](https://github.com/ReactiveX/RxJava)、
[Retrofit2](https://github.com/square/retrofit)、
[Fastjson](https://github.com/alibaba/fastjson)、
[EventBus](https://github.com/greenrobot/EventBus)、
[PhotoView](https://github.com/chrisbanes/PhotoView)、
[core-kt](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md)
这些常用的工具组件，
如果你的app有重复引用可以选择去掉顶层引用或者保证版本一致
```
    dependencies {
        api 'com.lodz:core-kt:1.0.2'
        api 'io.reactivex.rxjava2:rxjava:2.2.4'
        api 'io.reactivex.rxjava2:rxandroid:2.1.0'
        api 'com.squareup.retrofit2:retrofit:2.5.0'
        api 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
        api 'com.alibaba:fastjson:1.2.54'
        api 'com.trello.rxlifecycle3:rxlifecycle-components:3.0.0'
        api 'org.greenrobot:eventbus:3.1.1'
        api 'com.github.chrisbanes:PhotoView:2.0.0'
    }
```

## 目录
- [Application基类](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_application.md)
- [Activity基类](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_activity.md)
- [Fragment基类](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_fragment.md)
- 图片选择和预览[待补充]()
- RxJava封装[待补充]()
- ACache封装[待补充]()
- 加载框封装[待补充]()
- 共享元素封装[待补充]()
- 状态控件基类[待补充]()
- BottomSheet封装[待补充]()
- [Dialog封装](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_dialog.md)
- [DialogFragment封装](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_dialogfragment.md)
- PopupWindow封装[待补充]()
- 自定义控件封装[待补充]()
- 九宫格封装[待补充]()
- RecyclerView封装[待补充]()

## 扩展

- [更新记录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora_update.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md#pandora)
- [AgileDevKt 主页](https://github.com/LZ9/AgileDevKt)
- [了解 core-kt](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md)
- [了解 imageloader-kt](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md)
