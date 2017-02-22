package com.wardrobe.www;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wardrobe.www.adapter.SelfTalkingAdapter;
import com.wardrobe.www.base.model.SelfTalking;

import java.util.ArrayList;
import java.util.List;

public class SelfTalkingActivity extends BaseActivity {
    private static final String TAG = "SelfTalkingActivity";
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private SelfTalkingAdapter selfTalkingAdapter;
    private SelfTalking selfTalking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_talking);

        init();
    }

    private void init() {
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
            leftBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_back));
            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelfTalkingActivity.this.finish();
                }
            });
            Button rightBtn = (Button) findViewById(R.id.toolbar_unfold_btn_right);
            rightBtn.setVisibility(View.GONE);
            TextView titleText = (TextView) findViewById(R.id.toolbar_unfold_text_title);
            titleText.setText(R.string.self_talking);
        }
    }

    private void initRecycler() {
        List<SelfTalking> selfTalkingList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            selfTalking = new SelfTalking();
            switch (i) {
                case 0:
                    selfTalking.setDate("2017.01.15");
                    selfTalking.setTitle(getResources().getString(R.string.about_us));
                    selfTalking.setContent("产品设计：资深产品经理一毛君\n" +
                            "技术开发：热血二次元程序员二毛君\n" +
                            "UI设计：四海八荒首席设计师三毛君\n" +
                            "没错，我们就是传说中的三角铁组合\n" +
                            "一共6毛，谢谢！");
                    break;
                case 1:
                    selfTalking.setDate("2017.01.15");
                    selfTalking.setTitle("联系二毛君（这是一个广告位）");
                    selfTalking.setContent("二毛君接受来自四面八方的吐槽及赞美\n" +
                            "QQ：1297997492\n" +
                            "邮箱：1297997492@qq.com\n" +
                            "QQ群：452890696\n" +
                            "接头暗号：衣帽间");
                    break;
                case 2:
                    selfTalking.setDate("2017.02.10");
                    selfTalking.setTitle("我们的目标是：更酷的交互！");
                    selfTalking.setContent("面试官听说二毛的App基于Android4.4时迷之沉默，二毛君顿悟，" +
                            "Material Design是14年Android5.0才推出的设计语言，效果酷炫，性能更优，" +
                            "于是……");
                    break;
            }
            selfTalkingList.add(selfTalking);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_self_talking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new SelfTalkingActivity.SpaceItemDecoration());
        selfTalkingAdapter = new SelfTalkingAdapter(selfTalkingList);
        recyclerView.setAdapter(selfTalkingAdapter);
    }

}
