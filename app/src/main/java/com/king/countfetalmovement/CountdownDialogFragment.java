package com.king.countfetalmovement;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.countfetalmovement.dialog.DialogListener;
import com.king.countfetalmovement.dialog.DialogViewer;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by king.zhou on 2016/6/23.
 */
public class CountdownDialogFragment extends DialogFragment
{
    private static final String TAG = "KING&TB" ;
    private TextView time,amountTv,startTimeTv;
    private ImageView stopCountIv;
    private CircularSeekBar seekbar;
    private int values = 1;
//    private Handler mHandler = new Handler();

    private long startTime;
    private long endTime;

    private int amount = 0;
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//
//        }
//    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utils.isWorked(getActivity())){
//            getDatafromService();

        }else{
            Intent pushIntent = new Intent(getActivity(), CountService.class);
            getActivity().startService(pushIntent);
        }
        EventBus.getDefault().register(this);
    }


    public void getDatafromService() {
        values = 0;
        amount = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_count_down, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        amountTv = (TextView) view.findViewById(R.id.tv_amount);
        startTimeTv = (TextView) view.findViewById(R.id.tv_start_time);
        if (amount != 0) {
            amountTv.setText(""+amount);
        }
        seekbar = (CircularSeekBar) view.findViewById(R.id.circularSeekBar1);
        time = (TextView) view.findViewById(R.id.time);
//        mHandler.postDelayed(runnable, 1000);// 每一秒执行一次runnable.

        stopCountIv = (ImageView) view.findViewById(R.id.iv_close);
        stopCountIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogViewer dialogViewer = new DialogViewer(getActivity(), "提示", DialogViewer.DIALOG_TITLE_STYLE_NORMAL, "你将终止此次记录？", "取消", "确定", new DialogListener() {
                    @Override
                    public void onDialogClick(Dialog dialog, boolean isLeftButonClick, boolean isRightButtonClick) {
                        if (isLeftButonClick) {
                            dialog.dismiss();
                        }
                        if (isRightButtonClick) {
                            Utils.saveRecordBean(getActivity(),startTime,amount);
                            CountdownDialogFragment.this.dismiss();
                        }
                    }
                });
                dialogViewer.show();
            }
        });

        startTime = System.currentTimeMillis();

        return view;
    }


    @Override  //仅用户状态跟踪
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.v(TAG, "CountdownDialogFragment dismiss");
//        Intent pushIntent = new Intent(getActivity(), CountService.class);
//        getActivity().stopService(pushIntent);
        EventBus.getDefault().unregister(this);//反注册EventBus
//        saveRecordBean();
        if(Utils.isWorked(getActivity())){
            Toast.makeText(getActivity(),"已经切换到后台继续记录\n" +
                    "如需停止点击右上角叉号",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(MsgShakeEvent msg){
        String content = msg.getMsg()
                + "\n ThreadName: " + Thread.currentThread().getName()
                + "\n ThreadId: " + Thread.currentThread().getId();
        if("shake".equals(msg.getMsg())){
            amount++;
            amountTv.setText(""+amount);
        }
        if("updateClockUI".equals(msg.getMsg())){
            updateClockUI(msg);
        }
    }

    void updateClockUI(MsgShakeEvent msg){
        values= msg.getValue();
        seekbar.setProgress(values%60);
        long remainingTime = 60*60 - values;
        String minStr = remainingTime / 60 +"";
        String secStr = (remainingTime % 60+"").length()>1?(remainingTime % 60+""):("0"+remainingTime % 60+"");
        time.setText(minStr + ":" + secStr);
        amount = msg.getAmount();
        amountTv.setText(""+amount);
        startTime = msg.getStartTime();
        startTimeTv.setText(""+DateTimeUtils.convertUnixTimeStampToNormalTime(startTime,"yyyy/MM/dd HH:mm:ss"));
        Log.i(TAG+"update",DateTimeUtils.convertUnixTimeStampToNormalTime(startTime,"yyyy/MM/dd HH:mm:ss"));

    }


}