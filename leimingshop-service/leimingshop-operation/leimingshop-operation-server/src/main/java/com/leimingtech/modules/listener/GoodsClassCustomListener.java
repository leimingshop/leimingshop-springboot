/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO;
import com.leimingtech.modules.excel.GoodsClassCustomTemplateExcel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lishuo
 * @Date: 20/7/2020 10:20
 * @email: lishuo@leimingtech.com
 * @Description: goodsClassCustom导入的文本监听器
 */
public class GoodsClassCustomListener extends AnalysisEventListener<GoodsClassCustomTemplateExcel> {
    List<GoodsClassCustomTemplateExcel> list = new ArrayList<>();
    Map<String, List<GoodsClassListDTO>> firstGoodsClassMap;
    Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap;
    Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap;
    List<String> errorMessage = new ArrayList<>();
    private Integer count = 0;

    public GoodsClassCustomListener(Map<String, List<GoodsClassListDTO>> firstGoodsClassMap, Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap, Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap) {
        this.firstGoodsClassMap = firstGoodsClassMap;
        this.secondGoodsClassMap = secondGoodsClassMap;
        this.thirdGoodsClassMap = thirdGoodsClassMap;
    }

    @Override
    public void invoke(GoodsClassCustomTemplateExcel goodsClassCustomTemplateExcel, AnalysisContext analysisContext) {
        // 校验数据
        checkGoodsClass(count, goodsClassCustomTemplateExcel, firstGoodsClassMap, secondGoodsClassMap, thirdGoodsClassMap);
        list.add(goodsClassCustomTemplateExcel);
    }

    /**
     * 功能描述 校验数据 关联的后台分类
     *
     * @param *                   @param goodsClassCustomTemplateExcel
     * @param count
     * @param firstGoodsClassMap
     * @param secondGoodsClassMap
     * @param thirdGoodsClassMap
     * @return void
     * @author lishuo
     * @date 20/7/2020
     */
    private void checkGoodsClass(Integer count, GoodsClassCustomTemplateExcel goodsClassCustomTemplateExcel, Map<String, List<GoodsClassListDTO>> firstGoodsClassMap, Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap, Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap) {
        count++;
        String firstGcName = goodsClassCustomTemplateExcel.getFirstGcName();
        String secondGcName = goodsClassCustomTemplateExcel.getSecondGcName();
        String thirdGcName = goodsClassCustomTemplateExcel.getThirdGcName();
        if (StringUtils.isEmpty(firstGcName) || StringUtils.isEmpty(secondGcName) || StringUtils.isEmpty(thirdGcName)) {
            errorMessage.add("第" + (count - 1) + "行,关联后台分类个数部位3");
            return;
        }
        List<GoodsClassListDTO> goodsClassListDTOList = firstGoodsClassMap.get(firstGcName);
        if (CollectionUtils.isEmpty(goodsClassListDTOList)) {
            errorMessage.add("第" + (count - 1) + "行，关联后台一级分类不存在");
            return;
        }
        Long firstGoodsClassId = goodsClassListDTOList.get(0).getId();
        List<GoodsClassListDTO> goodsClassList = secondGoodsClassMap.get(firstGoodsClassId);
        List<GoodsClassListDTO> collect = goodsClassList.stream().filter(goodsClassListDTO -> goodsClassListDTO.getGcName().equals(secondGcName)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            errorMessage.add("第" + (count - 1) + "行，关联后台二级分类不存在");
            return;
        }
        Long secondGoodsClassId = collect.get(0).getId();
        List<GoodsClassListDTO> thirdGoodsClass = thirdGoodsClassMap.get(secondGoodsClassId);
        List<GoodsClassListDTO> collect1 = thirdGoodsClass.stream().filter(goodsClassListDTO -> goodsClassListDTO.getGcName().equals(thirdGcName)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect1)) {
            errorMessage.add("第" + (count - 1) + "行，关联后台三级分类不存在");
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<GoodsClassCustomTemplateExcel> getList() {
        return this.list;
    }

    public List<String> getErrorMessage() {
        return this.errorMessage;
    }
}