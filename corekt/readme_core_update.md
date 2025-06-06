# core-kt库更新记录

##### 2025/05/30
1. 升级工程
2. 更新依赖库
3. AnkoString.kt添加TextView和EditText获取textString的方法
4. AnkoNumFormat.kt增加15位以内数字转中文的方法
5. 发布2.1.0版

##### 2024/05/13
1. 升级工程
2. 更新依赖库 
3. 发布2.0.8版

##### 2023/09/07
1. 添加DataStore的依赖和扩展类
2. 优化各个扩展类方法
3. 升级依赖库版本
4. 增加IPV4地址列表的获取方法
5. 发布2.0.7版

##### 2023/07/05
1. 更新依赖库
2. 增加Boolean为空的校验方法
3. 发布2.0.6版

##### 2023/07/04
1. AnkoTab.kt添加文字资源的入参
2. 更新依赖库
3. 发布2.0.5版

##### 2023/06/08
1. AnkoIntent.kt增加使用第三方播放音频方法
2. 更新依赖库版本
3. 修改获取音频时长的方法
4. 添加boolean和int互相转换的方法
5. 修改方法支持在任意对象尾部进行文字拼接
6. 发布2.0.4版

##### 2022/10/18
1. 更新依赖库版本
2. 添加getColorStateList兼容方法；
3. 增加TabLayout扩展类，支持配置ViewPager2联动
4. 增加应用角标工具类，支持华为和小米
5. 发布2.0.3版

##### 2022/07/08
1. 升级工程、更新依赖库版本
2. AnkoDrawable.kt增加创建圆角方法
3. SharedPreferencesUtils.kt使用弱关联包裹Context
4. 新增通讯录常用方法封装AnkoContact.kt
5. 新增多媒体资源（相册/音频/视频/文件）查询工具类AnkoMedia.kt
6. AnkoDevice.kt增加获取永久SIM标识符方法
7. AnkoFile.kt增加uri转base64的方法
8. 根据sonar扫描结果修复相关类的BUG 
9. 发布2.0.0版

##### 2022/03/15
1. 更新依赖库版本
2. 优化协程相关扩展类的方法
3. 发布1.4.9版

##### 2022/02/10
1. 更新依赖库版本
2. 升级工程
3. 优化AnkoCoroutines.kt里的方法
4. AnkoString.kt增加ping方法
5. 发布1.4.8版

##### 2021/12/08
1. 更新依赖库版本
2. NotificationUtils.kt增加取消通知方法
3. 修复文件path转URI的BUG
4. 发布1.4.7版

##### 2021/11/05
1. AnkoIntent.kt增加Activity的Intent和Fragment的arguments的委托方法
2. 文件帮助类增加读取文件内容方法和写入文件开头、结尾方法
3. 更新依赖库版本
4. 发布1.4.5版

##### 2021/10/28
1. ACache增加其他常见类型的数据存取
2. 适配android12
3. 添加AnkoByte扩展函数
4. 发布1.4.4版

##### 2021/09/28
1. 修改设置类中的音量相关方法，增加音量类型入参
2. 发布1.4.2版

##### 2021/09/26
1. 增加时间日期格式化类型
2. 去掉kotlin插件
3. 文件帮助类增加获取目录下所有文件的方法
4. 更新依赖库版本
5. 发布1.4.1版

##### 2021/08/19
1. FileUtils.kt增加文件和base64互转的方法
2. 发布1.4.0版

##### 2021/08/12
1. 更新依赖库版本
2. 增加ByteBuffer、ByteArray和ShortArray互转方法
3. 添加列表转为数组方法
4. 修改toast帮助类，支持获取toast对象
5. 去掉android5.0以下的兼容判断
6. 修改APP名称获取方法
7. 更新依赖库版本
8. 发布1.3.9版

##### 2021/06/29
1. 更新依赖库版本
2. 发布1.3.8版

##### 2021/06/15
1. 更新依赖库版本
2. BitmapUtils.kt增加相机数据流转bitmap方法
3. 增加Android相机的YUV420（NV21）帮助类，提供旋转角度方法
4. 发布1.3.7版

##### 2021/04/21
1. 修改域名
2. 迁移发布到mavenCentral
3. 更新依赖库
4. 修改导航栏和状态栏帮助类逻辑
5. 发布1.3.6版

##### 2021/02/24
1. 更新kotlin插件
2. 优化接口方法，支持Lambda语法
3. 发布1.3.5版

##### 2021/01/21
1. AlbumUtils类里的方法添加静态方法注解
2. 发布1.3.3版本

##### 2021/01/20
1. BitmapUtils添加拍照相关处理方法
2. 发布1.3.2版本

##### 2020/12/26
1. MD5增加文件生成方法
2. 发布1.3.0版本

##### 2020/12/16
1. 优化toEditable方法
2. 发布1.2.9版本

##### 2020/12/14
1. 更新依赖库版本
2. FileUtils增加文件路径转uri方法
3. 发布1.2.8版本

##### 2020/11/19
1. Toast扩展类增加扩展方法
2. 文件帮助类增加Uri的拷贝方法
3. 更新依赖库版本
4. 发布1.2.7版本

##### 2020/10/16
1. 优化协程扩展类
2. 发布1.2.5版本

##### 2020/10/15
1. 手机图片转base64帮助方法兼容android10的uri
2. 更新依赖库版本
3. 添加是否亮屏帮助方法
4. 打开APP帮助方法添加启动类的类名入参
5. 协程扩展类添加ViewModel扩展方法
6. 发布1.2.4版本

##### 2020/07/20
1. 更新依赖库版本
2. AnkoCoroutines.kt增加await方法
3. DateUtils.kt增加格式化时间戳
4. NetworkManager.kt增加电信运行商判断逻辑
5. AnkoDevice.kt增加获取AndroidId方法
6. 修复ToastUtils.kt会弹出两次的bug
7. AnkoApp.kt增加获取APP图标的方法
8. AlbumUtils.kt修改图片查询语句，适配android10
9. 发布1.2.2版本

##### 2019/12/13
1. 优化协程扩展类
2. 更新依赖库版本
3. 增加Jetpack依赖
4. 更新Kotlin插件版本
5. 增加String扩展类
6. 发布1.2.1版本

##### 2019/11/20
1. 更新协程依赖库版本
2. AnkoScreen增加获取手机屏幕真实高度方法
3. AnkoCoroutines去掉主线程挂起函数
4. AnkoNumFormat修复数字转中文的BUG
5. UiHandler添加Deprecated注解
6. StringUtils增加数组拼接分隔符方法
7. 更新Kotlin插件版本
8. 增加SharedPreferences的工具类
9. 发布1.1.3版本

##### 2019/10/31
1. 适配Android10
2. 优化代码
3. 发布1.1.2版本

##### 2019/09/25
1. 添加NFC帮助方法
2. 更新kotlin插件版本
3. 修改日志分段长度
4. 更新依赖库版本
5. 发布1.1.1版本

##### 2019/07/16
1. 修复GPS开关判断BUG
2. 添加协程依赖和扩展类AnkoCoroutines
3. 重构优化AnkoDimensions
4. UiHandler增加lambda入参方法
5. AnkoDrawable增加View扩展
6. 重构优化ReflectUtils代码
7. 重构优化AnkoViewBinder代码
8. 新增注解扩展类AnkoAnnotation
9. 修改SnackbarUtils的调用方法名
10. 优化代码，增加方法重载注解@JvmOverloads
11. 更新依赖库版本
12. 更新kotlin插件版本
13. AnkoApp添加对APP堆栈情况的判断方法
14. 发布1.1.0版本

##### 2019/05/06
1. 数组列表帮助类增加判空辅助方法
2. 添加日期格式类型
3. 静态方法添加JvmStatic注解
4. 添加UUID的获取方法
5. 发布1.0.10版本

##### 2019/04/02
1. 优化日期帮助类
2. 发布1.0.9版本

##### 2019/03/22
1. 更新依赖库版本
2. 日期帮助类增加日期选择器和时间选择器弹框
3. 数字格式帮助类添加数字转中文方法
4. 发布1.0.8版本

##### 2019/02/25
1. 更新Kotlin插件版本
2. 修复选择器帮助类BUG
3. 发布1.0.5版本

##### 2019/02/01
1. 更新Kotlin插件版本
2. BitmapUtils添加长图分段截取方法
3. 修复编译器入参范围注解BUG
4. 优化Anko扩展类
5. 发布1.0.4版本

##### 2018/12/27
1. AppUtils添加wifi判断方法
2. DeviceUtils增加root判断
3. 在PopupWindow里增加控件绑定扩展方法
4. 在AnkoArray里增加ArrayList的转换方法
5. 修复AlbumUtils里的bug
6. AnkoIntent增加拍照方法扩展
7. 更新依赖版本
8. 发布1.0.2版本

##### 2018/11/08
1. 将core库转为kotlin
2. 根据kotlin特性重构代码
3. 发布1.0.1版本

## 扩展
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core_update.md#core-kt库更新记录)
- [AgileDevKt 主页](https://github.com/LZ9/AgileDevKt)
- [了解 core-kt](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md)
- [了解 Pandora](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [了解 imageloader-kt](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md)
