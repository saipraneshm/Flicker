package com.codepath.assignment.flicker.util;



import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkUtil {


    private volatile static NetworkUtil sInstance;
    private final OkHttpClient mHttpClient = new OkHttpClient();
    private static final String TAG = NetworkUtil.class.getSimpleName();

    private static final String MOVIE_DB_API = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_DATA = "MOVIE_DATA";
    private static final Uri NOW_PLAYING_ENDPOINT = Uri.parse(MOVIE_DB_API)
            .buildUpon()
            .appendPath("now_playing")
            .appendQueryParameter("api_key","68851cef4b730bead98aa3172c93d2b5")
            .build();


    private NetworkUtil(){}

    public static NetworkUtil getInstance(){
        if(sInstance == null){
            synchronized (NetworkUtil.class){
                if(sInstance == null)
                    sInstance = new NetworkUtil();
            }
        }
        return sInstance;
    }

    public OkHttpClient getHttpClient(){
        return mHttpClient;
    }

    public interface OnResponseListener{
        void onSuccess(JSONObject jsonObject);
    }

    public void getCurrentlyPlayingMovies(final AppCompatActivity activity,
                                          final OnResponseListener listener){

        Request request = new Request.Builder()
                .url(NOW_PLAYING_ENDPOINT.toString())
                .tag(MOVIE_DATA)
                .build();

        OkHttpClient client = getHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.body() != null) {
                        String responseData = response.body().string();
                        final JSONObject json = new JSONObject(responseData);
                       // Log.d(TAG,"response received: " + json);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(listener !=null)
                                    listener.onSuccess(json);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cancelMovieCall(){
        Log.d("RESPONSE","cancelMovie called");
        OkHttpClient client = getHttpClient();
        for(Call call : client.dispatcher().queuedCalls()) {
            if(call.request().tag().equals(MOVIE_DATA)) {
                Log.d(TAG, "cancelling call");
                call.cancel();
            }
        }
        for(Call call : client.dispatcher().runningCalls()) {
            if(call.request().tag().equals(MOVIE_DATA)){
                Log.d(TAG,"cancelling call");
                call.cancel();
            }
        }

        client.dispatcher().executorService().shutdown();
    }




}
