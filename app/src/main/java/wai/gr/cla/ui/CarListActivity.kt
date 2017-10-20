package wai.gr.cla.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import kotlinx.android.synthetic.main.activity_car_list.*
import okhttp3.Call
import okhttp3.Response

import wai.gr.cla.R
import wai.gr.cla.base.BaseActivity
import wai.gr.cla.callback.JsonCallback
import wai.gr.cla.method.CommonAdapter
import wai.gr.cla.method.CommonViewHolder
import wai.gr.cla.method.common
import wai.gr.cla.model.CarModel
import wai.gr.cla.model.LzyResponse
import wai.gr.cla.model.TradeModel
import wai.gr.cla.model.url
import java.util.*

class CarListActivity : BaseActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_car_list
    }

    internal var kc1_list: ArrayList<CarModel> = ArrayList()//购买的课程列表
    var kc1_adapter: CommonAdapter<CarModel>? = null//购买的课程
    override fun initViews() {
        kc1_adapter = object : CommonAdapter<CarModel>(this, kc1_list, R.layout.item_car) {
            override fun convert(holder: CommonViewHolder, model: CarModel, position: Int) {
                holder.setText(R.id.title_tv, model.course_title)
                holder.setText(R.id.price_tv, "￥" + model.price)
                holder.setGImage(R.id.total_iv, url().total + model.thumbnail)
                if (model.isChecked) {
                    holder.setImageResource(R.id.check_iv, R.mipmap.ic_launcher)
                } else {
                    holder.setImageResource(R.id.check_iv, R.mipmap.icon_font)
                }
                holder.setOnClickListener(R.id.check_iv) {
                    model.isChecked = !model.isChecked
                    if (model.isChecked) {
                        holder.setImageResource(R.id.check_iv, R.mipmap.ic_launcher)
                    } else {
                        holder.setImageResource(R.id.check_iv, R.mipmap.icon_font)
                    }
                    refresh_data()
                }
            }
        }
        main_lv.adapter = kc1_adapter
        main_srl.setOnRefreshListener {
            load_data()
        }
        toolbar.setRightClick {
            tool_click = !tool_click
            if (tool_click) {//编辑模式
                toolbar.setRighttv("完成")
                refresh_data()
            } else {//完成模式
                toolbar.setRighttv("编辑")
                refresh_data()
            }
        }
        load_data()
        check_all_rb.setOnClickListener {
            check_all = !check_all
            check_all_rb.isChecked = check_all

            for (model in kc1_list) {
                model.isChecked = check_all
            }
            refresh_data()
        }
    }

    var tool_click = false//true:点击false：完成
    var check_all = false//true:选中全部
    var checked_size = 0
    var checked_price = 0.0
    //刷新价格
    fun refresh_data() {
        checked_size = 0
        checked_price = 0.0
        for (model in kc1_list) {
            if (model.isChecked) {
                checked_size++
                if (!TextUtils.isEmpty(model.price))
                    checked_price += model.price.toDouble()
            }
        }
        check_all_rb.isChecked = checked_size == kc1_list.size
        if (tool_click) {
            pay_tv.text = "删除"
            hj_tv.visibility = View.GONE
            total_price_tv.visibility = View.GONE
        } else {
            pay_tv.text = "去结算($checked_size)"
            hj_tv.visibility = View.VISIBLE
            total_price_tv.visibility = View.VISIBLE
            total_price_tv.text = "￥$checked_price"
        }
        kc1_adapter!!.refresh(kc1_list)
    }

    fun load_data() {
        OkGo.post(url().auth_api + "get_shopcar_infos")
                .execute(object : JsonCallback<LzyResponse<ArrayList<CarModel>>>() {
                    override fun onSuccess(model: LzyResponse<ArrayList<CarModel>>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        kc1_list.clear()
                        kc1_list.addAll(model.data!!)
                        kc1_adapter!!.refresh(kc1_list)
                        main_srl.isRefreshing = false
                        main_lv.getIndex(1, 10, 1)
                        if (kc1_list!!.size == 0) {
                            error_ll.visibility = View.VISIBLE;
                            main_lv.visibility = View.GONE;
                        } else {
                            error_ll.visibility = View.GONE;
                            main_lv.visibility = View.VISIBLE;
                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        toast(common().toast_error(e!!))
                        main_srl.isRefreshing = false
                    }
                })
    }

    override fun initEvents() {

    }
}
