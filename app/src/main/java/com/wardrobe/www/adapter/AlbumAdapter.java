package com.wardrobe.www.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wardrobe.www.R;
import com.wardrobe.www.base.model.Clothes;

import java.util.List;

/**
 * 相册适配器
 * Created by admin on 2016/9/26.
 */

public class AlbumAdapter extends BaseQuickAdapter<Clothes, AlbumAdapter.AlbumViewHolder> {

    public AlbumAdapter(List<Clothes> clothesList) {
        super(R.layout.recycler_item_album, clothesList);
    }

    @Override
    protected AlbumViewHolder createBaseViewHolder(View view) {
        return new AlbumViewHolder(view);
    }

    @Override
    protected void convert(AlbumViewHolder albumViewHolder, Clothes clothes) {
        albumViewHolder.setImageURL(R.id.recycler_item_photo, clothes.getImgUrl(), albumViewHolder.getAdapterPosition());
    }

     class AlbumViewHolder extends BaseViewHolder {
         AlbumViewHolder(View view) {
            super(view);
        }

         AlbumViewHolder setImageURL(int viewId, String url, int position) {
            if (position == 0) {
                Glide.with(mContext).load(R.drawable.but_camera).thumbnail(0.1f).crossFade().centerCrop().into((ImageView) getView(viewId));
            } else {
                Glide.with(mContext).load(url).thumbnail(0.1f).crossFade().centerCrop().into((ImageView) getView(viewId));
            }
            return this;
        }
    }

}
