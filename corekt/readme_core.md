# core-kt库
这个是最底层的核心依赖库，主要用来放androidx库和通用的帮助类

## 目录
- [1、涉及的androidx和kotlin依赖](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#1涉及的androidx和kotlin依赖)
- [2、日志类PrintLog](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#2日志类printlog)
- [3、网络状态类NetworkManager](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#3网络状态类networkmanager)
- [4、加密相关](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#4加密相关)
- [5、线程池ThreadPoolManager](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#5线程池threadpoolmanager)
- [6、缓存Cache](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#6缓存cache)
- [7、通知帮助类NotificationUtils](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#7通知帮助类notificationutils)
- [8、各种通用工具类](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#8各种通用工具类)
- [扩展](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#扩展)

## 1、涉及的androidx和kotlin依赖
该库引用了下方这些support库，如果您的app有重复引用可以选择去掉顶层引用或者保证版本一致
```
    dependencies {
        api 'androidx.appcompat:appcompat:1.2.0'
        api 'com.google.android.material:material:1.2.0'
        api 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
        api 'androidx.recyclerview:recyclerview:1.1.0'
        api 'androidx.cardview:cardview:1.0.0'
        api "androidx.viewpager2:viewpager2:1.0.0"
        api 'androidx.annotation:annotation:1.1.0'
        api 'androidx.constraintlayout:constraintlayout:2.0.1'
        api 'androidx.core:core-ktx:1.3.1'
        api 'androidx.lifecycle:lifecycle-extensions:2.2.0'
        api 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
        api 'com.google.android:flexbox:2.0.1'
        api 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'
        api 'org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.0'
    }
```

## 2、日志类PrintLog
PrintLog主要封装了日志的打印开关，小伙伴可以在app里的build.gradle文件里添加日志开关的变量，如下所示：
```
    android {
        ....

        def LOG_DEBUG = "LOG_DEBUG"
        defaultConfig {
            buildConfigField "boolean", LOG_DEBUG, "true"
        }

        buildTypes {
            debug {
                .....
            }
            release {
                buildConfigField "boolean", LOG_DEBUG, "false"
                .....
            }
        }
    }
```
在application的初始化中可以配置该变量，起到开关日志的效果
```
    PrintLog.setPrint(BuildConfig.LOG_DEBUG)
```

## 3、网络状态类NetworkManager
网络管理采用单例模式，需要在application里初始化该类，如下所示
```
    NetworkManager.get().init(Context)
```
在退出应用的时候释放资源并且清除添加的网络状态回调监听器
```
    NetworkManager.get().release(Context)
    NetworkManager.get().clearNetworkListener()
```
如果你需要监听网络状态变化，可以增加一个监听器，如下所示：
```
    NetworkManager.get().addNetworkListener(NetworkManager.NetworkListener)
```
如果不需要再使用了可以移除掉这个监听器，你可以选择用完就立刻移除，也可以选择在退出app时统一移除
```
    NetworkManager.get().removeNetworkListener(NetworkManager.NetworkListener)
```
以下为常用方法，具体可以参见代码注释
```
    NetworkManager.get().isNetworkAvailable()
    NetworkManager.get().isWifi()
    NetworkManager.get().getNetType()
    NetworkManager.get().getSimOperator()
    NetworkManager.get().getOperatorInfo()
```
记得加入下面两个权限，确保功能正常使用
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
## 4、加密相关
目前只收入了4个加密相关类，后续如果有用到其他的或者小伙伴们有需要会再陆续补充
- AES
- MD5
- SHA1
- RSA

## 5、线程池ThreadPoolManager
1)我为大家准备了3个优先级的线程池（高、中、低），当执行该线程池时才会创建对应的线程池对象，调用方法分别如下：
```
    ThreadPoolManager.get().executeHighest(Runnable)
    ThreadPoolManager.get().executeNormal(Runnable)
    ThreadPoolManager.get().executeLowest(Runnable)
```

2)如果希望订制线程池的配置，可以使用下面的方法（）
```
    ThreadPoolManager.get().newBuilder()
        .setAwaitTime(50)// 设置线程结束等待时间
        .setAwaitTimeUnit(TimeUnit.MILLISECONDS)// 设置线程结束等待时间单位
        .setKeepAliveTime(1)// 设置线程数空闲时间
        .setKeepAliveTimeUnit(TimeUnit.SECONDS)// 设置线程数空闲时间单位
        .setCorePoolSize(4)// 设置线程数
        .setMaximumPoolSize(8)// 设置最大线程数
        .setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy())// 设置拒绝策略
        .build()
```

## 6、缓存Cache
缓存这块引用了[yangfuhai](https://github.com/yangfuhai)的[ASimpleCache](https://github.com/yangfuhai/ASimpleCache)来做，我封装了ACacheUtils方便小伙伴进行缓存的配置和使用。

- 在你APP启动的位置对ACacheUtils进行配置，如下代码：
```
    ACacheUtils.get().newBuilder()
        .setCacheDir(dirPath)// 设置缓存路径，不设置则使用默认路径
        .setMaxSize(maxSize)// 自定义缓存大小，不设置则使用默认大小
        .setMaxCount(maxCount)// 自定义缓存数量，不设置则使用默认数量
        .build(this)// 完成构建
```
```
    // 如果你均使用默认配置，那么请这么写
    ACacheUtils.get().newBuilder().build(this)
```
- 如果你要保存一个String数据，你可以这么使用
```
    ACacheUtils.get().create().put("data", "萌萌哒")
```
- 当然，你可以为你的数据设置缓存时间，类似下面这样
```
    ACacheUtils.get().create().put("data", "萌萌哒", 10)//保存10秒，如果超过10秒去获取这个key，将返回空字符串
    ACacheUtils.get().create().put("data", "萌萌哒", 2 * ACache.TIME_DAY)//保存两天，如果超过两天去获取这个key，将返回空字符串
```
- 如果你想获取一个String数据你可以这么使用
```
    ACacheUtils.get().create().getAsString("data")
```
- 如果你想清空缓存数据，可以调用下面的方法
```
    ACacheUtils.get().create().clear()
```
- 如果你想删除某个key对应的缓存，你可以这么干
```
    ACacheUtils.get().create().remove("data")
```

## 7、通知帮助类NotificationUtils
### 1）创建通知组和频道【适配O（api26）】
在Android8.0里发送通知需要指定该通知的频道id。

<1> 如果你的targetSdkVersion小于26，那么在创建Notification的时候，频道id可以传null来避开适配问题

<2> 如果你的targetSdkVersion大于等于26，那么你在创建Notification的时候必须指定频道id，否则发送时会抛出异常，无法正常显示通知

- 创建一个通知组，设置该通知组的groupId和groupName，只要在改APP里设置过一次以后就无需再重复设置
```
    NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName)
    NotificationUtils.create(getContext()).createNotificationChannelGroup(group)// 设置通知组
```
- 创建一个通知频道，设置该频道的channelId和channelName，只要在改APP里设置过一次以后就无需再重复设置.
- 通知频道内有许多参数可以设置，大家可以根据自己的需要配置参数
- 一个组里可以有多个频道，通过setGroup(groupId)方法，将该频道配置进id对应的组里
```
    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
    channel.enableLights(true)// 开启指示灯，如果设备有的话。
    channel.setLightColor(Color.GREEN)// 设置指示灯颜色
    channel.setDescription("应用主通知频道")// 通道描述
    channel.enableVibration(true)// 开启震动
    channel.setVibrationPattern(new long[]{100, 200, 400, 300, 100})// 设置震动频率
    channel.setGroup(groupId)
    channel.canBypassDnd()// 检测是否绕过免打扰模式
    channel.setBypassDnd(true)// 设置绕过免打扰模式
    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE)
    channel.canShowBadge()// 检测是否显示角标
    channel.setShowBadge(true)// 设置是否显示角标
    NotificationUtils.create(getContext()).createNotificationChannel(channel)// 设置频道
```

### 2）发送通知
通过NotificationCompat.Builder方法来构建你的通知内容，在获得Notification后可以传入NotificationUtils里的send()方法来发送通知
```
    // channelId的设置方法请参考第一点
    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), channelId)

    ......

    Notification notification = builder.build()//构建通知
    NotificationUtils.create(getContext()).send(notification)
    NotificationUtils.create(getContext()).send(notifiId, notification)
```
- 如果你希望随机生成id来发送，你可以在send()方法里直接传入Notification
- 如果你希望指定id来发送，你可以在send()方法里传入id和Notification

## 8、各种通用工具类
1. AnkoAnim 动画帮助类，在View中进行了扩展，支持简单的动画变化
2. AnkoArray 数组列表帮助类，在Array和Collection中进行了扩展
3. AnkoBoolean 在Boolean中进行扩展，支持三目运算符
4. AnkoContextCompat 兼容类，在Context中进行扩展，获取Color和Drawable
5. AnkoDimensions 单位转换类，在Context、View和Fragment中进行扩展，支持dp、px、sp之间的相互转化
6. AnkoDrawable Drawable帮助类，在Context中进行扩展，可以通过颜色或Bitmap创建Drawable
7. AnkoIntent Intent帮助方法，在Context中进行扩展，包装了Intent相关的操作
8. AnkoKeyBoard 软键盘控制类,在Context和View中进行扩展，可以操作键盘显隐
9. AnkoNumFormat 数字格式化类，在Double和Float中进行扩展，可以格式化小数
10. AnkoScreen 屏幕信息类，在Context、Fragment、View和Activity中进行扩展，获取屏幕、状态栏和导航栏宽高
11. AnkoSetting 系统设置帮助类，在Context和Activity中进行扩展，对手机的亮度音量等等进行设置
12. AnkoStorage 存储帮助类，在Context中进行扩展，获取存储卡路径
13. AnkoVibrator 手机震动类，在Context中进行扩展，设置手机震动
14. AnkoViewBinder 控件绑定类，在Activity、Fragment、RecyclerView.ViewHolder、Dialog和ViewGroup中进行扩展，通过id绑定控件
15. AlbumUtils 系统相册工具类，你可以通过里面的方法获取系统相册的图片和图片的文件夹信息
16. AppUtils APP帮助类，部分方法在Context中进行扩展，可获取应用相关的信息
17. BitmapUtils Bitmap帮助类，对bitmap进行处理变换和压缩
18. ColorUtils 颜色帮助类，修改颜色透明度
19. DateUtils 时间格式化帮助类，可将格式化时间和时间戳以及Date数据相互转换
20. DeviceUtils 设备帮助类，部分方法在Context中进行扩展，可获取手机的IMEI、IMESI、UA等设备信息，获取前请注意动态获取权限
21. FileUtils 文件操作帮助类，封装了文件相关的操作方法
22. IdCardUtils 身份证帮助类，可校验身份证号
23. NotificationUtils 通知帮助类，可创建组/通道以及发送通知
24. ReflectUtils 反射帮助类，封装了反射相关的方法，已在内部进行trycatch，注意返回对象的转型
25. SelectorUtils Selector状态帮助类，可设置按钮的按压、禁用、选中等背景颜色
26. SnackbarUtils Snackbar帮助类，可创建Snackbar提示，并对其进行配置
27. StatusBarUtil 状态栏帮助类，我将[laobie](https://github.com/laobie)的[StatusBarUtil](https://github.com/laobie/StatusBarUtil)转为kotlin并进行了优化，功能主要是对状态栏的颜色进行控制。
28. StringUtils 字符串帮助类，可进行UTF-8的编解码以及分隔符处理
29. ToastUtils Toast帮助类，部分方法在Context和Fragment中进行扩展，可支持toast的配置显示
30. UiHandler 主线程的post帮助类，使用后需要在app退出时调用UiHandler.destroy()释放资源

## 扩展

- [更新记录](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core_update.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md#core-kt库)
- [AgileDevKt 主页](https://github.com/LZ9/AgileDevKt)
- [了解 Pandora](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [了解 imageloader-kt](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md)
