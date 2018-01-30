package com.nandi.cqdisaster.securitydao.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.utils.FileUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 视频播放
 * Created by qingsong on 2018/1/8.
 */

public class VideoFragment extends Fragment {

    @BindView(R.id.videoTitle)
    TextView videoTitle;
    @BindView(R.id.video_start)
    RelativeLayout videoStart;
    private String fileName;
    private File file;
    private File fileDir;

    public static VideoFragment newInstance(String url) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("URL", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        fileName = getArguments().getString("URL");
        if (fileName.trim().length() == 0) {
            fileName = "5002322010050101.mp4";
        }
        videoTitle.setText(fileName);
        return view;
    }

    private void playMedia(File response, String type) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            uri = Uri.parse(response.getAbsolutePath());
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            uri = Uri.parse("file://" + response.getAbsolutePath());
        }
        it.setDataAndType(uri, type);
        startActivity(it);
    }

    private File createFileDir(String dir) {
        String path = Environment.getExternalStorageDirectory() + "/" + dir;
        boolean orExistsDir = FileUtil.createOrExistsDir(path);
        if (orExistsDir) {
            return new File(path);
        } else {
            return null;
        }
    }

    private void videoRequest() {

        String url = "http://183.230.182.149:19088/rend/down/video?path=d:/cmd/video/dis/" + fileName;
        OkHttpUtils.get().url(url)
                .build()
                .execute(new FileCallBack(fileDir.getPath(), fileName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        System.out.println("视频下载失败" + e.getMessage());
                        Toast.makeText(getActivity(), "视频下载失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Toast.makeText(getActivity(), "视频下载成功", Toast.LENGTH_SHORT).show();
                        playMedia(response, "video/mp4");
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (this.isVisible()) {
            if (!isVisibleToUser) {
            }

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick(R.id.video_start)
    public void onViewClicked() {
        fileDir = createFileDir("Video");
        file = new File(fileDir, fileName);
        long length = file.length();
        if (length > 0) {
            playMedia(file, "video/mp4");
        } else {
            videoRequest();
        }
    }
}