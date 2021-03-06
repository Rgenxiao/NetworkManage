package com.example.networkmanage.requestWork;

import android.util.Log;

import com.example.networkmanage.callback.DownloadCallback;
import com.example.networkmanage.callback.RequestCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class RetrofitFramework implements NetworkRequestMethod{

    private static RetrofitFramework retrofitFramework;
    public static RetrofitFramework getInstance(){
        if(retrofitFramework == null){
            retrofitFramework = new RetrofitFramework();
        }
        return retrofitFramework;
    }

    @Override
    public void postDownload(String tag, String baseUrl, String restUrl, Map<String, Object> createDownloadParam, Map<String, Object> paramMap, DownloadCallback callback) {

    }

    @Override
    public void getDownload(String tag, String baseUrl, String restUrl, Map<String, Object> createDownloadParam, Map<String, Object> paramMap, DownloadCallback downloadCallback) {

        if(retrofitHttpService == null){
            retrofitHttpService = createRetrofitRequest(baseUrl);
        }
        if (createDownloadParam!=null&&!createDownloadParam.isEmpty()) {
            createDownloadParam.put("tag",tag);
        }
        Call<ResponseBody> request = retrofitHttpService.getDownload(createDownloadParam,restUrl, paramMap);
        download(request,downloadCallback);
    }

    @Override
    public void getRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {
        if(retrofitHttpService == null){
            retrofitHttpService = createRetrofitRequest(baseUrl);
        }
        Call<ResponseBody> request = retrofitHttpService.getRequest(tag,restUrl,paramMap);
        request(request,callback);
    }

    @Override
    public void postRequest(String tag, String baseUrl, String restUrl, String jsonStr, RequestCallback callback) {
        if(retrofitHttpService == null){
            retrofitHttpService = createRetrofitRequest(baseUrl);
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<ResponseBody> request = retrofitHttpService.postRequest(tag,restUrl,requestBody);
        request(request,callback);
    }

    @Override
    public void postRequest(String tag, String baseUrl, String restUrl, Map<String, Object> fileMap, RequestCallback callback) {
        if(retrofitHttpService == null){
            retrofitHttpService = createRetrofitRequest(baseUrl);
        }
        Call<ResponseBody> request = retrofitHttpService.postFileRequest(tag,restUrl,fileMap);
        request(request,callback);
    }
    @Override
    public void postRequest(String tag, String baseUrl, String restUrl, MultipartBody multipartBody, RequestCallback callback) {
        if(retrofitHttpService == null){
            retrofitHttpService = createRetrofitRequest(baseUrl);
        }
        Call<ResponseBody> request = retrofitHttpService.postFileRequest(tag,restUrl,multipartBody);
        request(request,callback);
    }

    @Override
    public void putRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {

    }

    @Override
    public void deleteRequest(String tag, String baseUrl, String restUrl, Map<String, Object> paramMap, RequestCallback callback) {

    }

    /**
     * 实现网络请求
     * */
    private <T> void request(Call<T> request, final RequestCallback callback){
        request.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                try {
                    T body = response.body();
                    if(body instanceof ResponseBody){
                        ResponseBody responseBody = (ResponseBody) body;
                        String requestResult = responseBody.string();
                        String tag = call.request().header("tag");
                        callback.requestResult(tag,requestResult);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.requestResult("","数据异常!");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.requestResult("onFailure",t.getMessage());
            }
        });
    }

    /**
     * 实现文件下载请求
     * */
    private <T> void download(Call<T> request, final DownloadCallback downloadCallback){
        request.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                String tag = "unknown";
                try {
                    tag = call.request().header("tag");
                    T body = response.body();
                    if(body instanceof ResponseBody){
                        ResponseBody responseBody = (ResponseBody) body;

    //                        byteToLocalFile(bytes,fileSavePath);
    //                        downloadCallback.downloadResult(tag,true,fileFolder,100);
                        writeFileFromIS(call, responseBody, downloadCallback);
                    } else{
                        downloadCallback.onFail(tag,"callback data type Exception");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadCallback.onFail(tag,"data error");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d("","");
                String tag = call.request().header("tag");
                downloadCallback.onFail(tag,t.getMessage().toString());
            }
        });
    }

    public interface RetrofitHttpService {
//        @Multipart
        @POST
        Call<ResponseBody> postFileRequest(@Header("tag") String tag, @Url String url,@QueryMap Map<String, Object> multipartParam);
//        @Multipart
        @POST
        Call<ResponseBody> postFileRequest(@Header("tag") String tag, @Url String url,@Body MultipartBody multipartBody);
        @GET
        Call<ResponseBody> getRequest(@Header("tag") String tag, @Url String url, @QueryMap Map<String, Object> params);
        @POST
        Call<ResponseBody> postRequest(@Header("tag") String tag, @Url String url, @Body RequestBody jsonData);

        @Streaming
        @GET
        Call<ResponseBody> getDownload(@HeaderMap Map<String,Object> headerMap, @Url String url, @QueryMap Map<String, Object> params);
    }

    /**
     * 将输入流写入文件
     * */
    private int sBufferSize = 8192;
    private <T> void writeFileFromIS(Call<T> call, ResponseBody responseBody,DownloadCallback downloadCallback) {

        //下载标识
        String tag = call.request().header("tag");
        if(call.isCanceled()){
            downloadCallback.onCancel(tag);
            return;
        }

        //文件输入流
        InputStream inputStream = responseBody.byteStream();
        //文件总长度
        long totalLength = responseBody.contentLength();
        //文件存放目录
        String fileFolder = call.request().header("fileFolder");
        //存放文件名（带后缀）
        String fileName = call.request().header("fileName");
        try {
            fileName= URLDecoder.decode(fileName,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //文件完整路径
        String fileSavePath = fileFolder+File.separator+fileName;

        Log.d("retrofit download","开始下载...");
        downloadCallback.onStart(tag);

        File file=new File(fileSavePath);
        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                downloadCallback.onFail(tag,"createNewFile IOException");
                return ;
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = inputStream.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                int progress = (int) (100 * (Float.parseFloat(currentLength+"") / totalLength));
                downloadCallback.onProgress(tag,progress);
            }
            //下载完成，并返回保存的文件路径
            downloadCallback.onFinish(tag,fileSavePath);
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onFail(tag,"IOException");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 字节存到的文件
     *
     * @param bytes         获取到的文件字节
     * @param localFileName 字节存到的文件路径：文件绝对路径
     */
    public static void byteToLocalFile(final byte[] bytes, final String localFileName) {
        FileOutputStream output = null;
        try {
            File storeFile = new File(localFileName);
            if (storeFile.exists()) {
                storeFile.delete();
            }
            // 目标文件不存在则创建
            if (!new File(storeFile.getParent()).exists()) {
                new File(storeFile.getParent()).mkdirs();
            }

            output = new FileOutputStream(storeFile);
            System.out.println("文件下载中......");
            // 得到资源的字节数组,并写入文件
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 创建请求
     * */
    private static RetrofitHttpService retrofitHttpService;
    private static synchronized RetrofitHttpService createRetrofitRequest(String baseUrl) {
        if(retrofitHttpService == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            client.dispatcher().setMaxRequests(3);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .callbackExecutor(Executors.newCachedThreadPool())
                    .build();
            retrofitHttpService = retrofit.create(RetrofitHttpService.class);
        }
        return retrofitHttpService;
    }
}
