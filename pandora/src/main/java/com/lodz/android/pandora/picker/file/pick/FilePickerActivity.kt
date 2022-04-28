package com.lodz.android.pandora.picker.file.pick

import android.content.Context
import android.content.Intent
import android.view.View
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.databinding.PandoraActivityPickerBinding
import com.lodz.android.pandora.picker.file.PickerBean
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 图片选择页面
 * Created by zhouL on 2018/12/20.
 */
internal open class FilePickerActivity<V : View, T : Any> : AbsActivity() {

    companion object {
        private var sPickerBean: PickerBean<*, *>? = null

        internal fun <V : View, T : Any> start(context: Context, pickerBean: PickerBean<V, T>, flags: List<Int>?) {
            synchronized(PickerBean::class.java) {
                if (sPickerBean != null) {
                    return
                }
                sPickerBean = pickerBean
                val intent = Intent(context, FilePickerActivity::class.java)
                flags?.forEach {
                    intent.addFlags(it)
                }
                context.startActivity(intent)
            }
        }
    }

    private val mBinding: PandoraActivityPickerBinding by bindingLayout(PandoraActivityPickerBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root
//
//    /** 列表适配器 */
//    private lateinit var mPdrAdapter: FilePickerAdapter<T>
//
//    /** 选择器数据 */
//    @Suppress("UNCHECKED_CAST")
//    private val mPdrPickerBean: PickerBean<V, T>? by lazy { sPickerBean as PickerBean<V, T> }
//
//    /** 当前文件 */
//    private val mPdrCurrentPhotoList = ArrayList<WrapperData<T>>()
//    /** 已选中的文件 */
//    private val mPdrSelectedList = ArrayList<WrapperData<T>>()
//
//    /** 当前展示的图片文件夹 */
//    private var mPdrCurrentImageFolder: ImageFolder? = null
//
//    override fun findViews(savedInstanceState: Bundle?) {
//        super.findViews(savedInstanceState)
//        val bean = mPdrPickerBean
//        if (bean == null) {
//            finish()
//            return
//        }
//        mPdrPickerBean?.lifecycleObserver?.onCreate { lifecycle }
//        initRecyclerView(bean)
//
//        //页面绘制
//        mBinding.pdrRootLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getItemBgColor()))
//        mBinding.pdrTopLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getTopLayoutColor()))//设置顶部栏背景色
//        mBinding.pdrBottomLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getBottomLayoutColor()))//设置底部栏背景色
//        mBinding.pdrTitleTv.setTextColor(getColorCompat(bean.pickerUIConfig.getMainTextColor()))//设置标题文字颜色
//        mBinding.pdrFolderNameTv.setTextColor(getColorCompat(bean.pickerUIConfig.getMainTextColor()))//设置文件夹名称文字颜色
//        drawBackBtn(bean.pickerUIConfig.getBackBtnColor())//绘制返回按钮
//        if (bean.pickerUIConfig.getMoreFolderImg() != 0) {
//            mBinding.pdrMoreImg.setImageResource(bean.pickerUIConfig.getMoreFolderImg())//设置更多文件夹图片
//        } else {
//            drawMoreImg(bean.pickerUIConfig.getMainTextColor())//绘制默认更多文件夹图标
//        }
//        drawConfirmBtn(bean)//绘制确认按钮
//        mBinding.pdrPreviewBtn.visibility = if (bean.isNeedPreview) View.VISIBLE else View.GONE
//        mBinding.pdrPreviewBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(//绘制预览按钮
//                getContext(),
//                bean.pickerUIConfig.getPreviewBtnNormal(),
//                bean.pickerUIConfig.getPreviewBtnPressed(),
//                bean.pickerUIConfig.getPreviewBtnUnable()
//        ))
//        mBinding.pdrConfirmBtn.isEnabled = false
//        mBinding.pdrPreviewBtn.isEnabled = false
//        //设置状态栏和导航栏颜色
//        StatusBarUtil.setColor(window, getColorCompat(bean.pickerUIConfig.getStatusBarColor()))
//        StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.pickerUIConfig.getNavigationBarColor()))
//    }
//
//    /** 初始化RecyclerView */
//    private fun initRecyclerView(bean: PickerBean<V, T>) {
//        val layoutManager = GridLayoutManager(getContext(), 3)
//        layoutManager.orientation = RecyclerView.VERTICAL
//        mPdrAdapter = FilePickerAdapter(getContext(), bean.imgLoader, bean.isNeedCamera, bean.pickerUIConfig)
//        mBinding.pdrPickerPhototRv.layoutManager = layoutManager
//        mBinding.pdrPickerPhototRv.setHasFixedSize(true)
//        mBinding.pdrPickerPhototRv.adapter = mPdrAdapter
//    }
//
//    override fun onPressBack(): Boolean {
//        mPdrPickerBean?.filePickerListener?.onPickerSelected(ArrayList())
//        return super.onPressBack()
//    }
//
//    override fun setListeners() {
//        super.setListeners()
//        // 返回按钮
//        mBinding.pdrBackBtn.setOnClickListener {
//            mPdrPickerBean?.filePickerListener?.onPickerSelected(ArrayList())
//            finish()
//        }
//
//        // 文件夹按钮
//        mBinding.pdrFolderBtn.setOnClickListener {
//            val bean = mPdrPickerBean ?: return@setOnClickListener
//            Observable.just("")
//                .map {
//                    if (mPdrCurrentImageFolder == null) {
//                        mPdrCurrentImageFolder = AlbumUtils.getTotalImageFolder(getContext())
//                    }
//
//                    val list = ArrayList<FolderItemBean>()
//                    val folders = AlbumUtils.getAllImageFolders(getContext())
//                    // 组装数据
//                    for (folder in folders) {
//                        val itemBean = FolderItemBean()
//                        itemBean.imageFolder = folder
//                        itemBean.isSelected = mPdrCurrentImageFolder?.dir.equals(folder.dir)
//                        list.add(itemBean)
//                    }
//                    return@map list
//                }
//                .compose(RxUtils.ioToMainObservable())
//                .compose(bindDestroyEvent())
//                .subscribe(object : ProgressObserver<List<FolderItemBean>>() {
//                    override fun onPgNext(any: List<FolderItemBean>) {
//                        val dialog = FolderDialog(getContext())
//                        dialog.setOnImgLoader(bean.imgLoader)
//                        dialog.setPickerUIConfig(bean.pickerUIConfig)
//                        dialog.setData(any)
//                        dialog.setOnCancelListener { dialogInterface ->
//                            dialogInterface.dismiss()
//                            mBinding.pdrMoreImg.startRotateSelf(-180, 0, 500, true)
//                        }
//                        dialog.setListener { dialogInterface, folderItemBean ->
//                            dialogInterface.dismiss()
//                            val imageFolder = folderItemBean.imageFolder ?: return@setListener
//                            if (mPdrCurrentImageFolder?.dir.equals(imageFolder.dir)) {// 选择了同一个文件夹
//                                return@setListener
//                            }
//                            mPdrCurrentImageFolder = imageFolder
//                            mBinding.pdrFolderNameTv.text = imageFolder.name
//                            configAdapterData(imageFolder.picList)
//                            mBinding.pdrMoreImg.startRotateSelf(-180, 0, 500, true)
//                        }
//                        dialog.show()
//                        mBinding.pdrMoreImg.startRotateSelf(0, -180, 500, true)
//                    }
//
//                    override fun onPgError(e: Throwable, isNetwork: Boolean) {}
//                }.create(getContext(), R.string.pandora_picker_folder_loading, true))
//        }
//
//        // 确定按钮
//        mBinding.pdrConfirmBtn.setOnClickListener {
//            val list = ArrayList<T>()
//            for (item in mPdrSelectedList) {
//                list.add(item.data)
//            }
//            mPdrPickerBean?.filePickerListener?.onPickerSelected(list)
//            finish()
//        }
//
//        // 预览按钮
//        mBinding.pdrPreviewBtn.setOnClickListener {
//            val bean = mPdrPickerBean ?: return@setOnClickListener
//            if (!bean.isNeedPreview) {// 不需要预览
//                return@setOnClickListener
//            }
//            val list = ArrayList<T>()
//            for (item in mPdrSelectedList) {
//                val data = item.data
//                if (data is DocumentFile || data is File){
//                    val file = when (data) {
//                        is DocumentFile -> data
//                        is File -> DocumentFile.fromFile(data)
//                        else -> null
//                    }
//                    if (file != null){
//                        val mimeType = file.type
//                        if (mimeType != null && mimeType.startsWith("image/")) {
//                            list.add(data)
//                        }
//                        continue
//                    }
//                }
//                list.add(data)
//            }
//            if (list.size > 0){
//                if (mPdrSelectedList.size != list.size){
//                    toastShort(R.string.pandora_picker_preview_photo_only)
//                }
//                previewPic(list, true)
//            }
//        }
//
//        mPdrAdapter.setListener(object : FilePickerAdapter.Listener<T> {
//            override fun onSelected(bean: WrapperData<T>, position: Int) {
//                val pickerBean = mPdrPickerBean ?: return
//
//                if (mPdrSelectedList.size == pickerBean.maxCount && !bean.isSelected) {// 已经选满图片且用户点击了可选图
//                    toastShort(getString(R.string.pandora_picker_photo_count_tips, pickerBean.maxCount.toString()))
//                    return
//                }
//
//                synchronized(this) {
//                    for (i in mPdrCurrentPhotoList.indices) {
//                        if (bean.info.path == mPdrCurrentPhotoList[i].info.path) {
//                            mPdrCurrentPhotoList[i].isSelected = !bean.isSelected//更改选中状态
//                            mPdrAdapter.setData(mPdrCurrentPhotoList)
//                            mPdrAdapter.notifyItemChanged(position)//刷新数据
//                            if (mPdrCurrentPhotoList[i].isSelected) {// 点击后是选中状态
//                                mPdrSelectedList.add(bean)
//                            } else {// 点击后是非选中状态
//                                val iterator = mPdrSelectedList.iterator()
//                                while (iterator.hasNext()) {
//                                    if (iterator.next().info.path == bean.info.path) {
//                                        iterator.remove()// 从选中列表里去除
//                                        break
//                                    }
//                                }
//                            }
//                            // 设置按钮状态
//                            mBinding.pdrConfirmBtn.isEnabled = mPdrSelectedList.isNotEmpty()
//                            mBinding.pdrConfirmBtn.text = if (mPdrSelectedList.isNotEmpty()) {
//                                getString(R.string.pandora_picker_confirm_num, mPdrSelectedList.size.toString(), pickerBean.maxCount.toString())
//                            } else {
//                                getString(R.string.pandora_picker_confirm)
//                            }
//                            mBinding.pdrPreviewBtn.isEnabled = mPdrSelectedList.isNotEmpty()
//                            //可能出现文件和图片混合，因此不再显示预览数字
////                            mBinding.pdrPreviewBtn.text = if (mPdrSelectedList.isNotEmpty()) {
////                                getString(R.string.pandora_picker_preview_num, mPdrSelectedList.size.toString())
////                            } else {
////                                getString(R.string.pandora_picker_preview)
////                            }
//                            return
//                        }
//                    }
//                }
//
//            }
//
//            override fun onClickCamera() {
//                takeCameraPhoto()
//            }
//        })
//
//        // 图片点击回调
//        mPdrAdapter.setOnItemClickListener { viewHolder, item, position ->
//            val bean = mPdrPickerBean ?: return@setOnItemClickListener
//            if (!bean.isNeedPreview) {// 不需要预览
//                return@setOnItemClickListener
//            }
//            val data = item.data
//            if (data is DocumentFile || data is File) {
//                val file = when (data) {
//                    is DocumentFile -> data
//                    is File -> DocumentFile.fromFile(data)
//                    else -> null
//                }
//                if (file != null){
//                    val mimeType = file.type
//                    if (mimeType != null && mimeType.startsWith("image/")) {
//                        previewPic(arrayListOf(data), false)
//                        return@setOnItemClickListener
//                    }
//                    toastShort(R.string.pandora_picker_preview_photo_only)
//                    return@setOnItemClickListener
//                }
//            }
//            previewPic(arrayListOf(data), false)
//        }
//    }
//
//    /** 图片预览器 */
//    private fun previewPic(list: List<T>, isShowPage: Boolean) {
//        val bean = mPdrPickerBean ?: return
//        val view = bean.imgView ?: return
//        PreviewManager.create<V, T>()
//            .setPosition(0)
//            .setPagerTextSize(14)
//            .setShowPagerText(isShowPage)
//            .setBackgroundColor(getContext(), bean.pickerUIConfig.getPreviewBgColor())
//            .setStatusBarColor(getContext(), bean.pickerUIConfig.getStatusBarColor())
//            .setNavigationBarColor(getContext(), bean.pickerUIConfig.getNavigationBarColor())
//            .setPagerTextColor(getContext(), bean.pickerUIConfig.getMainTextColor())
//            .setImageView(view)
//            .build(list)
//            .open(getContext())
//    }
//
//    override fun initData() {
//        super.initData()
//        val bean = mPdrPickerBean
//        if (bean == null) {
//            finish()
//            return
//        }
//        val list = bean.sourceList
//        if (list == null || list.isEmpty()) {// 挑选相册图片
//            mPdrCurrentImageFolder = AlbumUtils.getTotalImageFolder(getContext())
//            configAdapterData(getContext().getAllFiles(bean.mimeTypeArray))
//        } else {// 挑选指定图片
//            mPdrCurrentImageFolder = null
//            configAdapterData(list)//让用户选择指定的图片
//            mBinding.pdrFolderBtn.isEnabled = false
//            mBinding.pdrFolderNameTv.setText(R.string.pandora_picker_custom_file)
//            mBinding.pdrMoreImg.visibility = View.GONE
//        }
//    }
//
//    /** 配置适配器数据 */
//    private fun configAdapterData(source: List<T>) {
//        mPdrCurrentPhotoList.clear()
//        for (info in source) {
//            val itemBean = WrapperData<T>()
//            itemBean.info = info
//            for (selectedBean in mPdrSelectedList) {
//                if (selectedBean.info.path == info.path) {
//                    itemBean.isSelected = true
//                    break
//                }
//            }
//            mPdrCurrentPhotoList.add(itemBean)
//        }
//        mPdrAdapter.setData(mPdrCurrentPhotoList)
//        mBinding.pdrPickerPhototRv.smoothScrollToPosition(0)
//        mPdrAdapter.notifyDataSetChanged()
//    }
//
//    /** 绘制返回按钮，颜色[color] */
//    private fun drawBackBtn(@ColorRes color: Int) {
//        Observable.just(color)
//                .map { resId ->
//                    return@map BitmapUtils.rotateBitmap(getArrowBitmap(resId), 90f)
//                }
//                .compose(RxUtils.ioToMainObservable())
//                .subscribe(object : BaseObserver<Bitmap>() {
//                    override fun onBaseNext(any: Bitmap) {
//                        mBinding.pdrBackBtn.setImageBitmap(any)
//                    }
//
//                    override fun onBaseError(e: Throwable) {}
//                })
//    }
//
//
//    /** 绘制更多图标，颜色[color] */
//    private fun drawMoreImg(@ColorRes color: Int) {
//        Observable.just(color)
//                .map { resId ->
//                    return@map getArrowBitmap(resId)
//                }
//                .compose(RxUtils.ioToMainObservable())
//                .subscribe(object : BaseObserver<Bitmap>() {
//                    override fun onBaseNext(any: Bitmap) {
//                        mBinding.pdrMoreImg.setImageBitmap(any)
//                    }
//
//                    override fun onBaseError(e: Throwable) {}
//                })
//    }
//
//    /** 获取箭头图片，颜色[color] */
//    private fun getArrowBitmap(@ColorRes color: Int): Bitmap {
//        val side = dp2pxRF(20)
//        val centerPoint = side / 2
//
//        val bitmap = Bitmap.createBitmap(side.toInt(), side.toInt(), Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        canvas.drawColor(Color.TRANSPARENT)
//        val paint = Paint()
//        paint.color = getColorCompat(color)
//        paint.strokeWidth = 7f
//        paint.isAntiAlias = true
//        paint.style = Paint.Style.STROKE
//        val path = Path()
//        path.moveTo(centerPoint, centerPoint + dp2pxRF(3))
//        path.lineTo(centerPoint - dp2pxRF(7), centerPoint - dp2pxRF(3))
//        path.moveTo(centerPoint, centerPoint + dp2pxRF(3))
//        path.lineTo(centerPoint + dp2pxRF(7), centerPoint - dp2pxRF(3))
//        canvas.drawPath(path, paint)
//
//        paint.isAntiAlias = true
//        paint.style = Paint.Style.FILL
//        canvas.drawCircle(centerPoint, centerPoint + dp2pxRF(3), 3f, paint)
//        return bitmap
//    }
//
//    /** 绘制确定按钮 */
//    private fun drawConfirmBtn(bean: PickerBean<V, T>) {
//        mBinding.pdrConfirmBtn.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                mBinding.pdrConfirmBtn.viewTreeObserver.removeOnPreDrawListener(this)
//                val width = mBinding.pdrConfirmBtn.measuredWidth
//                val height = mBinding.pdrConfirmBtn.measuredHeight
//
//                mBinding.pdrConfirmBtn.background = SelectorUtils.createBgPressedUnableDrawable(
//                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnNormal(), width, height),
//                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnPressed(), width, height),
//                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnUnable(), width, height)
//                )
//
//                mBinding.pdrConfirmBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(
//                        getContext(),
//                        bean.pickerUIConfig.getConfirmTextNormal(),
//                        bean.pickerUIConfig.getConfirmTextPressed(),
//                        bean.pickerUIConfig.getConfirmTextUnable()
//                ))
//                return true
//            }
//        })
//    }
//
//    /** 根据颜色[color]、宽[width]、高[height]来获取圆角Drawable */
//    private fun getCornerDrawable(@ColorRes color: Int, width: Int, height: Int): Drawable {
//        val bitmap = BitmapUtils.drawableToBitmap(createColorDrawable(color), width, height) ?: return createColorDrawable(color)
//        return createBitmapDrawable(BitmapUtils.createRoundedCornerBitmap(bitmap, 8f))
//    }
//
//    /** 拍照 */
//    private fun takeCameraPhoto() {
//        val bean = mPdrPickerBean ?: return
//        TakePhotoManager.create()
//            .setPublicDirectoryName(bean.publicDirectoryName)
//            .setAuthority(bean.authority)
//            .setImmediately(true)
//            .setStatusBarColor(getContext(), bean.pickerUIConfig.getStatusBarColor())
//            .setNavigationBarColor(getContext(), bean.pickerUIConfig.getNavigationBarColor())
//            .setPreviewBgColor(getContext(), bean.pickerUIConfig.getPreviewBgColor())
//            .setOnPhotoTakeListener{
//                if (it == null){
//                    return@setOnPhotoTakeListener
//                }
//                handleCameraSuccess()
//            }
//            .build()
//            .take(getContext())
//    }
//
//    /** 处理拍照成功 */
//    private fun handleCameraSuccess() {
//        val currentFolder = mPdrCurrentImageFolder ?: return
//        val list = AlbumUtils.getAllImageFolders(getContext())
//        for (folder in list) {
//            if (folder.dir == currentFolder.dir) {
//                configAdapterData(folder.picList)
//                break
//            }
//        }
//    }
//
//    override fun finish() {
//        release()
//        super.finish()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mPdrPickerBean?.lifecycleObserver?.onResume { lifecycle }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mPdrPickerBean?.lifecycleObserver?.onStart { lifecycle }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mPdrPickerBean?.lifecycleObserver?.onPause { lifecycle }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mPdrPickerBean?.lifecycleObserver?.onStop { lifecycle }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        release()
//    }
//
//    /** 释放资源 */
//    private fun release() {
//        mPdrPickerBean?.lifecycleObserver?.onDestroy { lifecycle }
//        mPdrAdapter.release()
//        mPdrPickerBean?.clear()
//        sPickerBean?.clear()
//        sPickerBean = null
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onPhotoPickerFinishEvent(event: PhotoPickerFinishEvent) {
//        if (!isFinishing) {
//            finish()
//        }
//    }
//
}