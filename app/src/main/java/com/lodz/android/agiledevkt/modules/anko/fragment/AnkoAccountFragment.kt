package com.lodz.android.agiledevkt.modules.anko.fragment

//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.TextView
//import com.lodz.android.agiledevkt.R
//import com.lodz.android.pandora.base.activity.UseAnkoLayout
//import com.lodz.android.pandora.base.fragment.LazyFragment
//import org.jetbrains.anko.*
//import org.jetbrains.anko.support.v4.UI
//
///**
// * 信息修改Fragment
// * Created by zhouL on 2018/11/20.
// */
//@UseAnkoLayout
//class AnkoAccountFragment : LazyFragment() {
//
//    companion object {
//
//        private const val EXTRA_ACCOUNT = "extra_account"
//
//        fun newInstance(account: String): AnkoAccountFragment {
//            val fragment = AnkoAccountFragment()
//            val bundle = Bundle()
//            bundle.putString(EXTRA_ACCOUNT, account)
//            fragment.arguments = bundle
//            return fragment
//        }
//    }
//
//    /** 账号控件 */
//    private lateinit var mAccountTv: TextView
//    /** 姓名控件 */
//    private lateinit var mNameTv: TextView
//    /** 切换按钮 */
//    private lateinit var mChangeBtn: Button
//    /** 监听器 */
//    private var mListener: ((view: View) -> Unit)? = null
//
//    /** 账号 */
//    private var mAccount = ""
//
//    override fun getAbsLayoutId(): Int = 0
//
//    override fun getAnkoLayoutView(): View? {
//        return UI {
//            verticalLayout {
//                padding = dip(30)
//                mAccountTv = textView {
//                    textSize = 16f
//                }
//                mNameTv = textView {
//                    textSize = 16f
//                    text = StringBuilder(getString(R.string.al_name) + "  Jack")
//                }
//                mChangeBtn = button {
//                    setText(R.string.al_change_detail)
//                    setOnClickListener { view ->
//                        mListener?.invoke(view)
//                    }
//                }
//            }
//        }.view
//    }
//
//    override fun startCreate() {
//        super.startCreate()
//        val bundle = arguments
//        if (bundle != null) {
//            val account = bundle.getString(EXTRA_ACCOUNT)
//            if (!account.isNullOrEmpty()) {
//                mAccount = account
//            }
//        }
//    }
//
//    override fun initData(view: View) {
//        super.initData(view)
//        mAccountTv.text = StringBuilder(getString(R.string.al_account) + "  " + mAccount)
//    }
//
//    /** 设置页面切换监听器[listener] */
//    fun setOnChangeListener(listener: (view: View) -> Unit) {
//        mListener = listener
//    }
//}