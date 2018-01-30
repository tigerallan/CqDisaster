package com.nandi.cqdisaster.securitydao.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.adapter.MessageAdapter;
import com.nandi.cqdisaster.securitydao.bean.LocationPoint;
import com.nandi.cqdisaster.securitydao.fragment.BasicInformationFragment;
import com.nandi.cqdisaster.securitydao.fragment.PersonnelInformationFragment;
import com.nandi.cqdisaster.securitydao.fragment.PictureFragment;
import com.nandi.cqdisaster.securitydao.fragment.VideoFragment;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_title;
    private ArrayList<Fragment> list;
    private ViewPager vpMessage;
    private TabLayout tabMessage;
    private LocationPoint baseMessage;
    private BasicInformationFragment basicInformationFragment;
    private VideoFragment videoFragment;
    private PictureFragment pictureFragment;
    private PersonnelInformationFragment personnelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        initFragment();

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        vpMessage = (ViewPager) findViewById(R.id.vp_message);
        tabMessage = (TabLayout) findViewById(R.id.tab_message);
        baseMessage = (LocationPoint) getIntent().getSerializableExtra("baseMessage");
        System.out.println("baseMessage = " + baseMessage.toString());
        tv_title.setText(baseMessage.getDis_name());
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragment() {
        basicInformationFragment = BasicInformationFragment.newInstance(baseMessage);
        videoFragment = VideoFragment.newInstance(baseMessage.getVideo_name());
        pictureFragment = PictureFragment.newInstance(baseMessage.getDis_no());
        personnelFragment = PersonnelInformationFragment.newInstance(baseMessage.getDis_no());
        list = new ArrayList<>();
        list.add(basicInformationFragment);
        list.add(pictureFragment);
        list.add(videoFragment);
        list.add(personnelFragment);
        vpMessage.setOffscreenPageLimit(4);
        MessageAdapter messageAdapter = new MessageAdapter(getSupportFragmentManager(), list);
        vpMessage.setAdapter(messageAdapter);
        tabMessage.setupWithViewPager(vpMessage);
    }
}
