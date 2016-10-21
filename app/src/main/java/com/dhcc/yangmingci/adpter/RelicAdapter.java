/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dhcc.yangmingci.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.entity.Relic;
import com.dhcc.yangmingci.listener.OnItemClickListener;
import com.dhcc.yangmingci.util.UrlUtils;

import java.util.List;

/**
 * Created by pengbangqin on 16-9-21.
 */
public class RelicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE = -1;
    Context context;
    List<Relic> list;

    private OnItemClickListener mOnItemClickListener;
    public RelicAdapter(Context context, List<Relic> list) {
        this.context=context;
        this.list=list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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
    @Override
    public int getItemCount() {
        //即使空数据，也显示一条条目
        return list.size() > 0 ? list.size() : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (VIEW_TYPE == viewType) {
            view = inflater.inflate(R.layout.item_empty, parent, false);

            return new MyEmptyHolder(view);
        }
        view = inflater.inflate(R.layout.menu_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).setOnItemClickListener(mOnItemClickListener);
            //根据索引获取CheckInfo对象
            Relic relic=list.get(position);
            ((MyViewHolder) holder).name.setText("文物名称:"+relic.getrName());
            ((MyViewHolder) holder).desc.setText("文物描述:"+relic.getrDesc());
            ((MyViewHolder) holder).cCount.setText("隐患数:"+relic.getcCount());
            ((MyViewHolder) holder).level.setText("文物级别:"+relic.getrLevel());
            for(int i=0;i<relic.getFiles().size();i++){
                //获取图片路径
                String picPath=relic.getFiles().get(i).getfPath();
                //加载图片
                String url= UrlUtils.URL_IMG+picPath;
                Glide.with(context).load(url)
                        .placeholder(R.mipmap.default_image)
                        .into(((MyViewHolder) holder).img);
            }
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView desc;
        ImageView img;
        TextView cCount;
        TextView level;
        OnItemClickListener mOnItemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name= (TextView) itemView.findViewById(R.id.name);
            desc= (TextView) itemView.findViewById(R.id.desc);
            img= (ImageView) itemView.findViewById(R.id.img);
            cCount= (TextView) itemView.findViewById(R.id.cCount);
            level= (TextView) itemView.findViewById(R.id.level);
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

}
