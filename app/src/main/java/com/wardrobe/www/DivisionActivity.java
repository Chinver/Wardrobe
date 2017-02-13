package com.wardrobe.www;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wardrobe.www.adapter.DivisionAdapter;
import com.wardrobe.www.model.Division;

import java.util.ArrayList;
import java.util.List;

public class DivisionActivity extends BaseActivity/* implements View.OnClickListener*/ {
    private Intent intent;
    private Bundle bundle;
    private RecyclerView recyclerView;
    private Division division;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        init();
    }



    private void init() {
//        initView();

        if (intent == null) {
            intent = new Intent();
        }
        bundle = intent.getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }

        initRecycler();
    }

    private void initRecycler(){
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
        DivisionAdapter divisionAdapter = new DivisionAdapter(divisionList);
        recyclerView.setAdapter(divisionAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Division division = (Division) baseQuickAdapter.getItem(i);
                bundle.putString("division", getString(division.getNameCN()));
                intent.setClass(DivisionActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        initActionBar(R.layout.actionbar_main, this);
        return true;
    }

    private void initActionBar(int layoutId, Context mContext) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_main);

        //LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflater = DivisionActivity.this.getLayoutInflater();   //先获取当前布局的填充器
        View actionbarLayout = inflater.inflate(R.layout.actionbar_main, null);   //通过填充器获取另外一个布局的对象
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarLayout, layout);
        TextView actionbarTextView = (TextView) actionbarLayout.findViewById(R.id.text_actionbar_main);
        actionbarTextView.setText(R.string.my_wardrobe);
    }

}
