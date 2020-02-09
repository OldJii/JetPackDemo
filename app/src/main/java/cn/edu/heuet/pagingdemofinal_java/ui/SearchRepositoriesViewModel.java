package cn.edu.heuet.pagingdemofinal_java.ui;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import cn.edu.heuet.pagingdemofinal_java.data.GithubDataSource;
import cn.edu.heuet.pagingdemofinal_java.model.Repo;
import cn.edu.heuet.pagingdemofinal_java.model.RepoSearchResult;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SearchRepositoriesViewModel extends AndroidViewModel {
    private GithubDataSource githubDataSource;
    private MutableLiveData<String> queryLiveData = new MutableLiveData<String>();

    /**
     * 构造方法
     *
     * @param application
     * @param githubDataSource
     */
    public SearchRepositoriesViewModel(@NonNull Application application, GithubDataSource githubDataSource) {
        super(application);
        this.githubDataSource = githubDataSource;
    }

    // Activity.onCreate()中调用
    // 将用户输入的String转为LiveDat<String>
    public void searchRepo(String queryString) {
        queryLiveData.postValue(queryString);
    }


    //****************************************************************************************
    //** 要理解的是：利用Transformations.map()来实现转换对LiveData是有粘黏性、连续性的
    //** 你可以点进map看一下，转化的两者间有观察和被观察的关系（与Activity中观察ViewModel中的LiveData是一样的）
    //** 也就是说，外部观察的是repos，会被主动修改的是queryLiveData，但当queryLiveData被修改时，repos自动被同步修改，进而自动调用外部的onchange()方法，刷新数据
    //****************************************************************************************
    // 传入LiveData<String> 拿到查询结果
    private LiveData<RepoSearchResult> repoResult = Transformations.map(
            queryLiveData,
            new Function<String, RepoSearchResult>() {
                @Override
                public RepoSearchResult apply(String it) {
                    return githubDataSource.search(it);
                }
            }
    );

    // 取出repoResult中的PagedList
    public LiveData<PagedList<Repo>> repos = Transformations.switchMap(
            repoResult,
            new Function<RepoSearchResult, LiveData<PagedList<Repo>>>() {
                @Override
                public LiveData<PagedList<Repo>> apply(RepoSearchResult it) {
                    return it.data;
                }
            }
    );

    // 取出repoResult中的networkErrors
    public LiveData<String> networkErrors = Transformations.switchMap(
            repoResult,
            new Function<RepoSearchResult, LiveData<String>>() {
                @Override
                public LiveData<String> apply(RepoSearchResult it) {
                    return it.networkErrors;
                }
            }
    );

    // Activity.onSaveInstanceState()中调用
    public String lastQueryValue() {
        return queryLiveData.getValue();
    }


}
