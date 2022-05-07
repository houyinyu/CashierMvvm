package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 功能介绍 :销售记录查询订单返回
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/22
 */
public class RecordOrderResultBean {


    @SerializedName("result")
    private Integer result;
    @SerializedName("action")
    private String action;
    @SerializedName("message")
    private String message;
    @SerializedName("detail")
    private String detail;
    @SerializedName("resultObject")
    private ResultObjectDTO resultObject;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ResultObjectDTO getResultObject() {
        return resultObject;
    }

    public void setResultObject(ResultObjectDTO resultObject) {
        this.resultObject = resultObject;
    }

    public static class ResultObjectDTO {
        @SerializedName("pageindex")
        private PageindexDTO pageindex;
        @SerializedName("total")
        private Integer total;
        @SerializedName("pageSize")
        private Integer pageSize;
        @SerializedName("page")
        private Integer page;
        @SerializedName("records")
        private Integer records;
        @SerializedName("startPage")
        private Integer startPage;
        @SerializedName("pagecode")
        private Integer pagecode;
        @SerializedName("firstResult")
        private Integer firstResult;
        @SerializedName("rows")
        private List<RowsDTO> rows;

        public PageindexDTO getPageindex() {
            return pageindex;
        }

        public void setPageindex(PageindexDTO pageindex) {
            this.pageindex = pageindex;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getRecords() {
            return records;
        }

        public void setRecords(Integer records) {
            this.records = records;
        }

        public Integer getStartPage() {
            return startPage;
        }

        public void setStartPage(Integer startPage) {
            this.startPage = startPage;
        }

        public Integer getPagecode() {
            return pagecode;
        }

        public void setPagecode(Integer pagecode) {
            this.pagecode = pagecode;
        }

        public Integer getFirstResult() {
            return firstResult;
        }

        public void setFirstResult(Integer firstResult) {
            this.firstResult = firstResult;
        }

        public List<RowsDTO> getRows() {
            return rows;
        }

        public void setRows(List<RowsDTO> rows) {
            this.rows = rows;
        }

        public static class PageindexDTO {
            @SerializedName("startindex")
            private String startindex;
            @SerializedName("endindex")
            private String endindex;

            public String getStartindex() {
                return startindex;
            }

            public void setStartindex(String startindex) {
                this.startindex = startindex;
            }

            public String getEndindex() {
                return endindex;
            }

            public void setEndindex(String endindex) {
                this.endindex = endindex;
            }
        }

        public static class RowsDTO {
            @SerializedName("ordNo")
            private String ordNo;
            @SerializedName("ordPrice")
            private String ordPrice;
            @SerializedName("ordIsPayed")
            private Object ordIsPayed;
            @SerializedName("isBack")
            private Integer isBack;
            @SerializedName("ordBackPrice")
            private String ordBackPrice;
            @SerializedName("createTime")
            private String createTime;

            private int ordStatus;
            private boolean check;

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }

            public int getOrdStatus() {
                return ordStatus;
            }

            public void setOrdStatus(int ordStatus) {
                this.ordStatus = ordStatus;
            }

            public String getOrdNo() {
                if (ordNo.equals("")) return "";
                return ordNo;
            }

            public void setOrdNo(String ordNo) {
                this.ordNo = ordNo;
            }

            public String getOrdPrice() {
                if (ordPrice.equals("")) return "";
                return ordPrice;
            }

            public void setOrdPrice(String ordPrice) {
                this.ordPrice = ordPrice;
            }

            public Object getOrdIsPayed() {
                if (ordIsPayed.equals("")) return "";
                return ordIsPayed;
            }

            public void setOrdIsPayed(Object ordIsPayed) {
                this.ordIsPayed = ordIsPayed;
            }

            public Integer getIsBack() {
                return isBack;
            }

            public void setIsBack(Integer isBack) {
                this.isBack = isBack;
            }

            public String getOrdBackPrice() {
                if (ordBackPrice.equals("")) return "";
                return ordBackPrice;
            }

            public void setOrdBackPrice(String ordBackPrice) {
                this.ordBackPrice = ordBackPrice;
            }

            public String getCreateTime() {
                if (createTime.equals("")) return "";
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
