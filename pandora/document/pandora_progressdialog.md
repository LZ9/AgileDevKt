# 加载框封装
我为大家提供了一个简单的加载库封装类ProgressDialogHelper，方便大家快速创建一个加载框，
用法可以参考[ProgressDialogActivity.kt](https://github.com/LZ9/AgileDevKt/blob/master/app/src/main/java/com/lodz/android/agiledevkt/modules/progressdialog/ProgressDialogActivity.kt)。

## 调用方法
大家在配置后可以调用create()方法获取AlertDialog对象，然后控制显示或者隐藏
```
    ProgressDialogHelper.get()
        .setCancelable(true)//点击返回键关闭
        .setCanceledOnTouchOutside(true)//点击外部空白关闭
        .setMsg(getString(R.string.pd_loading))//提示语
        .setIndeterminateDrawable(R.drawable.anims_custom_progress)//自定义加载图标
        .setOnCancelListener(DialogInterface.OnCancelListener { dialog ->//取消弹框监听器
            toastShort(R.string.pd_dismiss)
        })
        .create(getContext())//创建AlertDialog对象
        .show()//显示加载框
```

## 扩展
- [返回目录](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/readme_pandora.md)
- [回到顶部](https://github.com/LZ9/AgileDevKt/blob/master/pandora/document/pandora_progressdialog.md#加载框封装)