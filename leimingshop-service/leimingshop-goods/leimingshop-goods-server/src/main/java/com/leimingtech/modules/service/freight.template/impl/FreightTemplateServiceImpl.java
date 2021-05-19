/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.freight.template.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.freight.template.FreightTemplateDao;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplateDetailDTO;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplatePageDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateRuleDTO;
import com.leimingtech.modules.entity.freight.template.FreightTemplateEntity;
import com.leimingtech.modules.enums.freight.template.FreightTemplateEnum;
import com.leimingtech.modules.service.freight.template.FreightTemplateRuleService;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运费模板管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class FreightTemplateServiceImpl extends BaseServiceImpl<FreightTemplateDao, FreightTemplateEntity> implements FreightTemplateService {

    @Autowired

    private FreightTemplateRuleService freightTemplateRuleService;

    @Autowired

    private GoodsService goodsService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override

    public PageData<FreightTemplateDTO> page(@RequestParam Map<String, Object> params) {
        IPage<FreightTemplateEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, FreightTemplateDTO.class);
    }

    /**
     * 功能描述：
     * <运营端运费模板分页>
     *
     * @param params 查询条件
     * @return
     * @date 2020/4/21 15:46
     * @author 刘远杰
     **/
    @Override

    public PageData<AdminFreightTemplatePageDTO> adminPage(@RequestParam Map<String, Object> params) {
        IPage<FreightTemplateEntity> page = getPage(params, null, false);
        //查询
        List<AdminFreightTemplatePageDTO> list = baseDao.adminPage(page, params);
        int total = baseDao.countPageTotal(params);

        return new PageData<>(list, total);
    }

    @Override

    public List<FreightTemplateDTO> list(@RequestParam Map<String, Object> params) {
        List<FreightTemplateEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, FreightTemplateDTO.class);
    }

    /**
     * 功能描述：
     * <>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/4/23 11:54
     * @author 刘远杰
     **/
    @Override

    public List<AdminFreightTemplatePageDTO> storeList(@RequestParam("storeId") Long storeId) {
        // 店铺运费模板列表
        List<AdminFreightTemplatePageDTO> dtoList = baseDao.storeList(storeId);

        dtoList.forEach(dto -> {
            if (FreightTemplateEnum.TEMPLATE_TYPE_PIECE.value() == dto.getTemplateType()) {
                dto.setTemplateName("按件：" + dto.getTemplateName());
            } else if (FreightTemplateEnum.TEMPLATE_TYPE_WEIGHT.value() == dto.getTemplateType()) {
                dto.setTemplateName("按重量：" + dto.getTemplateName());
            }
        });

        return dtoList;
    }

    /**
     * 功能描述：
     * <构造查询条件>
     *
     * @param params 查询条件
     * @return
     * @date 2020/4/22 9:32
     * @author 刘远杰
     **/
    private QueryWrapper<FreightTemplateEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String templateName = (String) params.get("templateName");
        String storeId = (String) params.get("storeId");

        QueryWrapper<FreightTemplateEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(templateName), "template_name", templateName);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);

        return wrapper;
    }

    @Override

    public FreightTemplateDTO get(Long id) {
        FreightTemplateEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, FreightTemplateDTO.class);
    }

    /**
     * 功能描述：
     * <运营端运费模板详情>
     *
     * @param id      运费模板id
     * @param storeId 店铺id
     * @return
     * @date 2020/4/22 11:24
     * @author 刘远杰
     **/
    @Override

    public AdminFreightTemplateDetailDTO adminDetail(@RequestParam("id") Long id, @RequestParam("storeId") Long storeId) {
        FreightTemplateDTO freightTemplateDTO = this.getStoreFreightTemplateById(id, storeId);

        if (freightTemplateDTO != null) {
            FreightTemplateEntity entity = this.selectById(id);
            AdminFreightTemplateDetailDTO adminFreightTemplateDetail = ConvertUtils.sourceToTarget(entity, AdminFreightTemplateDetailDTO.class);

            List<FreightTemplateRuleDTO> templateRuleList = freightTemplateRuleService.getByTemplateId(id);
            adminFreightTemplateDetail.setFreightTemplateRuleList(templateRuleList);

            return adminFreightTemplateDetail;
        } else {
            return null;
        }
    }

    /**
     * 功能描述：
     * <查询店铺指定运费模板>
     *
     * @param id      运费模板id
     * @param storeId 店铺id
     * @return
     * @date 2020/4/22 10:02
     * @author 刘远杰
     **/
    @Override

    public FreightTemplateDTO getStoreFreightTemplateById(@RequestParam("id") Long id, @RequestParam("storeId") Long storeId) {
        QueryWrapper<FreightTemplateEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id).eq("store_id", storeId);

        FreightTemplateEntity entity = baseDao.selectOne(wrapper);

        return ConvertUtils.sourceToTarget(entity, FreightTemplateDTO.class);
    }

    @Override

    public void save(@RequestBody FreightTemplateDTO dto) {
        FreightTemplateEntity entity = ConvertUtils.sourceToTarget(dto, FreightTemplateEntity.class);

        insert(entity);
    }

    /**
     * 功能描述：
     * <保存运费模板>
     *
     * @param dto 保存参数
     * @return
     * @date 2020/4/21 17:52
     * @author 刘远杰
     **/
    @Override

    public Boolean saveFreightTemplate(@RequestBody FreightTemplateDTO dto) {
        List<FreightTemplateRuleDTO> freightTemplateRuleList = dto.getFreightTemplateRuleList();
        FreightTemplateEntity entity = ConvertUtils.sourceToTarget(dto, FreightTemplateEntity.class);

        // 1.此模板设置为默认运费模板，原默认运费模板修改为非默认
        if (FreightTemplateEnum.DEFAULT_FLAG_YES.value() == dto.getDefaultFlag()) {
            this.clearStoreDefaultFreightTemplate(dto.getStoreId());
        }

        // 2.保存运费模板规则
        if (CollectionUtils.isNotEmpty(freightTemplateRuleList)) {
            Boolean flag = freightTemplateRuleService.saveBatch(freightTemplateRuleList);
            if (!flag) {
                return false;
            }
        }

        // 3.保存运费模板
        return insert(entity);
    }

    @Override

    public void update(@RequestBody FreightTemplateDTO dto) {
        FreightTemplateEntity entity = ConvertUtils.sourceToTarget(dto, FreightTemplateEntity.class);

        updateById(entity);
    }

    /**
     * 功能描述：
     * <修改运费模板>
     *
     * @param dto 修改参数
     * @return
     * @date 2020/4/21 17:52
     * @author 刘远杰
     **/
    @Override

    public Boolean updateFreightTemplate(@RequestBody FreightTemplateDTO dto) {
        List<FreightTemplateRuleDTO> freightTemplateRuleList = dto.getFreightTemplateRuleList();
        FreightTemplateEntity entity = ConvertUtils.sourceToTarget(dto, FreightTemplateEntity.class);

        // 1.此模板设置为默认运费模板，原默认运费模板修改为非默认
        if (FreightTemplateEnum.DEFAULT_FLAG_YES.value() == dto.getDefaultFlag()) {
            this.clearStoreDefaultFreightTemplate(dto.getStoreId());
        }

        // 2.删除原规则、保存运费模板规则
        if (freightTemplateRuleService.noLogicDeleteByTemplateId(dto.getId())) {
            if (CollectionUtils.isNotEmpty(freightTemplateRuleList)) {
                Boolean flag = freightTemplateRuleService.saveBatch(freightTemplateRuleList);
                if (!flag) {
                    return false;
                }
            }
        } else {
            return false;
        }

        // 3.保存运费模板
        return updateById(entity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        // 逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述：
     * <删除指定id店铺运费模板>
     *
     * @param ids     运费模板id集合
     * @param storeId 店铺id
     * @return
     * @date 2020/4/22 10:26
     * @author 刘远杰
     **/
    @Override

    public void deleteStoreFreightTemplate(@RequestBody Long[] ids, @RequestParam("storeId") Long storeId) {
        UpdateWrapper<FreightTemplateEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in(ids.length > 0, "id", Arrays.asList(ids))
                .eq("store_id", storeId);

        baseDao.delete(updateWrapper);
    }

    /**
     * 功能描述：
     * <更新商品运费模板并删除>
     *
     * @param oldFreightTemplateId
     * @param newFreightTemplateId
     * @param storeId
     * @return
     * @date 2020/4/24 18:03
     * @author 刘远杰
     **/
    @Override

    public void deleteStoreFreightTemplate(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                           @RequestParam("newFreightTemplateId") Long newFreightTemplateId,
                                           @RequestParam("storeId") Long storeId) {

        // 更新关联运费模板
        goodsService.updateGoodsFreightTemplate(oldFreightTemplateId, newFreightTemplateId, storeId);

        // 删除旧运费模板
        baseDao.deleteById(oldFreightTemplateId);

        // 商品es更新
        Map<String, String> map = new HashMap<>(10);
        map.put("oldFreightTemplateId", oldFreightTemplateId.toString());
        map.put("newFreightTemplateId", newFreightTemplateId.toString());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_FREIGHT_TEMPLATE_UPDATE, JSONObject.toJSONString(map));

    }

    /**
     * 功能描述：
     * <修改默认运费模板>
     *
     * @param id          运费模板id
     * @param defaultFlag 是否默认
     * @param storeId     店铺id
     * @return
     * @date 2020/4/22 10:52
     * @author 刘远杰
     **/
    @Override

    public void updateDefaultFreightTemplate(@RequestParam("id") Long id,
                                             @RequestParam("defaultFlag") Integer defaultFlag,
                                             @RequestParam("storeId") Long storeId) {

        if (FreightTemplateEnum.DEFAULT_FLAG_YES.value() == defaultFlag) {
            // 设置为默认运费模板 - 清除原默认运费模板
            this.clearStoreDefaultFreightTemplate(storeId);
        }

        UpdateWrapper<FreightTemplateEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).eq("store_id", storeId);

        FreightTemplateEntity entity = new FreightTemplateEntity();
        entity.setDefaultFlag(defaultFlag);

        baseDao.update(entity, updateWrapper);

    }

    /**
     * 功能描述：
     * <清空店铺已设置默认运费模板>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/4/21 18:22
     * @author 刘远杰
     **/
    private void clearStoreDefaultFreightTemplate(Long storeId) {
        // 修改条件：当前店铺默认运费模板
        UpdateWrapper<FreightTemplateEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("store_id", storeId)
                .eq("default_flag", FreightTemplateEnum.DEFAULT_FLAG_YES.value());

        FreightTemplateEntity freightTemplateEntity = new FreightTemplateEntity();
        freightTemplateEntity.setDefaultFlag(FreightTemplateEnum.DEFAULT_FLAG_NO.value());

        // 清空默认运费模板
        baseDao.update(freightTemplateEntity, updateWrapper);
    }

    /**
     * 功能描述 根据模板名称和店铺id 查询模板的id
     *
     * @param freightTemplateName 模板名称
     * @param storeId             店铺id
     * @return java.lang.Long 模板id
     * @author lishuo
     * @date 24/6/2020
     */

    @Override
    public Long findFreightTemplate(@RequestParam("freightTemplateName") String freightTemplateName, @RequestParam("storeId") Long storeId) {
        return baseDao.findFreightTemplate(freightTemplateName, storeId);
    }

}
