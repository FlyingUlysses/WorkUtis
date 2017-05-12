

@SuppressWarnings("serial")
public class RptMonthTank extends Model<RptMonthTank>{
	public static final RptMonthTank me = new RptMonthTank();
	
	/**
	 *返回查询附加条件
	 * @return
	 */
	public String getFilters(Integer site, String start, String end,Integer company) {
		String filter = "";
		
		if (company!=null) {
			filter +=" and ( t.company= "+ company+" OR t.company IN(SELECT id FROM base_companys a WHERE a.parent="+ company + ") ) ";
		}
		if (StringUtils.isNotEmpty(start)) {
			filter += " and t.cycle >= '" + start + "' ";
		}
		if (StringUtils.isNotEmpty(end)) {
			filter += " and t.cycle <= '" + end + "' ";
		}
		if (site!=null) {
			filter+= " and t.site_id ="+site+" ";
		}
		
		return filter;
	}
	
	
	/**
	 * 查询分页信息
	 * @param company
	 * @param site
	 * @param start
	 * @param end
	 * @param page
	 * @param limit
	 * @return
	 */
	public Page<Record> getPages(Integer company, Integer site,String start, String end,
			Integer page, Integer limit) {
		
			String filter = getFilters(site, start, end,company);
			
			String sql="SELECT count(1) from rpt_month_tank t where 1=1"+ filter;
			
			Long count = Db.queryLong(sql);
			
			
			
			sql="SELECT (SELECT name FROM base_companys where id =t.company) companyName,s.name siteName,date_format(str_to_date(t.cycle,'%Y%m'),'%Y-%m') cycle,t.sumfilling,t.sumflow,t.cardflow,t.gasrate,t.onlinerate " 
					+ " FROM rpt_month_tank t LEFT JOIN sites s on t.site_id=s.id where 1=1" +filter +" ORDER BY t.cycle asc limit ?,?";
			
			return new Page<Record>(page, limit, count, Db.find(sql, (page - 1)
					* limit, limit));
	}

	
	
	
	
	/**
	 * 获取环比柱状图
	 * @param site 
	 * @param string 
	 * @param year
	 * @param company
	 * @return
	 */
	
	public List<Record> HBChart(String start_date,String factor, Integer site, Integer company) {
		String filter = getMonthChartFilters(site, start_date,company);
		String sql="select date_format(str_to_date(t.cycle,'%Y%m'),'%Y-%m') month,cast( sum("+factor+") AS SIGNED) cnt "
		 +" from rpt_month_tank t,base_companys t1 where t.company =t1.id "+filter+" GROUP BY month";
		return Db.find(sql);
	}

	
	
	/**
	 * 获取同比柱状图
	 * @param site 
	 * @param string 
	 * @param year
	 * @param company
	 * @return
	 */
	
	public List<Record> TBChart(String start_date,String factor, Integer site, Integer company) {
		String filter = getMonthChartFilters(site, start_date,company);
		String sql="select date_format(str_to_date(t.cycle,'%Y%m'),'%Y-%m') month,cast( sum("+factor+") AS SIGNED) newCnt "
		 +" from rpt_month_tank t,base_companys t1 where t.company =t1.id "+filter+" GROUP BY month";
		List<Record> newYear = Db.find(sql);
		
//判断传值是整年‘2yyy’还是具体到月‘2yyyMM’		
		String tempYear ="";
		int date = Integer.parseInt(start_date);
		if (date>3000) {
			tempYear=(date -= 100)+"";
		}else if (date<3000) {
			tempYear=(date -= 1)+"";
		}
		
		filter = getMonthChartFilters(site, tempYear,company);
		 sql="select date_format(str_to_date(t.cycle,'%Y%m'),'%Y-%m') month,cast( sum("+factor+") AS SIGNED) lastCnt "
				 +" from rpt_month_tank t,base_companys t1 where t.company =t1.id "+filter+" GROUP BY month";
		List<Record> lastYrar = Db.find(sql);
		
		
		List<Record> result =new ArrayList<Record>();
		for (Record tempNew : newYear) {
			Record temp =new Record();
			temp.set("newCnt", tempNew.getNumber("newCnt"));
			temp.set("month", tempNew.getStr("month").substring(5));
			temp.set("newMonth", tempNew.getStr("month"));
			int lastMonth = Integer.parseInt(tempNew.getStr("month").replace("-", ""))-100;
			temp.set("lastMonth", (lastMonth+"").substring(0,4)+"-"+(lastMonth+"").substring(4));
			
			for (Record tempLast : lastYrar) {
				if (lastMonth==(Integer.parseInt(tempLast.getStr("month").replace("-", "")))) {
					temp.set("lastCnt", tempLast.getNumber("lastCnt"));
				}
			}
			result.add(temp);
			
		}
		return result;
	}
	
	
	
	/**
	 * 返回同比环比比较条件
	 * @param site
	 * @param start_date
	 * @param company
	 * @param factor
	 * @return
	 */
	public String getMonthChartFilters(Integer site, String start_date,
			Integer company) {
		String filter = "";
		
		if (company!=null) {
			filter +=" and ( t.company= "+ company+" OR t.company IN(SELECT id FROM base_companys a WHERE a.parent="+ company + ") ) ";
		}
		if (StringUtils.isNotEmpty(start_date)) {
			filter += " and t.cycle like '" + start_date + "%' ";
		}
		if (site!=null) {
			filter+= " and t.site_id ="+site+" ";
		}
		
		return filter;
	}

	
}