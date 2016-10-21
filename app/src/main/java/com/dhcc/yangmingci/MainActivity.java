package com.dhcc.yangmingci;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.yangmingci.fragment.Fragment1;
import com.dhcc.yangmingci.fragment.Fragment2;
import com.dhcc.yangmingci.fragment.Fragment3;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pengbangqin on 2016/8/3.
 */
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg;
    private List<Fragment> list;
    /**
     * 选中的Fragment的对应的位置
     */
    private int position;
    /**
     * 记录上次切换的Fragment
     */
    private Fragment mFragment;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("");
        textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("首页");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }


        rg= (RadioGroup) findViewById(R.id.rg);
        //初始化Fragment
        initFragment();
        //注册监听
        rg.setOnCheckedChangeListener(this);
        //设置默认选中第一个
        rg.check(R.id.rb1);
    }


    private void initFragment() {
        list=new ArrayList<Fragment>();
        list.add(new Fragment1());
        list.add(new Fragment2());
        list.add(new Fragment3());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //切换Fragment
        changeFragment(checkedId);
    }

    private void changeFragment(int checkedId) {
        switch (checkedId){
            case R.id.rb1:
                position=0;
                textView.setText("首页");
                break;
            case R.id.rb2:
                position=1;
                textView.setText("检查上报");
                break;
            case R.id.rb3:
                position=2;
                textView.setText("我的记录");
                break;
        }
        //根据位置得到对应的Fragment
        Fragment to = list.get(position);
        switchFragment(mFragment,to);
    }

    /**
     *
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to 马上要切换到的Fragment，一会要显示
     */
    private void switchFragment(Fragment from,Fragment to) {
        if(from != to){
            mFragment = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换 ,判断有没有被添加
            if(!to.isAdded()){
                //to没有被添加,from隐藏
                if(from != null) ft.hide(from);
                //添加to
                if(to != null)   ft.add(R.id.fl_content,to).commit();
            }else{
                //to已经被添加,from隐藏
                if(from != null) ft.hide(from);
                //显示to
                if(to != null)  ft.show(to).commit();
            }
        }
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        //取去原有的父类方法 重写方法
        //super.onBackPressed();
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.error)
                .setTitle("退出应用")
                .setMessage("是否退出应用？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();//结束当前Activity
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        System.exit(0);// 退出程序
                    }
                }).create().show();
    }
}
