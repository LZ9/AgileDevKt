package com.lodz.android.agiledevkt.modules.rxjava.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getScreenHeight
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.corekt.utils.UiHandler
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils

/**
 * Rx帮助类测试
 * Created by zhouL on 2019/1/11.
 */
class RxUtilsTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RxUtilsTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 清空数据 */
    private val mCleanBtn by bindView<MaterialButton>(R.id.clean_btn)
    /** 滚动控件 */
    private val mScrollView by bindView<NestedScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)
    /** 内存缓存 */
    private val mMemorySwitch by bindView<Switch>(R.id.memory_switch)
    /** 磁盘缓存 */
    private val mDiskSwitch by bindView<Switch>(R.id.disk_switch)
    /** 网络数据 */
    private val mNetworkSwitch by bindView<Switch>(R.id.network_switch)
    /** 请求数据 */
    private val mRequestDataBtn by bindView<MaterialButton>(R.id.request_data_btn)
    /** 搜索框 */
    private val mSearchEdit by bindView<EditText>(R.id.search_edit)
    /** 快速点击 */
    private val mQuickClickBtn by bindView<MaterialButton>(R.id.quick_click_btn)
    /** 图片路径转Base64 */
    private val mPathToBase64Btn by bindView<MaterialButton>(R.id.path_to_base64_btn)
    /** 图片路径数组转Base64 */
    private val mPathsToBase64Btn by bindView<MaterialButton>(R.id.paths_to_base64_btn)
    /** 路径图片 */
    private val mPathImg by bindView<ImageView>(R.id.path_img)
    /** 快速点击 */
    private val mBase64Img by bindView<ImageView>(R.id.base64_img)

    override fun getLayoutId(): Int = R.layout.activity_rx_utils_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 清空数据
        mCleanBtn.setOnClickListener {
            cleanResult()
        }

        // 请求数据
        mRequestDataBtn.setOnClickListener {
            RxCache.create(mMemorySwitch.isChecked, mDiskSwitch.isChecked, mNetworkSwitch.isChecked)
                    .requestData()
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : BaseObserver<String>() {
                        override fun onBaseNext(any: String) {
                            printResult(any)
                        }

                        override fun onBaseError(e: Throwable) {
                            printResult(e.cause.toString())
                        }
                    })
        }

        // 联想搜索
        RxUtils.textChanges(mSearchEdit)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(object :BaseObserver<CharSequence>(){
                    override fun onBaseNext(any: CharSequence) {
                        printResult("搜索联想：$any")
                    }

                    override fun onBaseError(e: Throwable) {
                        printResult(e.cause.toString())
                    }

                })

        // 快速点击
        RxUtils.viewClick(mQuickClickBtn)
                .subscribe(object :BaseObserver<View>(){
                    override fun onBaseNext(any: View) {
                        printResult(getString(R.string.rx_utils_quick_click))
                    }

                    override fun onBaseError(e: Throwable) {
                        printResult(e.cause.toString())
                    }
                })

        // 图片路径转Base64
        mPathToBase64Btn.setOnClickListener {
            val list = AlbumUtils.getAllImages(getContext())
            if (list.isEmpty()){
                printResult(getString(R.string.rx_utils_pic_empty))
                return@setOnClickListener
            }
            val path = list.get(0)
            printResult("图片路径：$path")
            ImageLoader.create(getContext())
                    .loadFilePath(path)
                    .setCenterInside()
                    .into(mPathImg)
            RxUtils.decodePathToBase64(path, getScreenWidth() / 8, getScreenHeight() / 8)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object : ProgressObserver<String>(){
                        override fun onPgNext(any: String) {
                            printResult("\nBase64路径：$any")
                            ImageLoader.create(getContext())
                                    .loadBase64(any)
                                    .setCenterInside()
                                    .into(mBase64Img)
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {

                        }
                    }.create(getContext(), R.string.rx_utils_pic_coding, true))

        }

        mPathsToBase64Btn.setOnClickListener {
            val list = AlbumUtils.getAllImages(getContext())
            if (list.isEmpty()){
                printResult(getString(R.string.rx_utils_pic_empty))
                return@setOnClickListener
            }
            val paths = if (list.size > 3){
                arrayListOf(list.get(2))
            }else if (list.size > 2){
                arrayListOf(list.get(1))
            }else{
                arrayListOf(list.get(0))
            }
            printResult("图片路径：$paths")
            ImageLoader.create(getContext())
                    .loadFilePath(paths.get(0))
                    .setCenterInside()
                    .into(mPathImg)
            RxUtils.decodePathToBase64(paths, getScreenWidth() / 8, getScreenHeight() / 8)
                    .compose(RxUtils.ioToMainObservable())
                    .compose(bindDestroyEvent())
                    .subscribe(object :ProgressObserver<ArrayList<String>>(){
                        override fun onPgNext(any: ArrayList<String>) {
                            printResult("\nBase64路径：$any")
                            ImageLoader.create(getContext())
                                    .loadBase64(any.get(0))
                                    .setCenterInside()
                                    .into(mBase64Img)
                        }

                        override fun onPgError(e: Throwable, isNetwork: Boolean) {

                        }
                    }.create(getContext(), R.string.rx_utils_pic_coding, true))
        }
    }

    private fun printResult(result: String) {
        mResultTv.text = (mResultTv.text.toString() + "\n" + result)
        UiHandler.postDelayed(100){
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun cleanResult() {
        mResultTv.text = ""
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}