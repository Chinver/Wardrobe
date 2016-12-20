package com.wardrobe.www;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initView();

        if (intent == null) {
            intent = new Intent();
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
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
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();   //先获取当前布局的填充器
        View actionbarLayout = inflater.inflate(R.layout.actionbar_main, null);   //通过填充器获取另外一个布局的对象
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionbarLayout, layout);
        TextView actionbarTextView = (TextView) actionbarLayout.findViewById(R.id.text_actionbar_main);
        actionbarTextView.setText(R.string.my_wardrobe);
    }

    private void initView() {
        RelativeLayout topRelative = (RelativeLayout) findViewById(R.id.layout_wardrobe_top);
        RelativeLayout bottomsRelative = (RelativeLayout) findViewById(R.id.layout_wardrobe_bottoms);
        RelativeLayout skirtRelative = (RelativeLayout) findViewById(R.id.layout_wardrobe_skirt);
        RelativeLayout shoesRelative = (RelativeLayout) findViewById(R.id.layout_wardrobe_shoes);
        RelativeLayout overcoatRelative = (RelativeLayout) findViewById(R.id.layout_wardrobe_overcoat);
        RelativeLayout otherRelative = (RelativeLayout) findViewById(R.id.layout_wardrobe_other);
        topRelative.setOnClickListener(this);
        bottomsRelative.setOnClickListener(this);
        skirtRelative.setOnClickListener(this);
        shoesRelative.setOnClickListener(this);
        overcoatRelative.setOnClickListener(this);
        otherRelative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_wardrobe_top:
                bundle.putString("division", "上衣");
                intent.setClass(MainActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_wardrobe_bottoms:
                bundle.putString("division", "下装");
                intent.setClass(MainActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_wardrobe_skirt:
                bundle.putString("division", "连衣裙");
                intent.setClass(MainActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_wardrobe_shoes:
                bundle.putString("division", "鞋子");
                intent.setClass(MainActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_wardrobe_overcoat:
                bundle.putString("division", "外套");
                intent.setClass(MainActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_wardrobe_other:
                bundle.putString("division", "配饰");
                intent.setClass(MainActivity.this, WardrobeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
