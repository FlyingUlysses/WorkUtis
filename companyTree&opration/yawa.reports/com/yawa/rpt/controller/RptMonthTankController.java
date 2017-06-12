package com.yawa.rpt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.render.RenderException;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.Dict;
import com.yawa.core.model.User;
import com.yawa.rpt.model.RptMonthTank;
import com.yawa.tank.model.Site;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.excel.PoiMergeExporter;


@Before(SessionInterceptor.class)
public class RptMonthTankController extends Controller {
	
	
	/**
	 * 月报模块
	 * 跳转首页
	 */
	public void monthIndex() {
		User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
		setAttr("classify", user.getStr("classify"));
		setAttr("company", Company.me.getById(user.getInt("company")));
		render("monthIndex.jsp");
	}
	
	
	/**
	 *月报模块、 同比环比月报模块共用
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
	
	
	/**
	 * 月报模块
	 * 分页展示月报数据
	 */
	public void getPages() {
		Integer company=getParaToInt("company");
		if (company==null) {
			User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
			company =user.getInt("company");
		}
		String start = getPara("start_date");
		System.out.println(start);
			if (StringUtils.isNotEmpty(start)) {
				start=start.replace("-", ""); 
			}
		String end = getPara("end_date");
			if (StringUtils.isNotEmpty(end)) {
				end=end.replace("-", "");
			}	
		renderJson(RptMonthTank.me.getPages(company,getParaToInt("site"),
				start,end,getParaToInt("page"),getParaToInt("limit")));
	}
	
	/**
	 * 月报模块
	 * 月报导出
	 */
	public void exportExcel() {
		String start_date = getPara("start_date").replace("-", "").substring(0,6);
		String end_date = getPara("end_date").replace("-", "").substring(0,6);
		Integer site = getParaToInt("site");
		Integer company=getParaToInt("company");
		try {
			String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ "月报.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sheetSQL = " and ( t.company= "+ company+" OR t.company IN(SELECT id FROM base_companys a WHERE a.parent="+ company + ") ) ";
				if(site != null) sheetSQL += " and id = " + site;
				
				String rowFilter = RptMonthTank.me.getFilters(null,start_date, end_date,company);
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("rpt_month_tank", sheetSQL, rowFilter).write(os);
				
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
	 * 同比环比月报模块
	 * 跳转同比环比页面
	 */
	public void monthChartIndex() {
		Integer companyId = getParaToInt("company");
		if(companyId == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			companyId = user.	getInt("company");
		}
		Company company = Company.me.getById(companyId);
		List<Site> siteList = Site.me.find("select * from sites where company =" +companyId);
		List<Dict> factor = Dict.me .find("SELECT * FROM dict t where t.GROUP_CODE='TBHBMONTH.FACTOR' and t.state =1 and t.TYPE='I'");
		setAttr("company",company );
		setAttr("month", DateFormatUtils.format(new Date(), "yyyy"));
		setAttr("factor",factor );
		setAttr("siteList",siteList );
		render("monthChartIndex.jsp");
	}
	
	/**
	 * 同比环比月报模块
	  * 功能：获取环比柱状图
	 */
	public void getHBChart(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		
		String start_date = getPara("start_date").replace("-", "");
		if (start_date==null || start_date.isEmpty()) {
			start_date =DateFormatUtils.format(new Date(), "yyyy");
		}
	
		renderJson(RptMonthTank.me.HBChart(start_date,getPara("factor"),getParaToInt("site"),company));
	}
	
	/**
	 * 同比环比月报模块
	  * 功能：获取同比柱状图
	 */
	public void getTBChart(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		
		 String start_date = getPara("start_date").replace("-", "");
		if (start_date==null || start_date.isEmpty()) {
			start_date =DateFormatUtils.format(new Date(), "yyyy");
		}
			
			
		renderJson(RptMonthTank.me.TBChart(start_date,getPara("factor"),getParaToInt("site"),company));
	}

}
