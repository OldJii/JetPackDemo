package com.example.JetPackDemo.db;

import com.example.JetPackDemo.data.RepoBoundaryCallback;

public class CRUDApiImpl implements CRUDApi {
    @Override
    public void insertFinished(String errorMsg) {
        RepoBoundaryCallback.networkErrors.postValue(errorMsg);
        RepoBoundaryCallback.isRequestInProgress = false;
    }
}
