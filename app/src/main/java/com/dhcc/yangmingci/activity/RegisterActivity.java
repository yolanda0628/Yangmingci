package com.dhcc.yangmingci.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.yangmingci.MainActivity;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.adpter.SpAdapter;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.entity.CheckInfoResult;
import com.dhcc.yangmingci.entity.Org;
import com.dhcc.yangmingci.entity.OrgResult;
import com.dhcc.yangmingci.util.UrlUtils;
import com.dhcc.yangmingci.view.ClearEditText;
import com.dhcc.yangmingci.view.PasswordEditText;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by pengbangqin on 16-9-29.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 用户名
     */
    private ClearEditText et_user;
    /**
     * 密码
     */
    private PasswordEditText et_pwd;
    /**
     * 确认密码
     */
    private PasswordEditText et_repwd;
    /**
     * 联系电话
     */
    private ClearEditText et_phone;
    /**
     * email
     */
    private ClearEditText et_email;
    /**
     * 文保单位
     */
    private Spinner sp_org;
    String orgId;
    /**
     * 注册
     */
    private Button register;
    List<Org> list;
    List<String> items=new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initToolbar(R.id.toolbar,R.id.toolbar_title,"注册");

        //获取文保单位记录
        getOrgInfo();
        et_user=(ClearEditText) findViewById(R.id.et_user);
        et_pwd=(PasswordEditText) findViewById(R.id.et_pwd);
        et_repwd=(PasswordEditText) findViewById(R.id.et_repwd);
        et_phone=(ClearEditText) findViewById(R.id.et_phone);
        et_email=(ClearEditText) findViewById(R.id.et_email);
        sp_org=(Spinner) findViewById(R.id.sp_org);
        register=(Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        /**
         * Spinner的选择事件
         */
        sp_org.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 在选中之后触发
                String orgName=parent.getItemAtPosition(position).toString();
                orgId=list.get(position).getoId();
                Log.i("pbq","orgId=="+orgId+"orgName=="+orgName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 获取文保单位信息
     */
    private void getOrgInfo() {
        list=new ArrayList<>();
        OkHttpUtils.post(UrlUtils.URL_INFO)//
                .tag(this)//
                .params("cmd","13")
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        //将JSON对象转换为结果实体对象
                        OrgResult result=gson.fromJson(s,OrgResult.class);
                        //获取数据成功
                        if(result.getCode()==0){
                            //获取全部的Org的集合
                            list=result.getOrgList();
                        }else{
                            Toast.makeText(RegisterActivity.this,"获取检查记录信息失败",Toast.LENGTH_SHORT).show();
                        }
                        // 建立数据源
                        for (int i=0;i<list.size();i++){
                            items.add(list.get(i).getoName());
                        }
                        // 建立Adapter并且绑定数据源
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(RegisterActivity.this,android.R.layout.simple_expandable_list_item_1, items);
                        //将adapter 添加到spinner中
                        sp_org.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(RegisterActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        String name=et_user.getText().toString().trim();
        String password=et_pwd.getText().toString().trim();
        String repwd=et_repwd.getText().toString().trim();
        String phone=et_phone.getText().toString().trim();
        String email=et_email.getText().toString().trim();
        if(email.equals("")){
            email="";
        }
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(repwd)&&!TextUtils.isEmpty(orgId)){
            if(!password.equals(repwd)){
                Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            }else{
                regiseter(name,password,phone,email,orgId);
            }
        }else{
            Toast.makeText(RegisterActivity.this, "必填信息不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册逻辑
     * @param name
     * @param password
     * @param phone
     * @param email
     * @param orgId
     */
    private void regiseter(final String name, final String password, String phone, String email, final String orgId) {
        OkHttpUtils.post(UrlUtils.URL_REGISTER)
                .tag(this)
                .params("cmd","1")
                .params("name",name)
                .params("password",password)
                .params("phone",phone)
                .params("email",email)
                .params("orgId",orgId)
                .params("type","1")
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                String uid=jsonObject.getString("uid");
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("password",password);
                                finish();
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(RegisterActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
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
