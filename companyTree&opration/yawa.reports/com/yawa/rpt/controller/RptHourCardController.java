package com.yawa.rpt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.render.RenderException;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.User;
import com.yawa.rpt.model.RptHourCard;
import com.yawa.tank.model.Site;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.excel.PoiMergeExporter;

@Before(SessionInterceptor.class)
public class RptHourCardController extends Controller {
	/*
	 * 卡控表日报
	 */
	public void index(){
		User user = SecurityContextUtil.getLoginUser(getRequest());
		setAttr("classify", user.getStr("classify"));
		setAttr("company", Company.me.getById(user.getInt("company")));
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		List<Site> find = Site.me.find("select t.* from sites t where t.company ="+user.getInt("company")+" and t.state =1");
		setAttr("sites",find);
		render("index.jsp");
	}
	
	/**
	 * 分页记录
	 */
	public void getPages() {
		User user = SecurityContextUtil.getLoginUser(getRequest());
		Integer company = getParaToInt("company");
		if (company == null) {	
			company = user.getInt("company");
		}
		String start = getPara("start_date").replace("-", ""); 
		String end = getPara("end_date").replace("-", "")+"23";
		renderJson(RptHourCard.me.getPages(getParaToInt("showState"),
				getParaToInt("site"), start ,end, company,
				getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 卡控表日报导出
	 * showState 0每天，1每小时
	 */
	public void exportExcel() {
		String start_date = getPara("start_date").replace("-", "");
		String end_date = getPara("end_date").replace("-", "")+"23";
		Integer site = getParaToInt("site");
		Integer showState = getParaToInt("showState");
		Integer company = getParaToInt("company");
		if (company == null) {	
			User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
			company = user.getInt("company");
		}
		try {
			String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ "卡控表日报.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sheetSQL = " and company = " + company;
				if(site != null) sheetSQL += " and id = " + site;
				String rowFilter = RptHourCard.getFilters(null,start_date, end_date, company);
				os = getResponse().getOutputStream();
					if (showState==1) {
					PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("rpt_hour_card", sheetSQL, rowFilter).write(os);
					renderNull();
					}
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("rpt_day_card", sheetSQL, rowFilter).write(os);
				
			} catch (Exception e) {
				throw new RenderException(e);
			} finally {
				try {
					if (os != null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	
	/**
	 * 功能：根据选择公司获取站点 
	 */
	public void getSite(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		List<Site> site = Site.me.find("SELECT * FROM sites t WHERE t.company = "+company);
		renderJson(site);
	}

}
