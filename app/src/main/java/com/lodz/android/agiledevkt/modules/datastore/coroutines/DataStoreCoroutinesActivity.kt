package com.lodz.android.agiledevkt.modules.datastore.coroutines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDataStoreCoroutinesBinding
import com.lodz.android.agiledevkt.utils.dataStore.DsManager
import com.lodz.android.corekt.anko.AnkoNumFormat
import com.lodz.android.corekt.anko.IoScope
import com.lodz.android.corekt.anko.clean
import com.lodz.android.corekt.anko.format
import com.lodz.android.corekt.utils.AppUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * DataStore协程测试类
 * @author zhouL
 * @date 2023/7/25
 */
class DataStoreCoroutinesActivity : BaseActivity() {
    companion object {

        fun start(context: Context){
            val intent = Intent(context, DataStoreCoroutinesActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDataStoreCoroutinesBinding by bindingLayout(ActivityDataStoreCoroutinesBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.datastore_coroutines)
    }

    override fun onClickBackBtn() {
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.idCtv.setOnJumpClickListener {
            updateId()
        }

        mBinding.nameCtv.setOnJumpClickListener {
            updateName()
        }

        mBinding.ageCtv.setOnJumpClickListener {
            updateAge()
        }

        mBinding.heightCtv.setOnJumpClickListener {
            updateHeight()
        }

        mBinding.postgraduateCtv.setOnJumpClickListener {
            updatePostgraduate()
        }

        mBinding.salaryCtv.setOnJumpClickListener {
            updateSalary()
        }

        mBinding.hobbyCtv.setOnJumpClickListener {
            updateHobby()
        }

        mBinding.cleanBtn.setOnClickListener {
            IoScope().launch {
                App.get().dataStore.clean()
                updateUI()
            }
        }
    }

    override fun initData() {
        super.initData()
        updateUI()
        showStatusCompleted()
    }

    private fun updateUI() {
        MainScope().launch {
            mBinding.apply {
                idCtv.setContentText(DsManager.getId().toString())
                nameCtv.setContentText(DsManager.getName())
                ageCtv.setContentText(DsManager.getAge().toString())
                heightCtv.setContentText(DsManager.getHeight().toString())
                postgraduateCtv.setContentText(DsManager.getPostgraduate().toString())
                salaryCtv.setContentText(DsManager.getSalary().toString())
                hobbyCtv.setContentText(DsManager.getHobby().toString())
            }
        }
    }

    private fun updateId() {
        IoScope().launch {
            var id = AppUtils.getUUID32().hashCode().toLong()
            if (id < 0) {
                id *= -1
            }
            DsManager.setId(id)
            launch(Dispatchers.Main) {
                mBinding.idCtv.setContentText(DsManager.getId().toString())
            }
        }
    }

    private fun updateName() {
        IoScope().launch {
            val list = arrayListOf("张三", "李四", "王五", "赵六", "孙七", "周八", "吴九", "郑十")
            DsManager.setName(list[Random.nextInt(list.size - 1)])
            launch(Dispatchers.Main) {
                mBinding.nameCtv.setContentText(DsManager.getName())
            }
        }
    }

    private fun updateAge(){
        IoScope().launch {
            DsManager.setAge(Random.nextInt(99) + 1)
            launch(Dispatchers.Main) {
                mBinding.ageCtv.setContentText(DsManager.getAge().toString())
            }
        }
    }

    private fun updateHeight() {
        IoScope().launch {
            val offset = Random.nextDouble(10.0) + 1
            DsManager.setHeight((180 - offset).format(AnkoNumFormat.TYPE_ONE_DECIMAL).toFloat())
            launch(Dispatchers.Main) {
                mBinding.heightCtv.setContentText(DsManager.getHeight().toString())
            }
        }
    }

    private fun updatePostgraduate() {
        IoScope().launch {
            DsManager.setPostgraduate(!DsManager.getPostgraduate())
            launch(Dispatchers.Main) {
                mBinding.postgraduateCtv.setContentText(DsManager.getPostgraduate().toString())
            }
        }
    }

    private fun updateSalary() {
        IoScope().launch {
            val offset = Random.nextDouble(3000.0) + 1
            DsManager.setSalary((10000 - offset).format().toDouble())
            launch(Dispatchers.Main) {
                mBinding.salaryCtv.setContentText(DsManager.getSalary().toString())
            }
        }
    }

    private fun updateHobby(){
        IoScope().launch {
            val set = setOf("看书", "写字", "画画", "游泳", "足球", "篮球", "乒乓球", "台球", "棒球", "田径")
            val length = Random.nextInt(set.size) + 1
            val hobbySet = hashSetOf<String>()
            for (i in 0 until length) {
                hobbySet.add(set.elementAt(i))
            }
            DsManager.setHobby(hobbySet)
            launch(Dispatchers.Main) {
                mBinding.hobbyCtv.setContentText(DsManager.getHobby().toString())
            }
        }
    }


}