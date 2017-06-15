

set @excelId =(SELECT id from excels where busiType = 'operation');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'operation';

set @excelId =(SELECT id from excels where busiType = 'operation_sum');
set @templateTemp =(SELECT template from excel_parts where excel_id = @excelId);
DELETE FROM excel_cells WHERE template = @templateTemp;
DELETE FROM excel_parts where excel_id = @excelId;
DELETE from excels WHERE busiType = 'operation_sum';



INSERT INTO excels ( `busiType`, `name`, `state`) VALUES ( 'operation', '运维记录导出表', '1');

set @excelId =(SELECT LAST_INSERT_ID());
set @templateTemp =((SELECT max(template) from ngms.excel_parts) +1);

INSERT INTO excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) VALUES ( @excelId, 'select DISTINCT s.id id,s.name name from sites s left join tank_operation t on t.siteid = s.id where t.state = 1 ', NULL, 'select t.id,t.siteid,t.faulttype,t.subtype,t.subtypename,t.desc,t.procedure,t.changedev,t.createtime,t.creator,t.company,t.state,\r\n(select group_concat(name) from dict where group_code = \'OPERATION.DEVICE\' and find_in_set(code, t.changedev)) changedevname, \r\nt.occurdtime ,t.solvetime, \r\n(case t.faulttype when \'1\' then \'离线\' else \'告警\' end) faulttypename,s.name from tank_operation t \r\nleft join sites s on t.siteid = s.id \r\nwhere t.state = 1 and t.siteid=#id #filter\r\n order by t.occurdtime asc ', @templateTemp, '1', '1');


INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '运维记录', NULL, NULL, '0', '0', 'Y', '0', '7', '', '', NULL, '', NULL, NULL, '', '');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '站点名称', '6000', NULL, '1', '0', 'N', '1', '0', '', '', NULL, '', NULL, NULL, '', 'name');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '发生时间', '6000', NULL, '1', '1', 'N', '1', '1', '', '', NULL, '', NULL, NULL, '', 'occurdtime');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '解决时间', '6000', NULL, '1', '2', 'N', '1', '2', '', '', NULL, '', NULL, NULL, '', 'solvetime');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '故障类型', '4500', NULL, '1', '3', 'N', '1', '3', '', '', NULL, '', NULL, NULL, '', 'changedevname');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '故障小类', '4500', NULL, '1', '4', 'N', '1', '4', '', '', NULL, '', NULL, NULL, '', 'subtype');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '更换设备', '4500', NULL, '1', '5', 'N', '1', '5', '', '', NULL, '', NULL, NULL, '', 'changedevname');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '设备描述', '6500', NULL, '1', '6', 'N', '1', '6', '', '', NULL, '', NULL, NULL, '', 'desc');
INSERT INTO excel_cells ( `template`, `cellname`, `width`, `height`, `startRow`, `startColumn`, `isMerge`, `endRow`, `endColumn`, `border`, `autoWrap`, `bgColor`, `fontName`, `fontColor`, `fontSize`, `fontBold`, `property`) VALUES ( @templateTemp, '处理过程', '8000', NULL, '1', '7', 'N', '1', '7', '', '', NULL, '', NULL, NULL, '', 'procedure');



	
INSERT INTO excels ( `busiType`, `name`, `state`) VALUES ( 'operation_sum', '运维记录汇总导出表', '1');

set @excelId =(SELECT LAST_INSERT_ID());

INSERT INTO excel_parts ( `excel_id`, `sheet_sql`, `head_sql`, `data_sql`, `template`, `sort`, `state`) VALUES ( @excelId, 'select 0 id,\'运维记录汇总导出表\' name', NULL, 'select t.id,t.siteid,t.faulttype,t.subtype,t.subtypename,t.desc,t.procedure,t.changedev,t.createtime,t.creator,t.company,t.state,\r\n(select group_concat(name) from dict where group_code = \'OPERATION.DEVICE\' and find_in_set(code, t.changedev)) changedevname, \r\nt.occurdtime ,t.solvetime, \r\n(case t.faulttype when \'1\' then \'离线\' else \'告警\' end) faulttypename,s.name from tank_operation t \r\nleft join sites s on t.siteid = s.id \r\nwhere t.state = 1  #filter\r\norder by t.occurdtime asc ', @templateTemp, '1', '1');

