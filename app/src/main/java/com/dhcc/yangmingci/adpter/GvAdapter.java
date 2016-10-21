package com.dhcc.yangmingci.adpter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.entity.Relic;
import com.dhcc.yangmingci.util.UrlUtils;

import java.util.List;

/**
 * Created by pengbangqin on 16-9-21.
 */
public class GvAdapter extends BaseAdapter {
    Context context;
    List<Relic> list;
    public GvAdapter(Context context, List<Relic> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler hodler=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.gv_item,null);
            hodler=new ViewHodler();
            hodler.iv= (ImageView) convertView.findViewById(R.id.iv);
            hodler.tv= (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(hodler);
        }else {
            hodler=(ViewHodler) convertView.getTag();
        }

        //根据索引获取CheckInfo对象
        Relic relic=list.get(position);
        hodler.tv.setText(relic.getrName());
        for(int i=0;i<relic.getFiles().size();i++){
            //获取图片路径
            String picPath=relic.getFiles().get(i).getfPath();
            //加载图片
            String url= UrlUtils.URL_IMG+picPath;
            Glide.with(context).load(url)
                    .placeholder(R.mipmap.default_image)
                    .into(hodler.iv);
        }
        return convertView;
    }

    public class ViewHodler{
        ImageView iv;
        TextView tv;
    }
}
