package com.lodz.android.agiledevkt.modules.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder

/**
 * 蓝牙设备适配器
 * Created by zhouL on 2018/8/1.
 */
class BleDeviceAdapter(context: Context) : BaseRecyclerViewAdapter<BluetoothDevice>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataViewHolder(getLayoutView(parent, R.layout.rv_item_ble_device))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val device: BluetoothDevice = getItem(position) ?: return
        showItem(holder as DataViewHolder, device)
    }

    private fun showItem(holder: DataViewHolder, device: BluetoothDevice) {
        holder.withView<TextView>(R.id.name_tv).text = device.name ?: ""
        holder.withView<TextView>(R.id.address_tv).text = device.address ?: ""
    }
}