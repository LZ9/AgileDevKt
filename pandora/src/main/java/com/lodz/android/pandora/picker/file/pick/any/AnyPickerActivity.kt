package com.lodz.android.pandora.picker.file.pick.any

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.BitmapUtils
import com.lodz.android.corekt.utils.SelectorUtils
import com.lodz.android.corekt.utils.StatusBarUtil
import com.lodz.android.pandora.R
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.databinding.PandoraActivityPickerBinding
import com.lodz.android.pandora.event.PhotoPickerFinishEvent
import com.lodz.android.pandora.picker.file.PickerBean
import com.lodz.android.pandora.picker.file.pick.DataWrapper
import com.lodz.android.pandora.picker.preview.PreviewManager
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.grid
import com.lodz.android.pandora.widget.rv.anko.setup
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 任意对象选择页面
 * Created by zhouL on 2018/12/20.
 */
@SuppressLint("NotifyDataSetChanged")
internal open class AnyPickerActivity<T> : AbsActivity() {

    companion object {
        var sPickerBean: PickerBean<*>? = null

        internal fun <T> start(context: Context, pickerBean: PickerBean<T>, flags: List<Int>?) {
            synchronized(PickerBean::class.java) {
                if (sPickerBean != null) {
                    return
                }
                sPickerBean = pickerBean
                val intent = Intent(context, AnyPickerActivity::class.java)
                flags?.forEach {
                    intent.addFlags(it)
                }
                context.startActivity(intent)
            }
        }
    }

    protected val mBinding: PandoraActivityPickerBinding by bindingLayout(PandoraActivityPickerBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    /** 列表适配器 */
    private lateinit var mPdrAdapter: AnyPickerAdapter<T>

    /** 选择器数据 */
    @Suppress("UNCHECKED_CAST")
    private val mPdrPickerBean: PickerBean<T>? by lazy { sPickerBean as PickerBean<T> }

    /** 当前文件数据 */
    private val mPdrDataList = ArrayList<DataWrapper<T>>()
    /** 已选中的文件 */
    private val mPdrSelectedList = ArrayList<DataWrapper<T>>()

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        val bean = mPdrPickerBean
        if (bean == null) {
            finish()
            return
        }
        mPdrPickerBean?.lifecycleObserver?.onCreate { lifecycle }
        initRecyclerView(bean)

        //页面绘制
        mBinding.pdrRootLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getItemBgColor()))
        mBinding.pdrTopLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getTopLayoutColor()))//设置顶部栏背景色
        mBinding.pdrBottomLayout.setBackgroundColor(getColorCompat(bean.pickerUIConfig.getBottomLayoutColor()))//设置底部栏背景色
        mBinding.pdrTitleTv.setTextColor(getColorCompat(bean.pickerUIConfig.getMainTextColor()))//设置标题文字颜色
        mBinding.pdrFolderNameTv.setTextColor(getColorCompat(bean.pickerUIConfig.getMainTextColor()))//设置文件夹名称文字颜色
        drawBackBtn(bean.pickerUIConfig.getBackBtnColor())//绘制返回按钮
        if (bean.pickerUIConfig.getMoreFolderImg() != 0) {
            mBinding.pdrMoreImg.setImageResource(bean.pickerUIConfig.getMoreFolderImg())//设置更多文件夹图片
        } else {
            drawMoreImg(bean.pickerUIConfig.getMainTextColor())//绘制默认更多文件夹图标
        }
        drawConfirmBtn(bean)//绘制确认按钮
        mBinding.pdrPreviewBtn.visibility = if (bean.isNeedPreview) View.VISIBLE else View.GONE
        mBinding.pdrPreviewBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(//绘制预览按钮
                getContext(),
                bean.pickerUIConfig.getPreviewBtnNormal(),
                bean.pickerUIConfig.getPreviewBtnPressed(),
                bean.pickerUIConfig.getPreviewBtnUnable()
        ))
        mBinding.pdrConfirmBtn.isEnabled = false
        mBinding.pdrPreviewBtn.isEnabled = false
        //设置状态栏和导航栏颜色
        StatusBarUtil.setColor(window, getColorCompat(bean.pickerUIConfig.getStatusBarColor()))
        StatusBarUtil.setNavigationBarColor(window, getColorCompat(bean.pickerUIConfig.getNavigationBarColor()))
    }

    /** 初始化RecyclerView */
    private fun initRecyclerView(bean: PickerBean<T>) {
        mPdrAdapter = AnyPickerAdapter(getContext(), bean.imgLoader, bean.isNeedCamera, bean.isNeedBottomInfo, bean.pickerUIConfig)
        mPdrAdapter = mBinding.pdrPickerPhototRv
            .grid(3)
            .setup(mPdrAdapter)
    }

    override fun onPressBack(): Boolean {
        mPdrPickerBean?.filePickerListener?.onPickerSelected(ArrayList())
        return super.onPressBack()
    }

    override fun setListeners() {
        super.setListeners()
        // 返回按钮
        mBinding.pdrBackBtn.setOnClickListener {
            mPdrPickerBean?.filePickerListener?.onPickerSelected(ArrayList())
            finish()
        }

        // 文件夹按钮
        mBinding.pdrFolderBtn.setOnClickListener {
            val bean = mPdrPickerBean ?: return@setOnClickListener
            onClickFolderBtn(bean)
        }

        // 确定按钮
        mBinding.pdrConfirmBtn.setOnClickListener {
            val list = ArrayList<T>()
            for (item in mPdrSelectedList) {
                list.add(item.data)
            }
            mPdrPickerBean?.filePickerListener?.onPickerSelected(list)
            finish()
        }

        // 预览按钮
        mBinding.pdrPreviewBtn.setOnClickListener {
            val bean = mPdrPickerBean ?: return@setOnClickListener
            if (!bean.isNeedPreview) {// 不需要预览
                return@setOnClickListener
            }
            if (mPdrSelectedList.isEmpty()){
                mBinding.pdrPreviewBtn.isEnabled = false
                return@setOnClickListener
            }
            val list = ArrayList<T>()
            mPdrSelectedList.forEach {
                list.add(it.data)
            }
            if (mPdrPickerBean?.filePreviewListener != null) {// 由外部处理预览逻辑
                mPdrPickerBean?.filePreviewListener?.onPickerPreview(getContext(), list, bean.pickerUIConfig)
                return@setOnClickListener
            }
            handlePreview(list)
        }

        mPdrAdapter.setListener(object : AnyPickerAdapter.Listener<T> {
            override fun onSelected(bean: DataWrapper<T>, position: Int) {
                val pickerBean = mPdrPickerBean ?: return

                if (mPdrSelectedList.size == pickerBean.maxCount && !bean.isSelected) {// 已经选满图片且用户点击了可选图
                    toastShort(getString(R.string.pandora_picker_max_count_tips, pickerBean.maxCount.toString()))
                    return
                }

                synchronized(this) {
                    for (i in mPdrDataList.indices) {
                        if (bean.data.hashCode() == mPdrDataList[i].data.hashCode()) {
                            mPdrDataList[i].isSelected = !bean.isSelected//更改选中状态
                            mPdrAdapter.setData(mPdrDataList)
                            mPdrAdapter.notifyItemChanged(position)//刷新数据
                            if (mPdrDataList[i].isSelected) {// 点击后是选中状态
                                mPdrSelectedList.add(bean)
                            } else {// 点击后是非选中状态
                                val iterator = mPdrSelectedList.iterator()
                                while (iterator.hasNext()) {
                                    if (iterator.next().data.hashCode() == bean.data.hashCode()) {
                                        iterator.remove()// 从选中列表里去除
                                        break
                                    }
                                }
                            }
                            // 设置按钮状态
                            mBinding.pdrConfirmBtn.isEnabled = mPdrSelectedList.isNotEmpty()
                            mBinding.pdrConfirmBtn.text = if (mPdrSelectedList.isNotEmpty()) {
                                getString(R.string.pandora_picker_confirm_num, mPdrSelectedList.size.toString(), pickerBean.maxCount.toString())
                            } else {
                                getString(R.string.pandora_picker_confirm)
                            }
                            mBinding.pdrPreviewBtn.isEnabled = mPdrSelectedList.isNotEmpty()
                            return
                        }
                    }
                }

            }

            override fun onClickCamera() {
                val bean = mPdrPickerBean ?: return
                takeCameraPhoto(bean)
            }
        })

        // 图片点击回调
        mPdrAdapter.setOnItemClickListener { viewHolder, item, position ->
            val bean = mPdrPickerBean ?: return@setOnItemClickListener
            if (mPdrPickerBean?.fileClickListener != null) {// 由外部处理预览逻辑
                mPdrPickerBean?.fileClickListener?.onItemClick(getContext(), item.data)
                return@setOnItemClickListener
            }
            if (!bean.isNeedPreview) {// 不需要预览
                return@setOnItemClickListener
            }
            handlePreview(arrayListOf(item.data))
        }
    }

    /** 处理预览逻辑 */
    protected open fun handlePreview(list: ArrayList<T>) {
        previewPic(list, list.size > 1)
    }

    /** 点击文件夹按钮 */
    protected open fun onClickFolderBtn(bean: PickerBean<T>) {}

    /** 图片预览器 */
    protected fun previewPic(list: List<T>, isShowPage: Boolean) {
        val bean = mPdrPickerBean ?: return
        val view = bean.view ?: return
        PreviewManager.create<T, DataViewHolder>()
            .setPosition(0)
            .setPagerTextSize(14)
            .setShowPagerText(isShowPage)
            .setBackgroundColor(getContext(), bean.pickerUIConfig.getPreviewBgColor())
            .setStatusBarColor(getContext(), bean.pickerUIConfig.getStatusBarColor())
            .setNavigationBarColor(getContext(), bean.pickerUIConfig.getNavigationBarColor())
            .setPagerTextColor(getContext(), bean.pickerUIConfig.getMainTextColor())
            .setImageView(view)
            .build(list)
            .open(getContext())
    }

    override fun initData() {
        super.initData()
        val bean = mPdrPickerBean
        if (bean == null) {
            finish()
            return
        }
        loadData(bean, mBinding)
    }

    protected open fun loadData(bean: PickerBean<T>, binding: PandoraActivityPickerBinding) {
        binding.pdrFolderBtn.isEnabled = false
        binding.pdrFolderNameTv.setText(R.string.pandora_picker_custom_file)
        binding.pdrMoreImg.visibility = View.GONE
        CoroutinesWrapper.create(this)
            .request {
                transformDataWrapper(bean.sourceList)
            }
            .actionPg(context = getContext(), cancelable = false) {
                onSuccess {
                    configAdapterData(it)
                }
            }
    }

    /** 转换数据包装实体 */
    protected fun transformDataWrapper(list: List<T>?): ArrayList<DataWrapper<T>> {
        val data = ArrayList<DataWrapper<T>>()
        list?.forEach {
            data.add(DataWrapper(it))
        }
        return data
    }

    /** 配置适配器数据 */
    protected fun configAdapterData(source: List<DataWrapper<T>>) {
        mPdrDataList.clear()
        for (info in source) {
            for (selectedBean in mPdrSelectedList) {
                if (selectedBean.data.hashCode() == info.hashCode()) {
                    info.isSelected = true
                    break
                }
            }
            mPdrDataList.add(info)
        }
        mPdrAdapter.setData(mPdrDataList)
        mBinding.pdrPickerPhototRv.smoothScrollToPosition(0)
        mPdrAdapter.notifyDataSetChanged()
    }

    /** 绘制返回按钮，颜色[color] */
    private fun drawBackBtn(@ColorRes color: Int) {
        CoroutinesWrapper.create(this)
            .request { BitmapUtils.rotateBitmap(getArrowBitmap(color), 90f) }
            .action { onSuccess { mBinding.pdrBackBtn.setImageBitmap(it) } }
    }

    /** 绘制更多图标，颜色[color] */
    private fun drawMoreImg(@ColorRes color: Int) {
        CoroutinesWrapper.create(this)
            .request { getArrowBitmap(color) }
            .action { onSuccess { mBinding.pdrMoreImg.setImageBitmap(it) } }
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
    private fun drawConfirmBtn(bean: PickerBean<T>) {
        mBinding.pdrConfirmBtn.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                mBinding.pdrConfirmBtn.viewTreeObserver.removeOnPreDrawListener(this)
                val width = mBinding.pdrConfirmBtn.measuredWidth
                val height = mBinding.pdrConfirmBtn.measuredHeight

                mBinding.pdrConfirmBtn.background = SelectorUtils.createBgPressedUnableDrawable(
                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnNormal(), width, height),
                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnPressed(), width, height),
                        getCornerDrawable(bean.pickerUIConfig.getConfirmBtnUnable(), width, height)
                )

                mBinding.pdrConfirmBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(
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
    protected open fun takeCameraPhoto(bean: PickerBean<T>) {}

    override fun finish() {
        release()
        super.finish()
    }

    override fun onResume() {
        super.onResume()
        mPdrPickerBean?.lifecycleObserver?.onResume { lifecycle }
    }

    override fun onStart() {
        super.onStart()
        mPdrPickerBean?.lifecycleObserver?.onStart { lifecycle }
    }

    override fun onPause() {
        super.onPause()
        mPdrPickerBean?.lifecycleObserver?.onPause { lifecycle }
    }

    override fun onStop() {
        super.onStop()
        mPdrPickerBean?.lifecycleObserver?.onStop { lifecycle }
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    /** 释放资源 */
    private fun release() {
        mPdrPickerBean?.lifecycleObserver?.onDestroy { lifecycle }
        mPdrAdapter.release()
        mPdrPickerBean?.clear()
        sPickerBean?.clear()
        sPickerBean = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPhotoPickerFinishEvent(event: PhotoPickerFinishEvent) {
        if (!isFinishing) {
            finish()
        }
    }

}