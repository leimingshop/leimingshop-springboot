/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class ImportExcelsellerUtils {

    private static Logger logger = LoggerFactory.getLogger(ImportExcelsellerUtils.class);

    private ImportExcelsellerUtils() {
    }

    /**
     * 导入Excel文件（电商 商品）
     */
    public static List<?> readNewExcelTitle(InputStream in, Object obj) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        //获得object类的属性
        Class<?> clazz = Class.forName(obj.getClass().getCanonicalName());
        Field[] field = clazz.getDeclaredFields();

        //标题总列数
        int colNum = row.getPhysicalNumberOfCells();//Excal中有多少列
        //判断对象属性和excal的属性的数目是否相同
        if (field.length != colNum) {
            return Collections.emptyList();
        }
        //创建List容器
        List<Object> list = new ArrayList<>();
        //循环,将Excal中的数据放入到对象中
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            //得到excal内的一行
            row = sheet.getRow(i + 1);
            int sum = 0;
            for (int j = 0; j < colNum; j++) {
                field[j].setAccessible(true);
                //得到Excal中的一个单元格
                HSSFCell cell = row.getCell(j);
                //System.out.println("cell=====" + cell + "====type====" + cell.getCellType());
                //判断是否和类中的属性的类型相同,如果相同那么将excal中的值赋给obj
                //PropertyDescriptor pd = new PropertyDescriptor(field[j].getName(), clazz);
                //System.out.println(field[j].getName());
                //Method method = pd.getWriteMethod();
                try {
                    //判断是否是每行的第一列值
                    if (j == 0 || j == 1) {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ||
                                cell.getRichStringCellValue().getString().length() < 5 ||
                                cell.getRichStringCellValue().getString().length() > 50) {
                            break;
                        }
                    }
                    String str = cell.getRichStringCellValue().getString();
                    field[j].set(obj, str);
                    sum++;
                } catch (Exception e) {
                    logger.error("错误信息为" + e);
                    try {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            String time = cell.getRichStringCellValue().getString();
                            field[j].set(obj, Timestamp.valueOf(time));
                            continue;
                        }
                    } catch (Exception e2) {
                        logger.error("错误信息为" + e2);
                        try {
                            if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                String decimal = cell.getRichStringCellValue().getString();
                                BigDecimal bd = new BigDecimal(decimal);
                                field[j].set(obj, bd);
                                continue;
                            }
                        } catch (Exception e3) {
                            logger.error("错误信息为" + e3);
                            field[j].set(obj, null);
                            continue;
                        }

                    }
                    field[j].set(obj, null);
                }
            }

            if (sum > 0) {
                list.add(obj);
            }

            obj = obj.getClass().newInstance();
        }
        return list;
    }


    /**
     * 读取Excel表格表头的内容
     *
     * @param in
     * @return String 表头内容的数组
     * @throws Exception
     */
    public static List<?> readExcelTitle(InputStream in, Object obj) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        //获得object类的属性
        Class<?> clazz = Class.forName(obj.getClass().getCanonicalName());
        Field[] field = clazz.getDeclaredFields();

        //标题总列数
        int colNum = row.getPhysicalNumberOfCells();//Excal中有多少列
        //判断对象属性和excal的属性的数目是否相同
        if (field.length != colNum) {
            return Collections.emptyList();
        }
        //创建List容器
        List<Object> list = new ArrayList<>();
        //循环,将Excal中的数据放入到对象中
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            //得到excal内的一行
            row = sheet.getRow(i + 1);
            if (row == null || isContentBlank(row)) {
                continue;
            }
            for (int j = 0; j < colNum; j++) {
                field[j].setAccessible(true);
                //得到Excal中的一个单元格
                HSSFCell cell = row.getCell(j);
                //System.out.println("cell=====" + cell + "====type====" + cell.getCellType());
                //判断是否和类中的属性的类型相同,如果相同那么将excal中的值赋给obj
                //PropertyDescriptor pd = new PropertyDescriptor(field[j].getName(), clazz);
                //System.out.println(field[j].getName());
                //Method method = pd.getWriteMethod();
                try {
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC://如果是数字
                            // 处理日期格式、时间格式
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = null;
                                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else {// 日期
                                    sdf = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN);
                                }
                                Date date = cell.getDateCellValue();
                                System.out.println(sdf.format(date) + "==========111==" + cell);
                                field[j].set(obj, DateUtils.strToLong(sdf.format(date)));
                                break;
                            } else if (cell.getCellStyle().getDataFormat() == 58) {
                                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                new SimpleDateFormat("yyyy-MM-dd");
                                double value = cell.getNumericCellValue();
                                org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                            } else {
                                cell.getNumericCellValue();
                                CellStyle style = cell.getCellStyle();
                                DecimalFormat format = new DecimalFormat();
                                String temp = style.getDataFormatString();
                                // 单元格设置成常规
                                if ("General".equals(temp)) {
                                    format.applyPattern("#");
                                }
                            }
                            Double d = cell.getNumericCellValue();
                            //商品和规格导入
                            if (j == 1 || j == 2 || j == 5 || j == 6 || j == 7) {
                                if (d > d.intValue()) {
                                    field[j].set(obj, d.toString());
                                } else {
                                    Integer s = d.intValue();
                                    field[j].set(obj, s.toString());
                                }
                            } else {
                                if (d > d.intValue()) {
                                    field[j].set(obj, d);
                                } else {
                                    field[j].set(obj, d.intValue());
                                }
                            }
                            break;

                        case HSSFCell.CELL_TYPE_STRING://如果是字符串
                            String str = cell.getRichStringCellValue().getString();
                            field[j].set(obj, str);
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA://如果是日期
                            String time = cell.getCellFormula();
                            System.out.print("time:-------------" + time);
                            log.debug(time);
                            field[j].set(obj, Timestamp.valueOf(time));
                    }
                } catch (Exception e) {
                    logger.error("错误信息为" + e);
                    try {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            String time = cell.getRichStringCellValue().getString();
                            field[j].set(obj, Timestamp.valueOf(time));
                            continue;
                        }
                    } catch (Exception e2) {
                        logger.error("错误信息为" + e2);
                        try {
                            if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                String decimal = cell.getRichStringCellValue().getString();
                                BigDecimal bd = new BigDecimal(decimal);
                                field[j].set(obj, bd);
                                continue;
                            }
                        } catch (Exception e3) {
                            logger.error("错误信息为" + e3);
                            field[j].set(obj, null);
                            continue;
                        }

                    }
                    field[j].set(obj, null);
                }
            }
            list.add(obj);
            obj = obj.getClass().newInstance();
        }
        return list;
    }

    /**
     * 读取Excel表格表头的内容
     * seller:评价管理:导入评价（单独使用）
     *
     * @param in
     * @return String 表头内容的数组
     * @throws Exception
     */
    public static List<?> readExcelTitles(InputStream in, Object obj) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(in);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        //获得object类的属性
        Class<?> clazz = Class.forName(obj.getClass().getCanonicalName());
        Field[] field = clazz.getDeclaredFields();

        //标题总列数
        int colNum = row.getPhysicalNumberOfCells();//Excal中有多少列
        //创建List容器
        List<Object> list = new ArrayList<>();
        //循环,将Excal中的数据放入到对象中
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            //得到excal内的一行
            row = sheet.getRow(i + 1);
            for (int j = 0; j < colNum; j++) {
                field[j].setAccessible(true);
                //得到Excal中的一个单元格
                HSSFCell cell = row.getCell(j);
                //System.out.println("cell=====" + cell + "====type====" + cell.getCellType());
                //判断是否和类中的属性的类型相同,如果相同那么将excal中的值赋给obj
                //PropertyDescriptor pd = new PropertyDescriptor(field[j].getName(), clazz);
                //System.out.println(field[j].getName());
                //Method method = pd.getWriteMethod();
                try {
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC://如果是数字
                            // 处理日期格式、时间格式
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = null;
                                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else {// 日期
                                    sdf = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN);
                                }
                                Date date = cell.getDateCellValue();
                                field[j].set(obj, DateUtils.strToLong(sdf.format(date)));
                                break;
                            } else if (cell.getCellStyle().getDataFormat() == 58) {
                                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                double value = cell.getNumericCellValue();
                                org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                            } else {
                                cell.getNumericCellValue();
                                CellStyle style = cell.getCellStyle();
                                DecimalFormat format = new DecimalFormat();
                                String temp = style.getDataFormatString();
                                // 单元格设置成常规
                                if ("General".equals(temp)) {
                                    format.applyPattern("#");
                                }
                            }
                            Double d = cell.getNumericCellValue();
                            //商品和规格导入
                            if (j == 1 || j == 5 || j == 6 || j == 7) {
                                if (d > d.intValue()) {
                                    field[j].set(obj, d.toString());
                                } else {
                                    String s = String.valueOf(d.intValue());
                                    field[j].set(obj, s);
                                }
                            } else {
                                if (d > d.intValue()) {
                                    field[j].set(obj, String.valueOf(d));
                                } else {
                                    field[j].set(obj, String.valueOf(d.intValue()));
                                }
                            }
                            break;

                        case HSSFCell.CELL_TYPE_STRING://如果是字符串
                            String str = String.valueOf(cell.getRichStringCellValue());
                            field[j].set(obj, str);
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA://如果是日期
                            String time = cell.getCellFormula();
                            field[j].set(obj, Timestamp.valueOf(time));
                    }
                } catch (Exception e) {
                    logger.error("错误信息为" + e);
                    try {
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            String time = String.valueOf(cell.getRichStringCellValue());
                            field[j].set(obj, Timestamp.valueOf(time));
                            continue;
                        }
                    } catch (Exception e2) {
                        logger.error("错误信息为" + e2);
                        try {
                            if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                                String decimal = String.valueOf(cell.getRichStringCellValue());
                                BigDecimal bd = new BigDecimal(decimal);
                                field[j].set(obj, bd);
                                continue;
                            }
                        } catch (Exception e3) {
                            logger.error("错误信息为" + e3);
                            field[j].set(obj, null);
                            continue;
                        }

                    }
                    field[j].set(obj, null);
                }
            }
            list.add(obj);
            obj = obj.getClass().newInstance();
        }
        return list;
    }


    /*
     * @desc 检查单元格内容是否为空
     * @param hssfRow
     * @author weibo
     * @date 2018.08.02
     * @return boolean
     **/
    public static boolean isContentBlank(HSSFRow hssfRow) {
        boolean resultBool = true;
        String cellContent;
        for (int i = 0; i < 3; i++) {
            if (hssfRow != null && hssfRow.getCell(i) != null) {
                HSSFCell cell = hssfRow.getCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellContent = cell.getStringCellValue();
                if (StringUtils.isNotBlank(cellContent)) {
                    resultBool = false;
                    break;
                }
            }
        }
        return resultBool;
    }

}
