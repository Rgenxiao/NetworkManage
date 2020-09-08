package com.example.networkmanage.callback;

public interface DownloadCallback {

    /**
     * 开始下载
     * */
    void onStart(String tag);
    /**
     * 进度回传
     * */
    void onProgress(String tag,int progress);
    /**
     * 下载完成
     * */
    void onFinish(String tag, String folderPath);
    /**
     * 下载错误
     * */
    void onFail(String tag,String errorMessage);
    /**
     * 取消下载
     * */
    void onCancel(String tag);
}
