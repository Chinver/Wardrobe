package com.wardrobe.www.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wardrobe.www.R;
import com.wardrobe.www.base.model.Division;

import java.util.List;

public class DivisionAdapter extends BaseQuickAdapter<Division,BaseViewHolder> {

    public DivisionAdapter(List<Division> data) {
        super(R.layout.recycler_item_division, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Division division) {
        baseViewHolder.setImageResource(R.id.division_item_image, division.getImage())
                .setText(R.id.division_item_text_cn, division.getNameCN())
                .setText(R.id.division_item_text_en, division.getNameEN());
    }


}
