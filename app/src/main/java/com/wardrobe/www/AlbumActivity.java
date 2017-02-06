package com.wardrobe.www;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.wardrobe.www.Utils.BaseUtils;
import com.wardrobe.www.adapter.AlbumAdapter;
import com.wardrobe.www.db.DatabaseHelper;
import com.wardrobe.www.model.Clothes;
import com.wardrobe.www.service.serviceImpl.ClothesServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class Name：UpdatePhotoActivity
 * Class Function（Chinese）：用户可在该界面进入拍照模式，也可以选择本地相册中已存在的图片
 * Class Function（English）：1.Taking photo, 2.Choosing photos which are in the local photo album.
 * Created by Summer on 2016/9/22.
 */

public class AlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "AlbumActivity";
    private String photosPath = Environment.getExternalStorageDirectory().getPath() + "/Wardrobe/Photo/";
    private String albumsPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
    private Intent intent;
    private Bundle bundle;
    private Clothes clothes;
    private ClothesServiceImpl productService;
    private DatabaseHelper databaseHelper;
    private static final int REQUEST_PERMISSION_CAMERA_CODE = 1;
    private static final int LIST_TAKE_PHOTO = 3;
    private boolean canTakePhoto = true;
    private List<Clothes> clothesList;
    private List<Clothes> selectedClothesList;
    private SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//设置时间格式
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());//设置日期格式

    @ViewInject(R.id.grid_update_photo)
    private GridView cameraGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        init();
    }

    private void init() {
        x.view().inject(this);
        initProduct();
        initIntent();
        initPermission();
        initGrid();
    }

    private void initIntent() {
        intent = getIntent();
        if (intent == null) {
            intent = new Intent();
        }
        bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
    }

    private void initProduct() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        if (productService == null) {
            productService = new ClothesServiceImpl();
        }
        if (clothes == null) {
            clothes = new Clothes();
        }
    }

    private void initPermission() {//申请相机的相关授权
        //TODO 1.优化相机授权的反馈，包括提示成功和失败。2.实现授权被拒绝后再次打开依旧提示是否授权
        if (ContextCompat.checkSelfPermission(AlbumActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            canTakePhoto = false;
            if (ActivityCompat.shouldShowRequestPermissionRationale(AlbumActivity.this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AlbumActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA_CODE);
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(RESULT_CANCELED, intent);
            AlbumActivity.this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        initActionBar(R.layout.actionbar_album, this);
        return true;
    }

    private void initActionBar(int layoutId, Context mContext) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_album);

            LayoutInflater inflater = AlbumActivity.this.getLayoutInflater();   //先获取当前布局的填充器
            View actionbarLayout = inflater.inflate(R.layout.actionbar_album, null);   //通过填充器获取另外一个布局的对象
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(actionbarLayout, layout);
            TextView actionbarTextView = (TextView) actionbarLayout.findViewById(R.id.text_actionbar_update_photo);
            actionbarTextView.setText(getString(R.string.choose_photo));
            Button backBtn = (Button) actionbarLayout.findViewById(R.id.btn_actionbar_update_photo_back);//通过另外一个布局对象的findViewById获取其中的控件
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_CANCELED, intent);
                    AlbumActivity.this.finish();
                }
            });
            Button commitBtn = (Button) actionbarLayout.findViewById(R.id.btn_actionbar_update_photo_commit);     //通过另外一个布局对象的findViewById获取其中的控件
            commitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (Clothes temp : selectedClothesList) {//遍历选中的照片
                        copyFile(temp.getImgUrl(), photosPath + temp.getName());
                        temp.setDivision(bundle.getString("division"));
                        temp.setDate(sdfDate.format(new Date()));
                        productService.insertClothes(databaseHelper, temp);
                    }
                    setResult(RESULT_OK, intent);
                    AlbumActivity.this.finish();
                }
            });
        } else {
            Log.e(TAG, "There is no action bar");
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public void copyFile(String oldPath, String newPath) {
        File fromFile = new File(oldPath);
        File toFile = new File(newPath);
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists()) {
            return;
        }
        try {
            BufferedInputStream fosFrom = new BufferedInputStream(new FileInputStream(fromFile)); //读入原文件
            BufferedOutputStream fosTo = new BufferedOutputStream(new FileOutputStream(toFile));
            byte[] bt = new byte[1024];
            int c;
            while ((c = fosFrom.read(bt)) >= 0) {
                fosTo.write(bt, 0, c);
            }
            fosFrom.close();
            fosTo.close();
        } catch (FileNotFoundException e) {
            Log.e("复制文件异常", e.toString());
        } catch (IOException e) {
            Log.e("复制文件异常", e.toString());
        }
    }

    private void initGrid() {
        if (clothesList != null && clothesList.size() > 0) {
            clothesList.clear();
        }
        clothesList = getPhotos(albumsPath, clothesList);
        if (clothesList != null && clothesList.size() > 1) {
            clothesList = BaseUtils.quickSort(clothesList, 1, clothesList.size() - 1);
        }
        if (selectedClothesList == null) {
            selectedClothesList = new ArrayList<>();
        }
        cameraGridView = (GridView) findViewById(R.id.grid_update_photo);
        AlbumAdapter cameraGridViewAdapter = new AlbumAdapter(AlbumActivity.this, clothesList);
//        cameraGridViewAdapter.notifyDataSetChanged();
        cameraGridView.setAdapter(cameraGridViewAdapter);// 为GridView设置适配器
        cameraGridView.setOnItemClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canTakePhoto = true;
                    Log.d(TAG, "The permission of camera is granted.");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    canTakePhoto = false;
                    Log.d(TAG, "the permission of camera is denied.");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LIST_TAKE_PHOTO) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
                Bitmap bitmap = BitmapFactory.decodeFile(clothes.getImgUrl());
                // 防止OOM发生
                options.inJustDecodeBounds = false;
                bitmap.recycle();//回收
                productService.insertClothes(databaseHelper, clothes);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (position == 0) {
            if (canTakePhoto) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                File out = new File(getPhotoPath());
                Uri uri = Uri.fromFile(out);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, LIST_TAKE_PHOTO);
            } else {
                Toast.makeText(AlbumActivity.this, getString(R.string.camera_do_not_available_hint), Toast.LENGTH_SHORT).show();
            }
        } else {
            int index = position - parent.getFirstVisiblePosition();
            Clothes temp = clothesList.get(position - 1);
            if (!temp.isChosen()) {
                temp.setChosen(true);
                temp.setTime(sdfTime.format(new Date()));
                selectedClothesList.add(temp);
                parent.getChildAt(index).findViewById(R.id.layout_recycler_album).setVisibility(View.VISIBLE);
            } else {
                temp.setChosen(false);
                selectedClothesList.remove(temp);
                parent.getChildAt(index).findViewById(R.id.layout_recycler_album).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取所拍摄照片的存储路径
     *
     * @return The path of photo which are photographed just now.
     */
    private String getPhotoPath() {
        // 照片所存放的文件夹路径
        File file = new File(photosPath);
        file.mkdirs();
        //照片的命名，目标文件夹下，以当前时间数字串为名称，即可确保每张照片名称不相同。
        String photoName;
        Date date;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);//获取当前时间，进一步转化为字符串
        date = new Date();
        photoName = format.format(date);
        String photoPath = photosPath + photoName + ".jpg";
        Log.d(TAG, "File = " + photoPath);
        clothes.setImgUrl(photoPath);
        clothes.setDivision(bundle.getString("division"));//照片所属的服饰分类
        return photoPath;
    }

    private List<Clothes> getPhotos(final String strPath, List<Clothes> clothesArray) {//获取相册照片list
        if (clothesArray != null && clothesArray.size() > 0) {
            clothesArray.clear();
        } else {
            clothesArray = new ArrayList<>();
        }
        Clothes temp;
        File file = new File(strPath);
        File[] allFiles = file.listFiles();
        if (allFiles == null) {
            return null;
        }
        for (File fi : allFiles) {
            if (fi.isFile()) {
                int idx = fi.getPath().lastIndexOf(".");
                if (idx <= 0) {
                    continue;
                }
                String suffix = fi.getPath().substring(idx);
                String photoName = fi.getPath().substring(fi.getPath().lastIndexOf("/") + 1);
                if (suffix.toLowerCase().equals(".jpg") ||
                        suffix.toLowerCase().equals(".jpeg") ||
                        suffix.toLowerCase().equals(".bmp") ||
                        suffix.toLowerCase().equals(".png") ||
                        suffix.toLowerCase().equals(".gif")) {
                    temp = new Clothes();
                    temp.setImgUrl(fi.getPath());
                    temp.setName(photoName);
                    clothesArray.add(temp);
                }
            }
        }
        return clothesArray;
    }
}
