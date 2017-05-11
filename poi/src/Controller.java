import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.render.RenderException;
import com.yawa.core.interceptor.SessionInterceptor;
import com.yawa.core.model.Company;
import com.yawa.core.model.Dict;
import com.yawa.core.model.User;
import com.yawa.rpt.model.RptMonthTank;
import com.yawa.tank.model.Site;
import com.yawa.util.SecurityContextUtil;
import com.yawa.util.excel.PoiMergeExporter;


@Before(SessionInterceptor.class)
public class Controller {

	public void exportExcel() {
		String start_date = getPara("start_date").replace("-", "").substring(0,6);
		String end_date = getPara("end_date").replace("-", "").substring(0,6);
		Integer site = getParaToInt("site");
		Integer company=getParaToInt("company");
		try {
			String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ "月报.xls", "utf-8");
			getResponse().reset();
			getResponse().setHeader("Content-disposition","attachment; filename=" + fileName);
			getResponse().setContentType("application/msexcel;charset=UTF-8");
			OutputStream os = null;
			try {
				String sheetSQL = " and ( t.company= "+ company+" OR t.company IN(SELECT id FROM base_companys a WHERE a.parent="+ company + ") ) ";
				if(site != null) sheetSQL += " and id = " + site;
				
				String rowFilter = RptMonthTank.me.getFilters(null,start_date, end_date,company);
				os = getResponse().getOutputStream();
				PoiMergeExporter.getInstance().version("2003").cellWidth(2200).exportExcel("rpt_month_tank", sheetSQL, rowFilter).write(os);
				
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