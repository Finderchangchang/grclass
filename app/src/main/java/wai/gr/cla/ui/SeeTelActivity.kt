package wai.gr.cla.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lzy.okgo.OkGo
import kotlinx.android.synthetic.main.activity_see_tel.*
import okhttp3.Call
import okhttp3.Response
import wai.gr.cla.R
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.callback.JsonCallback
import wai.gr.cla.method.Utils
import wai.gr.cla.model.LzyResponse
import wai.gr.cla.model.UserModel
import wai.gr.cla.model.url

class SeeTelActivity : BaseActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_see_tel
    }

    override fun initViews() {
        toolbar.setLeftClick { finish() }
        tel_et.text="手机号："+Utils.getCache("tel")
    }

    override fun initEvents() {
        }

}
