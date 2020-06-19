package com.lodz.android.agiledevkt.modules.download

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.AppInfoBean
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.format
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder
import org.jetbrains.anko.textColor
import zlc.season.rxdownload4.manager.*

/**
 * 下载列表适配器
 * @author zhouL
 * @date 2020/6/14
 */
class DownloadMarketAdapter(context : Context) :BaseRecyclerViewAdapter<AppInfoBean>(context){

    private var mListener: OnDownloadListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
        DownloadViewHolder(getLayoutView(parent, R.layout.rv_item_download_market))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position)
        if (bean == null || holder !is DownloadViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DownloadViewHolder, bean: AppInfoBean) {
        val tag = holder.tag
        if (tag != null){
            holder.taskManager?.dispose(tag)
        }
        // 图标
        showImg(holder.withView(R.id.app_img), bean.imgUrl)
        // 名称
        holder.withView<TextView>(R.id.app_name_tv).text = bean.appName
        if (FileUtils.isFileExists(FileManager.getDownloadFolderPath().append(bean.getSaveName()))){
            showCompleted(holder, bean)
        } else {
            showReady(holder, bean)
        }
        createTaskManager(holder, bean)
    }

    private fun showImg(imageView: ImageView, url: String) {
        ImageLoader.create(context)
            .loadUrl(url)
            .setFitCenter()
            .into(imageView)
    }

    private fun createTaskManager(holder: DownloadViewHolder, bean: AppInfoBean) {
        holder.taskManager = bean.downloadUrl.pathTaskManager(bean.getSaveName())
        holder.tag = holder.taskManager?.subscribe { status ->
            when (status) {
                is Normal -> {
                    showReady(holder, bean)
                }// app未下载
                is Deleted -> {
                    showReady(holder, bean)
                }// app下载已被删除
                is Started -> {
                    showDownloading(holder, bean, status)
                }// 开始下载
                is Downloading -> {
                    showDownloading(holder, bean, status)
                }// 下载中
                is Paused -> {
                    showPause(holder, bean, status)
                }// 暂停下载
                is Completed -> {
                    showCompleted(holder, bean)
                }// 下载完成
                is Failed -> {
                    showFailed(holder, bean, status)
                }// 下载失败
            }
        }
    }

    private fun showReady(holder: DownloadViewHolder, bean: AppInfoBean) {
        holder.tipsTv.text = bean.appDesc
        holder.tipsTv.textColor = context.getColorCompat(R.color.color_1a1a1a)
        holder.tipsTv.visibility = View.VISIBLE
        holder.downloadingLayout.visibility = View.GONE
        holder.statusBtn.setText(R.string.market_download)
        holder.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null) {
                mListener?.onClickDownload(taskManager, bean)
            }
        }
    }

    private fun showDownloading(holder: DownloadViewHolder, bean: AppInfoBean, status: Status) {
        holder.tipsTv.visibility = View.GONE
        holder.downloadingLayout.visibility = View.VISIBLE
        var progress = status.progress.percent().toInt()
        if (progress > 100){
            progress = 100
        }
        if (progress < 0){
            progress = 0
        }
        holder.progressBar.progress = progress
        holder.percentTv.text = status.progress.percent().format().append("%")
        holder.statusBtn.setText(R.string.market_pause)
        holder.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickPause(taskManager, bean)
            }
        }
    }

    private fun showPause(holder: DownloadViewHolder, bean: AppInfoBean, status: Status) {
        holder.tipsTv.visibility = View.GONE
        holder.downloadingLayout.visibility = View.VISIBLE
        var progress = status.progress.percent().toInt()
        if (progress > 100){
            progress = 100
        }
        if (progress < 0){
            progress = 0
        }
        holder.progressBar.progress = progress
        holder.percentTv.text = status.progress.percent().format().append("%")
        holder.statusBtn.setText(R.string.market_download)
        holder.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickDownload(taskManager, bean)
            }
        }
    }

    private fun showCompleted(holder: DownloadViewHolder, bean: AppInfoBean) {
        holder.tipsTv.text = bean.appDesc
        holder.tipsTv.textColor = context.getColorCompat(R.color.color_1a1a1a)
        holder.tipsTv.visibility = View.VISIBLE
        holder.downloadingLayout.visibility = View.GONE
        holder.statusBtn.setText(R.string.market_delete)
        holder.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickDelete(taskManager, bean)
            }
        }
    }

    private fun showFailed(holder: DownloadViewHolder, bean: AppInfoBean, status: Failed) {
        PrintLog.e("testtag", "  下载失败  ", status.throwable)
        holder.tipsTv.text = context.getString(R.string.market_failed).append(" : ").append(status.throwable.message)
        holder.tipsTv.textColor = context.getColorCompat(R.color.red)
        holder.tipsTv.visibility = View.VISIBLE
        holder.downloadingLayout.visibility = View.GONE
        holder.statusBtn.setText(R.string.market_download)
        holder.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickDownload(taskManager, bean)
            }
        }
    }

    fun setOnDownloadListener(listener: OnDownloadListener){
        this.mListener = listener
    }

    interface OnDownloadListener {
        /** 点击下载 */
        fun onClickDownload(taskManager: TaskManager, bean: AppInfoBean)

        /** 点击暂停 */
        fun onClickPause(taskManager: TaskManager, bean: AppInfoBean)

        /** 点击删除 */
        fun onClickDelete(taskManager: TaskManager, bean: AppInfoBean)
    }

    private inner class DownloadViewHolder(itemView: View) : DataViewHolder(itemView) {
        /** 提示语 */
        val tipsTv by bindView<TextView>(R.id.tips_tv)
        /** 下载布局 */
        val downloadingLayout by bindView<ViewGroup>(R.id.downloading_layout)
        /** 进度条 */
        val progressBar by bindView<ProgressBar>(R.id.progress_bar)
        /** 进度百分比 */
        val percentTv by bindView<TextView>(R.id.percent_tv)
        /** 状态按钮 */
        val statusBtn by bindView<Button>(R.id.status_btn)

        internal var taskManager: TaskManager? = null
        internal var tag: Any? = null
    }
}