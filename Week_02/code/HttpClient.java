package code;

import okhttp3.*;


import java.io.IOException;

public class HttpClient {


    private static String URL = "http://localhost:8801";

    public static void main(String[] args) {
        //     String url = "http://www.baidu.com";
        getDataAsync();
     //   getDataSync();
    }

    /**
     * 异步处理

     */
    public static void getDataAsync() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(URL)
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        System.out.println("code: " + response.code());
                        System.out.println("data: " + response.body().string());
                    } else {
                        System.out.println("get fail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 同步处理

     */
    public static void getDataSync() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("get fail");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    System.out.println("code: " + response.code());
                    System.out.println("data: " + response.body().string());
                }
            }
        });
    }
}


