package wai.gr.cla.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kaopiz.kprogresshud.KProgressHUD
import com.lzy.okgo.OkGo
import kotlinx.android.synthetic.main.activity_ask_list.*
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

/**
 * 提问管理
 * */
class AskListActivity : BaseActivity() {
    var zx_adapter: CommonAdapter<ZiXunModel>? = null//资讯
    var zx_list = ArrayList<ZiXunModel>()
    internal var page_index = 1//当前页数
    internal var teacher_id = 1//老师id
    internal var cid = 0
    override fun setLayout(): Int {
        return R.layout.activity_ask_list
    }

    override fun initViews() {
        teacher_id = intent.getIntExtra("id", 1)
        cid = intent.getIntExtra("cid", 0)
        toolbar.center_str = intent.getStringExtra("name")
        zx_adapter = object : CommonAdapter<ZiXunModel>(this, zx_list, R.layout.item_zixun) {
            override fun convert(holder: CommonViewHolder, model: ZiXunModel, position: Int) {
                holder.setGlideImage(R.id.user_iv, model.author_img)
                holder.setText(R.id.title_tv, model.title)
                holder.setText(R.id.data_tv, model.show_time)
                //holder.setText(R.id.sc_tv, model.dots.toString())
                //holder.setText(R.id.pl_tv, model.comments.toString())
                holder.setInVisible(R.id.zixun_item_xihuan)
                holder.setInVisible(R.id.item_zixun_ll_dot)
            }
        }

        toolbar.setLeftClick { finish() }
        asl_llv.adapter = zx_adapter
        main_srl.setOnRefreshListener { loadData(1) }//刷新加载数据
        //asl_llv.emptyView = error_ll

        asl_llv.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(MainActivity.main, ZiXunDetailActivity::class.java)
            intent.putExtra("cid", zx_list[i].id.toString())
            intent.putExtra("title", "资讯")
            intent.putExtra("is_dy",true)
            startActivity(intent)
        }
        asl_llv.setInterface { loadData(page_index++) }//上划加载更多数据
        ask_btn.setOnClickListener { startActivityForResult(Intent(this@AskListActivity, AddTeacherAskActivity::class.java).putExtra("teacher_id", cid), 0) }//跳转到添加提问页面
        hf_rl.setOnClickListener { startActivity(Intent(this@AskListActivity, HFActivity::class.java).putExtra("id", cid)) }
        loadData(1)
        val num = intent.getIntExtra("answer_num", 0)
        if (num > 0) {
            answer_num_tv.visibility = View.VISIBLE
            answer_num_tv.text = num.toString()
        }
    }

    /**
     * 加载数据
     * */
    fun loadData(index: Int) {
        page_index = index//获得当前要加载页面id
        main_srl.isRefreshing = true
        OkGo.get(url().public_api + "get_phone_teacher_news_list")
                .params("teacher_id", teacher_id)
                .params("page", page_index)
                .execute(object : JsonCallback<LzyResponse<PageModel<ZiXunModel>>>() {
                    override fun onSuccess(t: LzyResponse<PageModel<ZiXunModel>>, call: okhttp3.Call?, response: okhttp3.Response?) {
                        zx_list = t.data!!.list as ArrayList<ZiXunModel>
                        zx_adapter!!.refresh(zx_list)
                        asl_llv.getIndex(page_index, 20, zx_list.size)
                        main_srl.isRefreshing = false
                        if(zx_list.size == 0){
                            error_ll.visibility=View.VISIBLE;
                        }else{
                            error_ll.visibility=View.GONE;
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