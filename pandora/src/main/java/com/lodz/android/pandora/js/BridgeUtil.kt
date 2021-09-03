package com.lodz.android.pandora.js

import android.content.Context
import android.webkit.WebView
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.getAssetsFileContent
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 *
 * @author zhouL
 * @date 2021/8/23
 */
object BridgeUtil {

    const val YY_OVERRIDE_SCHEMA = "yy://"
    const val YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/"//格式为   yy://return/{function}/returncontent
    const val YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/"
    const val EMPTY_STR = ""
    const val UNDERLINE_STR = "_"
    const val SPLIT_MARK = "/"

    const val CALLBACK_ID_FORMAT = "JAVA_CB_%s"
    const val JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');"
    const val JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();"
    const val JAVASCRIPT_STR = "javascript:"

    const val JAVA_SCRIPT = "WebViewJavascriptBridge.js"

    @JvmStatic
    /** 获取默认的JsBridgei接口名 */
    fun getJsBridgeName(): String {
        return JS_FETCH_QUEUE_FROM_JAVA
            .replace("javascript:WebViewJavascriptBridge.".toRegex(), EMPTY_STR)
            .replace("\\(.*\\);".toRegex(), EMPTY_STR)
    }

    @JvmStatic
    /** 从[url]里解析回调数据 */
    fun getDataFromReturnUrl(url: String): String? {
        if (url.startsWith(YY_FETCH_QUEUE)) {
            return url.replace(YY_FETCH_QUEUE, EMPTY_STR)
        }
        val temp = url.replace(YY_RETURN_DATA, EMPTY_STR)
        val functionAndData = temp.split(SPLIT_MARK)
        if (functionAndData.size >= 2) {
            val sb = StringBuilder()
            for (i in 1 until functionAndData.size) {
                sb.append(functionAndData[i])
            }
            return sb.toString()
        }
        return null
    }

    @JvmStatic
    fun getJsBridgeNameFromReturnUrl(url: String): String? {
        val temp = url.replace(YY_RETURN_DATA, EMPTY_STR)
        val functionAndData = temp.split(SPLIT_MARK)
        return if (functionAndData.isNotEmpty()) functionAndData[0] else null
    }

    fun assetJsFile2Str(context: Context, fileName: String): String {
        context.assets.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { bufferedReader ->
                var line: String?
                val sb = StringBuilder()
                do {
                    line = bufferedReader.readLine()
                    if (line != null && !line.matches("^\\s*\\/\\/.*".toRegex())) {
                        sb.append(line)
                    }
                } while (line != null)
                return sb.toString()
            }
        }
    }
}