package com.temoa.startor2.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.temoa.startor2.MyApp;
import com.temoa.startor2.beans.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/24 19:22
 */

public class TaskDBController {

    public static String TABLE_NAME = "task";
    private SQLiteDatabase db;

    public TaskDBController() {
        TasksManagerDBOpenHelper openHelper = MyApp.getTasksManagerDBOpenHelper();
        db = openHelper.getWritableDatabase();
    }

    /**
     * 获取全部任务
     */
    public List<Task> getAllTasks() {
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        List<Task> list = new ArrayList<>();
        try {
            if (!c.moveToLast()) {
                return list;
            }

            do {
                Task task = new Task();
                task.setId(c.getInt(c.getColumnIndex(Task.ID)));
                task.setName(c.getString(c.getColumnIndex(Task.NAME)));
                task.setUrl(c.getString(c.getColumnIndex(Task.URL)));
                task.setPath(c.getString(c.getColumnIndex(Task.PATH)));
                list.add(task);
            } while (c.moveToPrevious());
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return list;
    }

    /**
     * 添加一个任务
     *
     * @param name 任务名称
     * @param url  任务链接
     * @param path 任务路径
     */
    public Task addTask(String name, String url, String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path) || TextUtils.isEmpty(name))
            return null;

        // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
        int id = FileDownloadUtils.generateId(url, path);

        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setUrl(url);
        task.setPath(path);

        boolean succeed = db.insert(TABLE_NAME, null, task.toContentValues()) != -1;
        return succeed ? task : null;
    }

    /**
     * 删除一个任务
     *
     * @param id 任务ID
     * @return 是否删除成功
     */
    public boolean removeTask(int id) {
        String idStr = String.valueOf(id);
        return db.delete(TABLE_NAME, "id = ?", new String[]{idStr}) != 0;
    }

    /**
     * 删除所有的任务
     */
    public boolean releaseTasks() {
        return db.delete(TABLE_NAME, null, null) != 0;
    }

    public static class TasksManagerDBOpenHelper extends SQLiteOpenHelper {
        public static String DATABASE_NAME = "tasks.db";
        public static int DATABASE_VERSION = 2;

        public TasksManagerDBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TaskDBController.TABLE_NAME
                    + String.format(
                    "("
                            + "%s INTEGER PRIMARY KEY, " //id
                            + "%s VARCHAR, " // name
                            + "%s VARCHAR, " // url
                            + "%s VARCHAR " // path
                            + ")"
                    , Task.ID
                    , Task.NAME
                    , Task.URL
                    , Task.PATH

            ));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                db.delete(TaskDBController.TABLE_NAME, null, null);
            }
        }
    }
}
