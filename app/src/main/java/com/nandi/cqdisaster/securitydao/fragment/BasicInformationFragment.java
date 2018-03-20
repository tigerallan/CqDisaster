package com.nandi.cqdisaster.securitydao.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.bean.LocationPoint;
import com.nandi.cqdisaster.securitydao.utils.AMapUtil;
import com.nandi.cqdisaster.securitydao.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 显示基础信息页面
 * Created by qingsong on 2018/1/8.
 */

public class BasicInformationFragment extends Fragment {

    @BindView(R.id.tv_dangerName)
    TextView tvDangerName;
    @BindView(R.id.tv_dangerNum)
    TextView tvDangerNum;
    @BindView(R.id.tv_dangerType)
    TextView tvDangerType;
    @BindView(R.id.tv_cause)
    TextView tvCause;
    @BindView(R.id.tv_dangerLevel)
    TextView tvDangerLevel;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_storageTime)
    TextView tvStorageTime;
    @BindView(R.id.tv_dangerAddress)
    TextView tvDangerAddress;
    @BindView(R.id.tv_object)
    TextView tvObject;
    @BindView(R.id.tv_peopleNum)
    TextView tvPeopleNum;
    @BindView(R.id.tv_familyNum)
    TextView tvFamilyNum;
    @BindView(R.id.tv_houseNum)
    TextView tvHouseNum;
    @BindView(R.id.tv_threatArea)
    TextView tvThreatArea;
    @BindView(R.id.tv_asset)
    TextView tvAsset;
    @BindView(R.id.tv_village)
    TextView tvVillage;
    @BindView(R.id.tv_longitude)
    TextView tvLongitude;
    @BindView(R.id.tv_latitude)
    TextView tvLatitude;
    @BindView(R.id.tv_slope)
    TextView tvSlope;
    @BindView(R.id.tv_acreage)
    TextView tvAcreage;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_leadEdge)
    TextView tvLeadEdge;
    @BindView(R.id.tv_BackEdge)
    TextView tvBackEdge;
    @BindView(R.id.tv_notes)
    TextView tvNotes;
    @BindView(R.id.tv_dispose)
    TextView tvDispose;
    @BindView(R.id.tv_stability)
    TextView tvStability;
    @BindView(R.id.tv_prevenLevel)
    TextView tvPrevenLevel;
    @BindView(R.id.tv_quncePhone)
    TextView tvQuncePhone;
    @BindView(R.id.tv_defendPhone)
    TextView tvDefendPhone;
    Unbinder unbinder;
    @BindView(R.id.naviBtn)
    Button naviBtn;
    private LocationPoint point;
    private PopupWindow popupWindow;

    public static BasicInformationFragment newInstance(LocationPoint baseMessage) {
        BasicInformationFragment fragment = new BasicInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable("baseMessage", baseMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        point = (LocationPoint) getArguments().getSerializable("baseMessage");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_basic, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        naviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popView();
            }
        });
        return view;
    }

    private void popView() {
        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.map_navagation_sheet, null);
        Button gaode = (Button) popView.findViewById(R.id.gaode_btn);
        Button baidu = (Button) popView.findViewById(R.id.baidu_btn);
        Button tencent = (Button) popView.findViewById(R.id.tencent_btn);
        Button clear = (Button) popView.findViewById(R.id.clear_btn);
        final String name = tvDangerName.getText().toString();
        final String lat = tvLatitude.getText().toString();
        final String lon = tvLongitude.getText().toString();
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AMapUtil.isInstallByRead("com.autonavi.minimap")) {
                    double[] togcj02 =  AppUtils.bd09togcj02(Double.parseDouble(lon),Double.parseDouble(lat));
                    AMapUtil.goToGaoDe(getActivity(), name, togcj02[1] + "", togcj02[0] + "", "0");
                } else {
                    Toast.makeText(getActivity(), "您尚未安装高德地图", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }

        });
        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AMapUtil.isInstallByRead("com.baidu.BaiduMap")) {
                    AMapUtil.goToBaidu(getActivity(), name, lat, lon);
                } else {
                    Toast.makeText(getActivity(), "您尚未安装百度地图", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }
        });
        tencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AMapUtil.isInstallByRead("com.tencent.map")) {
                    double[] togcj02 =  AppUtils.bd09togcj02(Double.parseDouble(lon),Double.parseDouble(lat));
                    AMapUtil.goToTencent(getActivity(), name, togcj02[1] + "", togcj02[0] + "");
                } else {
                    Toast.makeText(getActivity(), "您尚未安装腾讯地图", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_information_basic, null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        popView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                popupWindow.dismiss();
                return false;
            }
        });
    }

    private void initView() {
        tvDangerName.setText(checkNull(point.getDis_name()));
        tvDangerNum.setText(checkNull(point.getDis_no()));
        tvDangerType.setText(checkNull(getDangerType(point.getDis_type())));
        tvCause.setText(checkNull(getCause(point.getDis_cause())));
        tvDangerLevel.setText(checkNull(getDisLevel(point.getImperil_level())));
        tvTime.setText(checkNull(point.getDis_time()));
        tvStorageTime.setText(checkNull(point.getCome_time()));
        tvDangerAddress.setText(checkNull(point.getDis_location()));
        tvObject.setText(checkNull(point.getMain_object()));
        tvPeopleNum.setText(checkNull(point.getImperil_man() + ""));
        tvFamilyNum.setText(checkNull(point.getImperil_families() + ""));
        tvHouseNum.setText(checkNull(point.getImperil_house() + ""));
        tvThreatArea.setText(checkNull(point.getImperil_area()));
        tvAsset.setText(checkNull(point.getImperil_money()));
        tvVillage.setText(checkNull(point.getVillage()));
        tvLongitude.setText(checkNull(String.valueOf(point.getDis_lon())));
        tvLatitude.setText(checkNull(String.valueOf(point.getDis_lat())));
        tvSlope.setText(checkNull(point.getDis_slope()));
        tvAcreage.setText(checkNull(point.getDis_area()));
        tvVolume.setText(checkNull(point.getDis_volume()));
        tvLeadEdge.setText(checkNull(point.getDis_before()));
        tvBackEdge.setText(checkNull(point.getDis_after()));
        tvNotes.setText(checkNull(point.getRemark()));
        tvDispose.setText(checkNull(point.getDeal_idea()));
        tvStability.setText(getStability(point.getStable_level()));
        tvPrevenLevel.setText(getLevel(point.getDefense_level()));
        tvQuncePhone.setText(checkNull(point.getQcqfry_tel()));
        tvDefendPhone.setText(checkNull(point.getZsry_tel()));
    }

    private String checkNull(String s) {

        return "null".equals(s) ? "无" : s;
    }

    private String getLevel(int defense_level) {
        String result;
        switch (defense_level) {
            case 37:
                result = "一级";
                break;
            case 38:
                result = "二级";
                break;
            case 40:
                result = "四级";
                break;
            case 41:
                result = "五级";
                break;
            case 42:
                result = "六级";
                break;
            default:
                result = "三级";
        }
        return result;
    }

    /**
     * 获取稳定性
     *
     * @param stable_level
     * @return
     */
    private String getStability(int stable_level) {
        String result;
        switch (stable_level) {
            case 14:
                result = "稳定";
                break;
            case 15:
                result = "不稳定";
                break;
            case 31:
                result = "欠稳定";
                break;
            default:
                result = "基本稳定";
        }
        return result;
    }

    private String getDisLevel(int imperil_level) {
        String result;
        switch (imperil_level) {

            case 16:
                result = "特大型";
                break;
            case 17:
                result = "大型";
                break;
            case 18:
                result = "中型";
                break;
            default:
                result = "小型";

        }
        return result;
    }

    private String getCause(String dis_cause) {
        Log.d("cp", "getCause:" + dis_cause);
        String result = "";
        String[] split = dis_cause.split(",");
        for (String s : split) {
            switch (s) {
                case "77":
                    result += "暴雨,";
                    break;
                case "78":
                    result += "库水位,";
                    break;
                case "70":
                    result += "地震,";
                    break;
                case "80":
                    result += "工程活动,";
                    break;
                default:
                    result += "不确定,";
            }
        }
        return result.substring(0, result.length() - 1);
    }

    private String getDangerType(int dis_type) {
        String result = null;
        switch (dis_type) {
            case 1:
                result = "滑坡";
                break;
            case 2:
                result = "泥石流";
                break;
            case 3:
                result = "危岩";
                break;
            case 4:
                result = "不稳定斜坡";
                break;
            case 5:
                result = "地面塌陷";
                break;
            case 6:
                result = "地裂缝";
                break;
            case 7:
                result = "库岸";
                break;

        }
        return result;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
