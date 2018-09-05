# imageloader库
图片加载库已经集成了Glide v4以上版本，并且对Glide做了解耦封装，支持链式调用，小伙伴可以随心所欲使用。

## 目录
 - [1、内部引用](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#1内部引用)
 - [2、AppGlideModule实现](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#2appglidemodule实现)
 - [3、初始化](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#3初始化)
 - [4、缓存操作](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#4缓存操作)
 - [5、使用方法](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#5使用方法)
 - [扩展](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#扩展)

## 1、内部引用
在图片库里已经为大家加入了Glide的依赖，小伙伴不需要再重复添加，我在图片库里也已经集成了图片变换，可以直接通过链式来调用。
```
    dependencies {
        implementation 'com.android.support:appcompat-v7:28.0.0-rc01'
        implementation 'com.android.support:support-annotations:28.0.0-rc01'
        implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.2.61"

        api 'com.github.bumptech.glide:glide:4.8.0'
        kapt 'com.github.bumptech.glide:compiler:4.8.0'
        implementation "com.github.bumptech.glide:okhttp3-integration:4.8.0"
    }
```

## 2、AppGlideModule实现
我帮大家简单的配置了一个CacheAppGlideModule，如果小伙伴需要自定义可以继承CacheAppGlideModule来进行扩展。

## 3、初始化
请在Application里面对图片库进行初始化，你可以根据自己的需要配置默认占位符、失败图、缓存路径等等。
```
    ImageloaderManager.get().newBuilder()
        .setPlaceholderResId(R.drawable.ic_launcher)//设置默认占位符
        .setErrorResId(R.drawable.ic_launcher)// 设置加载失败图
        .setDirectoryFile(applicationContext.cacheDir)// 设置缓存路径
        .setDirectoryName("image_cache")// 缓存文件夹名称
        .build()
```

## 4、缓存操作
1）清除内存缓存
```
    ImageloaderManager.get().clearMemoryCaches(context)
```
2）清除内存缓存（包括GC内存）
```
    ImageloaderManager.get().clearMemoryCachesWithGC(context)
```
3）清除磁盘缓存
```
    ImageloaderManager.get().clearDiskCaches(context)
```
5）暂停加载
```
    ImageloaderManager.get().pauseLoad(context)
```
6）恢复加载
```
    ImageloaderManager.get().resumeLoad(context)
```
7）是否暂停加载
```
    ImageloaderManager.get().isPaused(context)
```

## 5、使用方法
下面为基本的使用方法，你可以根据自己的需要链上对应的方法，对图片进行控制。
```
    ImageLoader.create(this)
        .load("http://ww2.sinaimg.cn/large/610dc034jw1f91ypzqaivj20u00k0jui.jpg")// 设置加载路径（String/Uri/File/Integer/byte[]）
        .setPlaceholder(R.drawable.ic_launcher)// 设置加载图占位图
        .setError(R.drawable.ic_launcher)// 设置加载失败图
        .setImageSize(100, 100)// 设置图片宽高（单位px）
        .useCircle()// 使用圆形图片
        .useBlur()// 使用高斯模糊
        .setBlurRadius(10)// 设置高斯模糊（0-25）
        .useRoundCorner()// 使用圆角
        .setRoundCorner(10)// 设置圆角半径
        .setRoundedCornerType(RoundedCornersTransformation.CornerType.LEFT)// 设置圆角的位置参数
        .setRoundedCornersMargin(5)// 设置圆角的间距
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)// 设置磁盘缓存方式
        .skipMemoryCache()// 跳过图片缓存入内存
        .setCenterCrop()// 设置居中裁切
        .setFitCenter()// 设置居中自适应
        .setCenterInside()// 设置内部居中
        .dontAnimate()// 设置使用动画
        .userCrossFade()// 使用默认渐变效果
        .setAnimResId(R.anim.componentkt_anim_bottom_in)// 设置动画资源id
        .setAnim(object :ViewPropertyTransition.Animator{// 设置动画
            override fun animate(view: View?) {

            }
        })
        .setRequestListener(object :RequestListener<String>{// 添加图片请求监听器
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<String>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: String?, model: Any?, target: Target<String>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        })
        .useFilterColor()// 使用覆盖颜色
        .setFilterColor(Color.YELLOW)// 设置覆盖颜色
        .useGrayscale()// 使用灰度化
        .useCropSquare()// 切正方形图
        .useMask()// 使用蒙板图片
        .setMaskResId(R.drawable.imageloaderkt_mask_starfish)// 设置蒙板图片资源id
        .setVideo()// 显示视频第一帧
        .into(imageView)
```

## 扩展

- [更新记录](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader_update.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md#imageloader库)
- [AgileDevKt 主页](https://github.com/LZ9/AgileDevKt)
- [了解 core]()
- [了解 component]()
