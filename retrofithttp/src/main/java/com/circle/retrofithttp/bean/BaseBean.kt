package com.circle.retrofithttp.bean

import java.io.Serializable

/**
 * 作者：junj
 * 时间：2017/4/19 15:09
 * 描述：TOTO
 */

data class BaseBean<T>(val code: Int, val msg: String?, var data: T?, var service_status: String?, var description: String?) : Serializable