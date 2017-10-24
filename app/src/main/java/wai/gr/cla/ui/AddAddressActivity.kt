package wai.gr.cla.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import chihane.jdaddressselector.BottomDialog
import com.lzy.okgo.OkGo
import com.umeng.socialize.utils.Log
import kotlinx.android.synthetic.main.activity_add_address.*

import wai.gr.cla.R
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.callback.JsonCallback
import wai.gr.cla.method.Utils
import wai.gr.cla.method.common
import wai.gr.cla.model.*

/**
 * 地址添加
 * */
class AddAddressActivity : BaseActivity() {
    var address = AddressModel()
    override fun setLayout(): Int {
        return R.layout.activity_add_address
    }

    override fun initViews() {
        toolbar.setLeftClick { finish() }
    }

    override fun initEvents() {
        address = intent.getSerializableExtra("model") as AddressModel
        if (address.id != 0) {
            et_address_name.setText(address.name)
            et_address_qq.setText(address.qq)
            et_address_tel.setText(address.tel)
            et_address_city.setText(address.province + address.city)
            et_address_address.setText(address.address)
        }
        et_address_city.setOnClickListener {
            val dialog = BottomDialog(this)
            dialog.setOnAddressSelectedListener { province, city, county, street ->
                //toast(province.name + city.name + county.name + street.name)
                address.province = province!!.name
                address.city = city!!.name
                et_address_city.setText(address.province + address.city)
                dialog.dismiss()
            }
            dialog.show()
        }

        toolbar.setRightClick {
            //保存
            if (checkvalue()) {
                getValue()
                //向服务器提交
                var ok = OkGo.post(url().auth_api + "save_user_address")
                if(address.id != 0){
                    ok.params("id", address.id)
                }
                ok.params("name", address.name)// 请求方式和请求url
                        .params("tel", address.tel)// 请求方式和请求url
                        .params("province", address.province)// 请求方式和请求url
                        .params("address", address.address)
                        .params("qq", address.qq)
                        .params("city", address.city)// 请求方式和请求url
                        .execute(object : JsonCallback<LzyResponse<MyBussinessModel>>() {
                            override fun onSuccess(t: LzyResponse<MyBussinessModel>, call: okhttp3.Call?, response: okhttp3.Response?) {
                                if (t.code == 0) {
                                    toast(t.msg!!)
                                    setResult(77)
                                    finish()
                                }
                            }

                            override fun onError(call: okhttp3.Call?, response: okhttp3.Response?, e: Exception?) {
                                toast(common().toast_error(e!!))
                            }
                        })
            }
        }


    }

    //验证数据
    fun checkvalue(): Boolean {
        if (et_address_name.text.toString().trim().equals("")) {
            toast("请填写收件人姓名")
            return false
        } else if (et_address_qq.text.toString().trim().equals("")) {
            toast("请填写联系QQ号码")
            return false
        } else if (et_address_tel.text.toString().trim().equals("")) {
            toast("请填写联系方式")
            return false
        } else if (address.province!!.toString().equals("") || address.city!!.toString().equals("")) {
            toast("请选择所在省份和城市")
            return false
        } else if (et_address_address.text.toString().trim().equals("")) {
            toast("请填写详细地址")
            return false
        }

        return true
    }

    //获取数据
    fun getValue() {
        address.address = et_address_address.text.toString().trim()
        address.tel = et_address_tel.text.toString().trim()
        address.qq = et_address_qq.text.toString().trim()
        address.name = et_address_name.text.toString().trim()

    }

}
