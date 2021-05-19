/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.modules.aftersale.dao.AftersaleBarterDao;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.enmus.AfterSaleStateEnum;
import com.leimingtech.modules.aftersale.entity.AftersaleBarterEntity;
import com.leimingtech.modules.aftersale.service.*;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 订单换货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AftersaleBarterServiceImpl extends CrudServiceImpl<AftersaleBarterDao, AftersaleBarterEntity, AftersaleBarterDTO> implements AftersaleBarterService {
//    @ApiImplicitParam(name = "sellerDeliveryType", value = "商家配送方式（0:快递,1:自提,2:无需物流）", paramType = "query", dataType = "int"),

    private static final String MAP_PARAM_DELIVERY_TYPE = "sellerDeliveryType";
    private static final String MAP_PARAM_DELIVERY_MENTION = "1";
    private static final String MAP_PARAM_DELIVERY_NO = "2";


    @Resource
    private AftersaleBarterDao aftersaleBarterDao;

    @Autowired

    private AftersaleApplyService aftersaleApplyService;

    @Autowired

    private AftersaleAuditLogService aftersaleAuditLogService;

    @Autowired

    private AftersaleGoodsService aftersaleGoodsService;

    @Autowired

    private AftersaleLogService aftersaleLogService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private ArbitrationService arbitrationService;

    @Override
    public QueryWrapper<AftersaleBarterEntity> getWrapper(Map<String, Object> params) {

        QueryWrapper<AftersaleBarterEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(null != params.get("id"), "id", params.get("id"));
        wrapper.eq(null != params.get("aftersaleSn"), "aftersale_sn", params.get("aftersaleSn"));

        return wrapper;
    }

    /**
     * 保存订单换货
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody AftersaleBarterSaveDTO dto) {
        AftersaleBarterEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, AftersaleBarterEntity.class);
        aftersaleBarterDao.insert(reasonDescriptionEntity);
    }

    /**
     * 修改订单换货
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody AftersaleBarterDTO dto) {
        super.update(dto);
    }

    /**
     * 删除订单换货
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID查询订单换货
     *
     * @param id
     * @return
     */
    @Override

    public AftersaleBarterDTO get(Long id) {
        QueryWrapper<AftersaleBarterEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "aftersale_sn", id);
        AftersaleBarterEntity entity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(entity, AftersaleBarterDTO.class);
    }

    /**
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleBarterDTO>
     * @Description 查询所有的订单换货列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */
    @Override

    public List<AftersaleBarterDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleBarterDTO>
     * @Description 分页查询所有的订单换货列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */
    @Override

    public PageData<AftersaleReturnPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId) {
        if (storeId != null) {
            params.put("storeId", storeId);
        }
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        // 分页
        IPage<AftersaleBarterEntity> page = getPage(params, "lar.create_date", false);
        // 查询
        List<AftersaleReturnPageDTO> list = aftersaleBarterDao.findPage(params, page);
        return new PageData<>(list, page.getTotal());
    }

    /***
     * @Description 换货详情
     * @Param  * @param aftersaleSn:
     * @Author huangkeyuan
     * @Date 11:16 2019-06-17
     * @return com.leimingtech.modules.aftersale.dto.aftersaleBarterDetailDTO
     */
    @Override

    public AftersaleBarterDetailDTO detail(Long aftersaleSn) {

        AftersaleBarterDetailDTO aftersaleBarterDetailDTO = new AftersaleBarterDetailDTO();
        // 获取基本的换货信息
        AftersaleBarterDTO aftersaleBarterDTO = this.get(aftersaleSn);
        aftersaleBarterDetailDTO.setAftersaleBarterDTO(aftersaleBarterDTO);
        //获取仲裁信息
        ArbitrationDTO arbitrationDTO = arbitrationService.getDataByAfterSn(aftersaleSn);
        if (!BeanUtil.isEmpty(arbitrationDTO)) {
            aftersaleBarterDetailDTO.setArbitrationDTO(arbitrationDTO);
        }

        // 获取换货原因
        AftersaleApplyDTO aftersaleApplyDTO = aftersaleApplyService.getApply(aftersaleSn);
        aftersaleBarterDetailDTO.setAftersaleApplyDTO(aftersaleApplyDTO);

        // 获取商家换货审核
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", aftersaleSn);
        List<AftersaleAuditLogDTO> auditList = aftersaleAuditLogService.list(map);
        aftersaleBarterDetailDTO.setAftersaleAuditLogDTOList(auditList);

        // 获取换货商品数据
        List<AftersaleGoodsDTO> goodsDTOList = aftersaleGoodsService.list(map);
        aftersaleBarterDetailDTO.setAftersaleGoodsDTOList(goodsDTOList);

        // 获取日志列表
        List<AftersaleLogListDTO> logDTOList = aftersaleLogService.listLog(aftersaleSn);
        List<AftersaleLogListDTO> logAuditDTOList = aftersaleAuditLogService.listLog(aftersaleSn);
        logDTOList.addAll(logAuditDTOList);
        Collections.sort(logDTOList, new TradeNoComparatorUp());

        aftersaleBarterDetailDTO.setAftersaleLogListDTOList(logDTOList);

        return aftersaleBarterDetailDTO;

    }

    /**
     * 换货商家确认收货接口
     *
     * @param
     */
    @Override

    public void confirmGoods(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("aftersaleStatus") Integer aftersaleStatus) {
        aftersaleBarterDao.confirmGoods(aftersaleSn, aftersaleStatus, AfterSaleStateEnum.LOGISTICS_STATUS_SELLERIN.value());
        // 发送异步消息保存审核日志和售后日志
        AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
        aftersaleLogSaveDTO.setAftersaleSn(aftersaleSn);
        aftersaleLogSaveDTO.setMessage("换货-商家确认收货");
        aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_BARTEROUT.value());
        aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_IN.value());
        aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_BARTER.value());
        aftersaleLogSaveDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
        aftersaleLogSaveDTO.setUpdater("店铺小二(" + SecurityUser.getUserName() + ")");
        aftersaleLogSaveDTO.setCreateDate(new Date());
        String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:审核同意换货申请xx天后，买家未处理，自动取消退货处理
     * @Date: 2019/6/24 17:42
     * @Version: V1.0
     */
    @Override

    public List<String> findSellerInOutTimeList(@RequestParam("days") String days, @RequestParam("logisticsStatus") String logisticsStatus) {
        return baseDao.findSellerInOutTimeList(days, logisticsStatus);
    }

    /**
     * 换货商家上传发货物流信息的接口
     *
     * @param
     */
    @Override

    public void uploadExpress(@RequestParam Map<String, Object> params) {
        Long sellerDeliveryNo = IdGenerator.defaultSnowflakeId();
        params.put("sellerDeliveryNo", sellerDeliveryNo);
        Date sellerDeliveryTime = new Date();
        params.put("sellerDeliveryTime", sellerDeliveryTime);
        if (MAP_PARAM_DELIVERY_MENTION.equals(params.get(MAP_PARAM_DELIVERY_TYPE))
                || MAP_PARAM_DELIVERY_NO.equals(params.get(MAP_PARAM_DELIVERY_TYPE))) {
            params.put("sellerLogisticsCompany", "无");
            params.put("sellerLogisticsNumber", "无");
        }
        aftersaleBarterDao.uploadExpress(params);

        Long aftersaleSn = Long.valueOf(params.get("aftersaleSn").toString());
        AftersaleBarterDTO aftersaleBarterDTO = this.get(aftersaleSn);
        // 发送异步消息保存审核日志和售后日志
        AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
        aftersaleLogSaveDTO.setAftersaleSn(aftersaleSn);
        aftersaleLogSaveDTO.setMessage("换货-商家再次发货并已上传物流信息");
        aftersaleLogSaveDTO.setCreator("店铺小二(" + SecurityUser.getUserName() + ")");
        aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_BARTEROUT.value());
        aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_BARTOUT.value());
        aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_BARTER.value());
        aftersaleLogSaveDTO.setCreateDate(new Date());
        String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:定时批量取消
     * @Date: 2019/6/25 20:58
     * @Version: V1.0
     */
    @Override

    //@GlobalTransactional(rollbackFor = Exception.class)
    public void batchCancel(@RequestBody List<String> aftersaleBarterList) {
        // 批量取消
        baseDao.batchCancel(aftersaleBarterList);
        // 发送日志
        for (String afterSaleSn : aftersaleBarterList) {
            // 发送异步消息保存审核日志和售后日志
            AftersaleLogSaveDTO aftersaleLogSaveDTO = new AftersaleLogSaveDTO();
            aftersaleLogSaveDTO.setAftersaleSn(Long.valueOf(afterSaleSn));
            aftersaleLogSaveDTO.setMessage("定时自动取消");
            aftersaleLogSaveDTO.setCreator("平台运营");
            aftersaleLogSaveDTO.setUpdater("平台运营");
            aftersaleLogSaveDTO.setStatus(AfterSaleStateEnum.AFTER_STATUS_CANCEL.value());
            aftersaleLogSaveDTO.setProcess(AfterSaleStateEnum.LOG_CANCEL.value());
            aftersaleLogSaveDTO.setType(AfterSaleStateEnum.TYPE_BARTER.value());
            aftersaleLogSaveDTO.setCreateDate(new Date());
            String saveApplyLogMessage = JSONObject.toJSONString(aftersaleLogSaveDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE, saveApplyLogMessage);
        }

    }

    /**
     * @param aftersaleSn: 售后单号
     * @param memberId:    用户id
     * @Author: SWH ab4856812@163.com
     * @Description: 根据售后单号和用户id查询退货信息
     * @Date: 2019/6/28 21:20
     * @Version: V1.0
     */

    @Override
    public AftersaleBarterDTO getDetail(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId) {
        AftersaleBarterDTO aftersaleBarterDTO = new AftersaleBarterDTO();
        QueryWrapper<AftersaleBarterEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(aftersaleSn != null, "aftersale_sn", aftersaleSn);
        wrapper.eq(memberId != null, "member_id", memberId);
        AftersaleBarterEntity entity = baseDao.selectOne(wrapper);
        if (null != entity) {
            BeanCopier.create(AftersaleBarterEntity.class, AftersaleBarterDTO.class, false).copy(entity, aftersaleBarterDTO, null);
        } else {
            return null;
        }
        return aftersaleBarterDTO;
    }

    /**
     * @Author weixianchun
     * @Description 换货 换货列表导出
     * @Param params
     * @Date 2019/12/4 12:22
     * @Return java.util.List<com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO>
     * @version 1.0
     */

    @Override
    public List<AftersaleReturnPageDTO> findListExport(@RequestParam Map<String, Object> params) {
        return baseDao.findPage(params, null);
    }

    /**
     * 根据时间排序---降序
     */
    static class TradeNoComparatorUp implements Comparator {
        @Override
        public int compare(Object object1, Object object2) {
            // 强制转换
            AftersaleLogListDTO aftersaleLogListDTO = (AftersaleLogListDTO) object1;
            AftersaleLogListDTO logListDTO = (AftersaleLogListDTO) object2;
            return logListDTO.getCreateDate().compareTo(aftersaleLogListDTO.getCreateDate());
        }
    }

}
