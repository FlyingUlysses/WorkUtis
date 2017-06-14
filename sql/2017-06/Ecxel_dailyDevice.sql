set @excelId =(SELECT id from excels where busiType = 'rpt_daily_devcie');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'rpt_daily_devcie';




INSERT INTO excels ( `busiType`, `name`, `state`) VALUES ( 'rpt_daily_devcie', '在线率日报', '1');

set @excelId =(SELECT LAST_INSERT_ID());
set @templateTemp =((SELECT max(template) from ngms.excel_parts) +1);


INSERT INTO excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) VALUES (@excelId, 'select distinct s.id id,s.name name  from sites s  join rpt_daily_device t on t.site_id =s.id', NULL, 'select t.site_id, s.name site_name, t.device_id, d.name device_name, t.alarm_cnt,\r\n			t.off_cnt, t.rate,t.stat_date,\r\n			CONCAT(convert(TRUNCATE(t.off_duration/86400, 0), char), \'天\',convert(TRUNCATE((t.off_duration%86400)/3600, 0), char), \'小时\',convert(TRUNCATE(((t.off_duration%86400)%3600)/60, 0), char), \'分\',convert(TRUNCATE((t.off_duration%3600)%60, 0), char),\'秒\') off_duration \r\n			from rpt_daily_device t left join sites s on t.site_id = s.id \r\n			left join site_device sd on t.device_id = sd.id\r\n			left join devices d on sd.deviceId = d.id where s.state = 1 and sd.state = 1 and t.site_id=#id #filter  order by t.stat_date asc ', @templateTemp, '1', '1');


INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '在线率日报', NULL, NULL, '0', '0', 'Y', '0', '6', '', '', NULL, '', NULL, NULL, '', '');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '账期', '6000', NULL, '1', '0', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'stat_date');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '站点名称', '6000', NULL, '1', '1', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'site_name');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '设备名称', '6000', NULL, '1', '2', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'device_name');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '离线次数', '3000', NULL, '1', '3', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'off_cnt');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '离线时长', '6000', NULL, '1', '4', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'off_duration');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '在线率', '6000', NULL, '1', '5', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'rate');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '警告次数', '6000', NULL, '1', '6', 'N', NULL, NULL, '', '', NULL, '', NULL, NULL, '', 'alarm_cnt');
