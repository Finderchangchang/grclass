package wai.gr.cla.online.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tv.buka.sdk.demo.show.R;
import tv.buka.sdk.demo.show.entity.UserBean;
import tv.buka.sdk.entity.User;

public class UserAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater = null;
	private List<User> mList;

	public UserAdapter(Activity activity) {
		super();
		this.mLayoutInflater = LayoutInflater.from(activity);
		this.mList = new ArrayList<User>();
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
			convertView = mLayoutInflater.inflate(R.layout.cell_user, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(new UserBean(mList.get(arg0).getUser_extend()).getUser_nickname());
		return convertView;
	}

	public static class ViewHolder {
		TextView name;
	}

	public List<User> getmList() {
		return mList;
	}

	public void setmList(List<User> mList) {
		this.mList = mList;
	}

}
