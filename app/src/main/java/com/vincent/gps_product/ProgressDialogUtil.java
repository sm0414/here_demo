package com.vincent.gps_product;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class ProgressDialogUtil {
    private static AlertDialog mAlertDialog;

    /**
     * 彈出耗時對話方塊
     * @param context
     */
    public static void showProgressDialog(Context context) {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(context, R.style.CustomProgressDialog).create();
        }

        View loadView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_view, null);
        mAlertDialog.setView(loadView);
        mAlertDialog.setCancelable(false);


        //TextView tvTip = loadView.findViewById(R.id.tvTip);
        //tvTip.setText("註冊中...");

        mAlertDialog.show();
    }

    public static void showProgressDialog(Context context, String tip) {

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(context, R.style.CustomProgressDialog).create();
        }

        View loadView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_view, null);
        mAlertDialog.setView(loadView);
        mAlertDialog.setCancelable(false);

        TextView tvTip = loadView.findViewById(R.id.tvTip);
        tvTip.setText(tip);

        mAlertDialog.show();
    }

    /**
     * 隱藏耗時對話方塊
     */
    public static void dismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }
}