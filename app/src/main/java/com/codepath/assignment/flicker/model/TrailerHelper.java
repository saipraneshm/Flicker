package com.codepath.assignment.flicker.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by saip92 on 9/17/2017.
 */

public class TrailerHelper {

    private volatile static TrailerHelper sInstance;

    private HashMap<String,List<TrailerData>> mTrailers;

    private TrailerHelper(){
        mTrailers = new HashMap<>();
    }

    public static TrailerHelper getInstance(){
        if(sInstance == null){
            synchronized (TrailerHelper.class){
                if(sInstance == null){
                    sInstance = new TrailerHelper();
                }
            }
        }
        return sInstance;
    }

    public void addTrailerInfo(String movieId, List<TrailerData> trailers){
        mTrailers.put(movieId,trailers);
    }

    public List<TrailerData> getTrailerInfoList(String movieId){
        if(mTrailers.containsKey(movieId)){
            return mTrailers.get(movieId);
        }
        return null;
    }

    public TrailerData getFirstAvailableTrailerInfo(String movieId){
        if(mTrailers.containsKey(movieId)){
            if(mTrailers.get(movieId).size() > 0) {
                return mTrailers.get(movieId).get(0);
            }
        }
        return null;
    }


    public void clearAllTrailers(){
        mTrailers.clear();
    }
}
