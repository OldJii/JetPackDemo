package com.example.JetPackDemo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "repos")
public class Repo {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "full_name")
    public String full_name;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "html_url")
    public String html_url;
    @ColumnInfo(name = "stargazers_count")
    public int stargazers_count;
    @ColumnInfo(name = "forks_count")
    public int forks_count;
    @ColumnInfo(name = "language")
    public String language;
}
