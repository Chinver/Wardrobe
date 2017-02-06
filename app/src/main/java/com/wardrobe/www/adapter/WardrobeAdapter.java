package com.wardrobe.www.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wardrobe.www.WardrobeActivity;
import com.wardrobe.www.R;
import com.wardrobe.www.model.Clothes;
import com.wardrobe.www.service.RecyclerItemClickListener;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by admin on 2016/9/26.
 */

public class WardrobeAdapter extends RecyclerView.Adapter<WardrobeAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "WardrobeAdapter";
    private Context context;
    private List<Clothes> clothesList;
    private LayoutInflater mInflater;
    private ImageOptions imageOptions;
    private RecyclerItemClickListener listener;

    public WardrobeAdapter(WardrobeActivity context, List<Clothes> clothesList) {
        this.context = context;
        this.clothesList = clothesList;
        this.mInflater = LayoutInflater.from(context);
        imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType(缩放方式)
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                .setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER).build();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.recycler_wardrobe, null);
//            holder = new ViewHolder();
//            x.view().inject(holder, convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        x.image().bind(holder.imgView, imgList.get(position).getImg(), imageOptions);
//        return convertView; // 返回ImageView
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return imgList.get(position);
//    }
//
//
//    @Override
//    public int getCount() {
//        return imgList.size();
//    }
//
//    private class ViewHolder {
//        @ViewInject(R.id.recycler_wardrobe_item)
//        private ImageView imgView;
//    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_wardrobe, null);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return (vh);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        x.image().bind(holder.imgView, clothesList.get(position).getImgUrl(), imageOptions);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }

    /**
     * 向指定位置添加元素
     *
     * @param position
     * @param value
     */
    public void addItem(int position, Clothes value) {
        if (position > clothesList.size()) {
            position = clothesList.size();
        }
        if (position < 0) {
            position = 0;
        }
        clothesList.add(position, value);
        /**
         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
         * 而使用notifyDataSetChanged()则没有
         */
        notifyItemInserted(position);
    }

    /**
     * 移除指定位置元素
     *
     * @param position
     * @return
     */
    public Clothes removeItem(int position) {
        if (position > clothesList.size() - 1) {
            return null;
        }
        Clothes value = clothesList.remove(position);
        notifyItemRemoved(position);
        return value;

    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, (int) view.getTag());
        }
    }

    public void setOnItemClickListener(RecyclerItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.image_recycler_wardrobe_img)
        private ImageView imgView;

        private ViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.image_recycler_wardrobe_img);
        }
    }
}
