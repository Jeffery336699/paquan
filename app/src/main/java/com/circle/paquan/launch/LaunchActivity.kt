package com.circle.paquan.launch

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import com.circle.paquan.MainActivity
import com.circle.paquan.R
import com.circle.paquan.*
import com.circle.paquan.base.BaseActivity
import com.circle.paquan.base.DataBindingConfig
import com.circle.paquan.common.AppViewModel
import com.circle.paquan.common.CommonViewModel
import com.circle.paquan.common.Const
import com.circle.paquan.utils.MyJson
import com.circle.paquan.utils.SPUtils
import kotlinx.coroutines.runBlocking

class LaunchActivity : BaseActivity() {
    private var mCommonViewModel: CommonViewModel? = null
    private var mViewModel: LaunchViewModel? = null
    private var mAppViewModel: AppViewModel? = null

    override fun initViewModel() {
        mViewModel = getActivityViewModel(LaunchViewModel::class.java)
        mAppViewModel = appViewModelProvider[AppViewModel::class.java]
        mCommonViewModel = getActivityViewModel(CommonViewModel::class.java)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_launch, BR.vm, mViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(null)
        super.onCreate(savedInstanceState)
        WebView(this).destroy()
        mAppViewModel?.mRequest?.orderBean?.observe(this){
            mViewModel?.testBean?.value = it
        }
        runBlocking {
            Thread.sleep(2000)
            goMain()
        }
    }

    /**
     * 跳转主页
     */
    private fun goMain() {
        SPUtils.getInstance().put(Const.FIRST, true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}