package com.king.countfetalmovement;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.king.countfetalmovement.baseadapter.BaseAdapterHelper;
import com.king.countfetalmovement.baseadapter.QuickAdapter;
import com.king.countfetalmovement.dialog.DialogListener;
import com.king.countfetalmovement.dialog.DialogViewer;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "KING&TB";
    QuickAdapter<MovementRecordBean> adapter;
    private ListView contentLv;
    List<MovementRecordBean> list = null;
    DbUtils dbUtils;

    long firstTimeOfClickBackKey;
    CountdownDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        contentLv = (ListView) findViewById(R.id.lv_content);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountdownDialog();
            }
        });
        EventBus.getDefault().register(this);

        contentLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showDeleteDialog(position);
                return true;
            }
        });

        dbUtils = DBConfig.getDbUtils(this);
        getData();

    }

    private void showDeleteDialog(final int position) {
        DialogViewer dialogViewer = new DialogViewer(this, "提示", DialogViewer.DIALOG_TITLE_STYLE_NORMAL, "你将删掉此项记录？", "取消", "确定", new DialogListener() {
            @Override
            public void onDialogClick(Dialog dialog, boolean isLeftButonClick, boolean isRightButtonClick) {
                if (isLeftButonClick) {
                    dialog.dismiss();
                }
                if (isRightButtonClick) {
                    MovementRecordBean bean = list.get(position);
                    try {
                        dbUtils.delete(bean);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    getData();

                }
            }
        });
        dialogViewer.show();

    }

    public void onEventMainThread(MsgUpdateEvent msg) {
        if (("dismiss").equals(msg.getMsg())) {
            if (dialog.isVisible()) {
                dialog.dismiss();
            }
        } else {
            getData();

        }
    }

    private void getData() {

        Log.i(TAG, "get Data");
        try {
            list = dbUtils.findAll(MovementRecordBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list != null) {
//            if (adapter == null) {
            adapter = new QuickAdapter<MovementRecordBean>(this, R.layout.list_view_content_item, list) {

                @Override
                protected void convert(BaseAdapterHelper helper, MovementRecordBean item, int position) {
                    helper.setText(R.id.tv_list_view_item_date, item.date);
                    helper.setText(R.id.tv_list_view_item_duration, item.duration);
                    helper.setText(R.id.tv_list_view_item_amount, item.moveAmount);
                }
            };
            contentLv.setAdapter(adapter);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showCountdownDialog() {
        dialog = new CountdownDialogFragment();
        dialog.show(getFragmentManager(), "CountdownDialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - firstTimeOfClickBackKey) > 2000) {
                Toast.makeText(this, "再按退出应用", Toast.LENGTH_SHORT).show();
                MyApplication.stopService = true;
                firstTimeOfClickBackKey = System.currentTimeMillis();
                return false;
            } else {
                exitApp();
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public void exitApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //延迟半秒杀进程
                System.exit(0);
                System.gc();

                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                am.killBackgroundProcesses(getPackageName());
            }
        }, 500);
    }

}