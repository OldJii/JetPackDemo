package com.example.JetPackDemo.api;

import java.util.List;

import com.example.JetPackDemo.model.Repo;

public interface OnResponse {
    public void onSuccess(List<Repo> repos);

    public void onError(String errorMsg);

    public void onFailure(String failureMsg);
}

