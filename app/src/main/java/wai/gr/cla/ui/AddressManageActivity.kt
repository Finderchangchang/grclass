package wai.gr.cla.ui

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable

import wai.gr.cla.R
import wai.gr.cla.base.BaseActivity
import com.lljjcoder.citylist.CityListSelectActivity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.view.View
import chihane.jdaddressselector.BottomDialog
import chihane.jdaddressselector.model.Province
import com.google.gson.Gson
import com.lljjcoder.citylist.bean.CityInfoBean
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.umeng.socialize.utils.DeviceConfig.context
import kotlinx.android.synthetic.main.activity_address_manage.*
import okhttp3.Call
import okhttp3.Response
import wai.gr.cla.callback.JsonCallback
import wai.gr.cla.method.common
import wai.gr.cla.model.AddressModel
import wai.gr.cla.model.LzyResponse
import wai.gr.cla.model.url


class AddressManageActivity : BaseActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_address_manage
    }

    override fun initViews() {
        val dialog = BottomDialog(this)
        dialog.setOnAddressSelectedListener { province, city, county, street ->
            toast(province.name + city.name + county.name + street.name)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun initEvents() {
        OkGo.post(url().auth_api + "get_our_address")
                .execute(object : StringCallback() {
                    override fun onSuccess(model: String, call: okhttp3.Call?, response: okhttp3.Response?) {
                        var m = Gson().fromJson(model, LzyResponse::class.java)
                        if (m.data != null) {
                            var data = m as LzyResponse<AddressModel>
                            name_tv.text = data.data!!.name
                            tel_tv.text = data.data!!.tel
                            address_tv.text = data.data!!.address
                            qq_tv.text = data.data!!.qq

                            add_btn.visibility = View.GONE
                        } else {
                            add_btn.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        toast(common().toast_error(e!!))
                    }
                })
    }
}
