package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 功能介绍 :销售记录订单列表查询
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/22
 */
public class RecordOrderQueryBean {

    @SerializedName("etime")
    private String etime;
    @SerializedName("ordNo")
    private String ordNo;
    @SerializedName("paging")
    private PagingDTO paging;
    @SerializedName("stime")
    private String stime;
    @SerializedName("syDeviceId")
    private String syDeviceId;

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public PagingDTO getPaging() {
        return paging;
    }

    public void setPaging(PagingDTO paging) {
        this.paging = paging;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getSyDeviceId() {
        return syDeviceId;
    }

    public void setSyDeviceId(String syDeviceId) {
        this.syDeviceId = syDeviceId;
    }

    public static class PagingDTO {

        @SerializedName("page")
        private Integer page;
        @SerializedName("pageSize")
        private Integer pageSize;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

    }
}
