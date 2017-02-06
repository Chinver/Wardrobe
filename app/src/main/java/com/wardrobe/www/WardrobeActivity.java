package com.wardrobe.www;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wardrobe.www.adapter.WardrobeAdapter;
import com.wardrobe.www.db.DatabaseHelper;
import com.wardrobe.www.model.Clothes;
import com.wardrobe.www.service.RecyclerItemClickListener;
import com.wardrobe.www.service.serviceImpl.ClothesServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private WardrobeAdapter wardrobeAdapter;
    private TextView emptyHintText;
    private RecyclerView wardrobeRecycler;

    private static final int DELETE_CLOTHES = 1;
    private static final int INSERT_CLOTHES = 2;

//    @ViewInject(R.id.grid_product_display)
//    private GridView displayGridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);

        init();
    }

    private void init() {
        x.view().inject(this);
        initProduct();
        initIntent();
        initRecycler();
//        initGrid();
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

    private void initProduct() {
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
//        initGrid();
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
            actionBar.setCustomView(R.layout.actionbar_wardrobe);

            //LayoutInflater inflater = getLayoutInflater();
            LayoutInflater inflater = WardrobeActivity.this.getLayoutInflater();   //先获取当前布局的填充器
            View actionbarLayout = inflater.inflate(R.layout.actionbar_wardrobe, null);   //通过填充器获取另外一个布局的对象
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(actionbarLayout, layout);
            TextView actionbarTextView = (TextView) actionbarLayout.findViewById(R.id.text_actionbar_product_display);
            actionbarTextView.setText(division);
            Button backBtn = (Button) actionbarLayout.findViewById(R.id.btn_actionbar_product_display_back);//通过另外一个布局对象的findViewById获取其中的控件
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WardrobeActivity.this.finish();
                }
            });
            Button addBtn = (Button) actionbarLayout.findViewById(R.id.btn_actionbar_product_display);     //通过另外一个布局对象的findViewById获取其中的控件
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.setClass(WardrobeActivity.this, AlbumActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, INSERT_CLOTHES);
                }
            });
        } else {
            Log.e(TAG, "There is no action bar");
        }
    }


//    private void initGrid() {
//
//        displayGridView = (GridView) findViewById(R.id.grid_product_display);
//        if (productList != null && productList.size() > 0) {
//            productList.clear();
//        } else {
//            productList = new ArrayList<>();
//        }
//        productList = productService.showProductsByDivision(databaseHelper, division);
//        if (productList.size() > 1) {
//            productList = quickSort(productList, 0, productList.size() - 1);
//        }
//        if (productList.size() == 0) {
//            emptyHintText.setVisibility(View.VISIBLE);
//            displayGridView.setVisibility(View.GONE);
//        } else {
//            emptyHintText.setVisibility(View.GONE);
//            displayGridView.setVisibility(View.VISIBLE);
//            WardrobeAdapter displayGridViewAdapter = new WardrobeAdapter(WardrobeActivity.this, productList);
//            displayGridView.setAdapter(displayGridViewAdapter);// 为GridView设置适配器
//            displayGridView.setOnItemClickListener(this);
//        }
//    }

    private void initRecycler() {
        emptyHintText = (TextView) findViewById(R.id.text_wardrobe_empty);
        wardrobeRecycler = (RecyclerView) findViewById(R.id.recycler_wardrobe);
        wardrobeRecycler.setLayoutManager(new GridLayoutManager(this, 2));//设置布局管理器
        // 设置item动画
        wardrobeRecycler.setItemAnimator(new DefaultItemAnimator());
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
            wardrobeAdapter = new WardrobeAdapter(WardrobeActivity.this, clothesList);
            wardrobeRecycler.setAdapter(wardrobeAdapter);// 为GridView设置适配器
            wardrobeAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    bundle.putInt("position", position);
                    bundle.putString("division", division);
                    bundle.putString("imgUrl", clothesList.get(position).getImgUrl());
                    bundle.putParcelableArrayList("clothes", (ArrayList<Clothes>) clothesList);
                    intent.putExtras(bundle);
                    intent.setClass(WardrobeActivity.this, ClothesActivity.class);
                    startActivityForResult(intent, DELETE_CLOTHES);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == DELETE_CLOTHES) {
//                Bundle bundle = data.getExtras();
//                ArrayList<Integer> list = bundle.getIntegerArrayList("deleteList");
//                if (list != null && list.size() > 0) {
//                    for (Integer temp : list) {
//                        wardrobeAdapter.removeItem(temp);
//                    }
//                }
                refreshRecyclerData();
            } else if (requestCode == INSERT_CLOTHES) {
//                Bundle bundle = data.getExtras();
//                ArrayList<Integer> list = bundle.getIntegerArrayList("deleteList");
//                if (list != null && list.size() > 0) {
//                    for (Integer temp : list) {
//                        wardrobeAdapter.removeItem(temp);
//                    }
//                }
                refreshRecyclerData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<Clothes> quickSort(List<Clothes> clothesList, int low, int high) {
        Clothes temp;

        int left = low;
        int right = high;
        String indexDateStr = clothesList.get(low).getTime();
        if (StringUtils.isBlank(indexDateStr) || StringUtils.isEmpty(indexDateStr)) {
            return clothesList;
        }
        while (left < right) {
            while (left < right && clothesList.get(right).getTime().compareTo(indexDateStr) < 0) {
                right--;
            }

            if (left < right) {
                temp = clothesList.get(right);
                clothesList.set(right, clothesList.get(left));
                clothesList.set(left, temp);
                left++;
            }

            while (left < right && clothesList.get(left).getTime().compareTo(indexDateStr) >= 0) {
                left++;
            }

            if (left < right) {
                temp = clothesList.get(right);
                clothesList.set(right, clothesList.get(left));
                clothesList.set(left, temp);
                right--;
            }

            if (left > low) {
                quickSort(clothesList, low, left - 1); // 递归调用
            }
            if (right < high) {
                quickSort(clothesList, right + 1, high);
            }
        }
        return clothesList;
    }

}
