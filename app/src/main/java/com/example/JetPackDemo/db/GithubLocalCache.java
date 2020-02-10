package com.example.JetPackDemo.db;

import android.util.Log;

import androidx.paging.DataSource;

import java.util.List;
import java.util.concurrent.Executor;

import com.example.JetPackDemo.model.Repo;

/**
 * - 引入线程池 + 封装dao方法
 */
public class GithubLocalCache {

    private Executor ioExecutor;
    private RepoDao repoDao;
    private static GithubLocalCache mInstance;

    public static synchronized GithubLocalCache getInstance(Executor ioExecutor, RepoDao repoDao) {
        if (mInstance == null) {
            mInstance = new GithubLocalCache(ioExecutor, repoDao);
        }
        return mInstance;
    }

    public GithubLocalCache(Executor ioExecutor, RepoDao repoDao) {
        this.ioExecutor = ioExecutor;
        this.repoDao = repoDao;
    }

    public void insert(final List<Repo> repos) {
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("GithubLocalCache", "插入 " + repos.size() + " 条数据");
                try {
                    repoDao.insert(repos);
                } catch (Exception e) {
                    CRUDApi crudApi = new CRUDApiImpl();
                    crudApi.insertFinished("GithubLocalCache: Insert Error");
                }
            }
        });
    }

    public DataSource.Factory<Integer, Repo> reposByName(String name) {
        String query = "%" + name.replace(' ', '%') + "%";
        return repoDao.reposByName(query);
    }
}
