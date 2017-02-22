package com.wardrobe.www;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wardrobe.www.adapter.WardrobeAdapter;
import com.wardrobe.www.base.db.DatabaseHelper;
import com.wardrobe.www.base.model.Clothes;
import com.wardrobe.www.service.serviceImpl.ClothesServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name：WardrobeActivity
 * Class Function（Chinese）：衣柜，商品展示列表
 * Created by admin on 2016/9/20.
 */

public class WardrobeActivity extends BaseActivity {
    private static final String TAG = "WardrobeActivity";
    private Intent intent;
    private Bundle bundle;
    private String division;
    private Clothes clothes;
    private List<Clothes> clothesList;
    private ClothesServiceImpl clothesService;
    private TextView emptyHintText;
    private RecyclerView wardrobeRecycler;
    private WardrobeAdapter wardrobeAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);

        init();
    }

    private void init() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        initClothes();
        initIntent();
        initToolbar();
        initRecycler();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_fold);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle("");
            Button leftBtn = (Button) findViewById(R.id.toolbar_fold_btn_left);
            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WardrobeActivity.this.finish();
                }
            });
            Button rightBtn = (Button) findViewById(R.id.toolbar_fold_btn_right);
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setClass(WardrobeActivity.this, AlbumActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            rightBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_tj));
            TextView titleText = (TextView) findViewById(R.id.toolbar_fold_text_title);
            titleText.setText(division);
        }
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
            division = bundle.getString("division");
        }
    }

    private void initClothes() {
        if (clothesService == null) {
            clothesService = new ClothesServiceImpl();
        }
        if (clothes == null) {
            clothes = new Clothes();
        }
    }

    @Override
    protected void onResume() {
        refreshRecyclerData();
        super.onResume();
    }

    private void initRecycler() {
        emptyHintText = (TextView) findViewById(R.id.text_wardrobe_empty);
        wardrobeRecycler = (RecyclerView) findViewById(R.id.recycler_wardrobe);
        wardrobeRecycler.setLayoutManager(new GridLayoutManager(this, 2));//设置布局管理器
        // 设置item动画
        wardrobeRecycler.setItemAnimator(new DefaultItemAnimator());
        wardrobeRecycler.addItemDecoration(new SpaceItemDecoration());
        getData();
        if (clothesList.size() <= 0) {
            emptyHintText.setVisibility(View.VISIBLE);
            wardrobeRecycler.setVisibility(View.GONE);
        } else {
//            if (clothesList.size() > 1) {
//                clothesList = quickSort(clothesList, 0, clothesList.size() - 1);
//            }
            emptyHintText.setVisibility(View.GONE);
            wardrobeRecycler.setVisibility(View.VISIBLE);
            wardrobeAdapter = new WardrobeAdapter(clothesList);
            wardrobeRecycler.setAdapter(wardrobeAdapter);
            wardrobeRecycler.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    Clothes clothes = (Clothes) baseQuickAdapter.getItem(i);
                    bundle.putInt("position", i);
                    bundle.putString("division", clothes.getDivision());
                    bundle.putString("imgUrl", clothes.getImgUrl());
                    bundle.putParcelableArrayList("clothes", (ArrayList<Clothes>) clothesList);
                    intent.putExtras(bundle);
                    intent.setClass(WardrobeActivity.this, ClothesActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void refreshRecyclerData() {
        getData();
        if (clothesList != null && clothesList.size() > 0) {
            emptyHintText.setVisibility(View.GONE);
            wardrobeRecycler.setVisibility(View.VISIBLE);
            if (wardrobeAdapter == null) {
                wardrobeAdapter = new WardrobeAdapter(clothesList);
                wardrobeRecycler.setAdapter(wardrobeAdapter);
                wardrobeRecycler.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        Clothes clothes = (Clothes) baseQuickAdapter.getItem(i);
                        bundle.putInt("position", i);
                        bundle.putString("division", clothes.getDivision());
                        bundle.putString("imgUrl", clothes.getImgUrl());
                        bundle.putParcelableArrayList("clothes", (ArrayList<Clothes>) clothesList);
                        intent.putExtras(bundle);
                        intent.setClass(WardrobeActivity.this, ClothesActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                wardrobeAdapter.setNewData(clothesList);
            }
        } else {
            emptyHintText.setVisibility(View.VISIBLE);
            wardrobeRecycler.setVisibility(View.GONE);
        }
    }

    private void getData() {
        if (clothesList != null && clothesList.size() > 0) {
            clothesList.clear();
        } else {
            clothesList = new ArrayList<>();
        }
        clothesList = clothesService.showClothesByDivision(databaseHelper, division);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        public SpaceItemDecoration() {
            this.mSpace = WardrobeActivity.this.getResources().getDimensionPixelSize(R.dimen.divider);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);

            outRect.top = mSpace;
            if (pos % 2 == 0) {
                outRect.left = mSpace;
                outRect.right = mSpace / 2;
            } else {
                outRect.right = mSpace;
                outRect.left = mSpace / 2;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
