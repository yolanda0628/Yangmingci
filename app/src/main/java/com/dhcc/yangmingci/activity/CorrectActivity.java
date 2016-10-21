package com.dhcc.yangmingci.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.adpter.SpAdapter;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.entity.CheckInfo;
import com.dhcc.yangmingci.entity.CheckInfoResult;
import com.dhcc.yangmingci.entity.ChoiceCheck;
import com.dhcc.yangmingci.entity.ChoiceCheckResult;
import com.dhcc.yangmingci.event.EventBusEvents;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.UrlUtils;
import com.dhcc.yangmingci.view.ClearEditText;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 整改填报
 * Created by pengbangqin on 16-9-30.
 */
public class CorrectActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 检查记录信息的list集合
     */
    List<ChoiceCheck> list;
    /**
     * 整改信息
     */
    String sStartTime,sEndTime,sDesc,sType;
    Spinner sp_stype;
    EditText et_require,et_limit;
    ClearEditText et_startTime,et_endTime,et_sdesc;
    private Button btn_correct,btn_cancel;
    long longStart= 0;
    long longEnd = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.correct_activity);
        initToolbar(R.id.toolbar,R.id.toolbar_title,"整改填报");

        //获取检查记录
        getCheckInfo();

        et_require= (EditText) findViewById(R.id.et_require);
        et_limit= (EditText) findViewById(R.id.et_limit);
        et_startTime= (ClearEditText) findViewById(R.id.et_startTime);
        et_endTime= (ClearEditText) findViewById(R.id.et_endTime);
        et_sdesc = (ClearEditText) findViewById(R.id.et_sdesc);
        sp_stype = (Spinner) findViewById(R.id.sp_stype);
        btn_correct = (Button) findViewById(R.id.btn_correct);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_correct.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        /**
         * Spinner的选择事件
         */
        sp_stype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 在选中之后触发
                sType=list.get(position).getcId();
                //选择之后获取整改要求和整改时限
                et_require.setText(list.get(position).getCrRequired());
                et_limit.setText(list.get(position).getCrLimit());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //不显示系统输入键盘
        et_startTime.setInputType(InputType.TYPE_NULL);
        et_endTime.setInputType(InputType.TYPE_NULL);
        et_startTime.setOnClickListener(this);
        et_endTime.setOnClickListener(this);
    }

    /**
     * 获取检查记录
     */
    private void getCheckInfo() {
        list=new ArrayList<>();
        OkHttpUtils.post(UrlUtils.URL_GET_CHOICE_CHECKINFO)//
                .tag(this)//
                .params("cmd","14")
                .params("orgId",MyApp.orgId)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        //将JSON对象转换为结果实体对象
                        ChoiceCheckResult result=gson.fromJson(s,ChoiceCheckResult.class);
                        //获取数据成功
                        if(result.getCode()==0){
                            //获取全部的CheckInfo的集合
                            list=result.getList();
                        }else{
                            Toast.makeText(CorrectActivity.this,"获取检查记录信息失败",Toast.LENGTH_SHORT).show();
                        }

                        if(list!=null){
                            SpAdapter adapter=new SpAdapter(CorrectActivity.this,list);
                            //将adapter 添加到spinner中
                            sp_stype.setAdapter(adapter);
                        }else{
                            Toast.makeText(CorrectActivity.this,"检查记录为空",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(CorrectActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
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
    public void onClick(final View v) {
        if(v.getId()==R.id.btn_correct){
            //整改信息录入
            sDesc=et_sdesc.getText().toString();
            sStartTime=et_startTime.getText().toString();
            sEndTime=et_endTime.getText().toString();
            if(!TextUtils.isEmpty(sDesc)&&!TextUtils.isEmpty(sStartTime)&&!TextUtils.isEmpty(sEndTime)){
                try {
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date start = sdf.parse(et_startTime.getText().toString());
                    Date end = sdf.parse(et_endTime.getText().toString());
                    longStart = start.getTime() / 1000;
                    longEnd = end.getTime() / 1000;
                    if(longStart>longEnd) {
                        Toast.makeText(CorrectActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                    }else{
                        correctSave(sStartTime,sEndTime,sDesc,sType);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(CorrectActivity.this, "必填信息不能为空", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId()==R.id.btn_cancel){
            finish();
        }else {
            final Calendar c = Calendar.getInstance();
            // 获取当前的年、月、日、小时、分钟
            new DatePickerDialog(CorrectActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                    new TimePickerDialog(CorrectActivity.this,new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker tp, int hourOfDay,
                                              int minute) {
                            if(v.getId()==R.id.et_startTime){
                                et_startTime.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth+
                                        " "+hourOfDay+":"+minute+":"+c.get(Calendar.SECOND));
                            }else{
                                et_endTime.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth+
                                        " "+hourOfDay+":"+minute+":"+c.get(Calendar.SECOND));
                            }
                        }
                        //设置初始时间  true表示采用24小时制
                    },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE), true).show();
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    /**
     * 刷新
     */
    private void refresh() {
        et_startTime.setText("");
        et_endTime.setText("");
        et_sdesc.setText("");
        sp_stype.setSelection(0);
    }

    /**
     * 整改信息保存到服务器
     * @param sStartTime
     * @param sEndTime
     * @param sDesc
     */
    private void correctSave(String sStartTime,String sEndTime, String sDesc,String sType) {
        OkHttpUtils.post(UrlUtils.URL_CORRECT)//
                .tag(this)//
                .params("cmd","9")
                .params("startTime",sStartTime)
                .params("endTime",sEndTime)
                .params("sDesc",sDesc)
                .params("uId",MyApp.userId)
                .params("cId",sType)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                String sId=jsonObject.getString("sId");

                                Toast.makeText(CorrectActivity.this,"整改录入成功",Toast.LENGTH_SHORT).show();

                                refresh();
                            }else {
                                Toast.makeText(CorrectActivity.this,"整改录入失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(CorrectActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
