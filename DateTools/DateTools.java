import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateTools {
	
	public static String formatDate(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}
	
	public static Date parseData(String date){
		if(StringUtils.isNotEmpty(date)){
			try {
				return DateUtils.parseDate(date, new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String formatDate(Date date, String format) {
		return DateFormatUtils.format(date, format);
	}
	
	public static String formatTime(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date getLast(Date date) {
		return DateUtils.add(date, Calendar.DAY_OF_MONTH, -1);
	}
	
	public static Date getLastHour(Date date) {
		return DateUtils.add(date, Calendar.HOUR_OF_DAY, -1);
	}
	
	public static Date getLastMonth(Date date) {
		return DateUtils.add(date, Calendar.MONTH, -1);
	}
	
	public static Date getNext(Date date) {
		return DateUtils.add(date, Calendar.DAY_OF_MONTH, 1);
	}

	public static Date getNextHour(Date date) {
		return DateUtils.add(date, Calendar.HOUR_OF_DAY, 1);
	}
	
	public static List<String> listAll(String month){
		List<String> all = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		int nm = cal.get(Calendar.MONTH);
		int nd = cal.get(Calendar.DAY_OF_MONTH);
		try {
			cal.setTime(DateUtils.parseDate(month, new String[]{ "yyyy-MM" }));
			cal.set(Calendar.DATE, 1);
			int m = cal.get(Calendar.MONTH);
			while(cal.get(Calendar.MONTH) == m && m <= nm){
				if(m < nm || m == nm && nd >= cal.get(Calendar.DAY_OF_MONTH))
					all.add(formatDate(cal.getTime(), "MM-dd"));
				cal.add(Calendar.DATE, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return all;
	}
	
	public static String transTime(String seconds){
		if(StringUtils.isEmpty(seconds))
			return seconds;
		long ss = 0;
		try{
			ss = Long.parseLong(seconds);
		}catch(Exception e){
			return seconds;
		}
		String stamp = "";
		if(ss > 86400){
			stamp += (ss / 86400) + "天";
			ss = ss % 86400;
		}
		if(ss > 3600){
			stamp += (ss / 3600) + "小时";
			ss = ss % 3600;
		}
		if(ss > 60){
			stamp += (ss / 60) + "分钟";
			ss = ss % 60;
		}
		if(ss > 0){
			stamp += ss + "秒";
		}
		return stamp;
	};
}
