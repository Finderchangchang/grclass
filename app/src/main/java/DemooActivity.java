package wai.gr.cla;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bkrtc_sdk.StreamBandwidth;
import com.bkrtc_sdk.bkrtc_impl;
import com.geekbean.android.utils.GB_DeviceUtils;
import com.geekbean.android.utils.GB_JsonUtils;
import com.geekbean.android.utils.GB_SecurityUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.List;

import tv.buka.sdk.BukaSDK;
import tv.buka.sdk.BukaSDKManager;
import tv.buka.sdk.BukaSDKVersion;
import tv.buka.sdk.entity.Chat;
import tv.buka.sdk.entity.Num;
import tv.buka.sdk.entity.Room;
import tv.buka.sdk.entity.Rpc;
import tv.buka.sdk.entity.Status;
import tv.buka.sdk.entity.Stream;
import tv.buka.sdk.entity.User;
import tv.buka.sdk.listener.ChatListener;
import tv.buka.sdk.listener.ConnectListener;
import tv.buka.sdk.listener.ReceiptListener;
import tv.buka.sdk.listener.RpcListener;
import tv.buka.sdk.listener.StatusListener;
import tv.buka.sdk.listener.UserListener;
import wai.gr.cla.R;
import wai.gr.cla.online.adapter.ContentAdapter;
import wai.gr.cla.online.adapter.UserAdapter;
import wai.gr.cla.online.entity.UserBean;

public class DemooActivity extends Activity implements OnClickListener {
    private final String KEY_ROOM = "KEY_ROOM";
    private final String KEY_NICKNAME = "KEY_NICKNAME";
    private final String KEY_IP = "KEY_IP";
    private ContentAdapter contentAdapter;
    private UserAdapter userAdapter;
    private TextView userTitleText, serverText, login;
    private ArrayAdapter<String> ipSpinnerAdapter;
    private LinearLayout mStreamLayout;
    private LinearLayout mStreamLayout2;
    private LinearLayout mPlayLayout;
    private LinearLayout mPlayLayout2;
    private int mCameraId = 0;
    private Stream mPublishStreamStatus;
    private Stream mPublishStreamStatus2;
    private TextView switchCamera;
    private String mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initFrame();
        this.initBuka();
        mDeviceId = GB_DeviceUtils.getDeviceId(this);
        this.initAve();

        requestPermisson();
        BukaSDKManager.getConnectManager().connect(GB_DeviceUtils.getDeviceId(getApplicationContext()), "",
                new ReceiptListener() {

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });
        logins();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (playAid != 0 && play2Vid != 0) {
//            stopPlay();
//        }
//        if (play2Aid != 0 && play2Vid != 0) {
//            stopPlay2();
//        }
//        if (mPublishStreamStatus != null) {
//            stopPublish(mPublishStreamStatus.aid, mPublishStreamStatus.vid);
//        }
//        if (mPublishStreamStatus2 != null) {
//            stopPublish(mPublishStreamStatus2.aid, mPublishStreamStatus2.vid);
//        }
        logout();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (playAid != 0 && play2Vid != 0) {
//            stopPlay();
//        }
//        if (play2Aid != 0 && play2Vid != 0) {
//            stopPlay2();
//        }
//        if (mPublishStreamStatus != null) {
//            stopPublish(mPublishStreamStatus.getAid(), mPublishStreamStatus.getVid());
//        }
//        if (mPublishStreamStatus2 != null) {
//            stopPublish(mPublishStreamStatus2.getAid(), mPublishStreamStatus2.getVid());
//        }
        logout();

    }

    private void requestPermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionList = new ArrayList<>();
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (this.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CALL_PHONE);
            }
            if (this.checkSelfPermission(Manifest.permission.READ_LOGS)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_LOGS);
            }
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (this.checkSelfPermission(Manifest.permission.SET_DEBUG_APP)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.SET_DEBUG_APP);
            }
            if (this.checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
            }
            if (this.checkSelfPermission(Manifest.permission.GET_ACCOUNTS)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.GET_ACCOUNTS);
            }
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.RECORD_AUDIO);
            }
            if (this.checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, permissions[i] + "，权限别用户拒绝了。", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initTest() {
//        int num = BukaSDKManager.getUserManager().getUserArr().size();
//        if (num < 2) {
//            publish();
//            publish2();
//        } else {
//            play();
//            play2();
//        }

        //publish();
        //publish2();

    }

    private void addLog(String log, String color) {
        contentAdapter.getmList().push(log);
        if (contentAdapter.getmList().size() > 1000) {
            contentAdapter.getmList().poll();
        }
        contentAdapter.getmColorList().push(color);
        if (contentAdapter.getmColorList().size() > 1000) {
            contentAdapter.getmColorList().poll();
        }
        contentAdapter.notifyDataSetChanged();
        ListView contentListView = (ListView) findViewById(R.id.content_list);
        contentListView.setSelection(contentAdapter.getmList().size() - 1);
    }

    private void addLog(String log) {
        addLog(log, "#ffffff");
    }

    private void clearLog() {
        contentAdapter.getmList().clear();
        contentAdapter.getmColorList().clear();
        contentAdapter.notifyDataSetChanged();
    }

    private void initBuka() {
        //河北冠人教育
        BukaSDK.init("760daf0e36d158834ba3cbf7a53b1225",
                getApplicationContext(), BukaSDKVersion.BukaSDKVersion3);
        BukaSDKManager.getConnectManager().addListener(new ConnectListener() {

            @Override
            public void onSessionPackageLost() {
                // TODO Auto-generated method stub
                //addLog("丢包，正在离线补包（暂未实现）", "#ff0000");
            }

            @Override
            public void onSessionOff() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSessionDisconnected() {
                // TODO Auto-generated method stub
                stopPublish();
                stopPublish2();
                stopPlay();
                stopPlay2();
            }

            @Override
            public void onSessionConnected() {
                // TODO Auto-generated method stub
                serverText.setText(BukaSDKManager.getConnectManager()
                        .getSession().getServer_ip());
            }

            @Override
            public void onServerArrChanged() {
                // TODO Auto-generated method stub
                if (!BukaSDKManager.getConnectManager().isConnect()
                        && !BukaSDKManager.getConnectManager().isConnecting()) {

                }
                ipSpinnerAdapter.clear();
                List<String> ipList = BukaSDKManager.getConnectManager()
                        .getServerArr();
                for (String ip : ipList) {
                    ipSpinnerAdapter.add(ip);
                }
                ipSpinnerAdapter.notifyDataSetChanged();
                login.setText(getString(R.string.login_text));
                login.setBackgroundResource(R.color.app_red_color);
                login.setEnabled(true);
            }
        });
        BukaSDKManager.getUserManager().addListener(new UserListener() {

            @Override
            public void onUserIn(User user) {
                // TODO Auto-generated method stub
                userAdapter.setmList(BukaSDKManager.getUserManager()
                        .getUserArr());
                userAdapter.notifyDataSetChanged();
                addLog("用户" + new UserBean(user.getUser_extend()).getUser_nickname()
                        + "进入房间");
            }

            @Override
            public void onUserChanged(User oldUser, User user) {
                // TODO Auto-generated method stub
                userAdapter.setmList(BukaSDKManager.getUserManager()
                        .getUserArr());
                userAdapter.notifyDataSetChanged();
                addLog("用户"
                        + new UserBean(oldUser.getUser_extend()).getUser_nickname()
                        + "将昵称修改成"
                        + new UserBean(user.getUser_extend()).getUser_nickname());
            }

            @Override
            public void onUserNumChanged(Num num) {
                // TODO Auto-generated method stub
                userTitleText.setText(getString(R.string.user_title,
                        num.getValue()));
                addLog("房间人数变成" + num.getValue());
            }

            @Override
            public void onUserOut(User user) {
                // TODO Auto-generated method stub
                userAdapter.setmList(BukaSDKManager.getUserManager()
                        .getUserArr());
                userAdapter.notifyDataSetChanged();
                addLog("用户" + new UserBean(user.getUser_extend()).getUser_nickname()
                        + "退出房间");
            }

            @Override
            public void onUserDisconnect(User user) {
                // TODO Auto-generated method stub
                userAdapter.setmList(BukaSDKManager.getUserManager()
                        .getUserArr());
                userAdapter.notifyDataSetChanged();
                addLog("用户" + new UserBean(user.getUser_extend()).getUser_nickname()
                        + "意外断线，暂离房间");
            }

            @Override
            public void onSelfDisconnect() {
                // TODO Auto-generated method stub
                addLog("您已断线", "#ff0000");

            }

            @Override
            public void onSelfConnect(Room room) {
                addLog("您已重连成功", "#ff0000");

                List<Status> statusList = BukaSDKManager.getStatusManager().getStatusArr("stream");
                if (statusList != null && statusList.size() > 0) {
                    for (int i = 0; i < statusList.size(); i++) {
                        if (isflag) {
                            play(statusList.get(i));
                        } else {
                            play2(statusList.get(i));
                        }
                        isflag = !isflag;
                    }
                }

                stopPublish();
                publish();
                stopPublish2();
                publish2();
            }

        });
        BukaSDKManager.getChatManager().addListener(new ChatListener() {

            @Override
            public void onChatReceive(final Chat chat) {
                // TODO Auto-generated method stub
                BukaSDKManager.getUserManager().getUser(
                        chat.getSend_session_id(), new ReceiptListener<User>() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null) {
                                    if (chat.getReceive_session_id().length() > 0) {
                                        addLog("收到"
                                                + new UserBean(user.getUser_extend())
                                                .getUser_nickname() + "单发給我的消息："
                                                + chat.getMessage());
                                    } else {
                                        addLog("收到"
                                                + new UserBean(user.getUser_extend())
                                                .getUser_nickname() + "群发的消息："
                                                + chat.getMessage());
                                    }
                                } else {
                                    addLog("收到消息，但未找到此人信息");
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });

            }
        });
        BukaSDKManager.getRpcManager().addListener(new RpcListener() {

            @Override
            public void onRpcReceive(final Rpc rpc) {
                // TODO Auto-generated method stub
                BukaSDKManager.getUserManager().getUser(
                        rpc.getSend_session_id(), new ReceiptListener<User>() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null) {
                                    if (rpc.getReceive_session_id().length() > 0) {
                                        addLog("收到"
                                                + new UserBean(user.getUser_extend())
                                                .getUser_nickname() + "单发給我的RPC：" + "type:"
                                                + rpc.getType() + "，延时：" + rpc.getDelay()
                                                + "秒，消息：" + rpc.getMessage());
                                    } else {
                                        addLog("收到"
                                                + new UserBean(user.getUser_extend())
                                                .getUser_nickname() + "群发的RPC：" + "type:"
                                                + rpc.getType() + "，延时：" + rpc.getDelay()
                                                + "秒，消息：" + rpc.getMessage());
                                    }
                                } else {
                                    addLog("收到PRC，但未找到此人信息");
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });

            }
        });
        BukaSDKManager.getStatusManager().addListener(new StatusListener() {

            @Override
            public void onStatusDelete(Status status) {
                // TODO Auto-generated method stub
                addLog("状态删除");
            }

            @Override
            public void onStatusAdd(Status status) {
                // TODO Auto-generated method stub
                addLog("状态添加");


                if (isFirst) {
                    play(status);

                } else {
                    play2(status);
                }
                isFirst = !isFirst;
            }

            @Override
            public void onStatusChanged(Status status, Status status1) {

            }
        });
        BukaSDK.start();
    }

    boolean isFirst = true;
    TextView room_name_tv;
    RelativeLayout room_name_rl;
    RelativeLayout media_layout;
    ImageView exit_iv;
    ImageView message_iv;
    TextView online_tv;//连麦操作

    /**
     * UI BEGIN
     **/
    private void initFrame() {
        setContentView(R.layout.activity_main1);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        online_tv = findViewById(R.id.online_tv);//
        message_iv = findViewById(R.id.message_iv);
        exit_iv = findViewById(R.id.exit_iv);
        media_layout = findViewById(R.id.media_layout);
        room_name_rl = findViewById(R.id.room_name_rl);
        room_name_tv = findViewById(R.id.room_name_tv);
        mStreamLayout = (LinearLayout) findViewById(R.id.stream_layout);
        mStreamLayout2 = (LinearLayout) findViewById(R.id.stream_layout_2);
        mPlayLayout = (LinearLayout) findViewById(R.id.play_layout);//老师推过来的流信息
        mPlayLayout2 = (LinearLayout) findViewById(R.id.play_layout_2);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.chat).setOnClickListener(this);
        findViewById(R.id.rpc).setOnClickListener(this);
        findViewById(R.id.update).setOnClickListener(this);

        switchCamera = (TextView) findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this);
        findViewById(R.id.publish).setOnClickListener(this);
        findViewById(R.id.publish_2).setOnClickListener(this);
        findViewById(R.id.publish_stop).setOnClickListener(this);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.play_stop).setOnClickListener(this);
        userTitleText = (TextView) findViewById(R.id.user_title);
        serverText = (TextView) findViewById(R.id.server);
        userAdapter = new UserAdapter(this);
        ListView userListView = (ListView) findViewById(R.id.user_list);
        userListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                chat(userAdapter.getmList().get(arg2).getSession_id());
            }
        });
        userListView.setAdapter(userAdapter);

        contentAdapter = new ContentAdapter(this);
        ListView contentListView = (ListView) findViewById(R.id.content_list);
        contentListView.setAdapter(contentAdapter);

        ipSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_spinner_item);
        ipSpinnerAdapter
                .setDropDownViewResource(R.layout.simple_spinner_textview);
        room_name_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s="";
                //right_is_open = !right_is_open;
                //media_layout.setVisibility(right_is_open ? View.VISIBLE : View.GONE);
            }
        });
        online_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (("上麦").equals(online_tv.getText())) {
                    online_tv.setText("下麦");
                } else {
                    online_tv.setText("下麦");
                }
            }
        });
        exit_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                finish();
            }
        });
        message_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chat(null);
            }
        });
    }

    boolean right_is_open = true;//true--右侧打开

    private void initAve() {
        bkrtc_impl.GetInstance().register(this);
        bkrtc_impl.GetInstance().AveCreate(false, true, 3);
        bkrtc_impl.GetInstance().AveSetUserId("2356");
    }

    private void refreshFrame() {
        //logins();

    }

    /**
     * UI END
     **/

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.login) {
            login();
        }
        if (arg0.getId() == R.id.logout) {
            logout();
        }
        if (arg0.getId() == R.id.update) {
            nickname();
        }
        if (arg0.getId() == R.id.chat) {
            chat(null);
        }
        if (arg0.getId() == R.id.rpc) {
            rpc(null);
        }
        if (arg0.getId() == R.id.publish) {
            publish();
            publish2();
        }
        if (arg0.getId() == R.id.publish_2) {
            publish2();
        }
        if (arg0.getId() == R.id.publish_stop) {
            stopPublish();
            stopPublish2();
        }
        if (arg0.getId() == R.id.play) {
//            play();
//            play2();
        }
        if (arg0.getId() == R.id.play_stop) {
            stopPlay();
            stopPlay2();
        }
        if (arg0.getId() == R.id.switch_camera) {
            if (mPublishStreamStatus != null) {
                switchCamera(mPublishStreamStatus.getAid(), mPublishStreamStatus.getVid());
            }
        }
    }

    private final int FLAG_MAINACTIVITY_DISMISS = 5;
    private final int FLAG_MAINACTIVITY_SHOW = 6;
    private final int FLAG_AVE_STREAM_BAND_WIDTH = 0x1000;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLAG_MAINACTIVITY_DISMISS) {
                dismiss();
                return;
            }
            if (msg.what == FLAG_MAINACTIVITY_SHOW) {
                show();
                return;
            }
            if (msg.what == FLAG_AVE_STREAM_BAND_WIDTH) {
                addLog("BandWidth = " + StreamBandwidth.GetInstance().GetSendBandwidth());
                return;
            }
        }
    };

    private void logout() {
        show();
        isFirst = true;
        isflag = true;
        BukaSDKManager.getMediaManager().onDestroy();
        clearStream();
        BukaSDKManager.getUserManager().logout(new ReceiptListener() {
            @Override
            public void onSuccess(Object o) {
                dismiss();
                refreshFrame();
                clearLog();
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub
                dismiss();
            }
        });
    }

    private void clearStream() {
        mStreamLayout.removeAllViews();
        mStreamLayout.setVisibility(View.GONE);
        mStreamLayout2.removeAllViews();
        mStreamLayout2.setVisibility(View.GONE);
        mPlayLayout.removeAllViews();
        mPlayLayout.setVisibility(View.GONE);
        mPlayLayout2.removeAllViews();
        mPlayLayout2.setVisibility(View.GONE);
    }

    private void login() {


        final EditText edit = new EditText(this);
        edit.setGravity(Gravity.CENTER);
        final LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.addView(edit, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        edit.setHint(getString(R.string.login_alert_hint));
        edit.setText(getStringForKey(KEY_ROOM, "test"));

        final Spinner ipSpinner = new Spinner(this);
        l.addView(ipSpinner, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ipSpinner.setAdapter(ipSpinnerAdapter);
        ipSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                setString(KEY_IP, ipSpinnerAdapter.getItem(arg2));
                BukaSDKManager.getConnectManager().changeServer(
                        ipSpinnerAdapter.getItem(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        String defaultIp = getStringForKey(KEY_IP, "");
        int index = ipSpinnerAdapter.getPosition(defaultIp);
        if (index == -1) {
            ipSpinner.setSelection(0);
        } else {
            ipSpinner.setSelection(index);
        }

        AlertDialog alerdialog = new AlertDialog.Builder(DemooActivity.this)
                .setTitle(getString(R.string.login_alert_title))
                .setPositiveButton(getString(R.string.login_alert_go),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if (edit.getText().toString().length() == 0) {
                                    alert(getString(R.string.login_alert_hint));
                                    return;
                                }

                            }
                        }).create();

        alerdialog.setView(l);
        alerdialog.show();
    }

    //账号登录操作
    void logins() {
        show();
        String room = "test";
        setString(KEY_ROOM, room);
        UserBean userBean = new UserBean();
        userBean.setUser_nickname(getStringForKey(
                KEY_NICKNAME,
                GB_SecurityUtils.getRandomNumber(10)));
        BukaSDKManager.getUserManager().login(
                room,
                GB_JsonUtils.toJson(userBean),
                new ReceiptListener() {

                    @Override
                    public void onSuccess(Object o) {
                        // TODO Auto-generated method
                        // stub
                        refreshFrame();
                        userTitleText
                                .setText(getString(
                                        R.string.user_title,
                                        BukaSDKManager
                                                .getUserManager()
                                                .getUserArr()
                                                .size()));
                        addLog("当前房间人数"
                                + BukaSDKManager
                                .getUserManager()
                                .getUserArr()
                                .size());
                        dismiss();

                        List<Status> statusList = BukaSDKManager.getStatusManager().getStatusArr("stream");
                        if (statusList != null && statusList.size() > 0) {
                            for (int i = 0; i < statusList.size(); i++) {
                                if (isflag) {
                                    play(statusList.get(i));
                                } else {
                                    play2(statusList.get(i));
                                }
                                isflag = !isflag;
                            }
                        }

                        initTest();
                        if (BukaSDKManager.getUserManager().isLogin()) {
                            findViewById(R.id.login).setVisibility(View.GONE);
                            //findViewById(R.id.tool_layout).setVisibility(View.VISIBLE);
                            //findViewById(R.id.stream_tool).setVisibility(View.VISIBLE);
                            findViewById(R.id.media_layout).setVisibility(View.VISIBLE);
                            findViewById(R.id.content_list).setVisibility(View.VISIBLE);
                            findViewById(R.id.bg).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.login).setVisibility(View.VISIBLE);
                            findViewById(R.id.tool_layout).setVisibility(View.GONE);
                            findViewById(R.id.stream_tool).setVisibility(View.GONE);
                            findViewById(R.id.media_layout).setVisibility(View.GONE);
                            findViewById(R.id.content_list).setVisibility(View.GONE);
                            findViewById(R.id.bg).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method
                        // stub
                        dismiss();
                        alert(getString(R.string.alert_exe_error));
                    }
                });
    }

    boolean isflag = true;

    private void nickname() {
        final EditText edit = new EditText(this);
        edit.setGravity(Gravity.CENTER);
        final FrameLayout fl = new FrameLayout(this);
        fl.addView(edit, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        edit.setHint(getString(R.string.login_alert_hint));
        if (BukaSDKManager.getUserManager().isLogin()) {
            BukaSDKManager.getUserManager()
                    .getSelfUser(new ReceiptListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            UserBean userBean = new UserBean(user.getUser_extend());
                            edit.setText(userBean.getUser_nickname());
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }

        AlertDialog alerdialog = new AlertDialog.Builder(DemooActivity.this)
                .setTitle(getString(R.string.nickname_alert_title))
                .setPositiveButton(getString(R.string.nickname_alert_update),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if (edit.getText().toString().length() == 0) {
                                    alert(getString(R.string.nickname_alert_update));
                                    return;
                                }
                                show();
                                UserBean userBean = new UserBean();
                                userBean.setUser_nickname(edit.getText().toString());
                                setString(KEY_NICKNAME, edit.getText()
                                        .toString());
                                BukaSDKManager.getUserManager().update(
                                        GB_JsonUtils.toJson(userBean),
                                        new ReceiptListener() {

                                            @Override
                                            public void onSuccess(Object o) {
                                                // TODO Auto-generated method
                                                // stub
                                                dismiss();
                                            }

                                            @Override
                                            public void onError() {
                                                // TODO Auto-generated method
                                                // stub
                                                dismiss();
                                                alert(getString(R.string.alert_exe_error));
                                            }
                                        });
                            }
                        }).create();
        alerdialog.setView(fl);
        alerdialog.show();
    }

    private void chat(final String sessionId) {
        final EditText edit = new EditText(this);
        edit.setGravity(Gravity.CENTER);
        final FrameLayout fl = new FrameLayout(this);
        fl.addView(edit, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        edit.setHint(getString(R.string.chat_alert_hint));
        edit.setText("测试消息：" + GB_SecurityUtils.getRandomNumber(10));

        AlertDialog alerdialog = new AlertDialog.Builder(DemooActivity.this)
                .setTitle(
                        getString(sessionId == null ? R.string.chat_alert_title
                                : R.string.chat_session_alert_title))
                .setPositiveButton(getString(R.string.alert_send),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if (edit.getText().toString().length() == 0) {
                                    alert(getString(R.string.chat_alert_hint));
                                    return;
                                }
                                show();
                                ReceiptListener listener = new ReceiptListener() {

                                    @Override
                                    public void onSuccess(Object o) {
                                        // TODO Auto-generated
                                        // method
                                        // stub
                                        dismiss();
                                    }

                                    @Override
                                    public void onError() {
                                        // TODO Auto-generated
                                        // method
                                        // stub
                                        dismiss();
                                        alert(getString(R.string.alert_exe_error));
                                    }
                                };
                                if (sessionId == null) {
                                    BukaSDKManager.getChatManager()
                                            .sendBroadcastChat(
                                                    edit.getText().toString(),
                                                    listener);
                                } else {
                                    BukaSDKManager.getChatManager()
                                            .sendUnicastChat(
                                                    edit.getText().toString(),
                                                    sessionId, listener);
                                }
                            }
                        }).create();
        alerdialog.setView(fl);
        alerdialog.show();
    }

    private void rpc(final String sessionId) {
        final LinearLayout fl = new LinearLayout(this);
        final LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);

        // final TextView delay = new TextView(this);
        // delay.setText("延时：");
        // l.addView(delay, new LinearLayout.LayoutParams(
        // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        final Spinner delaySpinner = new Spinner(this);
        l.addView(delaySpinner, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        fl.addView(l, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        ArrayAdapter<String> delaySpinnerAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.simple_spinner_item);

        String delayItems[] = new String[5];
        final int delayItemsValue[] = new int[delayItems.length];
        for (int i = 0; i < delayItems.length; i++) {
            delayItemsValue[i] = i;
            delayItems[i] = "延时：" + delayItemsValue[i] + "秒";
        }
        for (int i = 0; i < delayItems.length; i++) {
            delaySpinnerAdapter.add(delayItems[i]);
        }
        delaySpinnerAdapter
                .setDropDownViewResource(R.layout.simple_spinner_textview);
        delaySpinner.setAdapter(delaySpinnerAdapter);
        delaySpinner.setSelection(0);

        final Spinner typeSpinner = new Spinner(this);
        l.addView(typeSpinner, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.simple_spinner_item);

        String typeItems[] = new String[5];
        final long typeItemsValue[] = new long[typeItems.length];
        for (int i = 0; i < typeItems.length; i++) {
            typeItemsValue[i] = Long.valueOf(GB_SecurityUtils
                    .getRandomNumber(3));
            typeItems[i] = "自定义类型：" + typeItemsValue[i];
        }
        for (int i = 0; i < typeItems.length; i++) {
            typeSpinnerAdapter.add(typeItems[i]);
        }
        typeSpinnerAdapter
                .setDropDownViewResource(R.layout.simple_spinner_textview);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        typeSpinner.setSelection(0);

        final EditText edit = new EditText(this);
        edit.setGravity(Gravity.CENTER);
        fl.setOrientation(LinearLayout.VERTICAL);
        fl.addView(edit, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        edit.setHint(getString(R.string.rpc_alert_hint));
        edit.setText("测试RPC：" + GB_SecurityUtils.getRandomNumber(10));

        AlertDialog alerdialog = new AlertDialog.Builder(DemooActivity.this)
                .setTitle(
                        getString(sessionId == null ? R.string.rpc_alert_title
                                : R.string.rpc_session_alert_title))
                .setPositiveButton(getString(R.string.alert_send),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                if (edit.getText().toString().length() == 0) {
                                    alert(getString(R.string.rpc_alert_hint));
                                    return;
                                }
                                show();
                                ReceiptListener listener = new ReceiptListener() {

                                    @Override
                                    public void onSuccess(Object o) {
                                        // TODO Auto-generated
                                        // method
                                        // stub
                                        dismiss();
                                    }

                                    @Override
                                    public void onError() {
                                        // TODO Auto-generated
                                        // method
                                        // stub
                                        dismiss();
                                        alert(getString(R.string.alert_exe_error));
                                    }
                                };
                                if (sessionId == null) {
                                    BukaSDKManager
                                            .getRpcManager()
                                            .sendBroadcastRpc(
                                                    edit.getText().toString(),
                                                    typeItemsValue[typeSpinner
                                                            .getSelectedItemPosition()],
                                                    delayItemsValue[delaySpinner
                                                            .getSelectedItemPosition()],
                                                    listener);
                                } else {
                                    BukaSDKManager.getRpcManager()
                                            .sendUnicastRpc(
                                                    edit.getText().toString(),
                                                    1, 0, sessionId, listener);
                                }
                            }
                        }).create();
        alerdialog.setView(fl);
        alerdialog.show();
    }

    private void switchCamera(long aid, long vid) {
        mCameraId = mCameraId == 0 ? 1 : 0;
        BukaSDKManager.getMediaManager().switchCamera(
                DemooActivity.this, aid, vid, mCameraId, mCameraId,
                320, 180, 10, mPublishStreamStatus.getSvr());

//        setVolume(mCameraId == 0 ? 10 : 0);
    }

    private void setVolume(int volume) {
        BukaSDKManager.getMediaManager().setMicVolume(volume);
        BukaSDKManager.getMediaManager().setSpeakerVolume(volume);
    }

    private void publish() {
        JSONObject object = new JSONObject();

        try {
            object.put("id", mDeviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BukaSDKManager.getMediaManager().startPulish(0, true, true, object.toString(), new ReceiptListener<Stream>() {
            @Override
            public void onSuccess(Stream status) {
                addLog("推流1成功");
                Log.e("publish", "status0 aid=" + status.getAid() + "; vid=" + status.getVid());
                mStreamLayout.removeAllViews();
                mStreamLayout.setVisibility(View.VISIBLE);
                mStreamLayout.addView(status.getSvr());
                mPublishStreamStatus = status;
                switchCamera.setEnabled(true);
            }

            @Override
            public void onError() {
                addLog("推流失败");
            }
        });
    }

    private void publish2() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", mDeviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BukaSDKManager.getMediaManager().startPulish(1, true, true, object.toString(), new ReceiptListener<Stream>() {
            @Override
            public void onSuccess(Stream status) {
                addLog("推流2成功");
                Log.e("publish", "status1 aid=" + status.getAid() + "; vid=" + status.getVid());
                mStreamLayout2.removeAllViews();
                mStreamLayout2.setVisibility(View.VISIBLE);
                mStreamLayout2.addView(status.getSvr());
                mPublishStreamStatus2 = status;
                switchCamera.setEnabled(true);
            }

            @Override
            public void onError() {
                addLog("推流失败");
            }
        });
    }

    private void stopPublish() {
        BukaSDKManager.getMediaManager().stopPulish(mPublishStreamStatus.getAid(), mPublishStreamStatus.getVid(), new ReceiptListener<SurfaceViewRenderer>() {
            @Override
            public void onSuccess(SurfaceViewRenderer renderer) {
                addLog("停止推流");
                mStreamLayout.removeAllViews();
            }

            @Override
            public void onError() {
                addLog("停止推流失败");
            }
        });
    }

    private void stopPublish2() {
        BukaSDKManager.getMediaManager().stopPulish(mPublishStreamStatus2.getAid(), mPublishStreamStatus2.getVid(), new ReceiptListener<SurfaceViewRenderer>() {
            @Override
            public void onSuccess(SurfaceViewRenderer renderer) {
                addLog("停止2推流");
                mStreamLayout2.removeAllViews();
            }

            @Override
            public void onError() {
                addLog("停止推流失败");
            }
        });
    }

    private final String KEY = "BukaSDK_Media_Info";
    private long playAid = 0;
    private long playVid = 0;

    private void play(Status status) {
//        List<Status> statusList = BukaSDKManager.getStatusManager().getStatusArr("stream");
//        if (statusList != null && statusList.size() > 0) {
//            Status status = statusList.get(0);
        try {
            JSONObject extend = new JSONObject(status.getStatus_extend());
            if (mDeviceId.equals(extend.getString("id"))) {
                return;
            }
            JSONObject stream = new JSONObject(extend.getString(KEY));
            final long aid = stream.getLong("aid");
            final long vid = stream.getLong("vid");
            BukaSDKManager.getMediaManager().startPlay(aid, vid, new ReceiptListener<SurfaceViewRenderer>() {
                @Override
                public void onSuccess(SurfaceViewRenderer renderer) {
                    mPlayLayout.removeAllViews();
                    addLog("拉流1成功");
                    mPlayLayout.setVisibility(View.VISIBLE);
                    mPlayLayout.addView(renderer);
                    playAid = aid;
                    playVid = vid;
                }

                @Override
                public void onError() {
                    addLog("拉流失败");
                    mPlayLayout.removeAllViews();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        }
    }

    private long play2Aid = 0;
    private long play2Vid = 0;

    private void play2(Status status) {
//        List<Status> statusList = BukaSDKManager.getStatusManager().getStatusArr("stream");
//        if (statusList != null && statusList.size() > 1) {
//            Status status = statusList.get(1);
        try {
            JSONObject extend = new JSONObject(status.getStatus_extend());
            if (mDeviceId.equals(extend.getString("id"))) {
                return;
            }
            JSONObject stream = new JSONObject(extend.getString(KEY));
            final long aid = stream.getLong("aid");
            final long vid = stream.getLong("vid");
            BukaSDKManager.getMediaManager().startPlay(aid, vid, new ReceiptListener<SurfaceViewRenderer>() {
                @Override
                public void onSuccess(SurfaceViewRenderer renderer) {
                    mPlayLayout2.removeAllViews();
                    addLog("拉流2成功");
                    mPlayLayout2.setVisibility(View.VISIBLE);
                    mPlayLayout2.addView(renderer);
                    play2Aid = aid;
                    play2Vid = vid;
                }

                @Override
                public void onError() {
                    addLog("拉流失败");
                    mPlayLayout.removeAllViews();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        }
    }

    private void stopPlay() {
        BukaSDKManager.getMediaManager().stopPlay(playAid, playVid, new ReceiptListener<SurfaceViewRenderer>() {
            @Override
            public void onSuccess(SurfaceViewRenderer renderer) {
                addLog("停止拉流");
                mPlayLayout.removeAllViews();
                playAid = 0;
                playVid = 0;
            }

            @Override
            public void onError() {
                addLog("停止拉流失败");
            }
        });

//        List<Status> statusList = BukaSDKManager.getStatusManager().getStatusArr("stream");
//        if (statusList != null && statusList.size() > 0) {
//            Status status = statusList.get(0);
//            try {
//                JSONObject extend = new JSONObject(status.getStatus_extend());
//
//                JSONObject stream = new JSONObject(extend.getString(KEY));
//                long aid = stream.getLong("aid");
//                long vid = stream.getLong("vid");
//                BukaSDKManager.getMediaManager().stopPlay(aid, vid, new ReceiptListener<SurfaceViewRenderer>() {
//                    @Override
//                    public void onSuccess(SurfaceViewRenderer renderer) {
//                        addLog("停止拉流");
//                        mPlayLayout.removeAllViews();
//                    }
//
//                    @Override
//                    public void onError() {
//                        addLog("停止拉流失败");
//                    }
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private void stopPlay2() {
        if (BukaSDKManager.getMediaManager().isPlay(play2Aid, play2Vid)) {
            BukaSDKManager.getMediaManager().stopPlay(play2Aid, play2Vid, new ReceiptListener<SurfaceViewRenderer>() {
                @Override
                public void onSuccess(SurfaceViewRenderer renderer) {
                    addLog("停止拉流2");
                    mPlayLayout2.removeAllViews();
                    play2Aid = 0;
                    play2Vid = 0;
                }

                @Override
                public void onError() {
                    addLog("停止拉流2失败");
                }
            });
        }
    }

    private ProgressDialog progressDialog;

    private synchronized void show() {
        if (progressDialog != null)
            dismiss();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.progress_title);
        progressDialog.setMessage(getString(R.string.progress_message));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private synchronized void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private Toast mToast = null;

    public void alert(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(msg);
        }
        mToast.show();
    }

    private void setString(String key, String str) {
        SharedPreferences sPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        sPrefs.edit().putString(key, str).commit();
    }

    private String getStringForKey(String key) {
        return getStringForKey(key, "");
    }

    private String getStringForKey(String key, String value) {
        return PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()).getString(key, value);
    }

}
