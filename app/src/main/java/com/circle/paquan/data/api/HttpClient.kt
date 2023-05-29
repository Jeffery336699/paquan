package com.circle.paquan.data.api

import androidx.lifecycle.viewmodel.CreationExtras
import com.circle.paquan.common.Const
import com.circle.paquan.data.bean.TestBean
import com.circle.paquan.utils.SPUtils
import com.circle.retrofithttp.bean.BaseBean
import com.circle.retrofithttp.utils.BuildFactory
import okhttp3.MultipartBody
import retrofit2.http.*

interface HttpClient {
    object Builder {
        @JvmStatic
        val instance: HttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BuildFactory.getInstance()
                .createCCFox(HttpClient::class.java, SPUtils.getInstance().getString(Const.BASE_URL, Const.BASE_URL))
        }
    }

    /**
     * 获取banner列表
     *
     * @param map
     * @return
     */
    @GET(value = "/common/banner/list")
    suspend fun getBanner(@QueryMap map: @JvmSuppressWildcards Map<String, Any?>?): BaseBean<List<TestBean>?>

    /**
     * 获取合约列表
     *
     * @return
     */
    @GET(value = "/future/queryCommonData")
    suspend fun getContracts(): BaseBean<TestBean?>

}