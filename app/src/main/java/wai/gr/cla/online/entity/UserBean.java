package wai.gr.cla.online.entity;

import com.geekbean.android.utils.GB_JsonUtils;

public class UserBean {
	private String user_nickname;

	public UserBean(String json) {
		UserBean userBean = GB_JsonUtils.getBean(json, getClass());
		setUser_nickname(userBean.getUser_nickname());
	}

	public UserBean() {
	}

	public String getUser_nickname() {
		return user_nickname;
	}

	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}

}
