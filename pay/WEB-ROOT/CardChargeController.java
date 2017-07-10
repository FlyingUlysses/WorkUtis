package com.yawa.tank.controller;


import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.yawa.core.model.Dict;
import com.yawa.core.model.User;
import com.yawa.tank.model.LogRecharge;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.model.ResponseData;
/**
 * 
 *@Title:卡表充值
 *@Description:
 *@Author:WangYong
 *@Since:2017-7-6
 *@Version:1.1.0
 */
public class CardChargeController extends Controller {
    /**
     * 模块：卡表充值
     * 功能：首页展示
     */
    public void index() {
        User user = SecurityContextUtil.getLoginUser(getRequest());
        setAttr("siteList", Db.find("select id,name from sites where state = 1 and company =? order by id",user.getInt("company")));
        render("index.jsp");
    }
    

    /**
     * 模块：卡表充值
     * 功能：卡表详情分页列表
     */
    public void getPages() {
        User user = SecurityContextUtil.getLoginUser(getRequest());
        renderJson(LogRecharge.me.getPages(getPara("name"),getParaToInt("site_id"),user.getInt("conpamy"),getParaToInt("page"),getParaToInt("limit")));
    }
    
    
    
    /**
    /**
     * 模块：卡表充值
     * 功能：订单确认
     */
    public void cardChargePay() {
        setAttr("gas_count", getPara("gas_count"));
        User user = SecurityContextUtil.getLoginUser(getRequest());
        setAttr("person", LogRecharge.me.getReCardPay(getParaToInt("site_id"),user.getInt("conpamy"),getPara("name"),getPara("cardNum")));
        setAttr("rechargetype", Dict.me.getGroupItems("RECHARGE.TYPE"));
        render("cardCharge_pay.jsp");
    }
    
 
    
    /**
     * 模块：卡表充值
     * 功能：用户充值缴费
     */
    public void savePay() {
        try{
            String cardNum = getPara("cardnum");
            Integer dataId = getParaToInt("data_id");
            Integer siteId =  getParaToInt("site_id");
            Integer gasCount =  getParaToInt("gas_count");
            Integer deviceId =  getParaToInt("device_id");
            Integer csrId =  getParaToInt("csr_id");
            User user = SecurityContextUtil.getLoginUser(getRequest());
            LogRecharge.me.recharge(cardNum,siteId,deviceId,dataId,gasCount,user.getInt("id"),csrId);
            renderJson(new ResponseData(true, "客户充值成功，充值金额：" + gasCount));
        }catch(Exception e){
            renderJson(new ResponseData(false, "客户充值失败，异常: " + e.getMessage()));
        }
    }   
    
    
    
    /**
     * 模块：卡表充值
     * 功能：卡表详情分页列表
     */
    public void getLogPages() {
        renderJson(LogRecharge.me.getLogPages(getPara("name"),getParaToInt("site_id"),getParaToInt("page"),getParaToInt("limit")));
    }    
    
    
    
}
