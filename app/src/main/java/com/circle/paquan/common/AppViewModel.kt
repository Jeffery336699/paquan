package com.circle.paquan.common

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.circle.paquan.data.request.TickTradeRequest

class AppViewModel: ViewModel() {
    @JvmField
    var list = MutableLiveData<MutableList<MutableList<String>>>()

    @JvmField
    var notifyCurrentListChanged = ObservableBoolean()
    @JvmField
    var mRequest = TickTradeRequest(viewModelScope)

    init {
        notifyCurrentListChanged.set(false)
        mRequest.queryOrderByOrderId(hashMapOf())
    }
}