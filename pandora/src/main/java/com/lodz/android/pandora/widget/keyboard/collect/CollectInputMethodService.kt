package com.lodz.android.pandora.widget.keyboard.collect

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lodz.android.corekt.anko.showInputMethodPicker
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.collect.CltEditView

/**
 * 采集专用键盘输入法
 * @author zhouL
 * @date 2025/12/17
 */
class CollectInputMethodService : InputMethodService() {

    companion object {
        /** 输入法键盘类型 */
        const val IMS_INPUT_TYPE = "com.lodz.android.pandora.collect.ims.inputType"
    }

    private var keyboardView: View? = null

    override fun onCreateInputView(): View {
        // 加载自定义键盘布局
        val view = layoutInflater.inflate(R.layout.pandora_view_collect_keyboard, null)
        keyboardView = view
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        val view = keyboardView ?: return
        bindView(view, currentInputConnection, info)
    }

    private fun bindView(view: View, inputConnection: InputConnection?, info: EditorInfo?){
        val inputType = info?.extras?.getInt(IMS_INPUT_TYPE) ?: -1

        bindTabView(view, inputType)
        bindIdCardView(view, inputConnection)
        bindCarPlateView(view, inputConnection)
        bindComCertView(view, inputConnection)
    }

    /** 绑定标签视图 */
    private fun bindTabView(view: View, inputType: Int) {
        val idCardBtn = view.findViewById<TextView>(R.id.id_card_btn)
        val carPlateBtn = view.findViewById<TextView>(R.id.car_plate_btn)
        val comCertBtn = view.findViewById<TextView>(R.id.com_cert_btn)
        when(inputType){
            CltEditView.TYPE_ID_CARD -> showIdCardLayout(view)
            CltEditView.TYPE_CAR_PLATE -> showCarPlateLayout(view)
            CltEditView.TYPE_FOREIGN_CERT -> showComCertLayout(view)
            else -> showIdCardLayout(view)
        }

        idCardBtn.setOnClickListener {
            showIdCardLayout(view)
        }

        carPlateBtn.setOnClickListener {
            showCarPlateLayout(view)
        }

        comCertBtn.setOnClickListener {
            showComCertLayout(view)
        }

        val hideBtn = view.findViewById<ImageView>(R.id.hide_btn)
        hideBtn.setOnClickListener {
            requestHideSelf(0) // 隐藏输入法键盘
        }

        val pickerBtn = view.findViewById<ImageView>(R.id.picker_btn)
        pickerBtn.setOnClickListener {
            baseContext.showInputMethodPicker() // 显示系统输入法选择器
        }

    }

    /** 展示身份证键盘布局 */
    private fun showIdCardLayout(view: View) {
        view.findViewById<TextView>(R.id.id_card_btn).isSelected = true
        view.findViewById<LinearLayout>(R.id.id_card_layout).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.car_plate_btn).isSelected = false
        view.findViewById<LinearLayout>(R.id.car_plate_layout).visibility = View.GONE
        view.findViewById<TextView>(R.id.com_cert_btn).isSelected = false
        view.findViewById<LinearLayout>(R.id.com_cert_layout).visibility = View.GONE
    }

    /** 展示车牌键盘布局 */
    private fun showCarPlateLayout(view: View) {
        view.findViewById<TextView>(R.id.id_card_btn).isSelected = false
        view.findViewById<LinearLayout>(R.id.id_card_layout).visibility = View.GONE
        view.findViewById<TextView>(R.id.car_plate_btn).isSelected = true
        view.findViewById<LinearLayout>(R.id.car_plate_layout).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.com_cert_btn).isSelected = false
        view.findViewById<LinearLayout>(R.id.com_cert_layout).visibility = View.GONE
    }

    /** 展示通用证件键盘布局 */
    private fun showComCertLayout(view: View) {
        view.findViewById<TextView>(R.id.id_card_btn).isSelected = false
        view.findViewById<LinearLayout>(R.id.id_card_layout).visibility = View.GONE
        view.findViewById<TextView>(R.id.car_plate_btn).isSelected = false
        view.findViewById<LinearLayout>(R.id.car_plate_layout).visibility = View.GONE
        view.findViewById<TextView>(R.id.com_cert_btn).isSelected = true
        view.findViewById<LinearLayout>(R.id.com_cert_layout).visibility = View.VISIBLE
    }


    /** 绑定身份证键盘视图 */
    private fun bindIdCardView(view: View, inputConnection: InputConnection?) {
        // 绑定文字按钮
        val textBtns = listOf<TextView>(
            view.findViewById(R.id.id_card_0_btn),
            view.findViewById(R.id.id_card_1_btn),
            view.findViewById(R.id.id_card_2_btn),
            view.findViewById(R.id.id_card_3_btn),
            view.findViewById(R.id.id_card_4_btn),
            view.findViewById(R.id.id_card_5_btn),
            view.findViewById(R.id.id_card_6_btn),
            view.findViewById(R.id.id_card_7_btn),
            view.findViewById(R.id.id_card_8_btn),
            view.findViewById(R.id.id_card_9_btn),
            view.findViewById(R.id.id_card_X_btn)
        )
        textBtns.forEach { btn ->
            btn.setOnClickListener {
                inputConnection?.commitText(btn.text.toString(), 1)// 向输入框输入数字
            }
        }

        // 绑定删除按钮
        val deleteBtn = view.findViewById<TextView>(R.id.id_card_delete_btn)
        deleteBtn.setOnClickListener {
            inputConnection?.deleteSurroundingText(1, 0)// 删除光标前的一个字符
        }
        deleteBtn.setOnLongClickListener {
            inputConnection?.deleteSurroundingText(Int.MAX_VALUE, Int.MAX_VALUE) // 长按删除键：清空所有输入
            true
        }

        // 绑定完成按钮
        val completeBtn = view.findViewById<TextView>(R.id.id_card_complete_btn)
        completeBtn.setOnClickListener {
            inputConnection?.performEditorAction(EditorInfo.IME_ACTION_DONE) // 触发输入框的完成动作
            requestHideSelf(0) // 隐藏输入法键盘
        }
    }

    /** 绑定车牌号键盘视图 */
    private fun bindCarPlateView(view: View, inputConnection: InputConnection?) {
        val textBtns = listOf<TextView>(
            view.findViewById(R.id.car_plate_1_btn),
            view.findViewById(R.id.car_plate_2_btn),
            view.findViewById(R.id.car_plate_3_btn),
            view.findViewById(R.id.car_plate_4_btn),
            view.findViewById(R.id.car_plate_5_btn),
            view.findViewById(R.id.car_plate_6_btn),
            view.findViewById(R.id.car_plate_7_btn),
            view.findViewById(R.id.car_plate_8_btn),
            view.findViewById(R.id.car_plate_9_btn),
            view.findViewById(R.id.car_plate_0_btn),
            view.findViewById(R.id.car_plate_q_btn),
            view.findViewById(R.id.car_plate_w_btn),
            view.findViewById(R.id.car_plate_e_btn),
            view.findViewById(R.id.car_plate_r_btn),
            view.findViewById(R.id.car_plate_t_btn),
            view.findViewById(R.id.car_plate_y_btn),
            view.findViewById(R.id.car_plate_u_btn),
            view.findViewById(R.id.car_plate_i_btn),
            view.findViewById(R.id.car_plate_o_btn),
            view.findViewById(R.id.car_plate_p_btn),
            view.findViewById(R.id.car_plate_a_btn),
            view.findViewById(R.id.car_plate_s_btn),
            view.findViewById(R.id.car_plate_d_btn),
            view.findViewById(R.id.car_plate_f_btn),
            view.findViewById(R.id.car_plate_g_btn),
            view.findViewById(R.id.car_plate_h_btn),
            view.findViewById(R.id.car_plate_j_btn),
            view.findViewById(R.id.car_plate_k_btn),
            view.findViewById(R.id.car_plate_l_btn),
            view.findViewById(R.id.car_plate_z_btn),
            view.findViewById(R.id.car_plate_x_btn),
            view.findViewById(R.id.car_plate_c_btn),
            view.findViewById(R.id.car_plate_v_btn),
            view.findViewById(R.id.car_plate_b_btn),
            view.findViewById(R.id.car_plate_n_btn),
            view.findViewById(R.id.car_plate_m_btn)
        )
        // 绑定切换按钮
        val changeBtn = view.findViewById<TextView>(R.id.car_plate_zw_btn)
        changeBtn.setOnClickListener {
            if (changeBtn.text == getString(R.string.pandora_im_abc)) {
                changeCarPlateToAbc(textBtns, changeBtn)
            } else {
                changeCarPlateToChinese(textBtns, changeBtn)
            }
        }

        if (getTextLength(inputConnection) == 0) changeCarPlateToChinese(textBtns, changeBtn) else changeCarPlateToAbc(textBtns, changeBtn)
        textBtns.forEach { btn ->
            btn.setOnClickListener {
                inputConnection?.commitText(btn.text.toString(), 1)// 向输入框输入文字
                if (changeBtn.text == getString(R.string.pandora_im_chinese)){
                    return@setOnClickListener
                }
                if (getTextLength(inputConnection) > 0) {
                    changeCarPlateToAbc(textBtns, changeBtn)
                }
            }
        }

        // 绑定删除按钮
        val deleteBtn = view.findViewById<TextView>(R.id.car_plate_delete_btn)
        deleteBtn.setOnClickListener {
            inputConnection?.deleteSurroundingText(1, 0)// 删除光标前的一个字符
            if (getTextLength(inputConnection) == 0) {
                changeCarPlateToChinese(textBtns, changeBtn)// 全部删除后切换到中文键盘
            }
        }
        deleteBtn.setOnLongClickListener {
            inputConnection?.deleteSurroundingText(Int.MAX_VALUE, Int.MAX_VALUE) // 长按删除键：清空所有输入
            if (getTextLength(inputConnection) == 0) {
                changeCarPlateToChinese(textBtns, changeBtn)// 全部删除后切换到中文键盘
            }
            true
        }

        // 绑定完成按钮
        val completeBtn = view.findViewById<TextView>(R.id.car_plate_complete_btn)
        completeBtn.setOnClickListener {
            inputConnection?.performEditorAction(EditorInfo.IME_ACTION_DONE) // 触发输入框的完成动作
            requestHideSelf(0) // 隐藏输入法键盘
        }
    }

    /** 切换车牌键盘到英文 */
    private fun changeCarPlateToAbc(textBtns: List<TextView>, changeBtn: TextView) {
        val abcStrs = arrayListOf(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
            "A", "S", "D", "F", "G", "H", "J", "K", "L",
            "Z", "X", "C", "V", "B", "N", "M"
        )
        for ((i, view) in textBtns.withIndex()) {
            view.text = abcStrs[i]
        }
        changeBtn.text = getString(R.string.pandora_im_chinese)
    }

    /** 切换车牌键盘到中文 */
    private fun changeCarPlateToChinese(textBtns: List<TextView>, changeBtn: TextView) {
        val zwStrs = arrayListOf(
            "京", "津", "渝", "沪", "冀", "晋", "辽", "吉", "黑", "苏",
            "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "琼",
            "川", "贵", "云", "陕", "甘", "青", "蒙", "桂", "宁",
            "新", "藏", "港", "澳", "使", "领", "学"
        )
        for ((i, view) in textBtns.withIndex()) {
            view.text = zwStrs[i]
        }
        changeBtn.text = getString(R.string.pandora_im_abc)
    }

    /** 获取输入框[inputConnection]的文本长度，需设置预期长度[range] */
    private fun getTextLength(inputConnection: InputConnection?, range: Int = 100): Int {
        val after = inputConnection?.getTextAfterCursor(range, 0)?.length ?: 0// 获取光标前的文本长度（最多20个字符）
        val before = inputConnection?.getTextBeforeCursor(range, 0)?.length ?: 0// 获取光标后的文本长度（最多20个字符）
        return after + before
    }

    /** 绑定通用证件键盘视图 */
    private fun bindComCertView(view: View, inputConnection: InputConnection?) {
        val textBtns = listOf<TextView>(
            view.findViewById(R.id.com_cert_1_btn),
            view.findViewById(R.id.com_cert_2_btn),
            view.findViewById(R.id.com_cert_3_btn),
            view.findViewById(R.id.com_cert_4_btn),
            view.findViewById(R.id.com_cert_5_btn),
            view.findViewById(R.id.com_cert_6_btn),
            view.findViewById(R.id.com_cert_7_btn),
            view.findViewById(R.id.com_cert_8_btn),
            view.findViewById(R.id.com_cert_9_btn),
            view.findViewById(R.id.com_cert_0_btn),
            view.findViewById(R.id.com_cert_q_btn),
            view.findViewById(R.id.com_cert_w_btn),
            view.findViewById(R.id.com_cert_e_btn),
            view.findViewById(R.id.com_cert_r_btn),
            view.findViewById(R.id.com_cert_t_btn),
            view.findViewById(R.id.com_cert_y_btn),
            view.findViewById(R.id.com_cert_u_btn),
            view.findViewById(R.id.com_cert_i_btn),
            view.findViewById(R.id.com_cert_o_btn),
            view.findViewById(R.id.com_cert_p_btn),
            view.findViewById(R.id.com_cert_a_btn),
            view.findViewById(R.id.com_cert_s_btn),
            view.findViewById(R.id.com_cert_d_btn),
            view.findViewById(R.id.com_cert_f_btn),
            view.findViewById(R.id.com_cert_g_btn),
            view.findViewById(R.id.com_cert_h_btn),
            view.findViewById(R.id.com_cert_j_btn),
            view.findViewById(R.id.com_cert_k_btn),
            view.findViewById(R.id.com_cert_l_btn),
            view.findViewById(R.id.com_cert_z_btn),
            view.findViewById(R.id.com_cert_x_btn),
            view.findViewById(R.id.com_cert_c_btn),
            view.findViewById(R.id.com_cert_v_btn),
            view.findViewById(R.id.com_cert_b_btn),
            view.findViewById(R.id.com_cert_n_btn),
            view.findViewById(R.id.com_cert_m_btn)
        )
        // 绑定切换按钮
        val shiftBtn = view.findViewById<TextView>(R.id.com_cert_shift_btn)
        shiftBtn.setOnClickListener {
            if (textBtns.last().text == "M") changeComCertLowercase(textBtns) else changeComCertUppercase(textBtns)
        }

        textBtns.forEach { btn ->
            btn.setOnClickListener {
                inputConnection?.commitText(btn.text.toString(), 1)// 向输入框输入文字
            }
        }

        // 绑定删除按钮
        val deleteBtn = view.findViewById<TextView>(R.id.com_cert_delete_btn)
        deleteBtn.setOnClickListener {
            inputConnection?.deleteSurroundingText(1, 0)// 删除光标前的一个字符
        }
        deleteBtn.setOnLongClickListener {
            inputConnection?.deleteSurroundingText(Int.MAX_VALUE, Int.MAX_VALUE) // 长按删除键：清空所有输入
            true
        }

        // 绑定完成按钮
        val completeBtn = view.findViewById<TextView>(R.id.com_cert_complete_btn)
        completeBtn.setOnClickListener {
            inputConnection?.performEditorAction(EditorInfo.IME_ACTION_DONE) // 触发输入框的完成动作
            requestHideSelf(0) // 隐藏输入法键盘
        }
    }

    /** 切换通用证件键盘为全大写 */
    private fun changeComCertUppercase(textBtns: List<TextView>) {
        for ((i, view) in textBtns.withIndex()) {
            view.text = view.text.toString().uppercase()
        }
    }

    /** 切换通用证件键盘为全小写 */
    private fun changeComCertLowercase(textBtns: List<TextView>) {
        for ((i, view) in textBtns.withIndex()) {
            view.text = view.text.toString().lowercase()
        }
    }
}