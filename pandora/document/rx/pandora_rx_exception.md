# 异常封装
我将订阅的异常做了包装和分类，方便根据类型来做处理。

### 1. 异常基类RxException
RxException继承Exception，可携带自定义的异常信息文字

### 2. 网络异常基类NetworkException
NetworkException继承RxException，底下区分了两个子类，一个是网络未连接异常NetworkNoConnException，
还有一个是网络超时异常NetworkTimeOutException。

子类名称|描述
:---|:---
NetworkNoConnException|网络未连接异常
NetworkTimeOutException|网络超时异常

### 3. 数据异常DataException
DataException继承RxException，内部支持存储ResponseStatus对象，用户可以将获取到的ResponseStatus转型为它的封装子类，
来获取数据包装实体。

### 4. 异常包装工厂RxExceptionFactory
可以将Throwable传入异常工厂，内部会对其进行判断和包装，返回对应的RxException或RxException子类

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/rx/pandora_rx.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/rx/pandora_rx_exception.md#异常封装)