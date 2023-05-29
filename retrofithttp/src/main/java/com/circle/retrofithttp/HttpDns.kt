package com.circle.retrofithttp

import android.text.TextUtils
import okhttp3.Dns
import java.net.InetAddress

class HttpDns: Dns{

    override fun lookup(hostname: String): List<InetAddress> {
        var ip = InetAddress.getByName(hostname).hostAddress
        if (hostname == "api-test.ccfox.com"){
            ip = "47.242.48.192"
        }
        return if (!TextUtils.isEmpty(ip)) {
            //返回自己解析的地址列表
            InetAddress.getAllByName(ip).toList()
        } else {
            // 解析失败，使用系统解析
            Dns.SYSTEM.lookup(hostname)
        }
    }

}