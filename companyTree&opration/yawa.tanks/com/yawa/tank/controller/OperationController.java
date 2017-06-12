package com.yawa.tank.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.render.RenderException;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.Dict;
import com.yawa.core.model.User;
import com.yawa.tank.model.CoreAttr;
import com.yawa.tank.model.Operation;
import com.yawa.tank.model.Site;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.ToolKit;
import com.yawa.util.excel.PoiMergeExporter;
import com.yawa.util.model.ResponseData;

/**
 * 运维记录
 * 
 * @author Administrator
 */
@Before(SessionInterceptor.class)
public class OperationController extends Controller {
	
	/**
	 * 模块：运维记录
	 * 功能：跳转至运维记录界面
	 */
	public void index() {
		User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
		setAttr("classify", user.getStr("classify"));
		setAttr("company", Company.me.getById(user.getInt("company")));
		render("index.jsp");
	}

	/**
	 * 模块：获取告警小类
	 * 功能：跳转至运维记录界面
	 */
	public void redit() {
		Integer faulttype = getParaToInt("faulttype");//获取
		if(faulttype==1 || faulttype==3){
			renderJson(Dict.me.getGroupItems("OPERATION.ALERTTYPE"));
		}else{
			renderJson(CoreAttr.me.find("select attr_id code,name from core_attrs where type = 'value' order by sort asc"));
		}	
	}
	/**
	 * 进入运维记录编辑页面
	 */
	public void edit() {
		Integer company = getParaToInt("company");//获取公司
		Integer id = getParaToInt("id");
		Operation operation = new Operation();
		String faulttype="";
		if(id != null){
			operation = Operation.me.findById(id);
			company = operation.getInt("company");
			faulttype = operation.getStr("faulttype");
		}
		setAttr("sites",Site.me.find("select t.* from sites t,base_companys c"
				+ " where t.company = c.id and find_in_set(?, c.path) and t.state = 1", company));
		//根据告警类型设置告警小类。1:离线，2:告警
		if("1".equals(faulttype)){
			setAttr("subtype", Dict.me.getGroupItems("OPERATION.ALERTTYPE"));//告警属性
		}else if("2".equals(faulttype)){
			setAttr("subtype", CoreAttr.me.find("select attr_id code,name from core_attrs where type = 'value' order by sort asc"));//告警属性
		}
		setAttr("devices", Dict.me.getGroupItems("OPERATION.DEVICE"));//更换设备
		setAttr("company", company);
		setAttr("operation", operation);
		render("edit.jsp");
	}

	/**
	 * 模块：运维记录
	 * 功能：展示运维记录分页列表
	 * 
	 */
	public void getPages() {
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		renderJson(Operation.me.getPages(getPara("sitename"),getPara("faulttype"), company,getPara("start_date"),getPara("end_date"),
				getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 模块：运维记录
	 * 功能：运维记录信息保存
	 */
	public void update() {
		try {
			User user = SecurityContextUtil.getLoginUser(getRequest());
			String[] replaces = getParaValues("operation.changedev");
			String[] faluls = getParaValues("operation.subtype");
			Operation operation = getModel(Operation.class,"operation");
			if (operation != null) {
				operation.set("changedev", ToolKit.join(replaces, ',')).set("subtype", ToolKit.join(faluls, ','));
				operation.set("state", 1).set("creator",user.getInt("id"));
				if(operation.getStr("faulttype").equals("1")){
					String namesString=Dict.me.findFirst("select group_concat(name) names from dict where group_code = 'OPERATION.ALERTTYPE' and find_in_set(code, ?)",operation.getStr("subtype")).getStr("names");
					operation.set("subtypename", namesString);
				}else{
					String namesString=CoreAttr.me.findFirst("select group_concat(name) names from core_attrs where type = 'value' and find_in_set(attr_id, ?)",operation.getStr("subtype")).getStr("names");
					operation.set("subtypename", namesString);
				}
				if (operation.get("id") == null) {
					operation.save();
					renderJson(new ResponseData(true, "运维记录新增成功!"));
				} else{
					operation.update();
					renderJson(new ResponseData(true, "运维记录修改成功!"));
				}
			}
		} catch (Exception e) {
			renderJson(new ResponseData(false, "保存发生异常，异常操作如下: " + e.getMessage()));
		}
	}

	/**
	 * 模块：运维记录
	 * 功能：删除运维记录功能
	 */
	public void delete() {
		Operation operation = Operation.me.findById(getParaToInt("id"));
		if (operation != null)
			try {
				operation.set("state", 0)
					.set("update_date", new Date())
					.update();
			} catch (Exception e) {
				renderJson(new ResponseData(false, "删除发生异常，异常操作如下：" + e.getMessage()));
				return;
			}
		renderJson(new ResponseData(true, "记录删除成功!"));
	}
	
	/**
	 * 模块：运维记录
	 * 功能：运维记录分站导出
	 */
	public void exportExcel() {
		String siteName = getPara("sitename");
		Integer company = getParaToInt("company");
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		String faulttype = getPara("faulttype");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		
		try {
			String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ "运维记录.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sheetSQL = Operation.me.getFilter(siteName, faulttype, company, start_date, end_date);
				String rowFilter = Operation.me.getFilter(null,faulttype,company,start_date,end_date);
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("operation", sheetSQL, rowFilter).write(os);
				
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
	 * 模块：运维记录
	 * 功能：运维记录汇总导出
	 */
	public void exportAllExcel() {
		String siteName = getPara("sitename");
		Integer company = getParaToInt("company");
		String start_date = getPara("start_date");
		String end_date = getPara("end_date");
		String faulttype = getPara("faulttype");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		
		try {
			String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ "运维记录.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sheetSQL = Operation.me.getFilter(siteName, faulttype, company, start_date, end_date);
				String rowFilter = Operation.me.getFilter(siteName,faulttype,company,null,null);
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("operation_sum", sheetSQL, rowFilter).write(os);
				
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
	
	
}


