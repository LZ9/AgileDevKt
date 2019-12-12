# 订阅封装
我将Observable、Flowable、Single、Maybe和Completable对应的订阅器都做了基础封装，
大家可以根据特定的需要使用对应订阅器封装类

### 1. Observable的订阅器Observer
Observable能够发射0或n个数据，即onNext()可以回调多次，并以完成onComplete()或错误onError()终止。
使用方式可以参考[RxObservableActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/rxjava/observable/RxObservableActivity.kt)

#### 1）基础封装BaseObserver
BaseObserver实现了Observer，内部对异常进行了日志打印，并且可以便捷的获取Disposable对象

方法名称|描述|备注
:---|:---|:---
getDisposable()|获取Disposable|对象可能为null
clearDisposable()|清空Disposable对象|无
dispose()|停止订阅|无
onBaseSubscribe()|订阅起始时回调|可重写
onBaseNext()|数据回调|需要开发者实现
onBaseError()|异常回调|需要开发者实现
onBaseComplete()|完成回调|可重写
onDispose()|调用dispose()时回调|可重写
empty()|方法实现均为空的|可静态调用BaseObserver.empty()

 - 在AndroidManifest.xml里配置异常日志的打印标签
```
    <application
        ......>

        <!--订阅日志标签-->
        <meta-data
           android:name="error_tag"
           android:value="你自定义的日志标签" />

        ......
        
    </application>
```

 - 使用时通常只需要实现onBaseNext()和onBaseError()即可，onBaseSubscribe()和onBaseComplete()根据业务需要开发者可以自己重写实现
 ```
    Observable.just("")
        .subscribe(object :BaseObserver<String>(){
            override fun onBaseNext(any: String) {
                //处理数据结果
            }
            override fun onBaseError(e: Throwable) {
                //处理异常结果             
            }
    })
 ```
 
 - 无需处理的订阅可以直接调用BaseObserver.empty()完成
```
    Observable.just("").subscribe(BaseObserver.empty())
```

#### 2）接口数据封装RxObserver
RxObserver继承了BaseObserver，内部对实现ResponseStatus接口的数据类进行了判断处理，
当isSuccess()为false时，会回调onRxError()

方法名称|描述|备注
:---|:---|:---
onRxSubscribe()|订阅起始时回调|可重写
onRxNext()|数据回调|需要开发者实现
onRxError()|异常回调|需要开发者实现
onRxComplete()|完成回调|可重写
onErrorEnd()|onRxError()回调后回调该方法|可重写

 - 使用时通常只需要实现onRxNext()和onRxError()即可，onRxSubscribe()和onRxComplete()根据业务需要开发者可以自己重写实现
```
    // 假设ResponseBean实现了ResponseStatus接口
    val responseBean = ResponseBean<String>()
    Observable.just(responseBean)
        .subscribe(object :RxObserver<ResponseBean<String>>(){
            override fun onRxNext(any: ResponseBean<String>) {
                //处理数据结果
            }
    
            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                //处理异常结果
            }
    })
```

#### 3）带加载框的接口数据封装ProgressObserver
ProgressObserver继承了RxObserver，内部添加了加载框实现，开发者可以根据自己的需要配置加载框的样式，可以单独配置也可以继承该类进行配置

方法名称|描述|备注
:---|:---|:---
onPgSubscribe()|订阅起始时回调|可重写
onPgNext()|数据回调|需要开发者实现
onPgError()|异常回调|需要开发者实现
onPgComplete()|完成回调|可重写
onPgCancel()|用户取消加载框|可重写
create()|创建加载框|开发者可以根据需要选择不同入参的重载方法调用

 - 使用时通常只需要实现onPgNext()和onPgError()即可，onPgSubscribe()和onPgComplete()根据业务需要开发者可以自己重写实现
```
    // 假设ResponseBean实现了ResponseStatus接口
    val responseBean = ResponseBean<String>()
    Observable.just(responseBean)
        .subscribe(object : ProgressObserver<ResponseBean<String>>() {
            override fun onPgNext(any: ResponseBean<String>) {
                //处理数据结果
            }
            
            override fun onPgError(e: Throwable, isNetwork: Boolean) {
                //处理异常结果
            }
        }.create(getContext(), "loading", false, false))// 一定要调用create()方法才会显示加载框
```

方法名称|描述|备注
:---|:---|:---
getArgs()|获取构造函数内的不定入参|入参数组
doNext()|封装ObservableEmitter里的onNext()，增加订阅停止判断|无
doError()|封装ObservableEmitter里的onError()，增加订阅停止判断|无
doComplete()|封装ObservableEmitter里的onComplete()，增加订阅停止判断|无

### 2. Flowable的订阅器Subscriber
Flowable能够发射0或n个数据，即onNext()可以回调多次，并以完成onComplete()或错误onError()终止。
支持Backpressure，可以控制数据源发射的速度。也支持下游调用request()方法来限制接收数据数
使用方式可以参考[RxFlowableActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/rxjava/flowable/RxFlowableActivity.kt)

#### 1）基础封装BaseSubscriber
BaseSubscriber实现了Subscriber，内部对异常进行了日志打印，并且可以便捷的获取Subscription对象

方法名称|描述|备注
:---|:---|:---
getSubscription()|获取Subscription|对象可能为null
clearSubscription()|清空Subscription对象|无
cancel()|停止订阅|无
request()|请求订阅数|可多次调用，控制下游的数据接收数量
onBaseSubscribe()|订阅起始时回调|可重写
onBaseNext()|数据回调|需要开发者实现
onBaseError()|异常回调|需要开发者实现
onBaseComplete()|完成回调|可重写
onCancel()|调用cancel()时回调|可重写
isAutoSubscribe()|是否自动订阅，默认是，否的时候需要自己调用request()方法订阅|可重写配置
empty()|方法实现均为空的|BaseSubscriber.empty()

 - 在AndroidManifest.xml里配置异常日志的打印标签
```
    <application
        ......>

        <!--订阅日志标签-->
        <meta-data
           android:name="error_tag"
           android:value="你自定义的日志标签" />

        ......
        
    </application>
```

 - 使用时通常只需要实现onBaseNext()和onBaseError()即可，onBaseSubscribe()和onBaseComplete()根据业务需要开发者可以自己重写实现
 ```
    Flowable.just("")
        .subscribe(object :BaseSubscriber<String>(){
            override fun onBaseNext(any: String) {
                //处理数据结果
            }
        
            override fun onBaseError(e: Throwable) {
                //处理异常结果  
            }
        })
 ```
 
 - 无需处理的订阅可以直接调用BaseSubscriber.empty()完成
```
    Flowable.just("").subscribe(BaseSubscriber.empty())
```

#### 2）接口数据封装RxSubscriber
RxSubscriber继承了BaseSubscriber，内部对实现ResponseStatus接口的数据类进行了判断处理，
当isSuccess()为false时，会回调onRxError()

方法名称|描述|备注
:---|:---|:---
onRxSubscribe()|订阅起始时回调|可重写
onRxNext()|数据回调|需要开发者实现
onRxError()|异常回调|需要开发者实现
onRxComplete()|完成回调|可重写
onErrorEnd()|onRxError()回调后回调该方法|可重写

 - 使用时通常只需要实现onRxNext()和onRxError()即可，onRxSubscribe()和onRxComplete()根据业务需要开发者可以自己重写实现
```
    // 假设ResponseBean实现了ResponseStatus接口
    val responseBean = ResponseBean<String>()
    Flowable.just(responseBean)
        .subscribe(object :RxSubscriber<ResponseBean<String>>(){
            override fun onRxNext(any: ResponseBean<String>) {
                //处理数据结果
            }

            override fun onRxError(e: Throwable, isNetwork: Boolean) {
                //处理异常结果
            }
        })
```

#### 3）带加载框的接口数据封装ProgressSubscriber
ProgressSubscriber继承了RxSubscriber，内部添加了加载框实现，开发者可以根据自己的需要配置加载框的样式，可以单独配置也可以继承该类进行配置

方法名称|描述|备注
:---|:---|:---
onPgSubscribe()|订阅起始时回调|可重写
onPgNext()|数据回调|需要开发者实现
onPgError()|异常回调|需要开发者实现
onPgComplete()|完成回调|可重写
onPgCancel()|用户取消加载框|可重写
create()|创建加载框|开发者可以根据需要选择不同入参的重载方法调用

 - 使用时通常只需要实现onPgNext()和onPgError()即可，onPgSubscribe()和onPgComplete()根据业务需要开发者可以自己重写实现
```
    // 假设ResponseBean实现了ResponseStatus接口
    val responseBean = ResponseBean<String>()
    Flowable.just(responseBean)
        .subscribe(object :ProgressSubscriber<ResponseBean<String>>(){
            override fun onPgNext(any: ResponseBean<String>) {
                //处理数据结果
            }
        
            override fun onPgError(e: Throwable, isNetwork: Boolean) {
                //处理异常结果
            }
        }.create(getContext(), "loading", false, false))// 一定要调用create()方法才会显示加载框
        
```
 
方法名称|描述|备注
:---|:---|:---
getArgs()|获取构造函数内的不定入参|入参数组
doNext()|封装FlowableEmitter里的onNext()，增加订阅停止判断|无
doError()|封装FlowableEmitter里的onError()，增加订阅停止判断|无
doComplete()|封装FlowableEmitter里的onComplete()，增加订阅停止判断|无


## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/rx/pandora_rx.md)
- [回到顶部]()