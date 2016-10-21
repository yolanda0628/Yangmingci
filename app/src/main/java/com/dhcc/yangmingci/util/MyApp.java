package com.dhcc.yangmingci.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhcc.yangmingci.R;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheEntity;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.cookie.store.PersistentCookieStore;

import java.util.Calendar;

/**
 * Created by pengbangqin on 16-9-30.
 */
public class MyApp extends Application {
    /**
     * 文保id
     */
    public static String orgId="";
    /**
     * 文保名称
     */
    public static String orgName="";
    /**
     * 文保描述
     */
    public static String orgDesc="";
    /**
     * 文保地址
     */
    public static String orgAddress="";
    /**
     * 用户id
     */
    public static String userId="";
    /**
     * 用户名
     */
    public static String username="";
    public static String year="2016";

    @Override
    public void onCreate() {
        super.onCreate();
        //必须调用初始化
        OkHttpUtils.init(this);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkHttpUtils.getInstance()

                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkHttpUtils")

                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy0216/
                    .setCacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore());        //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置
//                    .setCertificates()                                  //方法一：信任所有证书（选一种即可）
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书（选一种即可）
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密

                    //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

