package com.dhcc.yangmingci.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.entity.CheckInfo;
import com.dhcc.yangmingci.entity.PicInfo;
import com.dhcc.yangmingci.listener.OnItemClickListener;
import com.dhcc.yangmingci.util.UrlUtils;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengbangqin on 16-9-21.
 */
public class MyCheckAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE = -1;
    List<CheckInfo> list;
    Context context;
    private OnItemClickListener mOnItemClickListener;

    public MyCheckAdapter(Context context, List<CheckInfo> list) {
        this.list=list;
        this.context=context;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (VIEW_TYPE == viewType) {
            view = inflater.inflate(R.layout.item_empty, parent, false);

            return new MyEmptyHolder(view);
        }
        view = inflater.inflate(R.layout.detail_item, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * 将数据绑定到ViewHolder上
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setOnItemClickListener(mOnItemClickListener);

            //根据索引获取CheckInfo对象
            CheckInfo checkInfo=list.get(position);
            ((MyViewHolder) holder).tv_name.setText("文物名称:"+checkInfo.getrName());
            ((MyViewHolder) holder).tv_time.setText("检查时间:"+checkInfo.getcDatetime());
            ((MyViewHolder) holder).tv_content.setText("检查内容:"+checkInfo.getcDesc());
            String cStatus=checkInfo.getcStatus();
            if(cStatus.equals("1")){
                if(checkInfo.getcType().equals("1")){
                    ((MyViewHolder) holder).tv_status.setText("整改情况:未处理");
                }
            }else if(cStatus.equals("2")){
                ((MyViewHolder) holder).tv_status.setText("整改情况:整改中..");
            }else{
                ((MyViewHolder) holder).tv_status.setText("整改情况:整改完成");
            }

            if(checkInfo.getcType().equals("1")){
                ((MyViewHolder) holder).isIv.setImageResource(R.mipmap.my_error);
            }else{
                ((MyViewHolder) holder).isIv.setImageResource(R.mipmap.my_corr);
            }
            /**
             * 给nineGrid设置图片
             */
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            List<PicInfo> images = checkInfo.getFiles();
            if (images != null) {
                for (PicInfo image : images) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(UrlUtils.URL_IMG+image.getfPath());
                    info.setBigImageUrl(UrlUtils.URL_IMG+image.getfPath());
                    imageInfo.add(info);
                }
            }
            ((MyViewHolder) holder).nineGrid.setImageLoader(new GlideImageLoader());
            ((MyViewHolder) holder).nineGrid.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
            if (images != null && images.size() == 1) {
                ((MyViewHolder) holder).nineGrid.setSingleImageRatio(images.get(0).getWidth() * 1.0f / images.get(0).getHeight());
            }
        }
    }

    @Override
    public int getItemCount() {
        //即使空数据，也显示一条条目
        return list.size() > 0 ? list.size() : 1;
    }

    /**
     *  获取条目 View填充的类型
     *  默认返回0
     * @param position
     * @return
     */
    public int getItemViewType(int position) {
        if (list.size() <= 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_content;
        TextView tv_time;
        TextView tv_status;
        ImageView isIv;
        NineGridView nineGrid;
        OnItemClickListener mOnItemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_content= (TextView) itemView.findViewById(R.id.tv_content);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            tv_status= (TextView) itemView.findViewById(R.id.tv_status);
            isIv= (ImageView) itemView.findViewById(R.id.isIv);
            nineGrid= (NineGridView) itemView.findViewById(R.id.nineGrid);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public class MyEmptyHolder extends RecyclerView.ViewHolder{
        TextView tv_empty;

        public MyEmptyHolder(View itemView) {
            super(itemView);

            tv_empty = (TextView) itemView.findViewById(R.id.tv_empty);
        }
    }
    /** Glide 加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.mipmap.default_image)//
                    .error(R.mipmap.default_image)//
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}

