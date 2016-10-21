package com.dhcc.yangmingci.index;

import android.content.Intent;
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
import com.dhcc.yangmingci.activity.RelicDetailActivity;
import com.dhcc.yangmingci.adpter.RelicAdapter;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.entity.Relic;
import com.dhcc.yangmingci.entity.RelicResult;
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
 * Created by pengbangqin on 16-9-22.
 */
public class Index1Fragment extends Fragment {
    /**
     * 下拉刷新控件
     */
    private PtrClassicFrameLayout ptr;
    private RecyclerView mRecyclerView;

    private RelicAdapter mMenuAdapter;
    /**
     * 文物信息的list集合
     */
    List<Relic> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.index1_fragment,null);
        ptr=(PtrClassicFrameLayout) view.findViewById(R.id.ptr);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//        mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration(getActivity()));// 添加分割线。

        //不加载更多
        getRelicInfo(false);
        ptr.setLastUpdateTimeRelateObject(this);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //加载更多
                //获取文物信息
                getRelicInfo(true);
            }
        });

        return view;
    }

    /**
     * 获取文物的信息
     */
    private void getRelicInfo(final boolean isMore) {
        list=new ArrayList<>();
        OkHttpUtils.post(UrlUtils.URL_GET_RELIC)
                .tag(this)
                .params("cmd","5")
                .params("orgId",MyApp.orgId)
                .execute(new StringDialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson=new Gson();
                        //下拉刷新加载更多
                        if(isMore){
                            List<Relic> more= gson.fromJson(s,RelicResult.class).getList();
                            list.addAll(0,more);
                        }else{
                            //将JSON对象转换为结果实体对象
                            RelicResult result=gson.fromJson(s,RelicResult.class);
                            //获取数据成功
                            if(result.getCode()==0){
                                //获取全部的CheckInfo的集合
                                list=result.getList();
                            }else{
                                Toast.makeText(getActivity(),"获取文物信息失败",Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(list!=null){
                            mMenuAdapter = new RelicAdapter(getActivity(),list);
                            mMenuAdapter.setOnItemClickListener(onItemClickListener);
                            mRecyclerView.setAdapter(mMenuAdapter);
                        }else{
                            Toast.makeText(getActivity(),"文物信息为空",Toast.LENGTH_SHORT).show();
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

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent intent=new Intent(getActivity(),RelicDetailActivity.class);
            //传值
            intent.putExtra("rName",list.get(position).getrName());
            intent.putExtra("rId",list.get(position).getrId());
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //获取文物信息
        getRelicInfo(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}