package com.yawa.rpt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.render.RenderException;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.Dict;
import com.yawa.core.model.User;
import com.yawa.rpt.model.ViewCarLoadUnLoad;
import com.yawa.rpt.model.ViewCarLoadUnLoadDay;
import com.yawa.tank.model.Distribution;
import com.yawa.tank.model.Filling;
import com.yawa.tank.model.Site;
import com.yawa.tank.model.Tank;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.excel.PoiMergeExporter;

/**
 * 气化站卸液报表
 * 
 * @author Administrator
 */
@Before(SessionInterceptor.class)
public class CarUnloadController extends Controller {

	/**
	 * 配送卸液月报汇总
	 */
	public void rpt_carUnLoad_day() {
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		setAttr("CARUNLOAD",Dict.me.getGroupItems("CAR.UNLOAD"));
		render("rpt_carUnLoad_day.jsp");
	}
	/**
	 * 气化站卸液日报
	 */
	public void rpt_carUnLoad_detail() {
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		setAttr("CARUNLOAD",Dict.me.getGroupItems("CAR.UNLOAD"));
		render("rpt_carUnLoad_detail.jsp");
	}
	
	/**
	 * 配送车装卸液情况
	 */
	public void rpt_carLoadUnLoad() {
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		setAttr("CARUNLOAD",Dict.me.getGroupItems("CAR.UNLOAD"));
		render("rpt_carloadUnLoad.jsp");
	}
	/**
	 * 配送装卸液月报
	 */
	public void rpt_carLoadUnLoad_day() {
		User user = SecurityContextUtil.getLoginUser(getRequest());
		setAttr("classify", user.getStr("classify"));
		Company company = Company.me.getById(user.getInt("company"));
		setAttr("company", company);
		setAttr("yearmmdd", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		setAttr("CARDRIVER",Dict.me.getGroupItems("CAR.DRIVER"));
		setAttr("tanks",Tank.me.find("select t.* from tanks t,base_companys c"
				+ " where t.company = c.id and find_in_set(?, c.path) and t.state = 1", company.getInt("id")));
		render("rpt_carloadUnLoad_day.jsp");
	}
	
	/**
	 * 气化站卸液月报表
	 */
	public void getLogPages() {
		Hashtable<String, Object> htTable = new Hashtable<String, Object>();
		if(StringUtils.isNotEmpty(getPara("carid")))
			htTable.put("carid", getPara("carid"));
		if(StringUtils.isNotEmpty(getPara("start_date")))
			htTable.put("start_date", getPara("start_date"));
		if(StringUtils.isNotEmpty(getPara("end_date")))
			htTable.put("end_date", getPara("end_date"));
		if(StringUtils.isNotEmpty(getPara("tankid")))
			htTable.put("tankid", getPara("tankid"));
		User user = SecurityContextUtil.getLoginUser(getRequest());
		if(StringUtils.isNotEmpty(getPara("company"))){
			htTable.put("company", getPara("company"));
		}else{
		htTable.put("company", user.getInt("company"));
		}
		renderJson(Distribution.me.getDayPages(htTable,getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 气化站卸液日报表
	 */
	public void getLogDetailPages() {
		Map<String,String[]> paraMap = new LinkedHashMap<String, String[]>();
		if(StringUtils.isNotEmpty(getPara("carid")))
			paraMap.put("carid", new String[]{ getPara("carid")});
		if(StringUtils.isNotEmpty(getPara("start_date")))
			paraMap.put("start_date", new String[]{ getPara("start_date")} );
		if(StringUtils.isNotEmpty(getPara("end_date")))
			paraMap.put("end_date", new String[]{ getPara("end_date")} );
		User user = SecurityContextUtil.getLoginUser(getRequest());
		renderJson(Filling.me.getPages(paraMap,user.getInt("company"),getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 查询配送车装卸液情况
	 */
	public void getCarLoadUnLoadPages() {
		renderJson(ViewCarLoadUnLoad.me.getPages(
				getPara("carid"), getPara("start_date"), getPara("end_date"),
				getParaToInt("page"), getParaToInt("limit")));
	}
	/**
	 * 查询配送装卸液月报,明细到天
	 */
	public void getCarLoadUnLoadDayPages() {
		renderJson(ViewCarLoadUnLoadDay.me.getPages(
				getPara("carid"), getPara("start_date"), getPara("end_date"),
				getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 装卸液报表
	 */
	public void exportUnloadExcel() {
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		try {
			String fileName = java.net.URLEncoder.encode("配送装卸液报表.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sqlCdtString = filterCondition(start_date, end_date);
				String carid = getPara("carid");
				if(StringUtils.isNotEmpty(carid)){
					sqlCdtString += " and carid = '" + carid + "'";
				}
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2500).exportExcel("rpt_carLoadUnload_detail", sqlCdtString,sqlCdtString).write(os);
			} catch (Exception e) {
				throw new RenderException(e);
			} finally {
				try {
					if (os != null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	/**
	 * 卸液月报表
	 */
	public void exportUnloadDateExcel() {
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		try {
			String fileName = java.net.URLEncoder.encode("配送装卸液月报.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sqlCdtString = filterCondition(start_date, end_date);
				String carid = getPara("carid");
				if(StringUtils.isNotEmpty(carid)){
					sqlCdtString += " and driver = '" + carid + "'";
				}
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2500).exportExcel("rpt_carLoadUnload", sqlCdtString,sqlCdtString).write(os);
			} catch (Exception e) {
				throw new RenderException(e);
			} finally {
				try {
					if (os != null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	/**
	 * 汇总卸液月报表
	 */
	public void exportDayExcel() {
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		try {
			String fileName = java.net.URLEncoder.encode("配送卸液月报汇总.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sqlCdtString = filterCondition(start_date, end_date);
				String carid = getPara("carid");
				if(StringUtils.isNotEmpty(carid)){
					sqlCdtString += " and carid = '" + carid + "'";
				}
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2500).exportExcel("rpt_carUnload", sqlCdtString,sqlCdtString).write(os);
			} catch (Exception e) {
				throw new RenderException(e);
			} finally {
				try {
					if (os != null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	/**
	 * 气化站卸载日报表
	 */
	public void exportDetailExcel() {
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		String carid = getPara("carid");
		try {
			String fileName = java.net.URLEncoder.encode(
					DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")
							+ "气化站列表数据.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition",
					"attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sqlCdtString = filterCondition(start_date, end_date);
				carid = getPara("carid");
				if(StringUtils.isNotEmpty(carid)){
					sqlCdtString += " and carid = '" + carid + "'";
				}
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200)
						.exportExcel("rpt_carUnload_detail", sqlCdtString,sqlCdtString).write(os);
			} catch (Exception e) {
				throw new RenderException(e);
			} finally {
				try {
					if (os != null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}

	private String filterCondition(String start_date, String end_date) {
		String sql = "";
		if (StringUtils.isNotEmpty(start_date)) {
			sql += " and date_format(cycle, '%Y-%m-%d') >='" + start_date + "'";
		}
		if (StringUtils.isNotEmpty(end_date)) {
			sql += " and date_format( cycle, '%Y-%m-%d') <='" + end_date + "'";
		}
		return sql;
	}
	
	/**
	 * 导出卸液报表,按站导出
	 * @throws IOException 
	 */
	public void exportExcelByTank() throws IOException {
		OutputStream os = null;
		try{
			String start_date = getPara("start_date");
			String end_date = getPara("end_date");
			Integer tankid = getParaToInt("tankid");
			Integer company = getParaToInt("company");
			if (company == null) {	
				User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
				company = user.getInt("company");
			}
			String fileName = java.net.URLEncoder.encode("配送装卸报表分站导出.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition",
					"attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			String tankSQL = filterConByTankSheet(company,tankid);
			String dataSql = filterConByTankData(start_date,end_date);
			os = getResponse().getOutputStream();
			PoiMergeExporter.getInstance().version("2003").cellWidth(2200)
					.exportExcel("rpt_tankUnload", tankSQL, dataSql).write(os);
			
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
	 * 出卸液报表,按站导出 过滤条件设置
	 * @param company
	 * @param tankid
	 * @return
	 */
	private String filterConByTankSheet(Integer company,Integer tankid) {
		String sql = "";
		if(null!=company){
			sql += " and company="+company;
		}
		if(null!=tankid){
			sql += " and id="+tankid;
		}
		return sql;
	}
	/**
	 * 出卸液报表,按站导出 过滤条件设置
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	private String filterConByTankData(String start_date, String end_date) {
		String sql = "";
		if (StringUtils.isNotEmpty(start_date)) {
			sql += " and b.cycle >='" + start_date + "'";
		}
		if (StringUtils.isNotEmpty(end_date)) {
			sql += " and b.cycle <='" + end_date + "'";
		}
		return sql;
	}
	
	
	/**
	 *出卸液报表,按站导出 过滤条件设置
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
