package com.nandi.cqdisaster.examine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.adapter.MyCompleteTaskAdapter;
import com.nandi.cqdisaster.examine.bean.MyCompleteTask;
import com.nandi.cqdisaster.examine.constant.ConnectUrl;
import com.nandi.cqdisaster.examine.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class SendMessageActivity extends AppCompatActivity {

    @BindView(R.id.rv_my_task)
    RecyclerView rvMyTask;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private List<MyCompleteTask> taskList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private MyCompleteTaskAdapter adapter;
    private SharedPreferences sp;
    private String sessionId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        userId = sp.getString("userId", "");
        initData();
        setAdapter();
        setListener();
    }

    private void setListener() {
        adapter.setOnItemClickListener(new MyCompleteTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rvMyTask.getChildAdapterPosition(view);
                String id = taskList.get(position).getTaskId();
                Intent intent = new Intent(SendMessageActivity.this, EditMessageActivity.class);
                intent.putExtra("TASK_ID", id);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();
        OkHttpUtils.get().url(new ConnectUrl().getMyCompleteTaskUrl())
                .addParams("sessionId", sessionId)
                .addParams("id", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("网络连接失败！");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            progressDialog.dismiss();
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("results");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject oj = array.getJSONObject(i);
                                    MyCompleteTask task = new MyCompleteTask(oj.getString("text"), oj.getString("id"));
                                    taskList.add(task);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void setAdapter() {
        adapter = new MyCompleteTaskAdapter(this, taskList);
        rvMyTask.setLayoutManager(new LinearLayoutManager(this));
        rvMyTask.setAdapter(adapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
