# 图片选择和预览
图片这块我封装了3个常用的帮助类，一个是图片选择器 **PickerManager**，你可以用他来选取指定图片或者手机内的图片。
一个是拍照管理器 **TakePhotoManager**，你可以用它直接调用系统相机拍照后得到照片路径。
还有一个是图片预览器 **PreviewManager**，灵活的泛型支持各类资源地址的图片预览。
使用方法参考 [PicActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/pic/PicActivity.kt)。

## 一、照片选择器PickerManager
PickerManager可以帮你快速构造一个图片选择器，包括挑选指定图片，挑选手机图片，拍照，以及丰富的UI订制，
同时还支持你使用你喜欢的任何图片加载器来加载图片。
参考方法 [PicPickerTestActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/pic/picker/PicPickerTestActivity.kt)。

### 1. 构建方法
我使用链式的封装来帮助大家快速构造选择器对象。

```
    PickerManager.create()// 获取PickerManager对象
        .setMaxCount(max)// 设置可选图片的最大数量（n > 0）
        .setScale(isScale)// 设置预览是否缩放图片
        .setNeedCamera(isNeedCamera)// 设置是否需要相机功能
        .setNeedItemPreview(isNeedItemPreview)// 设置是否需要item的预览功能
        .setClickClosePreview(isClickClosePreview)// 设置是否点击关闭预览
        .setPickerUIConfig(UIConfig)// 设置选择器的界面配置，不设置使用默认UI风格
        .setCameraSavePath(path)// 设置拍照保存地址
        .setAuthority(authority)// 设置7.0的FileProvider名字
        .setImgLoader { context, source, imageView ->// 设置item的图片加载器
            // 使用你自己的图片加载器，如果加载器要传入Context请务必使用我回调出来的context
        }
        .setPreviewImgLoader { context, source, imageView ->// 设置预览图图加载器
            // 同上
        }
        .setOnPhotoPickerListener { photos ->// 设置图片选中回调
            // 获取用户选中的图片
        }
        .build()// 选择手机图片
        .build(list)// 选择自定义的图片列表list
        .build(array)// 选择自定义的图片数组array
        .open(getContext())// 打开选择器
```

 - build()、build(list)和build(array)只需要选择一个调用即可
 - 如果你没有设置setImgLoader()会提示：**您尚未设置图片加载器**
 - 如果你没有设置setPreviewImgLoader()会默认采用setImgLoader()里监听回调
 - 如果你没有设置setOnPhotoPickerListener()会提示：**您尚未设置图片选中监听器**
 - 如果你调用了build(list)和build(array)却传入空的list或array，会提示：**您没有可以选择的图片**
 - 如果你调用了build()并且没有开启相机拍照（setNeedCamera()没有设置或设置为false），而手机里又没有任何图片时，会提示：**您没有可以选择的图片**
 - 如果你setMaxCount()的入参小于1时，我会默认将它重置为1
 - 如果你调用了build(list)和build(array)，我会默认把相机拍照功能关闭，并且对传入的数据列表去重
 - 如果你开启了相机拍照setNeedCamera(true)，但是没有设置图片保存路径setCameraSavePath()，我会默认保存在手机的DCIM目录
 - 如果你开启了相机拍照setNeedCamera(true)，并且编译版本是7.0及以上，辣么你必须设置setAuthority()，或则会提示：**您尚未配置FileProvider**

### 2. 配置UI
为了大家方便根据自己的APP风格来打造UI，我提供了一个功能丰富的UI配置器，大家可以根据自己的需要配置UI的风格

1）获取默认UI风格
```
    val config = PickerUIConfig.createDefault()
```

2）自定义配置
```
    val config = PickerUIConfig.createDefault()
        .setStatusBarColor(color)// 设置顶部状态栏颜色（sdk >= 5.0）
        .setNavigationBarColor(color)// 设置底部导航栏颜色（sdk >= 5.0）
        .setCameraImg(drawable)// 设置相机图标
        .setCameraBgColor(color)// 设置拍照按钮背景颜色
        .setItemBgColor(color)// 设置照片背景颜色
        .setSelectedBtnUnselect(color)// 设置选择按钮未选中颜色
        .setSelectedBtnSelected(color)// 设置选择按钮选中颜色
        .setMaskColor(color)// 设置选中后的遮罩层颜色（建议带透明度）
        .setBackBtnColor(color)// 设置返回按钮颜色
        .setMainTextColor(color)// 设置主文字颜色
        .setMoreFolderImg(drawable)// 设置更多文件夹图片
        .setTopLayoutColor(color)// 设置顶部背景颜色
        .setBottomLayoutColor(color)// 设置底部背景颜色
        .setPreviewBtnNormal(color)// 设置预览按钮普通颜色
        .setPreviewBtnPressed(color)// 设置预览按钮按压颜色
        .setPreviewBtnUnable(color)// 设置预览按钮不可用颜色
        .setConfirmBtnNormal(color)// 设置确认按钮普通颜色
        .setConfirmBtnPressed(color)// 设置确认按钮按压颜色
        .setConfirmBtnUnable(color)// 设置确认按钮不可用颜色
        .setConfirmTextNormal(color)// 设置确认文字普通颜色
        .setConfirmTextPressed(color)// 设置确认文字按压颜色
        .setConfirmTextUnable(color)// 设置确认文字不可用颜色
        .setFolderSelectColor(color)// 设置文件夹选择颜色
        .setPreviewBgColor(color)// 设置预览页背景色
```
配置完后只需要把 **config** 放入 **setPickerUIConfig()** 方法里即可。

### 3. 拍照权限
如果你开启了拍照功能，请务必添加相机权限并且动态申请（sdk >= 6.0）
```
    <uses-permission android:name="android.permission.CAMERA" />
```

### 4. FileProvider配置
如果你开启了拍照功能，并且编译版本是7.0及以上，请务必设置FileProvider

1）首先在res目录里新建一个xml的文件夹，创建一个file_provider_paths.xml文件（文件名你可以自定）
```
    <?xml version="1.0" encoding="utf-8"?>
    <paths xmlns:android="http://schemas.android.com/apk/res/android">

        <!--自定义地址-->
        <external-path
            name="app_provider_path"
            path="AgileDevKt" />

        <!--默认地址-->
        <external-path
            name="dcim_provider_path"
            path="DCIM" />
    </paths>
```
 - 自定义地址是指你要开放的文件夹路径，正常你可以填自己APP的目录，例如我测试APP的目录叫AgileDevKt
 - 默认地址是我将拍照照片默认存储的地方，如果你希望使用默认地址，则保留

2）然后在 **AndroidManifest.xml** 里配置FileProvider
```
    <application
        .... >

        ....

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="packageName.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths" />
        </provider>

        ....

    </application>
```
 - android:resource=""里放你刚才在xml里创建的文件名称
 - android:authorities=""里放你自定义的FileProvider名称，正常是以你的包名命名**packageName.fileprovider**保证不会和其他人重复，例如我的测试APP就是**com.lodz.android.agiledevkt.fileprovider**
 - 然后在选择器里配置你自定义的FileProvider名称 **setAuthority("packageName.fileprovider")**
 - 我这里的android:name=""使用的是androidx，如果你还在使用support包，那你需要改为**android.support.v4.content.FileProvider**

## 二、拍照管理器TakePhotoManager
TakePhotoManager对调用系统拍照做了封装，帮助你摆脱onActivityResult高耦合的困扰，方便你在任何地方进行调用。
参考方法[TakePhotoTestActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/pic/take/TakePhotoTestActivity.kt)。

### 1. 构建方法
与图片选择器相似，链式调用完成构建，通过接口回调路径结果。

```
    TakePhotoManager.create()// 获取TakePhotoManager对象
        .setStatusBarColor(color)// 设置顶部状态栏颜色
        .setNavigationBarColor(color)// 设置底部导航栏颜色
        .setPreviewBgColor(color)// 设置预览页背景色
        .setImmediately(isImmediately)// 设置拍照后立即返回结果，默认立即返回，false会先弹出预览页由用户点击确认和取消
        .setCameraSavePath(FileManager.getCacheFolderPath())// 设置拍照保存地址
        .setAuthority(authority)// 设置7.0的FileProvider名字
        .setPreviewImgLoader { context, source, imageView ->// 设置预览图图加载器，setImmediately(true)时可以不设置
            // 使用你自己的图片加载器，如果加载器要传入Context请务必使用我回调出来的context
        }
        .setOnPhotoTakeListener { photo ->// 设置拍照结果回调
            // 拍照结果回调
        }
        .build()// 完成构建
        .take(getContext())// 启动拍照
```
 - 如果你没有设置setImmediately()方法或者将它设置为true，你可以不需要设置setPreviewImgLoader()方法，否则会提示：**您尚未设置图片预览器**
 - 如果你没有设置setOnPhotoTakeListener()方法会提示：**您尚未设置拍照回调监听器**
 - 如果你没有设置图片保存路径setCameraSavePath()，我会默认保存在手机的DCIM目录
 - 如果你编译版本是7.0及以上，辣么你必须设置setAuthority()，或则会提示：**您尚未配置FileProvider**

### 2. 注意事项
 - 拍照功能需要用到相机权限，请务必添加并且动态申请（sdk >= 6.0）
 - 适配7.0及以上版本的手机请务必设置FileProvider，配置方法参考 [FileProvider配置](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_picker_preview.md#4-fileprovider配置)

## 三、图片预器PreviewManager
PreviewManager具备灵活的泛型支持，你只需要在creat()方法里指定传入图片列表的类型，在接口回调中就会回调相应类型的数据
参考方法 [PicPreviewTestActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/pic/preview/PicPreviewTestActivity.kt)。

### 1. 构建方法
通过链式调用快速构建，支持UI配置和泛型支持。

```
    PreviewManager.create<T>()// 创建PreviewManager对象，T为泛型
        .setScale(isScale)// 设置是否可缩放
        .setPosition(position)// 设置默认展示图片的位置
        .setBackgroundColor(color)// 设置背景色
        .setStatusBarColor(color)// 设置顶部状态栏颜色
        .setNavigationBarColor(color)// 设置底部导航栏颜色
        .setPagerTextColor(color)// 设置页码文字颜色
        .setPagerTextSize(textSize)// 设置页码文字大小
        .setShowPagerText(isShow)// 设置是否显示页码文字
        .setOnClickListener { context, source, position, controller ->// 设置点击监听
            // 点击图回调
        }
        .setOnLongClickListener { context, source, position, controller ->// 设置长按监听
            // 长按图回调
        }
        .setImgLoader { context, source, imageView ->// 设置图片加载器
            // 使用你自己的图片加载器，如果加载器要传入Context请务必使用我回调出来的context
        }
        .build(pic)// 构建单张图片预览
        .build(list)// 构建图片列表预览
        .build(array)// 构建图片数组预览
        .open(getContext())//打开预览器
```
 - build(pic)、build(list)和build(array)选择一个调用即可
 - 如果你没有设置setImgLoader()方法会提示：**您尚未设置图片加载器**
 - 如果你的build()方法入参是空，会提示：**图片数据为空**
 - 如果构建的图片只有一张则不显示页码
 - 如果setPosition()的值超出了图片的数量，我会重置为从第一张开始预览

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_picker_preview.md#图片选择和预览)