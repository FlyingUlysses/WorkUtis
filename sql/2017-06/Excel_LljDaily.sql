set @excelId =(SELECT id from excels where busiType = 'rpt_day_llj');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'rpt_day_llj';


set @excelId =(SELECT id from excels where busiType = 'rpt_hour_llj');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'rpt_hour_llj';


INSERT INTO excels ( `busiType`, `name`, `state`) VALUES ( 'rpt_day_llj', '流量计日报每天', '1');

set @excelId =(SELECT LAST_INSERT_ID());
set @templateTemp =((SELECT max(template) from ngms.excel_parts) +1);

INSERT INTO excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) VALUES ( @excelId, 'select id,name from sites t where t.state = 1', NULL, 'select s.name,date_format(str_to_date(l.stat_date,\'%Y%m%d%H\'),\'%Y-%m-%d\')newdate,\r\n     l.ext_flow,l.flow_bk,l.flow_gk,l.llj_temp,l.llj_press,l.total_used \r\n    from rpt_hour_llj l left join sites s on s.id = l.site_id where s.state=1  and l.stat_date like \'%00\' and l.site_id=#id #filter order by newdate', @templateTemp, '1', '1');


INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '流量计日报', NULL, NULL, '0', '0', 'Y', '0', '8', '', '', NULL, '', NULL, NULL, '', '');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '站点名称', '6000', NULL, '1', '0', 'N', '1', '0', '', '', NULL, '', NULL, NULL, '', 'name');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '账期', '6000', NULL, '1', '1', 'N', '1', '1', '', '', NULL, '', NULL, NULL, '', 'newdate');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '总流量', '4500', NULL, '1', '2', 'N', '1', '2', '', '', NULL, '', NULL, NULL, '', 'ext_flow');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '标况流量', '4500', NULL, '1', '3', 'N', '1', '3', '', '', NULL, '', NULL, NULL, '', 'flow_bk');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '工况流量', '4500', NULL, '1', '4', 'N', '1', '4', '', '', NULL, '', NULL, NULL, '', 'flow_gk');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '温度计温度', '4500', NULL, '1', '5', 'N', '1', '5', '', '', NULL, '', NULL, NULL, '', 'llj_temp');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '温度计压力', '4500', NULL, '1', '6', 'N', '1', '6', '', '', NULL, '', NULL, NULL, '', 'llj_press');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '时用气量', '4500', NULL, '1', '7', 'N', '1', '7', '', '', NULL, '', NULL, NULL, '', 'gas_used');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '日累计用气', '4500', NULL, '1', '8', 'N', '1', '8', '', '', NULL, '', NULL, NULL, '', 'total_used');


INSERT INTO excels ( `busiType`, `name`, `state`) VALUES ( 'rpt_hour_llj', '流量计日报每小时', '1');

set @excelId =(SELECT LAST_INSERT_ID());

INSERT INTO excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) VALUES ( @excelId, 'select id,name from sites t where t.state = 1', '', 'select s.name,date_format(str_to_date(l.stat_date,\'%Y%m%d%H\'),\'%Y-%m-%d %H时\') newdate, \r\n                   l.ext_flow,l.flow_bk,l.flow_gk,l.llj_temp,l.llj_press, \r\n                   l.gas_used,l.total_used \r\n                   from rpt_hour_llj l  join sites s on s.id = l.site_id where s.state=1 and l.site_id =#id #filter order by newdate', @templateTemp, '1', '1');

