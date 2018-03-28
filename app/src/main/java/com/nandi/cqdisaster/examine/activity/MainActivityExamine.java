package com.nandi.cqdisaster.examine.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.adapter.MyFragmentPagerAdapter;
import com.nandi.cqdisaster.examine.constant.ConnectUrl;
import com.nandi.cqdisaster.examine.receiver.UpdataService;
import com.nandi.cqdisaster.examine.utils.MyUtils;
import com.nandi.cqdisaster.examine.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.Call;

/**
 *
 */
public class MainActivityExamine extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static String TAG = "MainActivityExamine", status, msg;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private Toolbar myToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main_examine);
        ButterKnife.bind(this);
        //动态获取权限
        checkPremission();
        //初始化视图
        initViews();
//        updateManager();
        // setListeners();
    }

    private void checkPremission() {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写SD卡", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读写SD卡", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.SEND_SMS, "短信", R.drawable.permission_ic_sms));
        HiPermission.create(this)
                .title("权限申请")
                .permissions(permissionItems)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))//图标的颜色
                .msg("为了您更好的使用体验，开启这些权限吧！\n一定要确认啰！")
                .style(R.style.PermissionDefaultBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        checkPremission();
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


    }

    private void updateManager() {
        Log.d(TAG, "updateManager:" + MyUtils.getVerCode(this));
        OkHttpUtils.get().url(new ConnectUrl().getUpdateVerCodeUrl())
                .addParams("versionNumber", MyUtils.getVerCode(this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络故障，请检查网路！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "返回的数据：" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            Log.d(TAG, "用户数据：" + status);
                            if ("200".equals(status)) {
                                showNoticeDialog();
                            } else if ("300".equals(status)) {
//                                ToastUtils.showShortToast("暂时没有更新");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showNoticeDialog() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("软件版本更新");
        builder.setMessage(msg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent service = new Intent(MainActivityExamine.this, UpdataService.class);
                startService(service);
//                Intent intent = new Intent(MainActivityExamine.this,SettingsActivity.class);
//                intent.putExtra("settings_type","8");
//                intent.putExtra("settings", "系统信息");
//                startActivity(intent);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }


    private void initViews() {
        tvTitle.setText("应急处置");
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);


        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        //设置Tab的图标，假如不需要则把下面的代码删去
        one.setIcon(R.drawable.selected_tab_image_workbench);
        two.setIcon(R.drawable.selected_tab_image_directory);
        three.setIcon(R.drawable.selected_tab_image_setup);
    }




    public void signOut() {
        new AlertDialog.Builder(this)
                .setTitle("退出程序")
                .setMessage("确定退出程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //initPushOut();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void signOut2() {
        new AlertDialog.Builder(this)
                .setTitle("退出程序")
                .setMessage("确定退出程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        initPushOut();
                        Intent intent = new Intent(MainActivityExamine.this, LoginActivity.class);
                        MainActivityExamine.this.startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


    /**
     * 注销推送绑定
     //     */
//    private void initPushOut() {
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
//        pushService.unbindAccount(new CommonCallback() {
//            @Override
//            public void onSuccess(String s) {
//                Log.e("onSuccess", "" + s);
//            }
//
//            @Override
//            public void onFailed(String s, String s1) {
//                Log.e("onFailed", "" + s + s1);
//            }
//        });
//    }
}
