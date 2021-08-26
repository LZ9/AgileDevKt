package com.lodz.android.pandora.js

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject

/**
 * @author zhouL
 * @date 2021/8/23
 */
class Message {

    companion object{

        private val CALLBACK_ID_STR: String = "callbackId"
        private val RESPONSE_ID_STR: String = "responseId"
        private val RESPONSE_DATA_STR: String = "responseData"
        private val DATA_STR: String = "data"
        private val HANDLER_NAME_STR: String = "handlerName"

        @JvmStatic
        fun toObject(json: String): Message {
            val message = Message()
            try {
                val jsonObject = JSON.parseObject(json)
                message.handlerName = jsonObject.getString(HANDLER_NAME_STR) ?: ""
                message.callbackId = jsonObject.getString(CALLBACK_ID_STR) ?: ""
                message.responseData = jsonObject.getString(RESPONSE_DATA_STR) ?: ""
                message.responseId = jsonObject.getString(RESPONSE_ID_STR) ?: ""
                message.data = jsonObject.getString(DATA_STR) ?: ""
                return message
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return message
        }

        @JvmStatic
        fun toArrayList(json: String): ArrayList<Message> {
            val list = ArrayList<Message>()
            try {
                val jsonArray = JSON.parseArray(json)
                for (i in 0 until jsonArray.size) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val message = Message()
                    message.handlerName = jsonObject.getString(HANDLER_NAME_STR) ?: ""
                    message.callbackId = jsonObject.getString(CALLBACK_ID_STR) ?: ""
                    message.responseData = jsonObject.getString(RESPONSE_DATA_STR) ?: ""
                    message.responseId = jsonObject.getString(RESPONSE_ID_STR) ?: ""
                    message.data = jsonObject.getString(DATA_STR) ?: ""
                    list.add(message)
                }
                return list
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return list
        }
    }

    var callbackId: String = "" //callbackId
    var responseId: String = "" //responseId
    var responseData: String = "" //responseData
    var data: String = "" //data of message
    var handlerName: String = "" //name of handler

    fun toJson(): String {
        try {
            val jsonObject = JSONObject()
            jsonObject[CALLBACK_ID_STR] = callbackId
            jsonObject[DATA_STR] = data
            jsonObject[HANDLER_NAME_STR] = handlerName
            jsonObject[RESPONSE_DATA_STR] = responseData
            jsonObject[RESPONSE_ID_STR] = responseId
            return jsonObject.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}