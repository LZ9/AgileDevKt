# Pandora更新记录

##### 2025/05/30
1. 更新依赖库版本
2. ExceptionFactory.kt里的createDataException增加默认提示语入参
3. AnkoJson.kt添加String转json的格式化方法
4. 发布2.1.0版

##### 2024/05/14
1. 更新依赖库版本
2. 发布2.0.8版

##### 2023/09/07
1. 优化装饰器代码
2. AnkoRv.kt增加装饰器相关扩展方法
3. 优化自定义键盘相关代码
4. 新增jackson的解析转换类并升级jackson依赖的版本
5. 增加jackson前缀修复类PrefixFixSerializer.kt
6. BaseApplication.kt增加dataStore的对象调用
7. 添加DataStore的Rx扩展类
8. 升级Rxjava依赖库
9. 发布2.0.7版

##### 2023/07/05
1. SimpleVpAdapter增加无标题入参
2. 发布2.0.6版

##### 2023/07/04
1. RV的setupData方法增加上下文回调
2. 增加VP适配器基础封装类
3. 添加水印控件和水印快捷添加方法（包括Activity、Dialog、DialogFragment、PopupWindow）
4. BaseApplication增加水印文字全局配置
5. 发布2.0.5版

##### 2023/06/09
1. 新增吸边控件AdsorbView
2. 更新依赖库版本并进行代码适配
3. 如果json字符串为空时，parseJsonObject返回null
4. 发布2.0.4版

##### 2022/10/18
1. 添加侧滑菜单控件
2. 添加RV侧滑适配器基类
3. 优化拖拽帮助类
4. 修复RV树结构适配器点击组节点没有回调监听器的问题
5. 异常提示语工具类增加DataException的message数据判断
6. 发布2.0.3版

##### 2022/09/23
1. 根据sonar修复代码逻辑
2. 增加RV树结构适配器基类
3. 更新依赖库
4. 基类增加状态控件判断方法
5. 基类增加设置刷新状态方法
6. 发布2.0.1版

##### 2022/07/08
1. 升级工程、更新依赖库
2. 修复fragment懒加载配置项未生效的BUG
3. 重构拍照管理模块
4. 加载控件LoadingLayout.kt改为使用默认加载图标
5. 重构图片预览器（内部改为VP2实现）
6. 重构九宫格相关控件
7. 重构图片选择器为文件选择器，支持相册图片/音频/视频/指定后缀文件/自定义对象的筛选
8. 将fastjson替换为jackson，新增AnkoJson.kt工具类，BaseApplication.kt增加jackson默认配置
9. 扩展RV适配器，增加DataVBViewHolder和DataViewHolder的封装
10. 增加AnkoRv.kt帮助类，消除模板代码，快速完成RV和对应的适配器构建
11. 根据sonar扫描结果修复各个类的BUG
12. 压缩资源包里的图片大小
13. 发布2.0.0版

##### 2022/03/15
1. 优化协程请求的封装
2. 发布1.8.7版

##### 2022/02/10
1. 更新依赖库
2. 升级工程
3. 优化AnkoCoroutinesRes.kt方法
4. 修改ViewModel基类使用方式
5. 新增ParamViewModelFactory.kt，支持创建构造函数携带参数的ViewModel类
6. 发布1.8.6版

##### 2021/12/10
1. 更新依赖库
2. 图片选择器加上生命周期回调
3. 图片选择器增加手动关闭的方法
4. 发布1.8.5版

##### 2021/11/17
1. 更新依赖库
2. NineGridAdapter改为公共类
3. 发布1.8.1版

##### 2021/10/28
1. 适配android12
2. 更新依赖库
3. 修复RX相关的继承类泛型报错
4. 发布1.7.9版

##### 2021/09/28
1. 更新依赖库
2. 发布1.7.7版

##### 2021/09/26
1. GlobalScope替换为MainScope()
2. 更新依赖库版本
3.BottomMenuBar增加获取当前选中的menu类型方法
4. 发布1.7.6版

##### 2021/08/19
1. BottomMenuBar增加加文字配置方法
2. 更新依赖库版本
3. 发布1.7.4版

##### 2021/08/17
1. BottomMenuBar增加指示器配置
2. 发布1.7.3版

##### 2021/08/12
1. 框架增加ViewBinding支持
2. 增加AnkoViewBinding.kt扩展函数
3. 修复BaseNavigationView.kt里的反射BUG
4. 增加NestedScrollableHost.kt支持ViewPager2的嵌套滑动
5. 增加BottomMenuBar.kt的配置方法，支持自定义角标和图片
6. 修复粘黏分组标签装饰器分组中只有1条数据时没有过度动画的BUG
7. 去掉android5.0以下的兼容判断
8. 更新依赖库版本
9. 发布1.7.0版

##### 2021/06/29
1. 修复CameraHelper.kt代码里尺寸匹配逻辑问题
2. 发布1.6.7版

##### 2021/06/15
1. 优化CameraHelper.kt代码
2. 发布1.6.6版

##### 2021/05/14
1. 优化CameraHelper.kt的代码，增加状态回调
2. 发布1.6.5版

##### 2021/04/21
1. 修改域名
2. 迁移发布到mavenCentral
3. 更新依赖库
4. 修复BottomSheet的bug
5. 优化CameraHelper.kt的代码
6. 发布1.6.3版

##### 2021/02/24
1. 优化CameraHelper类
2. 优化图片选择、预览和拍照管理类
3. 优化接口方法，支持Lambda语法
4. 更新依赖库版本
5. 添加自定义键盘（身份证、通用证件、车牌）
6. 发布1.6.1版本

##### 2021/01/21
1. CameraHelper添加控制闪关灯方法
2. 发布1.5.7版本

##### 2021/01/20
1. 更新依赖库版本
2. 发布1.5.6版本

##### 2020/12/26
1. 修改异常封装类，不再使用自定义的异常来包裹
2. 发布1.5.5版本

##### 2020/12/16
1. 优化RecyclerView拖拽帮助类
2. 更新core版本
3. 发布1.5.3版本

##### 2020/12/14
1. 更新core版本
2. 图片选择器和拍照器取消时也增加空数据回调
3. 增加MVP模式的封装类
4. 发布1.5.1版本

##### 2020/11/19
1. 更新core版本
2. 更新依赖库版本
3. 添加镂空布局控件和扫描控件
4. 发布1.5.0版本

##### 2020/10/19
1. 修复布局id重复引起的控件加载异常BUG
2. 修改协程扩展函数入参名称
3. 基类增加本目录布局对象获取
4. 发布1.4.8版本

##### 2020/10/16
1. 协程扩展和Rx订阅类增加token验证失败回调
2. 改回使用fastjson，修复fastjson转换类bug
3. 发布1.4.4版本

##### 2020/10/15
1. 手机图片转base64帮助方法兼容android10的uri
2. 更新依赖库版本
3. 最低兼容到android5.0
4. ViewModel里设置RxJava的生命周期
5. 协程增加接口判断方法
6. 移除fastjson改用gson
7. 发布1.4.2版本

##### 2020/07/20
1. 更新依赖库版本
2. 添加ViewPager2相关封装
3. LazyFragment增加lifecycle判断懒加载逻辑
4. 增加可订阅滚动事件的ObservableScrollView.kt
5. NineGridView.kt九宫格空间增加间距属性
6. 调整基类整体适配RxJava3
7. 修改ViewModelProvider的调用方式
8. 添加通用数据ViewHolder，消除模板代码
9. 适配android10文件存储和展示
10. 删除图片文件增加权限提醒；
11. 图片选择器更新新增拍照图片兼容android10
12. Acache存储兼容android10
13. BottomSheetDialog兼容全面屏展示
14. 发布1.4.0版本

##### 2019/12/13
1. Rx订阅者封装增加lambda调用
2. 增加协程加载框扩展类，支持带progress的调用
3. 增加Activity和Fragment的MVVM基类封装
4. 增加Rx扩展类，去掉Rx的create相关的继承类
5. 增加Rx转换代理，支持rx和同步切换
6. 更新依赖库版本
7. 代码优化
8. 发布1.3.0版本

##### 2019/11/20
1. 修复SearchTitleBarLayout配置默认文字清空按钮不显示的BUG
2. 更新rxjava版本
3. 修改组件库变量名，避免外部调用时异常
4. 更新retrofit版本
5. 使用协程替换UiHandler
6. BaseRecyclerViewAdapter的setData方法入参允许为null
7. 发布1.2.1版本

##### 2019/10/31
1. 带基础状态控件的Activity和Fragment的showStatusError()增加重载方法，支持判断数据异常或网络异常
2. 加载失败控件ErrorLayout增加网络异常图标和设置方法
3. 加载控件LoadingLayout修改默认加载动图
4. 更新rxjava和rxlifecycle版本
5. 适配Android10
6. 优化代码
7. 增加自定义底部导航栏BaseNavigationView
8. 图片选择器修改为异步获取图片文件夹目录
9. 修改搜索标题栏，增加标题栏样式，增加搜索联想列表展示配置
10. 发布1.2.0版本

##### 2019/09/25
1. 修改基础控件的默认图片和逻辑
2. 更新依赖库版本
3. 发布1.1.9版本

##### 2019/07/16
1. 优化BottomMenuBar代码
2. 优化CltTextView代码
3. 增加CltEditView采集输入框
4. 修改单位转换方法和调用方式
5. 重构优化ScrollEditText，修复滚动bug
6. 优化UiHandler调用的方法
7. 增加CltRadioGroup单选组控件
8. Rx相关订阅器增加lambda方法调用
9. 修复无数据页面提示语属性获取BUG
10. 修改标题栏扩展区是否展示属性赋值顺序
11. 增加带进度条的PgWebView
12. 增加方法重载注解@JvmOverloads
13. 优化工程代码
14. 更新依赖库版本
15. 发布1.1.8版本

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
