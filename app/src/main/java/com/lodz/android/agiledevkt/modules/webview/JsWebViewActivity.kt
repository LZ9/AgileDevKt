package com.lodz.android.agiledevkt.modules.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import com.alibaba.fastjson.JSON
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.UserBean
import com.lodz.android.agiledevkt.databinding.ActivityJsWebviewBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.js.BridgeHandler
import com.lodz.android.pandora.js.CallBackFunction
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * JS交互的WebView测试类
 * @author zhouL
 * @date 2019/5/31
 */
class JsWebViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, JsWebViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 测试本地信息（不带头信息）  */
    private val TEST_LOCAL_TEST = "file:///android_asset/h5_test"
    /** 测试本地信息  */
    private val TEST_LOCAL_TEST_APP = "file:///android_asset/h5_test_app"
    /** JS交互测试页  */
    private val TEST_JS_BRIDGE = "file:///android_asset/JsBridgeDemo"

    /** 文件上传回调  */
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    private val mBinding: ActivityJsWebviewBinding by bindingLayout(ActivityJsWebviewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.wv_js_title)
        initWebView()
    }

    private fun initWebView() {
        mBinding.webView.loadUrl(TEST_JS_BRIDGE)
        mBinding.webView.settings.defaultTextEncodingName = "UTF-8"
        mBinding.webView.settings.loadsImagesAutomatically = true
        mBinding.webView.settings.setSupportZoom(false)
        mBinding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        mBinding.webView.webChromeClient = object : WebChromeClient() {

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                mFilePathCallback = filePathCallback
                mGetImgResult.launch("image/*")
                return true
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                PrintLog.d("console", consoleMessage?.message() ?: "")
                return super.onConsoleMessage(consoleMessage)
            }
        }
    }

    /** 删除图片的ActivityResult回调 */
    private val mGetImgResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (mFilePathCallback == null) {
            return@registerForActivityResult
        }
        mFilePathCallback?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
        mFilePathCallback = null
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // JAVA调用WEB（有回调）
        mBinding.callWebResponse.setOnClickListener {
            val data = JSON.toJSONString(UserBean("1238784791", "qwesdw"))
            appendLog("java 发给web ：$data")
            mBinding.webView.callHandler("functionInJs", data, object :CallBackFunction{
                override fun onCallBack(data: String) {
                    appendLog("web 响应数据：$data")
                }
            })
        }

        // JAVA调用WEB（无回调）
        mBinding.callWebUnresponse.setOnClickListener {
            val msg = "hello"
            appendLog("java 发给web ：$msg")
            mBinding.webView.send(msg)
        }

        mBinding.webView.registerHandler("submitFromWeb", object :BridgeHandler{
            override fun handler(data: String, function: CallBackFunction?) {
                appendLog("web 发送过来的数据：$data")
                function?.onCallBack("java get param")
            }
        })

        mBinding.webView.setDefaultHandler(object : BridgeHandler{
            override fun handler(data: String, function: CallBackFunction?) {
                appendLog("web 发送过来的数据：$data")
                function?.onCallBack("java get user info")
            }
        })
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    override fun finish() {
        super.finish()
        mBinding.webView.loadDataWithBaseURL(null, "","text/html", "utf-8", null)
        mBinding.webView.clearHistory()
        mBinding.webView.clearCache(true)
        mBinding.webView.destroy()
    }

    private fun appendLog(log: String) {
        if (mBinding.resultTv.text.isEmpty()) {
            mBinding.resultTv.text = log
            return
        }
        mBinding.resultTv.text = log.append("\n").append(mBinding.resultTv.text)
    }
}