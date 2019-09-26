package com.example.networkmanage.requestWork;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import org.json.JSONObject;
import java.util.Map;

public class NoHttpFramework implements NetworkRequestMethod {

    @Override
    public void postDownload(String tag, String baseUrl, String restUrl, Map<String, Object> createDownloadParam, Map<String, Object> paramMap, DownloadCallback callback) {

        String requestUrl = baseUrl+restUrl;
        DownloadRequest downloadRequest = createDownloadRequest(requestUrl, RequestMethod.POST,createDownloadParam, paramMap);
        if (downloadRequest!=null) {
            download(tag, downloadRequest, callback);
        }
    }

    @Override
    public void getDownload(String tag, String baseUrl, String restUrl, Map<String, Object> createDownloadParam, Map<String, Object> paramMap, DownloadCallback callback) {

        String requestUrl = baseUrl+restUrl;
        DownloadRequest downloadRequest = createDownloadRequest(requestUrl, RequestMethod.GET,createDownloadParam, paramMap);
        if (downloadRequest!=null) {
            download(tag, downloadRequest, callback);
        }
    }

    @Override
    public void getRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {

        String requestUrl = baseUrl+restUrl;
        Request<String> stringRequest = NoHttp.createStringRequest(requestUrl, RequestMethod.GET);
        if (paramMap!=null&&!paramMap.isEmpty()) {
            stringRequest.add(paramMap);
        }
        request(tag, stringRequest, callback);
    }

    @Override
    public void postRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {

        String requestUrl = baseUrl+restUrl;
        Request<String> stringRequest = NoHttp.createStringRequest(requestUrl, RequestMethod.POST);
        if (paramMap!=null&&!paramMap.isEmpty()) {
            stringRequest.setDefineRequestBodyForJson(new JSONObject(paramMap));
        }
        request(tag, stringRequest, callback);
    }

    @Override
    public void putRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {

        String requestUrl = baseUrl+restUrl;
        Request<String> stringRequest = NoHttp.createStringRequest(requestUrl, RequestMethod.PUT);
        if (paramMap!=null&&!paramMap.isEmpty()) {
            stringRequest.add(paramMap);
        }
        request(tag, stringRequest, callback);
    }

    @Override
    public void deleteRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {

        String requestUrl = baseUrl+restUrl;
        Request<String> stringRequest = NoHttp.createStringRequest(requestUrl, RequestMethod.DELETE);
        if (paramMap!=null&&!paramMap.isEmpty()) {
            stringRequest.add(paramMap);
        }
        request(tag, stringRequest, callback);
    }

    /**
     * 网络请求
     * */
    private <T> void request(Object tag, Request<T> request, final RequestCallback callback) {
        int what = 0;
        if (tag instanceof Integer) {
            what = (int) tag;
        }
        NoHttp.getRequestQueueInstance().add(what, request, new OnResponseListener<T>() {
            @Override
            public void onStart(int tag) {
            }

            @Override
            public void onSucceed(int tag, Response<T> response) {
                T result = response.get();
                //TODO 返回数据可以在这里统一处理或直接返回
                if (result instanceof String) {
                    callback.requestResult(tag + "", result + "");
                }
            }

            @Override
            public void onFailed(int tag, Response<T> response) {

            }

            @Override
            public void onFinish(int tag) {

            }
        });
    }

    /**
     * 文件下载
     * */
    private <T> void download(Object tag, DownloadRequest downloadRequest, final DownloadCallback downloadCallback) {
        int what = 0;
        if (tag instanceof Integer) {
            what = (int) tag;
        }
        NoHttp.getDownloadQueueInstance().add(what, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int tag, Exception exception) {
                downloadCallback.onFail(tag+"",exception.getMessage());
            }

            @Override
            public void onStart(int tag, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                downloadCallback.onStart(tag+"");
            }

            @Override
            public void onProgress(int tag, int progress, long fileCount, long speed) {
                downloadCallback.onProgress(tag+"", progress);
            }

            @Override
            public void onFinish(int tag, String filePath) {
                downloadCallback.onFinish(tag+"", filePath);
            }

            @Override
            public void onCancel(int tag) {
                downloadCallback.onCancel(tag+"");
            }
        });
    }

    /**
     * 创建下载请求
     * @param restUrl            url
     * @param createDownloadParam   create param
     * @param paramMap              request param
     * @return
     */
    private DownloadRequest createDownloadRequest(String restUrl, RequestMethod method,Map<String, Object> createDownloadParam, Map<String, Object> paramMap){

        DownloadRequest downloadRequest=null;
        if (createDownloadParam.isEmpty()) {
            return null;
        }
        String fileFolder = createDownloadParam.get("fileFolder") + "";
        boolean isDeleteOld = (boolean) createDownloadParam.get("isDeleteOld");
        downloadRequest = NoHttp.createDownloadRequest(restUrl, method,fileFolder, isDeleteOld);

        if (paramMap!=null&&!paramMap.isEmpty()) {
            downloadRequest.setDefineRequestBodyForJson(new JSONObject(paramMap));
        }
        return downloadRequest;
    }
}
