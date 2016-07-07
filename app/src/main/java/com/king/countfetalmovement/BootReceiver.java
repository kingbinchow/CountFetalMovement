package com.king.countfetalmovement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by king.zhou on 2016/7/6.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.king.countfetalmeovement.destroy")) {
            //TODO
            //在这里写重新启动service的相关操作

            Intent pushIntent = new Intent(context, CountService.class);
            Bundle bundle = new Bundle();
            bundle.putLong("startTime",intent.getExtras().getLong("startTime"));
            bundle.putInt("amount",intent.getExtras().getInt("amount"));
            Log.i("TB","receive "+intent.getExtras().getLong("startTime"));
            Log.i("TB","receive "+intent.getExtras().getInt("amount"));
            pushIntent.putExtras(bundle);
            context.startService(pushIntent);
        }

    }

}