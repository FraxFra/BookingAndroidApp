package com.example.bookingandroidapp.Connections;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Result<T> {
    public boolean ok;
    public String error;
    public ArrayList<T> data;

    public Result(boolean ok, String error, ArrayList<T> data){
        this.ok = ok;
        this.error = error;
        this.data = data;
    }

    public Result(){}

    public static <X> Result<X> getFromJson(String json, TypeToken<Result<X>> t){
        Executor<X> executor = new Executor<X>(t);
        executor.execute(json);
        return executor.getResponse();
    }
}
