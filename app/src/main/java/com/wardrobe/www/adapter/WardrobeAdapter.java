package com.wardrobe.www.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wardrobe.www.R;
import com.wardrobe.www.model.Clothes;

import java.util.List;

public class WardrobeAdapter extends BaseQuickAdapter<Clothes> {
    public WardrobeAdapter(List<Clothes> data) {
        super(R.layout.recycler_item_wardrobe, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Clothes clothes) {
        Glide.with(mContext).load(clothes.getImgUrl()).crossFade().into((ImageView) baseViewHolder.getView(R.id.wardrobe_item_image));
    }
}
