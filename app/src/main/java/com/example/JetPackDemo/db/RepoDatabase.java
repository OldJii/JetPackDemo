package com.example.JetPackDemo.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.JetPackDemo.model.Repo;

/**
 * 注意：
 * 抽象类
 * 里面包含一个抽象方法
 * 两者具体代码room会自动生成
 */
@Database(entities = {Repo.class}, version = 1, exportSchema = false)
public abstract class RepoDatabase extends RoomDatabase {

    public abstract RepoDao reposDao();

    private static RepoDatabase instance;

    private static final String TAG = "RepoDatabase";

    private static RepoDatabase buildDatabase(Context context) {
        RepoDatabase db = Room.databaseBuilder(context, RepoDatabase.class, "Github.db").build();
        if (db != null){
            Log.i(TAG,"db init success");
        }else{
            Log.i(TAG,"db init fail");
        }
        return db;
    }

    public static synchronized RepoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = buildDatabase(context);
        }
        return instance;
    }
}
