package cn.edu.heuet.pagingdemofinal_java.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cn.edu.heuet.pagingdemofinal_java.model.Repo;

/**
 * @Description Json反序列化第一层
 *
 * 为了能让 GitHubClient.java 直接使用该类的属性
 * 这里使用了public修饰符，省去写Getter、Setter了
 */
public class RepoSearchResponse {

    @SerializedName("total_count")
    public int total_count = 0;

    @SerializedName("items")
    public List<Repo> items = new ArrayList<>();

    @SerializedName("incomplete_results")
    public boolean incomplete_results = false;
}
