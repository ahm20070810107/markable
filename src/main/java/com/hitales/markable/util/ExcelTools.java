package com.hitales.markable.util;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


public class ExcelTools {

    public static  Workbook loadExcelFile(String excelPath){

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

    public static Object getCellValue(Cell cell){
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
