package com.example.JetPackDemo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Executors;

import com.example.JetPackDemo.data.GithubDataSource;
import com.example.JetPackDemo.db.GithubLocalCache;
import com.example.JetPackDemo.db.RepoDatabase;
import com.example.JetPackDemo.model.Repo;
import com.example.JetPackDemo.ui.ReposAdapter;
import com.example.JetPackDemo.ui.SearchRepositoriesViewModel;
import com.example.JetPackDemo.ui.ViewModelFactory;

public class SearchRepositoriesActivity extends AppCompatActivity {
    private static final String LAST_SEARCH_QUERY = "last_search_query";
    private static final String DEFAULT_QUERY = "Android";

    private EditText search_repo;
    private RecyclerView list;
    private TextView emptyList;

    private ReposAdapter adapter;
    private SearchRepositoriesViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_repositories);
        initView();
        adapter = new ReposAdapter();
        // 得到ViewModel（最关键的代码都在ViewModel中）
        viewModel = ViewModelProviders.of(this, provideViewModelFactory(getApplicationContext()))
                .get(SearchRepositoriesViewModel.class);
        // add dividers between RecyclerView's row items
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        list.addItemDecoration(decoration);
        // 初始化适配器
        initAdapter();
        String query = null;
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(LAST_SEARCH_QUERY);
        }
        if (TextUtils.isEmpty(query)) {
            query = DEFAULT_QUERY;
        }
        // 进行搜索
        viewModel.searchRepo(query);
        initSearch(query);
    }

    private void initView() {
        search_repo = findViewById(R.id.search_repo);

        list = findViewById(R.id.list);
        emptyList = findViewById(R.id.emptyList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initAdapter() {
        list.setAdapter(adapter);
        // 观察 repos
        viewModel.repos.observe(this, new Observer<PagedList<Repo>>() {
            @Override
            public void onChanged(PagedList<Repo> repos) {
                Log.d("Activity", "list: " + repos.size());
                showEmptyList(repos.size() == 0);
                adapter.submitList(repos);
            }
        });
        // 观察 networkErrors
        viewModel.networkErrors.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(SearchRepositoriesActivity.this, "\uD83D\uDE28 Wooops " + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initSearch(String query) {
        search_repo.setText(query);
        // 软键盘回车键
        search_repo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateRepoListFromInput();
                    return true;
                }
                return false;
            }
        });
        // 电脑键盘回车键
        search_repo.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateRepoListFromInput();
                    return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateRepoListFromInput() {
        if (!TextUtils.isEmpty(search_repo.getText().toString().trim())) {
            list.scrollToPosition(0);
            viewModel.searchRepo(search_repo.getText().toString());
            adapter.submitList(null);
        }
    }

    private void showEmptyList(Boolean show) {
        if (show) {
            emptyList.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 构建ViewModelFactory
     * <p>
     * 通过自定义ViewModelProvider.Factory的方式来向ViewModel中传递额外参数，如果不这样无法实现
     *
     * @param context
     * @return
     */
    private ViewModelProvider.Factory provideViewModelFactory(Context context) {
        /*
        单线程化线程池(newSingleThreadExecutor)的优点: 串行执行所有任务。
        如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它。
        此线程池保证**并发执行时**所有任务的执行顺序按照任务的提交顺序执行。
         */
        //RepoDatabase -> GithubLocalCache -> GithubDataSource -> ViewModelFactory

        RepoDatabase repoDatabase = RepoDatabase.getInstance(context);
        GithubLocalCache githubLocalCache = GithubLocalCache.getInstance(Executors.newSingleThreadExecutor(), repoDatabase.reposDao());
        GithubDataSource githubDataSource = new GithubDataSource(githubLocalCache);
        ViewModelFactory viewModelFactory = new ViewModelFactory(getApplication(), githubDataSource);

        return viewModelFactory;
    }
}













