package com.lodz.android.agiledevkt.ui.image

import android.animation.ObjectAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RemoteViews
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.NotificationTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.ui.main.MainActivity
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.corekt.utils.NotificationUtils
import com.lodz.android.corekt.utils.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.imageloaderkt.glide.transformations.RoundedCornersTransformation

/**
 * Glide测试
 * Created by zhouL on 2018/7/12.
 */
class GlideActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, GlideActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 主频道id  */
    private val NOTIFI_CHANNEL_MAIN_ID = "c0001"
    /** 网络图片地址  */
    private val IMG_URL = "http://hiphotos.baidu.com/zhidao/pic/item/d439b6003af33a87dd932ba4cd5c10385243b595.jpg"
    /** 网络gif地址  */
    private val GIF_URL = "http://image2.sina.com.cn/gm/ol/cross/tujian/446034.gif"
    /** 图片的BASE64  */
    private val PIC_BASE64 = "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"

    /** 自定义通知栏 */
    @BindView(R.id.custom_notification_button)
    lateinit var mCustomNotifyBtn: Button
    /** 原生通知栏 */
    @BindView(R.id.notification_button)
    lateinit var mSystemNotifyBtn: Button

    /** BASE64图片 */
    @BindView(R.id.base64_img)
    lateinit var mBase64Img: ImageView
    /** 本地居中剪切图片 */
    @BindView(R.id.local_crop_img)
    lateinit var mLocalCropImg: ImageView
    /** 网络图片 */
    @BindView(R.id.url_img)
    lateinit var mUrlImg: ImageView
    /** 动画显示 */
    @BindView(R.id.anim_img)
    lateinit var mAnimImg: ImageView
    /** 网络gif */
    @BindView(R.id.url_gif_img)
    lateinit var mUrlGifImg: ImageView
    /** 本地gif */
    @BindView(R.id.local_gif_img)
    lateinit var mLocalGifImg: ImageView
    /** 毛玻璃 */
    @BindView(R.id.blur_img)
    lateinit var mBlurImg: ImageView
    /** 覆盖颜色 */
    @BindView(R.id.filter_color_img)
    lateinit var mFilterColorImg: ImageView
    /** 全圆角 */
    @BindView(R.id.corners_all_img)
    lateinit var mCornersAllImg: ImageView
    /** 顶部圆角 */
    @BindView(R.id.corners_top_img)
    lateinit var mCornersTopImg: ImageView
    /** 灰度化 */
    @BindView(R.id.grayscale_img)
    lateinit var mGrayscaleImg: ImageView
    /** 圆角/灰度化 */
    @BindView(R.id.corners_grayscale_img)
    lateinit var mCornersGrayscaleImg: ImageView
    /** 圆形 */
    @BindView(R.id.circle_img)
    lateinit var mCircleImg: ImageView
    /** 圆形/毛玻璃 */
    @BindView(R.id.circle_blur_img)
    lateinit var mCircleBlurImg: ImageView
    /** 圆形/灰度化/毛玻璃 */
    @BindView(R.id.circle_grayscale_blur_img)
    lateinit var mCircleGrayscaleBlurImg: ImageView
    /** 蒙板效果 */
    @BindView(R.id.mask_img)
    lateinit var mMaskImg: ImageView
    /** 正方形 */
    @BindView(R.id.square_img)
    lateinit var mSquareImg: ImageView
    /** 正方形/毛玻璃 */
    @BindView(R.id.square_blur_img)
    lateinit var mSquareBlurImg: ImageView
    /** 本地webp */
    @BindView(R.id.local_webp_img)
    lateinit var mLocalWebpImg: ImageView
    /** 本地视频 */
    @BindView(R.id.local_video_img)
    lateinit var mLocalVideoImg: ImageView

    override fun getLayoutId() = R.layout.activity_glide

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 自定义通知栏
        mCustomNotifyBtn.setOnClickListener {
            val builder = NotificationCompat.Builder(getContext(), NOTIFI_CHANNEL_MAIN_ID)
            builder.setTicker("")// 通知栏显示的文字
            builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
            builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
            builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
            builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面

            val remoteViews = RemoteViews(packageName, R.layout.notify_glide_custom)
            remoteViews.setImageViewResource(R.id.remoteview_icon, R.drawable.ic_launcher)
            remoteViews.setTextViewText(R.id.remoteview_title, "耿鬼")
            remoteViews.setTextViewText(R.id.remoteview_msg, "鬼斯-鬼斯通-耿鬼")
            builder.setContent(remoteViews)

            ImageLoader.create(getContext())
                    .load(IMG_URL)
                    .setCenterCrop()
                    .asBitmapInto(NotificationTarget(getContext(), R.id.remoteview_icon, remoteViews, builder.build(), 1235))
        }

        // 原生通知栏
        mSystemNotifyBtn.setOnClickListener {
            ImageLoader.create(getContext())
                    .load(IMG_URL)
                    .setCenterCrop()
                    .asBitmapInto(object :SimpleTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val builder = NotificationCompat.Builder(getContext(), NOTIFI_CHANNEL_MAIN_ID)
                            builder.setTicker("震惊！谁是超能力霸主")// 通知栏显示的文字
                            builder.setContentTitle("胡地")// 通知栏通知的标题
                            builder.setContentText("凯西-勇吉拉-胡地")// 通知栏通知的详细内容（只有一行）
                            builder.setAutoCancel(true)// 设置为true，点击该条通知会自动删除，false时只能通过滑动来删除（一般都是true）
                            builder.setLargeIcon(resource)
                            builder.setSmallIcon(R.mipmap.ic_launcher)//通知上面的小图标（必传）
                            builder.setDefaults(NotificationCompat.DEFAULT_ALL)//通知默认的声音 震动 呼吸灯
                            builder.priority = NotificationCompat.PRIORITY_DEFAULT//设置优先级，级别高的排在前面
                            NotificationUtils.create(getContext()).send(builder.build())
                        }
                    })
        }
    }

    override fun initData() {
        super.initData()
        initNotificationChannel()
        showLocalImg()// 本地图片
        showLocalCropImg()// 本地居中剪切图片
        showUrlImg()// 网络图片
        showAnimImg()// 动画显示
        showUrlGifImg()// 网络gif
        showLocalGifImg()// 本地gif
        showBlurImg()// 毛玻璃
        showFilterColorImg()// 覆盖颜色
        showCornersAllImg()// 全圆角
        showCornersTopImg()// 顶部圆角
        showGrayscaleImg()// 灰度化
        showCornersGrayscaleImg()// 圆角/灰度化
        showCircleImg()// 圆形
        showCircleBlurImg()// 圆形/毛玻璃
        showCircleGrayscaleBlurImg()// 圆形/灰度化/毛玻璃
        showMaskImg()// 蒙板效果
        showSquareImg()// 正方形
        showSquareBlurImg()// 正方形/毛玻璃
        showLocalWebpImg()// 本地webp
        showLocalVideoImg()// 本地视频
        showStatusCompleted()
    }

    /** 初始化通知通道 */
    private fun initNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = getMainChannel()
            if (channel != null){
                NotificationUtils.create(getContext()).createNotificationChannel(channel) // 设置频道
            }
        }
    }

    /** 获取主通道 */
    private fun getMainChannel(): NotificationChannel? {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFI_CHANNEL_MAIN_ID, "主通知", NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)// 开启指示灯，如果设备有的话。
            channel.lightColor = Color.GREEN// 设置指示灯颜色
            channel.description = "应用主通知频道"// 通道描述
            channel.enableVibration(true)// 开启震动
            channel.vibrationPattern = longArrayOf(100, 200, 400, 300, 100)// 设置震动频率
            channel.canBypassDnd()// 检测是否绕过免打扰模式
            channel.setBypassDnd(true)// 设置绕过免打扰模式
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            channel.canShowBadge()// 检测是否显示角标
            channel.setShowBadge(true)// 设置是否显示角标
            return channel
        }
        return null
    }

    /** BASE64图片 */
    private fun showLocalImg(){
        ImageLoader.create(getContext())
                .load(BitmapUtils.base64ToByte(PIC_BASE64))
                .setCenterInside()
                .into(mBase64Img)
    }

    /** 本地居中剪切图片  */
    private fun showLocalCropImg() {
        ImageLoader.create(getContext())
                .load(R.drawable.bg_pokemon)
                .setCenterCrop()
                .into(mLocalCropImg)
    }

    /** 网络图片 */
    private fun showUrlImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .setFitCenter()
                .setRequestListener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        if (e != null){
                            PrintLog.e("testtag", model?.toString() ?: "", e)
                        }
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        PrintLog.d("testtag", model?.toString() ?: "")
                        return false
                    }
                })
                .into(mUrlImg)
    }

    /** 动画显示 */
    private fun showAnimImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .setAnim(object :ViewPropertyTransition.Animator{
                    override fun animate(view: View) {
                        view.alpha = 0.5f
                        val fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f)
                        fadeAnim.duration = 5000
                        fadeAnim.start()
                    }
                })
                .setFitCenter()
                .into(mAnimImg)
    }

    /** 网络gif */
    private fun showUrlGifImg() {
        ImageLoader.create(getContext())
                .load(GIF_URL)
                .setFitCenter()
                .into(mUrlGifImg)
    }

    /** 本地gif */
    private fun showLocalGifImg() {
        ImageLoader.create(getContext())
                .load(R.drawable.ic_gif)
                .setFitCenter()
                .into(mLocalGifImg)
    }

    /** 毛玻璃 */
    private fun showBlurImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useBlur()
                .setFitCenter()
                .into(mBlurImg)
    }

    /** 覆盖颜色 */
    private fun showFilterColorImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .setFitCenter()
                .setFilterColor(ContextCompat.getColor(getContext(), R.color.color_60ea413c))
                .useFilterColor()
                .into(mFilterColorImg)
    }

    /** 全圆角 */
    private fun showCornersAllImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useRoundCorner()
                .setRoundCorner(10)
                .setRoundedCornerType(RoundedCornersTransformation.CornerType.ALL)
                .setFitCenter()
                .into(mCornersAllImg)
    }

    /** 顶部圆角 */
    private fun showCornersTopImg() {
        ImageLoader.create(getContext())
                .load(R.drawable.ic_regret)
                .useRoundCorner()
                .setRoundCorner(10)
                .setRoundedCornerType(RoundedCornersTransformation.CornerType.TOP)
                .setCenterCrop()
                .into(mCornersTopImg)
    }

    /** 灰度化 */
    private fun showGrayscaleImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .setFitCenter()
                .useGrayscale()
                .into(mGrayscaleImg)
    }

    /** 圆角/灰度化 */
    private fun showCornersGrayscaleImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useRoundCorner()
                .setRoundCorner(10)
                .useGrayscale()
                .setFitCenter()
                .into(mCornersGrayscaleImg)
    }

    /** 圆形 */
    private fun showCircleImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useCircle()
                .setFitCenter()
                .into(mCircleImg)
    }

    /** 圆形/毛玻璃 */
    private fun showCircleBlurImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useBlur()
                .useCircle()
                .setFitCenter()
                .into(mCircleBlurImg)
    }

    /** 圆形/灰度化/毛玻璃 */
    private fun showCircleGrayscaleBlurImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useCircle()
                .useBlur()
                .useGrayscale()
                .setFitCenter()
                .into(mCircleGrayscaleBlurImg)
    }

    /** 蒙板效果 */
    private fun showMaskImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useMask()
                .setFitCenter()
                .into(mMaskImg)
    }

    /** 正方形 */
    private fun showSquareImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .setCenterCrop()
                .useCropSquare()
                .into(mSquareImg)
    }

    /** 正方形/毛玻璃 */
    private fun showSquareBlurImg() {
        ImageLoader.create(getContext())
                .load(IMG_URL)
                .useBlur()
                .setCenterCrop()
                .useCropSquare()
                .into(mSquareBlurImg)
    }

    /** 本地webp */
    private fun showLocalWebpImg() {
        ImageLoader.create(getContext())
                .load(R.drawable.ic_webp)
                .setFitCenter()
                .into(mLocalWebpImg)
    }

    /** 本地视频 */
    private fun showLocalVideoImg() {
        val filePath = FileManager.getDownloadFolderPath() + "20170422.mp4"

        val file = FileUtils.create(filePath)
        if (file == null || !FileUtils.isFileExists(file)){
            toastShort( R.string.glide_video_file_unexists)
            return
        }
        ImageLoader.create(getContext())
                .load(Uri.fromFile(file))
                .setVideo()
                .into(mLocalVideoImg)
    }

}