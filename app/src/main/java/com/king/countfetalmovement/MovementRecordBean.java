package com.king.countfetalmovement;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by king.zhou on 2016/6/23.
 */

@Table(name="MovementRecordBean")
public class MovementRecordBean implements Serializable {

    @Id(column="date")
    String date;
    String duration;
    String moveAmount;
}
