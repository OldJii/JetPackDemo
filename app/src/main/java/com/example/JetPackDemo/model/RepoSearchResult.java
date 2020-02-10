package com.example.JetPackDemo.model;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

/**
 * RepoSearchResponse 是为了Json反序列化而写的
 * 这里是为了返回给UI使用PagedList、networkErrors数据而写的
 */
public class RepoSearchResult {
    public LiveData<PagedList<Repo>> data;
    public LiveData<String> networkErrors;

    public RepoSearchResult(LiveData<PagedList<Repo>> data, LiveData<String> networkErrors) {
        this.data = data;
        this.networkErrors = networkErrors;
    }
}
