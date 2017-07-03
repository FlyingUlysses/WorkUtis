package com.yawa.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yawa.common.ConstantItems;
import com.yawa.common.DataPkg;
import com.yawa.tank.model.LedgerSiteInspect;
import com.yawa.tank.model.LedgerSiteInspectItems;
import com.yawa.util.DateTools;
import com.yawa.util.model.Attach;
/**
 * 
 *@Title:台账巡检模块
 *@Description:
 *@Author:WangYong
 *@Since:2017-6-27
 *@Version:1.1.0
 */
public class APPLedgerInspect {
    public static final APPLedgerInspect me = new APPLedgerInspect();
    
    
    /**
     * 首页信息
     * @param datapkg
     * @return
     * @Description:
     */
    public DataPkg getPageLedgerInsp(DataPkg datapkg) throws Exception {
        DataPkg rtdatapkg = new DataPkg();
        rtdatapkg.setRetcode(ConstantItems.CODE_COMMON_SUCCESS);
        JSONObject jsonObj = datapkg.getInParam();
        String state = null;
        String filter ="";
        try{ state =  jsonObj.getString("state"); }catch(Exception e){}
        if (StringUtils.isBlank(state) && state =="except" ) {
            filter+="where s.state = "+3;
        }else if (StringUtils.isBlank(state) && state =="own") {
            filter+="where s.creator  = "+datapkg.getOperID();
        }
        List<Record> data = Db.find("SELECT s.id,"
                            +" (SELECT name from base_users where id = s.creator ) creator_name,s.creator,s.start_time,s.end_time,"
                            +" (SELECT name from sites  where id =s.site_id) site_name,s.site_id, "
                            +" (case when s.state =0 then '无效' when s.state =1 then '未完成' when s.state =2 then '巡检正常' when s.state =3 then '巡检异常' end) state_name ,s.state "
                            +" FROM site_inspect s  "+filter);
        for (int i = 0; i < data.size(); i++) {
            if (StringUtils.isBlank(state)&& state!="own" && data.get(i).getInt("creator")!=datapkg.getOperID()) {
                data.remove(i);
            }else{
                List<Record> images = Db.find("select concat((select value from app_base where code = 'access_path'),'/',a.attach_type,'/',sii.id,'/',a.store_name) image , a.id from attaches a  join site_inspect_items sii on a.attach_id=sii.id "
                    +"  join site_inspect si on si.id = sii.inspect_id "
                    +"  where a.state =1 and a.attach_type=? and si.id =? limit 0,3 ",com.yawa.util.model.ConstantItems.APP_LEDGERINSPECT,data.get(i).getInt("id"));
                data.get(i).set("images", images);
            }
        }
        rtdatapkg.setOutParam(data);
        return rtdatapkg;
    }
    
    
    /**
     * 获取巡检台账列表
     * @param datapkg
     * @return
     * @Description:
     */
    public DataPkg getSiteLedgers(DataPkg datapkg) throws Exception {
        DataPkg rtdatapkg = new DataPkg();
        rtdatapkg.setRetcode(ConstantItems.CODE_COMMON_SUCCESS);
        JSONObject jsonObj = datapkg.getInParam();
        JSONObject site =null;
        Integer inspectId =null;
        HashMap<String,Object> hm =new HashMap<String, Object>();
        try{ site =  jsonObj.getJSONObject("site"); }catch(Exception e){}
        try{ inspectId =  jsonObj.getInt("inspectId"); }catch(Exception e){}
        Record inspect = null;
        if (inspectId ==null) {
            if (site ==null) {
                throw new Exception("获取站点信息失败！");
            }
            try{
                    //如果没有根据站点生成巡检相关信息
                    LedgerSiteInspect inspectTemp = new LedgerSiteInspect();
                    inspectTemp.set("site_id", site.getInt("site_id"))
                    .set("start_time", new Date())
                    .set("stat_date",DateTools.formatDate(new Date(), "yyyyMMdd"))
                    .set("location",site.getString("location"))
                    .set("address", site.getString("address"))
                    .set("state",1)
                    .set("company", 2)
                    .set("creator",2)
                    .set("create_time",new Date());
                    inspectTemp.save();
                    List<Record> ledgers = Db.find("SELECT * FROM site_ledgers sl where sl.state =1 and  sl.site_id =? and sl.state =1 and sl.parent is null",site.getInt("site_id"));
                    for (Record record : ledgers) {
                        LedgerSiteInspectItems item =new LedgerSiteInspectItems();
                        item.set("inspect_id", inspectTemp.getInt("id"))
                        .set("ledger_id", record.getInt("id"))
                        .set("start_time", new Date())
                        .set("state",2);
                        item.save();
                        List<Record> subItemList = Db.find("SELECT * FROM site_ledgers sl where sl.state =1 and  sl.site_id =? and sl.state =1 and sl.parent =?",site.getInt("site_id"),record.getInt("id"));
                        for (Record subRecord : subItemList) {
                            LedgerSiteInspectItems sub =new LedgerSiteInspectItems();
                            sub.set("inspect_id", inspectTemp.getInt("id"))
                            .set("ledger_id", subRecord.getInt("id"))
                            .set("start_time", new Date())
                            .set("state",2)
                            .set("parent", item.getInt("id"));
                            sub.save();
                        }
                    }
                    inspectId=inspectTemp.getInt("id");
                    
            }catch (Exception e) {
                rtdatapkg.setRetcode(ConstantItems.CODE_COMMON_BUSFAILED);
                rtdatapkg.setRetmsg("新增巡检失败:" + e.getMessage());
            }
        }
        inspect = Db.findFirst("SELECT si.id,si.site_id,s.name site_name,si.location,si.address FROM site_inspect si LEFT JOIN sites s on si.site_id =s.id where s.state =1 and si.id=? ",inspectId);
        List<Record> itemList = Db.find("SELECT sii.id,sl.name, "
                              +"  concat((select value from app_base where code = 'access_path'),'/',?,'/',sii.id,'/',"
                              +" (select store_name from attaches where attach_type = ? and state =1 and attach_id =sii.id limit 0,1) ) image ,"
                              +" (case when sii.state =1 then '正常' when sii.state =0 then '异常' when sii.state =2 then '未完成' end ) state_name,sii.state"
                              +" FROM site_inspect_items sii  JOIN site_inspect si on sii.inspect_id = si.id "
                              +" JOIN site_ledgers sl on sii.ledger_id = sl.id where sl.state =1 and sii.parent is null and si.site_id =?",
                              com.yawa.util.model.ConstantItems.APP_LEDGERINSPECT,com.yawa.util.model.ConstantItems.APP_LEDGERINSPECT,
                              inspect.getInt("site_id"));
        inspect.set("count", itemList.size());
        hm.put("inspect", inspect);
        hm.put("itemList",itemList);
        rtdatapkg.setOutParam(hm);
        return rtdatapkg;
    }
    

   