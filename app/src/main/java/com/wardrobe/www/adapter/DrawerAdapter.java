package com.wardrobe.www.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wardrobe.www.R;
import com.wardrobe.www.base.model.Drawer;

import java.util.List;

public class DrawerAdapter extends BaseQuickAdapter<Drawer,BaseViewHolder> {

    public DrawerAdapter(List<Drawer> data) {
        super(R.layout.recycler_item_drawer, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Drawer drawer) {
        baseViewHolder.setImageResource(R.id.drawer_item_image, drawer.getImage())
                .setText(R.id.drawer_item_text, drawer.getFunction())
                .setText(R.id.drawer_item_text_describe, drawer.getDescribe());
    }


}
