package com.yawa.monitor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.Right;
import com.yawa.core.model.User;
import com.yawa.tank.model.Tank;
import com.yawa.tank.model.TankAttr;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.model.ResponseData;

/**
 * 储罐监控大屏界面
 * 
 * @author wrz
 */
@Before(SessionInterceptor.class)
public class MonitorController extends Controller {

	/**
	 * 监控大屏页面
	 */
	public void index() {
		try {
			int company = getParaToInt("id");
			setAttr("company", Company.me.findById(company));
		} catch (Exception e) {
			User user = SecurityContextUtil.getLoginUser(getRequest());
			setAttr("company", Company.me.findById(user.getInt("company")));
		}
		render("index_new.jsp");
	}

	/**
	 * 查看储罐详情
	 */
	public void tank() {
		int tankid = 0;
		User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
		Company company = Company.me.getById(user.getInt("company"));
		List<Tank> tanks = new ArrayList<Tank>();
		if(Company.me.isSystem(company))
			tanks = Tank.me.find("select t.* from tanks t where t.state = 1 order by t.id asc");
		else
			tanks = Tank.me.find("select t.* from tanks t left join base_companys t1 on t.company = t1.id "
					+ " where find_in_set(?, t1.path) and t.state = 1 and t1.state = 1 order by t.id asc", company.getInt("id"));
		try {
			tankid = getParaToInt("id");
		} catch (Exception e) {
			tankid = tanks.get(0).getInt("id").intValue();
		}
		setAttr("tankid", tankid);
		setAttr("tankList", tanks);
		render("/tanks/tank/tank.jsp");
	}
	
	/**
	 * 查看储罐详情,以新的页面展示
	 */
	public void tankV2() {
		int tankid = 0;
		User user = User.me.findById(SecurityContextUtil.getLoginUserId(getRequest()));
		Company company = Company.me.getById(user.getInt("company"));
		List<Tank> tanks = new ArrayList<Tank>();
		if(Company.me.isSystem(company))
			tanks = Tank.me.find("select t.* from tanks t where t.state = 1 order by t.id asc");
		else
			tanks = Tank.me.find("select t.* from tanks t left join base_companys t1 on t.company = t1.id "
					+ " where find_in_set(?, t1.path) and t.state = 1 and t1.state = 1 order by t.id asc", company.getInt("id"));
		try {
			tankid = getParaToInt("id");
		} catch (Exception e) {
			tankid = tanks.get(0).getInt("id").intValue();
		}
		setAttr("tankid", tankid);
		setAttr("tankList", tanks);
		render("/tanks/tank/tank_v2.jsp");
	}
	
	/**
	 * 阀门控制
	 */
	public void valve() {
		setAttr("tank", getParaToInt("id"));
		render("valve.jsp");
	}

	
	/**
	 * 获取新页面所需要的储罐详情
	 */
	public void showTankV2() {
		Integer tankid = getParaToInt("id");
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("right", false);
		try {
			Record tv = Db.findFirst("select * from data_tanks where find_in_set(?, tank_ids)", tankid);
			String disRate = "100%";
			try {
				String rate = tv.getStr("rate");
				if (rate != null) {
					rate = rate.replace("%", "");
					Float a = 100 - Float.parseFloat(rate);
					disRate = a.intValue() + "%";
				}
			} catch (Exception e) {}
			
			hm.put("disRate", disRate);
			hm.put("dnmc", tv);
			hm.put("llj", Db.findFirst("select * from data_lljs where find_in_set(?, tank_ids)", tankid));
			hm.put("sattrs", Tank.me.getNewSwitchs(tankid));
			List<TankAttr> attrs = TankAttr.me.getNewValAttrs(tankid);
			hm.put("vattrs", attrs);
			hm.put("ckb", Db.findFirst("select * from data_cards where find_in_set(?, tank_ids)", tankid));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		User user = SecurityContextUtil.getLoginUser(getRequest());
		if (user != null)
			hm.put("right", Right.me.isAuthorized(
					SecurityContextUtil.getLoginUserId(getRequest()), "valve"));
		hm.put("alert",Db.findFirst("select count(1) cnt,ifnull(sum(a.sound),0) sound from tank_alerts a,core_attrs b"
					+ " where a.attr_id = b.attr_id and a.state = 0 and b.field is not null and a.tank_id = ?",tankid));
		renderJson(hm);
	}
	
	
	/**
	 * 获取储罐详情
	 */
	public void showTank() {
		Integer tankid = getParaToInt("id");
		Hashtable<String, Object> htTable = new Hashtable<String, Object>();
		htTable.put("right", false);
		try {
			Record tv = Db.findFirst("select * from data_tanks where find_in_set(?, tank_ids)", tankid);
			String disRate = "100%";
			try {
				String rate = tv.getStr("rate");
				if (rate != null) {
					rate = rate.replace("%", "");
					Float a = 100 - Float.parseFloat(rate);
					disRate = a.intValue() + "%";
				}
			} catch (Exception e) {}
			htTable.put("disRate", disRate);
			htTable.put("dnmc", tv);
			htTable.put("llj", Db.findFirst("select * from data_lljs where find_in_set(?, tank_ids)", tankid));
			htTable.put("sattrs", Tank.me.getSwitchs(tankid));
			htTable.put("vattrs", TankAttr.me.getValAttrs(tankid));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		User user = SecurityContextUtil.getLoginUser(getRequest());
		if (user != null)
			htTable.put("right", Right.me.isAuthorized(
					SecurityContextUtil.getLoginUserId(getRequest()), "valve"));
		htTable.put("alert",Db.findFirst("select count(1) cnt,ifnull(sum(a.sound),0) sound from tank_alerts a,core_attrs b"
					+ " where a.attr_id = b.attr_id and a.state = 0 and b.field is not null and a.tank_id = ?",tankid));
		renderJson(htTable);
	}

	/**
	 * 打开关闭提示声音
	 */
	public void toggleNoice() {
		Integer tank = getParaToInt("id");
		String desc = "";
		try {
			Integer swch = 0;
			Record record = Db
					.findFirst(
							"select count(1) cnt,ifnull(sum(a.sound),0) sound from tank_alerts a,core_attrs b"
									+ " where a.attr_id = b.attr_id and a.state = 0 and b.field is not null and a.tank_id = ?",
							tank);
			BigDecimal sound = record.getBigDecimal("sound");
			Long cnt = record.getLong("cnt");
			if (sound.intValue() == 0 && cnt.intValue() > 0)
				swch = 1;
			Db.update("update tank_alerts set sound = ?"
					+ " where tank_id = ? and state = 0", swch, tank);
			desc = swch == 0 ? "储罐消音操作" : "消音恢复操作";
		} catch (Exception e) {
			renderJson(new ResponseData(false, desc + "失败!"));
			return;
		}
		renderJson(new ResponseData(true, desc + "成功!"));
	}

	/**
	 * 获取储罐控制面板
	 */
	public void getTankDeviceValves() {
//		renderJson(SiteDeviceValve.me.getValves(getParaToInt("id")));
	}

	/**
	 * 操作阀门
	 */
	public void valveAct() {
//		try {
//			SiteDeviceValve.me.valveAct(getParaToInt("tank"), getPara("attr"),
//					getPara("code"));
//		} catch (Exception e) {
//			renderJson(new ResponseData(false, e.getMessage()));
//			return;
//		}
//		renderJson(new ResponseData(true, "阀门操作成功！"));
	}
}
