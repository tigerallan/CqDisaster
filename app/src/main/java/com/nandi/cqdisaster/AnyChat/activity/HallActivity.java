package com.nandi.cqdisaster.AnyChat.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatUserInfoEvent;
import com.bairuitech.anychat.AnyChatVideoCallEvent;
import com.nandi.cqdisaster.AnyChat.bussinesscenter.BussinessCenter;
import com.nandi.cqdisaster.AnyChat.bussinesscenter.SessionItem;
import com.nandi.cqdisaster.AnyChat.bussinesscenter.UserItem;
import com.nandi.cqdisaster.AnyChat.util.BaseConst;
import com.nandi.cqdisaster.AnyChat.util.BaseMethod;
import com.nandi.cqdisaster.AnyChat.util.ConfigEntity;
import com.nandi.cqdisaster.AnyChat.util.ConfigService;
import com.nandi.cqdisaster.AnyChat.util.DialogFactory;
import com.nandi.cqdisaster.R;

import java.util.ArrayList;

public class HallActivity extends AppCompatActivity implements OnItemClickListener, AnyChatBaseEvent, AnyChatVideoCallEvent,
        AnyChatUserInfoEvent {
    private AnyChatCoreSDK anychat;
    private GridView mGridUsers;
    private Dialog dialog;
    private UserAdapter mUserAdapter;
    private SearchView mSearchView;
    private ArrayList<UserItem> userItems = new ArrayList<>();
    private ArrayList<UserItem> searchUserItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initSdk();
        ApplyVideoConfig();
        BussinessCenter.getBussinessCenter().getOnlineFriendDatas();
        userItems.addAll(BussinessCenter.mOnlineFriendItems);
        setContentView(R.layout.hall_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("在线人员列表");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_blue));
        setSupportActionBar(toolbar);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(true);
        //设置输入框提示语
        mSearchView.setQueryHint("输入姓名查找...");
        setListener();
        return super.onCreateOptionsMenu(menu);
    }

    private void setListener() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    return false;
                }
                searchUserItems.clear();
                for (UserItem userItem : userItems) {
                    if (userItem.getUserName().equals(query)) {
                        searchUserItems.add(userItem);
                    }
                }
                userItems.clear();
                userItems.addAll(searchUserItems);
                mUserAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    userItems.clear();
                    userItems.addAll(BussinessCenter.mOnlineFriendItems);
                    mUserAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
//        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                userItems.clear();
//                userItems=BussinessCenter.mOnlineFriendItems;
//                mUserAdapter.notifyDataSetChanged();
//                return false;
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            Intent intent = new Intent();
            intent.setClass(this, VideoConfigActivity.class);
            this.startActivityForResult(intent, 1000);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /*
             * if (dialog != null && dialog.isShowing()) dialog.dismiss();
			 */
        }

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();


    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        BussinessCenter.mContext = HallActivity.this;
        initSdk();
        if (mUserAdapter != null)
            mUserAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (anychat != null) {
            anychat.Logout();
            anychat.removeEvent(this);
            anychat.Release();
        }
        BussinessCenter.getBussinessCenter().realseData();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            ApplyVideoConfig();
        }
    }

    // 根据配置文件配置视频参数
    private void ApplyVideoConfig() {
        ConfigEntity configEntity = ConfigService.LoadConfig(this);
        if (configEntity.configMode == 1)        // 自定义视频参数配置
        {
            // 设置本地视频编码的码率（如果码率为0，则表示使用质量优先模式）
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_BITRATECTRL, configEntity.videoBitrate);
            if (configEntity.videoBitrate == 0) {
                // 设置本地视频编码的质量
                AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_QUALITYCTRL, configEntity.videoQuality);
            }
            // 设置本地视频编码的帧率
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_FPSCTRL, configEntity.videoFps);
            // 设置本地视频编码的关键帧间隔
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_GOPCTRL, configEntity.videoFps * 4);
            // 设置本地视频采集分辨率
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_WIDTHCTRL, configEntity.resolution_width);
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_HEIGHTCTRL, configEntity.resolution_height);
            // 设置视频编码预设参数（值越大，编码质量越高，占用CPU资源也会越高）
            AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_PRESETCTRL, configEntity.videoPreset);
        }
        // 让视频参数生效
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_APPLYPARAM, configEntity.configMode);
        // P2P设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_NETWORK_P2PPOLITIC, configEntity.enableP2P);
        // 本地视频Overlay模式设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_OVERLAY, configEntity.videoOverlay);
        // 回音消除设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_AUDIO_ECHOCTRL, configEntity.enableAEC);
        // 平台硬件编码设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_CORESDK_USEHWCODEC, configEntity.useHWCodec);
        // 视频旋转模式设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_ROTATECTRL, configEntity.videorotatemode);
        // 本地视频采集偏色修正设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_FIXCOLORDEVIA, configEntity.fixcolordeviation);
        // 视频GPU渲染设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_GPUDIRECTRENDER, configEntity.videoShowGPURender);
        // 本地视频自动旋转设置
        AnyChatCoreSDK.SetSDKOptionInt(AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION, configEntity.videoAutoRotation);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        UserItem item = userItems.get(arg2);
        if (item != null) {
            if (item.getUserId() == BussinessCenter.selfUserId) {
                BaseMethod.showToast(this.getString(R.string.str_targetwrong),
                        this);
                return;
            }
            BussinessCenter.sessionItem = new SessionItem(0, item.getUserId(),
                    BussinessCenter.selfUserId);
            dialog = DialogFactory.getDialog(DialogFactory.DIALOGID_CALLRESUME,
                    item.getUserId(), this);
            dialog.show();
        }
    }

    private void initView() {
        mGridUsers = (GridView) findViewById(R.id.grid_user);
        mUserAdapter = new UserAdapter(this, userItems);
        mGridUsers.setAdapter(mUserAdapter);
        mGridUsers.setOnItemClickListener(this);

    }

    private void initSdk() {
        if (anychat == null) {
            anychat = AnyChatCoreSDK.getInstance(HallActivity.this);
        }
        anychat.SetBaseEvent(this);
        anychat.SetVideoCallEvent(this);
        anychat.SetUserInfoEvent(this);
        Log.i("ANYCHAT", "initSdk");
    }

//    protected void startBackServce() {
//        Intent intent = new Intent(this, BackService.class);
//        this.startService(intent);
//    }


    @Override
    public void OnAnyChatConnectMessage(boolean bSuccess) {
        // TODO Auto-generated method stub
        if (dialog != null
                && dialog.isShowing()
                && DialogFactory.getCurrentDialogId() == DialogFactory.DIALOGID_RESUME) {
            dialog.dismiss();
        }
    }

    @Override
    public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
        // TODO Auto-generated method stub
        if (dwErrorCode == 0) {
            BussinessCenter.selfUserId = dwUserId;
            BussinessCenter.selfUserName = anychat.GetUserName(dwUserId);
        } else {
            BaseMethod.showToast(this.getString(R.string.str_login_failed) + "(ErrorCode:" + dwErrorCode + ")", this);
        }
    }

    @Override
    public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
        // TODO Auto-generated method stub
        Log.e("HAllActivity", "房间号：" + dwRoomId);
        if (dwErrorCode == 0) {
            Intent intent = new Intent();
            intent.setClass(this, VideoActivity.class);
            this.startActivity(intent);
        } else {
            BaseMethod.showToast(this.getString(R.string.str_enterroom_failed),
                    this);
        }
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
        // TODO Auto-generated method stub

        if (dwErrorCode == 0) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            dialog = DialogFactory.getDialog(DialogFactory.DIALOG_NETCLOSE,
                    DialogFactory.DIALOG_NETCLOSE, this);
            dialog.show();
        } else {
            BaseMethod.showToast(this.getString(R.string.str_serverlink_close),
                    this);
            Intent intent = new Intent();
            intent.putExtra("INTENT", BaseConst.AGAIGN_LOGIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(this, LoginActivity.class);
            this.startActivity(intent);
            this.finish();
        }
        Log.i("ANYCHAT", "OnAnyChatLinkCloseMessage:" + dwErrorCode);
    }

    @Override
    public void OnAnyChatVideoCallEvent(int dwEventType, int dwUserId,
                                        int dwErrorCode, int dwFlags, int dwParam, String userStr) {
        // TODO Auto-generated method stub
        switch (dwEventType) {

            case AnyChatDefine.BRAC_VIDEOCALL_EVENT_REQUEST:
                BussinessCenter.getBussinessCenter().onVideoCallRequest(
                        dwUserId, dwFlags, dwParam, userStr);
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                dialog = DialogFactory.getDialog(DialogFactory.DIALOGID_REQUEST,
                        dwUserId, this);
                dialog.show();
                break;
            case AnyChatDefine.BRAC_VIDEOCALL_EVENT_REPLY:
                BussinessCenter.getBussinessCenter().onVideoCallReply(
                        dwUserId, dwErrorCode, dwFlags, dwParam, userStr);
                if (dwErrorCode == AnyChatDefine.BRAC_ERRORCODE_SUCCESS) {
                    dialog = DialogFactory.getDialog(
                            DialogFactory.DIALOGID_CALLING, dwUserId,
                            HallActivity.this);
                    dialog.show();

                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                break;
            case AnyChatDefine.BRAC_VIDEOCALL_EVENT_START:
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                BussinessCenter.getBussinessCenter().onVideoCallStart(
                        dwUserId, dwFlags, dwParam, userStr);
                break;
            case AnyChatDefine.BRAC_VIDEOCALL_EVENT_FINISH:
                BussinessCenter.getBussinessCenter().onVideoCallEnd(dwUserId,
                        dwFlags, dwParam, userStr);
                break;
        }
    }

    @Override
    public void OnAnyChatUserInfoUpdate(int dwUserId, int dwType) {
        // TODO Auto-generated method stub
        // 同步完成服务器中的所有好友数据，可以在此时获取数据
        if (dwUserId == 0 && dwType == 0) {
            BussinessCenter.getBussinessCenter().getOnlineFriendDatas();
            if (mUserAdapter != null)
                userItems.clear();
                userItems.addAll(BussinessCenter.mOnlineFriendItems);
                mUserAdapter.notifyDataSetChanged();
            Log.i("ANYCHAT", "OnAnyChatUserInfoUpdate");
        }
    }

    @Override
    public void OnAnyChatFriendStatus(int dwUserId, int dwStatus) {
        // TODO Auto-generated method stub
        BussinessCenter.getBussinessCenter().onUserOnlineStatusNotify(dwUserId,
                dwStatus);
        if (mUserAdapter != null)
            userItems.clear();
            userItems.addAll(BussinessCenter.mOnlineFriendItems);
            mUserAdapter.notifyDataSetChanged();
    }

}
