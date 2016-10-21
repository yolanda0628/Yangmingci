package com.dhcc.yangmingci.entity;

import java.util.List;

/**
 * Created by pengbangqin on 16-10-14.
 */
public class ChoiceCheckResult {
    int code;
    List<ChoiceCheck> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ChoiceCheck> getList() {
        return list;
    }

    public void setList(List<ChoiceCheck> list) {
        this.list = list;
    }
}
