package com.example.networkmanage.requestWork;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface NetworkRequestMethod {

    /**
     * method post
     * file download
     * @param tag         request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param paramMap    request param
     * @param callback    request callback
     * */
    void postDownload(String tag, String baseUrl, String restUrl, Map<String,Object> createDownloadParam,Map<String,Object> paramMap, DownloadCallback callback);
    /**
     * method get
     * file download
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param paramMap    request param
     * @param callback    request callback
     * */
    void getDownload(String tag, String baseUrl, String restUrl, Map<String,Object> createDownloadParam,Map<String,Object> paramMap, DownloadCallback callback);
    /**
     * method get
     * network request
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param paramMap    request param
     * @param callback    request callback
     * */
    void getRequest(String tag, String baseUrl, String restUrl, Map<String,Object> paramMap, RequestCallback callback);
    /**
     * method post
     * network request
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param jsonData    request param
     * @param callback    request callback
     * */
    void postRequest(String tag,String baseUrl, String restUrl,String jsonData,RequestCallback callback);
    /**
     * method post
     * network request
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param fileMap    request param
     * @param callback    request callback
     * */
    void postRequest(String tag, String baseUrl, String restUrl, Map<String, Object> fileMap, RequestCallback callback);/**
     * method post
     * network request
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param multipartBody    request param
     * @param callback    request callback
     * */
    void postRequest(String tag, String baseUrl, String restUrl, MultipartBody multipartBody, RequestCallback callback);
    /**
     * method put
     * network request
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param paramMap    request param
     * @param callback    request callback
     * */
    void putRequest(String tag,String baseUrl, String restUrl,Map<String,Object> paramMap,RequestCallback callback);
    /**
     * method delete
     * network request
     * @param tag        request tag
     * @param baseUrl     request baseUrl
     * @param restUrl     request restUrl
     * @param paramMap    request param
     * @param callback    request callback
     * */
    void deleteRequest(String tag,String baseUrl, String restUrl,Map<String,Object> paramMap,RequestCallback callback);
}
