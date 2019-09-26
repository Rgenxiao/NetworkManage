package com.example.networkmanage;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;
import com.example.networkmanage.requestWork.RetrofitFramework;
import java.util.HashMap;
import java.util.Map;

public class NetworkManage extends RetrofitFramework {
    //TODO 接口调用请求

    /**
     * post request
     * http://192.168.2.206:8080/download?param=0.tpk
     * */
    public void getFileDownload(String tag, String root, DownloadCallback downloadCallback){
        String baseUrl = "http://192.168.2.206:8080";
        String restUrl= "/download";

        Map<String,Object> createDownloadParam = new HashMap<>();
        createDownloadParam.put("fileFolder",root);
        createDownloadParam.put("isDeleteOld",false);
        createDownloadParam.put("fileName",tag+".tpk");


        Map<String,Object> params = new HashMap<>();
        params.put("param",tag+".tpk");

        getDownload(tag,baseUrl,restUrl,createDownloadParam,params,downloadCallback);
    }

    /**
     * get request
     * http://192.168.2.206:8080/get?param=
     * */
    public void getDataRequest(String tag,RequestCallback callback){
        String baseUrl = "http://192.168.2.206:8080";
        String restUrl= "/get";
        Map<String,Object> params = new HashMap<>();
        params.put("param","");

        getRequest(tag,baseUrl,restUrl,params,callback);
    }
    /**
     * post request
     * http://192.168.2.206:8080/get?param=
     * */
    public void postDataRequest(String tag,RequestCallback callback){
        String baseUrl = "http://192.168.2.206:8080";
        String restUrl= "/post";
        Map<String,Object> params = new HashMap<>();
        params.put("param","");

        postRequest(tag,baseUrl,restUrl,params,callback);
    }
}
