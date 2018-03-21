package com.nandi.cqdisaster.examine.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.activity.CompleteTaskActivity;
import com.nandi.cqdisaster.examine.activity.CreateTaskActivity;
import com.nandi.cqdisaster.examine.activity.MyTaskActivity;
import com.nandi.cqdisaster.examine.activity.SendMessageActivity;
import com.nandi.cqdisaster.examine.constant.ConnectUrl;
import com.nandi.cqdisaster.examine.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by lemon on 2017/2/22.
 */

public class WorkbenchFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.ll_create_task)
    LinearLayout llCreateTask;
    @BindView(R.id.ll_send_message)
    LinearLayout llSendMessage;
    @BindView(R.id.iv_my_task_badge)
    ImageView ivMyTaskBadge;
    @BindView(R.id.ll_my_task)
    RelativeLayout llMyTask;
    @BindView(R.id.iv_complete_task_badge)
    ImageView ivCompleteTaskBadge;
    @BindView(R.id.ll_complete_task)
    RelativeLayout llCompleteTask;
    private Activity activity;
    private Intent intent;
    private String myTask = "0";
    private String allTask = "0";
    private SharedPreferences sp;
    private String sessionId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_workbench, container, false);
        unbinder = ButterKnife.bind(this, view);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        setData();
        return view;
    }


    private void setData() {
        Log.d("WorkbenchFragment", "sessionId:" + sessionId);
        OkHttpUtils.get().url(new ConnectUrl().getTaskNumUrl())
                .addParams("sessionId", sessionId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String status, msg;
                        try {
                            JSONObject object = new JSONObject(response);
                            status = object.getString("status");
                            msg = object.getString("message");
                            Log.d("WorkbenchFragment", "获取任务数量的status:" + status);
                            Log.d("WorkbenchFragment", "获取任务数量的msg:" + status);
                            if ("200".equals(status)) {
                                JSONArray array = object.getJSONArray("message");
                                myTask = array.getJSONObject(0).getString("myTask");
                                allTask = array.getJSONObject(1).getString("allTask");
                                Log.d("WorkbenchFragment", "我的任务数量：" + myTask);
                                Log.d("WorkbenchFragment", "完成任务数量：" + allTask);
                                BadgeFactory.create(activity)
                                        .setTextColor(Color.WHITE)
                                        .setWidthAndHeight(30, 30)
                                        .setBadgeBackground(Color.RED)
                                        .setTextSize(15)
                                        .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                                        .setBadgeCount(myTask)
                                        .setShape(BadgeView.SHAPE_CIRCLE)
                                        .setSpace(10, 10)
                                        .bind(ivMyTaskBadge);
                                BadgeFactory.create(activity)
                                        .setTextColor(Color.WHITE)
                                        .setWidthAndHeight(30, 30)
                                        .setBadgeBackground(Color.RED)
                                        .setTextSize(15)
                                        .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                                        .setBadgeCount(allTask)
                                        .setShape(BadgeView.SHAPE_CIRCLE)
                                        .setSpace(10, 10)
                                        .bind(ivCompleteTaskBadge);
                            } else if ("400".equals(status)) {
                                ToastUtils.showShortToast(msg);
                                sp.edit().putBoolean("isLogin", false).apply();
//                                startActivity(new Intent(getActivity(), LoginActivity.class));
//                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_my_task, R.id.ll_complete_task, R.id.ll_create_task, R.id.ll_send_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_task:
                intent = new Intent(activity, MyTaskActivity.class);//我的任务
                startActivity(intent);
                break;
            case R.id.ll_complete_task:
                intent = new Intent(activity, CompleteTaskActivity.class);//完成任务
                startActivity(intent);
                break;
            case R.id.ll_create_task:
                intent = new Intent(activity, CreateTaskActivity.class);//发起任务
                startActivity(intent);
                break;
            case R.id.ll_send_message:
                intent = new Intent(activity, SendMessageActivity.class);//发短信
                startActivity(intent);
                break;
        }
    }

}
