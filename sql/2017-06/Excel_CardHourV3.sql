set @excelId =(SELECT id from excels where busiType = 'rpt_hour_card');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'rpt_hour_card';


set @excelId =(SELECT id from excels where busiType = 'rpt_day_card');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'rpt_day_card';


INSERT INTO ngms.excels ( `busiType`, `name`, `state`) VALUES ( 'rpt_hour_card', '卡控表日报每小时', '1');

set @excelId =(SELECT LAST_INSERT_ID());
set @templateTemp =((SELECT max(template) from ngms.excel_parts) +1);

INSERT INTO ngms.excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) 

VALUES ( @excelId, 'select id,name from sites t where t.state = 1', NULL, 'select date_format(str_to_date

(t.stat_date,\'%Y%m%d%H\'),\'%Y-%m-%d %H时\')  newDate,t.card_gqzl,t.card_syql,t.card_yqzl,t.gas_used,t.total_used  

from rpt_hour_card t join sites t1  ON t.site_id =t1.id where t1.id= #id #filter order by t.stat_date asc',  

@templateTemp, '1','1');

INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, '卡控表日报', NULL, NULL, '0', '0', 'Y', '0', '5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, ' 账期', '4500', NULL, '1', '0', 'N', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'newDate');
INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, '购气总量', '4500', NULL, '1', '1', 'N', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'card_gqzl');
INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, '剩余总量', '4500', NULL, '1', '2', 'N', '1', '2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'card_syql');
INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, '用气总量', '4500', NULL, '1', '3', 'N', '1', '3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'card_yqzl');
INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, '小时累计用气', '4500', NULL, '1', '4', 'N', '1', '4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'gas_used');
INSERT INTO ngms.excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES (@templateTemp, '日累计用气', '4500', NULL, '1', '5', 'N', '1', '5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'total_used');




INSERT INTO ngms.excels (`busiType`, `name`, `state`) VALUES ( 'rpt_day_card','卡控表日报每天', '1');

set @excelId=(SELECT LAST_INSERT_ID());

INSERT INTO ngms.excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) 

VALUES ( @excelId , 'select id,name from sites t where t.state = 1', NULL, "select t1.name name, date_format(str_to_date(t.stat_date,'%Y%m%d%H'),'%Y-%m-%d')newDate,t.card_gqzl,t.card_yqzl,t.card_syql,t.total_used,t.id,t.site_id from rpt_hour_card t join sites t1  ON t.site_id =t1.id where t1.state = 1  and t.stat_date LIKE '%00' and t.site_id=#id #filter order by t.stat_date asc", @templateTemp, '1', '1');

