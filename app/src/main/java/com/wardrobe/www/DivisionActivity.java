package com.wardrobe.www;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wardrobe.www.adapter.DivisionAdapter;
import com.wardrobe.www.model.Division;

import java.util.ArrayList;
import java.util.List;

public class DivisionActivity extends BaseActivity {
    private Intent intent;
    private Bundle bundle;
    private RecyclerView recyclerView;
    private Division division;
    private Toolbar mToolbar;

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
        mToolbar = (Toolbar) findViewById(R.id.toolbar_unfold);
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle("");
            Button leftBtn = (Button) findViewById(R.id.toolbar_unfold_btn_left);
            leftBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_nav));
            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            Button rightBtn = (Button) findViewById(R.id.toolbar_unfold_btn_right);
            rightBtn.setVisibility(View.GONE);
            TextView titleText = (TextView) findViewById(R.id.toolbar_unfold_text_title);
            titleText.setText(R.string.my_wardrobe);
        }
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
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                bundle = intent.getExtras();
                if (bundle == null) {
                    bundle = new Bundle();
                }
                Division division = (Division) baseQuickAdapter.getItem(i);
                bundle.putString("division", getString(division.getNameCN()));
                intent.setClass(DivisionActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        public SpaceItemDecoration() {
            this.mSpace = DivisionActivity.this.getResources().getDimensionPixelSize(R.dimen.divider);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);

            if (pos > 0) {
                outRect.top = mSpace;
            } else {
                outRect.top = 0;
            }
        }
    }

}
