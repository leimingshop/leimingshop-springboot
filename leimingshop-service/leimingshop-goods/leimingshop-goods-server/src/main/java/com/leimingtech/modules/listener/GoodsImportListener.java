/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.leimingtech.modules.dto.freight.template.FreightTemplateDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO;
import com.leimingtech.modules.entity.goods.GoodsEntity;
import com.leimingtech.modules.enums.goodsclass.GoodsClassErrorCodeEnum;
import com.leimingtech.modules.excel.goods.GoodsTemplateExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lishuo
 * @Date: 15/7/2020 16:34
 * @email: lishuo@leimingtech.com
 * @Description:商品导入的Listener
 */
@Slf4j
public class GoodsImportListener extends AnalysisEventListener<GoodsTemplateExcel> {

    private List<GoodsTemplateExcel> list = new ArrayList<>();
    private List<GoodsEntity> listGoodsEntity = new ArrayList<>();
    private List<FreightTemplateDTO> freightTemplateDTOList;
    private Map<String, List<GoodsClassListDTO>> firstGoodsClassMap;
    private Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap;
    private Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap;
    private List<Map<String, String>> errorMessage = Lists.newArrayList();

    public GoodsImportListener(Map<String, List<GoodsClassListDTO>> firstGoodsClassMap, Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap, Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap, List<FreightTemplateDTO> freightTemplateDTOList) {
        this.firstGoodsClassMap = firstGoodsClassMap;
        this.secondGoodsClassMap = secondGoodsClassMap;
        this.thirdGoodsClassMap = thirdGoodsClassMap;
        this.freightTemplateDTOList = freightTemplateDTOList;
    }


    @Override
    public void invoke(GoodsTemplateExcel goodsTemplateExcel, AnalysisContext analysisContext) {
        // 对数据进行校验
        GoodsEntity goodsEntity = checkGoodsEntity(goodsTemplateExcel);
        if (goodsEntity != null) {
            listGoodsEntity.add(goodsEntity);
        }
        list.add(goodsTemplateExcel);
    }


    /**
     * 功能描述 校验数据 主要是goodsentity的校验
     *
     * @param goodsTemplateExcel 读取到的数据
     * @return void
     * @author lishuo
     * @date 15/7/2020
     */
    private GoodsEntity checkGoodsEntity(GoodsTemplateExcel goodsTemplateExcel) {

        GoodsEntity goodsEntity = new GoodsEntity();

        // 商品分类判断
        String goodsGcName = goodsTemplateExcel.getGoodsGcName();
        String[] goodsGcNames = StringUtils.split(goodsGcName, ";");
        if (goodsGcNames.length != GoodsClassErrorCodeEnum.GOODS_CLASS_THREE.value()) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "商品分类的个数不等于3");
            errorMessage.add(map);
            return null;
        }
        List<GoodsClassListDTO> goodsClassListDTOList = firstGoodsClassMap.get(goodsGcNames[0]);
        if (CollectionUtils.isEmpty(goodsClassListDTOList)) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "不存在商品一级分类:" + goodsGcNames[0]);
            errorMessage.add(map);
            return null;
        }
        Long firstGoodsClassId = goodsClassListDTOList.get(0).getId();
        List<GoodsClassListDTO> secondGoodsClassListDTOList = secondGoodsClassMap.get(firstGoodsClassId);
        if (CollectionUtils.isEmpty(secondGoodsClassListDTOList)) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "不存在商品二级分类:" + goodsGcNames[1]);
            errorMessage.add(map);
            return null;
        }
        List<GoodsClassListDTO> secondGoodsClassListDTO = secondGoodsClassListDTOList.stream().filter(goodsClassListDTO -> goodsClassListDTO.getGcName().equals(goodsGcNames[1])).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(secondGoodsClassListDTO)) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "不存在商品二级分类:" + goodsGcNames[1]);
            errorMessage.add(map);
            return null;
        }
        Long secondGoodsClassId = secondGoodsClassListDTO.get(0).getId();
        List<GoodsClassListDTO> thirdGoodsClassListDTOList = thirdGoodsClassMap.get(secondGoodsClassId);
        if (CollectionUtils.isEmpty(thirdGoodsClassListDTOList)) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "不存在商品三级分类:" + goodsGcNames[2]);
            errorMessage.add(map);
            return null;
        }
        List<GoodsClassListDTO> collect = thirdGoodsClassListDTOList.stream().filter(goodsClassListDTO -> goodsClassListDTO.getGcName().equals(goodsGcNames[2])).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "不存在商品三级分类:" + goodsGcNames[2]);
            errorMessage.add(map);
            return null;
        }
        // 运费模板的判断
        Integer expressFlag = goodsTemplateExcel.getExpressFlag();
        if (expressFlag != null && 1 == expressFlag && goodsTemplateExcel.getFreightBearType() == 1) {
            String freightTemplateName = goodsTemplateExcel.getFreightTemplate();
            List<FreightTemplateDTO> collect1 = freightTemplateDTOList.stream().filter(freightTemplateDTO -> freightTemplateDTO.getTemplateName().equals(freightTemplateName)).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(collect1)) {
                HashMap<String, String> map = new HashMap<>(10);
                map.put(goodsTemplateExcel.getGoodsName(), "运费模板不能为空");
                errorMessage.add(map);
                return null;
            }
        }
        // sku的主规格数量和图片的判断
        if (goodsTemplateExcel.getSkuPictureUrl() != null && goodsTemplateExcel.getGoodsSepcNameValue() != null) {
            String skuPictureUrl = goodsTemplateExcel.getSkuPictureUrl();
            String[] split = skuPictureUrl.split(";");
            String goodsSpec = goodsTemplateExcel.getGoodsSepcNameValue();
            String goodsSpecRel = goodsSpec.replace("{", "").replace("}", "").replace("\"", "");
            String[] specNameAndValues = goodsSpecRel.split(";");
            // sku主规格集合
            List<String> specValue1 = new ArrayList<>();
            for (int i = 0; i < specNameAndValues.length; i++) {
                String[] oneSpecNameAndValue = specNameAndValues[i].split(",");
                for (int k = 0; k < oneSpecNameAndValue.length; k++) {
                    String[] attrAndValue = oneSpecNameAndValue[k].split(":");
                    if (k == 0) {
                        if (!specValue1.contains(attrAndValue[1])) {
                            specValue1.add(attrAndValue[1]);
                        }
                    }
                }
            }
            if (specValue1.size() != split.length) {
                HashMap<String, String> map = new HashMap<>(10);
                map.put(goodsTemplateExcel.getGoodsName(), "商品规格与图片数量不符合");
                errorMessage.add(map);
            }
        }
        if (goodsTemplateExcel.getSkuPictureUrl() == null && goodsTemplateExcel.getGoodsSepcNameValue() != null) {
            HashMap<String, String> map = new HashMap<>(10);
            map.put(goodsTemplateExcel.getGoodsName(), "商品规格与图片数量不符合");
            errorMessage.add(map);
        }
        return goodsEntity;

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<GoodsTemplateExcel> getList() {
        return list;
    }

    public List<Map<String, String>> getErrorMessage() {
        return errorMessage;
    }

    public List<GoodsEntity> getListGoodsEntity() {
        return listGoodsEntity;
    }


}
