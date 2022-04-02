package com.lodz.android.agiledevkt.modules.contact

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.contacts.bean.*
import com.lodz.android.corekt.contacts.getContactData
import com.lodz.android.corekt.contacts.insertContactData
import com.lodz.android.corekt.contacts.updateContactData
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

    /** 更新通讯录数据 */
    fun updateContactData(context: Context, bean: ContactsInfoBean) {
        CoroutinesWrapper.create(this)
            .request {
                context.updateContactData(bean)
                bean
            }
            .actionPg(context) {
                onSuccess {
                    toastShort("更新成功：${it.name}")
                    getAllContactData(context)
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
        bean.avatarArray = stream.toByteArray()

        val index = Random.nextInt(999) + 1
        val textArray = arrayOf("赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨")

        bean.name = textArray[index % textArray.size].append("某")
        bean.givenName = bean.name
        bean.phonetic = ""
        bean.fullNameStyle = ContactsContract.FullNameStyle.CJK.toString()
        bean.phoneticNameStyle = ContactsContract.PhoneticNameStyle.PINYIN.toString()

        bean.company = "嘉里顿集团"
        bean.title = "董事长"
        bean.nickName = "老".append(textArray[index % textArray.size])

        bean.website = "https://www.baidu.com/"
        bean.websiteType = ContactsContract.CommonDataKinds.Website.TYPE_BLOG.toString()

        bean.note = DateUtils.getCurrentFormatString(DateUtils.TYPE_23)

        var phone = "13".append((System.currentTimeMillis() % 9))
        if (phone.length < 11){
            for (i in 1..(11 - phone.length)) {
                phone = phone.append(i)
            }
        }
        val phoneBean = ContactsPhoneBean()
        phoneBean.number = phone
        phoneBean.normalizedNumber = "+86".append(phone)
        phoneBean.type = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString()
        bean.phoneList.add(phoneBean)

        val emailBean = ContactsEmailBean()
        emailBean.address = bean.name.hashCode().toString().subSequence(0, 5).append("@qq.com")
        emailBean.type = ContactsContract.CommonDataKinds.Email.TYPE_HOME.toString()
        bean.emailList.add(emailBean)

        val postalBean = ContactsPostalBean()
        postalBean.address = "成化大道10号"
        postalBean.street = postalBean.address
        postalBean.type = ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString()
        postalBean.label = "学校"
        bean.postalList.add(postalBean)

        val imBean = ContactsImBean()
        imBean.account = bean.nickName.hashCode().toString().subSequence(0, 5).toString()
        imBean.type = ContactsContract.CommonDataKinds.Im.TYPE_HOME.toString()
        imBean.protocol = ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ.toString()
        bean.imList.add(imBean)

        val relationBean = ContactsRelationBean()
        relationBean.name = "张三"
        relationBean.type = ContactsContract.CommonDataKinds.Relation.TYPE_PARTNER.toString()
        bean.relationList.add(relationBean)

        val eventBean = ContactsEventBean()
        eventBean.date = DateUtils.getCurrentFormatString(DateUtils.TYPE_6)
        eventBean.type = ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY.toString()
        bean.eventList.add(eventBean)
        return bean
    }
}