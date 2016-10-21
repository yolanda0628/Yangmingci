package com.dhcc.yangmingci.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.callback.StringDialogCallback;
import com.dhcc.yangmingci.util.MyApp;
import com.dhcc.yangmingci.util.PictureUtil;
import com.dhcc.yangmingci.util.UrlUtils;
import com.dhcc.yangmingci.view.ClearEditText;
import com.lzy.okhttputils.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 文物信息录入
 * Created by pengbangqin on 16-9-29.
 */
public class RelicSaveActivity extends AppCompatActivity implements View.OnClickListener {
    String path;
    private ImageView iv;
    Spinner sp_rlevel,sp_rtype;
    String rName,rLevel,rDesc,rType;
    ClearEditText et_rname,et_rdesc;
    private Button btn_rpic,btn_save,btn_cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relicsave);
        initToolbar(R.id.toolbar,R.id.toolbar_title,"文物信息录入");

        iv = (ImageView) findViewById(R.id.iv);
        et_rname = (ClearEditText) findViewById(R.id.et_rname);
        sp_rlevel = (Spinner) findViewById(R.id.sp_rlevel);
        et_rdesc = (ClearEditText) findViewById(R.id.et_rdesc);
        sp_rtype = (Spinner) findViewById(R.id.sp_rtype);
        btn_rpic = (Button) findViewById(R.id.btn_rpic);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_rpic.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        /**
         * Spinner的选择事件
         */
        sp_rlevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 在选中之后触发
                rLevel=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /**
         * Spinner的选择事件
         */
        sp_rtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 在选中之后触发
                rType=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                String [] proj={MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, proj,null,null,null);
                if(cursor!=null){
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//找到图片的索引
                    if( cursor.getCount()>0 && cursor.moveToFirst() ){
                        path = cursor.getString(column_index);//获取路径
                        iv.setImageBitmap(bitmap);
                    }
                }
					/* 将Bitmap设定到ImageView */
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //添加图片
            case R.id.btn_rpic:
                Intent intent1 = new Intent(Intent.ACTION_PICK,null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 2);
                break;
            //保存逻辑
            case R.id.btn_save:
                rName=et_rname.getText().toString();
                rDesc=et_rdesc.getText().toString();
                if(!TextUtils.isEmpty(rName)&&!TextUtils.isEmpty(rLevel)&&!TextUtils.isEmpty(rDesc)
                        &&!TextUtils.isEmpty(rType)&&path!=null){
                    save(rName,rLevel,rDesc,rType,path);
                }else{
                    Toast.makeText(RelicSaveActivity.this, "必填信息不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            //返回
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    /**
     * 清空
     */
    private void refresh() {
        et_rname.setText("");
        sp_rlevel.setSelection(0);
        sp_rtype.setSelection(0);
        et_rdesc.setText("");
        iv.setImageBitmap(null);
    }

    /**
     * 保存文物信息到服务器上
     * @param rName
     * @param rLevel
     * @param rDesc
     * @param rType
     * @param path
     */
    private void save(String rName,String rLevel,String rDesc,String rType, String path) {
        List<File> files = new ArrayList<>();
        //临时文件集合
        List<File> temFiles=new ArrayList<>();
        files.add(new File(path));
        //文件压缩后再上传
        for(int i=0;i<files.size();i++){
            final String pic_path = files.get(i).getPath();
            String targetPath = Environment.getExternalStorageDirectory()+"/PBQRelic/compressPic"+i+".jpg";
            File f=new File(targetPath);
            if(!f.exists()){
                f.mkdir();
            }
            final String compressImage = PictureUtil.compressImage(pic_path, targetPath, 30);
            final File compressedPic = new File(compressImage);
            temFiles.add(compressedPic);
        }
        upload(temFiles,rName,rLevel,rDesc,rType);
    }

    /**
     * 上传图片
     * @param files
     * @param rName
     * @param rLevel
     * @param rDesc
     * @param rType
     */
    private void upload(List<File> files,String rName,String rLevel,String rDesc,String rType) {
        /**
         * 图片以文件的方式上传
         */
        OkHttpUtils.post(UrlUtils.URL_RELIC_SAVE)
                .tag(this)
                .params("cmd","3")
                .params("rName",rName)
                .params("rLevel",rLevel)
                .params("rDesc",rDesc)
                .params("rType",rType)
                .params("orgId", MyApp.orgId)
                .addFileParams("list", files)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            int code=jsonObject.getInt("code");
                            if(code==0){
                                String rid=jsonObject.getString("rid");
                                Toast.makeText(RelicSaveActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                                refresh();
                            }else {
                                Toast.makeText(RelicSaveActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(RelicSaveActivity.this,"访问服务器失败"+e+call,Toast.LENGTH_SHORT).show();
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
