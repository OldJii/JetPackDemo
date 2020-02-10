package com.example.JetPackDemo.ui;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.JetPackDemo.data.GithubDataSource;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private GithubDataSource githubDataSource;
    private Application application;

    /*
    构造Factory
     */
    public ViewModelFactory(Application application, GithubDataSource githubDataSource) {
        this.application = application;
        this.githubDataSource = githubDataSource;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel.class)) {
            return (T) new SearchRepositoriesViewModel(application, githubDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
