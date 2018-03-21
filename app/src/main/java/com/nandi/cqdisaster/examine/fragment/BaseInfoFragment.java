package com.nandi.cqdisaster.examine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.nandi.cqdisaster.MyAPP;
import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.activity.FillInfoActivity;
import com.nandi.cqdisaster.examine.bean.TaskInfoBean;
import com.nandi.cqdisaster.examine.database.BaseInfoBean;
import com.nandi.cqdisaster.examine.database.BaseInfoBeanDao;
import com.nandi.cqdisaster.examine.utils.DateTimePickUtil;
import com.nandi.cqdisaster.examine.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 反馈信息填报---基础信息页面
 * Created by ChenPeng on 2017/3/6.
 */
public class BaseInfoFragment extends Fragment {
    private static final String TAG = "BaseInfoFragment";
    @BindView(R.id.et_baseinfo_name)
    EditText etBaseinfoName;
    @BindView(R.id.et_baseinfo_address)
    EditText etBaseinfoAddress;
    @BindView(R.id.et_baseinfo_contact)
    EditText etBaseinfoContact;
    @BindView(R.id.et_baseinfo_mobile)
    EditText etBaseinfoMobile;
    @BindView(R.id.btn_baseinfo_save)
    Button btnBaseinfoSave;
    @BindView(R.id.et_baseinfo_happen_time)
    TextView etBaseinfoHappenTime;
    @BindView(R.id.et_baseinfo_look_time)
    TextView etBaseinfoLookTime;
    @BindView(R.id.et_baseinfo_look_person)
    TextView etBaseinfoLookPerson;
    private TaskInfoBean taskInfoBean;
    private Unbinder bind;
    private String currentTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baseinfo, container, false);
        bind = ButterKnife.bind(this, view);
        taskInfoBean = ((FillInfoActivity) getActivity()).taskInfoBean;
        initView();
        setListener();
        return view;
    }


    private void setListener() {
        btnBaseinfoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBaseInfo();
            }
        });
        etBaseinfoLookTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickUtil(getActivity(), currentTime).dateTimePicKDialog(etBaseinfoLookTime);
            }
        });
        etBaseinfoHappenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickUtil(getActivity(), currentTime).dateTimePicKDialog(etBaseinfoHappenTime);
            }
        });
    }

    private void saveBaseInfo() {
        String name = etBaseinfoName.getText().toString().trim();
        String address = etBaseinfoAddress.getText().toString().trim();
        String contact = etBaseinfoContact.getText().toString().trim();
        String mobile = etBaseinfoMobile.getText().toString().trim();
        String happenTime = etBaseinfoHappenTime.getText().toString().trim();
        String lookTime = etBaseinfoLookTime.getText().toString().trim();
        String lookPerson = etBaseinfoLookPerson.getText().toString().trim();
        BaseInfoBean baseInfoBean = new BaseInfoBean();
        baseInfoBean.setTaskId(taskInfoBean.getmTaskId());
        baseInfoBean.setBaseInfoAddress(address);
        baseInfoBean.setBaseInfoContact(contact);
        baseInfoBean.setBaseInfoMobile(mobile);
        baseInfoBean.setBaseInfoLookPerson(lookPerson);
        baseInfoBean.setBaseInfoName(name);
        baseInfoBean.setBaseInfoHappenTime(happenTime);
        baseInfoBean.setBaseInfoLookTime(lookTime);
        Log.e(TAG, "获取输入框内容：" + baseInfoBean.toString());
        BaseInfoBean infoBean = MyAPP.getDaoSession().getBaseInfoBeanDao().queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (infoBean == null) {
            MyAPP.getDaoSession().getBaseInfoBeanDao().insertOrReplace(baseInfoBean);
            ToastUtils.showShortToast("保存成功");
        } else {
            infoBean.setBaseInfoAddress(address);
            infoBean.setBaseInfoContact(contact);
            infoBean.setBaseInfoMobile(mobile);
            infoBean.setBaseInfoLookPerson(lookPerson);
            infoBean.setBaseInfoName(name);
            infoBean.setBaseInfoHappenTime(happenTime);
            infoBean.setBaseInfoLookTime(lookTime);
            MyAPP.getDaoSession().getBaseInfoBeanDao().update(infoBean);
            ToastUtils.showShortToast("更新成功");

        }
    }

    private void initView() {
        currentTime = new SimpleDateFormat("yyyy年MM月dd日hh:mm").format(new Date());
        BaseInfoBean bean = MyAPP.getDaoSession().getBaseInfoBeanDao().queryBuilder().where(BaseInfoBeanDao.Properties.TaskId.eq(taskInfoBean.getmTaskId())).unique();
        if (bean == null) {
            etBaseinfoName.setText("null".equals(taskInfoBean.getmDisaster())?"":taskInfoBean.getmDisaster());
            etBaseinfoAddress.setText("null".equals(taskInfoBean.getmDisasterLocation())?"":taskInfoBean.getmDisasterLocation());
            etBaseinfoLookPerson.setText(taskInfoBean.getmName());
            etBaseinfoLookTime.setText(taskInfoBean.getmSurveyTime());
            etBaseinfoHappenTime.setText(taskInfoBean.getmHappenTime());
            etBaseinfoContact.setText("null".equals(taskInfoBean.getmDisasterContact())?"":taskInfoBean.getmDisasterContact());
            etBaseinfoMobile.setText("null".equals(taskInfoBean.getmDisasterMobile())?"":taskInfoBean.getmDisasterMobile());
        } else {
            etBaseinfoName.setText(bean.getBaseInfoName());
            etBaseinfoAddress.setText(bean.getBaseInfoAddress());
            etBaseinfoLookPerson.setText(bean.getBaseInfoLookPerson());
            etBaseinfoLookTime.setText(bean.getBaseInfoLookTime());
            etBaseinfoHappenTime.setText(bean.getBaseInfoHappenTime());
            etBaseinfoContact.setText(bean.getBaseInfoContact());
            etBaseinfoMobile.setText(bean.getBaseInfoMobile());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
