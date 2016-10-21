package com.dhcc.yangmingci.dataEntity;

import java.util.List;

/**
 * Created by pengbangqin on 16-10-20.
 */
public class DescResult {
    int code;
    List<CDesc> cDescs;
    List<SDesc> sDescs;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CDesc> getcDescs() {
        return cDescs;
    }

    public void setcDescs(List<CDesc> cDescs) {
        this.cDescs = cDescs;
    }

    public List<SDesc> getsDescs() {
        return sDescs;
    }

    public void setsDescs(List<SDesc> sDescs) {
        this.sDescs = sDescs;
    }
}
