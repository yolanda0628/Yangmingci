package com.dhcc.yangmingci.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.adpter.MyCheckAdapter;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.entity.CheckInfo;
import com.dhcc.yangmingci.entity.CheckInfoResult;
import com.dhcc.yangmingci.listener.OnItemClickListener;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.UrlUtils;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by pengbangqin on 2016/8/3.
 */
public class Fragment3 extends Fragment {
    /**
     * 下拉刷新控件
     */
    private PtrClassicFrameLayout ptr;
    private RecyclerView mRecyclerView;
    MyCheckAdapter adapter;
    /**
     * 检查记录信息的list集合
     */
    List<CheckInfo> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.fragment3,null);

        ptr=(PtrClassicFrameLayout) view.findViewById(R.id.ptr);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//        mRecyclerView.addItemDecoration(new ListViewDecoration(this));// 添加分割线。
        //不加载更多
        getMyCheckInfo(false);

        ptr.setLastUpdateTimeRelateObject(this);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //加载更多
                getMyCheckInfo(true);
            }
        });
        return view;
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
     * 获取我的检查记录
     */
    private void getMyCheckInfo(final boolean isMore) {
        /**
         * 获取从Fragment2传递过来的值.
         * 判断是直接进来的,还是从检查记录界面过来的.
         * 如果是前者 则url是根据某个文物来查询检查记录.
         * 反之,则查询所有的检查记录.
         */
        list=new ArrayList<>();
        OkHttpUtils.post(UrlUtils.URL_GET_CHECKINFO)//
                .tag(this)//
                .params("cmd","8")
                .params("uId",MyApp.userId)
                .execute(new StringDialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        //下拉刷新加载更多
                        if(isMore){
                            List<CheckInfo> more= gson.fromJson(s,CheckInfoResult.class).getList();
                            list.addAll(0,more);
                        }else{
                            //将JSON对象转换为结果实体对象
                            CheckInfoResult result=gson.fromJson(s,CheckInfoResult.class);
                            //获取数据成功
                            if(result.getCode()==0){
                                //获取全部的CheckInfo的集合
                                list=result.getList();
                            }else{
                                Toast.makeText(getActivity(),"获取检查记录信息失败",Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(list!=null){
                            adapter = new MyCheckAdapter(getActivity(),list);
                            adapter.setOnItemClickListener(onItemClickListener);
                            mRecyclerView.setAdapter(adapter);
                        }else{
                            Toast.makeText(getActivity(),"检查记录为空",Toast.LENGTH_SHORT).show();
                        }

                        //刷新完成
                        ptr.refreshComplete();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(getActivity(),"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取检查记录 刷新UI
        getMyCheckInfo(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
