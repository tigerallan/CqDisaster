package com.nandi.cqdisaster.examine.fragment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.activity.CompleteInfoActivity;
import com.nandi.cqdisaster.examine.bean.TaskInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 查看反馈信息---基本信息页面
 * Created by ChenPeng on 2017/3/14.
 */
public class CompleteBaseInfoFragment extends Fragment {
    @BindView(R.id.tv_base_info_name)
    TextView tvBaseInfoName;
    @BindView(R.id.tv_base_info_address)
    TextView tvBaseInfoAddress;
    @BindView(R.id.tv_base_info_look_person)
    TextView tvBaseInfoLookPerson;
    @BindView(R.id.tv_base_info_happen_time)
    TextView tvBaseInfoHappenTime;
    @BindView(R.id.tv_base_info_look_time)
    TextView tvBaseInfoLookTime;
    @BindView(R.id.tv_base_info_contact)
    TextView tvBaseInfoContact;
    @BindView(R.id.tv_base_info_mobile)
    TextView tvBaseInfoMobile;
    private TaskInfoBean taskInfoBean;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_baseinfo, null);
        bind = ButterKnife.bind(this, view);
        taskInfoBean = ((CompleteInfoActivity) getActivity()).taskInfoBean;
        initView();
        return view;
    }

    private void initView() {
        tvBaseInfoName.setText("null".equals(taskInfoBean.getmDisasterName()) ? "" : taskInfoBean.getmDisasterName());
        tvBaseInfoAddress.setText("null".equals(taskInfoBean.getmDisasterLocation()) ? "" : taskInfoBean.getmDisasterLocation());
        tvBaseInfoLookPerson.setText("null".equals(taskInfoBean.getmName()) ? "" : taskInfoBean.getmName());
        tvBaseInfoHappenTime.setText("null".equals(taskInfoBean.getmHappenTime()) ? "" : taskInfoBean.getmHappenTime());
        tvBaseInfoLookTime.setText("null".equals(taskInfoBean.getmSurveyTime()) ? "" : taskInfoBean.getmSurveyTime());
        tvBaseInfoContact.setText("null".equals(taskInfoBean.getmDisasterContact()) ? "" : taskInfoBean.getmDisasterContact());
        tvBaseInfoMobile.setText("null".equals(taskInfoBean.getmDisasterMobile()) ? "" : taskInfoBean.getmDisasterMobile());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
