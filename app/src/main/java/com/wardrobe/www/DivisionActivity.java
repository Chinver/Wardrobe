package com.wardrobe.www;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wardrobe.www.base.util.DataCleanManager;
import com.wardrobe.www.base.util.LogUtil;
import com.wardrobe.www.adapter.DivisionAdapter;
import com.wardrobe.www.adapter.DrawerAdapter;
import com.wardrobe.www.base.model.Division;
import com.wardrobe.www.base.model.Drawer;

import java.util.ArrayList;
import java.util.List;

public class DivisionActivity extends BaseActivity {
    private static final String TAG = "DivisionActivity";
    private Intent intent;
    private Bundle bundle;
    private RecyclerView recyclerView;
    private Division division;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView drawerRecycler;
    private Drawer drawer;
    private DrawerAdapter drawerAdapter;
    private List<Drawer> drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        init();
    }

    private void init() {
        if (intent == null) {
            intent = new Intent();
        }
        initToolbar();
        initRecycler();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_drawer);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_nav);
        TextView titleText = (TextView) findViewById(R.id.toolbar_drawer_text_title);
        titleText.setText(R.string.my_wardrobe);
        initDrawer();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);//打开抽屉
            }
        });
    }

    private void initRecycler() {
        List<Division> divisionList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            division = new Division();
            switch (i) {
                case 0:
                    division.setImage(R.drawable.img_top);
                    division.setNameCN(R.string.wardrobe_top);
                    division.setNameEN(R.string.wardrobe_top_describe);
                    break;
                case 1:
                    division.setImage(R.drawable.img_bottoms);
                    division.setNameCN(R.string.wardrobe_bottoms);
                    division.setNameEN(R.string.wardrobe_bottoms_describe);
                    break;
                case 2:
                    division.setImage(R.drawable.img_skirt);
                    division.setNameCN(R.string.wardrobe_skirt);
                    division.setNameEN(R.string.wardrobe_skirt_describe);
                    break;
                case 3:
                    division.setImage(R.drawable.img_shoes);
                    division.setNameCN(R.string.wardrobe_shoes);
                    division.setNameEN(R.string.wardrobe_shoes_describe);
                    break;
                case 4:
                    division.setImage(R.drawable.img_overcoat);
                    division.setNameCN(R.string.wardrobe_overcoat);
                    division.setNameEN(R.string.wardrobe_overcoat_describe);
                    break;
                case 5:
                    division.setImage(R.drawable.img_other);
                    division.setNameCN(R.string.wardrobe_other);
                    division.setNameEN(R.string.wardrobe_other_describe);
                    break;
            }
            divisionList.add(division);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_division);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        DivisionAdapter divisionAdapter = new DivisionAdapter(divisionList);
        recyclerView.setAdapter(divisionAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                bundle = intent.getExtras();
                if (bundle == null) {
                    bundle = new Bundle();
                }
                Division division = (Division) baseQuickAdapter.getItem(position);
                bundle.putString("division", getString(division.getNameCN()));
                intent.setClass(DivisionActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int topSpace;
        int bottomSpace;

        public SpaceItemDecoration() {
            this.topSpace = DivisionActivity.this.getResources().getDimensionPixelSize(R.dimen.divider_division);
            this.bottomSpace = DivisionActivity.this.getResources().getDimensionPixelSize(R.dimen.divider_bottom);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);
            int lastPosition = state.getItemCount() - 1;

            outRect.top = topSpace;
            if (lastPosition == pos) {
                outRect.bottom = bottomSpace;
            } else {
                outRect.bottom = 0;
            }
        }

    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        if (drawerList == null) {
            drawerList = new ArrayList<>();
        }
        drawerData();
        drawerRecycler = (RecyclerView) findViewById(R.id.recycler_drawer);
        drawerRecycler.setLayoutManager(new LinearLayoutManager(this));
        drawerAdapter = new DrawerAdapter(drawerList);
        drawerRecycler.setAdapter(drawerAdapter);
        refreshDrawer();
    }

    private void refreshDrawer() {
        drawerRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (i) {
                    case 0:
                        intent.setClass(DivisionActivity.this, SelfTalkingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        DataCleanManager.clearAllCache(DivisionActivity.this);
                        drawer = drawerList.get(1);
                        drawer.setDescribe(getCacheSize());
                        drawerList.set(1, drawer);
                        drawerAdapter.setNewData(drawerList);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    private void drawerData() {
        for (int i = 0; i < 4; i++) {
            drawer = new Drawer();
            switch (i) {
                case 0:
                    drawer.setImage(R.drawable.icon_ssn);
                    drawer.setFunction(R.string.about_us);
                    drawer.setDescribe("");
                    break;
                case 1:
                    drawer.setImage(R.drawable.icon_qchc);
                    drawer.setFunction(R.string.clear_cache);
                    String dataCache = getCacheSize();
                    drawer.setDescribe(dataCache);
                    break;
                case 2:
                    drawer.setImage(R.drawable.icon_jcgx);
                    drawer.setFunction(R.string.check_version);
                    drawer.setDescribe(getVersion());
                    break;
                case 3:
                    drawer.setImage(R.drawable.icon_qhzh);
                    drawer.setFunction(R.string.exit_account);
                    drawer.setDescribe(getResources().getString(R.string.coming_soon));
                    break;
            }
            drawerList.add(drawer);
        }
    }

    private String getCacheSize() {
        String dataCache = "";
        try {
            dataCache = DataCleanManager.getTotalCacheSize(DivisionActivity.this);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return dataCache;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            LogUtil.d(TAG, "version:=" + version);
            return this.getString(R.string.version) + version;
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            return this.getString(R.string.version_unknown);
        }

    }
}
