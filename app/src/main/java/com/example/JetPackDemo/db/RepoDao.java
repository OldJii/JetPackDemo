package com.example.JetPackDemo.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.example.JetPackDemo.model.Repo;

@Dao
public interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<Repo> repos);

    @Query("SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE " +
            ":queryString) ORDER BY stargazers_count DESC, name ASC")
    public DataSource.Factory<Integer, Repo> reposByName(String queryString);
}
