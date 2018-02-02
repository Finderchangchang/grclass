package wai.gr.cla.online.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import tv.buka.sdk.demo.show.R;

public class ContentAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private LinkedList<String> mList;
	private LinkedList<String> mColorList;

	public ContentAdapter(Activity activity) {
		super();
		this.mLayoutInflater = LayoutInflater.from(activity);
		this.mList = new LinkedList<String>();
		this.mColorList = new LinkedList<String>();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.cell_content, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setText(mList.get(mList.size() - arg0 - 1));
		holder.text.setTextColor(Color.parseColor(mColorList.get(mColorList.size() - arg0 - 1)));
		return convertView;
	}

	public static class ViewHolder {
		TextView text;
	}

	public LayoutInflater getmLayoutInflater() {
		return mLayoutInflater;
	}

	public void setmLayoutInflater(LayoutInflater mLayoutInflater) {
		this.mLayoutInflater = mLayoutInflater;
	}

	public LinkedList<String> getmList() {
		return mList;
	}

	public void setmList(LinkedList<String> mList) {
		this.mList = mList;
	}

	public LinkedList<String> getmColorList() {
		return mColorList;
	}

	public void setmColorList(LinkedList<String> mColorList) {
		this.mColorList = mColorList;
	}

}
