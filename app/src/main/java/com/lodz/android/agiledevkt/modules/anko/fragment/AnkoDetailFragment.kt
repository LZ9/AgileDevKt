package com.lodz.android.agiledevkt.modules.anko.fragment

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.imageloaderkt.ImageLoader
import com.lodz.android.pandora.base.activity.UseAnkoLayout
import com.lodz.android.pandora.base.fragment.LazyFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * 详情页Fragment
 * Created by zhouL on 2018/11/20.
 */
@UseAnkoLayout
class AnkoDetailFragment : LazyFragment() {

    companion object {
        fun newInstance(): AnkoDetailFragment = AnkoDetailFragment()
    }

    /** 头像 */
    private lateinit var mAvatarImg: ImageView
    /** 签名 */
    private lateinit var mSignTv: TextView
    /** 切换按钮 */
    private lateinit var mChangeBtn: Button
    /** 监听器 */
    private var mListener: ((view: View) -> Unit)? = null

    override fun getAbsLayoutId(): Int = 0

    override fun getAnkoLayoutView(): View? {
        return UI {
            verticalLayout {
                padding = dip(30)

                mAvatarImg = imageView {
                }.lparams(dip(100), dip(100))

                mSignTv = textView {
                    textSize = 18f
                    setText(R.string.al_sign)
                }

                mChangeBtn = button {
                    setText(R.string.al_change_account)
                    setOnClickListener { view ->
                        mListener?.invoke(view)
                    }
                }
            }
        }.view
    }

    override fun initData(view: View) {
        super.initData(view)
        ImageLoader.create(context)
                .loadResId(R.drawable.bg_pokemon)
                .useCircle()
                .setCenterCrop()
                .into(mAvatarImg)
    }

    /** 设置页面切换监听器[listener] */
    fun setOnChangeListener(listener: (view: View) -> Unit) {
        mListener = listener
    }

}