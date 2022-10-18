# 敏捷开发解决方案
这套敏捷开发解决方案基于Kotlin语法实现，根据Kotlin的特性进行了方法扩展。
解决方案主要包含了3块内容，能帮助你快速构建一个中小型的App，你只需要专注于UI开发和业务逻辑即可。
三块组件都只支持Androidx，请确保你的工程已经升级支持Androidx。

由于jcenter删库跑路，请大家添加mavenCentral依赖，并引用最新版本（为了配合迁移，引用的域名从**com.lodz**改为**ink.lodz**）
```
    repositories {
        ...
        mavenCentral()
        ...
    }
```
## 1. 使用扩展方法：
如果你希望获取丰富的扩展方法来支持你的工程，你可以选择依赖[corek-kt](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md)，
里面同时包含了Androidx相关的支持库。
```
    implementation 'ink.lodz:core-kt:2.0.3'
```

## 2. 使用Pandora组件：
如果你希望开发一个新的中小型应用，并且把自己的精力专注在业务UI和逻辑开发上，你可以选择依赖[Pandora](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)，
里面包含了控件的封装、调用方法的封装、开发工具封装等等，你可以从Pandora的盒子里拿到你需要的绝大多数东西。
```
    implementation 'ink.lodz:pandora:2.0.2'
```

## 3. 使用图片库：
如果你希望便捷的使用图片加载库来服务你的工程，你可以选择依赖[imageloader-kt](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md)，
它内部集成了Glide，通过简单的链式调用来降低你的学习梯度。
```
    implementation 'ink.lodz:imageloader-kt:1.2.2'
```

## 4、详细了解
- [了解 corek-kt](https://github.com/LZ9/AgileDevKt/blob/master/corekt/readme_core.md)
- [了解 Pandora](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [了解 imageloader-kt](https://github.com/LZ9/AgileDevKt/blob/master/imageloaderkt/readme_imageloader.md)

## 3、关于Issues
如果小伙伴发现BUG或者有任何的建议，都欢迎到 [Github Issues](https://github.com/LZ9/AgileDevKt/issues) 上留言，我会定期查看回复哒

## License
- [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Copyright 2019 Lodz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
