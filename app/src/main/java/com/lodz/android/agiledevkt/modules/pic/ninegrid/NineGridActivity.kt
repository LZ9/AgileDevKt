package com.lodz.android.agiledevkt.modules.pic.ninegrid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.widget.ninegrid.NineGridView
import com.lodz.android.componentkt.widget.ninegrid.OnNineGridViewListener
import com.lodz.android.componentkt.widget.ninegrid.OnSimpleNineGridViewListener
import com.lodz.android.componentkt.widget.ninegrid.SimpleNineGridView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.goAppDetailSetting
import com.lodz.android.corekt.utils.isPermissionGranted
import com.lodz.android.corekt.utils.toastShort
import com.lodz.android.imageloaderkt.ImageLoader
import permissions.dispatcher.*

/**
 * 九宫格测试类
 * Created by zhouL on 2018/12/26.
 */
@RuntimePermissions
class NineGridActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NineGridActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 图片列表 */
    private val IMG_URLS = arrayListOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135406740&di=ad56c6b92e5d9888a04f0b724e5219d0&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F3801213fb80e7beca9004ec5252eb9389b506b38.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135426954&di=45834427b6f8ec30f1d7e1d99f59ee5c&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F0b7b02087bf40ad1cd0f99c55d2c11dfa9ecce29.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135506833&di=6b22dd2085f18b3643fe62b0f8b8955f&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F242dd42a2834349b8d289fafcbea15ce36d3beea.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135457903&di=e107c45dd449126ae54f0f665c558d05&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Df49943efb8119313d34ef7f30d5166a2%2Fb17eca8065380cd736f92fc0ab44ad345982813c.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135473894&di=27b040e674c4f9ac8b499f38612cab39&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fd788d43f8794a4c2fc3e95eb07f41bd5ac6e39d4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135496262&di=5cef907ceff298c8d5d6c79841a72696&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201409%2F07%2F20140907224542_h4HvW.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508135447478&di=90ddcac4604965af5d9bc744237a27aa&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd52a2834349b033b1c4bcdcf1fce36d3d439bde7.jpg",
            "http://hiphotos.baidu.com/zhidao/pic/item/d439b6003af33a87dd932ba4cd5c10385243b595.jpg")


    /** 只显示图片的九宫格 */
    private val mShowOnlyNineGridView by bindView<NineGridView>(R.id.show_only_nine_grid_view)
    /** 获取九宫格路径按钮 */
    private val mOnlyPicBtn by bindView<TextView>(R.id.only_pic_btn)
    /** 可添加图片的九宫格 */
    private val mPickerNineGridView by bindView<SimpleNineGridView>(R.id.picker_nine_grid_view)
    /** 获取九宫格路径按钮 */
    private val mGetPicBtn by bindView<TextView>(R.id.get_pic_btn)
    /** 预览缩放图片 */
    private val mScaleSwitch by bindView<Switch>(R.id.scale_switch)
    /** 显示拍照 */
    private val mShowCameraSwitch by bindView<Switch>(R.id.show_camera_switch)
    /** 单张预览 */
    private val mItemPreviewSwitch by bindView<Switch>(R.id.item_preview_switch)
    /** 点击关闭预览 */
    private val mClickClosePreviewSwitch by bindView<Switch>(R.id.click_close_preview_switch)
    /** 添加图片按钮 */
    private val mAddBtnSwitch by bindView<Switch>(R.id.add_btn_switch)
    /** 删除图片按钮 */
    private val mDeleteBtnSwitch by bindView<Switch>(R.id.delete_btn_switch)
    /** 允许拖拽 */
    private val mDragSwitch by bindView<Switch>(R.id.drag_switch)
    /** 拖拽震动 */
    private val mDragVibrateSwitch by bindView<Switch>(R.id.drag_vibrate_switch)
    /** 选项组 */
    private val mRadioGroup by bindView<RadioGroup>(R.id.radio_group)

    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    override fun getLayoutId(): Int = R.layout.activity_nine_grid

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.pic_nine_grid)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getTitleBarLayout().elevation = 0f
        }
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onClickReload() {
        super.onClickReload()
        showStatusLoading()
        onRequestPermissionWithPermissionCheck()//申请权限
    }

    override fun setListeners() {
        super.setListeners()
        mShowOnlyNineGridView.setOnNineGridViewListener(object : OnNineGridViewListener {
            override fun onAddPic(addCount: Int) {}

            override fun onDisplayImg(context: Context, data: String, imageView: ImageView) {
                ImageLoader.create(context).loadUrl(data).setCenterCrop().into(imageView)
            }

            override fun onDeletePic(data: String, position: Int) {}

            override fun onClickPic(data: String, position: Int) {
                toastShort("click position $position")
            }
        })

        mOnlyPicBtn.setOnClickListener {
            val list = mShowOnlyNineGridView.getPicData()
            var result = ""
            for (path in list) {
                result += path + "\n\n"
            }
            mResultTv.text = result
        }

        mScaleSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setScale(isChecked)
        }

        mShowCameraSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setNeedCamera(isChecked)
        }

        mItemPreviewSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setNeedItemPreview(isChecked)
        }

        mClickClosePreviewSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setClickClosePreview(isChecked)
        }

        mAddBtnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setNeedAddBtn(isChecked)
        }

        mDeleteBtnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setShowDelete(isChecked)
        }

        mDragSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setNeedDrag(isChecked)
        }

        mDragVibrateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mPickerNineGridView.setNeedDragVibrate(isChecked)
        }

        mRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.six_rb -> mPickerNineGridView.setMaxPic(6)
                R.id.nine_rb -> mPickerNineGridView.setMaxPic(9)
                R.id.twelve_rb -> mPickerNineGridView.setMaxPic(12)
            }
        }

        mPickerNineGridView.setOnSimpleNineGridViewListener(object : OnSimpleNineGridViewListener {
            override fun onDisplayNineGridImg(context: Context, data: String, imageView: ImageView) {
                ImageLoader.create(context).loadUrl(data).setCenterCrop().into(imageView)
            }

            override fun onDisplayPreviewImg(context: Context, data: String, imageView: ImageView) {
                ImageLoader.create(context).loadUrl(data).setCenterInside().into(imageView)
            }

            override fun onDisplayPickerImg(context: Context, data: String, imageView: ImageView) {
                ImageLoader.create(context).loadUrl(data).setCenterCrop().into(imageView)
            }
        })

        mGetPicBtn.setOnClickListener {
            val list = mPickerNineGridView.getPicData()
            var result = ""
            for (path in list) {
                result += path + "\n\n"
            }
            mResultTv.text = result
        }
    }

    override fun initData() {
        super.initData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0以上的手机对权限进行动态申请
            onRequestPermissionWithPermissionCheck()//申请权限
        } else {
            init()
        }
    }

    /** 权限申请成功 */
    @NeedsPermission(Manifest.permission.CAMERA)// 相机
    fun onRequestPermission() {
        if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            return
        }
        init()
    }

    /** 用户拒绝后再次申请前告知用户为什么需要该权限 */
    @OnShowRationale(Manifest.permission.CAMERA)// 相机
    fun onShowRationaleBeforeRequest(request: PermissionRequest) {
        request.proceed()//请求权限
    }

    /** 被拒绝 */
    @OnPermissionDenied(Manifest.permission.CAMERA)// 相机
    fun onDenied() {
        onRequestPermissionWithPermissionCheck()//申请权限
    }

    /** 被拒绝并且勾选了不再提醒 */
    @OnNeverAskAgain(Manifest.permission.CAMERA)// 相机
    fun onNeverAskAgain() {
        toastShort(R.string.splash_check_permission_tips)
        goAppDetailSetting()
        showStatusError()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun init() {
        mShowOnlyNineGridView.setData(IMG_URLS)
        mPickerNineGridView.config(FileManager.getCacheFolderPath(), "com.lodz.android.agiledevkt.fileprovider")
        showStatusCompleted()
    }
}