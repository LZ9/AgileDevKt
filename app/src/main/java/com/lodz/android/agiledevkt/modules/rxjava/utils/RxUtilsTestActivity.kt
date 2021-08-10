package com.lodz.android.agiledevkt.modules.rxjava.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityRxUtilsTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.album.AlbumUtils
import com.lodz.android.corekt.anko.*
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.NullPointerException

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

    private val mBinding: ActivityRxUtilsTestBinding by bindingLayout(ActivityRxUtilsTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 清空数据
        mBinding.cleanBtn.setOnClickListener {
            cleanResult()
        }

        // 请求数据
        mBinding.requestDataBtn.setOnClickListener {
            RxCache.create(mBinding.memorySwitch.isChecked, mBinding.diskSwitch.isChecked, mBinding.networkSwitch.isChecked)
                .requestData()
                .compose(RxUtils.ioToMainObservable())
                .compose(bindDestroyEvent())
                .subscribe(
                    BaseObserver.action(
                        next = { printResult(it) },
                        error = { printResult(it.cause.toString()) }
                    )
                )
        }

        // 联想搜索
        RxUtils.textChanges(mBinding.searchEdit)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindDestroyEvent())
            .subscribe(
                BaseObserver.action(
                    next = { printResult("搜索联想：$it") },
                    error = { printResult(it.cause.toString()) }
                )
            )

        // 快速点击
        RxUtils.viewClick(mBinding.quickClickBtn)
            .subscribe(
                BaseObserver.action(
                    next = { printResult(getString(R.string.rx_utils_quick_click)) },
                    error = { printResult(it.cause.toString()) }
                )
            )

        // 图片路径转Base64
        mBinding.pathToBase64Btn.setOnClickListener {
            Observable.just("")
                .map {
                    val list = AlbumUtils.getAllImages(getContext())
                    if (list.isEmpty()){
                        throw NullPointerException(getString(R.string.rx_utils_pic_empty))
                    }
                    val info = list[0]
                    info
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    printResult("图片路径：$it")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ImageLoader.create(getContext())
                            .loadUri(it.uri)
                            .setCenterInside()
                            .into(mBinding.pathImg)
                    } else {
                        ImageLoader.create(getContext())
                            .loadFilePath(it.path)
                            .setCenterInside()
                            .into(mBinding.pathImg)
                    }
                    it
                }
                .observeOn(Schedulers.io())
                .flatMap {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        RxUtils.decodeUriToBase64(getContext(), it.uri, getScreenWidth() / 8, getScreenHeight() / 8)
                    } else {
                        RxUtils.decodePathToBase64(it.path, getScreenWidth() / 8, getScreenHeight() / 8)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindDestroyEvent())
                .subscribe(
                    ProgressObserver.action(
                        context = getContext(),
                        msg = getString(R.string.rx_utils_pic_coding),
                        cancelable = false,
                        next = {
                            printResult("\nBase64路径：$it")
                            ImageLoader.create(getContext())
                                .loadBase64(it)
                                .setCenterInside()
                                .into(mBinding.base64Img)
                        },
                        error = { e, isNetwork ->
                            printResult(e.cause.toString())
                        }
                    )
                )
        }

        // 图片路径数组转Base64
        mBinding.pathsToBase64Btn.setOnClickListener {
            Observable.just("")
                .map {
                    val list = AlbumUtils.getAllImages(getContext())
                    if (list.isEmpty()){
                        throw NullPointerException(getString(R.string.rx_utils_pic_empty))
                    }
                    val infos = when {
                        list.size > 3 -> arrayListOf(list[2])
                        list.size > 2 -> arrayListOf(list[1])
                        else -> arrayListOf(list[0])
                    }
                    infos
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    printResult("图片路径：$it")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ImageLoader.create(getContext())
                            .loadUri(it[0].uri)
                            .setCenterInside()
                            .into(mBinding.pathImg)
                    } else {
                        ImageLoader.create(getContext())
                            .loadFilePath(it[0].path)
                            .setCenterInside()
                            .into(mBinding.pathImg)
                    }
                    it
                }
                .observeOn(Schedulers.io())
                .flatMap {
                    val uris = ArrayList<Uri>()
                    val paths = ArrayList<String>()
                    for (info in it) {
                        uris.add(info.uri)
                        paths.add(info.path)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        RxUtils.decodeUriToBase64(getContext(), uris, getScreenWidth() / 8, getScreenHeight() / 8)
                    } else {
                        RxUtils.decodePathToBase64(paths, getScreenWidth() / 8, getScreenHeight() / 8)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindDestroyEvent())
                .subscribe(
                    ProgressObserver.action(
                        context = getContext(),
                        msg = getString(R.string.rx_utils_pic_coding),
                        cancelable = false,
                        next = {
                            printResult("\nBase64路径：$it")
                            ImageLoader.create(getContext())
                                .loadBase64(it[0])
                                .setCenterInside()
                                .into(mBinding.base64Img)
                        },
                        error = { e, isNetwork ->
                            printResult(e.cause.toString())
                        }
                    )
                )
        }
    }

    private fun printResult(result: String) {
        mBinding.resultTv.text = result.append("\n").append(mBinding.resultTv.text.toString())
    }

    private fun cleanResult() {
        mBinding.resultTv.text = ""
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}