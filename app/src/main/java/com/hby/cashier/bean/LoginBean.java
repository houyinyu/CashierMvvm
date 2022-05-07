package com.hby.cashier.bean;


/**
 * 功能介绍:
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/25 17:28
 * 最后编辑: 2020/11/25 - Hyy
 *
 * @author HouYinYu
 */
public class LoginBean {
    private int result;
    private String action;
    private String message;
    private String detail;
    private ResultObjectBean resultObject;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
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

    public ResultObjectBean getResultObject() {
        return resultObject;
    }

    public void setResultObject(ResultObjectBean resultObject) {
        this.resultObject = resultObject;
    }

    public static class ResultObjectBean {
        private String sessionId;
        private boolean isRegister;//微信登录是否需要注册
        private String wxUnionid;//微信登录返回的uniID
        private String userLoginType;
        private UserBean user;

        public boolean isRegister() {
            return isRegister;
        }

        public void setRegister(boolean register) {
            isRegister = register;
        }

        public String getWxUnionid() {
            return wxUnionid;
        }

        public void setWxUnionid(String wxUnionid) {
            this.wxUnionid = wxUnionid;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getUserLoginType() {
            return userLoginType;
        }

        public void setUserLoginType(String userLoginType) {
            this.userLoginType = userLoginType;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            private String id;
            private String userName;
            private String userAccount;
            private String userPassword;
            private String userIsNb;
            private String userPhone;
            private String userDptId;
            private String userStatus;
            private String userJobsName;
            private String userPositionName;
            private long updatetime;
            private long createtime;
            private String mid;
            private String userIsDel;
            private String userDel;
            private String userIsUdatepass;
            private Object deletedstate;
            private String dptId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserAccount() {
                return userAccount;
            }

            public void setUserAccount(String userAccount) {
                this.userAccount = userAccount;
            }

            public String getUserPassword() {
                return userPassword;
            }

            public void setUserPassword(String userPassword) {
                this.userPassword = userPassword;
            }

            public String getUserIsNb() {
                return userIsNb;
            }

            public void setUserIsNb(String userIsNb) {
                this.userIsNb = userIsNb;
            }

            public String getUserPhone() {
                return userPhone;
            }

            public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
            }

            public String getUserDptId() {
                return userDptId;
            }

            public void setUserDptId(String userDptId) {
                this.userDptId = userDptId;
            }

            public String getUserStatus() {
                return userStatus;
            }

            public void setUserStatus(String userStatus) {
                this.userStatus = userStatus;
            }

            public String getUserJobsName() {
                return userJobsName;
            }

            public void setUserJobsName(String userJobsName) {
                this.userJobsName = userJobsName;
            }

            public String getUserPositionName() {
                return userPositionName;
            }

            public void setUserPositionName(String userPositionName) {
                this.userPositionName = userPositionName;
            }

            public long getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(long updatetime) {
                this.updatetime = updatetime;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getUserIsDel() {
                return userIsDel;
            }

            public void setUserIsDel(String userIsDel) {
                this.userIsDel = userIsDel;
            }

            public String getUserDel() {
                return userDel;
            }

            public void setUserDel(String userDel) {
                this.userDel = userDel;
            }

            public String getUserIsUdatepass() {
                return userIsUdatepass;
            }

            public void setUserIsUdatepass(String userIsUdatepass) {
                this.userIsUdatepass = userIsUdatepass;
            }

            public Object getDeletedstate() {
                return deletedstate;
            }

            public void setDeletedstate(Object deletedstate) {
                this.deletedstate = deletedstate;
            }

            public String getDptId() {
                return dptId;
            }

            public void setDptId(String dptId) {
                this.dptId = dptId;
            }
        }
    }
}
