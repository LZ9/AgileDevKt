package com.lodz.android.agiledevkt.modules.contact

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lodz.android.corekt.contacts.ContactsInfoBean
import com.lodz.android.corekt.contacts.getAllContactData
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper

/**
 * 通讯录ViewModel
 * @author zhouL
 * @date 2022/3/30
 */
class ContactViewModel : BaseViewModel() {

    var mContactList = MutableLiveData<ArrayList<ContactsInfoBean>>()

    fun getAllContactData(context: Context) {
        CoroutinesWrapper.create(this)
            .request {
                context.getAllContactData()
            }
            .action {
                onSuccess {
                    mContactList.value = it
                    showStatusCompleted()
                }
                onError { e, isNetwork ->
                    showStatusError(e)
                    toastShort(e.toString())
                }
            }
    }
}