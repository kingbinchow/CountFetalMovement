package com.king.countfetalmovement;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

/**
 * Created by king.zhou on 2016/6/24.
 */
public class DBConfig {
    public static String name = "cfm_db";
    public static int version = 1;
    public static String getDBName() {
        return name;
    }

    public static DbUtils getDbUtils(final Context context) {
        return DbUtils.create(context, getDBName(), version,
                new DbUtils.DbUpgradeListener() {

                    @Override
                    public void onUpgrade(DbUtils arg0, int arg1,
                                          int arg2) {

                    }

                });
    }
}
