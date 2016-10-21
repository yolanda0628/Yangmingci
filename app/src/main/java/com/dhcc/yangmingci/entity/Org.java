package com.dhcc.yangmingci.entity;

/**
 * Created by pengbangqin on 16-10-14.
 */
public class Org {
    String oId;
    String oName;
    String oAddress;
    String oDesc;

    public Org() {
    }


    public String getoId() {
        return oId;
    }

    public void setoId(String oId) {
        this.oId = oId;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public String getoAddress() {
        return oAddress;
    }

    public void setoAddress(String oAddress) {
        this.oAddress = oAddress;
    }

    public String getoDesc() {
        return oDesc;
    }

    public void setoDesc(String oDesc) {
        this.oDesc = oDesc;
    }
}
