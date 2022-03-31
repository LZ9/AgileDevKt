package com.lodz.android.agiledevkt.modules.contact

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.contacts.ContactsInfoBean
import com.lodz.android.corekt.contacts.ContactsPhoneBean
import com.lodz.android.corekt.contacts.getContactData
import com.lodz.android.corekt.contacts.insertContactData
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import java.io.ByteArrayOutputStream
import kotlin.random.Random

/**
 * 通讯录ViewModel
 * @author zhouL
 * @date 2022/3/30
 */
class ContactViewModel : BaseViewModel() {

    var mContactList = MutableLiveData<ArrayList<ContactsInfoBean>>()

    /** 获取全部通讯录数据 */
    fun getAllContactData(context: Context) {
        CoroutinesWrapper.create(this)
            .request { context.getContactData() }
            .actionPg(context) {
                onSuccess {
                    mContactList.value = it
                }
                onError { e, isNetwork ->
                    toastShort(e.toString())
                }
            }
    }

    /** 添加通讯录数据 */
    fun addContactData(context: Context) {
        CoroutinesWrapper.create(this)
            .request {
                val bean = createRandomContactsInfoBean(context)
                context.insertContactData(bean)
                bean
            }
            .actionPg(context) {
                onSuccess {
                    toastShort("创建成功：${it.name}")
                    getAllContactData(context)
                }
                onError { e, isNetwork ->
                    toastShort(e.toString())
                }
            }
    }

    /** 创建通讯录数据实体 */
    private fun createRandomContactsInfoBean(context: Context): ContactsInfoBean {
        val bean = ContactsInfoBean()

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_logo)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        bean.avatarBitmap = bitmap
        bean.avatarArray = stream.toByteArray()

        val index = Random.nextInt(999) + 1
        val textArray = arrayOf("赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨")
        bean.name = textArray[index % textArray.size].append("某")
        bean.company = "嘉里顿集团"
        bean.title = "董事长"
        bean.postal = "嘉里顿"

        var phone = "13".append((System.currentTimeMillis() % 9))
        if (phone.length < 11){
            for (i in 1..(11 - phone.length)) {
                phone = phone.append(i)
            }
        }
        val phoneBean = ContactsPhoneBean()
        phoneBean.number = phone
        phoneBean.normalizedNumber = "+86".append(phone)
        bean.phoneList.add(phoneBean)

        bean.emailList.add(bean.name.hashCode().toString().subSequence(0, 5).append("@qq.com"))
        bean.note = DateUtils.getCurrentFormatString(DateUtils.TYPE_23)
        return bean
    }
}