package com.dhcc.yangmingci.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.event.EventBusEvents;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.PictureUtil;
import com.dhcc.yangmingci.util.UrlUtils;
import com.dhcc.yangmingci.view.ClearEditText;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.widget.ExpandGridView;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 检查记录
 * Created by pengbangqin on 16-9-29.
 */
public class CheckActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 存储图片信息的集合
     */
    private ArrayList<ImageItem> images;
    MyAdapter adapter;
    private ImagePicker imagePicker;
    private ExpandGridView egv;
    /**
     * 文物的id
     */
    String relicId;
    /**
     * 检查记录信息
     */
    String cDatetime,cDesc,cType;
    RadioGroup rg_ctype;
    RadioButton rb1,rb2;
    EditText et_cdatetime;
    ClearEditText et_cdesc;
    /**
     * 文物信息
     */
    TextView tv_rname,tv_rtype,tv_rdesc,tv_rlevel;
    private Button btn_rpic,btn_save,btn_cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        initToolbar(R.id.toolbar,R.id.toolbar_title,"检查记录上报");

        tv_rname = (TextView) findViewById(R.id.tv_rname);
        tv_rtype = (TextView) findViewById(R.id.tv_rtype);
        tv_rdesc = (TextView) findViewById(R.id.tv_rdesc);
        tv_rlevel = (TextView) findViewById(R.id.tv_rlevel);

        rg_ctype =(RadioGroup) findViewById(R.id.rg_ctype);
        rb1 =(RadioButton) findViewById(R.id.rb1);
        rb2 =(RadioButton) findViewById(R.id.rb2);

        egv = (ExpandGridView) findViewById(R.id.egv);
        et_cdatetime= (EditText) findViewById(R.id.et_cdatetime);
        et_cdesc = (ClearEditText) findViewById(R.id.et_cdesc);
        btn_rpic = (Button) findViewById(R.id.btn_rpic);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        btn_rpic.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //设置默认值
        rb1.setChecked(true);
        cType="1";
        rg_ctype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==rb1.getId()){
                    cType="1";
                }else{
                    cType="2";
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                //获取到选中图片数据
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                adapter = new MyAdapter(images);
                egv.setAdapter(adapter);
            }else {
                Toast.makeText(CheckActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }


        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            //截取字符串
            String s = new String(result);
            String a[] = s.split("_");
            String rName=a[0];
            String rId=a[1];
            //扫描成功后获取系统当前的时间
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便
            et_cdatetime.setText(dateFormat.format(now));
            getRelicInfo(rId,rName);
        }
    }

    /**
     * 扫描二维码获取文物的初始化信息
     * 应该不用文物id来获取信息,用文物名称
     * @param rId
     * @param rName
     */
    private void getRelicInfo(String rId,String rName) {
        OkHttpUtils.post(UrlUtils.URL_GET_RELIC)//
                .tag(this)//
                .params("cmd","4")
                .params("rId",rId)
                .params("rName",rName)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                String rId=jsonObject.getString("rId");
                                String rName=jsonObject.getString("rName");
                                String rDesc=jsonObject.getString("rDesc");
                                String rType=jsonObject.getString("rType");
                                String rLevel=jsonObject.getString("rLevel");
                                relicId=rId;

                                tv_rname.setText("文物名称: "+rName);
                                tv_rtype.setText("文物类型: "+rType);
                                tv_rdesc.setText("文物描述: "+rDesc);
                                tv_rlevel.setText("文保级别: "+rLevel);
                                Toast.makeText(CheckActivity.this,"信息初始化成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(CheckActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(CheckActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //添加图片
            case R.id.btn_rpic:
                //对ImagePicker的初始化 可以放到全局Application中
                imagePicker = ImagePicker.getInstance();
                //设置图片加载的方式
                imagePicker.setImageLoader(new GlideImageLoader());
                //设置是否显示相机
                imagePicker.setShowCamera(true);
                //设置选择图片的最大数量
                imagePicker.setSelectLimit(9);
                //设置不允许使用裁剪
                imagePicker.setCrop(false);
                //启动选择图片意图
                Intent intent = new Intent(CheckActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
            //保存逻辑
            case R.id.btn_save:
                cDatetime=et_cdatetime.getText().toString();
                cDesc=et_cdesc.getText().toString();
                if(!TextUtils.isEmpty(cDatetime)&&!TextUtils.isEmpty(cDesc)
                        &&!TextUtils.isEmpty(cType)&&images!=null){
                    if(relicId==null){
                        Toast.makeText(CheckActivity.this, "请先扫描二维码获取文物的基本信息..", Toast.LENGTH_SHORT).show();
                    }else {
                        checkSave(cDatetime,cDesc,cType,relicId);
                    }
                }else{
                    Toast.makeText(CheckActivity.this, "必填信息不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            //返回
            case R.id.btn_cancel:
                finish();
                break;
            //扫描二维码
            case R.id.fab:
                startActivityForResult(new Intent(CheckActivity.this, CaptureActivity.class), 0);
                break;
        }
    }

    /**
     * 刷新
     */
    private void refresh() {
        et_cdesc.setText("");
        et_cdatetime.setText("");
        if(images!=null){
            images.clear();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查记录上报逻辑
     * @param cDatetime
     * @param cDesc
     * @param cType
     * @param relicId
     */
    private void checkSave(String cDatetime, String cDesc, String cType,String relicId) {

        List<File> files = new ArrayList<>();
        //临时文件集合
        List<File> temFiles=new ArrayList<>();
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                files.add(new File(images.get(i).path));
                Log.i("pbq",images.get(i).path);
            }
        }
        //文件压缩后再上传
        for(int i=0;i<files.size();i++){
            final String pic_path = files.get(i).getPath();
            String targetPath = Environment.getExternalStorageDirectory()+"/PBQCheck/compressPic"+i+".jpg";
            File f=new File(targetPath);
            if(!f.exists()){
                f.mkdir();
            }
            final String compressImage = PictureUtil.compressImage(pic_path, targetPath, 30);
            final File compressedPic = new File(compressImage);
            temFiles.add(compressedPic);
        }
        upload(temFiles,cDatetime,cDesc,cType,relicId);
    }

    /**
     * 上传图片
     * @param files
     * @param cDatetime
     * @param cDesc
     * @param cType
     * @param relicId
     */
    private void upload(List<File> files, String cDatetime, String cDesc, String cType, String relicId) {
        OkHttpUtils.post(UrlUtils.URL_CHECK)
                .tag(this)
                .params("cmd","7")
                .params("cDatetime",cDatetime)
                .params("cDesc",cDesc)
                .params("cType",cType)
                .params("uId",MyApp.userId)
                .params("rId",relicId)
                .addFileParams("list", files)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                String cId=jsonObject.getString("cId");

                                Toast.makeText(CheckActivity.this,"上报成功",Toast.LENGTH_SHORT).show();

                                refresh();
                            }else {
                                Toast.makeText(CheckActivity.this,"上报失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Toast.makeText(CheckActivity.this,"访问服务器失败"+e,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.upProgress(currentSize, totalSize, progress, networkSpeed);
                        Log.i("zll",progress+"");
                    }
                });
    }

    private class MyAdapter extends BaseAdapter {

        private List<ImageItem> items;

        public MyAdapter(List<ImageItem> items) {
            this.items = items;
        }

        public void setData(List<ImageItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public ImageItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int size = egv.getWidth() / 3;
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(CheckActivity.this, R.layout.item_upload_manager, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(getActivity(),ImageShower.class);
//                    intent.putExtra("item",getItem(position).path);
//                    Log.i("zll","getItem"+getItem(position).path);
//                    startActivity(intent);
//                }
//            });
            //把选中图片的资源放在imageView中
            imagePicker.getImageLoader().displayImage(CheckActivity.this, getItem(position).path, holder.imageView, size, size);
            return convertView;
        }
    }

    private class ViewHolder {

        private ImageView imageView;

        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, egv.getWidth() / 3);
            imageView.setLayoutParams(params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
