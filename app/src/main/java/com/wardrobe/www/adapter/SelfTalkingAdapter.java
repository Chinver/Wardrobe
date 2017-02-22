package com.wardrobe.www.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wardrobe.www.R;
import com.wardrobe.www.base.model.SelfTalking;

import java.util.List;

public class SelfTalkingAdapter extends BaseQuickAdapter<SelfTalking, BaseViewHolder> {
    private List<SelfTalking> data;

    public SelfTalkingAdapter(List<SelfTalking> data) {
        super(R.layout.recycler_item_self_talking, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SelfTalking selfTalking) {
        baseViewHolder.setText(R.id.self_talking_date_text, selfTalking.getDate())
                .setText(R.id.self_talking_title_text, selfTalking.getTitle())
                .setText(R.id.self_talking_content_text, selfTalking.getContent());
//        if (baseViewHolder.getAdapterPosition() == data.size() - 1) {
//            baseViewHolder.setVisible(R.id.line_date_bottom, false);
//        }else {
//            baseViewHolder.setVisible(R.id.line_date_bottom, true);
//        }
    }


}
