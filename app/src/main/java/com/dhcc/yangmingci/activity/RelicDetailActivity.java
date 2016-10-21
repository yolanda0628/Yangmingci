package com.dhcc.yangmingci.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.adpter.MyCheckAdapter;
import com.dhcc.yangmingci.adpter.RelicDetailAdapter;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.dataEntity.CDesc;
import com.dhcc.yangmingci.dataEntity.DataResult;
import com.dhcc.yangmingci.dataEntity.DescResult;
import com.dhcc.yangmingci.dataEntity.MonthData;
import com.dhcc.yangmingci.dataEntity.SDesc;
import com.dhcc.yangmingci.listener.OnItemClickListener;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.UrlUtils;
import com.dhcc.yangmingci.view.MyItemDecoration;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.mylhyl.superdialog.SuperDialog;
import com.wx.wheelview.widget.WheelViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by pengbangqin on 16-10-17.
 */
public class RelicDetailActivity extends AppCompatActivity{
    /**
     * 从fragment3传来的文物id
     */
    String rId;
    RecyclerView rv;
    TextView tv_year;
    List<MonthData> list;
    String year;
    RelicDetailAdapter adapter;
    List<CDesc> cDescs;
    List<SDesc> sDescs;
    TextView tv_cdesc,tv_sdesc;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relic_detail_activity);
        //接受传来的值
        String rName=getIntent().getStringExtra("rName");
        rId=getIntent().getStringExtra("rId");
        initToolbar(R.id.toolbar,R.id.toolbar_title,rName+"隐患整改情况");

        //找到RecyclerView控件
        rv = (RecyclerView) findViewById(R.id.rv);
        tv_cdesc = (TextView) findViewById(R.id.tv_cdesc);
        tv_sdesc = (TextView) findViewById(R.id.tv_sdesc);
//        tv_cdesc.setText("隐患内容:null");
//        tv_sdesc.setText("\n整改内容:null");
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog dialog = new WheelViewDialog(RelicDetailActivity.this);
                dialog.setTitle("选择年份")
                        .setSelection(46)
                        .setItems(createArrays())
                        .setButtonText("确定")
                        .setDialogStyle(Color.parseColor("#6699ff"))
                        .setCount(5)
                        .setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                            @Override
                            public void onItemClick(int position, String s) {
                                tv_year.setText(s);
                                MyApp.year=s;
                            }
                        })
                        .show();
            }
        });

        year=tv_year.getText().toString();
        //获取每个月的隐患总数和整改总数
        getCountInfo();
        //数据
        list = new ArrayList<>();
        // 如果我们想要一个GridView形式的RecyclerView，那么在LayoutManager上我们就要使用GridLayoutManager
        // 实例化一个GridLayoutManager，列数为3
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        //把LayoutManager设置给RecyclerView
        rv.setLayoutManager(layoutManager);
        //把我们自定义的ItemDecoration设置给RecyclerView
//        rv.addItemDecoration(new MyItemDecoration());

    }

    /**
     * item的点击事件
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            cDescs=new ArrayList<>();
            sDescs=new ArrayList<>();
            //获取信息
            getDescInfo(list.get(position).getMonth());
        }
    };


    /**
     * 获取检查内容信息和整改信息
     * @param month
     *
     */
    private void getDescInfo(String month) {
        OkHttpUtils.get(UrlUtils.URL_GET_COUNT)//
                .tag(this)//
                .params("cmd","11")
                .params("rId",rId)
                .params("year",year)
                .params("month",month)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        DescResult result=gson.fromJson(s,DescResult.class);
                        //获取数据成功
                        if(result.getCode()==0){
                            cDescs=result.getcDescs();
                            sDescs=result.getsDescs();
                            //设置信息
                            setDescInfo(cDescs,sDescs);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(RelicDetailActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置检查内容信息和整改信息
     * @param cDescs
     * @param sDescs
     */
    private void setDescInfo(List<CDesc> cDescs, List<SDesc> sDescs) {
        StringBuilder csb=new StringBuilder();
        for (int i=0;i<cDescs.size();i++){
            csb.append("\n"+cDescs.get(i).getcDesc());
        }
        tv_cdesc.setText("隐患内容:"+csb);

        StringBuilder ssb=new StringBuilder();
        for (int i=0;i<sDescs.size();i++){
            ssb.append("\n"+sDescs.get(i).getsDesc());
        }
        tv_sdesc.setText("\n整改内容:"+ssb);
    }

    /**
     * 初始化toolbar
     * @param id
     * @param titleId
     * @param titleString
     * @return
     */
    public Toolbar initToolbar(int id, int titleId, String titleString) {
        Toolbar toolbar = (Toolbar) findViewById(id);
//        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(titleId);
        textView.setText(titleString);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://actionbar的左侧图标的点击事件处理
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 年份数据
     * @return
     */
    private ArrayList<String> createArrays() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1970; i < 3000; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 获取每个月的隐患总数和整改总数
     */
    private void getCountInfo() {
        OkHttpUtils.get(UrlUtils.URL_GET_COUNT)//
                .tag(this)//
                .params("cmd","11")
                .params("rId",rId)
                .params("year",year)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        DataResult result=gson.fromJson(s,DataResult.class);
                        //获取数据成功
                        if(result.getCode()==0){
                            //获取全部的CheckInfo的集合
                            list=result.getList();
                            Toast.makeText(RelicDetailActivity.this,"展示成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RelicDetailActivity.this,"展示失败",Toast.LENGTH_SHORT).show();
                        }

                        if(list!=null){
                            //实例化Adapter并且给RecyclerView设上
                            adapter = new RelicDetailAdapter(list,RelicDetailActivity.this);
                            adapter.setOnItemClickListener(onItemClickListener);
                            rv.setAdapter(adapter);
                        }else{
                            Toast.makeText(RelicDetailActivity.this,"检查记录为空",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(RelicDetailActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
