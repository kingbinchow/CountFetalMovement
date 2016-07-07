package com.king.countfetalmovement.dialog;

import android.app.Dialog;

/**
 *
 */
public interface DialogListener {
    /**
     *如果只有一个按钮，则响应isLeftButtonClick
     * @param dialog
     * @param isLeftButonClick -- 左按钮被按
     * @param isRightButtonClick -- 右按钮被按
     */
    public void onDialogClick(Dialog dialog, boolean isLeftButonClick, boolean isRightButtonClick);

}
