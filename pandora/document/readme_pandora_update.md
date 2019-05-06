# Pandora更新记录

##### 2019/05/06
1. 静态方法添加JvmStatic注解
2. 更新依赖库版本
3. 发布1.1.7版本

##### 2019/04/02
1. 更新RxJava版本
2. 修复粘黏装饰器展示BUG
3. 优化采集控件类
4. Dialog和FragmentDialog支持Rx生命周期
5. 发布1.1.6版本

##### 2019/03/22
1. 更新依赖库版本
2. 添加采集控件
3. 修复Json转换器BUG
4. 发布1.1.5版本

##### 2019/02/25
1. 更新rxandroid版本
2. 添加底部菜单栏控件
3. 发布1.1.2版本

##### 2019/02/01
1.  修复BaseApplication内部获取时的判空BUG
2.  修复Activity回收后的数据保存BUG
3.  Fragment基类支持AnkoLayout
4.  重构加载框帮助类ProgressDialogHelper，改为链式调用
5.  Activity和Fragment基类增加Rx生命周期快捷绑定
6.  RxUtils增加Completable的线程切换方法、修复文本延迟回调BUG、增加获取ResponseStatus方法
7.  修改拍照管理类TakePhotoManager数据回调接口
8.  优化Progress订阅者的加载库创建方法
9.  RxObservableOnSubscribe增加订阅停止判断的辅助方法
10. 增加RxFlowableOnSubscribe、RxSingleOnSubscribe、RxMaybeOnSubscribe和RxCompletableOnSubscribe
11. 所有订阅者增加静态空方法
12. 增加Single相关的订阅者封装
13. 增加Maybe相关的订阅者封装
14. 增加Completable的订阅封装
15. 更新rxjava、fastjson和kotlin插件版本
16. 增加长图加载控件
17. 图片预览器增加AbsImageView抽象类，支持开发者自定义图片控件来加载
18. 优化PickerManager、PreviewManager和SimpleNineGridView支持AbsImageView
19. 发布1.1.1版本

##### 2018/12/27
1. 将component库转为kotlin
2. 根据kotlin特性重构代码
3. 发布1.0.0版本

## 扩展
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core_update.md#pandora更新记录)
- [AgileDevKt 主页](https://github.com/LZ9/AgileDevKt)
- [了解 core-kt](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md)
- [了解 Pandora](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [了解 imageloader-kt](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md)
