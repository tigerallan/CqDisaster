package com.nandi.cqdisaster.examine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.adapter.FillInfoPagerAdapter;
import com.nandi.cqdisaster.examine.bean.TaskInfoBean;
import com.nandi.cqdisaster.examine.fragment.CompleteBaseInfoFragment;
import com.nandi.cqdisaster.examine.fragment.CompleteCollectInfoFragment;
import com.nandi.cqdisaster.examine.fragment.CompleteMediaInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompleteInfoActivity extends AppCompatActivity {

    @BindView(R.id.tl_back_info)
    TabLayout tlBackInfo;
    @BindView(R.id.vp_back_info)
    ViewPager vpBackInfo;
    public TaskInfoBean taskInfoBean;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        ButterKnife.bind(this);
        taskInfoBean = (TaskInfoBean) getIntent().getSerializableExtra("TaskBean");
        initView();
    }

    private void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CompleteBaseInfoFragment());
        fragments.add(new CompleteCollectInfoFragment());
        fragments.add(new CompleteMediaInfoFragment());
        vpBackInfo.setAdapter(new FillInfoPagerAdapter(getSupportFragmentManager(), fragments));
        tlBackInfo.setupWithViewPager(vpBackInfo);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
