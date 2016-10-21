package com.dhcc.yangmingci.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.listener.OnItemClickListener;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.UrlUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.mylhyl.superdialog.SuperDialog;
import com.wx.wheelview.widget.WheelViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by pengbangqin on 16-10-8.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_year;
    String tvs;
    /**
     * 从fragment3传来的文物id
     */
    String rId;
    /**
     * 每个月的隐患量
     */
    TextView tv_c1,tv_c2,tv_c3,tv_c4,tv_c5,tv_c6,tv_c7,tv_c8,tv_c9,tv_c10,tv_c11,tv_c12;
    /**
     * 每个月的整改量
     */
    TextView tv_cr1,tv_cr2,tv_cr3,tv_cr4,tv_cr5,tv_cr6,tv_cr7,tv_cr8,tv_cr9,tv_cr10,tv_cr11,tv_cr12;
    LinearLayout LL1,LL2,LL3,LL4,LL5,LL6,LL7,LL8,LL9,LL10,LL11,LL12;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        //接受传来的值
        String rName=getIntent().getStringExtra("rName");
        rId=getIntent().getStringExtra("rId");

        initToolbar(R.id.toolbar,R.id.toolbar_title,rName+"隐患整改情况");

        //绑定控件
        initView();
        //事件监听
        initEvent();
        //获取每个月的隐患总数和整改总数
        for(int i=1;i<=12;i++){
            String sTime=tv_year.getText().toString()+"-"+i+"-01 00:00:00";
            String eTime=tv_year.getText().toString()+"-"+i+"-31 00:00:00";

            getCountInfo(sTime,eTime,i);
        }

        tv_year.setText(MyApp.year);
        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewDialog dialog = new WheelViewDialog(DetailActivity.this);
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
     * 事件监听
     */
    private void initEvent() {
        LL1.setOnClickListener(this);
        LL2.setOnClickListener(this);
        LL3.setOnClickListener(this);
        LL4.setOnClickListener(this);
        LL5.setOnClickListener(this);
        LL6.setOnClickListener(this);
        LL7.setOnClickListener(this);
        LL8.setOnClickListener(this);
        LL9.setOnClickListener(this);
        LL10.setOnClickListener(this);
        LL11.setOnClickListener(this);
        LL12.setOnClickListener(this);
    }

    /**
     * 绑定控件
     */
    private void initView() {
        tv_c1=(TextView) findViewById(R.id.tv_c1);
        tv_c2=(TextView) findViewById(R.id.tv_c2);
        tv_c3=(TextView) findViewById(R.id.tv_c3);
        tv_c4=(TextView) findViewById(R.id.tv_c4);
        tv_c5=(TextView) findViewById(R.id.tv_c5);
        tv_c6=(TextView) findViewById(R.id.tv_c6);
        tv_c7=(TextView) findViewById(R.id.tv_c7);
        tv_c8=(TextView) findViewById(R.id.tv_c8);
        tv_c9=(TextView) findViewById(R.id.tv_c9);
        tv_c10=(TextView) findViewById(R.id.tv_c10);
        tv_c11=(TextView) findViewById(R.id.tv_c11);
        tv_c12=(TextView) findViewById(R.id.tv_c12);

        tv_cr1=(TextView) findViewById(R.id.tv_cr1);
        tv_cr2=(TextView) findViewById(R.id.tv_cr2);
        tv_cr3=(TextView) findViewById(R.id.tv_cr3);
        tv_cr4=(TextView) findViewById(R.id.tv_cr4);
        tv_cr5=(TextView) findViewById(R.id.tv_cr5);
        tv_cr6=(TextView) findViewById(R.id.tv_cr6);
        tv_cr7=(TextView) findViewById(R.id.tv_cr7);
        tv_cr8=(TextView) findViewById(R.id.tv_cr8);
        tv_cr9=(TextView) findViewById(R.id.tv_cr9);
        tv_cr10=(TextView) findViewById(R.id.tv_cr10);
        tv_cr11=(TextView) findViewById(R.id.tv_cr11);
        tv_cr12=(TextView) findViewById(R.id.tv_cr12);

        tv_year=(TextView) findViewById(R.id.tv_year);
        LL1= (LinearLayout) findViewById(R.id.LL1);
        LL2= (LinearLayout) findViewById(R.id.LL2);
        LL3= (LinearLayout) findViewById(R.id.LL3);
        LL4= (LinearLayout) findViewById(R.id.LL4);
        LL5= (LinearLayout) findViewById(R.id.LL5);
        LL6= (LinearLayout) findViewById(R.id.LL6);
        LL7= (LinearLayout) findViewById(R.id.LL7);
        LL8= (LinearLayout) findViewById(R.id.LL8);
        LL9= (LinearLayout) findViewById(R.id.LL9);
        LL10= (LinearLayout) findViewById(R.id.LL10);
        LL11= (LinearLayout) findViewById(R.id.LL11);
        LL12= (LinearLayout) findViewById(R.id.LL12);
    }

    /**
     * item的点击事件
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
        }
    };

    /**
     * 获取每个月的隐患总数和整改总数
     */
    private void getCountInfo(String startTime, String endTime, final int i) {
        Log.i("test","test");
        OkHttpUtils.get(UrlUtils.URL_GET_COUNT)//
                .tag(this)//
                .params("cmd","11")
                .params("rId",rId)
                .params("startTime",startTime)
                .params("endTime",endTime)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                //隐患总数
                                String cCount=jsonObject.getString("cCount");
                                //整改总数
                                String crCount=jsonObject.getString("crCount");
                                //给TextView设置值
                                setValues(i,cCount,crCount);

                                Toast.makeText(DetailActivity.this,"展示成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(DetailActivity.this,"展示失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(DetailActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 给TextView设置值和背景颜色
     * @param i
     * @param cCount
     * @param crCount
     */
    private void setValues(int i, String cCount, String crCount) {
        switch (i){
            case 1:
                tv_c1.setText("隐患:"+cCount);
                tv_cr1.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL1.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL1.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 2:
                tv_c2.setText("隐患:"+cCount);
                tv_cr2.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL2.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL2.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 3:
                tv_c3.setText("隐患:"+cCount);
                tv_cr3.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL3.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL3.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 4:
                tv_c4.setText("隐患:"+cCount);
                tv_cr4.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL4.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL4.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 5:
                tv_c5.setText("隐患:"+cCount);
                tv_cr5.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL5.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL5.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 6:
                tv_c6.setText("隐患:"+cCount);
                tv_cr6.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL6.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL6.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 7:
                tv_c7.setText("隐患:"+cCount);
                tv_cr7.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL7.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL7.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 8:
                tv_c8.setText("隐患:"+cCount);
                tv_cr8.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL8.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL8.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 9:
                tv_c9.setText("隐患:"+cCount);
                tv_cr9.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL9.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL9.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 10:
                tv_c10.setText("隐患:"+cCount);
                tv_cr10.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL10.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL10.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 11:
                tv_c11.setText("隐患:"+cCount);
                tv_cr11.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL11.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL11.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
            case 12:
                tv_c12.setText("隐患:"+cCount);
                tv_cr12.setText("整改:"+crCount);
                if(cCount.equals("0")&&crCount.equals("0")){
                    LL12.setBackgroundColor(Color.parseColor("#B0F9B0"));
                }else{
                    LL12.setBackgroundColor(Color.parseColor("#ffff00"));
                }
                break;
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LL1:
                tvs="1月\n"+tv_c1.getText().toString()+"\n"+tv_cr1.getText().toString();
                break;
            case R.id.LL2:
                tvs="2月\n"+tv_c2.getText().toString()+"\n"+tv_cr2.getText().toString();
                break;
            case R.id.LL3:
                tvs="3月\n"+tv_c3.getText().toString()+"\n"+tv_cr3.getText().toString();
                break;
            case R.id.LL4:
                tvs="4月\n"+tv_c4.getText().toString()+"\n"+tv_cr4.getText().toString();
                break;
            case R.id.LL5:
                tvs="5月\n"+tv_c5.getText().toString()+"\n"+tv_cr5.getText().toString();
                break;
            case R.id.LL6:
                tvs="6月\n"+tv_c6.getText().toString()+"\n"+tv_cr6.getText().toString();
                break;
            case R.id.LL7:
                tvs="7月\n"+tv_c7.getText().toString()+"\n"+tv_cr7.getText().toString();
                break;
            case R.id.LL8:
                tvs="8月\n"+tv_c8.getText().toString()+"\n"+tv_cr8.getText().toString();
                break;
            case R.id.LL9:
                tvs="9月\n"+tv_c9.getText().toString()+"\n"+tv_cr9.getText().toString();
                break;
            case R.id.LL10:
                tvs="10月\n"+tv_c10.getText().toString()+"\n"+tv_cr10.getText().toString();
                break;
            case R.id.LL11:
                tvs="11月\n"+tv_c11.getText().toString()+"\n"+tv_cr11.getText().toString();
                break;
            case R.id.LL12:
                tvs="12月\n"+tv_c12.getText().toString()+"\n"+tv_cr12.getText().toString();
                break;
        }
        new SuperDialog.Builder(this)
                .setRadius(10)
                .setTitle(tv_year.getText().toString()+"年情况汇总")
                .setMessage(tvs)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
