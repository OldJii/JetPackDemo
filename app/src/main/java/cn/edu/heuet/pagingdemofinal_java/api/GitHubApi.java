package cn.edu.heuet.pagingdemofinal_java.api;

import cn.edu.heuet.pagingdemofinal_java.model.RepoSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubApi {
    /**
     * Get repos ordered by stars.
     * 注意这里泛型是：RepoSearchResponse
     * 不要和model包下的RepoSearchResult搞混了（我开始的时候就搞混了 [尴尬.png]）
     * RepoSearchResponse 是为了Json反序列化而写的
     * 而RepoSearchResult 是为了返回给UI使用PagedList、networkErrors数据而写的
     */
    @GET("search/repositories?sort=stars")
    Call<RepoSearchResponse> searchRepos(
            @Query("q") String query,
            @Query("page") int page,
            @Query("per_page") int itemsPerPage
    );
}

