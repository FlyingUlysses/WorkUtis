package com.yawa.tank.model;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.yawa.http.SocketClient;
import com.yawa.util.model.Page;


/**
 *@Title:卡表充值日志表
 *@Description:
 *@Author:WangYong
 *@Since:2017-7-7
 *@Version:1.1.0
 */
@SuppressWarnings("serial")
public class LogRecharge extends Model<LogRecharge> {
    public final static LogRecharge me = new LogRecharge();
    
    public Page<Record> getPages(String name, Integer siteId, Integer company,
        Integer page, Integer limit) {
        String filter ="";
        filter = getFilter(name, siteId, company, filter);
        Long count =Db.queryLong("select count(1) from sites a left join site_collect b on a.id = b.siteId left join site_device c on b.id = c.collectId  left join site_csr_relas d on c.id = d.card_id left join csrs e on d.csr_id = e.id " 
                                 + "left join devices f on c.deviceId = f.id  where a.state = 1 and b.state = 1 and c.state = 1 and f.classify = 'card' and c.addr is not null "+filter);
        String sql ="select a.id site_id,a.name site_name, e.name, e.linked, e.mobile, concat(b.dtu_code, '_', c.addr) cardNum, g.card_gqzl,  g.card_syql, g.id data_id,f.name device_name from sites a "
                  +"  left join site_collect b on a.id = b.siteId "
                  +"  left join site_device c on b.id = c.collectId "
                  +"  left join site_csr_relas d on c.id = d.card_id "
                  +"  left join csrs e on d.csr_id = e.id "
                  +"  left join devices f on c.deviceId = f.id "
                  +"  left join data_cards g on c.id = g.device_id "
                  +"  where a.state = 1 and b.state = 1 and c.state = 1 and f.classify = 'card' and c.addr is not null "+filter+" order by a.id limit ?,?";
        
        return new Page<Record>(page, limit, count, Db.find(sql, (page - 1) * limit, limit));
    }




    public String getFilter(String name, Integer siteId, Integer company,
        String filter) {
        if (StringUtils.isNotBlank(name)) {
            filter +=" and e.name like '%"+name+"%' ";
        }
        if (company !=null) {
            filter +=" and a.company = "+company;
        }
        if (siteId != null) {
            filter +=" and a.id = "+siteId;
        }
        return filter;
    }

    
    
    
    public Object getReCardPay(Integer siteId, Integer company, String name) {
        String filter ="";
        filter = getFilter(name, siteId, company, filter);
        String sql ="select a.id site_id,a.name site_name, e.name, e.linked, e.mobile, concat(b.dtu_code, '_', c.addr) cardNum, g.card_gqzl,  g.card_syql, g.id data_id,f.id device_id ,e.id csr_id from sites a "
            +"  left join site_collect b on a.id = b.siteId "
            +"  left join site_device c on b.id = c.collectId "
            +"  left join site_csr_relas d on c.id = d.card_id "
            +"  left join csrs e on d.csr_id = e.id "
            +"  left join devices f on c.deviceId = f.id "
            +"  left join data_cards g on c.id = g.device_id "
            +"  where a.state = 1 and b.state = 1 and c.state = 1 and f.classify = 'card' and c.addr is not null "+filter;
        Record record = Db.findFirst(sql);
        return record;
    }




    public void recharge(String cardNum, Integer siteId, Integer deviceId,Integer dataId,Integer gasCount, Integer user, Integer csrId) throws Exception {
        Record data = Db.findFirst("SELECT card_gqzl,card_yqzl FROM data_cards where id = ?",deviceId);
        String start_reading = data.getStr("card_gqzl");
        String end_reading =Double.parseDouble(start_reading)+gasCount+"";
        LogRecharge log =new LogRecharge();
        log.set("site_id", siteId)
           .set("oskey", UUID.randomUUID().toString().replace("-", ""))
           .set("device_id", deviceId)
           .set("cardNum", cardNum)
           .set("start_reading", start_reading)
           .set("end_reading", end_reading)
           .set("recharge", gasCount)
           .set("state", 0)
           .set("csr_id",csrId)
           .set("creator", user)
           .set("create_time",new Date())
           .save();
        //调用充值接口
        Map<String, Object> hm =new HashMap<String,Object>();
        hm.put("site", siteId);
        hm.put("device", deviceId);
        hm.put("carNo", cardNum);
        hm.put("value", gasCount);
       JSONObject obj = SocketClient.invoke("cardReCharge", hm);
       Integer issueId =null;
       try {issueId=obj.getInteger("issue_id");} catch (Exception e) {}
       if (issueId!=null) {
        log.set("issue_id", issueId)
            .update();
       }
       //遍历循环查看数值是否已经更新
       for (int i = 0; i < 5; i++) {
           LogRecharge newLog = me.findById(log.getInt("id"));
           if (newLog.getInt("state")==1) {
               return ;
           }
           try {Thread.sleep(2000); } catch (InterruptedException e) {}
       }
      throw new Exception("未能成功充值！");
    }




    public Page<Record> getLogPages(String name, Integer siteId, 
        Integer page, Integer limit) {
        String filter ="";
        if (StringUtils.isNotBlank(name)) {
            filter+=" and e.name like '%"+name+"' ";
        }
        if (siteId != null) {
            filter +=" and lr.site_id ="+siteId;
        }
        Long count =Db.queryLong("select count(1) from log_recharge lr left join csrs e on lr.csr_id = e.id where true  " +filter);
        String sql ="SELECT lr.id,lr.issue_id,e.name name,lr.cardNum,lr.start_reading,lr.end_reading,lr.recharge,DATE_FORMAT(lr.create_time,'%Y-%m-%d %H:%i') date, "
                     +"  (select name from base_users where id =lr.creator) creator, "
                     +"  (case when lr.state=0 then '充值中' when lr.state =1 then '充值成功' when lr.state = 2 then '充值失败' else '未知状态' end) state FROM log_recharge lr "
                     +"  left join csrs e on lr.csr_id = e.id where true "+filter+" order by lr.create_time,lr.site_id limit ?,?";
        return new Page<Record>(page, limit, count, Db.find(sql, (page - 1) * limit, limit));
    }

    
    
    
}
