package com.dhcc.yangmingci.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.UrlUtils;
import com.lzy.okhttputils.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by pengbangqin on 16-9-22.
 */
public class Index0Fragment extends Fragment {
    private TextView checkDay;
    private TextView relicCount;
    private TextView tCount;
    private TextView checkCount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.index0_fragment,null);
        checkDay= (TextView) view.findViewById(R.id.checkDay);
        relicCount= (TextView) view.findViewById(R.id.relicCount);
        tCount= (TextView) view.findViewById(R.id.cCount);
        checkCount= (TextView) view.findViewById(R.id.checkCount);
        getInfo();
        return view;
    }

    /**
     * 获取一些数据的信息
     */
    private void getInfo() {
        OkHttpUtils.post(UrlUtils.URL_GET_INDEX_COUNT)//
                .tag(this)//
                .params("cmd","15")
                .params("orgId", MyApp.orgId)
                .execute(new StringDialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                //检查次数
                                String cCount=jsonObject.getString("cCount");
                                checkCount.setText("检查的次数(次):"+cCount);
                                //安全检查天数
                                String dCount=jsonObject.getString("dCount");
                                checkDay.setText("安全检查天数(天):"+dCount);
                                //隐患次数
                                String t_Count=jsonObject.getString("tCount");
                                tCount.setText("安全隐患数(个):"+t_Count);
                                //文物的总数
                                String rCount=jsonObject.getString("rCount");
                                relicCount.setText("非移动文物个数(个):"+rCount);
                                Toast.makeText(getActivity(),"统计成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        getInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}