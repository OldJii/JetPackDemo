package com.example.JetPackDemo.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.example.JetPackDemo.api.GitHubClient;
import com.example.JetPackDemo.db.GithubLocalCache;
import com.example.JetPackDemo.model.Repo;

import static com.example.JetPackDemo.config.ConstantConfig.NETWORK_PAGE_SIZE;

public class RepoBoundaryCallback extends PagedList.BoundaryCallback<Repo> {
    private String query;

    private static final String TAG = "RepoBoundaryCallback";

    public static int lastRequestedPage = 1;
    public static boolean isRequestInProgress = false;

    public static boolean hasMore = true;

    // LiveData of network errors.
    public static MutableLiveData<String> networkErrors = new MutableLiveData<String>();

    private GithubLocalCache cache;

    public RepoBoundaryCallback(GithubLocalCache cache, String query) {
        this.cache = cache;
        this.query = query;
    }

    /*
        一开始，PagedList为空，RecyclerView想要渲染数据时，触发此方法
        相当于初始化数据，获取第一页数据
     */
    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded");
        requestAndSaveData(query);
    }

    /*
        RecyclerView渲染PagedList中最后一项数据时（不算prefetch），触发此方法
        相当于上拉加载
    */
    @Override
    public void onItemAtEndLoaded(Repo itemAtEnd) {
        Log.d(TAG, "onItemAtEndLoaded");
        Log.d(TAG, "query：" + query);
        requestAndSaveData(query);
    }

    /*
    ViewModel请求
     */
    public void requestMore(String query) {
        requestAndSaveData(query);
    }

    private void requestAndSaveData(String query) {
        // 避免同一时刻重复请求
        if (isRequestInProgress) return;
        isRequestInProgress = true;
        // retrofit对象实例化，请求网络数据并插入到数据库中
        GitHubClient.getInstance().searchRepos(
                cache,
                query,
                lastRequestedPage,
                NETWORK_PAGE_SIZE
        );
        Log.d(TAG, "NETWORK_PAGE_SIZE： " + NETWORK_PAGE_SIZE);
    }

}
