package com.lodz.android.corekt.anko

import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.lodz.android.corekt.utils.FileUtils
import java.io.File
import java.io.Serializable

/**
 * Intent扩展方法
 * Created by zhouL on 2018/6/26.
 */

/** 安装apk文件 */
@JvmOverloads
fun Context.installApk(apkPath: String, authority: String, newTask: Boolean = true) {
    val file: File? = FileUtils.create(apkPath)
    if (file == null || !FileUtils.isFileExists(file)) {
        throw IllegalArgumentException("file no exists")
    }

    if (authority.isEmpty()) {
        throw IllegalArgumentException("authority is null")
    }

    // 系统大于8.0且没有“安装未知应用”权限，则跳转到设置页面
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (!packageManager.canRequestPackageInstalls()) {
            //跳转至“安装未知应用”权限界面，引导用户开启权限
            startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName")))
            return
        }
    }

    val intent = Intent(Intent.ACTION_VIEW)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val apkUri = FileProvider.getUriForFile(this, authority, file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
    }
    startActivity(intent)
}

/** 卸载包名为[packageName]的应用 */
fun Context.uninstallApp(packageName: String) {
    if (packageName.isEmpty()) {
        return
    }
    startActivity(Intent(Intent.ACTION_DELETE, Uri.parse("package:$packageName")))
}

/** 通过LaunchIntent打开包名为[packageName]的应用 */
@JvmOverloads
fun Context.openAppByLaunch(packageName: String, newTask: Boolean = true) {
    if (packageName.isEmpty()) {
        throw IllegalArgumentException("packageName is null")
    }
    val intent: Intent = packageManager.getLaunchIntentForPackage(packageName) ?: throw ActivityNotFoundException("no found packageName")
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 通过[actionName]默认是android.intent.action.MAIN，来打开包名为[packageName]的应用 */
@JvmOverloads
fun Context.openAppByAction(packageName: String, actionName: String = Intent.ACTION_MAIN, activityName: String = "", newTask: Boolean = true) {
    if (packageName.isEmpty()) {
        throw IllegalArgumentException("packageName is null")
    }

    var mainActivityName: String? = ""// 启动页的路径
    val intent = Intent(actionName)
    for (resolve in packageManager.queryIntentActivities(intent, 0)) {
        val info: ActivityInfo = resolve.activityInfo ?: continue
        if (activityName.isNotEmpty()){
            if (activityName != info.name){
                continue
            }
        }
        if (packageName == info.packageName) {
            mainActivityName = info.name
            break
        }
    }

    if (mainActivityName.isNullOrEmpty()) {
        throw ActivityNotFoundException("no found packageName")
    }

    intent.component = ComponentName(packageName, mainActivityName)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 打开应用设置界面 */
fun Context.goAppDetailSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    startActivity(intent)
}

/** 跳转到日期设置页面 */
fun Context.goDateSetting() {
    startActivity(Intent(Settings.ACTION_DATE_SETTINGS))
}

/** 跳转到定位设置页 */
fun Context.goLocationSetting() {
    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
}

/** 跳转到WIFI设置页 */
fun Context.goWifiSetting() {
    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
}

/** 跳转到数据流量设置页 */
fun Context.goDataRoamingSetting() {
    startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
}

/** 跳转到密码设置页 */
fun Context.goSetPswdSetting() {
    startActivity(Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD))
}

/** 跳转到蓝牙设置页 */
fun Context.goBluetoothSetting() {
    startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
}

/** 跳转到NFC设置页 */
fun Context.goNfcSetting() {
    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
}

/** 打开浏览器访问[url] */
@JvmOverloads
fun Context.browse(url: String, newTask: Boolean = true) {
    if (url.isEmpty()) {
        throw IllegalArgumentException("url is null")
    }
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    if (newTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/** 分享标题为[subject]，内容为[text]的文字，应用选择框提示[tips] */
@JvmOverloads
fun Context.share(text: String, subject: String = "", tips: String = "") {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)// 主题
    intent.putExtra(Intent.EXTRA_TEXT, text)// 内容
    var title: String? = null// 应用选择框提示
    if (tips.isNotEmpty()) {
        title = tips
    }
    startActivity(Intent.createChooser(intent, title))
}

/** 发送标题为[subject]，内容为[text]的邮件到[email]邮箱 */
@JvmOverloads
fun Context.email(email: String, subject: String = "", text: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))//邮箱
    if (subject.isNotEmpty())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)//标题
    if (text.isNotEmpty())
        intent.putExtra(Intent.EXTRA_TEXT, text)// 内容
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

/** 拨打号码[number] */
@SuppressLint("MissingPermission")
fun Context.makeCall(number: String) {
    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
}

/** 发送短信[text]到号码[number] */
@JvmOverloads
fun Context.sendSMS(number: String, text: String = "") {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
    intent.putExtra("sms_body", text)
    startActivity(intent)
}

/** 拍照，存储地址[savePath]，FileProvider名称[authority]，请求码[requestCode] */
fun Activity.takePhoto(savePath: String, authority: String, requestCode: Int): Boolean {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(packageManager) == null) {
        return false
    }
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, authority, File(savePath)))
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(savePath)))
        }
        startActivityForResult(intent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return true
}

/** 从intent中根据[nameKey]来获取传递过来的Int对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
fun Activity.intentExtrasInt(nameKey: String, defaultValue: Int = 0, bundleKey: String? = null): Lazy<Int> = lazy {
    if (bundleKey == null){
        return@lazy this.intent?.getIntExtra(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getInt(nameKey, defaultValue) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Int对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasInt(nameKey: String, defaultValue: Int = 0, bundleKey: String? = null): Lazy<Int> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getInt(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getInt(nameKey, defaultValue) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Boolean对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
fun Activity.intentExtrasBoolean(name: String, defaultValue: Boolean = false, bundleKey: String? = null): Lazy<Boolean> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getBooleanExtra(name, defaultValue) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getBoolean(name, defaultValue) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Boolean对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasBoolean(nameKey: String, defaultValue: Boolean = false, bundleKey: String? = null): Lazy<Boolean> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getBoolean(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getBoolean(nameKey, defaultValue) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Long对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
fun Activity.intentExtrasLong(nameKey: String, defaultValue: Long = 0, bundleKey: String? = null): Lazy<Long> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getLongExtra(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getLong(nameKey, defaultValue) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Long对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasLong(nameKey: String, defaultValue: Long = 0, bundleKey: String? = null): Lazy<Long> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getLong(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getLong(nameKey, defaultValue) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Double对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
fun Activity.intentExtrasDouble(nameKey: String, defaultValue: Double = 0.0, bundleKey: String? = null): Lazy<Double> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getDoubleExtra(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getDouble(nameKey, defaultValue) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Double对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasDouble(nameKey: String, defaultValue: Double = 0.0, bundleKey: String? = null): Lazy<Double> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getDouble(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getDouble(nameKey, defaultValue) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Float对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
fun Activity.intentExtrasFloat(nameKey: String, defaultValue: Float = 0.0f, bundleKey: String? = null): Lazy<Float> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getFloatExtra(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getFloat(nameKey, defaultValue) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Float对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasFloat(nameKey: String, defaultValue: Float = 0.0f, bundleKey: String? = null): Lazy<Float> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getFloat(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getFloat(nameKey, defaultValue) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的String对象的值，若[bundleKey]不为空则从intent的Bundle中获取数据 */
fun Activity.intentExtrasString(nameKey: String, bundleKey: String? = null): Lazy<String?> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getStringExtra(nameKey)
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getString(nameKey)
}

/** 从intent中根据[nameKey]来获取传递过来的Float对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
fun Activity.intentExtrasStringNoNull(nameKey: String, defaultValue: String, bundleKey: String? = null): Lazy<String> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getStringExtra(nameKey) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getString(nameKey, defaultValue) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Float对象的值，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasString(nameKey: String, bundleKey: String? = null): Lazy<String?> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getString(nameKey)
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getString(nameKey)
}

/** 从arguments中根据[nameKey]来获取传递过来的Float对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun Fragment.argumentsExtrasStringNoNull(nameKey: String, defaultValue: String, bundleKey: String? = null): Lazy<String> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getString(nameKey, defaultValue) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getString(nameKey, defaultValue) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Parcelable对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
fun <T : Parcelable> Activity.intentExtrasParcelable(nameKey: String, defaultValue: T? = null, bundleKey: String? = null): Lazy<T?> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getParcelableExtra(nameKey) ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getParcelable(nameKey) ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Parcelable对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
fun <T : Parcelable> Fragment.argumentsExtrasParcelable(nameKey: String, defaultValue: T? = null, bundleKey: String? = null): Lazy<T?> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getParcelable(nameKey) ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getParcelable(nameKey) ?: defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Serializable对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从intent的Bundle中获取数据 */
@JvmOverloads
inline fun <reified T : Serializable> Activity.intentExtrasSerializable(
    nameKey: String,
    defaultValue: T? = null,
    bundleKey: String? = null
): Lazy<T?> = lazy {
    if (bundleKey == null) {
        return@lazy this.intent?.getSerializableExtra(nameKey) as? T ?: defaultValue
    }
    return@lazy this.intent?.getBundleExtra(bundleKey)?.getSerializable(nameKey) as? T
        ?: defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Serializable对象的值，默认值为[defaultValue]，若[bundleKey]不为空则从arguments的Bundle中获取数据 */
@JvmOverloads
inline fun <reified T : Serializable> Fragment.argumentsExtrasSerializable(
    nameKey: String,
    defaultValue: T? = null,
    bundleKey: String? = null
): Lazy<T?> = lazy {
    if (bundleKey == null) {
        return@lazy this.arguments?.getSerializable(nameKey) as? T ?: defaultValue
    }
    return@lazy this.arguments?.getBundle(bundleKey)?.getSerializable(nameKey) as? T ?: defaultValue
}