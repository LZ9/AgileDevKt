# 共享元素封装
我封装了一个共享元素传参的帮助类TransitionHelper，简化共享元素传参调用，
用法可以参考[TransitionActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/transition/TransitionActivity.kt)
和 [ElementActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/transition/ElementActivity.kt)

### 1. 可调用方法
共享元素动画需要再Android5.0及以上才有效果，我在方法内部已经进行了判断，如果是5.0以下的系统就不会调用共享元素的相关方法

方法名称|描述|备注
:---|:---|:---
jumpTransition(activity: Activity, intent: Intent, list: List<Pair<View, String>>)|使用过度动画跳转（多个元素）|list为共享元素列表，Pair的View是共享元素对象，String是元素对应的标签
jumpTransition(activity: Activity, intent: Intent, sharedElement: View, sharedElementName: String)|使用过度动画跳转（单个元素）|View是共享元素对象，String是元素对应的标签
finish(activity: Activity)|使用共享动画关闭页面|无
setTransition(view: View, shareElementName: String)|设置共享元素|View是共享元素对象，String是元素对应的标签
setEnterTransitionDuration(activity: Activity, duration: Long)|设置动画进入的过度时间|单位：毫秒
setReturnTransitionDuration(activity: Activity, duration: Long)|设置动画退出的过度时间|单位：毫秒

### 2. 使用方法
 - 把共享元素装载后启动Activity
```
    val intent = Intent(activity, XxxxActivity::class.java)
    val sharedElements = ArrayList<Pair<View, String>>()//创建共享元素列表
    sharedElements.add(Pair.create(imgView, "img"))//添加共享元素对象和标签
    sharedElements.add(Pair.create(titleView, "title"))
    TransitionHelper.jumpTransition(activity, intent, sharedElements)//启动Activity
```

 - 关闭Activity
```
    TransitionHelper.finish(activity)
```

 - 在跳转过去的Activity里设置共享元素
```
    TransitionHelper.setTransition(titleView, "title")
```

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_transition.md#共享元素封装)