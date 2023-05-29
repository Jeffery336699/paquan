package com.circle.paquan.utils

import com.google.gson.Gson

object MyJson {

    val gson = Gson()

    @JvmStatic
    fun toJson(src: Any?): String {
        return try {
            gson.toJson(src) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    @JvmStatic
    fun <T> fromJson(json: String?, classOfT: Class<T>?): T? {
        return try {
            gson.fromJson(json, classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}