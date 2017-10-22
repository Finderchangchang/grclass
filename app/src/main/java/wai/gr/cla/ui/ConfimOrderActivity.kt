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


class ConfimOrderActivity : BaseActivity() {
    var model: LzyResponse<String>? = null
    var kc1_adapter: CommonAdapter<CarModel>? = null//购买的课程
    var car_list: ArrayList<CarModel> = ArrayList()
    var quan_list: ArrayList<QuanModel> = ArrayList()
    var zfb_click=true//true点击支付宝
    override fun setLayout(): Int {
        return R.layout.activity_confim_order
    }

    override fun initViews() {
        model = intent.getSerializableExtra("model") as LzyResponse<String>
        car_list= model!!.car!! as ArrayList<CarModel>
        total_good_tv.text="共"+car_list.size+"件商品"
        var price=0.0
        for(model in car_list){
            price+=model.price.toDouble()
        }
        total_price_tv.text=price.toString()
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
            zfb_click=true
            refresh_zfb()
        }
        wx_ll.setOnClickListener {
            zfb_click=false
            refresh_zfb()
        }
        code_et.onFocusChangeListener
        //监听软键盘是否显示或隐藏
        code_et.setOnFocusChangeListener{view, b ->
            toast("结果："+b)
        }
    }
    fun refresh_zfb(){
        if(zfb_click){
            zfb_iv.setImageResource(R.mipmap.chose_s)
            wx_iv.setImageResource(R.mipmap.chose_n)
        }else{
            zfb_iv.setImageResource(R.mipmap.chose_n)
            wx_iv.setImageResource(R.mipmap.chose_s)
        }
    }
    val items = arrayOf<String>()
    var can_user_quan=ArrayList<QuanModel>()
    override fun initEvents() {
        load_address()
        load_quan()
        add_address_ll.setOnClickListener {
            startActivityForResult(Intent(this@ConfimOrderActivity,AddAddressActivity::class.java),0)
        }
        dialog_iv.setOnClickListener {
            if(can_user_quan.size>0) {
                var builder = AlertDialog.Builder(this);
                for (i in 0..can_user_quan.size - 1) {
                    items[i] = can_user_quan[i].type_name
                }
                builder.setItems(items) { dialogInterface, i ->
                    check_quan(can_user_quan[i].coupon_code)
                }
                builder.show();
            }else{
                toast("您没有优惠券")
            }
        }
        code_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var code=code_et.text.toString().trim()
                if(code.length==6){
                    check_quan(code)
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0&&resultCode==77){//地址有刷新
            load_address()
        }
    }
    //加载当前优惠券
    fun check_quan(quan:String) {
        OkGo.post(url().auth_api + "get_coupon_info")
                .params("coupon_code",quan)
                .execute(object : JsonCallback<LzyResponse<QuanModel>>() {
                    override fun onSuccess(model: LzyResponse<QuanModel>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        if(model.code==0){
                            yh_tv.text="优惠："+model.data!!.coupon_price+"元"
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
                       if(model.code==0){
                           for(key in model.data!!){
                               if (key.expiration_status<2){
                                   can_user_quan.add(key)
                               }
                           }
                           quan_list=model.data!!
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
                            user_tv.text = "收件人："+model.data!!.name
                            tel_tv.text = "联系方式："+model.data!!.tel
                            ss_port_tv.text = "所在区域："+model.data!!.province+model.data!!.city
                            address_tv.text = "详细地址："+model.data!!.address
                            qq_tv.text = "QQ号码："+model.data!!.qq
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
}
