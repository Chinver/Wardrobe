package com.wardrobe.www;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.wardrobe.www.db.DatabaseHelper;
import com.wardrobe.www.service.serviceImpl.IntentService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/9/20.
 */
public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    private String picturePath = Environment.getExternalStorageDirectory().getPath() + "/Wardrobe/";
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }

        File file = new File(picturePath);
        boolean isDirectoryCreated = file.exists();
        if (!isDirectoryCreated) {//文件夹不存在
            file.mkdirs();
            Log.d(TAG, "picturePath= " + picturePath);
        } else {//文件夹存在
            writeSDFile(df.format(new Date()), "log.txt");//写入文件夹创建时间，即APP安装时间
        }

        PushManager.getInstance().initialize(this.getApplicationContext(), null);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);
    }

    private void writeSDFile(String str, String fileName) {//写入文件
        try {
            File f = new File(picturePath + fileName);
            if (!f.exists()) {
                FileWriter fw = new FileWriter(picturePath + fileName);
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
