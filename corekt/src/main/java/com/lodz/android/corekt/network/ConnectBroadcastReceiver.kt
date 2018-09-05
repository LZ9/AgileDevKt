package com.lodz.android.corekt.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * 网络广播
 * Created by zhouL on 2018/7/3.
 */
class ConnectBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null){
            return
        }
        val action = intent.action
        if (action.isNullOrEmpty()){
            return
        }

        try {
            if (action!!.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                NetworkManager.get().updateNet(manager)
                NetworkManager.get().notifyNetworkListeners()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}