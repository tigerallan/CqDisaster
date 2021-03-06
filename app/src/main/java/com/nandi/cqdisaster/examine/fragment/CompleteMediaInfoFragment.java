package com.nandi.cqdisaster.examine.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.examine.activity.CompleteInfoActivity;
import com.nandi.cqdisaster.examine.adapter.MediaInfoAdapter;
import com.nandi.cqdisaster.examine.bean.MediaInfo;
import com.nandi.cqdisaster.examine.constant.ConnectUrl;
import com.nandi.cqdisaster.examine.utils.MyUtils;
import com.nandi.cqdisaster.examine.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 查看反馈信息---媒体信息页面
 * Created by ChenPeng on 2017/3/14.
 */

public class CompleteMediaInfoFragment extends Fragment {
    @BindView(R.id.rv_media_info_picture)
    RecyclerView rvMediaInfoPicture;
    @BindView(R.id.rv_media_info_video)
    RecyclerView rvMediaInfoVideo;
    @BindView(R.id.rv_media_info_audio)
    RecyclerView rvMediaInfoAudio;
    @BindView(R.id.rv_media_info_folder)
    RecyclerView rvMediaInfoFolder;
    private String taskId;
    private ProgressDialog progressDialog;
    private MediaPlayer player;
    private SharedPreferences sp;
    private String sessionId;
    private List<MediaInfo> pictureList = new ArrayList<>();
    private List<MediaInfo> videoList = new ArrayList<>();
    private List<MediaInfo> audioList = new ArrayList<>();
    private List<MediaInfo> folderList = new ArrayList<>();
    private List<File> downloadFile = new ArrayList<>();
    private MediaInfoAdapter pictureAdapter;
    private MediaInfoAdapter videoAdapter;
    private MediaInfoAdapter audioAdapter;
    private MediaInfoAdapter folderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_mediainfo, null);
        ButterKnife.bind(this, view);
        taskId = ((CompleteInfoActivity) getActivity()).taskInfoBean.getmTaskId();
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        sessionId = sp.getString("sessionId", "");
        initView();
        initData();
        setAdapter();
        setListener();
        return view;
    }

    private void setListener() {
        pictureAdapter.setOnItemClickListener(new MediaInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rvMediaInfoPicture.getChildAdapterPosition(view);
                File file = new File(getSdPath("picture") + "/" + pictureList.get(position).getName());
                if (file.isFile() && file.exists()) {
                    showPhoto(file);
                } else {
                    loadPhoto(pictureList.get(position));
                }
            }
        });
        videoAdapter.setOnItemClickListener(new MediaInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rvMediaInfoVideo.getChildAdapterPosition(view);
                File file = new File(getSdPath("video") + "/" + videoList.get(position).getName());
                if (file.isFile() && file.exists()) {
                    playVideo(file);
                } else {
                    showVideo(videoList.get(position));
                }
            }
        });
        audioAdapter.setOnItemClickListener(new MediaInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rvMediaInfoAudio.getChildAdapterPosition(view);
                File file = new File(getSdPath("audio") + "/" + audioList.get(position).getName());
                if (file.isFile() && file.exists()) {
                    playAudio(file.getAbsolutePath());
                } else {
                    showAudio(audioList.get(position));
                }
            }
        });
        folderAdapter.setOnItemClickListener(new MediaInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = rvMediaInfoFolder.getChildAdapterPosition(view);
                File file = new File(getSdPath("folder") + "/" + folderList.get(position).getName());
                if (file.isFile() && file.exists()) {
                    openFile(file);
                } else {
                    showFolder(folderList.get(position));
                }
            }
        });
    }

    private void showFolder(MediaInfo mediaInfo) {
        progressDialog.show();
        OkHttpUtils.get().url(new ConnectUrl().getMediaInfoUrl())
                .addParams("id", mediaInfo.getId())
                .addParams("type", mediaInfo.getType())
                .build()
                .execute(new FileCallBack(getSdPath("folder"), mediaInfo.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        progressDialog.dismiss();
                        downloadFile.add(response);
                        openFile(response);
                    }
                });
    }


    private void showAudio(MediaInfo mediaInfo) {
        progressDialog.show();
        OkHttpUtils.get().url(new ConnectUrl().getMediaInfoUrl())
                .addParams("id", mediaInfo.getId())
                .addParams("type", mediaInfo.getType())
                .build()
                .execute(new FileCallBack(getSdPath("audio"), mediaInfo.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        progressDialog.dismiss();
                        downloadFile.add(response);
                        playAudio(response.getAbsolutePath());
                    }
                });
    }

    private void loadPhoto(MediaInfo mediaInfo) {
        progressDialog.show();
        OkHttpUtils.get().url(new ConnectUrl().getMediaInfoUrl())
                .addParams("id", mediaInfo.getId())
                .addParams("type", mediaInfo.getType())
                .build()
                .execute(new FileCallBack(getSdPath("picture"), mediaInfo.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        progressDialog.dismiss();
                        downloadFile.add(response);
                        showPhoto(response);
                    }
                });
    }
    private void showVideo(MediaInfo mediaInfo) {
        progressDialog.show();
        OkHttpUtils.get().url(new ConnectUrl().getMediaInfoUrl())
                .addParams("id", mediaInfo.getId())
                .addParams("type", mediaInfo.getType())
                .build()
                .execute(new FileCallBack(getSdPath("video"), mediaInfo.getName()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShortToast("连接服务器失败！");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        progressDialog.dismiss();
                        downloadFile.add(response);
                        playVideo(response);
                    }
                });
    }

    private void playVideo(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "video/mp4";
        Uri uri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            uri = FileProvider.getUriForFile(getActivity(), "com.nandity.disastersystem", file);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, type);
        startActivity(intent);
    }

    private void playAudio(String path) {
        player = new MediaPlayer();
        try {
            player.setDataSource(path);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.diaolog_play_audio, null);
        Button btnStart = (Button) view.findViewById(R.id.btn_dialog_play);
        Button btnPause = (Button) view.findViewById(R.id.btn_dialog_pause);
        Button btnStop = (Button) view.findViewById(R.id.btn_dialog_stop);
        MyOnClickListener clickListener = new MyOnClickListener();
        btnStart.setOnClickListener(clickListener);
        btnPause.setOnClickListener(clickListener);
        btnStop.setOnClickListener(clickListener);
        new MaterialDialog.Builder(getActivity())
                .title("播放录音")
                .customView(view, false)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText("关闭")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        player.stop();
                    }
                }).show();
    }

    private void openFile(File file) {
        Intent intent = null;
        try {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, getMIMEType(file));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShortToast("该文件无法打开！");
        }
    }
    private final String[][] MIME_MapTable={
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-JavaScript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    private String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
//获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
/* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
//在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    private void showPhoto(File file) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_show_photo, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_dialog_picture);
        imageView.setImageBitmap(MyUtils.getSmallBitmap(file.getAbsolutePath(),600,800));
        new MaterialDialog.Builder(getActivity())
                .cancelable(true)
                .customView(view, false)
                .show();
    }

    private void setAdapter() {
        rvMediaInfoPicture.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMediaInfoVideo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMediaInfoAudio.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMediaInfoFolder.setLayoutManager(new LinearLayoutManager(getActivity()));
        pictureAdapter = new MediaInfoAdapter(pictureList);
        videoAdapter = new MediaInfoAdapter(videoList);
        audioAdapter = new MediaInfoAdapter(audioList);
        folderAdapter = new MediaInfoAdapter(folderList);
        rvMediaInfoPicture.setAdapter(pictureAdapter);
        rvMediaInfoVideo.setAdapter(videoAdapter);
        rvMediaInfoAudio.setAdapter(audioAdapter);
        rvMediaInfoFolder.setAdapter(folderAdapter);
    }

    private void initData() {
        pictureList.clear();
        videoList.clear();
        audioList.clear();
        folderList.clear();
        OkHttpUtils.get().url(new ConnectUrl().getMediaInfoListUrl())
                .addParams("sessionId", sessionId)
                .addParams("taskId", taskId)
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
                            if ("200".equals(status)) {
                                JSONArray audio = object.getJSONArray("audio");
                                JSONArray video = object.getJSONArray("video");
                                JSONArray picture = object.getJSONArray("pic");
                                JSONArray folder = object.getJSONArray("diaocha");
                                for (int i = 0; i < audio.length(); i++) {
                                    JSONObject oj = audio.getJSONObject(i);
                                    MediaInfo info = new MediaInfo();
                                    info.setId(oj.getString("id"));
                                    info.setName(oj.getString("path"));
                                    info.setType(oj.getString("type"));
                                    audioList.add(info);
                                }
                                for (int i = 0; i < video.length(); i++) {
                                    JSONObject oj = video.getJSONObject(i);
                                    MediaInfo info = new MediaInfo();
                                    info.setId(oj.getString("id"));
                                    info.setName(oj.getString("path"));
                                    info.setType(oj.getString("type"));
                                    videoList.add(info);
                                }
                                for (int i = 0; i < picture.length(); i++) {
                                    JSONObject oj = picture.getJSONObject(i);
                                    MediaInfo info = new MediaInfo();
                                    info.setId(oj.getString("id"));
                                    info.setName(oj.getString("path"));
                                    info.setType(oj.getString("type"));
                                    pictureList.add(info);
                                }
                                for (int i = 0; i < folder.length(); i++) {
                                    JSONObject oj = folder.getJSONObject(i);
                                    MediaInfo info = new MediaInfo();
                                    info.setId(oj.getString("id"));
                                    info.setName(oj.getString("path"));
                                    info.setType(oj.getString("type"));
                                    folderList.add(info);
                                }
                                audioAdapter.notifyDataSetChanged();
                                videoAdapter.notifyDataSetChanged();
                                pictureAdapter.notifyDataSetChanged();
                                folderAdapter.notifyDataSetChanged();
                            } else if ("400".equals(status)) {
                                msg = object.getString("message");
                                ToastUtils.showShortToast(msg);
//                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在下载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (File file : downloadFile) {
            if (file.isFile()&&file.exists()){
                file.delete();
            }
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_dialog_play:
                    player.start();
                    break;
                case R.id.btn_dialog_pause:
                    if (player.isPlaying()) {
                        player.pause();
                    }
                    break;
                case R.id.btn_dialog_stop:
                    player.stop();
                    break;
            }
        }
    }

    private String getSdPath(String folder) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + "/disaster/" + folder;
        }
        return "";
    }
}
