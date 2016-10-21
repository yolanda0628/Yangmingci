package com.dhcc.yangmingci.index;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.util.MyApp;

/**
 * Created by pengbangqin on 16-9-22.
 */
public class Index2Fragment extends Fragment {
    EditText et1,et2,et3,et4,et5,et6;
    TextView name;
    TextView desc;
    TextView address;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=View.inflate(getActivity(), R.layout.index2_fragment,null);
        name= (TextView) rootView.findViewById(R.id.name);
        desc= (TextView) rootView.findViewById(R.id.desc);
        address= (TextView) rootView.findViewById(R.id.address);

        et1= (EditText) rootView.findViewById(R.id.et1);
        et2= (EditText) rootView.findViewById(R.id.et2);
        et3= (EditText) rootView.findViewById(R.id.et3);
        et4= (EditText) rootView.findViewById(R.id.et4);
        et5= (EditText) rootView.findViewById(R.id.et5);
        et6= (EditText) rootView.findViewById(R.id.et6);
        et1.setInputType(InputType.TYPE_NULL);
        et2.setInputType(InputType.TYPE_NULL);
        et3.setInputType(InputType.TYPE_NULL);
        et4.setInputType(InputType.TYPE_NULL);
        et5.setInputType(InputType.TYPE_NULL);
        et6.setInputType(InputType.TYPE_NULL);

        name.setText(MyApp.orgName);
        desc.setText("\t\t\t\t"+MyApp.orgDesc);
        address.setText("地址:"+MyApp.orgAddress);
        return rootView;
    }
}
