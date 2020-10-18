package com.lodz.android.pandora.photopicker.picker.pick

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.album.ImageFolder
import com.lodz.android.corekt.album.PicInfo
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.*
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.photopicker.picker.PickerBean
import com.lodz.android.pandora.photopicker.picker.PickerItemBean
import com.lodz.android.pandora.photopicker.picker.dialog.ImageFolderDialog
import com.lodz.android.pandora.photopicker.picker.dialog.ImageFolderItemBean
import com.lodz.android.pandora.photopicker.preview.PreviewManager
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.GlobalScope

/**
 * 图片选择页面
 * Created by zhouL on 2018/12/20.
 */
internal class PhotoPickerActivity<V : View> : AbsActivity() {

    companion object {
        private var sPickerBean: PickerBean<*>? = null

        internal fun <V : View> start(context: Context, pickerBean: PickerBean<V>) {
            synchronized(this) {
                if (sPickerBean != null) {
                    return
                }
                sPickerBean = pickerBean
                val intent = Intent(context, PhotoPickerActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    /** 照相请求码 */
    private val REQUEST_CAMERA = 777

    /** 返回按钮 */
    private val mPdrBackBtn by bindView<ImageView>(R.id.pdr_back_btn)
    /** 确定按钮 */
    private val mPdrConfirmBtn by bindView<TextView>(R.id.pdr_confirm_btn)
    /** 列表 */
    private val mPdrRecyclerView by bindView<RecyclerView>(R.id.pdr_picker_photot_rv)
    /** 文件夹按钮 */
    private val mPdrFolderBtn by bindView<ViewGroup>(R.id.pdr_folder_btn)
    /** 文件夹名称 */
    private val mPdrFolderNameTv by bindView<TextView>(R.id.pdr_folder_name_tv)
    /** 更多图片 */
    private val mPdrMoreImg by bindView<ImageView>(R.id.pdr_more_img)
    /** 预览按钮 */
    private val mPdrPreviewBtn by bindView<TextView>(R.id.pdr_preview_btn)

    /** 列表适配器 */
    private lateinit var mPdrAdapter: PhotoPickerAdapter

    /** 选择器数据 */
    private var mPdrPickerBean: PickerBean<V>? = null
    /** 当前照片 */
    private val mPdrCurrentPhotoList = ArrayList<PickerItemBean>()
    /** 已选中的照片 */
    private val mPdrSelectedList = ArrayList<PickerItemBean>()

    /** 临时文件路径 */
    private var mPdrTempFilePath = ""
    /** 当前展示的图片文件夹 */
    private var mPdrCurrentImageFolder: ImageFolder? = null

    @Suppress("UNCHECKED_CAST")
    override fun startCreate() {
        super.startCreate()
        mPdrPickerBean = sPickerBean as PickerBean<V>
    }

    override fun getAbsLayoutId(): Int = R.layout.pandora_activity_picker

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        val rootLayout = findViewById<ViewGroup>(R.id.pdr_root_layout)
        val topLayout = findViewById<ViewGroup>(R.id.pdr_top_layout)
        val bottomLayout = findViewById<ViewGroup>(R.id.pdr_bottom_layout)
        val titleTv = findViewById<TextView>(R.id.pdr_title_tv)

        val bean = mPdrPickerBean
        if (bean == null) {
            finish()
            return
        }

        initRecyclerView(bean)
        rootLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getItemBgColor()))
        topLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getTopLayoutColor()))//设置顶部栏背景色
        bottomLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getBottomLayoutColor()))//设置底部栏背景色
        titleTv.setTextColor(getColorCompat(bean.pickerUIConfig.getMainTextColor()))//设置标题文字颜色
        mPdrFolderNameTv.setTextColor(getColorCompat(bean.pickerUIConfig.getMainTextColor()))//设置文件夹名称文字颜色
        drawBackBtn(bean.pickerUIConfig.getBackBtnColor())//绘制返回按钮
        if (bean.pickerUIConfig.getMoreFolderImg() != 0) {
            mPdrMoreImg.setImageResource(bean.pickerUIConfig.getMoreFolderImg())//设置更多文件夹图片
        } else {
            drawMoreImg(bean.pickerUIConfig.getMainTextColor())//绘制默认更多文件夹图标
        }
        drawConfirmBtn(bean)//绘制确认按钮
        mPdrPreviewBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(//绘制预览按钮
                getContext(),
                bean.pickerUIConfig.getPreviewBtnNormal(),
                bean.pickerUIConfig.getPreviewBtnPressed(),
                bean.pickerUIConfig.getPreviewBtnUnable()
        ))
        mPdrConfirmBtn.isEnabled = false
        mPdrPreviewBtn.isEnabled = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//设置状态栏和导航栏颜色
            StatusBarUtil.setColor(window, getColorCompat(bean.pickerUIConfig.getStatusBarColor()))
            StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.pickerUIConfig.getNavigationBarColor()))
        }
    }

    /** 初始化RecyclerView */
    private fun initRecyclerView(bean: PickerBean<V>) {
        val layoutManager = GridLayoutManager(getContext(), 3)
        layoutManager.orientation = RecyclerView.VERTICAL
        mPdrAdapter = PhotoPickerAdapter(getContext(), bean.imgLoader, bean.isNeedCamera, bean.pickerUIConfig)
        mPdrRecyclerView.layoutManager = layoutManager
        mPdrRecyclerView.setHasFixedSize(true)
        mPdrRecyclerView.adapter = mPdrAdapter
    }

    override fun setListeners() {
        super.setListeners()
        // 返回按钮
        mPdrBackBtn.setOnClickListener {
            finish()
        }

        // 文件夹按钮
        mPdrFolderBtn.setOnClickListener {
            val bean = mPdrPickerBean ?: return@setOnClickListener
            Observable.just("")
                .map {
                    if (mPdrCurrentImageFolder == null) {
                        mPdrCurrentImageFolder = AlbumUtils.getTotalImageFolder(getContext())
                    }

                    val list = ArrayList<ImageFolderItemBean>()
                    val folders = AlbumUtils.getAllImageFolders(getContext())
                    // 组装数据
                    for (folder in folders) {
                        val itemBean = ImageFolderItemBean()
                        itemBean.imageFolder = folder
                        itemBean.isSelected = mPdrCurrentImageFolder?.dir.equals(folder.dir)
                        list.add(itemBean)
                    }
                    return@map list
                }
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(object : ProgressObserver<List<ImageFolderItemBean>>() {
                    override fun onPgNext(any: List<ImageFolderItemBean>) {
                        val dialog = ImageFolderDialog(getContext())
                        dialog.setOnImgLoader(bean.imgLoader)
                        dialog.setPickerUIConfig(bean.pickerUIConfig)
                        dialog.setData(any)
                        dialog.setOnCancelListener { dialogInterface ->
                            dialogInterface.dismiss()
                            mPdrMoreImg.startRotateSelf(-180, 0, 500, true)
                        }
                        dialog.setListener { dialogInterface, folderItemBean ->
                            dialogInterface.dismiss()
                            val imageFolder = folderItemBean.imageFolder ?: return@setListener
                            if (mPdrCurrentImageFolder?.dir.equals(imageFolder.dir)) {// 选择了同一个文件夹
                                return@setListener
                            }
                            mPdrCurrentImageFolder = imageFolder
                            mPdrFolderNameTv.text = imageFolder.name
                            configAdapterData(imageFolder.picList)
                            mPdrMoreImg.startRotateSelf(-180, 0, 500, true)
                        }
                        dialog.show()
                        mPdrMoreImg.startRotateSelf(0, -180, 500, true)
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {}
                }.create(getContext(), R.string.pandora_picker_folder_loading, true))
        }

        // 确定按钮
        mPdrConfirmBtn.setOnClickListener {
            val list = ArrayList<PicInfo>()
            for (itemBean in mPdrSelectedList) {
                list.add(itemBean.info)
            }
            mPdrPickerBean?.photoPickerListener?.onPickerSelected(list)
            finish()
        }

        // 预览按钮
        mPdrPreviewBtn.setOnClickListener {
            val bean = mPdrPickerBean ?: return@setOnClickListener
            val view = bean.imgView ?: return@setOnClickListener

            val list = ArrayList<PicInfo>()
            for (itemBean in mPdrSelectedList) {
                list.add(itemBean.info)// 组装数据
            }

            // 图片预览器
            PreviewManager.create<V, PicInfo>()
                    .setPosition(0)
                    .setPagerTextSize(14)
                    .setShowPagerText(true)
                    .setBackgroundColor(bean.pickerUIConfig.getPreviewBgColor())
                    .setStatusBarColor(bean.pickerUIConfig.getStatusBarColor())
                    .setNavigationBarColor(bean.pickerUIConfig.getNavigationBarColor())
                    .setPagerTextColor(bean.pickerUIConfig.getMainTextColor())
                    .setImageView(view)
                    .build(list)
                    .open(getContext())
        }

        mPdrAdapter.setListener(object : PhotoPickerAdapter.Listener {
            override fun onSelected(bean: PickerItemBean, position: Int) {
                val pickerBean = mPdrPickerBean ?: return

                if (mPdrSelectedList.size == pickerBean.maxCount && !bean.isSelected) {// 已经选满图片且用户点击了可选图
                    toastShort(getString(R.string.pandora_picker_photo_count_tips, pickerBean.maxCount.toString()))
                    return
                }

                synchronized(this) {
                    for (i in mPdrCurrentPhotoList.indices) {
                        if (bean.info.path == mPdrCurrentPhotoList[i].info.path) {
                            mPdrCurrentPhotoList[i].isSelected = !bean.isSelected//更改选中状态
                            mPdrAdapter.setData(mPdrCurrentPhotoList)
                            mPdrAdapter.notifyItemChanged(position)//刷新数据
                            if (mPdrCurrentPhotoList[i].isSelected) {// 点击后是选中状态
                                mPdrSelectedList.add(bean)
                            } else {// 点击后是非选中状态
                                val iterator = mPdrSelectedList.iterator()
                                while (iterator.hasNext()) {
                                    if (iterator.next().info.path == bean.info.path) {
                                        iterator.remove()// 从选中列表里去除
                                        break
                                    }
                                }
                            }
                            // 设置按钮状态
                            mPdrConfirmBtn.isEnabled = mPdrSelectedList.isNotEmpty()
                            mPdrConfirmBtn.text = if (mPdrSelectedList.isNotEmpty()) {
                                getString(R.string.pandora_picker_confirm_num, mPdrSelectedList.size.toString(), pickerBean.maxCount.toString())
                            } else {
                                getString(R.string.pandora_picker_confirm)
                            }
                            mPdrPreviewBtn.isEnabled = mPdrSelectedList.isNotEmpty()
                            mPdrPreviewBtn.text = if (mPdrSelectedList.isNotEmpty()) {
                                getString(R.string.pandora_picker_preview_num, mPdrSelectedList.size.toString())
                            } else {
                                getString(R.string.pandora_picker_preview)
                            }
                            return
                        }
                    }
                }

            }

            override fun onClickCamera() {
                takeCameraPhoto()
            }
        })

        // 图片点击回调
        mPdrAdapter.setOnItemClickListener { viewHolder, item, position ->
            val bean = mPdrPickerBean ?: return@setOnItemClickListener
            if (!bean.isNeedItemPreview) {// 不需要预览
                return@setOnItemClickListener
            }
            val view = bean.imgView ?: return@setOnItemClickListener
            // 图片预览器
            PreviewManager.create<V, PicInfo>()
                    .setShowPagerText(false)
                    .setBackgroundColor(bean.pickerUIConfig.getPreviewBgColor())
                    .setStatusBarColor(bean.pickerUIConfig.getStatusBarColor())
                    .setNavigationBarColor(bean.pickerUIConfig.getNavigationBarColor())
                    .setPagerTextColor(bean.pickerUIConfig.getMainTextColor())
                    .setImageView(view)
                    .build(item.info)
                    .open(getContext())
        }
    }

    override fun initData() {
        super.initData()
        val bean = mPdrPickerBean
        if (bean == null) {
            finish()
            return
        }
        if (bean.sourceList.isNullOrEmpty()) {// 挑选相册图片
            mPdrCurrentImageFolder = AlbumUtils.getTotalImageFolder(getContext())
            configAdapterData(AlbumUtils.getAllImages(getContext()))
        } else {// 挑选指定图片
            mPdrCurrentImageFolder = null
            configAdapterData(bean.sourceList!!)//让用户选择指定的图片
            mPdrFolderBtn.isEnabled = false
            mPdrFolderNameTv.setText(R.string.pandora_picker_custom_photo)
            mPdrMoreImg.visibility = View.GONE
        }
    }

    /** 配置适配器数据 */
    private fun configAdapterData(source: List<PicInfo>) {
        mPdrCurrentPhotoList.clear()
        for (info in source) {
            val itemBean = PickerItemBean()
            itemBean.info = info
            for (selectedBean in mPdrSelectedList) {
                if (selectedBean.info.path == info.path) {
                    itemBean.isSelected = true
                    break
                }
            }
            mPdrCurrentPhotoList.add(itemBean)
        }
        mPdrAdapter.setData(mPdrCurrentPhotoList)
        mPdrRecyclerView.smoothScrollToPosition(0)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 绘制返回按钮，颜色[color] */
    private fun drawBackBtn(@ColorRes color: Int) {
        Observable.just(color)
                .map { resId ->
                    return@map BitmapUtils.rotateBitmap(getArrowBitmap(resId), 90)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mPdrBackBtn.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {}
                })
    }


    /** 绘制更多图标，颜色[color] */
    private fun drawMoreImg(@ColorRes color: Int) {
        Observable.just(color)
                .map { resId ->
                    return@map getArrowBitmap(resId)
                }
                .compose(RxUtils.ioToMainObservable())
                .subscribe(object : BaseObserver<Bitmap>() {
                    override fun onBaseNext(any: Bitmap) {
                        mPdrMoreImg.setImageBitmap(any)
                    }

                    override fun onBaseError(e: Throwable) {}
                })
    }

    /** 获取箭头图片，颜色[color] */
    private fun getArrowBitmap(@ColorRes color: Int): Bitmap {
        val side = dp2pxRF(20)
        val centerPoint = side / 2

        val bitmap = Bitmap.createBitmap(side.toInt(), side.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        paint.color = getColorCompat(color)
        paint.strokeWidth = 7f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        val path = Path()
        path.moveTo(centerPoint, centerPoint + dp2pxRF(3))
        path.lineTo(centerPoint - dp2pxRF(7), centerPoint - dp2pxRF(3))
        path.moveTo(centerPoint, centerPoint + dp2pxRF(3))
        path.lineTo(centerPoint + dp2pxRF(7), centerPoint - dp2pxRF(3))
        canvas.drawPath(path, paint)

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerPoint, centerPoint + dp2pxRF(3), 3f, paint)
        return bitmap
    }

    /** 绘制确定按钮 */
    private fun drawConfirmBtn(bean: PickerBean<V>) {
        mPdrConfirmBtn.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                mPdrConfirmBtn.viewTreeObserver.removeOnPreDrawListener(this)
                val width = mPdrConfirmBtn.measuredWidth
                val height = mPdrConfirmBtn.measuredHeight

                mPdrConfirmBtn.background = SelectorUtils.createBgPressedUnableDrawable(
                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnNormal(), width, height),
                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnPressed(), width, height),
                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnUnable(), width, height)
                )

                mPdrConfirmBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(
                        getContext(),
                        bean.pickerUIConfig.getConfirmTextNormal(),
                        bean.pickerUIConfig.getConfirmTextPressed(),
                        bean.pickerUIConfig.getConfirmTextUnable()
                ))
                return true
            }
        })
    }

    /** 根据颜色[color]、宽[width]、高[height]来获取圆角Drawable */
    private fun getCornerDrawable(@ColorRes color: Int, width: Int, height: Int): Drawable {
        val bitmap = BitmapUtils.drawableToBitmap(createColorDrawable(color), width, height) ?: return createColorDrawable(color)
        return createBitmapDrawable(BitmapUtils.createRoundedCornerBitmap(bitmap, 8f))
    }

    /** 拍照 */
    private fun takeCameraPhoto() {
        val bean = mPdrPickerBean ?: return
        if (!FileUtils.isFileExists(bean.cameraSavePath) && !FileUtils.createFolder(bean.cameraSavePath)) {// 文件夹不存在且创建文件夹失败
            toastShort(R.string.pandora_photo_folder_fail)
            return
        }
        mPdrTempFilePath = "${bean.cameraSavePath}P_${DateUtils.getCurrentFormatString(DateUtils.TYPE_4)}.jpg"
        if (!FileUtils.createNewFile(mPdrTempFilePath)) {
            toastShort(R.string.pandora_photo_temp_file_fail)
            return
        }
        if (Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(packageManager) == null) {
            toastShort(R.string.pandora_no_camera)
            return
        }
        if (!takePhoto(mPdrTempFilePath, bean.authority, REQUEST_CAMERA)) {
            toastShort(R.string.pandora_photo_temp_file_fail)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CAMERA) {//不是拍照请求码
            return
        }
        if (resultCode != Activity.RESULT_OK) {//拍照不成功
            FileUtils.delFile(mPdrTempFilePath)// 删除临时文件
            mPdrTempFilePath = ""
            return
        }
        // 拍照成功
        AlbumUtils.notifyScanImageCompat(getContext(), mPdrTempFilePath)
        GlobalScope.runOnMainDelay(300){
            handleCameraSuccess()
        }
    }

    /** 处理拍照成功 */
    private fun handleCameraSuccess() {
        val currentFolder = mPdrCurrentImageFolder ?: return
        mPdrTempFilePath = ""
        val list = AlbumUtils.getAllImageFolders(getContext())
        for (folder in list) {
            if (folder.dir == currentFolder.dir) {
                configAdapterData(folder.picList)
                break
            }
        }
    }

    override fun finish() {
        mPdrAdapter.release()
        mPdrPickerBean?.clear()
        mPdrPickerBean = null
        sPickerBean?.clear()
        sPickerBean = null
        super.finish()
    }
}