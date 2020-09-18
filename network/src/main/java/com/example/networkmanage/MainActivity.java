package com.example.networkmanage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;
import com.example.networkmanage.requestWork.RetrofitFramework;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.PartMap;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    init();
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                })
                .start();
    }

    private void init() {

        findViewById(R.id.startRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
    }

    private void postRequest() {
        RequestBody requestBody = getFileRequestBody();

        Map<String, Object> mapParam = getMapParams(requestBody);

//        MultipartBody multipartBody = getMultipartBody(requestBody);
        MultipartBody multipartBody = getUpdateMultipartBody(requestBody);

        RetrofitFramework.getInstance().postRequest("0", "http://192.168.1.40:18085/dgp-omms-server-web/rest/", "omms/v1/patrol", multipartBody,
                new RequestCallback() {
                    @Override
                    public void requestResult(String tag, String requestResult) {
                        Log.d("", "");
                    }
                });
    }
    @NotNull
    private MultipartBody getUpdateMultipartBody(RequestBody requestBody) {
        return new MultipartBody.Builder()
                .addFormDataPart("id","243")
                .addFormDataPart("patrolName","n")
                .addFormDataPart("userName","name")
                .addFormDataPart("question","11")
                .addFormDataPart("opinion","test")
                .addFormDataPart("geometry","7651651")
                .addFormDataPart("imageIds","7651651")
                .addFormDataPart("audioIds","tew,7898434")
                .addFormDataPart("moduleName","7651651")
                .addFormDataPart("objectId","5")
                .addFormDataPart("layerName","NEAt")
                .addFormDataPart("layerId","2")
                .setType(MultipartBody.FORM)
                .build();
    }

    @NotNull
    private MultipartBody getMultipartBody(RequestBody requestBody) {
        return new MultipartBody.Builder()
                    .addFormDataPart("patrolName","n")
                    .addFormDataPart("userName","name")
                    .addFormDataPart("question","11")
                    .addFormDataPart("opinion","test")
                    .addFormDataPart("geometry","7651651")
                    .addFormDataPart("objectId","5")
                    .addFormDataPart("moduleName","7651651")
                    .addFormDataPart("layerName","NEAt")
                    .addFormDataPart("layerId","2")
                    .addFormDataPart("files","Test.map",requestBody)
                    .setType(MultipartBody.FORM)
                    .build();
    }

    @NotNull
    private Map<String, Object> getMapParams(RequestBody requestBody) {
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("patrolName","n");
        mapParam.put("userName","name");
        mapParam.put("question","11");
        mapParam.put("opinion","test");
        mapParam.put("geometry","7651651");
        mapParam.put("objectId","5");
        mapParam.put("moduleName","7651651");
        mapParam.put("layerName","NEAt");
        mapParam.put("layerId","2");
        mapParam.put("files",requestBody);
        return mapParam;
    }

    @NotNull
    private RequestBody getFileRequestBody() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test.mp3";
        File file = new File(filePath);
        return RequestBody.create(MediaType.parse("audio/mpeg"),file);
    }

    private void request() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loginName", "webapp");
            jsonObject.put("loginPwd", "pass");
            RetrofitFramework.getInstance().postRequest("0", "http://192.168.1.40:8085/dgp-omms-server-web/rest/", "oms/public/client-account/v1/login-dev", jsonObject.toString(),
                    new RequestCallback() {
                        @Override
                        public void requestResult(String tag, String requestResult) {
                            Log.d("", "");
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void download(int count) {
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloadTpk";
        String baseUrl = "http://192.168.1.40:8085/dgp-omms-server-web/rest/";
        String restUrl = "omms/v1/offlinepackage/downloadSplitFile";
        Map<String, Object> createMap = new HashMap<>();
        createMap.put("fileFolder", absolutePath);
        createMap.put("fileName", count + ".tpk");
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("splitFileName", "移动电子底图/" + count + "_移动电子底图.tpk");
        RetrofitFramework.getInstance().getDownload(count + "", baseUrl, restUrl, createMap, paramsMap, downloadCallback);
    }

    private DownloadCallback downloadCallback = new DownloadCallback() {
        @Override
        public void onStart(String tag) {
            Log.d("onStart", tag + "=======");
        }

        @Override
        public void onProgress(String tag, int progress) {
            Log.d("downloadTag", "tag======" + progress);
        }

        @Override
        public void onFinish(String tag, String folderPath) {
            Log.d("onFinish", tag + "======");
        }

        @Override
        public void onFail(String tag, String errorMessage) {
            Log.d("", "");
        }

        @Override
        public void onCancel(String tag) {

        }
    };
    private RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }
}
