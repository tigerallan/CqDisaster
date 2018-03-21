package com.nandi.cqdisaster.examine.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.constant.ConnectUrl;
import com.nandi.cqdisaster.examine.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class EditMessageActivity extends AppCompatActivity {
    private static final String TAG = "EditMessageActivity";
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private String taskId;
    private ProgressDialog progressDialog;
    private final static String SMS_SEND_ACTION = "SMS_SEND";
    private final static String SMS_DELIVERED_ACTION = "SMS_DELIVERED";
    private SmsStatusReceiver mSmsStatusReceiver;
    private SmsDeliveryStatusReceiver mSmsDeliveryStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);
        ButterKnife.bind(this);
        mSmsStatusReceiver = new SmsStatusReceiver();
        mSmsDeliveryStatusReceiver = new SmsDeliveryStatusReceiver();
        registerReceiver(mSmsStatusReceiver, new IntentFilter(SMS_SEND_ACTION));
        registerReceiver(mSmsDeliveryStatusReceiver, new IntentFilter(SMS_DELIVERED_ACTION));
        taskId = getIntent().getStringExtra("TASK_ID");
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsStatusReceiver);
        unregisterReceiver(mSmsDeliveryStatusReceiver);
    }

    private void initData() {
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在加载...");
        progressDialog.show();
        OkHttpUtils.get().url(new ConnectUrl().getMyCompleteTaskInfoUrl())
                .addParams("id", taskId)
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
                            String info = object.getString("result");
                            etMessage.setText(info);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @OnClick({R.id.iv_back, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_send:
                String message = etMessage.getText().toString().trim();
                String number = etNumber.getText().toString().trim();
                if (number.isEmpty()) {
                    ToastUtils.showShortToast("请输入正确的电话号码！");
                    return;
                }
                String[] split = number.split("/");
                Log.d(TAG, "分割：" + Arrays.toString(split));
                SmsManager manager = SmsManager.getDefault();
                ArrayList<String> list = manager.divideMessage(message);  //因为一条短信有字数限制，因此要将长短信拆分
                ArrayList<PendingIntent> sendIntentList = new ArrayList<>();
                ArrayList<PendingIntent> deliveryIntentList = new ArrayList<>();
                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SEND_ACTION), 0);
                PendingIntent deliveryIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED_ACTION), 0);
                for (int i = 1; i <= list.size(); i++) {
                    if (i < list.size()) {
                        sendIntentList.add(null);
                        deliveryIntentList.add(null);
                    } else {
                        sendIntentList.add(sentIntent);
                        deliveryIntentList.add(deliveryIntent);
                    }
                }
                for (String s : split) {
                    manager.sendMultipartTextMessage(s, null, list, sendIntentList, deliveryIntentList);
                }
                btnSend.setEnabled(false);
                btnSend.setText("正在发送");
                break;
        }
    }


    public class SmsStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    ToastUtils.showShortToast("短信发送成功！");
                    btnSend.setEnabled(true);
                    btnSend.setText("发送");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    ToastUtils.showShortToast("短信发送失败！");
                    btnSend.setEnabled(true);
                    btnSend.setText("发送");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    ToastUtils.showShortToast("短信发送失败！");
                    btnSend.setEnabled(true);
                    btnSend.setText("发送");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    ToastUtils.showShortToast("短信发送失败！");
                    btnSend.setEnabled(true);
                    btnSend.setText("发送");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    ToastUtils.showShortToast("短信发送失败！");
                    btnSend.setEnabled(true);
                    btnSend.setText("发送");
                    break;
            }
        }
    }

    public class SmsDeliveryStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "SmsDeliveryStatusReceiver onReceive.");
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    ToastUtils.showShortToast("短信已接收");
                    break;
                case Activity.RESULT_CANCELED:
                    ToastUtils.showShortToast("短信已接收");
                    break;
            }
        }
    }
}
