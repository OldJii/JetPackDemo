package cn.edu.heuet.pagingdemofinal_java.api;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import java.util.List;
import java.util.concurrent.Executors;

import cn.edu.heuet.pagingdemofinal_java.data.RepoBoundaryCallback;
import cn.edu.heuet.pagingdemofinal_java.db.GithubLocalCache;
import cn.edu.heuet.pagingdemofinal_java.db.RepoDatabase;
import cn.edu.heuet.pagingdemofinal_java.model.Repo;

public interface OnResponse {
    public void onSuccess(List<Repo> repos);

    public void onError(String errorMsg);

    public void onFailure(String failureMsg);
}

