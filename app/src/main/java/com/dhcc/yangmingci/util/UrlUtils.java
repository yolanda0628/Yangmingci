package com.dhcc.yangmingci.util;

/**
 * 访问服务器的URL
 * Created by pengbangqin on 16-10-12.
 */
public class UrlUtils {
    /**
     * 服务器地址
     */
    public static final String SERVER = "http://139.224.44.12/console/relic/";
    /**
     * 图片地址
     */
    public static final String URL_IMG = "http://139.224.44.12/console";
    /**
     * 登录
     */
    public static final String URL_LOGIN = SERVER + "login.action";
    /**
     * 注册
     */
    public static final String URL_REGISTER = SERVER + "register.action";
    /**
     * 获取文保信息
     */
    public static final String URL_INFO = SERVER + "getOrg.action";
    /**
     * 文物信息录入
     */
    public static final String URL_RELIC_SAVE = SERVER+"addRelic.action";
    /**
     * 获取文物信息
     */
    public static final String URL_GET_RELIC = SERVER + "getRelic.action";
    /**
     * 删除文物信息
     */
    public static final String URL_DELETE_RELIC = SERVER + "deleteRelic.action";
    /**
     * 检查记录信息录入
     */
    public static final String URL_CHECK = SERVER + "addCheckInfo.action";
    /**
     * 整改信息录入
     */
    public static final String URL_CORRECT = SERVER + "addService.action";
    /**
     * 查询某段时间隐患数和整改数
     */
    public static final String URL_GET_COUNT= SERVER + "getCheckCount.action";
    /**
     * 获取我的检查记录信息
     */
    public static final String URL_GET_CHECKINFO = SERVER + "getCheckList.action";
    /**
     * 获取有隐患的检查记录信息
     */
    public static final String URL_GET_CHOICE_CHECKINFO = SERVER + "getChoiceCheckList.action";
    /**
     * 获取有隐患的检查记录信息
     */
    public static final String URL_GET_INDEX_COUNT = SERVER + "getIndexCount.action";
}
