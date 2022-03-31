package com.lodz.android.agiledevkt.modules.contact

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.RvItemContactBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.contacts.ContactsInfoBean
import com.lodz.android.corekt.utils.StringUtils
import com.lodz.android.imageloaderkt.glide.anko.loadBitmap
import com.lodz.android.imageloaderkt.glide.anko.loadUrl
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import com.lodz.android.pandora.widget.rv.recycler.DataVBViewHolder

/**
 *
 * @author zhouL
 * @date 2022/3/30
 */
class ContactAdapter(context: Context) :BaseRecyclerViewAdapter<ContactsInfoBean>(context){

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
            showImg(avatarImg, bean.avatarBitmap)
            nameTv.text = context.getString(R.string.contact_name).append(bean.name)
            val company = if (bean.company.isNotEmpty()) {
                bean.company
            } else if (bean.postal.isNotEmpty()) {
                bean.postal
            } else {
                ""
            }
            companyTv.text = context.getString(R.string.contact_company).append(company)
            titleTv.text = context.getString(R.string.contact_title).append(bean.title)
            var phone = ""
            for (phoneBean in bean.phoneList) {
                phone = phone.append("\n").append(phoneBean.number).append("(").append(phoneBean.from).append(")")
            }
            phoneTv.text = context.getString(R.string.contact_phone).append(phone)
            var email = StringUtils.getStringBySeparator(bean.emailList,"\n")
            if (email.isNotEmpty()){
                email = "\n".append(email)
            }
            emailTv.text = context.getString(R.string.contact_email).append(email)
            noteTv.text = context.getString(R.string.contact_note).append(bean.note)

            deleteBtn.setOnClickListener {
                mOnDeleteClickListener?.invoke(holder, bean)
            }
            updateNoteBtn.setOnClickListener {
                mOnUpdateNoteClickListener?.invoke(holder, bean)
            }
        }
    }

    private fun showImg(img: ShapeableImageView, bitmap: Bitmap?) {
        if (bitmap != null) {
            img.loadBitmap(bitmap)
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