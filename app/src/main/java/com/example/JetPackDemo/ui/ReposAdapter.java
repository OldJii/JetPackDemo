package com.example.JetPackDemo.ui;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.JetPackDemo.model.Repo;

public class ReposAdapter extends PagedListAdapter<Repo, RepoViewHolder> {
    public ReposAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Repo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Repo>() {
                @Override
                public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
                    return oldItem.id == newItem.id;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RepoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        //先获取数据
        Repo repo = getItem(position);

        if (repo != null) {
            holder.bindViewWithRepo(repo);
        }
    }
}
