package com.wardrobe.www.adapter;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wardrobe.www.R;
import com.wardrobe.www.AlbumActivity;
import com.wardrobe.www.model.Clothes;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 相册适配器
 * Created by admin on 2016/9/26.
 */

public class AlbumAdapter extends BaseAdapter {
    private static final String TAG = "AlbumAdapter";
    private AlbumActivity context;
    private List<Clothes> clothesList;
    private LayoutInflater mInflater;
    private ImageOptions imageOptions;

    public AlbumAdapter(AlbumActivity context, List<Clothes> clothesList) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.recycler_album, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position >= 1) {
            x.image().bind(holder.imgView, clothesList.get(position - 1).getImgUrl(), imageOptions);
            holder.imgView.setFocusable(false);
            if (clothesList.get(position - 1).isChosen()) {
                holder.layout.setVisibility(View.VISIBLE);
            } else {
                holder.layout.setVisibility(View.GONE);
            }
        } else {
            holder.imgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.but_camera));
            holder.imgView.setFocusable(false);
            holder.layout.setVisibility(View.GONE);
        }
        return convertView; // 返回ImageView
    }

    /*
     * 功能：获得当前选项的ID
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * 功能：获得当前选项
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return clothesList.get(position);
    }

    /*
     * 获得数量
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return clothesList.size() + 1;
    }

    private class ViewHolder {
        @ViewInject(R.id.image_recycler_album_photo)
        private ImageView imgView;
        @ViewInject(R.id.layout_recycler_album)
        private RelativeLayout layout;
    }
}
