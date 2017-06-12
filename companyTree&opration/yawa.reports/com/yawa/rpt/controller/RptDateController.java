package com.yawa.rpt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.User;
import com.yawa.rpt.model.RptTankDate;
import com.yawa.tank.model.Site;
import com.yawa.tank.model.Tank;
import com.yawa.util.DateTools;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.excel.PoiMergeExporter;

/**
 * 儲罐運行日報表
 * 
 * @author wrz
 */
@Before(SessionInterceptor.class)
public class RptDateController extends Controller {
	
	/**
	 * 气化站运行日报表
	 */
	public void index() {
		User user = SecurityContextUtil.getLoginUser(getRequest());
		setAttr("classify", user.getStr("classify"));
		setAttr("company", Company.me.getById(user.getInt("company")));
		setAttr("tanks",Tank.me.find("select t.* from tanks t,base_companys c"
				+ " where t.company = c.id and find_in_set(?, c.path) and t.state = 1", user.getInt("company")));
		render("rpt_date_index.jsp");
	}
	
	/**
	 * 领导视图查看用气记录
	 */
	public void leaderIndex() {
		setAttr("year", DateTools.formatDate(new Date(), "yyyy"));
		setAttr("month", DateTools.formatDate(new Date(), "yyyy-MM"));
		User user = SecurityContextUtil.getLoginUser(getRequest());
		setAttr("company", Company.me.getById(user.getInt("company")));
		render("tanks/leaderIndex.jsp");
	}
	
	/**
	 * 运行日表表图表
	 */
	public void chart() {
		User user = SecurityContextUtil.getLoginUser(getRequest());
		setAttr("month", DateFormatUtils.format(new Date(), "yyyy-MM"));
		setAttr("tanks",Tank.me.find("select t.* from tanks t,base_companys c"
			+ " where t.company = c.id and find_in_set(?, c.path) and t.state = 1", user.getInt("company")));
		render("/tanks/dateRpt/chart.jsp");
	}
	
	/**
	 * 运行日表图表JSON
	 */
	public void getDateChart() {
		String month = getPara("month");
		Integer tank = getParaToInt("tank");
		Integer company = getParaToInt("company");
		if(null==company){
			company= SecurityContextUtil.getLoginUser(getRequest()).getInt("company");
		}
		renderJson(RptTankDate.me.getDateChart(month, tank, company));
	}
	
	
	/**
	 * 模块：首页
	 * 功能：获取月用气柱状图
	 */
	public void getMonthUsed(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		renderJson(RptTankDate.me.getMonthUsed(getPara("year"), company));
	}
	
	/**
	 * 模块：首页
	 * 功能：获取公司用气柱状图
	 */
	public void getCompanyUsed(){
		Integer company = getParaToInt("company");
		renderJson(RptTankDate.me.getCompanyUsed(getPara("stat"), company));
	}
	
	/**
	 * 模块：首页
	 * 功能：获取日用气柱状图
	 */
	public void getDailyUsed(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		renderJson(RptTankDate.me.getDailyUsed(getPara("stat"), company));
	}
	
	/**
	 * 模块：首页
	 * 功能：获取每日各个地点用气柱状图
	 */
	public void getSitesUsed(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		renderJson(RptTankDate.me.getSitesUsed(getPara("stat"), company));
	}
	
	
	
	/**
	 * 气化站充装记录
	 */
	public void rpt_dayfilling_index() {
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		render("rpt_tank_dayfilling.jsp");
	}
	
	/**
	 * 查询日报表记录
	 */
	public void getPages() {
		Integer company = getParaToInt("company");
		User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
		if (company == null) {	
			company = user.getInt("company");
		}
		renderJson(RptTankDate.me.getPages(getParaToInt("tank"),
				getPara("start_date"), getPara("end_date"),company,
				getParaToInt("page"), getParaToInt("limit")));
	}

	/**
	 * 导出报表的处理
	 * @throws IOException 
	 */
	public void exportExcel() throws IOException {
		OutputStream os = null;
		try{
			Integer company = getParaToInt("company");
			if (company == null) {	
				User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
				company = user.getInt("company");
			}
			String fileName = java.net.URLEncoder.encode("气化站运营日报.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition",
					"attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			String tankSQL = RptTankDate.getFilters(getParaToInt("tank"), null, null, company);
			String dateSQL = RptTankDate.getFilters(null, getPara("start_date"), getPara("end_date"),null);
			os = getResponse().getOutputStream();
			PoiMergeExporter.getInstance().version("2003").cellWidth(2200)
					.exportExcel("rpt_date", tankSQL, dateSQL).write(os);
			
		}catch(Exception e){
			System.out.println("错误原因：" + e.getMessage());
		}finally{
			if(os != null){
				os.flush();
				os.close();
			}
		}
		renderNull();
	}
	
	/**
	 * 导出汇总数据
	 * @throws IOException 
	 */
	public void exportAll() throws IOException {
		OutputStream os = null;
		try{
			Integer company = getParaToInt("company");
			if (company == null) {	
				User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
				company = user.getInt("company");
			}
			String fileName = java.net.URLEncoder.encode("气化站运营日报汇总.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition",
					"attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			String tankSQL = RptTankDate.getFilters(getParaToInt("tank"), null, null,company);
			String dateSQL = RptTankDate.getFilters(null, getPara("start_date"), getPara("end_date"),company);
			os = getResponse().getOutputStream();
			PoiMergeExporter.getInstance().version("2003").cellWidth(2200)
					.exportExcel("rpt_date_sum", dateSQL, tankSQL).write(os);
			
		}catch(Exception e){
			System.out.println("错误原因：" + e.getMessage());
		}finally{
			if(os != null){
				os.flush();
				os.close();
			}
		}
		renderNull();
	}
	
	/**
	 * 导出充装记录
	 * @throws IOException
	 */
	public void exportFillExcel() throws IOException {
		OutputStream os = null;
		try{
			String fileName = java.net.URLEncoder.encode("气化站充装记录报表.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition",
					"attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			String filter = ""; 
			if(StringUtils.isNotEmpty(getPara("start_date")))
				filter += " and cycle >= '" + getPara("start_date") + "'";
			if(StringUtils.isNotEmpty(getPara("end_date")))
				filter += " and cycle <= '" + getPara("end_date") + "'";
			os = getResponse().getOutputStream();
			PoiMergeExporter.getInstance().version("2003").cellWidth(2200)
					.exportExcel("rpt_date_fill", filter).write(os);
		}catch(Exception e){
			System.out.println("错误原因：" + e.getMessage());
		}finally{
			if(os != null){
				os.flush();
				os.close();
			}
		}
		renderNull();
	}
	/**
	 * 报表导出说明页面
	 */
	public void explainDetail() {
		render("rpt_explainDetail.jsp");
	}
	
	
	/**
	 *
	 * 根据选择公司获取站点 
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
