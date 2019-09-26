package com.example.networkmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NetworkManage networkManage;

    private TextView startRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRequest = findViewById(R.id.startRequest);

        networkManage=new NetworkManage();

        startRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getRequest();
//                postRequest();
                AndPermission.with(MainActivity.this)
                        .requestCode(100)
                        .permission(Permission.STORAGE)
                        .callback(listener)
                        .start();
            }
        });
    }
    //授权结果
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == 100) {
                downloadFile();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == 100) {
                // TODO ...
            }
        }
    };

    /**
     * 文件下载请求
     * */
    private void downloadFile() {
        String root = getRoot();

        networkManage.getFileDownload("0",root,new DownloadCallback(){
            @Override
            public void onStart(String tag) {

            }

            @Override
            public void onProgress(String tag, int progress) {
                Log.d("onProgress:",tag+"===="+progress);
            }

            @Override
            public void onFinish(String tag, String folderPath) {
                Log.d("onFinish:",folderPath);
            }

            @Override
            public void onFail(String tag, String errorMessage) {

            }

            @Override
            public void onCancel(String tag) {

            }
        });
    }

    //获取文件存放目录
    private String getRoot() {
        String filePath = null;
        try {
            String packageName = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;
            filePath = Environment.getExternalStorageDirectory() +File.separator+ packageName;
            File fileDir = new File(filePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * post data request
     * */
    private void postRequest() {
        networkManage.postDataRequest("0",new RequestCallback(){
            @Override
            public void requestResult(String tag,String requestResult) {
                Toast.makeText(MainActivity.this,requestResult,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * get data request
     * */
    private void getRequest(){
        networkManage.getDataRequest("0",new RequestCallback(){
            @Override
            public void requestResult(String tag,String requestResult) {
                Toast.makeText(MainActivity.this,requestResult,Toast.LENGTH_LONG).show();
            }
        });
    }
}
