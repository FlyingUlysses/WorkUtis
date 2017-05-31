package utils;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;


@SuppressWarnings("serial")
public class LogMessage extends Model<LogMessage> {
	
	public static final LogMessage me = new LogMessage();
	
	private String filterSQL(String service, String mobile, String message){
		String sql = "";
		if(StringUtils.isNotEmpty(message)){
			sql += " and t.message like '%" + message + "%'";
		}
		if(StringUtils.isNotEmpty(mobile)){
			sql += " and t.mobile like '%" + mobile + "%'";
		}
		if(StringUtils.isNotEmpty(service)){
			sql += " and t.service = '" + service + "'";
		}
		return sql;
	}
	
	public Page<LogMessage> getPages(String service, String mobile, String message, Integer page,
			Integer limit) {
		String filter = filterSQL(service, mobile, message);
		String sql = "select count(1) from log_messages t where 1=1";
		Long count = Db.queryLong(sql + filter);
		sql = "select t.*,d.name service_name  from log_messages t left join (select code,name from dict"
			+ " where group_code = 'MESSAGE.SERVICE' and type = 'I') d on t.service = d.code"
			+ " where 1 = 1" + filter + " order by t.id desc limit ?,?";
		return new Page<LogMessage>(page, limit, count, find(sql, (page - 1) * limit,limit));
	}
}