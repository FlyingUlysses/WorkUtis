package com.yawa.tank.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

@SuppressWarnings("serial")
public class TankAttr extends Model<TankAttr> {
	
	public static final TankAttr me = new TankAttr();
	
	/**
	 * 获取设备数据数据
	 * @param device
	 * @return
	 */
	public List<Record> getDeviceAttrs(Integer device){
		String sql = "select distinct a.id,a.attr_id,a.name,a.range,b.initial,"
			+ " concat(b.value,' ',a.unit) value,a.limits,b.update_date, b.alert"
			+ " from site_device_attr a"
			+ " inner join tank_attrs b"
			+ " on a.deviceId = b.device_id and a.attr_id = b.attr_id"
			+ " where a.state = 1 and a.deviceId = ? order by a.sort asc";
		return Db.find(sql, device);
	}
	
	public List<Record> getDeviceLights(Integer device){
		String sql = "select distinct a.id,a.attr_id,a.name,b.value,c.color"
			+ " from site_device_light a"
			+ " left join tank_attrs b"
			+ " on a.deviceId = b.device_id and a.attr_id = b.attr_id"
			+ " left join core_lights c"
			+ " on a.attr_id = c.attr_id and b.value = c.value"
			+ " where a.state = 1 and a.deviceId = ? order by a.sort asc";
		return Db.find(sql, device);
	}
	
	public List<Record> getDeviceValves(Integer device){
		String sql = "select distinct a.id,a.attr_id,a.name,b.value,c.action"
			+ " from site_device_valve a"
			+ " left join tank_attrs b"
			+ " on a.deviceId = b.device_id and a.attr_id = b.attr_id"
			+ " left join site_valve_action c"
			+ " on a.id = c.valveId and b.value = c.code"
			+ " where a.state = 1 and a.deviceId = ? order by a.sort asc";
		return Db.find(sql, device);
	}
	
	/**
	 * 模块：储罐查看
	 * 功能：查看储罐属性信息
	 * @param id
	 * @return
	 */
	public List<TankAttr> getValAttrs(Integer id) {
		String sql = "select distinct a.attr_id, c.name attr_name, concat(a.value,' ',c.unit) fval, "
			+ " (case when b.online = 1 and a.alert is not null then '#de577b' end) color,"
			+ " (case when b.online = 0 then 'off' end) clazz"
			+ "  from tank_attrs a, site_device b, site_device_attr c"
			+ "  where a.device_id = b.id and b.id = c.deviceId and a.attr_id = c.attr_id "
			+ "  and find_in_set(?, b.tank_ids) and b.state = 1 and c.state = 1 order by c.sort asc";
		return find(sql,id);
	}
	
	/**
	 * 模块：储罐查看
	 * 功能：查看储罐属性信息
	 * @param id
	 * @return
	 */
	public List<TankAttr> getNewValAttrs(Integer id) {
		String sql = "SELECT DISTINCT a.device_id,d.classify,(SELECT dd.name FROM dict dd WHERE dd.`GROUP_CODE`='DEVICE.CLASSIFY' AND dd.`CODE`=d.classify ) classifyname, a.attr_id, c.name attr_name, a.value fval,c.unit unit, "
					+" (CASE WHEN b.online = 1 and a.alert is not null THEN 'orange' END) color, " 
					+" (CASE WHEN b.online = 0 THEN 'off' END) clazz " 
					+" FROM tank_attrs a, site_device b, site_device_attr c,devices d "
					+" WHERE a.device_id = b.id AND b.id = c.deviceId AND a.attr_id = c.attr_id "
					+" AND b.deviceid= d.id "
					+" AND FIND_IN_SET(?, b.tank_ids) AND b.state = 1 AND c.state = 1 ORDER BY c.sort ASC ";
		return find(sql,id);
	}
	
	/**
	 * 模块：储罐查看
	 * 功能：查看储罐阀门信息
	 * @param id
	 * @return
	 */
	public List<TankAttr> getSwitchAttrs(Integer id,String except) {
		String filter = StringUtils.isEmpty(except)? "" : " and b.attr_id <> '" + except + "'";
		String sql = "select b.name app_name,(case when (a.online = 0 or d.light is null) then 'hu.gif' else d.light end) light"
			+ " from site_device a inner join site_device_light b on a.id = b.deviceId"
			+ " left join tank_attrs c on a.id = c.device_id and b.attr_id = c.attr_id"
			+ " left join core_lights d on c.attr_id = d.attr_id and c.value = d.value"
			+ " where find_in_set(?, a.tank_ids)  and a.state = 1 and b.state = 1 " + filter + " order by b.sort asc";
		return find(sql, id);
	}
}
