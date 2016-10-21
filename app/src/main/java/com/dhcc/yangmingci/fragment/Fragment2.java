package com.dhcc.yangmingci.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.activity.CheckActivity;
import com.dhcc.yangmingci.activity.CorrectActivity;
import com.dhcc.yangmingci.activity.RelicSaveActivity;
import com.dhcc.yangmingci.event.EventBusEvents;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by pengbangqin on 2016/8/3.
 */
public class Fragment2 extends Fragment implements AdapterView.OnItemClickListener {
    private View view;
    private ListView lv;
    private String[]str={"文物信息录入","检查记录上报","安全整改任务"};
    private int[]imgs={R.mipmap.lr,R.mipmap.jc,R.mipmap.zg};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=View.inflate(getActivity(), R.layout.fragment2,null);
        lv=(ListView)view. findViewById(R.id.lv);
        MyAdapter adapter=new MyAdapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(),RelicSaveActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(),CheckActivity.class));
                break;
            case 2:
                Intent intent=new Intent(getActivity(),CorrectActivity.class);
                startActivity(intent);
                break;
        }
    }


    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View v=inflater.inflate(R.layout.lv_item, null);
            ImageView iv=(ImageView) v.findViewById(R.id.iv);
            TextView tv=(TextView) v.findViewById(R.id.tv);

            iv.setImageResource(imgs[position]);
            tv.setText(str[position]);

            return v;
        }

    }
}