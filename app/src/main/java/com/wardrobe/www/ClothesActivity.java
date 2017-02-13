package com.wardrobe.www;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wardrobe.www.Utils.LogUtil;
import com.wardrobe.www.db.DatabaseHelper;
import com.wardrobe.www.model.Clothes;
import com.wardrobe.www.service.serviceImpl.ClothesServiceImpl;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 * Created by admin on 2016/10/17.
 */

public class ClothesActivity extends BaseActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener {
    private static final String TAG = "ClothesActivity";

    private Intent intent;
    private Bundle bundle;
    private Clothes clothes;
    private List<Clothes> clothesList;
    //    private List<Integer> deleteList = new ArrayList<>();
    private ImageOptions imageOptions;
    private int position = 0;
    private ClothesServiceImpl clothesService;
    private DatabaseHelper databaseHelper;
    private boolean isDelete = false;

    private GestureDetector mGestureDetector;
    private static final int FLING_MIN_DISTANCE = 100;
    private static final int FLING_MIN_VELOCITY = 0;

    @ViewInject(R.id.image_clothes)
    private ImageView clothesImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        init();
    }

    private void init() {
        x.view().inject(this);
        imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType(缩放方式)
//                .setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                .setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(ClothesActivity.this, this);
        }
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        if (clothesService == null) {
            clothesService = new ClothesServiceImpl();
        }
        if (clothesList == null) {
            clothesList = new ArrayList<>();
        }
        initIntent();
        initImageView();
    }

    private void initIntent() {
        intent = getIntent();
        if (intent == null) {
            intent = new Intent();
        }
        bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        } else {
            position = bundle.getInt("position");
            ArrayList<Clothes> list = bundle.getParcelableArrayList("clothes");
            if (list != null && list.size() > 0) {
                for (Clothes temp : list) {
                    clothesList.add(temp);
                }
                clothes = clothesList.get(position);
            }
        }
    }

    private void initImageView() {
        x.image().bind(clothesImage, clothes.getImgUrl(), imageOptions);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isDelete) {
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED, intent);
            }
            ClothesActivity.this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        initActionBar(R.layout.actionbar_wardrobe, this);
        return true;
    }

    private void initActionBar(int layoutId, Context mContext) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_clothes);

            LayoutInflater inflater = ClothesActivity.this.getLayoutInflater();   //先获取当前布局的填充器
            View actionbarLayout = inflater.inflate(R.layout.actionbar_clothes, null);   //通过填充器获取另外一个布局的对象
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(actionbarLayout, layout);
            TextView actionbarTextView = (TextView) actionbarLayout.findViewById(R.id.text_actionbar_clothes);
            actionbarTextView.setText(R.string.albums);
            Button backBtn = (Button) actionbarLayout.findViewById(R.id.btn_actionbar_clothes_back);//通过另外一个布局对象的findViewById获取其中的控件
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isDelete) {
                        setResult(RESULT_OK, intent);
                    } else {
                        setResult(RESULT_CANCELED, intent);
                    }
                    ClothesActivity.this.finish();
                }
            });
            Button deleteBtn = (Button) actionbarLayout.findViewById(R.id.btn_actionbar_clothes_delete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clothesService.deleteClothesByName(databaseHelper, clothesList.get(position).getName()) > 0) {
                        clothesList.remove(position);//删除当前下标的照片,此时该position对应的照片已自动调整为下一张照片
//                        deleteList.add(position);
                        isDelete = true;
                        Toast.makeText(ClothesActivity.this, getString(R.string.delete_succeed_hint), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ClothesActivity.this, getString(R.string.delete_failed_hint), Toast.LENGTH_SHORT).show();
                    }
                    //显示早于当前照片对应时间的照片，即下一张照片
                    x.image().bind(clothesImage, clothesList.get(position).getImgUrl(), imageOptions);
                    if (clothesList.size() == 0) {
//                        bundle.putIntegerArrayList("deleteList", (ArrayList<Integer>) deleteList);
                        ClothesActivity.this.finish();
                    }
                }
            });
        } else

        {
            LogUtil.e(TAG, "There is no action bar");
        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // Fling left
            if (position < clothesList.size() - 1) {
                position++;
                clothes = clothesList.get(position);
                initImageView();
            } else {
                Toast.makeText(this, getString(R.string.wardrobe_boundary_hint), Toast.LENGTH_SHORT).show();//向左手势
            }
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // Fling right
            if (position > 0) {
                position--;
                clothes = clothesList.get(position);
                initImageView();
            } else {
                Toast.makeText(this, getString(R.string.wardrobe_boundary_hint), Toast.LENGTH_SHORT).show();//向右手势
            }
        }

        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        clothesImage.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

}
