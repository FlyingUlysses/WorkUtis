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
import com.yawa.rpt.model.RptTankHour;
import com.yawa.tank.model.Site;
import com.yawa.tank.model.Tank;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.excel.PoiMergeExporter;

@Before(SessionInterceptor.class)
public class RptHourController extends Controller{
	
	/**
	 * 运时报表
	 */
	public void rpt_hour_index() {
		
		User user = SecurityContextUtil.getLoginUser(getRequest());
		setAttr("classify", user.getStr("classify"));
		setAttr("company", Company.me.getById(user.getInt("company")));
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		setAttr("tanks",Tank.me.find("select t.* from tanks t,base_companys c"
											+ " where t.company = c.id and find_in_set(?, c.path) and t.state = 1", user.getInt("company")));
		render("rpt_hour_index.jsp");
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
		renderJson(RptTankHour.me.getPages(
				getParaToInt("tank"), getPara("start_date"), getPara("end_date"), company,
				getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 运时记录导出
	 */
	public void exportExcel() {
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		Integer tank = getParaToInt("tank");
		User user = SecurityContextUtil.getLoginUser(getRequest());
		Integer company = getParaToInt("company");
		if (company == null) {	
			company = user.getInt("company");
		}
		try {
			String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ "气化站列表数据.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sheetSQL = " and company = " + company;
				if(tank != null) sheetSQL += " and id = " + tank;
				String rowFilter = RptTankHour.getFilters(null,start_date, end_date, null);
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("gas_report_yunshi", sheetSQL, rowFilter).write(os);
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
	 *
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
