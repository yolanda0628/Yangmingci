package com.dhcc.yangmingci.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.activity.CheckActivity;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.entity.CheckInfo;
import com.dhcc.yangmingci.entity.ChoiceCheck;
import com.dhcc.yangmingci.event.EventBusEvents;
import com.dhcc.yangmingci.util.UrlUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by pengbangqin on 16-10-9.
 */
public class SpAdapter extends BaseAdapter {
    Context context;
    List<ChoiceCheck> list;
    public SpAdapter(Context context, List<ChoiceCheck> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.sp_item,null);
            hodler=new ViewHodler();
            hodler.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            hodler.tv_datetime= (TextView) convertView.findViewById(R.id.tv_datetime);
            hodler.tv_rName= (TextView) convertView.findViewById(R.id.tv_rName);
            convertView.setTag(hodler);
        }else {
            hodler=(ViewHodler) convertView.getTag();
        }

        //根据索引获取CheckInfo对象
        ChoiceCheck check=list.get(position);
        hodler.tv_content.setText("检查内容:"+check.getcDesc());
        hodler.tv_datetime.setText("检查时间:"+check.getcDatetime());
        hodler.tv_rName.setText("文物名称:"+check.getrName());

        return convertView;
    }

    public class ViewHodler{
        TextView tv_content;
        TextView tv_require;
        TextView tv_datetime;
        TextView tv_rName;
    }

}

