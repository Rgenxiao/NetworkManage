package com.example.networkmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;
import com.example.networkmanage.requestWork.NetworkRequestMethod;
import com.example.networkmanage.requestWork.RetrofitFramework;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.startRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<19;i++){
                    download(i);
                }
            }
        });
    }

    private void request() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loginName","webapp");
            jsonObject.put("loginPwd","pass");
            RetrofitFramework.getInstance().postRequest("0", "http://192.168.1.40:8085/dgp-omms-server-web/rest/", "oms/public/client-account/v1/login-dev", jsonObject.toString(),
                    new RequestCallback() {
                        @Override
                        public void requestResult(String tag, String requestResult) {
                            Log.d("","");
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void download(int count){
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloadTpk";
        String baseUrl = "http://192.168.1.40:8085/dgp-omms-server-web/rest/";
        String restUrl = "omms/v1/offlinepackage/downloadSplitFile";
        Map<String,Object> createMap = new HashMap<>();
        createMap.put("fileFolder",absolutePath);
        createMap.put("fileName",count+".tpk");
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("splitFileName","移动电子底图/"+count+"_移动电子底图.tpk");
        RetrofitFramework.getInstance().getDownload(count+"", baseUrl, restUrl, createMap, paramsMap, downloadCallback);
    }
    private DownloadCallback downloadCallback = new DownloadCallback() {
        @Override
        public void onStart(String tag) {
            Log.d("onStart",tag+"=======");
        }

        @Override
        public void onProgress(String tag, int progress) {
            Log.d("downloadTag","tag======"+progress);
        }

        @Override
        public void onFinish(String tag, String folderPath) {
            Log.d("onFinish",tag+"======");
        }

        @Override
        public void onFail(String tag, String errorMessage) {
            Log.d("","");
        }

        @Override
        public void onCancel(String tag) {

        }
    };
}
