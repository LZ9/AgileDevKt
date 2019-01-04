# ACache封装
我将[yangfuhai](https://github.com/yangfuhai)的[ASimpleCache](https://github.com/yangfuhai/ASimpleCache)转为Kotlin并做了简单的封装，
大家如果不需要数据库或者sp存储，可以考虑使用ACacheUtils来存储，使用方法可以参考[ACacheTestActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/acache/ACacheTestActivity.kt)。

### 1. 配置ACache
大家可以通过newBuilder()方法来对ACache进行配置，建议在Application里进行配置
```
    ACacheUtils.get().newBuilder()
        .setMaxCount(count)//设置缓存数量，建议不配置使用默认值
        .setMaxSize(size)// 设置缓存大小，建议不配置使用默认值
        .setCacheDir(path)// 设置缓存路径，不设置则使用默认路径
        .build(context)// 完成构建
```

### 2. 使用ACache
 - 保存数据方法
```
    // 保存Bitmap
    ACacheUtils.get().create().put(key, bitmap)
    ACacheUtils.get().create().put(key, bitmap, saveTime)

    // 保存String
    ACacheUtils.get().create().put(key, string)
    ACacheUtils.get().create().put(key, string, saveTime)

    // 保存Drawable
    ACacheUtils.get().create().put(key, drawable)
    ACacheUtils.get().create().put(key, drawable, saveTime)

    // 保存ByteArray
    ACacheUtils.get().create().put(key, bytearray)
    ACacheUtils.get().create().put(key, bytearray, saveTime)

    // 保存JSONArray
    ACacheUtils.get().create().put(key, jsonarray)
    ACacheUtils.get().create().put(key, jsonarray, saveTime)

    // 保存JSONObject
    ACacheUtils.get().create().put(key, jsonobject)
    ACacheUtils.get().create().put(key, jsonobject, saveTime)

    // 保存Serializable
    ACacheUtils.get().create().put(key, serializable)
    ACacheUtils.get().create().put(key, serializable, saveTime)
```

 - 保存数据例子
```
    ACacheUtils.get().create().put(key, string)// 长久保存string字符串
    ACacheUtils.get().create().put(key, string, 10)// 保存string字符串10秒
    ACacheUtils.get().create().put(key, string, 5 * ACache.TIME_HOUR)// 保存string字符串5小时
    ACacheUtils.get().create().put(key, string, 1 * ACache.TIME_DAY)// 保存string字符串1天
```

 - 获取数据方法
```
    ACacheUtils.get().create().getAsAny(key)// 读取Serializable对象
    ACacheUtils.get().create().getAsBitmap(key)// 读取Bitmap对象
    ACacheUtils.get().create().getAsByteArray(key)// 读取ByteArray对象
    ACacheUtils.get().create().getAsDrawable(key)// 读取Drawable对象
    ACacheUtils.get().create().getAsJSONArray(key)// 读取JSONArray对象
    ACacheUtils.get().create().getAsJSONObject(key)// 读取JSONObject对象
    ACacheUtils.get().create().getAsString(key)// 读取String对象
    ACacheUtils.get().create().file(key)// 读取File对象
```

 - 删除数据方法
```
    ACacheUtils.get().create().remove(key)// 删除key对应的数据
    ACacheUtils.get().create().clear()// 清空所有缓存数据
```

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部]()