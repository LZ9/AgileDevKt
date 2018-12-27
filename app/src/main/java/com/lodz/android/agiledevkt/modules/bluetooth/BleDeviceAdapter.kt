package com.lodz.android.agiledevkt.modules.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 蓝牙设备适配器
 * Created by zhouL on 2018/8/1.
 */
class BleDeviceAdapter(context: Context) : BaseRecyclerViewAdapter<BluetoothDevice>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(getLayoutView(parent, R.layout.rv_item_ble_device))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val device: BluetoothDevice? = getItem(position)
        if (device == null) {
            return
        }
        showItem(holder as DataViewHolder, device)
    }

    private fun showItem(holder: DataViewHolder, device: BluetoothDevice) {
        holder.nameTv.text = device.name ?: ""
        holder.addressTv.text = device.address ?: ""
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 设备名称 */
        internal val nameTv by bindView<TextView>(R.id.name_tv)
        /** 设备地址 */
        internal val addressTv by bindView<TextView>(R.id.address_tv)
    }
}