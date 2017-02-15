package com.wardrobe.www;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.wardrobe.www.Utils.LogUtil;
import com.wardrobe.www.adapter.WardrobeAdapter;
import com.wardrobe.www.db.DatabaseHelper;
import com.wardrobe.www.model.Clothes;
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
    private DatabaseHelper databaseHelper;
    private TextView emptyHintText;
    private RecyclerView wardrobeRecycler;
    private WardrobeAdapter wardrobeAdapter;
    private Toolbar mToolbar;

    private static final int DELETE_CLOTHES = 1;
    private static final int INSERT_CLOTHES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);

        init();
    }

    private void init() {
        initClothes();
        initIntent();
        initToolbar();
        initRecycler();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_fold);
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
                    startActivityForResult(intent, INSERT_CLOTHES);
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
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        if (clothesService == null) {
            clothesService = new ClothesServiceImpl();
        }
        if (clothes == null) {
            clothes = new Clothes();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRecycler() {
        emptyHintText = (TextView) findViewById(R.id.text_wardrobe_empty);
        wardrobeRecycler = (RecyclerView) findViewById(R.id.recycler_wardrobe);
        wardrobeRecycler.setLayoutManager(new GridLayoutManager(this, 2));//设置布局管理器
        // 设置item动画
        wardrobeRecycler.setItemAnimator(new DefaultItemAnimator());
        wardrobeRecycler.addItemDecoration(new SpaceItemDecoration());
//        wardrobeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                super.onScrolled(recyclerView, dx, dy);
//
//                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//
//                int totalItemCount = layoutManager.getItemCount();
//
//                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//
//                if (!loading && totalItemCount < (lastVisibleItem + 5)) {
//                    page++;
//                    loading = true;
//                    refreshRecycler();
//                }
//            }
//        });
        refreshRecyclerData();

    }

    private void refreshRecyclerData() {
        if (clothesList != null && clothesList.size() > 0) {
            clothesList.clear();
        } else {
            clothesList = new ArrayList<>();
        }
        clothesList = clothesService.showClothesByDivision(databaseHelper, division);
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
            wardrobeRecycler.setAdapter(wardrobeAdapter);// 为GridView设置适配器
            wardrobeRecycler.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    Clothes clothes = (Clothes) baseQuickAdapter.getItem(i);
                    bundle.putInt("position", i);
                    bundle.putString("division", clothes.getDivision());
                    bundle.putString("imgUrl", clothes.getImgUrl());
                    bundle.putParcelableArrayList("clothes", (ArrayList<Clothes>) clothesList);
                    intent.putExtras(bundle);
                    intent.setClass(WardrobeActivity.this, ClothesActivity.class);
                    startActivityForResult(intent, DELETE_CLOTHES);
                }
            });
            wardrobeAdapter.openLoadAnimation();
            wardrobeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            LogUtil.d(TAG,"resultCode="+resultCode);
            if (requestCode == DELETE_CLOTHES) {
//                Bundle bundle = data.getExtras();
//                ArrayList<Integer> list = bundle.getIntegerArrayList("deleteList");
//                if (list != null && list.size() > 0) {
//                    for (Integer temp : list) {
//                        wardrobeAdapter.removeItem(temp);
//                    }
//                }
            } else if (requestCode == INSERT_CLOTHES) {
//                Bundle bundle = data.getExtras();
//                ArrayList<Integer> list = bundle.getIntegerArrayList("deleteList");
//                if (list != null && list.size() > 0) {
//                    for (Integer temp : list) {
//                        wardrobeAdapter.removeItem(temp);
//                    }
//                }

            }
            refreshRecyclerData();
        }

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
                outRect.right = mSpace;
                outRect.left = mSpace;
            }
        }
    }

}
