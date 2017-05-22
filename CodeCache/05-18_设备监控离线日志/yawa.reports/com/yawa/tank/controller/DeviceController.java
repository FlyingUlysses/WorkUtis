package com.yawa.tank.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.Dict;
import com.yawa.core.model.User;
import com.yawa.http.SocketClient;
import com.yawa.tank.model.LogDevice;
import com.yawa.tank.model.LogNotice;
import com.yawa.tank.model.Site;
import com.yawa.tank.model.SiteCollect;
import com.yawa.tank.model.SiteDevice;
import com.yawa.tank.model.SiteDeviceAttr;
import com.yawa.tank.model.SiteDeviceLight;
import com.yawa.tank.model.SiteDeviceValve;
import com.yawa.tank.model.Tank;
import com.yawa.tank.model.TankAttr;
import com.yawa.tank.model.TankModel;
import com.yawa.tank.util.RDCKit;
import com.yawa.util.DateTools;
import com.yawa.util.MessageUtils;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.ToolKit;
import com.yawa.util.model.ResponseData;

/**
 * 储罐设备监控
 * 仅运维人员使用
 * @author Administrator
 *
 */
@Before(SessionInterceptor.class)
public class DeviceController extends Controller {
	
	/**
	 * 首页
	 * @param request
	 * @return
	 */
	public void index() {
		User user = SecurityContextUtil.getLoginUser(getRequest());
		if(user != null)
			setAttr("company", Company.me.getById(user.getInt("company")));
		render("index.jsp");
	}
	
	/**
	 * 模块：设备监控
	 * 功能：获取公司的监控设备信息
	 * 范围：管理人员专用
	 */
	public void getDevices() {
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		renderJson(SiteDevice.me.getDevices(company));
	}
	

	/**
	 * 模块：站点信息
	 * 功能：跳转远传设备编辑页面
	 * 范围：管理人员专用
	 */
	public void site() {
		Integer id = getParaToInt("id");
		Integer company = getParaToInt("company");
		Site site = new Site();
		if(id != null)
			site = Site.me.getSite(id);
		else if(company != null)
			site.set("company", company);
		setAttr("site", site);
		render("edit_site.jsp");
	}
	
	/**
	 * 模块：储罐编辑
	 * 功能：跳转储罐编辑页面
	 * 范围：管理人员专用
	 */
	public void tank() {
		Integer id = getParaToInt("id");
		Integer company = getParaToInt("company");
		setAttr("models", TankModel.me.getList());
		setAttr("classify", Dict.me.getGroupItems("TANK.CLASSIFY"));
		Tank tank = new Tank();
		if(id != null){
			tank = Tank.me.getById(id);
			company = tank.getInt("company");
		}else if(company != null)
			tank.set("company", company);
		setAttr("sites", Site.me.find("select id, name from sites where company = ? and state = 1", company));
		setAttr("tank", tank);
		render("edit_tank.jsp");
	}
	
	/**
	 * 模块：远传设备
	 * 功能：跳转远传设备编辑页面
	 * 范围：管理人员专用
	 */
	public void collect() {
		Integer id = getParaToInt("id");
		Integer company = getParaToInt("company");
		SiteCollect collect = new SiteCollect();
		if(id != null)
			collect = SiteCollect.me.findFirst("select t.*,s.name site from site_collect t"
					+ " left join sites s on t.siteId = s.id where t.id = ?", getParaToInt("id"));
		if(company != null)
			setAttr("sites", Db.find("select id,name from sites where state = 1 and company = ?", company));
		setAttr("devices", Db.find("select t.model from device_collect t where t.state = 1"));
		setAttr("collect", collect);
		render("edit_collect.jsp");
	}

	/**
	 * 模块：采集设备编辑
	 * 功能：跳转采集设备编辑页面
	 * 范围：管理人员专用
	 */
	public void device() {
		Integer id = getParaToInt("id");
		Integer company = getParaToInt("company");
		SiteDevice sdevice = new SiteDevice();
		if(id != null){
			sdevice = SiteDevice.me.findFirst("select sd.*,s.name site,sc.dtu_code,sc.model"
				+ " from site_device sd left join sites s on sd.site_id = s.id"
				+ " left join site_collect sc on sd.collectId = sc.id where sd.id = ?", id);
		}
		if(company != null)
			setAttr("sites", Db.find("select id,name from sites where state = 1 and company = ?", company));
		setAttr("devices", Db.find("select id, name from devices"));
		setAttr("device", sdevice);
		render("edit_device.jsp");
	}
	
	/**
	 * 模块：设备属性
	 * 功能：跳转设备属性页面
	 * 范围：管理人员专用
	 */
	public void attr() {
		setAttr("id", getParaToInt("id"));
		setAttr("site", getPara("site"));
		setAttr("device", getPara("device"));
		setAttr("intf", getPara("intf"));
		render("device_attr.jsp");
	}
	
	/**
	 * 模块：设备属性
	 * 功能：获取设备属性信息
	 * 范围：管理人员专用
	 */
	public void getDeviceAttrs() {
		Integer device = getParaToInt("device");
		Map<String, List<Record>> attrMap = new HashMap<String, List<Record>>();
		attrMap.put("attrs",  SiteDeviceAttr.me.getDeviceAttrs(device, ""));
		attrMap.put("lights", SiteDeviceLight.me.getDeviceLights(device));
		attrMap.put("valves", SiteDeviceValve.me.getDeviceValves(device));
		renderJson(attrMap);
	}
	
	/**
	 * 模块：设备属性
	 * 功能：跳转设备属性选择页面
	 * 范围：管理人员专用
	 */
	public void filter() {
		String type = getPara("type");
		Integer device = getParaToInt("device");
		setAttr("type", type);
		setAttr("device", device);
		if(type.equals("attr")){
			setAttr("attrs", Db.find("select a.attr_id,a.name,(case when b.id is not null and b.state = 1 then 'checked' end) checked"
				+ " from core_attrs a left join site_device_attr b on a.attr_id = b.attr_id and b.deviceId = ?"
				+ " where a.`type` = 'value'", device));
		}else if(type.equals("light")){
			setAttr("attrs", Db.find("select a.attr_id,a.name,(case when b.id is not null and b.state = 1 then 'checked' end) checked"
				+ " from core_attrs a left join site_device_light b on a.attr_id = b.attr_id and b.deviceId = ?"
				+ " where a.`type` = 'switch'", device));
		}else if(type.equals("valve")){
			setAttr("attrs", Db.find("select a.attr_id,a.name,(case when b.id is not null and b.state = 1 then 'checked' end) checked"
				+ " from core_attrs a left join site_device_valve b on a.attr_id = b.attr_id and b.deviceId = ?"
				+ " where a.`type` = 'switch'", device));
		}
		render("filter_attr.jsp");
	}
	
	/**
	 * 模块：设备属性
	 * 功能：跳转设备属性编辑页面
	 * 范围：管理人员专用
	 */
	public void attrEdit() {
		Integer id = getParaToInt("id");
		String type = getPara("type");
		Record attr = null;
		List<Record> lines = new ArrayList<Record>();
		if(type.equals("attr")){
			attr = Db.findFirst("select a.*,b.model from site_device_attr a left join device_line b"
				+ " on a.port = b.code where a.id = ?", id);
			lines = Db.find("select c.model, (select name from dict where group_code = 'LINE.MODEL'"
				+ " and code = c.model and type = 'I') name"
				+ " from site_device_attr a, site_device b, device_line c"
				+ " where a.deviceId = b.id and b.linked = c.interface and a.id = ? group by c.model", id);
			setAttr("lines", lines);
			setAttr("attr",  attr);
			render("edit_attr.jsp");
		}else if(type.equals("light")){
			attr = Db.findFirst("select a.*,b.model,(select group_concat(value,' - ',color) from core_lights where attr_id = a.attr_id) light"
				+ " from site_device_light a left join device_line b"
				+ " on a.port = b.code where a.id = ?", id);
			lines = Db.find("select c.model, (select name from dict where group_code = 'LINE.MODEL'"
				+ " and code = c.model and type = 'I') name"
				+ " from site_device_light a, site_device b, device_line c"
				+ " where a.deviceId = b.id and b.linked = c.interface and a.id = ? group by c.model", id);
			setAttr("lines", lines);
			setAttr("attr",  attr);
			render("edit_light.jsp");
		}else if(type.equals("valve")){
			attr = Db.findFirst("select a.*,b.model,(select group_concat(code,' - ',action) from site_valve_action where valveId = a.id) valve"
				+ " from site_device_valve a left join device_line b"
				+ " on a.port = b.code where a.id = ?", id);
			lines = Db.find("select c.model,(select name from dict where group_code = 'LINE.MODEL'"
				+ " and code = c.model and type = 'I') name"
				+ " from site_device_valve a, site_device b, device_line c"
				+ " where a.deviceId = b.id and b.linked = c.interface and a.id = ? group by c.model", id);
			setAttr("lines", lines);
			setAttr("attr",  attr);
			render("edit_valve.jsp");
		}
	}
	
	/**
	 * 模块：设备属性
	 * 功能：获取线路列表
	 * 范围：管理人员专用
	 */
	public void getLines() {
		Integer id = getParaToInt("id");
		String model = getPara("model");
		String type = getPara("type");
		if(type.equals("attr"))
			renderJson(Db.find("select c.code from site_device_attr a,site_device b,device_line c"
				+ " where a.deviceId = b.id and b.linked = c.interface and a.id = ? and c.model = ?", id,model));
		else if(type.equals("light"))
			renderJson(Db.find("select c.code from site_device_light a,site_device b,device_line c"
					+ " where a.deviceId = b.id and b.linked = c.interface and a.id = ? and c.model = ?", id,model));
		else if(type.equals("valve"))
			renderJson(Db.find("select c.code from site_device_valve a,site_device b,device_line c"
					+ " where a.deviceId = b.id and b.linked = c.interface and a.id = ? and c.model = ?", id,model));
	}
	
	/**
	 * 模块：设备监控
	 * 功能：跳转设备监控页面
	 * 范围：管理人员专用
	 */
	public void eye() {
		setAttr("id", getParaToInt("id"));
		setAttr("site", getPara("site"));
		setAttr("device", getPara("device"));
		render("device_eye.jsp");
	}
	
	/**
	 * 模块：设备监控
	 * 功能：获取设备数据
	 * 范围：管理人员专用
	 */
	public void getDeviceData() {
		Integer device = getParaToInt("device");
		Map<String, List<Record>> attrMap = new HashMap<String, List<Record>>();
		attrMap.put("attrs",  TankAttr.me.getDeviceAttrs(device));
		attrMap.put("lights", TankAttr.me.getDeviceLights(device));
		attrMap.put("valves", TankAttr.me.getDeviceValves(device));
		renderJson(attrMap);
	}
	
	/**
	 * 模块：设备监控
	 * 功能：获取设备范围数据
	 * 范围：管理人员专用
	 */
	public void getDeviceLimits() {
		try{
			Integer device = getParaToInt("device");
			Map<String, String> tokenMap = RDCKit.readLimits(device);
			ResponseData retn = RDCKit.sendCmd(tokenMap.get("ipaddr"), tokenMap.get("reg"), tokenMap.get("token"));
			if(retn.isSuccess()){
				@SuppressWarnings("unchecked")
				Map<String, String> dataMap = (Map<String, String>) retn.getData();
				SiteDeviceAttr.me.writeLimits(device, dataMap.get("token"));
			}
			renderJson(retn);
		}catch(Exception e){
			renderJson(new ResponseData(false,"参数获取失败：" + e.getMessage() + "!"));
		}
	}
	
	/**
	 * 模块：设置参数
	 * 功能：参数范围设置下穿
	 * 范围：管理人员专用
	 */
	public void limit() {
		Integer id = getParaToInt("id");
		setAttr("id",     id);
		setAttr("device", getPara("device"));
		Record model = Db.findFirst("select t.*,t3.ipaddr,t1.dtu_code,t2.name,t2.addr intf_addr from site_device t "
				+ " left join site_collect t1 on t.collectId = t1.id"
				+ " left join device_interface t2 on t.linked = t2.code"
				+ " left join tanks t3 on find_in_set(t3.id, t.tank_ids)"
				+ " where t.id = ?", id);
		setAttr("model", model);
		setAttr("attrs", SiteDeviceAttr.me.getDeviceAttrs(id, " and port is not null"));
		render("limit.jsp");
	}
	
	/**
	 * 模块：属性设置
	 * 功能：获取属性的限制范围
	 * 范围：管理人员专用
	 */
	public void getAttrLimit() {
		Integer device = getParaToInt("device");
		String attr = getPara("attr");
		renderJson(Db.findFirst("select * from site_device_attr"
			+ " where deviceId = ? and attr_id = ?", device, attr));
	}
	
	/**
	 * 模块：范围设置
	 * 功能：设置属性范围指令
	 * 范围：管理人员专用
	 */
	public void getLimitToken() {
		Integer device = getParaToInt("device");
		String attr = getPara("attr");
		String category = getPara("category");
		String value = getPara("value");
		try {
			String token = RDCKit.getLimitToken(device, attr, category, true, value);
			renderJson(new ResponseData(true, token));
		} catch (Exception e) {
			renderJson(new ResponseData(false, e.getMessage()));
		}
	}
	
	/**
	 * 模块：远程控制
	 * 功能：远程控制数据下穿
	 * 范围：管理人员专用
	 */
	public void remote() {
		Integer id = getParaToInt("id");
		setAttr("id",     id);
		setAttr("device", getPara("device"));
		Record model = Db.findFirst("select t.*,t3.ipaddr,t1.dtu_code,t2.name,t2.addr intf_addr from site_device t "
				+ " left join site_collect t1 on t.collectId = t1.id"
				+ " left join device_interface t2 on t.linked = t2.code"
				+ " left join tanks t3 on find_in_set(t3.id, t.tank_ids)"
				+ " where t.id = ?", id);
		setAttr("model", model);
		render("remote.jsp");
	}
	
	/**
	 * 模块：远程控制
	 * 功能：获取下拉框数据
	 * 范围：管理人员专用
	 */
	public void getTypeAttrs() {
		Integer device = getParaToInt("device");
		String type = getPara("type");
		if(type.equals("attr"))
			renderJson(SiteDeviceAttr.me.getDeviceAttrs(device, ""));
		else if(type.equals("light"))
			renderJson(SiteDeviceLight.me.getDeviceLights(device));
		else if(type.equals("valve"))
			renderJson(SiteDeviceValve.me.getDeviceValves(device));
	}
	
	/**
	 * 模块：远程控制
	 * 功能：获取远程操作命令
	 * 范围：管理人员专用
	 */
	public void getTokenMap() {
		Integer device = getParaToInt("device");
		String attr = getPara("attr");
		String value = getPara("value");
		try {
			Map<String, String> tokenMap = RDCKit.getValveToken(device, attr, value);
			renderJson(new ResponseData(true, tokenMap));
		} catch (Exception e) {
			renderJson(new ResponseData(false, e.getMessage()));
		}
	}
	
	/**
	 * 模块：离线日志
	 * 功能：查询设备离线信息
	 * 范围：管理人员专用
	 */
	public void log() {
		Date now = new Date();
		setAttr("start", DateTools.formatDate(now,"yyyy-MM-dd") + " 00时");
		setAttr("end", DateTools.formatDate(DateTools.getNextHour(now),"yyyy-MM-dd HH时"));
		setAttr("device", getParaToInt("device"));
		render("log.jsp");
	}
	
	/**
	 * 模块：离线日志
	 * 功能：查询设备离线信息
	 * 范围：管理人员专用
	 */
	public void getLogPage() {
		renderJson(LogDevice.me.getPages(getParaToInt("device"), 
				getPara("start_date"), getPara("end_date"), 
				getParaToInt("page"), getParaToInt("limit")));
	}
	
	/**
	 * 模块：离线日志
	 * 功能：设备离线信息额外补充离线总时长和离线率
	 * 范围：管理人员专用
	 */
	public void getSumOffTime() {
		renderJson(LogDevice.me.getSumOffTime(getParaToInt("device"), 
				getPara("start_date"), getPara("end_date")));
	}
	
	/**
	 * 模块：消息提醒
	 * 功能：获取公司的监控设备信息
	 * 范围：管理人员专用
	 */
	public void notice() {
		Integer site = getParaToInt("site");
		setAttr("site", Db.findFirst("select a.id,a.name,a.address,b.path_name,c.name company_name" 
				+ " from sites a left join base_areas b on a.area_id = b.id"
				+ " left join base_companys c on a.company = c.id where a.id = ?", site));
		setAttr("excepts", SiteDevice.me.getExcepts(site, "", "<br />"));
		setAttr("users",   Db.find("select b.id,concat(b.name,' - ',b.email) name from sites a, base_users b"
				+ " where a.company = b.company and a.id = ? and b.state = 1 and b.email is not null and b.email != ''", site));
		render("notice.jsp");
	}
	
	/**
	 * 模块：站点修改
	 * 功能：保存站点配置信息
	 * 范围：管理人员专用
	 */
	public void saveSite() {
		try{
			Site.me.save(getModel(Site.class,"site"));
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"站点保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"站点保存失败!"));
		}
	}
	
	/**
	 * 模块：储罐修改
	 * 功能：保存储罐配置信息
	 * 范围：管理人员专用
	 */
	public void saveTank() {
		try{
			Tank.me.save(getModel(Tank.class,"tank"));
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"储罐保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"储罐保存失败!"));
		}
	}
	
	/**
	 * 模块：远传设备
	 * 功能：保存远传设备信息
	 * 范围：管理人员专用
	 */
	public void saveCollect() {
		try{
			SiteCollect.me.save(getModel(SiteCollect.class, "collect"));
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"远传设备保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"远传设备保存失败!"));
		}
	}
	
	/**
	 * 模块：外挂设备
	 * 功能：根据站点选择站点已有采集盒子
	 * 范围：管理人员专用
	 */
	public void getCollects(){
		Integer site = getParaToInt("site");
		renderJson(Db.find("select * from site_collect where state = 1 and siteId = ?", site));
	}
	
	/**
	 * 模块：外挂设备
	 * 功能：根据采集盒子获取采集盒子接口
	 * 范围：管理人员专用
	 */
	public void getLinked(){
		Integer collect = getParaToInt("collect");
		renderJson(Db.find("select t2.code,t2.name from site_collect t"
			+ " left join device_collect t1 on t.model = t1.model"
			+ " left join device_interface t2 on find_in_set(t2.id, t1.interfaces)"
			+ " where t.id = ?", collect));
	}
	
	/**
	 * 模块：外挂设备
	 * 功能：根据站点标识获取储罐列表
	 * 范围：管理人员专用
	 */
	public void getTanks(){
		Integer site = getParaToInt("site");
		renderJson(Db.find("select id,name from tanks where state = 1 and site_id = ?", site));
	}
	
	/**
	 * 模块：采集设备
	 * 功能：保存采集设备信息
	 * 范围：管理人员专用
	 */
	public void saveDevice() {
		try{
			SiteDevice device = getModel(SiteDevice.class, "device");
			String[] tanks = getParaValues("device.tank_ids");
			SiteDevice.me.save(device, tanks);
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"远传设备保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"采集设备保存失败!"));
		}
	}
	
	/**
	 * 模块：属性过滤
	 * 功能：保存属性过滤信息
	 * 范围：管理人员专用
	 */
	public void saveFilter() {
		try{
			Integer device = getParaToInt("deviceId");
			String type = getPara("type");
			String[] attrs = getParaValues("attrs");
			SiteDevice.me.saveFilter(device, type, attrs);
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"属性过滤保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"属性过滤保存失败!"));
		}
	}
	
	/**
	 * 模块：属性修改
	 * 功能：保存采集属性信息
	 * 范围：管理人员专用
	 */
	public void saveAttr() {
		try{
			SiteDeviceAttr attr = getModel(SiteDeviceAttr.class, "attr");
			SiteDeviceAttr.me.saveAttr(attr);
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"属性设置保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"属性设置保存失败!"));
		}
		
	}
	
	/**
	 * 模块：阀门修改
	 * 功能：保存阀门修改信息
	 * 范围：管理人员专用
	 */
	public void saveValve() {
		try{
			SiteDeviceValve valve = getModel(SiteDeviceValve.class, "attr");
			String action = getPara("action");
			SiteDeviceValve.me.saveValve(valve, action.trim());
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"阀门设置保存成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"阀门设置保存失败!"));
		}
	}
	
	/**
	 * 模块：指示灯修改
	 * 功能：保存阀门修改信息
	 * 范围：管理人员专用
	 */
	public void saveLight() {
		try{
			SiteDeviceLight light = getModel(SiteDeviceLight.class, "attr");
			SiteDeviceLight.me.saveLight(light);
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"指示灯操作成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"指示灯操作失败!"));
		}
	}
	
	/**
	 * 模块：属性编辑
	 * 功能：删除设备采集属性
	 * 范围：管理人员专用
	 */
	public void removeAttr() {
		Integer device = getParaToInt("device");
		String type = getPara("type");
		Integer id = getParaToInt("id");
		try{
			SiteDevice.me.removeAttr(device, type, id);
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"属性移除成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"属性移除失败!"));
		}
	}
	
	/**
	 * 模块：属性编辑
	 * 功能：批量删除设备信息
	 * 范围：管理人员专用
	 */
	public void empty() {
		String type = getPara("type");
		Integer device = getParaToInt("device");
		try{
			SiteDevice.me.emptyAttr(type, device);
			SocketClient.flushDb();
			renderJson(new ResponseData(true,"属性移除成功!"));
		}catch(Exception e){
			renderJson(new ResponseData(false,"属性移除失败!"));
		}
	}
	
	/**
	 * 模块：异常通知
	 * 功能：异常通知发送邮件
	 * 范围：管理人员专用
	 */
	public void sendEmail() {
		try{
			String[] users = getParaValues("notice.recipient");
			LogNotice notice = getModel(LogNotice.class,"notice");
			if(notice != null){
				String userIds = ToolKit.join(users, ',');
				notice.set("recipient", userIds)
					  .set("creator", SecurityContextUtil.getLoginUserId(getRequest()))
				      .set("create_time", new Date());
				String html = SiteDevice.me.getHtml(notice);
				notice.set("excepts", html)
					  .save();
				String address = User.me.getEmailAddrs(userIds);
				MessageUtils.sendEmail("notice", "设备诊断通知", address, html);
			}
		}catch(Exception e){
			renderJson(new ResponseData(false,"操作发生异常，异常操作如下: " + e.getMessage()));
			return;
		}
		renderJson(new ResponseData(true,"消息通知成功!"));
	}
	
	/**
	 * 模块：远程控制
	 * 功能：发送远程控制命令
	 * 范围：管理人员专用
	 */
	public void sendToken() {
		try{
			Integer device = getParaToInt("device");
			String ipAddr = getPara("ipaddr");
			String attr   = getPara("attr");
			if(StringUtils.isEmpty(ipAddr))
				throw new Exception("通讯地址不能为空！");
			if(StringUtils.isEmpty(attr))
				throw new Exception("属性编码不能为空！");
			String value =  getPara("value");
			if(StringUtils.isEmpty(value))
				throw new Exception("属性值不能为空！");
			Map<String,String> paraMap = RDCKit.getValveToken(device, attr, value);
			renderJson(RDCKit.sendCmd(ipAddr, paraMap.get("reg"), paraMap.get("token")));
		}catch(Exception e){
			renderJson(new ResponseData(false,"命令下发失败：" + e.getMessage() + "!"));
			return;
		}
	}
	
	/**
	 * 模块：远程控制
	 * 功能：发送远程控制命令
	 * 范围：管理人员专用
	 */
	public void sendLimitToken() {
		try{
			Integer device = getParaToInt("device");
			String ipAddr = getPara("ipaddr");
			String dtu = getPara("dtu"); 
			String attr = getPara("attr");
			String category = getPara("category");
			String value = getPara("value");
			if(StringUtils.isEmpty(ipAddr))
				throw new Exception("通讯地址不能为空！");
			if(StringUtils.isEmpty(dtu))
				throw new Exception("硬件编码不能为空！");
			if(StringUtils.isEmpty(category))
				throw new Exception("限制类型不能为空！");
			if(StringUtils.isEmpty(attr))
				throw new Exception("属性编码不能为空！");
			if(StringUtils.isEmpty(value))
				throw new Exception("属性值不能为空！");
			String token = RDCKit.getLimitToken(device, attr, category, true, value);
			ResponseData retn = RDCKit.sendCmd(ipAddr, "REG" + dtu, token);
			if(retn.isSuccess()){
				RDCKit.inst_sequence ++;
				retn = RDCKit.sendCmd(ipAddr, "REG" + dtu, RDCKit.INST_LIMIT_READ);
				@SuppressWarnings("unchecked")
				Map<String, String> dataMap = (Map<String, String>) retn.getData();
				SiteDeviceAttr.me.writeLimits(device, dataMap.get("token"));
			}
			renderJson(retn);
		}catch(Exception e){
			renderJson(new ResponseData(false,"命令下发失败：" + e.getMessage() + "!"));
		}
	}
}
