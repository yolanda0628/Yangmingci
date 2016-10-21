package com.dhcc.yangmingci.dataEntity;


import java.util.List;

/**
 * Created by pengbangqin on 16-10-19.
 * 月份统计数据的结果
 */
public class DataResult {
    int code;
    List<MonthData> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MonthData> getList() {
        return list;
    }

    public void setList(List<MonthData> list) {
        this.list = list;
    }
}
