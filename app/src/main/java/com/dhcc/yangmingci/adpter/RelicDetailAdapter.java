package com.dhcc.yangmingci.adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.dataEntity.MonthData;
import com.dhcc.yangmingci.listener.OnItemClickListener;
import com.dhcc.yangmingci.util.MyApp;
import com.wx.wheelview.widget.WheelViewDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengbangqin on 16-10-17.
 */
public class RelicDetailAdapter extends RecyclerView.Adapter{
    //数据源
    private List<MonthData> list;

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    Context context;
    //构造函数
    public RelicDetailAdapter(List<MonthData> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //在onCreateViewHolder方法中，我们要根据不同的ViewType来返回不同的ViewHolder
        //对于Body中的item，我们也返回所对应的ViewHolder
        return new BodyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        //其他条目中的逻辑在此
        MonthData monthData=list.get(position);
        ((BodyViewHolder) holder).setOnItemClickListener(mOnItemClickListener);
        ((BodyViewHolder) holder).tv_month.setText(monthData.getMonth());
        ((BodyViewHolder) holder).tv_error.setText("隐患:"+monthData.getcCount());
        ((BodyViewHolder) holder).tv_coorect.setText("整改:"+monthData.getCrCount());

        if(monthData.getcCount()==0&&monthData.getCrCount()==0){
            ((BodyViewHolder) holder).LL.setBackgroundColor(Color.parseColor("#B0F9B0"));
        }else{
            ((BodyViewHolder) holder).LL.setBackgroundColor(Color.parseColor("#ffff00"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 给GridView中的条目用的ViewHolder，里面只有一个TextView
     */
    public class BodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_month;
        private TextView tv_error;
        private TextView tv_coorect;
        private LinearLayout LL;
        OnItemClickListener mOnItemClickListener;
        public BodyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
            tv_error = (TextView) itemView.findViewById(R.id.tv_error);
            tv_coorect = (TextView) itemView.findViewById(R.id.tv_coorect);
            LL = (LinearLayout) itemView.findViewById(R.id.LL);
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
}

