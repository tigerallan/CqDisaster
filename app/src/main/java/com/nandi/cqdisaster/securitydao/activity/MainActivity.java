package com.nandi.cqdisaster.securitydao.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.Constant;
import com.nandi.cqdisaster.securitydao.adapter.AreaAdapter;
import com.nandi.cqdisaster.securitydao.bean.AreaName;
import com.nandi.cqdisaster.securitydao.bean.LocationPoint;
import com.nandi.cqdisaster.securitydao.utils.DESUtil;
import com.nandi.cqdisaster.securitydao.utils.JsonFormat;
import com.nandi.cqdisaster.securitydao.utils.SharedUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private BitmapDescriptor mCurrentMarker;
    private BaiduMap mBaidumap;
    private Spinner areaName;
    private Spinner typePoint;
    private List<AreaName> areaNames;
    private int typeId;
    private int areaId;
    private LinearLayout search;
    private int level;
    private TextView cityName;
    private LatLng point;
    private ProgressDialog progressDialog;
    private Context context;
    private String passwordDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在搜索");
        loginRequest();
        initMapView();
        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println("marker = " + marker.getZIndex());
                Bundle extraInfo = marker.getExtraInfo();
                LocationPoint message = (LocationPoint) extraInfo.getSerializable("message");
                System.out.println("extraInfo = " + message.toString());
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                intent.putExtra("baseMessage", message);
                startActivity(intent);
                return false;
            }
        });
    }

    private void loginRequest() {
        progressDialog.show();
        String str = new StringBuilder("168168").reverse().toString();
        try {
            passwordDes = DESUtil.encryption(str, "ycmcwin2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.post().url(getString(R.string.base_url) + "/system/login")
                .addParams("username", "admin")
                .addParams("password", passwordDes)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject js = new JSONObject(response);
                            JSONObject meta = new JSONObject(js.optString("meta"));
                            Boolean success = meta.optBoolean("success");
                            String message = meta.optString("message");
                            if (success) {
                                JSONObject data = new JSONObject(js.optString("data"));
                                level = data.optInt("is_manager");
                                areaId = data.optInt("areaid");
                                SharedUtils.putShare(context, Constant.LEVEL, level);
                                SharedUtils.putShare(context, Constant.AREA_ID, areaId);
                                SharedUtils.putShare(context, Constant.USERNAME, "admin");
                                areaRequest();
                            } else {

                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void areaRequest() {
        OkHttpUtils.get().url(getString(R.string.base_url) + "/area/selectAreaByParentId/1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        System.out.println("response = " + response);
                        JSONObject js = null;
                        try {
                            js = new JSONObject(response);
                            JSONObject meta = new JSONObject(js.optString("meta"));
                            Boolean success = meta.optBoolean("success");
                            String message = meta.optString("message");
                            if (success) {
                                String data = js.optString("data");
                                areaNames = new ArrayList<>();
                                areaNames.add(new AreaName(0, 0, 0, 0, 0, "全部", 0, 0));
                                areaNames.addAll(JsonFormat.stringToList(data, AreaName.class));
                                AreaAdapter areaAdapter = new AreaAdapter(areaNames, MainActivity.this);
                                areaName.setAdapter(areaAdapter);
                                if (level > 1) {
                                    for (int i = 0; i < areaNames.size(); i++) {
                                        if (areaId == areaNames.get(i).getId()) {
                                            areaName.setSelection(i);
                                            areaName.setEnabled(false);
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    private void initOverLay(List<LocationPoint> locationPoints) {
        // TODO: 2018/1/8 打点
        //创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
//        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_lc01);


        //设置坐标点
        for (int i = 0; i < locationPoints.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("message", locationPoints.get(i));
            double disLon = locationPoints.get(i).getDis_lon();
            double disLat = locationPoints.get(i).getDis_lat();
            point = new LatLng(disLat, disLon);
            //创建OverlayOptions属性
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .extraInfo(bundle);

            //将OverlayOptions添加到list
            options.add(option);
        }
        //在地图上批量添加
        System.out.println("options = " + options.size());
        mBaidumap.addOverlays(options);
        progressDialog.dismiss();
    }

    /**
     * 设置地图
     */
    private void initMapView() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        initListener();
        mBaidumap = mMapView.getMap();
        mBaidumap.setMyLocationEnabled(true);
        mBaidumap.setCompassPosition(new android.graphics.Point(100, 380));
        // 改变地图状态，使地图显示在恰当的缩放大小
        MapStatus mMapStatus = new MapStatus.Builder().zoom(8).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaidumap.setMapStatus(mMapStatusUpdate);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0)
                .latitude(29.56667)
                .longitude(106.45000)
                .build();

        // 设置定位数据
        mBaidumap.setMyLocationData(locData);

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        mCurrentMarker = BitmapDescriptorFactory.fromResource(0);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, false, mCurrentMarker);
        mBaidumap.setMyLocationConfiguration(config);
        // 当不需要定位图层时关闭定位图层
        mBaidumap.setMyLocationEnabled(true);
    }

    private void initListener() {
        search = (LinearLayout) findViewById(R.id.search_point);
        cityName = (TextView) findViewById(R.id.city_name);
        areaName = (Spinner) findViewById(R.id.area_name);
        typePoint = (Spinner) findViewById(R.id.type_point);
        areaName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    areaId = 1;
                } else {
                    areaId = areaNames.get(position).getId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typePoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                typeId = position;
                if (position == 0) {
                    typeId = position - 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                mBaidumap.clear();
                OkHttpUtils.get().url(getString(R.string.base_url) + "/tabDisastersInfo/sreachDisasters/" + areaId + "/" + typeId)
                        .build()
                        .execute(new StringCallback() {


                            @Override
                            public void onError(Call call, Exception e, int id) {
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {

                                System.out.println("response = " + response);
                                JSONObject js;
                                try {
                                    js = new JSONObject(response);
                                    JSONObject meta = new JSONObject(js.optString("meta"));
                                    Boolean success = meta.optBoolean("success");
                                    String message = meta.optString("message");
                                    if (success) {
                                        String data = js.optString("data");
                                        List<LocationPoint> locationPoints = JsonFormat.stringToList(data, LocationPoint.class);
                                        initOverLay(locationPoints);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
