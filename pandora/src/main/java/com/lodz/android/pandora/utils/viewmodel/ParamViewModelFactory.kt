package com.lodz.android.pandora.utils.viewmodel

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 支持创建对象生成ViewModel的工厂
 * @author zhouL
 * @date 2022/1/20
 */
@Suppress("UNCHECKED_CAST")
class ParamViewModelFactory<VM : ViewModel>(private val factory: () -> VM) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory() as T
}

inline fun <reified VM : ViewModel> AppCompatActivity.bindViewModel(noinline vm: () -> VM): Lazy<VM> =
    viewModels { ParamViewModelFactory(vm) }

inline fun <reified VM : ViewModel> Fragment.bindViewModel(noinline vm: () -> VM): Lazy<VM> =
    viewModels { ParamViewModelFactory(vm) }