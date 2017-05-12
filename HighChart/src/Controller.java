/**
	 * 同比环比月报模块
	 * 跳转同比环比页面
	 */
	public void monthChartIndex() {
		Integer companyId = getParaToInt("company");
		if(companyId == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			companyId = user.	getInt("company");
		}
		Company company = Company.me.getById(companyId);
		List<Site> siteList = Site.me.find("select * from sites where company =" +companyId);
		List<Dict> factor = Dict.me .find("SELECT * FROM dict t where t.GROUP_CODE='TBHBMONTH.FACTOR' and t.state =1 and t.TYPE='I'");
		setAttr("company",company );
		setAttr("month", DateFormatUtils.format(new Date(), "yyyy"));
		setAttr("factor",factor );
		setAttr("siteList",siteList );
		render("monthChartIndex.jsp");
	}
	
	/**
	 * 同比环比月报模块
	  * 功能：获取环比柱状图
	 */
	public void getHBChart(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		
		String start_date = getPara("start_date").replace("-", "");
		if (start_date==null || start_date.isEmpty()) {
			start_date =DateFormatUtils.format(new Date(), "yyyy");
		}
	
		renderJson(RptMonthTank.me.HBChart(start_date,getPara("factor"),getParaToInt("site"),company));
	}
	
	/**
	 * 同比环比月报模块
	  * 功能：获取同比柱状图
	 */
	public void getTBChart(){
		Integer company = getParaToInt("company");
		if(company == null){
			User user = SecurityContextUtil.getLoginUser(getRequest());
			company = user.getInt("company");
		}
		
		 String start_date = getPara("start_date").replace("-", "");
		if (start_date==null || start_date.isEmpty()) {
			start_date =DateFormatUtils.format(new Date(), "yyyy");
		}
			
			
		renderJson(RptMonthTank.me.TBChart(start_date,getPara("factor"),getParaToInt("site"),company));
	}
	
	/**
	 * 同比环比月报模块
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