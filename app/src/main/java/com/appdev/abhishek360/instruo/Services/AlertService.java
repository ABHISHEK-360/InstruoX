package com.appdev.abhishek360.instruo.Services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class AlertService {
    private Context context;

    public AlertService(Context context) {
        this.context = context;
    }

    public void showToast(String msg){
        Handler handler = new Handler(Looper.getMainLooper());

        Thread thread = new Thread(){
            public void run(){
                handler.post((Runnable) () ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                );

            }
        };
        thread.start();
    }

    public void showAlert(String title, String msg){
        Handler handler = new Handler(Looper.getMainLooper());

        Thread thread = new Thread(){
            public void run(){
                handler.post((Runnable) () ->{
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(title);
                        builder.setCancelable(false);
                        builder.setMessage(msg);
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                );
            }
        };
        thread.start();
    }
}
