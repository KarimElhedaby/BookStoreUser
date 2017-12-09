package com.hamza.alif.bookstoreuser.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
import android.widget.ProgressBar;

import com.hamza.alif.bookstoreuser.R;


public class Utilities {

    private static final String TAG = "Utilities";

    static Dialog progressDialog;


    public static void showLoadingDialog(Context context, int color) {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loding_dialog);
            ProgressBar progressBar= progressDialog.findViewById(R.id.progressid);
            progressBar.getIndeterminateDrawable()
                    .setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            Window window = progressDialog.getWindow();
            if (window != null)
                progressDialog.getWindow().setBackgroundDrawable
                        (new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissLoadingDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
