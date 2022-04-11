package com.lodz.android.agiledevkt.modules.contact

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.toChinese
import com.lodz.android.corekt.contacts.*
import com.lodz.android.corekt.contacts.bean.*
import com.lodz.android.corekt.contacts.bean.data.*
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.mvvm.vm.BaseViewModel
import com.lodz.android.pandora.utils.coroutines.CoroutinesWrapper
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
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

    /** 更新通讯录备注数据 */
    fun updateContactData(context: Context, bean: ContactsNoteBean) {
        CoroutinesWrapper.create(this)
            .request {
                context.updateContactNote(bean)
                bean
            }
            .actionPg(context) {
                onSuccess {
                    toastShort("备注改为：${it.note}")
                    getAllContactData(context)
                }
                onError { e, isNetwork ->
                    toastShort(e.toString())
                }
            }
    }

    /** 删除通讯录数据 */
    fun deleteAllContact(context: Context) {
        CoroutinesWrapper.create(this)
            .request {
                context.deleteAllContact()
            }
            .actionPg(context) {
                onSuccess {
                    toastShort("全部删除成功")
                    getAllContactData(context)
                }
                onError { e, isNetwork ->
                    toastShort(e.toString())
                }
            }
    }

    /** 删除通讯录数据 */
    fun deleteContact(context: Context, bean: ContactsInfoBean) {
        CoroutinesWrapper.create(this)
            .request {
                context.deleteContact(bean.rawContactId)
                bean
            }
            .actionPg(context) {
                onSuccess {
                    toastShort("删除成功：${it.nameBean.name}")
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
                    toastShort("创建成功：${it.nameBean.name}")
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
        bean.photoBean.photoArray = stream.toByteArray()

        val index = Random.nextInt(999) + 1
        val textArray = arrayOf("赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨")

        bean.nameBean.name = textArray[index % textArray.size].append("某")
        bean.nameBean.givenName = bean.nameBean.name
        bean.nameBean.phonetic = ""
        bean.nameBean.fullNameStyle = ContactsContract.FullNameStyle.CJK.toString()
        bean.nameBean.phoneticNameStyle = ContactsContract.PhoneticNameStyle.PINYIN.toString()

        bean.organizationBean.company = "嘉里顿集团"
        bean.organizationBean.title = "董事长"
        bean.nicknameBean.nickname = "老".append(textArray[index % textArray.size])


        bean.noteBean.note = DateUtils.getCurrentFormatString(DateUtils.TYPE_23)

        var phone = "13".append((System.currentTimeMillis() % 9))
        if (phone.length < 11){
            for (i in 1..(11 - phone.length)) {
                phone = phone.append(i)
            }
        }

        val count = 3

        for (i in 0 until count) {
            val phoneBean = ContactsPhoneBean()
            phoneBean.number = (phone.toLong() + i).toString()
            phoneBean.normalizedNumber = "+86".append(phone)
            phoneBean.type = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString()
            bean.phoneList.add(phoneBean)
        }

        for (i in 0 until count) {
            val emailBean = ContactsEmailBean()
            emailBean.address = (bean.nameBean.name.hashCode() + i * 1000).toString().subSequence(0, 5).append("@qq.com")
            emailBean.type = ContactsContract.CommonDataKinds.Email.TYPE_HOME.toString()
            bean.emailList.add(emailBean)
        }

        for (i in 0 until count) {
            val postalBean = ContactsPostalBean()
            postalBean.address = "成华大道1${i}号"
            postalBean.street = postalBean.address
            postalBean.type = ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString()
            postalBean.label = "学校"
            bean.postalList.add(postalBean)
        }

        for (i in 0 until count) {
            val imBean = ContactsImBean()
            imBean.account = (bean.nicknameBean.nickname.hashCode() + i * 1000).toString().subSequence(0, 5).toString()
            imBean.type = ContactsContract.CommonDataKinds.Im.TYPE_HOME.toString()
            imBean.protocol = ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ.toString()
            bean.imList.add(imBean)
        }

        for (i in 0 until count) {
            val relationBean = ContactsRelationBean()
            relationBean.name = "张".append(i.toChinese())
            relationBean.type = ContactsContract.CommonDataKinds.Relation.TYPE_PARTNER.toString()
            bean.relationList.add(relationBean)
        }

        for (i in 0 until count) {
            val eventBean = ContactsEventBean()
            eventBean.date = DateUtils.getFormatString(DateUtils.TYPE_6, Date(System.currentTimeMillis() + i * 99999999999))
            eventBean.type = ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY.toString()
            bean.eventList.add(eventBean)
        }

        for (i in 0 until count) {
            val websiteBean = ContactsWebsiteBean()
            websiteBean.website = "https://www.baidu.com/".append(i)
            websiteBean.websiteType = ContactsContract.CommonDataKinds.Website.TYPE_BLOG.toString()
            bean.websiteList.add(websiteBean)
        }
        return bean
    }

}