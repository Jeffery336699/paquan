package com.circle.paquan.utils

import com.circle.paquan.R
import com.circle.paquan.bus.LiveDataBus
import com.circle.paquan.common.Const
import com.circle.retrofithttp.ErrCode
import com.circle.retrofithttp.ServerException
import com.circle.retrofithttp.bean.BaseBean
import com.circle.retrofithttp.utils.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> BaseBean<T>.dataConvert(): T? {
    if (code == 0 && service_status == null) {
        return data
    } else {
        throw ServerException(code.toString(), msg)
    }
}

suspend fun <T> CoroutineScope.withIOContext(
    toastException: Boolean = true,
    block: suspend CoroutineScope.() -> T
): T {
    try {
        return withContext(Dispatchers.IO, block)
    } catch (e: Exception) {
        processErr(this, e, toastException)
        throw e
    }
}

/**
 * 过滤频繁动作,结果cancel
 * 生命周期感知
 */
fun <T> debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

/**
 * 过滤频繁动作,结果执行一次
 * 生命周期感知
 */
fun <T> throttleFirst(
    skipMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var throttleJob: Job? = null
    return { param: T ->
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction(param)
                delay(skipMs)
            }
        }
    }
}

var depthMillis = 0L
private suspend fun processErr(coroutine: CoroutineScope, e: Exception, toastException: Boolean) {
    //对异常进行判断，这个是我随便写的一点，可以写一个工具类给封装起来
    when (e) {
        is SocketTimeoutException -> { //ArithmeticException

            toastStrByException(R.string.network_err, toastException)
        }
        is HttpException -> {
            when (e.code()) {
                401 -> {
                    LiveDataBus.get().with(Const.BUS_NET_ERROR_TOKEN).postValue("401")
                }
            }
        }
        is ServerException -> {
            val errString = ErrCode.getErrString(e.code)
            errString?.let {
                toastStrByException(errString, toastException)
            }
        }
        is JSONException -> toastStrByException(R.string.json_err, toastException)
        is UnknownHostException -> toastStrByException(R.string.network_err, toastException)
    }
}

private fun toastStrByException(
    @androidx.annotation.StringRes strRes: Int,
    toastException: Boolean = true
) {
    if (toastException) {
        ToastUtils.showShort(strRes)
    }
}

private fun toastStrByException(str: String, toastException: Boolean = true) {
    if (toastException) {
        ToastUtils.showShort(str)
    }
}

fun <T : Any> T.deepCopy(): T {
    val gson = Gson()
    return gson.fromJson(gson.toJson(this), this::class.java)
}

internal inline fun <reified T> T.deepListCopy(): T? {
    val gson = Gson()
    return gson.fromJson(gson.toJson(this), object : TypeToken<T>() {}.type)
}


/**
 * 0=>""
 */
fun String.zero2Null(): String {
    return if (isNullOrEmpty() || toInt() == 0) {
        ""
    } else {
        this
    }
}

/**
 * http://www.baidu.com=>www.baidu.com
 */
fun String.http2host(): String {
    return if (!isNullOrEmpty() && (contains("http://") || contains("https://"))) {
        substring(indexOf("//") + 2)
    } else {
        this
    }
}


