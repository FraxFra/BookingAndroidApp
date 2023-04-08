package com.example.bookingandroidapp.Connections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Executor<T> {

    private final TypeToken<Result<T>> responseType;
    private Result<T> response;

    public Executor(TypeToken<Result<T>> responseType) {
        this.responseType = responseType;
    }

    public void execute(String json) {
        this.response = new Gson().fromJson(json, responseType.getType());
    }

    public Result<T> getResponse() { return this.response; }

}