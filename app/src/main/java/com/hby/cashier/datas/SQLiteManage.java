package com.hby.cashier.datas;

import com.alibaba.fastjson.JSON;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.LitePalCategoryBean;
import com.hby.cashier.bean.LitePalConfigurationBean;
import com.hby.cashier.bean.LitePalEventGiftBean;
import com.hby.cashier.bean.LitePalEventInfoBean;
import com.hby.cashier.bean.LitePalEventProductBean;
import com.hby.cashier.bean.LitePalEventRuleBean;
import com.hby.cashier.bean.LitePalProGroupBean;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.bean.LitePalUserBean;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.ZipUtils;
import com.hyy.mvvm.utils.SPUtils;

import org.litepal.LitePal;

import java.util.List;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class SQLiteManage {

    public SQLiteManage() {
        saveCategoryData();
    }

    public void initData() {
        saveCategoryData();
        saveProGroupData();
        saveProductData();
        saveUserData();
        saveConfigurationData();
        saveEventGiftData();
        saveEventInfoData();
        saveEventProductData();
        saveEventRuleData();
    }

    /**
     * 获取并分类信息并保存数据库
     */
    public void saveCategoryData() {
        String categoryJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("baseDptClass.txt"));
        List<LitePalCategoryBean> categoryList = JSON.parseArray(categoryJson, LitePalCategoryBean.class);
        //先查询是否存在
        List<LitePalCategoryBean> selectCategoryList = LitePal.findAll(LitePalCategoryBean.class);
        if (selectCategoryList != null && selectCategoryList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(categoryList);

            //把原本saveID为1的改为2
            LitePalCategoryBean categoryOldBean = new LitePalCategoryBean();
            categoryOldBean.setSaveID(2);
            categoryOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalCategoryBean categoryNewBean = new LitePalCategoryBean();
            categoryNewBean.setSaveID(1);
            categoryNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalCategoryBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(categoryList);
            //保存的saveID为1
            LitePalCategoryBean categoryBean = new LitePalCategoryBean();
            categoryBean.setSaveID(1);
            categoryBean.updateAll();
        }
    }

    /**
     * 获取商品和分类的中间表并保存数据库
     */
    public void saveProGroupData() {
        String userJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("proGroup.txt"));
        List<LitePalProGroupBean> proGroupList = JSON.parseArray(userJson, LitePalProGroupBean.class);
        //先查询是否存在
        List<LitePalProGroupBean> selectProGroupList = LitePal.findAll(LitePalProGroupBean.class);
        if (selectProGroupList != null && selectProGroupList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(proGroupList);

            //把原本saveID为1的改为2
            LitePalProGroupBean proGroupOldBean = new LitePalProGroupBean();
            proGroupOldBean.setSaveID(2);
            proGroupOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalProGroupBean proGroupNewBean = new LitePalProGroupBean();
            proGroupNewBean.setSaveID(1);
            proGroupNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalProGroupBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(proGroupList);
            //保存的saveID为1
            LitePalProGroupBean userBean = new LitePalProGroupBean();
            userBean.setSaveID(1);
            userBean.updateAll();
        }
    }

    /**
     * 获取商品信息并保存数据库
     */
    public void saveProductData() {
        String productJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("goods.txt"));
        List<LitePalProductBean> productList = JSON.parseArray(productJson, LitePalProductBean.class);
        //先查询是否存在
        List<LitePalProductBean> selectProductList = LitePal.findAll(LitePalProductBean.class);
        if (selectProductList != null && selectProductList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(productList);

            //把原本saveID为1的改为2
            LitePalProductBean productOldBean = new LitePalProductBean();
            productOldBean.setSaveID(2);
            productOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalProductBean productNewBean = new LitePalProductBean();
            productNewBean.setSaveID(1);
            productNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalProductBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(productList);
            //保存的saveID为1
            LitePalProductBean productBean = new LitePalProductBean();
            productBean.setSaveID(1);
            productBean.updateAll();
        }
    }

    /**
     * 获取登录用户信息并保存数据库
     */
    public void saveUserData() {
        String userJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("persons.txt"));
        List<LitePalUserBean> userList = JSON.parseArray(userJson, LitePalUserBean.class);
        //先查询是否存在
        List<LitePalUserBean> selectUserList = LitePal.findAll(LitePalUserBean.class);
        if (selectUserList != null && selectUserList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(userList);

            //把原本saveID为1的改为2
            LitePalUserBean userOldBean = new LitePalUserBean();
            userOldBean.setSaveID(2);
            userOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalUserBean userNewBean = new LitePalUserBean();
            userNewBean.setSaveID(1);
            userNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalUserBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(userList);
            //保存的saveID为1
            LitePalUserBean userBean = new LitePalUserBean();
            userBean.setSaveID(1);
            userBean.updateAll();
        }
    }

    /**
     * 获取配置信息并保存数据库
     * (这个每个设备只有一个)
     */
    public void saveConfigurationData() {
        String configurationJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("discounts.txt"));
        List<LitePalConfigurationBean> configurationList = JSON.parseArray(configurationJson, LitePalConfigurationBean.class);
        //先查询是否存在
        List<LitePalConfigurationBean> selectConfigurationList = LitePal.findAll(LitePalConfigurationBean.class);
        if (selectConfigurationList != null && selectConfigurationList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(configurationList);

            //把原本saveID为1的改为2
            LitePalConfigurationBean configurationOldBean = new LitePalConfigurationBean();
            configurationOldBean.setSaveID(2);
            configurationOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalConfigurationBean configurationNewBean = new LitePalConfigurationBean();
            configurationNewBean.setSaveID(1);
            configurationNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalConfigurationBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(configurationList);
            //保存的saveID为1
            LitePalConfigurationBean configurationBean = new LitePalConfigurationBean();
            configurationBean.setSaveID(1);
            configurationBean.updateAll();
        }

    }

    /**
     * 获取活动赠品并保存数据库
     */
    public void saveEventGiftData() {
        String eventGiftJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("eventGift.txt"));
        List<LitePalEventGiftBean> eventGiftList = JSON.parseArray(eventGiftJson, LitePalEventGiftBean.class);
        //先查询是否存在
        List<LitePalEventGiftBean> selectEventGiftList = LitePal.findAll(LitePalEventGiftBean.class);
        if (selectEventGiftList != null && selectEventGiftList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(eventGiftList);

            //把原本saveID为1的改为2
            LitePalEventGiftBean eventGiftOldBean = new LitePalEventGiftBean();
            eventGiftOldBean.setSaveID(2);
            eventGiftOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalEventGiftBean eventGiftNewBean = new LitePalEventGiftBean();
            eventGiftNewBean.setSaveID(1);
            eventGiftNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalEventGiftBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(eventGiftList);
            //保存的saveID为1
            LitePalEventGiftBean eventGiftBean = new LitePalEventGiftBean();
            eventGiftBean.setSaveID(1);
            eventGiftBean.updateAll();
        }
    }

    /**
     * 获取活动信息并保存数据库
     */
    public void saveEventInfoData() {
        String eventInfoJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("eventInfo.txt"));
        List<LitePalEventInfoBean> eventInfoList = JSON.parseArray(eventInfoJson, LitePalEventInfoBean.class);
        //先查询是否存在
        List<LitePalEventInfoBean> selectEventInfoList = LitePal.findAll(LitePalEventInfoBean.class);
        if (selectEventInfoList != null && selectEventInfoList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(eventInfoList);

            //把原本saveID为1的改为2
            LitePalEventInfoBean eventInfoOldBean = new LitePalEventInfoBean();
            eventInfoOldBean.setSaveID(2);
            eventInfoOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalEventInfoBean eventInfoNewBean = new LitePalEventInfoBean();
            eventInfoNewBean.setSaveID(1);
            eventInfoNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalEventInfoBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(eventInfoList);
            //保存的saveID为1
            LitePalEventInfoBean eventInfoBean = new LitePalEventInfoBean();
            eventInfoBean.setSaveID(1);
            eventInfoBean.updateAll();
        }
    }

    /**
     * 获取活动商品关联并保存数据库
     */
    public void saveEventProductData() {
        String eventProductJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("eventProduct.txt"));
        List<LitePalEventProductBean> eventProductList = JSON.parseArray(eventProductJson, LitePalEventProductBean.class);
        //先查询是否存在
        List<LitePalEventProductBean> selectEventProductList = LitePal.findAll(LitePalEventProductBean.class);
        if (selectEventProductList != null && selectEventProductList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(eventProductList);

            //把原本saveID为1的改为2
            LitePalEventProductBean eventProductOldBean = new LitePalEventProductBean();
            eventProductOldBean.setSaveID(2);
            eventProductOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalEventProductBean eventProductNewBean = new LitePalEventProductBean();
            eventProductNewBean.setSaveID(1);
            eventProductNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalEventProductBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(eventProductList);
            //保存的saveID为1
            LitePalEventProductBean eventProductBean = new LitePalEventProductBean();
            eventProductBean.setSaveID(1);
            eventProductBean.updateAll();
        }
    }

    /**
     * 获取活动规则并保存数据库
     */
    public void saveEventRuleData() {
        String eventRuleJson = ZipUtils.readJsonFile(ZipUtils.getTextFile("eventRule.txt"));
        List<LitePalEventRuleBean> eventRuleList = JSON.parseArray(eventRuleJson, LitePalEventRuleBean.class);
        //先查询是否存在
        List<LitePalEventRuleBean> selectEventRuleList = LitePal.findAll(LitePalEventRuleBean.class);
        if (selectEventRuleList != null && selectEventRuleList.size() > 0) {
            //存在就先全部存一次数据
            LitePal.saveAll(eventRuleList);

            //把原本saveID为1的改为2
            LitePalEventRuleBean eventRuleOldBean = new LitePalEventRuleBean();
            eventRuleOldBean.setSaveID(2);
            eventRuleOldBean.updateAll("saveID = ?", "1");

            //把新数据saveID为0的改为1
            LitePalEventRuleBean eventRuleNewBean = new LitePalEventRuleBean();
            eventRuleNewBean.setSaveID(1);
            eventRuleNewBean.updateAll("saveID = ?", "0");
            //删除掉旧数据
            LitePal.deleteAll(LitePalEventRuleBean.class, "saveID = ?", "2");
        } else {
            //不存在就保存
            LitePal.saveAll(eventRuleList);
            //保存的saveID为1
            LitePalEventRuleBean eventRuleBean = new LitePalEventRuleBean();
            eventRuleBean.setSaveID(1);
            eventRuleBean.updateAll();
        }
    }
}
