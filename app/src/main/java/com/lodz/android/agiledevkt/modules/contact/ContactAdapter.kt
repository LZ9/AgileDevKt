package com.lodz.android.agiledevkt.modules.contact

import android.content.Context
import android.provider.ContactsContract
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.RvItemContactBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.contacts.bean.ContactsInfoBean
import com.lodz.android.imageloaderkt.glide.anko.loadBytes
import com.lodz.android.imageloaderkt.glide.anko.loadUrl
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * 通讯录适配器
 * @author zhouL
 * @date 2022/3/30
 */
class ContactAdapter(context: Context) : BaseRvAdapter<ContactsInfoBean>(context){

    /** 点击删除监听器 */
    private var mOnDeleteClickListener: ((viewHolder: RecyclerView.ViewHolder, item: ContactsInfoBean) -> Unit)? = null
    /** 点击更新备注监听器 */
    private var mOnUpdateNoteClickListener: ((viewHolder: RecyclerView.ViewHolder, item: ContactsInfoBean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DataVBViewHolder(getViewBindingLayout(RvItemContactBinding::inflate, parent))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val bean = getItem(position) ?: return
        if (holder !is DataVBViewHolder) {
            return
        }
        showItem(holder, bean)
    }

    private fun showItem(holder: DataVBViewHolder, bean: ContactsInfoBean) {
        holder.getVB<RvItemContactBinding>().apply {

            showImg(avatarImg, bean.photoBean.photoArray)

            nameTv.text = context.getString(R.string.contact_name).append(bean.nameBean.name)
            if (bean.nameBean.phonetic.isNotEmpty()){
                nameTv.text = nameTv.text.append("(").append(bean.nameBean.phonetic).append(")")
            }

            nickNameTv.text = context.getString(R.string.contact_nickname).append(bean.nicknameBean.nickname)

            companyTv.text = context.getString(R.string.contact_company).append(bean.organizationBean.company)
            if (bean.organizationBean.title.isNotEmpty()){
                companyTv.text = companyTv.text.append("(").append(bean.organizationBean.title).append(")")
            }

            var postalStr = context.getString(R.string.contact_postal)
            for (item in bean.postalList) {
                val label = if (item.type == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString()) {
                    item.label
                } else {
                    context.getString(ContactsContract.CommonDataKinds.StructuredPostal.getTypeLabelResource(item.type.toInt()))
                }
                postalStr = postalStr.append(item.address).append("(").append(label).append(")").append(" | ")
            }
            postalTv.text = postalStr

            var phoneStr = context.getString(R.string.contact_phone)
            for (item in bean.phoneList) {
                val label = if (item.type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM.toString()) {
                    item.label
                } else {
                    context.getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(item.type.toInt()))
                }
                phoneStr = phoneStr.append(label).append(" - ").append(item.number).append("(").append(item.from).append(")").append(" | ")
            }
            phoneTv.text = phoneStr

            var emailStr = context.getString(R.string.contact_email)
            for (item in bean.emailList) {
                val label = if (item.type == ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM.toString()) {
                    item.label
                } else {
                    context.getString(ContactsContract.CommonDataKinds.Email.getTypeLabelResource(item.type.toInt()))
                }
                emailStr = emailStr.append(item.address).append("(").append(label).append(")").append(" | ")
            }
            emailTv.text = emailStr

            noteTv.text = context.getString(R.string.contact_note).append(bean.noteBean.note)

            var websiteStr = context.getString(R.string.contact_website)
            for (item in bean.websiteList) {
                websiteStr = websiteStr.append(item.website).append(" | ")
            }
            websiteTv.text = websiteStr

            var imStr = context.getString(R.string.contact_im)
            for (item in bean.imList) {
                val label = if (item.protocol == ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM.toString()) {
                    item.customProtocolName
                } else {
                    context.getString(ContactsContract.CommonDataKinds.Im.getProtocolLabelResource(item.protocol.toInt()))
                }
                imStr = imStr.append(item.account).append("(").append(label).append(")").append(" | ")
            }
            imTv.text = imStr

            var relationStr = context.getString(R.string.contact_relation)
            for (item in bean.relationList) {
                val label = if (item.type == ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM.toString()) {
                    item.label
                } else {
                    context.getString(ContactsContract.CommonDataKinds.Relation.getTypeLabelResource(item.type.toInt()))
                }
                relationStr = relationStr.append(item.name).append("(").append(label).append(")").append(" | ")
            }
            relationTv.text = relationStr

            var eventStr = context.getString(R.string.contact_event)
            for (item in bean.eventList) {
                val label =  context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(item.type.toInt()))
                eventStr = eventStr.append(item.date).append("(").append(label).append(")").append(" | ")
            }
            eventTv.text = eventStr

            deleteBtn.setOnClickListener {
                mOnDeleteClickListener?.invoke(holder, bean)
            }
            updateNoteBtn.setOnClickListener {
                mOnUpdateNoteClickListener?.invoke(holder, bean)
            }
        }
    }

    private fun showImg(img: ShapeableImageView, bitmap: ByteArray?) {
        if (bitmap != null) {
            img.loadBytes(bitmap)
        } else {
            img.loadUrl("")
        }
    }

    /** 设置点击删除监听器 */
    fun setOnDeleteClickListener(listener: (viewHolder: RecyclerView.ViewHolder, item: ContactsInfoBean) -> Unit) {
        mOnDeleteClickListener = listener
    }

    /** 点击更新备注监听器 */
    fun setOnUpdateNoteClickListener(listener: (viewHolder: RecyclerView.ViewHolder, item: ContactsInfoBean) -> Unit) {
        mOnUpdateNoteClickListener = listener
    }
}