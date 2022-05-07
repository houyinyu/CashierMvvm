package com.hby.cashier.voice;


import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.StringUtils;

/**
 * @author 志尧
 * @date on 2018-01-12 15:12
 * @email 1417337180@qq.com
 * @describe 组合音频 实体类
 * @ideas 开头 + 金额 + 单位
 */

public class VoiceBuilder {
    //支付方式
    private String payMethod;
    //开头音频
    private String start;
    //播报金额
    private String money;
    //单位
    private String unit;
    //是否转成全数字。 默认人民币
    private boolean checkNum;

    public String getStart() {
        return start;
    }

    public String getMoney() {
        return money;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isCheckNum() {
        return checkNum;
    }

    public static class Builder {
        private String start;
        private String payMethod;
        private String money;
        private String unit;
        private boolean checkNum;

        public Builder payMethod(String payMethod) {
            this.payMethod = payMethod;
            if (payMethod .equals(EnumUtils.PayMethod.PAY_WXPAY) ) {
                this.start = VoiceConstants.WX_START;
            } else if (payMethod .equals(EnumUtils.PayMethod.PAY_ALIPAY) ) {
                this.start = VoiceConstants.ALI_START;
            }
            return this;
        }

        public Builder start(String start) {
            this.start = start;
            return this;
        }

        public Builder money(String money) {
            this.money = StringUtils.getMoney(money);
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder checkNum(boolean checkNum) {
            this.checkNum = checkNum;
            return this;
        }

        public VoiceBuilder builder() {
            return new VoiceBuilder(this);
        }
    }

    public VoiceBuilder(Builder builder) {
        this.payMethod = builder.payMethod;
        this.start = builder.start;
        this.money = builder.money;
        this.unit = builder.unit;
        this.checkNum = builder.checkNum;
    }
}
