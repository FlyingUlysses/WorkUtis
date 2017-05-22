package com.yawa.tank.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.yawa.util.model.Page;

@SuppressWarnings("serial")
public class LogDevice extends Model<LogDevice> {
	public static final LogDevice me = new LogDevice();
	
	public Page<LogDevice> getPages(Integer device,String start, String end,
			Integer page, Integer limit){
		String sql = "select count(1) from log_devices"
			+ " where ((state = 0 and off_time is not null and off_time < str_to_date(?,'%Y-%m-%d %H'))"
			+ " or (state = 1 and on_time >= str_to_date(?,'%Y-%m-%d %H') and on_time <= str_to_date(?,'%Y-%m-%d %H')))"
			+ " and device_id = ?"; 
		Long count = Db.queryLong(sql, end,start,end,device);
		sql = "select id,device_id,off_time,on_time,(case "
			+ " when state = 0 then timestampdiff(second,off_time,now()) "
			+ "	when state = 1 and off_time is null then timestampdiff(second,create_time,on_time)"
			+ " when state = 1 and off_time is not null then timestampdiff(second,off_time, on_time) end) off_seconds"
			+ " from log_devices where ((state = 0 and off_time is not null and off_time < str_to_date(?,'%Y-%m-%d %H'))"
			+ " or (state = 1 and on_time >= str_to_date(?,'%Y-%m-%d %H') and on_time < str_to_date(?,'%Y-%m-%d %H')))"
			+ " and device_id = ? order by id asc limit ?,?";
		return new Page<LogDevice>(page, limit, count, 
				find(sql, end,start,end,device,(page - 1) * limit,limit));
	}

	
	/**
	 * 查询总离线时长，并根据查询时间计算离线率
	 * @param device
	 * @param start
	 * @param end
	 * @return
	 */
	public Record getSumOffTime(Integer device, String start, String end)  {
		//算离线总时长，离线数据有部分处于选择时间内，将会以离线数据完整离线时间为准	
		String sql = "select sum((case "
				+ " when state = 0 then timestampdiff(second,off_time,now()) "
				+ "	when state = 1 and off_time is null then timestampdiff(second,create_time,on_time)"
				+ " when state = 1 and off_time is not null then timestampdiff(second,off_time, on_time) end)) sum_off_seconds "
				+ " from log_devices where ((state = 0 and off_time is not null and off_time < str_to_date(?,'%Y-%m-%d %H'))"
				+ " or (state = 1 and on_time >= str_to_date(?,'%Y-%m-%d %H') and on_time < str_to_date(?,'%Y-%m-%d %H')))"
				+ " and device_id = ?";
			Record TempRecord = Db.findFirst(sql,end,start,end,device);
			
		//计算离线率，只计算用户选择的开始至结束时间内的数据
		sql ="select sum((case  when state = 0 and off_time > str_to_date(?,'%Y-%m-%d %H') then timestampdiff(second,off_time,str_to_date(?,'%Y-%m-%d %H')) "
						+"when state = 0 and off_time < str_to_date(?,'%Y-%m-%d %H') then timestampdiff(second,?,str_to_date(?,'%Y-%m-%d %H'))"
						+" when state = 1 and off_time is null then timestampdiff(second,create_time,on_time) " 
						+" when state = 1 and off_time is not null and  off_time <? then timestampdiff(second,?, on_time) "
						+" when state = 1 and off_time is not null and  off_time >? then timestampdiff(second,off_time, on_time) end)) off_seconds " 
						 +" from log_devices "
						+" where ((state = 0 and off_time is not null and off_time < str_to_date(?,'%Y-%m-%d %H')) "
						+" or (state = 1 and on_time >= str_to_date(?,'%Y-%m-%d %H') "
						+"  and on_time < str_to_date(?,'%Y-%m-%d %H'))) and device_id = ? ";
		Record record = Db.findFirst(sql,start,end,start,start,end,start,start,start,end,start,end,device);
		record.set("sum_off_seconds",TempRecord.get("sum_off_seconds"));
		
			
			start = start.substring(0,start.length()-1).replace("-", "").replace(" ", "");
			end = end.substring(0,end.length()-1).replace("-", "").replace(" ", "");
			SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMddHH"); 
			try {
				Date startDate = sdf.parse(start);
				Date endDate = sdf.parse(end);
				Double sumTime =  (double) ((endDate.getTime()-startDate.getTime())/1000);
				BigDecimal TempSumOff =  record.getBigDecimal("off_seconds");
				if (TempSumOff!=null) {
					Double sumOff = TempSumOff.doubleValue();
					DecimalFormat df=new DecimalFormat("#.##");
					String off_rate = df.format((1-sumOff/sumTime)*100)+"%";
					record.set("off_rate", off_rate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return record;
	}
}