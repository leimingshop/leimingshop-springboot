/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class EasyExcelUtils {

    /**
     * 功能描述：
     * 导出excel到指定目录
     *
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/10
     **/

    public static <T> List<String> excelFileUpload(String localPath, String fileName, List<?> dataList, Class<?> clazz, Integer limit) {
        List<String> filesList = new ArrayList<>();
        int number = 0;
        try {
            // 判断传入的list数据是否为空
            if (CollectionUtils.isEmpty(dataList)) {
                // 如果是空则不需要导出直接返回
                return filesList;
            }
            // 下面的情况都是传入的list不为空时
            if (null == limit || limit == 0 || limit > 50000) {
                // 默认50000每个excel
                limit = 50000;
            }
            Integer size = dataList.size();
            //判断是否有必要分批
            if (size > limit) {
                //获取分批数是多少
                int part = size / limit;
                //是不是整除
                int surplus = size % limit;
                // 如果不是整除则循环数加一
                if (surplus > 0) {
                    part = part + 1;
                }
                log.info("导出excel共有:{}条，！ 分为 ：{}批", size, part);
                for (int i = 1; i <= part; i++) {
                    number++;
                    String filePath = getFilePath(fileName, number, localPath);
                    filesList.add(filePath);

                    // 清除数据
                    if (!CollectionUtils.isEmpty(dataList) && dataList.size() > limit) {
                        EasyExcel.write(filePath, clazz).sheet().doWrite(dataList.subList(0, limit));
                        dataList.subList(0, limit).clear();
                    } else {
                        EasyExcel.write(filePath, clazz).sheet().doWrite(dataList);
                    }

                }
            } else {
                //不需要分批
                String filePath = getFilePath(fileName, number, localPath);
                filesList.add(filePath);
                EasyExcel.write(filePath, clazz).sheet().doWrite(dataList);
            }
        } catch (Exception e) {
            log.error("写excel异常", e);
            if (filesList != null && filesList.size() > 0) {
                for (String li : filesList) {
                    File file = new File(li);
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
        return filesList;
    }

    /**
     * 功能描述：
     * 获取导出文件地址
     * 同时判断到处路径是否存在如果不存在则创建
     *
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/10
     **/

    private static String getFilePath(String fileName, int number, String localPath) {
        if (StringUtils.isBlank(fileName)) {
            fileName = number + "_" + System.currentTimeMillis() + "";
        } else {
            fileName = fileName + "_" + number + "_" + System.currentTimeMillis();
        }
        String filePath = localPath + File.separator + fileName + ".xls";
        //判断目录是否存在，不存在则创建
        File targetFile = new File(localPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        return filePath;

    }

    /**
     * 文件压缩
     *
     * @param localPath
     * @param filesList
     * @param filename
     * @return
     */
    public static File zipFiles(String localPath, List<String> filesList, String filename) {
        if (StringUtils.isBlank(filename)) {
            filename = File.separator + System.currentTimeMillis() + ".zip";
        } else {
            filename = File.separator + filename + ".zip";
        }
        File zipFile = new File(localPath + filename);
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                log.error("创建文件异常", e);
            }
        }
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        FileInputStream fileInputStream = null;
        File file = null;
        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;

            for (int i = 0; i < filesList.size(); i++) {
                file = new File(filesList.get(i));
                try {
                    fileInputStream = new FileInputStream(file);
                    // 实例化 ZipEntry 对象，源文件数组中的当前文件
                    zipEntry = new ZipEntry(file.getName());
                    zipOutputStream.putNextEntry(zipEntry);
                    // 该变量记录每次真正读的字节个数
                    int length;
                    // 定义每次读取的字节数组
                    byte[] buffer = new byte[1024];
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }

                } catch (IOException e) {
                    log.error("写zip文件异常", e);
                } finally {
                    fileInputStream.close();
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        } catch (IOException e) {
            log.error("写zip文件异常", e);
            zipFile = null;
        } finally {
            try {
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("关闭流异常", e);
                zipFile = null;
            }
        }
        return zipFile;
    }

    /**
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     *
     * @param in    excel输入流
     * @param clazz
     * @return
     */
    public static <T> List<T> readByModel(InputStream in, Class<T> clazz) {
        try {

            //自动读取第一个sheet
            List<Object> list = EasyExcel.read(in).head(clazz).sheet().doReadSync();
            return (List<T>) list;

        } catch (Exception e) {
            log.error("同步读取excel异常", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得easyexcel 的监听器
     *
     * @param consumer
     * @param threshold
     * @param <T>
     * @return
     */
    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer, int threshold) {
        return new AnalysisEventListener<T>() {
            private LinkedList<T> linkedList = new LinkedList<T>();

            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                linkedList.add(t);
                if (linkedList.size() == threshold) {
                    consumer.accept(linkedList);
                    linkedList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                if (linkedList.size() > 0) {
                    consumer.accept(linkedList);
                }
            }
        };
    }

    /**
     * 不指定阈值，阈值默认为10
     *
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer) {
        return getListener(consumer, 10);
    }

    public static void exportExcelToTarget(HttpServletResponse response, String fileName, Collection<?> sourceList, Class<?> targetclass) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            if (StringUtils.isEmpty(fileName)) {
                fileName = fileName = DateUtils.format(new Date());
            }
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<Object> targetList = new ArrayList<>(sourceList.size());
            for (Object source : sourceList) {
                Object target = targetclass.newInstance();
                BeanUtils.copyProperties(source, target);
                targetList.add(target);
            }
            EasyExcel.write(response.getOutputStream(), targetclass).sheet().doWrite(targetList);
        } catch (Exception e) {
            log.error("导出异常" + e);
        }
    }

    public static ByteArrayOutputStream excelFileByteUpload(ByteArrayOutputStream outputStream, List<?> list, Class<?> clazz, int limit) {
        EasyExcel.write(outputStream, clazz).sheet(limit).doWrite(list);
        return outputStream;
    }
}
