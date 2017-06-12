package com.yawa.rpt.controller;


import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.User;
import com.yawa.rpt.model.RptDailyDevice;
import com.yawa.util.SecurityContextUtil;


/**
 * 设备运行日报表
 * @author Administrator
 *
 */
@Before(SessionInterceptor.class)
public class DailyController extends Controller {
	/**
	 * 设备运行日报表模块
	 * 跳转首页
	 */
	public void index() {
		User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
		setAttr("classify", user.getStr("classify"));
		setAttr("company", Company.me.getById(user.getInt("company")));
		render("index.jsp");
	}
	
	
	
	
	/**
	 * 设备运行日报表模块
	 * 分页展示设备运行日报数据
	 */
	public void getPages() {
		Integer company = getParaToInt("company");
		if (company == null) {	
			User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
			company = user.getInt("company");
		}
		String start = getPara("start_date");
		renderJson(RptDailyDevice.me.getPages(company,getPara("site"),
				start,getParaToInt("page"),getParaToInt("limit")));
	}
	
	
}
