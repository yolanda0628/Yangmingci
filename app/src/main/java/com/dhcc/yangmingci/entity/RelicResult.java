package com.dhcc.yangmingci.entity;

import java.util.List;

/**
 * 访问服务器返回文物信息结果的实体类
 * Created by pengbangqin on 16-10-9.
 */
public class RelicResult {
    int code;
    //通过@SerializedName注解将list对应起来
//	@SerializedName("list")
    List<Relic> list;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public List<Relic> getList() {
        return list;
    }
    public void setList(List<Relic> list) {
        this.list = list;
    }
}