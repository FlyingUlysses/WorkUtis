set @excelId =(SELECT id from excels where busiType = 'rpt_month_tank');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'rpt_month_tank';


INSERT INTO ngms.excels ( `busiType`, `name`, `state`) VALUES ( 'rpt_month_tank', '月报表', '1');
set @excelId =(SELECT LAST_INSERT_ID());
set @templateTemp =((SELECT max(template) from ngms.excel_parts) +1);

INSERT INTO ngms.excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) VALUES ( @excelId, 'select id,name from sites t where t.state = 1', NULL, 'SELECT (SELECT name FROM base_companys where id =t.company) companyName,s.name siteName,date_format(str_to_date(t.cycle,\'%Y%m\'),\'%Y-%m\') cycle,t.sumfilling,t.sumflow,t.cardflow,t.gasrate,t.onlinerate FROM rpt_month_tank t LEFT JOIN sites s on t.site_id=s.id where t.site_id=#id #filter ORDER BY t.cycle asc', @templateTemp, '1', '1');


INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '月报', NULL, NULL, '0', '0', 'Y', '0', '7', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '公司名称', '5500', NULL, '1', '0', 'N', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'companyname');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '站点名称', '4500', NULL, '1', '1', 'N', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'sitename');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '周期', '4500', NULL, '1', '2', 'N', '1', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cycle');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '累计充装量（kg）', '4500', NULL, '1', '3', 'N', '1', '3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'sumfilling');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '流量计累计流量(m³）', '4500', NULL, '1', '4', 'N', '1', '4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'sumflow');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '卡控表总用量（m³）', '4500', NULL, '1', '5', 'N', '1', '5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'cardflow');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '气化率 m³|kg', '4500', NULL, '1', '6', 'N', '1', '6', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'gasrate');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '	在线率n%', '4500', NULL, '1', '7', 'N', '1', '7', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'onlinerate');
