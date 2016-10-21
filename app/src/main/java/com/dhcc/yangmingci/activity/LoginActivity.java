package com.dhcc.yangmingci.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dhcc.yangmingci.MainActivity;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.UrlUtils;
import com.dhcc.yangmingci.view.ClearEditText;
import com.dhcc.yangmingci.view.PasswordEditText;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity{

    private ClearEditText username;
    private PasswordEditText pwd;
    private TextInputLayout layout1;
    private TextInputLayout layout2;
    String name;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolbar(R.id.toolbar,R.id.toolbar_title,"登录");

        //获取注册页面传来的用户名密码
        Intent data=getIntent();
        if(data.getStringExtra("name")!=null){
            name=data.getStringExtra("name");
            password=data.getStringExtra("password");
            Log.i("pbq",name+password);
        }

        username = (ClearEditText) findViewById(R.id.name);
        layout1=(TextInputLayout) findViewById(R.id.layout1);
        layout2=(TextInputLayout) findViewById(R.id.layout2);
        pwd = (PasswordEditText) findViewById(R.id.pwd);
        pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        username.setText(name);
        pwd.setText(password);

        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);
        /**
         * 注册
         */
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 登录
         */
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pwd.startShakeAnimation();
                attemptLogin();
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
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    /**
     * 登录逻辑
     */
    private void attemptLogin() {
        layout1.setError(null);
        layout2.setError(null);

        String name = username.getText().toString();
        String password = pwd.getText().toString();

        if (TextUtils.isEmpty(password)) {
            layout2.setError("密码不能为空");
        }

        if (TextUtils.isEmpty(name)) {
            layout1.setError("用户名不能为空");
        }
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(password)){
            //登录逻辑 (访问网络)
            login(name,password);
        }

    }

    /**
     * 登录逻辑 访问网络操作
     * @param name
     * @param password
     */
    private void login(String name, String password) {
        OkHttpUtils.post(UrlUtils.URL_LOGIN)//
                .tag(this)//
                .params("cmd","0")
                .params("name",name)
                .params("password",password)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                String uid=jsonObject.getString("uid");
                                String username=jsonObject.getString("name");
                                String oId=jsonObject.getString("oId");
                                String oName=jsonObject.getString("oName");
                                String oAddress=jsonObject.getString("oAddress");
                                String oDesc=jsonObject.getString("oDesc");
                                MyApp.orgName=oName;
                                MyApp.orgAddress=oAddress;
                                MyApp.orgDesc=oDesc;
                                MyApp.orgId=oId;
                                MyApp.username=username;
                                MyApp.userId=uid;
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(LoginActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
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

