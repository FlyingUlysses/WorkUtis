package config;

import utils.LogMessage;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;


import controller.SendController;

public class DbConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setViewType(ViewType.JSP);
		PropKit.use("jdbc_config.txt");
		me.setEncoding("UTF-8");
	}

	@Override
	public void configRoute(Routes me) {
			me.add("/", SendController.class, "/send");
	}

	@Override
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
				DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), 
						PropKit.get("user"),PropKit.get("password").trim());
				DruidPlugin meterPlugin = new DruidPlugin(PropKit.get("meterUrl"), 
						PropKit.get("user"),PropKit.get("password").trim());
				me.add(druidPlugin);
				me.add(meterPlugin);
		// 配置ActiveRecord插件
				ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
				ActiveRecordPlugin meterArp = new ActiveRecordPlugin("meter",meterPlugin);
				arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));		//忽略大小写  
				meterArp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//忽略大小写
				me.add(arp);
				me.add(meterArp);
				
				arp.addMapping("log_messages", LogMessage.class);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

}
