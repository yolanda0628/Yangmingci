package com.dhcc.yangmingci.entity;

import java.util.List;

/**
 * 文保单位信息
 * Created by pengbangqin on 16-10-14.
 */
public class OrgResult {
    int code;
    //通过@SerializedName注解将list对应起来
//	@SerializedName("list")
    List<Org> orgList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Org> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<Org> orgList) {
        this.orgList = orgList;
    }
}
