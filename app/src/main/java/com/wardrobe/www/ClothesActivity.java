package com.wardrobe.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wardrobe.www.base.db.DatabaseHelper;
import com.wardrobe.www.base.model.Clothes;
import com.wardrobe.www.service.serviceImpl.ClothesServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 * Created by admin on 2016/10/17.
 */

public class ClothesActivity extends BaseActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener {
    private static final String TAG = "ClothesActivity";
    private static final int FLING_MIN_DISTANCE = 100;
    private static final int FLING_MIN_VELOCITY = 0;

    private Toolbar mToolbar;

    private Intent intent;
    private Bundle bundle;
    private Clothes clothes;
    private List<Clothes> clothesList;
    private int position = 0;
    private ClothesServiceImpl clothesService;

    private GestureDetector mGestureDetector;

    private ImageView clothesImage;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        init();
    }

    private void init() {
        if(databaseHelper==null){
            databaseHelper=new DatabaseHelper(this);
        }
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(ClothesActivity.this, this);
        }
        if (clothesService == null) {
            clothesService = new ClothesServiceImpl();
        }
        if (clothesList == null) {
            clothesList = new ArrayList<>();
        }
        initIntent();
        initToolbar();
        clothesImage = (ImageView) findViewById(R.id.image_clothes);
        refreshImageView();
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

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_unfold);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle("");
            Button leftBtn = (Button) findViewById(R.id.toolbar_unfold_btn_left);
            leftBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_back));
            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClothesActivity.this.finish();
                }
            });
            Button rightBtn = (Button) findViewById(R.id.toolbar_unfold_btn_right);
            rightBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_ljt));
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clothesService.deleteClothesByName(databaseHelper, clothes.getName()) > 0) {
                        clothesList.remove(position);//删除当前下标的照片,此时该position对应的照片已自动调整为下一张照片
                        if (clothesList.size() == 0) {
                            ClothesActivity.this.finish();
                        } else {
                            if (position == clothesList.size()) {
                                position = position - 1;
                            }
                            clothes = clothesList.get(position);
                            refreshImageView();  //显示早于当前照片对应时间的照片，即下一张照片
                        }
                        Toast.makeText(ClothesActivity.this, getString(R.string.delete_succeed_hint), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ClothesActivity.this, getString(R.string.delete_failed_hint), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            TextView titleText = (TextView) findViewById(R.id.toolbar_unfold_text_title);
            titleText.setText(R.string.albums);
        }
    }

    private void refreshImageView() {
        Glide.with(this).load(clothes.getImgUrl()).thumbnail(0.8f).fitCenter().crossFade().into(clothesImage);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ClothesActivity.this.finish();
        }
        return super.onKeyUp(keyCode, event);
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
                refreshImageView();
            } else {
                Toast.makeText(this, getString(R.string.wardrobe_boundary_hint), Toast.LENGTH_SHORT).show();//向左手势
            }
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // Fling right
            if (position > 0) {
                position--;
                clothes = clothesList.get(position);
                refreshImageView();
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
