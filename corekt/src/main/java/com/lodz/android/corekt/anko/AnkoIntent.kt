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
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.lodz.android.corekt.utils.FileUtils
import java.io.File
import java.io.Serializable
import java.util.ArrayList

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

/** 使用第三方软件播放音频文件[file]，FileProvider名称[authority] */
fun Activity.playAudio(file: File, authority: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val uri = FileProvider.getUriForFile(this, authority, file)
        intent.setDataAndType(uri, "audio/*")
    } else {
        intent.setDataAndType(file.toUri(), "audio/*")
    }
    this.startActivity(intent)
}

/** 从intent中根据[nameKey]来获取传递过来的对象的值，默认值为[defaultValue] */
inline fun <reified T> Activity.intentExtrasNoNull(nameKey: String, defaultValue: T): Lazy<T> = lazy {
    val value = getIntentExtras(nameKey, defaultValue)
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的ArrayList对象的值，默认值为[defaultValue] */
inline fun <reified T> Activity.intentExtrasNoNull(nameKey: String, defaultValue: ArrayList<T>): Lazy<ArrayList<T>> = lazy {
    val value = getIntentExtras(nameKey, defaultValue)
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Parcelable对象的值，默认值为[defaultValue] */
inline fun <reified T : Parcelable> Activity.intentParcelableExtrasNoNull(nameKey: String, defaultValue: T): Lazy<T> = lazy {
    val value = getIntentExtras<Parcelable>(nameKey, defaultValue) as? T
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Array<Parcelable>对象的值，默认值为[defaultValue] */
fun Activity.intentParcelableExtrasNoNull(nameKey: String, defaultValue: Array<Parcelable>): Lazy<Array<Parcelable>> = lazy {
    val value = getIntentExtras(nameKey, defaultValue)
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的Serializable对象的值，默认值为[defaultValue] */
inline fun <reified T : Serializable> Activity.intentSerializableExtrasNoNull(nameKey: String, defaultValue: T): Lazy<T> = lazy {
    val value = getIntentExtras<Serializable>(nameKey, defaultValue) as? T
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从intent中根据[nameKey]来获取传递过来的对象的值 */
inline fun <reified T> Activity.intentExtras(nameKey: String): Lazy<T?> = lazy {
    return@lazy getIntentExtras<T>(nameKey)
}

/** 从intent中根据[nameKey]来获取传递过来的Parcelable对象的值 */
inline fun <reified T : Parcelable> Activity.intentParcelableExtras(nameKey: String): Lazy<T?> = lazy {
    return@lazy getIntentExtras<Parcelable>(nameKey) as? T
}

/** 从intent中根据[nameKey]来获取传递过来的Array<Parcelable>对象的值 */
fun Activity.intentParcelableArrayExtras(nameKey: String): Lazy<Array<Parcelable>?> = lazy {
    return@lazy getIntentExtras<Array<Parcelable>>(nameKey)
}

/** 从intent中根据[nameKey]来获取传递过来的Serializable对象的值 */
inline fun <reified T : Serializable> Activity.intentSerializableExtras(nameKey: String): Lazy<T?> = lazy {
    return@lazy getIntentExtras<Serializable>(nameKey) as? T
}

/** 从intent中根据[nameKey]来获取传递过来的ArrayList对象的值 */
inline fun <reified T> Activity.intentListExtras(nameKey: String): Lazy<ArrayList<T>?> = lazy {
    return@lazy getIntentExtras<ArrayList<T>>(nameKey)
}

@JvmOverloads
inline fun <reified T> Activity.getIntentExtras(nameKey: String, defaultValue: T? = null): T? = when (T::class) {
    Int::class -> this.intent?.getIntExtra(nameKey, defaultValue as? Int ?: 0) as? T
    Short::class -> this.intent?.getShortExtra(nameKey, defaultValue as? Short ?: 0) as? T
    Byte::class -> this.intent?.getByteExtra(nameKey, defaultValue as? Byte ?: 0) as? T
    Char::class -> this.intent?.getCharExtra(nameKey, defaultValue as? Char ?: Char.MIN_VALUE) as? T
    Boolean::class -> this.intent?.getBooleanExtra(nameKey, defaultValue as? Boolean ?: false) as? T
    Long::class -> this.intent?.getLongExtra(nameKey, defaultValue as? Long ?: 0) as? T
    Float::class -> this.intent?.getFloatExtra(nameKey, defaultValue as? Float ?: 0.0f) as? T
    Double::class -> this.intent?.getDoubleExtra(nameKey, defaultValue as? Double ?: 0.0) as? T
    String::class -> (this.intent?.getStringExtra(nameKey) as? T) ?: defaultValue

    Bundle::class -> (this.intent?.getBundleExtra(nameKey) as? T) ?: defaultValue
    CharSequence::class -> (this.intent?.getCharSequenceExtra(nameKey) as? T) ?: defaultValue
    Parcelable::class -> (this.intent?.getParcelableExtra(nameKey) as? T) ?: defaultValue

    IntArray::class -> (this.intent?.getIntArrayExtra(nameKey) as? T) ?: defaultValue
    ShortArray::class -> (this.intent?.getShortArrayExtra(nameKey) as? T) ?: defaultValue
    ByteArray::class -> (this.intent?.getByteArrayExtra(nameKey) as? T) ?: defaultValue
    CharArray::class -> (this.intent?.getCharArrayExtra(nameKey) as? T) ?: defaultValue
    BooleanArray::class -> (this.intent?.getBooleanArrayExtra(nameKey) as? T) ?: defaultValue
    LongArray::class -> (this.intent?.getLongArrayExtra(nameKey) as? T) ?: defaultValue
    FloatArray::class -> (this.intent?.getFloatArrayExtra(nameKey) as? T) ?: defaultValue
    DoubleArray::class -> (this.intent?.getDoubleArrayExtra(nameKey) as? T) ?: defaultValue

    Array<String>::class -> (this.intent?.getStringArrayExtra(nameKey) as? T) ?: defaultValue
    Array<Parcelable>::class -> (this.intent?.getParcelableArrayExtra(nameKey) as? T) ?: defaultValue
    Array<CharSequence>::class -> (this.intent?.getCharSequenceArrayExtra(nameKey) as? T) ?: defaultValue

    ArrayList::class -> (this.intent?.getStringArrayListExtra(nameKey) as? T) ?: defaultValue

    Serializable::class -> (this.intent?.getSerializableExtra(nameKey) as? T) ?: defaultValue
    else -> null
}

/** 从arguments中根据[nameKey]来获取传递过来的对象的值，默认值为[defaultValue] */
inline fun <reified T> Fragment.argumentsExtrasNoNull(nameKey: String, defaultValue: T): Lazy<T> = lazy {
    val value = getArgumentsExtras(nameKey, defaultValue)
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的ArrayList对象的值，默认值为[defaultValue] */
inline fun <reified T> Fragment.argumentsExtrasNoNull(nameKey: String, defaultValue: ArrayList<T>): Lazy<ArrayList<T>> = lazy {
    val value = getArgumentsExtras(nameKey, defaultValue)
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Parcelable对象的值，默认值为[defaultValue] */
inline fun <reified T : Parcelable> Fragment.argumentsParcelableExtrasNoNull(nameKey: String, defaultValue: T): Lazy<T> = lazy {
    val value = getArgumentsExtras<Parcelable>(nameKey, defaultValue) as? T
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Array<Parcelable>对象的值，默认值为[defaultValue] */
fun Fragment.argumentsParcelableExtrasNoNull(nameKey: String, defaultValue: Array<Parcelable>): Lazy<Array<Parcelable>> = lazy {
    val value = getArgumentsExtras(nameKey, defaultValue)
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的Serializable对象的值，默认值为[defaultValue] */
inline fun <reified T : Serializable> Fragment.argumentsSerializableExtrasNoNull(nameKey: String, defaultValue: T): Lazy<T> = lazy {
    val value = getArgumentsExtras<Serializable>(nameKey, defaultValue) as? T
    if (value != null) {
        return@lazy value
    }
    return@lazy defaultValue
}

/** 从arguments中根据[nameKey]来获取传递过来的对象的值 */
inline fun <reified T> Fragment.argumentsExtras(nameKey: String): Lazy<T?> = lazy {
    return@lazy getArgumentsExtras<T>(nameKey)
}

/** 从arguments中根据[nameKey]来获取传递过来的Parcelable对象的值 */
inline fun <reified T : Parcelable> Fragment.argumentsParcelableExtras(nameKey: String): Lazy<T?> = lazy {
    return@lazy getArgumentsExtras<Parcelable>(nameKey) as? T
}

/** 从arguments中根据[nameKey]来获取传递过来的Array<Parcelable>对象的值 */
fun Fragment.argumentsParcelableArrayExtras(nameKey: String): Lazy<Array<Parcelable>?> = lazy {
    return@lazy getArgumentsExtras<Array<Parcelable>>(nameKey)
}

/** 从arguments中根据[nameKey]来获取传递过来的Serializable对象的值 */
inline fun <reified T : Serializable> Fragment.argumentsSerializableExtras(nameKey: String): Lazy<T?> = lazy {
    return@lazy getArgumentsExtras<Serializable>(nameKey) as? T
}

/** 从arguments中根据[nameKey]来获取传递过来的ArrayList对象的值 */
inline fun <reified T> Fragment.argumentsListExtras(nameKey: String): Lazy<ArrayList<T>?> = lazy {
    return@lazy getArgumentsExtras<ArrayList<T>>(nameKey)
}

@JvmOverloads
inline fun <reified T> Fragment.getArgumentsExtras(nameKey: String, defaultValue: T? = null): T? = when (T::class) {
    Int::class -> this.arguments?.getInt(nameKey, defaultValue as? Int ?: 0) as? T
    Short::class -> this.arguments?.getShort(nameKey, defaultValue as? Short ?: 0) as? T
    Byte::class -> this.arguments?.getByte(nameKey, defaultValue as? Byte ?: 0) as? T
    Char::class -> this.arguments?.getChar(nameKey, defaultValue as? Char ?: Char.MIN_VALUE) as? T
    Boolean::class -> this.arguments?.getBoolean(nameKey, defaultValue as? Boolean ?: false) as? T
    Long::class -> this.arguments?.getLong(nameKey, defaultValue as? Long ?: 0) as? T
    Float::class -> this.arguments?.getFloat(nameKey, defaultValue as? Float ?: 0.0f) as? T
    Double::class -> this.arguments?.getDouble(nameKey, defaultValue as? Double ?: 0.0) as? T
    String::class -> (this.arguments?.getString(nameKey) as? T) ?: defaultValue

    Bundle::class -> (this.arguments?.getBundle(nameKey) as? T) ?: defaultValue
    CharSequence::class -> (this.arguments?.getCharSequence(nameKey) as? T) ?: defaultValue
    Parcelable::class -> (this.arguments?.getParcelable(nameKey) as? T) ?: defaultValue

    IntArray::class -> (this.arguments?.getIntArray(nameKey) as? T) ?: defaultValue
    ShortArray::class -> (this.arguments?.getShortArray(nameKey) as? T) ?: defaultValue
    ByteArray::class -> (this.arguments?.getByteArray(nameKey) as? T) ?: defaultValue
    CharArray::class -> (this.arguments?.getCharArray(nameKey) as? T) ?: defaultValue
    BooleanArray::class -> (this.arguments?.getBooleanArray(nameKey) as? T) ?: defaultValue
    LongArray::class -> (this.arguments?.getLongArray(nameKey) as? T) ?: defaultValue
    FloatArray::class -> (this.arguments?.getFloatArray(nameKey) as? T) ?: defaultValue
    DoubleArray::class -> (this.arguments?.getDoubleArray(nameKey) as? T) ?: defaultValue

    Array<String>::class -> (this.arguments?.getStringArray(nameKey) as? T) ?: defaultValue
    Array<Parcelable>::class -> (this.arguments?.getParcelableArray(nameKey) as? T) ?: defaultValue
    Array<CharSequence>::class -> (this.arguments?.getCharSequenceArray(nameKey) as? T) ?: defaultValue

    ArrayList::class -> (this.arguments?.getStringArrayList(nameKey) as? T) ?: defaultValue

    Serializable::class -> (this.arguments?.getSerializable(nameKey) as? T) ?: defaultValue
    else -> null
}

fun Intent.intentOf(vararg pairs: Pair<String, Any>): Intent = this.putExtras(bundleOf(*pairs))