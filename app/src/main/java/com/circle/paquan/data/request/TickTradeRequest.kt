package com.circle.paquan.data.request

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.circle.paquan.common.Const
import com.circle.paquan.data.api.HttpClient
import com.circle.paquan.data.api.HttpClient.Builder.instance
import com.circle.paquan.data.bean.TestBean
import com.circle.paquan.utils.dataConvert
import com.circle.paquan.utils.withIOContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

class TickTradeRequest(private val viewModelScope: CoroutineScope?) {
    var orderBean: MutableLiveData<TestBean?>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set

    fun queryOrderByOrderId(map: Map<String, Any?>) {
//        val token = SPUtils.getInstance().getString(Const.TOKEN)
//        if (TextUtils.isEmpty(token)) return
        viewModelScope?.launch {
            try {
                val data = withIOContext {
                    instance.getContracts().dataConvert()
                }
                orderBean?.value = data
            } catch (e:Exception){
                orderBean?.value = null
                Log.i("GGGG", "queryAllContract: ==>报错了?${e.message}")
            }
        }
    }
}