package com.wardrobe.www;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Wardrobe/";
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        //字体黑色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        File file = new File(PATH);
        boolean isDirectoryCreated = file.exists();
        if (!isDirectoryCreated) {//文件夹不存在
            file.mkdirs();
            Log.d(TAG, "PATH= " + PATH);
        } else {//文件夹存在
            writeSDFile(df.format(new Date()), "log.txt");//写入文件夹创建时间，即APP安装时间
        }

    }

    private void setStatusBarColor() {
        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void writeSDFile(String str, String fileName) {//写入文件
        try {
            File f = new File(PATH + fileName);
            if (!f.exists()) {
                FileWriter fw = new FileWriter(PATH + fileName);
                fw.write(str);
                FileOutputStream os = new FileOutputStream(f);
                DataOutputStream out = new DataOutputStream(os);
                out.writeShort(2);
                out.writeUTF("");
                Log.d(TAG, out.toString());
                fw.flush();
                fw.close();
                Log.d(TAG, fw.toString());
            } else {
                Log.d(TAG, f.toString() + " is already exist!");
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

}
