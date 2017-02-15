package com.wardrobe.www.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wardrobe.www.R;
import com.wardrobe.www.model.Clothes;

import java.util.List;

/**
 * 相册适配器
 * Created by admin on 2016/9/26.
 */

public class AlbumAdapter extends BaseQuickAdapter<Clothes, AlbumAdapter.AlbumViewHolder> {

    public AlbumAdapter(List<Clothes> clothesList) {
        super(R.layout.recycler_item_album, clothesList);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final RecyclerView.ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.recycler_item_album, null);
//            holder = new ViewHolder();
//            x.view().inject(holder, convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if (position >= 1) {
//            x.image().bind(holder.imgView, clothesList.get(position - 1).getImgUrl(), imageOptions);
//            holder.imgView.setFocusable(false);
//            if (clothesList.get(position - 1).isChosen()) {
//                holder.layout.setVisibility(View.VISIBLE);
//            } else {
//                holder.layout.setVisibility(View.GONE);
//            }
//        } else {
//            holder.imgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.but_camera));
//            holder.imgView.setFocusable(false);
//            holder.layout.setVisibility(View.GONE);
//        }
//        return convertView; // 返回ImageView
//    }

    @Override
    protected AlbumViewHolder createBaseViewHolder(View view) {
        return new AlbumViewHolder(view);
    }

    @Override
    protected void convert(AlbumViewHolder albumViewHolder, Clothes clothes) {
        albumViewHolder.setImageURL(R.id.recycler_item_photo, clothes.getImgUrl(), albumViewHolder.getAdapterPosition());
    }

    public class AlbumViewHolder extends BaseViewHolder {
        public AlbumViewHolder(View view) {
            super(view);
        }

        public AlbumViewHolder setImageURL(int viewId, String url, int position) {
            if (position == 0) {
                Glide.with(mContext).load(R.drawable.but_camera).thumbnail(0.8f).crossFade().centerCrop().into((ImageView) getView(viewId));
            } else {
                Glide.with(mContext).load(url).thumbnail(0.8f).crossFade().centerCrop().into((ImageView) getView(viewId));
            }
            return this;
        }
    }

}
