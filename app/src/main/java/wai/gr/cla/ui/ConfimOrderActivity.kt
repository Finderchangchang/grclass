package wai.gr.cla.ui

import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import kotlinx.android.synthetic.main.activity_confim_order.*
import okhttp3.Call
import okhttp3.Response

import wai.gr.cla.R
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.callback.JsonCallback
import wai.gr.cla.method.CommonAdapter
import wai.gr.cla.method.CommonViewHolder
import wai.gr.cla.method.common
import wai.gr.cla.model.*
import java.util.*
import android.opengl.ETC1.getHeight
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.widget.Toast
import com.tsy.sdk.pay.alipay.Alipay
import com.tsy.sdk.pay.weixin.WXPay
import wai.gr.cla.base.App


class ConfimOrderActivity : BaseActivity() {
    var model: LzyResponse<String>? = null
    var kc1_adapter: CommonAdapter<CarModel>? = null//购买的课程
    var car_list: ArrayList<CarModel> = ArrayList()
    var quan_list: ArrayList<QuanModel> = ArrayList()
    var zfb_click = true//true点击支付宝
    var old_price = 0.0//最开始的价格
    var address: AddressModel? = null
    var orders = ""
    override fun setLayout(): Int {
        return R.layout.activity_confim_order
    }

    override fun initViews() {
        model = intent.getSerializableExtra("model") as LzyResponse<String>
        car_list = model!!.car!! as ArrayList<CarModel>
        total_good_tv.text = "共" + car_list.size + "件商品"

        for (model in car_list) {
            old_price += model.price.toDouble()
            orders += model.course_id.toString() + ","
        }
        orders = orders.substring(0, orders.length - 1)
        total_price_tv.text = old_price.toString()
        kc1_adapter = object : CommonAdapter<CarModel>(this, car_list, R.layout.item_car) {
            override fun convert(holder: CommonViewHolder, model: CarModel, position: Int) {
                holder.setText(R.id.title_tv, model.course_title)
                holder.setText(R.id.price_tv, "￥" + model.price)
                holder.setGImage(R.id.total_iv, url().total + model.thumbnail)
                holder.setVisible(R.id.check_iv, false)
            }
        }
        class_gvs.adapter = kc1_adapter
        zfb_ll.setOnClickListener {
            zfb_click = true
            refresh_zfb()
        }
        wx_ll.setOnClickListener {
            zfb_click = false
            refresh_zfb()
        }
        pay_tv.setOnClickListener {
            pay()
        }
    }

    fun refresh_zfb() {
        if (zfb_click) {
            zfb_iv.setImageResource(R.mipmap.chose_s)
            wx_iv.setImageResource(R.mipmap.chose_n)
        } else {
            zfb_iv.setImageResource(R.mipmap.chose_n)
            wx_iv.setImageResource(R.mipmap.chose_s)
        }
    }

    val items = arrayOf<String>()
    var can_user_quan = ArrayList<QuanModel>()
    override fun initEvents() {
        load_address()
        load_quan()
        address_rl.setOnClickListener {
            startActivityForResult(Intent(this@ConfimOrderActivity, AddAddressActivity::class.java), 0)
        }
        dialog_iv.setOnClickListener {
            if (can_user_quan.size > 0) {
                var builder = AlertDialog.Builder(this);
                for (i in 0..can_user_quan.size - 1) {
                    items[i] = can_user_quan[i].type_name
                }
                builder.setItems(items) { dialogInterface, i ->
                    check_quan(can_user_quan[i].coupon_code)
                }
                builder.show();
            } else {
                toast("您没有优惠券")
            }
        }
        code_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var code = code_et.text.toString().trim()
                if (code.length == 6) {
                    check_quan(code)
                } else {
                    yh_tv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == 77) {//地址有刷新
            load_address()
        }
    }

    /**
     * 支付管理
     * */
    fun pay() {
        if (address != null) {
            OkGo.post(url().auth_api + "create_new_course_order")
                    .params("course_id", orders)
                    .params("coupon_code", code_et.text.toString().trim())
                    .params("province", address!!.province)
                    .params("city", address!!.city)
                    .params("address", address!!.address)
                    .params("tel", address!!.tel)
                    .params("user_name", address!!.name)
                    .params("qq", address!!.qq)

                    .execute(object : StringCallback() {
                        override fun onSuccess(model: String, call: okhttp3.Call?, response: okhttp3.Response?) {
                            var key = Gson().fromJson(model, LzyResponse::class.java)
                            if (key.code == 0) {//生成预订单成功
                                var new_order=key.data as NewOrderModel
                                save_pay(new_order.trade_no)
                            }
                        }

                        override fun onError(call: Call?, response: Response?, e: Exception?) {
                            toast(common().toast_error(e!!))
                        }
                    })
        } else {
            toast("地址不能为空")
        }
    }

    //满减以后的最终价格
    fun man_jian(price: Double) {
        OkGo.post(url().auth_api + "get_subtract_price")
                .params("price", price)
                .execute(object : StringCallback() {
                    override fun onSuccess(model: String, call: okhttp3.Call?, response: okhttp3.Response?) {
                        var key = Gson().fromJson(model, LzyResponse::class.java)
                        if (key.code == 0) {
                            var model = key.data as QuanModel
                            yh_tv.visibility = View.VISIBLE
                            total_price_tv.text = "￥" + model.new_price
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        toast(common().toast_error(e!!))
                    }
                })
    }

    //加载当前优惠券
    fun check_quan(quan: String) {
        OkGo.post(url().auth_api + "get_coupon_info")
                .params("coupon_code", quan)
                .execute(object : JsonCallback<LzyResponse<QuanModel>>() {
                    override fun onSuccess(model: LzyResponse<QuanModel>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        if (model.code == 0) {
                            yh_tv.visibility = View.VISIBLE
                            yh_tv.text = "优惠：" + model.data!!.coupon_price + "元"
                            man_jian(old_price - model.data!!.coupon_price.toDouble())
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        toast(common().toast_error(e!!))
                    }
                })
    }

    //加载当前优惠券列表
    fun load_quan() {
        OkGo.post(url().auth_api + "get_coupon_list")
                .execute(object : JsonCallback<LzyResponse<ArrayList<QuanModel>>>() {
                    override fun onSuccess(model: LzyResponse<ArrayList<QuanModel>>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        if (model.code == 0) {
                            for (key in model.data!!) {
                                if (key.expiration_status < 2) {
                                    can_user_quan.add(key)
                                }
                            }
                            quan_list = model.data!!
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        toast(common().toast_error(e!!))
                    }
                })
    }

    //加载当前地址
    fun load_address() {
        OkGo.post(url().auth_api + "get_our_address")
                .execute(object : JsonCallback<LzyResponse<AddressModel>>() {
                    override fun onSuccess(model: LzyResponse<AddressModel>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        if (model.data != null) {
                            address = model.data
                            user_tv.text = "收件人：" + model.data!!.name
                            tel_tv.text = "联系方式：" + model.data!!.tel
                            ss_port_tv.text = "所在区域：" + model.data!!.province + model.data!!.city
                            address_tv.text = "详细地址：" + model.data!!.address
                            qq_tv.text = "QQ号码：" + model.data!!.qq
                            add_address_ll.visibility = View.GONE
                            address_ll.visibility = View.VISIBLE
                        } else {
                            add_address_ll.visibility = View.VISIBLE
                            address_ll.visibility = View.GONE
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        toast(common().toast_error(e!!))
                    }
                })
    }

    /**
     * 根据订单id生成预订单
     * */
    fun save_pay(id: String) {
        /**
         * 调起微信支付
         * */
        if (!zfb_click) {
            OkGo.post(url().wx_api + "unifiedorder")
                    .params("id", id)
                    .execute(object : JsonCallback<LzyResponse<PayModel>>() {
                        override fun onSuccess(model: LzyResponse<PayModel>, call: okhttp3.Call?, response: okhttp3.Response?) {
                            if (model.code == 0) {
                                val data = Gson().toJson(model.data)
                                val wx_appid = App.wx_id     //替换为自己的 appid
                                WXPay.init(this@ConfimOrderActivity, wx_appid)      //要在支付前调用
                                WXPay.getInstance().doPay(data, object : WXPay.WXPayResultCallBack {
                                    override fun onSuccess() {
                                        check_pay_status(id)
                                    }

                                    override fun onError(error_code: Int) {
                                        when (error_code) {
                                            WXPay.NO_OR_LOW_WX -> toast("未安装微信或微信版本过低")

                                            WXPay.ERROR_PAY_PARAM -> toast("参数错误")

                                            WXPay.ERROR_PAY -> toast("支付失败")
                                        }
                                    }

                                    override fun onCancel() {
                                        toast( "支付取消")
                                    }
                                })
                            } else {
                                toast("生成预订单失败")
                            }
                        }

                        override fun onError(call: Call?, response: Response?, e: Exception?) {
                            toast(common().toast_error(e!!))
                        }
                    })
        } else {
            /**
             * 调起支付宝支付
             * */
            OkGo.post(url().normal + "alipay/create_response")
                    .params("id", id)
                    .execute(object : JsonCallback<LzyResponse<String>>() {
                        override fun onSuccess(model: LzyResponse<String>, call: okhttp3.Call?, response: okhttp3.Response?) {
                            if (model.code == 0) {
                                Alipay(this@ConfimOrderActivity, model.data, object : Alipay.AlipayResultCallBack {
                                    override fun onSuccess() {
                                        check_pay_status(id)
                                    }

                                    override fun onDealing() {
                                        toast("支付处理中...")
                                    }

                                    override fun onError(error_code: Int) {
                                        when (error_code) {
                                            Alipay.ERROR_RESULT -> toast( "支付失败:支付结果解析错误")

                                            Alipay.ERROR_NETWORK -> toast( "支付失败:网络连接错误")

                                            Alipay.ERROR_PAY -> toast( "支付错误:支付码支付失败")

                                            else -> toast("支付错误")
                                        }
                                    }

                                    override fun onCancel() {
                                        toast("支付取消")
                                    }
                                }).doPay()
                            } else {
                                toast("生成预订单失败")
                            }
                        }

                        override fun onError(call: Call?, response: Response?, e: Exception?) {
                            toast(common().toast_error(e!!))
                        }
                    })
        }

    }

    /**
     * 检测是否支付成功 id：订单状态
     * */
    fun check_pay_status(id: String) {
        OkGo.post(url().normal + "pay/get_pay_status")
                .params("id", id)
                .execute(object : JsonCallback<LzyResponse<String>>() {
                    override fun onSuccess(model: LzyResponse<String>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        if (model.code == 0) {
                            toast("支付成功")
                        } else {
                            toast(model.msg!!)
                        }
                    }
                })
    }
}
