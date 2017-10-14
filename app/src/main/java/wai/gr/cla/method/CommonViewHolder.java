package wai.gr.cla.method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import wai.gr.cla.R;
import wai.gr.cla.glideimg.GlideImgManager;


/**
 * Created by liuliu on 2015/11/16   16:29
 *
 * @author 柳伟杰
 * @Email 1031066280@qq.com
 */
public class CommonViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;

    private CommonViewHolder(Context context, ViewGroup parent, int layoutId,
                             int position) {
        this.mPosition = position;
        this.mContext = context;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mConvertView.setTag(this);
    }

    public CommonViewHolder setHeight(int viewId, int height) {
        LinearLayout view = getView(viewId);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
        return this;
    }

    public CommonViewHolder setMargin(int viewId, int left, int right, int top, int bottom) {
        LinearLayout ll = getView(viewId);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
        layoutParams.setMargins(left, right, top, bottom);//4个参数按顺序分别是左上右下
        ll.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        }
        return (CommonViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text + "");
        view.setVisibility(View.VISIBLE);
        return this;
    }

    public CommonViewHolder setBGText(int viewId, String text) {
        Button view = getView(viewId);
        view.setText(text + "");
        return this;
    }

    public CommonViewHolder setBG(int viewId, int text) {
        View view = getView(viewId);
        view.setBackgroundResource(text);
        return this;
    }

    public CommonViewHolder setBGColor(int viewId, int text) {
        View view = getView(viewId);
        view.setBackgroundColor(text);
        return this;
    }

    public CommonViewHolder setTextColor(int viewId, int text) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(text));
        return this;
    }

    public CommonViewHolder setStar(int viewId, String num) {
        RatingBar rb = getView(viewId);
        rb.setNumStars(Integer.parseInt(num));
        return this;
    }


    //获取知道textview的值
    public String getText(int viewId) {
        TextView view = getView(viewId);
        return view.getText().toString().trim();
    }

    /**
     * 设置监听时间
     *
     * @param viewId
     * @param listener
     * @return
     */
    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public CommonViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    //正常加载图片
    public CommonViewHolder setGImage(int vid, String url) {
        ImageView img = getView(vid);
        img.setVisibility(View.VISIBLE);
        if (mContext != null) {
            Glide.with(mContext).load(url).error(R.mipmap.error_img_big).into(img);
        }
        return this;
    }

    //圆形图片
    public CommonViewHolder setGlideImage(int vid, String url) {
        ImageView img = getView(vid);
        if (mContext != null) {
            GlideImgManager.glideLoader(mContext, url, R.mipmap.denglu_logo, R.mipmap.denglu_logo, img, 0);
        }
        return this;
    }

    public CommonViewHolder setGlideImageYuan(int vid, String url) {
        ImageView img = getView(vid);
        if (mContext != null) {
            GlideImgManager.glideLoader(mContext, url, R.mipmap.error_yuan1, R.mipmap.error_yuan1, img, 0);
        }
        return this;
    }

    //方形圆角
    public CommonViewHolder setRoundImage(int vid, String url) {
        ImageView img = getView(vid);
        GlideImgManager.glideLoader(mContext, url, R.mipmap.error_img_sml, R.mipmap.error_img_sml, img, 1);
        return this;
    }
    public CommonViewHolder setTopRoundImage(int vid,String url){
        ImageView img = getView(vid);

        Glide.with(mContext)
                .load(url)
                //                .bitmapTransform(new GrayscaleTransformation(this))//带灰色蒙层
                .bitmapTransform(new RoundedCornersTransformation(mContext, 10, 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(img);
        return this;
    }
    //方形圆角
    public CommonViewHolder setSmallRoundImage(int vid, String url) {
        ImageView img = getView(vid);
        GlideImgManager.glideLoader(mContext, url, R.mipmap.error_img_sml, R.mipmap.error_img_sml, img, 1);
        return this;
    }

    //长方形
    public CommonViewHolder setRoundFangImage(int vid, String url) {
        ImageView img = getView(vid);
        if (mContext != null) {
            GlideImgManager.glideLoader(mContext, url, R.mipmap.error_img_sml, R.mipmap.error_img_sml, img, 2);
        }
        return this;
    }

    public CommonViewHolder setImageDrawable(int viewId, Drawable bm) {
        ImageView view = getView(viewId);
        view.setImageDrawable(bm);
        return this;
    }

    private ImageView setImageTag(int viewId, String url) {
        ImageView view = getView(viewId);
        view.setTag(url);
        return view;
    }

    public CommonViewHolder setInVisible(int viewId) {
        View view = getView(viewId);
        view.setVisibility(View.INVISIBLE);
        return this;
    }

    /**
     * 设置控件显示隐藏
     *
     * @param viewId（控件id）
     * @param result(控件显示隐藏)
     */
    public CommonViewHolder setVisible(int viewId, boolean result) {
        View view = getView(viewId);
        if (result) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}
