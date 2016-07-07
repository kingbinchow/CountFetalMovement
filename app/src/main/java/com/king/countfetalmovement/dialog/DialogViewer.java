package com.king.countfetalmovement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.countfetalmovement.R;
import com.king.countfetalmovement.StringUtil;


/**
 * Created by lucy.chen on 16/1/18.
 */
public class DialogViewer {

    public static final int DIALOG_TITLE_STYLE_NORMAL = 0;//不显示图标
    public static final int DIALOG_TITLE_STYLE_SUCCESS = 1;//title显示打钩符号
    public static final int DIALOG_TITLE_STYLE_ATTENTION = 2;// title显示叹号
    public static final int DIALOG_TITLE_STYLE_ERROR = 3;//title显示叉

    private Context context;
    private Dialog dialog;
    private TextView titleTextView;
    private View titleLayout;
    private TextView contentView;
    private ImageView titleImage;
    private Button leftButtonView;
    private Button rightButtonView;
    private Button submitButtonView;
    private View oneView;
    private View multiView;

    private String title;
    private int titleStyle;
    private String content;
    private Spanned contentSpanned;
    private String submitButtonText;
    private String leftButtonText;
    private String rightButtonText;
    private DialogListener dialogListener;
    private boolean isLeftActive;
    private boolean isRightActive;


    /**
     *
     *
     * @param context
     * @param title
     * @param dialogListener
     */
    public DialogViewer(Context context, String title, DialogListener dialogListener) {
        this(context, title, DIALOG_TITLE_STYLE_ATTENTION, null, context.getString(R.string.cancel), false, context.getString(R.string.confirm), true, dialogListener);
    }

    public Dialog getDialog() {
        return dialog;
    }

    /**
     *
     *
     * @param context
     * @param title
     * @param leftButtonText
     * @param rightButtonText
     * @param dialogListener
     */
    public DialogViewer(Context context, String title, String leftButtonText, String rightButtonText, DialogListener dialogListener) {
        this(context, title, DIALOG_TITLE_STYLE_ATTENTION, null, leftButtonText, false, rightButtonText, true, dialogListener);
    }


    /**
     *
     */
    public DialogViewer(Context context, String title, int titleStyle, String content, String submitButtonText, DialogListener dialogListener) {
        this(context, title, titleStyle, content, null, false, null, false, submitButtonText, dialogListener);
    }

    /**
     *
     */
    public DialogViewer(Context context, String title, int titleStyle, String content, String leftButtonText, String rightButtonText, DialogListener dialogListener) {
        this(context, title, titleStyle, content, leftButtonText, false, rightButtonText, true, dialogListener);
    }


    /**
     *
     *
     * @param context
     * @param title
     * @param titleStyle
     * @param content
     * @param leftButtonText
     * @param isLeftActive
     * @param rightButtonText
     * @param isRightActive
     * @param dialogListener
     */
    public DialogViewer(Context context, String title, int titleStyle, String content, String leftButtonText, boolean isLeftActive, String rightButtonText, boolean isRightActive, DialogListener dialogListener) {
        this.context = context;
        this.title = title;
        this.titleStyle = titleStyle;
        this.content = content;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
        this.dialogListener = dialogListener;
        this.isLeftActive = isLeftActive;
        this.isRightActive = isRightActive;

        initView();
    }


    public DialogViewer(Context context, String title, int titleStyle, String leftButtonText, boolean isLeftActive, String rightButtonText, boolean isRightActive, DialogListener dialogListener, Spanned content) {
        this.context = context;
        this.title = title;
        this.titleStyle = titleStyle;
        this.contentSpanned = content;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
        this.dialogListener = dialogListener;
        this.isLeftActive = isLeftActive;
        this.isRightActive = isRightActive;

        if (!StringUtil.isNull(leftButtonText) && StringUtil.isNull(rightButtonText)) {
            this.submitButtonText = leftButtonText;
            this.leftButtonText = null;
            this.rightButtonText = null;
        }

        if (!StringUtil.isNull(rightButtonText) && StringUtil.isNull(leftButtonText)) {
            this.submitButtonText = rightButtonText;
            this.leftButtonText = null;
            this.rightButtonText = null;
        }

        initView();
    }

    public DialogViewer(Context context, String title, int titleStyle, String content, String leftButtonText, boolean isLeftActive, String rightButtonText, boolean isRightActive, String submitButtonText, DialogListener dialogListener) {
        this.context = context;
        this.title = title;
        this.titleStyle = titleStyle;
        this.content = content;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
        this.dialogListener = dialogListener;
        this.isLeftActive = isLeftActive;
        this.isRightActive = isRightActive;
        this.submitButtonText = submitButtonText;

        initView();
    }

    public void setCancelable(boolean flag) {
        if (dialog != null) {
            dialog.setCancelable(flag);
        }
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = (LinearLayout) inflater.inflate(R.layout.submit_dialog, null);

        dialog = new Dialog(context, R.style.dialog);
        dialog.getWindow().setContentView(dialogView);

        initTitle();
        initContent();
        initButton();
    }


    private void initTitle() {
        //
        titleLayout = dialog.findViewById(R.id.title_layout);
        titleLayout.setVisibility(View.GONE);
        titleImage = (ImageView) dialog.findViewById(R.id.title_icon);
        titleImage.setVisibility(View.GONE);
        titleTextView = (TextView) dialog.findViewById(R.id.title_text);
        if (!StringUtil.isNull(title)) {
            titleLayout.setVisibility(View.VISIBLE);
            titleTextView.setText(title);

            //�б���û���ݣ�marginTop��marginBottom��Ҫ����
            if (StringUtil.isNull(content) && contentSpanned == null) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleLayout.getLayoutParams();
                if (params != null) {
                    int top = StringUtil.dip2px(40,context);
                    int bottom = StringUtil.dip2px(40,context);
                    params.topMargin = top;
                    params.bottomMargin = bottom;
                    titleLayout.setLayoutParams(params);
                }
            }

            switch (titleStyle) {
                case DIALOG_TITLE_STYLE_NORMAL:
                    break;

                case DIALOG_TITLE_STYLE_SUCCESS:
                    titleImage.setVisibility(View.VISIBLE);
                    titleTextView.setTextColor(context.getResources().getColor(R.color.dialog_title_green_color));
                    titleImage.setImageResource(R.drawable.icon_success_green);
                    break;

                case DIALOG_TITLE_STYLE_ATTENTION:
                    titleImage.setVisibility(View.VISIBLE);
                    titleTextView.setTextColor(context.getResources().getColor(R.color.app_text_black));
//                    titleImage.setImageResource(R.drawable.icon_attention_purple);
                    break;

                case DIALOG_TITLE_STYLE_ERROR:
                    titleImage.setVisibility(View.VISIBLE);
                    titleTextView.setTextColor(context.getResources().getColor(R.color.dialog_title_red_color));
                    titleImage.setImageResource(R.drawable.icon_wrong_purple);
                    break;
            }
        }
    }

    private void initContent() {
        //dialog����
        contentView = (TextView) dialog.findViewById(R.id.content_view);
        contentView.setVisibility(View.GONE);
        if (!StringUtil.isNull(content) || contentSpanned != null) {
            contentView.setVisibility(View.VISIBLE);

            if (contentSpanned != null) {
                contentView.setText(contentSpanned);
            }

            if (!StringUtil.isNull(content)) {
                contentView.setText(content);
            }

            //������û�б���
            if (StringUtil.isNull(title)) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
                if (params != null) {
                    int top = StringUtil.dip2px(24,context);
                    int bottom = StringUtil.dip2px(24,context);
                    params.topMargin = top;
                    params.bottomMargin = bottom;
                    contentView.setLayoutParams(params);
                }

                contentView.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }

    private void initButton() {
        multiView = dialog.findViewById(R.id.multi_button);
        leftButtonView = (Button) dialog.findViewById(R.id.left_button);
        if (leftButtonText != null) {
            leftButtonView.setText(leftButtonText);

            if (isLeftActive) {
                leftButtonView.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                leftButtonView.setTextColor(context.getResources().getColor(R.color.black));
            }

            leftButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialogListener != null) {
                        dialogListener.onDialogClick(dialog, true, false);
                    }

                }
            });
        }

        rightButtonView = (Button) dialog.findViewById(R.id.right_button);
        if (rightButtonText != null) {
            rightButtonView.setText(rightButtonText);
            if (isRightActive) {
                rightButtonView.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                rightButtonView.setTextColor(context.getResources().getColor(R.color.black));
            }

            rightButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialogListener != null) {
                        dialogListener.onDialogClick(dialog, false, true);
                    }

                }
            });
        }

        oneView = dialog.findViewById(R.id.one_button);
        submitButtonView = (Button) dialog.findViewById(R.id.submit_button);
        if (submitButtonText != null) {
            multiView.setVisibility(View.GONE);
            oneView.setVisibility(View.VISIBLE);
            submitButtonView.setText(submitButtonText);

            submitButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (dialogListener != null) {
                        dialogListener.onDialogClick(dialog, true, true);
                    }
                }
            });
        }

    }
}
