package com.circle.paquan.launch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.circle.paquan.data.bean.TestBean

class LaunchViewModel : ViewModel() { // TODO: Implement the ViewModel
    @JvmField
    val testBean = MutableLiveData<TestBean>()

    @JvmField
    var testStr = MutableLiveData<String>()

    init {
        testStr.value = "333aa"
    }
}