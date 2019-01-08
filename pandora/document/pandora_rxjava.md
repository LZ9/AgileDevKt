# RxJava封装
我在RxUtils里对一些常用的RxJava方法进行了封装，大家可以通过静态调用来使用订阅。
使用方法可以参考[RxJavaTestActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/rxjava/RxJavaTestActivity.kt)。

### 1. 起始线程和订阅线程的封装

方法名称|描述|备注
:---|:---|:---
ioToMainObservable()|在异步线程发起，在主线程订阅|支持Observable
ioToMainFlowable()|在异步线程发起，在主线程订阅|支持Flowable
ioToMainMaybe()|在异步线程发起，在主线程订阅|支持Maybe
ioToMainSingle()|在异步线程发起，在主线程订阅|支持Single
ioToMainCompletable()|在异步线程发起，在主线程订阅|支持Completable

 - 调用方法参考
```
    Observable.interval(1, TimeUnit.SECONDS)
        .compose(RxUtils.ioToMainObservable())//设置线程状态
        .subscribe(BaseObserver.empty())
```

### 2. 获取订阅者的异常提示语和异常对象
 - 该方法可以配合RxObserver、ProgressObserver、RxSubscriber、ProgressSubscriber使用

方法名称|描述|备注
:---|:---|:---
getExceptionTips(e: Throwable, isNetwork: Boolean, defaultTips: String)|获取异常的提示语|提示语优先级：网络异常>后台msg>默认提示语
getNetworkExceptionTips(e: Throwable, isNetwork: Boolean, defaultTips: String)|获取网络异常的提示语|提示语优先级：网络异常>默认提示语
getResponseStatus(e: Throwable)|获取接口数据状态|数据接收实体必须实现ResponseStatus接口

 - 调用方法参考
```
    Observable.interval(1, TimeUnit.SECONDS)
        .compose(RxUtils.ioToMainObservable())
        .subscribe(object :RxObserver<Long>(){
            override fun onRxNext(any: Long) {
                // do something
            }
            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                val tips = RxUtils.getExceptionTips(e, isNetwork, "error")//获取异常提示语
            }
        })
```

 - 配合ResponseStatus接口使用

你的接收对象封装类可以实现ResponseStatus接口，然后实现里面的3个方法，
在RxObserver、ProgressObserver、RxSubscriber、ProgressSubscriber这4个方法里均会对ResponseStatus的实现类进行判断，
如果isSuccess()是false，会直接从onError方法里返回，并且将valueMsg()方法里的文字传进异常里，
然后就可以通过getExceptionTips()或者getNetworkExceptionTips()获取。

```
// 实现ResponseStatus的数据封装类例子
open class ResponseBean<T> : ResponseStatus {
    companion object {
        const val SUCCESS = 1
        const val FAIL = 2
    }

    /** 状态 */
    var code = FAIL
    /** 信息 */
    var msg = ""
    /** 数据内容 */
    var data: T? = null

    override fun isSuccess(): Boolean = code == SUCCESS

    override fun valueMsg(): String = msg

    override fun valueStatus(): String = code.toString()
}
```

 - 如何在onError里获取后台返回的对象

假设你的接收对象的实体类叫ResponseBean并且实现了ResponseStatus接口，你可以通过getResponseStatus()方法来获取后台返回失败的数据对象
```
    Observable.interval(1, TimeUnit.SECONDS)
        .compose(RxUtils.ioToMainObservable())
        .subscribe(object :RxObserver<Long>(){
            override fun onRxNext(any: Long) {
                // do something
            }
            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                val responseBean = RxUtils.getResponseStatus(e)
                if (responseBean is ResponseBean){
                    responseBean.xxxxx//do something
                }
            }
        })
```

### 3. 图片路径编码Base64
支持单张图片和多张图片同时编码，如果图片太多编码是需要时间的，建议异步进行订阅

方法名称|描述|备注
:---|:---|:---
decodePathToBase64(path: String, widthPx: Int, heightPx: Int)|编码单张图片路径|widthPx和heightPx指压缩后的宽高，建议按图片比例进行压缩
decodePathToBase64(paths: ArrayList<String>, widthPx: Int, heightPx: Int)|编码多张图片路径|widthPx和heightPx指压缩后的宽高，建议按图片比例进行压缩

调用方法参考
```
    // 按屏幕宽高的八分之一比例进行压缩
    RxUtils.decodePathToBase64(path, getScreenWidth() / 8, getScreenHeight() / 8)
        .compose(RxUtils.ioToMainObservable())
        .compose(bindDestroyEvent())
        .subscribe(object :ProgressObserver<String>(){
            override fun onPgNext(any: String) {
                val tips = "Base64路径：$any"
            }

            override fun onPgError(e: Throwable, isNetwork: Boolean) {

            }
        }.create(getContext(), R.string.rx_pic_coding, true))
```

### 4. 防抖点击
防止用户在短时间内点击按钮多次，导致多次回调点击事件

方法名称|描述|备注
:---|:---|:---
viewClick(view: View, duration: Long = 1, unit: TimeUnit = TimeUnit.SECONDS)|在指定秒数内只返回一次点击事件|默认1秒内

调用方法参考
```
    // 500毫秒内只返回一次点击事件
    RxUtils.viewClick(mQuickClickBtn, 500, TimeUnit.MILLISECONDS)
        .subscribe(object :BaseObserver<View>(){
            override fun onBaseNext(any: View) {
                //do something
            }

            override fun onBaseError(e: Throwable) {

            }
        })
```

### 5. 文本变动延迟回调
可用于搜索框的关键字联想

方法名称|描述|备注
:---|:---|:---
textChanges(textView: TextView, duration: Long = 500, unit: TimeUnit = TimeUnit.MILLISECONDS)|文本变动延迟回调|默认变动后500秒回调一次

调用方法参考
```
    // 文本变更后300毫秒回调一次输入内容
    RxUtils.textChanges(mSearchEdit, 300, TimeUnit.MILLISECONDS)
        .compose(RxUtils.ioToMainObservable())
        .compose(bindDestroyEvent())
        .subscribe(object :BaseObserver<CharSequence>(){
            override fun onBaseNext(any: CharSequence) {
                 val tips = "输入框内容：$any"
            }

            override fun onBaseError(e: Throwable) {

            }
        })
```

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_rxjava.md#rxjava封装)