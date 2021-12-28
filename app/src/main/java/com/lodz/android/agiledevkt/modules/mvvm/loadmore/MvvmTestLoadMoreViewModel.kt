package com.lodz.android.agiledevkt.modules.mvvm.loadmore

import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.bean.base.response.PageBean
import com.lodz.android.corekt.anko.IoScope
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 * MVVM加载更多测试ViewModel
 * @author zhouL
 * @date 2019/12/5
 */
class MvvmTestLoadMoreViewModel :BaseRefreshViewModel(){

    var mDataList = MutableLiveData<Pair<Boolean, PageBean<ArrayList<String>>>>()

    fun getDataList(page: Int){
        IoScope().launch {
            val count = page * PageBean.DEFAULT_PAGE_SIZE
            if (count > PageBean.DEFAULT_TOTAL) {
                val pageBean = PageBean<ArrayList<String>>()
                pageBean.data = null
                pageBean.pageNum = page
                launch(Dispatchers.Main) {
                    mDataList.value = Pair(page == PageBean.DEFAULT_START_PAGE_NUM, pageBean)
                }
                return@launch
            }

            val list = ArrayList<String>()
            for (i in 1..PageBean.DEFAULT_PAGE_SIZE) {
                list.add(i.hashCode().toString())
            }
            delay(1500)
            val pageBean = PageBean<ArrayList<String>>()
            pageBean.data = list
            pageBean.pageNum = page
            launch(Dispatchers.Main) {
                mDataList.value = Pair(page == PageBean.DEFAULT_START_PAGE_NUM, pageBean)
            }
        }
    }
}