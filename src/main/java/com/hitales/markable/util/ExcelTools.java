package com.hitales.markable.util;

import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


@Service
@Slf4j
public class ExcelTools {

    public  Workbook loadExcelFile(String excelPath){

        File excelFile = new File(excelPath);
        if(!excelFile.exists() || excelFile.isDirectory()){
         //   log.info("文件 "+excelPath+" 不存在!");
            return null;
        }else if(!excelPath.endsWith(".xlsx") && !excelPath.endsWith(".xls")){
        //    Log.i("文件 "+excelPath+" 不是excel文件!");
            return null;
        }

        Workbook workbook;
        try{
          //  workbook = WorkbookFactory.create(excelFile);
			workbook = StreamingReader.builder().rowCacheSize(1000).open(excelFile);
        }catch(Exception e){
            throw new RuntimeException("读取excel错误:"+excelPath);
        }
        return workbook;
    }


    public void readExcel(String path) {
        Workbook wb = loadExcelFile(path);
        Sheet sheet = wb.getSheetAt(0); //获取第1个sheet
        Iterator<Row> rows = sheet.rowIterator();
        Row row = rows.next();
        //	int startCloumn = row.getFirstCellNum() + 1;//第一列不使用
        int endCloumn = row.getLastCellNum();
        while (rows.hasNext()) {
            row = rows.next();
            try {
                String strJYFlag = getCellValue(row.getCell(2)).toString();
                String strBZAnchor =getCellValue(row.getCell(1)).toString();
                if (strJYFlag.equals("不禁用"))
                    continue;
                if(strBZAnchor.equals(""))
                {
                   // array.add(strBZAnchor);
                }
            }catch (Exception e){continue;}

            for (int j = 4; j <= endCloumn; j++) {
                Cell cell = row.getCell(j);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) continue;
                String str = getCellValue(cell).toString();
              //  array.add(str);
            }
        }
    }

    public  Object getCellValue(Cell cell){
        if(cell == null)return "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断是日期类型
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());// 获取成DATE类型
                    return dateformat.format(dt);
                }
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                try { return cell.getStringCellValue(); } catch (Exception e) {
                }
                break;
        }

        return "";
    }
}
