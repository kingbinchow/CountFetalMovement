package com.king.countfetalmovement;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by king.zhou on 2016/6/27.
 */
public class Utils {
    //本方法判断自己些的一个Service-->com.android.controlAddFunctions.PhoneService是否已经运行
    public static boolean isWorked(Context context) {
        ActivityManager myManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for(int i = 0 ; i<runningService.size();i++)
        {
            if(runningService.get(i).service.getClassName().toString().equals("com.king.countfetalmovement.CountService"))
            {
                return true;
            }
        }
        return false;
    }

   public static void saveRecordBean(Context context,long startTime, int amount){

        MyApplication.stopService =true;

        Intent pushIntent = new Intent(context, CountService.class);
        context.stopService(pushIntent);

        long endTime = System.currentTimeMillis();
        if(endTime-startTime<60000*3){
            Toast.makeText(context,"不足三分钟将不会记录",Toast.LENGTH_SHORT).show();
            return ;
        }
        DbUtils dbUtils = DBConfig.getDbUtils(context);
        MovementRecordBean bean  = new MovementRecordBean();
        String startTimeStr = DateTimeUtils.convertUnixTimeStampToNormalTime(startTime,"yyyy/MM/dd HH:mm:ss");
        String durationStr = (endTime-startTime)/60000 +"min";

        bean.date = startTimeStr;
        bean.duration = "记录时长"+durationStr;
        bean.moveAmount = amount+"次";

        try {
            dbUtils.saveOrUpdate(bean);
        } catch (DbException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MsgUpdateEvent("update"));
        Log.i("KING&TB", startTimeStr);
    }


}
