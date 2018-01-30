package com.nandi.cqdisaster.securitydao.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.adapter.PersonnelAdapter;
import com.nandi.cqdisaster.securitydao.bean.PersonnelInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 人员信息
 * Created by qingsong on 2018/1/8.
 */

public class PersonnelInformationFragment extends Fragment {
    private RecyclerView dataShow;
    private PersonnelAdapter personnelAdapter;
    private PersonnelInfo personnelInfo;
    private String disasterId;


    public static PersonnelInformationFragment newInstance(String id) {
        PersonnelInformationFragment fragment = new PersonnelInformationFragment();
        Bundle args = new Bundle();
        args.putString("disasterId", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_personnel, container, false);
        dataShow = (RecyclerView) view.findViewById(R.id.data_show);
        dataShow.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        disasterId = getArguments().getString("disasterId");
        personnelRequest();
        return view;
    }

    private void personnelRequest() {
        OkHttpUtils.get().url(getString(R.string.base_url) + "/DisastersRelated/findFourPersonByDisNo/" + disasterId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("response = " + response);
                        JSONObject js = null;
                        try {
                            js = new JSONObject(response);
                            JSONObject meta = new JSONObject(js.optString("meta"));
                            Boolean success = meta.optBoolean("success");
                            String message = meta.optString("message");
                            if (success) {
                                JSONObject data = new JSONObject(js.optString("data"));
                                String humaninfo = data.optString("humaninfo");
                                String areaAdmin = data.optString("areaAdmin");
                                String dihuanzhan = data.optString("dihuanzhan");
                                String areaProfessor = data.optString("areaProfessor");
                                System.out.println("areaProfessor = " + areaProfessor);
                                List<PersonnelInfo> list = new ArrayList<>();
                                if (!TextUtils.isEmpty(humaninfo)) {
                                    JSONObject jsHumaninfo = new JSONObject(humaninfo);
                                    String tel = jsHumaninfo.optString("tel");
                                    String name = jsHumaninfo.optString("name");
                                    String url = jsHumaninfo.optString("url");
                                    personnelInfo = new PersonnelInfo("群测群防员", "MonitorManImg", tel, name, url);
                                    list.add(personnelInfo);
                                }
                                if (!TextUtils.isEmpty(areaAdmin)) {
                                    JSONObject jsAreaAdmin = new JSONObject(areaAdmin);
                                    String tel = jsAreaAdmin.optString("tel");
                                    String name = jsAreaAdmin.optString("name");
                                    String url = jsAreaAdmin.optString("url");
                                    personnelInfo = new PersonnelInfo("片区专管员", "AreaAdmin", tel, name, url);
                                    list.add(personnelInfo);
                                }
                                if (!TextUtils.isEmpty(dihuanzhan)) {
                                    JSONObject jsDihuanzhan = new JSONObject(dihuanzhan);
                                    String tel = jsDihuanzhan.optString("tel");
                                    String name = jsDihuanzhan.optString("name");
                                    String url = jsDihuanzhan.optString("url");
                                    personnelInfo = new PersonnelInfo("地环站人员", "EarthManimg", tel, name, url);
                                    list.add(personnelInfo);
                                }
                                if (!"null".equals(areaProfessor)) {
                                    System.out.println("list = 我还是进来了");
                                    JSONObject jsAreaProfessor = new JSONObject(areaProfessor);
                                    String tel = jsAreaProfessor.optString("tel");
                                    String name = jsAreaProfessor.optString("name");
                                    String url = jsAreaProfessor.optString("url");
                                    personnelInfo = new PersonnelInfo("驻守地质队员", "AreaProfessor", tel, name, url);
                                    list.add(personnelInfo);
                                }
                                initAdapter(list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void initAdapter(final List<PersonnelInfo> list) {
        dataShow.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        personnelAdapter = new PersonnelAdapter(getActivity(), list);
        personnelAdapter.setOnItemClickListener(new PersonnelAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position) {
                new AlertDialog.Builder(getActivity()).
                        setTitle("是否拨打" + list.get(position).getName() + "的电话").
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(position).getTel()));
                        startActivity(intent);
                    }
                }).show();

            }
        });
        dataShow.setAdapter(personnelAdapter);
    }
}
