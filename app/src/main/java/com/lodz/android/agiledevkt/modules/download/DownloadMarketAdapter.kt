package com.lodz.android.agiledevkt.modules.download

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.AppInfoBean
import com.lodz.android.agiledevkt.databinding.RvItemDownloadMarketBinding
import com.lodz.android.agiledevkt.utils.file.FileManager
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.format
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.corekt.utils.FileUtils
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataViewHolder
import zlc.season.rxdownload4.manager.*

/**
 * 下载列表适配器
 * @author zhouL
 * @date 2020/6/14
 */
class DownloadMarketAdapter(context : Context) :BaseRecyclerViewAdapter<AppInfoBean>(context){

    private var mListener: OnDownloadListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
        DownloadViewHolder(getViewBindingLayout(RvItemDownloadMarketBinding::inflate, parent))

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
        showImg(holder.viewBinding.appImg, bean.imgUrl)
        // 名称
        holder.viewBinding.appNameTv.text = bean.appName
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
                is Normal -> {// app未下载
                    showReady(holder, bean)
                }
                is Deleted -> {// app下载已被删除
                    showReady(holder, bean)
                }
                is Started -> {// 开始下载
                    showDownloading(holder, bean, status)
                }
                is Downloading -> {// 下载中
                    showDownloading(holder, bean, status)
                }
                is Paused -> {// 暂停下载
                    showPause(holder, bean, status)
                }
                is Completed -> {// 下载完成
                    showCompleted(holder, bean)
                }
                is Failed -> {// 下载失败
                    showFailed(holder, bean, status)
                }
            }
        }
    }

    private fun showReady(holder: DownloadViewHolder, bean: AppInfoBean) {
        holder.viewBinding.tipsTv.text = bean.appDesc
        holder.viewBinding.tipsTv.setTextColor(context.getColorCompat(R.color.color_1a1a1a))
        holder.viewBinding.tipsTv.visibility = View.VISIBLE
        holder.viewBinding.downloadingLayout.visibility = View.GONE
        holder.viewBinding.statusBtn.setText(R.string.market_download)
        holder.viewBinding.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null) {
                mListener?.onClickDownload(taskManager, bean)
            }
        }
    }

    private fun showDownloading(holder: DownloadViewHolder, bean: AppInfoBean, status: Status) {
        holder.viewBinding.tipsTv.visibility = View.GONE
        holder.viewBinding.downloadingLayout.visibility = View.VISIBLE
        var progress = status.progress.percent().toInt()
        if (progress > 100){
            progress = 100
        }
        if (progress < 0){
            progress = 0
        }
        holder.viewBinding.progressBar.progress = progress
        holder.viewBinding.percentTv.text = status.progress.percent().format().append("%")
        holder.viewBinding.statusBtn.setText(R.string.market_pause)
        holder.viewBinding.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickPause(taskManager, bean)
            }
        }
    }

    private fun showPause(holder: DownloadViewHolder, bean: AppInfoBean, status: Status) {
        holder.viewBinding.tipsTv.visibility = View.GONE
        holder.viewBinding.downloadingLayout.visibility = View.VISIBLE
        var progress = status.progress.percent().toInt()
        if (progress > 100){
            progress = 100
        }
        if (progress < 0){
            progress = 0
        }
        holder.viewBinding.progressBar.progress = progress
        holder.viewBinding.percentTv.text = status.progress.percent().format().append("%")
        holder.viewBinding.statusBtn.setText(R.string.market_download)
        holder.viewBinding.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickDownload(taskManager, bean)
            }
        }
    }

    private fun showCompleted(holder: DownloadViewHolder, bean: AppInfoBean) {
        holder.viewBinding.tipsTv.text = bean.appDesc
        holder.viewBinding.tipsTv.setTextColor(context.getColorCompat(R.color.color_1a1a1a))
        holder.viewBinding.tipsTv.visibility = View.VISIBLE
        holder.viewBinding.downloadingLayout.visibility = View.GONE
        holder.viewBinding.statusBtn.setText(R.string.market_delete)
        holder.viewBinding.statusBtn.setOnClickListener {
            val taskManager = holder.taskManager
            if (taskManager != null){
                mListener?.onClickDelete(taskManager, bean)
            }
        }
    }

    private fun showFailed(holder: DownloadViewHolder, bean: AppInfoBean, status: Failed) {
        PrintLog.e("testtag", "  下载失败  ", status.throwable)
        holder.viewBinding.tipsTv.text = context.getString(R.string.market_failed).append(" : ").append(status.throwable.message)
        holder.viewBinding.tipsTv.setTextColor(context.getColorCompat(R.color.red))
        holder.viewBinding.tipsTv.visibility = View.VISIBLE
        holder.viewBinding.downloadingLayout.visibility = View.GONE
        holder.viewBinding.statusBtn.setText(R.string.market_download)
        holder.viewBinding.statusBtn.setOnClickListener {
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

    private inner class DownloadViewHolder(val viewBinding: RvItemDownloadMarketBinding) : DataViewHolder(viewBinding.root) {
        var taskManager: TaskManager? = null
        var tag: Any? = null
    }
}