package com.yawa.tank.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.yawa.util.model.Page;

@SuppressWarnings("serial")
public class Tank extends Model<Tank> {
	
	public static final Tank me = new Tank();

	public void save(Tank tank){
		if(tank != null){
			tank.set("update_date", new Date());
			if(tank.getInt("id") == null)
				tank.set("create_date", new Date())
					.save();
			else
				tank.update();
		}
	}
	
	
	/**
	 * 模块：储罐配置
	 * 功能：展示储罐分页列表
	 */
	public Page<Tank> getPages(String name,Integer company, Integer page, Integer limit) {
		String filter = "";
		if(company != null)
			filter += " and s.company = " + company; 
		if(StringUtils.isNotEmpty(name))
			filter += " and t.name like '%" + name + "%'";
		String sql = "select count(1) from tanks t "
			+ " left join sites s on t.site_id = s.id"
			+ " left join base_companys c on s.company = c.id"
			+ " where t.state = 1" + filter; 
		Long count = Db.queryLong(sql);
		sql = "select t.id,t.name,class.name classify,a.path_name,t.address,tm.capacity cube,t.water,"
			+ " (select group_concat(name) from dict where group_code = 'TANK.DEVICE' and find_in_set(code, t.device)) device,"
			+ " tm.model, t.create_date from tanks t"
			+ " left join base_areas a on t.area_id = a.id"
			+ " left join (select code,name from dict where group_code = 'TANK.CLASSIFY' and type = 'I') class on t.classify = class.code"
			+ " left join tank_model tm on t.model = tm.id"
			+ " left join sites s on t.site_id = s.id"
			+ " left join base_companys c on s.company = c.id"
			+ " where t.state = 1" + filter
			+ " order by t.id asc limit ?,?";
		return new Page<Tank>(page, limit, count, find(sql,(page - 1) * limit,limit));
	}
	
	/**
	 * 模块：首页
	 * 功能：当前公司储罐数量
	 * @param company
	 * @return
	 */
	public Long getTotal(Integer company){
		return Db.queryLong("select count(1) from sites t,tanks t1,sites t2,base_companys t3"
			+ " where t.id = t1.site_id and t1.site_id = t2.id and t2.company = t3.id and t.state = 1 and t1.state = 1"
			+ " and find_in_set(?, t3.path)", company);
	}
	
	/**
	 * 液罐详情
	 * @param id
	 * @return
	 */
	public Tank getById(Integer id) {
		String sql = "select t.*,ar.path_name"
			+ " from tanks t left join base_areas ar on t.area_id = ar.id"
			+ " where t.id = ?";
		return findFirst(sql,id);
	}

	/**
	 * 获取站点储罐
	 * @param site
	 * @return
	 */
	public List<Tank> getSiteTanks(Integer site){
		return find("select a.*,b.model model_name,class.name classify_name"
			+ " from tanks a "
			+ " left join tank_model b on a.model = b.id"
			+ " left join (select code,name from dict where group_code = 'TANK.CLASSIFY' and type = 'I') class on a.classify = class.code"
			+ " where a.state = 1 and a.site_id = ?", site);
	}
	
	/**
	 * 页面指示灯展示
	 * @param tankid
	 * @return
	 */
	public Map<String,Object> getSwitchs(int tankid) {
		List<TankAttr> attrs = TankAttr.me.getSwitchAttrs(tankid, "92");
		List<TankAttr> firstRow = new ArrayList<TankAttr>();
		List<TankAttr> secondRow = new ArrayList<TankAttr>();
		for(int i = 0;i < attrs.size();i++){
			if(i < 4)
				firstRow.add(attrs.get(i));
			else
				secondRow.add(attrs.get(i));
		}
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("first", firstRow);
		attrMap.put("second", secondRow);
		return attrMap;
	}
	

	/**
	 * 新web页面指示灯展示
	 * @param tankid
	 * @return
	 */
	public Map<String,Object> getNewSwitchs(int tankid) {
		List<TankAttr> attrs = TankAttr.me.getSwitchAttrs(tankid, "92");
		Map<String,Object> attrMap = new HashMap<String, Object>();
		attrMap.put("light", attrs);
		return attrMap;
	}
	
	
	/**
	 * 模块：首页领导视图
	 * 功能：获取公司储罐数量
	 * @param int1
	 * @return
	 */
	public List<Record> getTankCharts(Integer company) {
		return Db.find("select t2.id,t2.name,count(*) cnt from tanks t,sites t1,base_companys t2"
				+ " where t.site_id = t1.id and t1.company = t2.id and t.state = 1 and t1.state = 1"
				+ " and find_in_set(?, t2.path) group by t2.id,t2.name", company);
	}
	
	/**
	 * 获取站点列表
	 * @param parent
	 * @return
	 */
	public List<Tank> getSites(String parent,Integer company) {
		String sql = "";
		if(parent.equals("#")){
			if(company != null){
				sql = " and company = " + company;
			}
		}else
			sql = " and site_id = " + parent.replace("c_", "");
		List<Tank> tank = find("select cast(concat('c_',id) as char) id,name text,1 level,0 cnt"
				+ " from tanks t "
				+ " where state = 1 " + sql);
		for(Tank comp: tank)
			comp.put("children", comp.getLong("cnt") > 0);
		return tank;
	}
	

	/**
	 * 模块：液位监控
	 * 功能：获取储罐液位数据
	 * @param company
	 * @return
	 */
	public List<Record> getScales(String name,Integer company) {
		String filter = "";
		if(StringUtils.isNotEmpty(name))
			filter = " and t.name like '%" + name + "%'";
		return Db.find("select t.id, t.name, dt.scale, dt.pressure, concat((100 - dt.rate) * 0.64 + 18,'%') rate, dl.ext_flow, sd.online"
			+ " from tanks t left join data_tanks dt on find_in_set(t.id, dt.tank_ids)"
			+ " left join data_lljs dl on find_in_set(t.id, dl.tank_ids) left join site_device sd on dt.device_id = sd.id"
			+ " where t.state = 1 and t.company = ?" + filter + " order by t.id asc", company);
	}
}