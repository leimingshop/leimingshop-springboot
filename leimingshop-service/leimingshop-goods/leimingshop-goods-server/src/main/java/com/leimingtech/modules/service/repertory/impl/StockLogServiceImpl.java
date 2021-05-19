/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.repertory.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.stock.StockLogDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.cart.CartDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecStorageUpdateDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.member.MemberUmengTokenInfo;
import com.leimingtech.modules.dto.stock.SaveStockLogDTO;
import com.leimingtech.modules.dto.stock.StockLogDTO;
import com.leimingtech.modules.dto.stock.UpdataStockLogDTO;
import com.leimingtech.modules.dto.stock.UpdateBatchStockLog;
import com.leimingtech.modules.entity.stock.StockLogEntity;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.stock.StockLogService;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存修改记录管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Service

public class StockLogServiceImpl extends CrudServiceImpl<StockLogDao, StockLogEntity, StockLogDTO> implements StockLogService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StockLogServiceImpl.class);
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SysMessageService sysMessageService;

    /**
     * 条件构造器
     *
     * @param params 分页参数
     * @return
     */
    @Override
    public QueryWrapper<StockLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String goodsId = (String) params.get("goodsId");
        String storeId = (String) params.get("storeId");
        String sku = (String) params.get("sku");
        String storeName = (String) params.get("storeName");
        String goodsName = (String) params.get("goodsName");
        QueryWrapper<StockLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.eq(StringUtils.isNotBlank(goodsId), "goods_id", goodsId);
        wrapper.eq(StringUtils.isNotBlank(sku), "sku", sku);
        wrapper.like(StringUtils.isNotBlank(storeName), "store_name", storeName);
        wrapper.like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName);
        wrapper.orderByDesc("create_date", "sku");
        return wrapper;
    }

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<StockLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<StockLogEntity> page = getPage(params, "lpl.create_date", false);
        List<StockLogDTO> priceLogDTOList = baseDao.selectLogPage(params);
        return new PageData<>(priceLogDTOList, page.getTotal());
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StockLogDTO get(@RequestParam("id") Long id) {
        return super.get(id);
    }

    /**
     * 保存价格修改记录
     *
     * @param dto 参数实体
     */
    @Override

    public void save(@RequestBody SaveStockLogDTO dto) {
        StockLogEntity stockLogEntity = ConvertUtils.sourceToTarget(dto, StockLogEntity.class);
        super.insert(stockLogEntity);

    }

    /**
     * 修改价格记录
     *
     * @param dto 参数实体
     */
    @Override

    public void update(@RequestBody UpdataStockLogDTO dto) {
        StockLogDTO stockLogDTO = ConvertUtils.sourceToTarget(dto, StockLogDTO.class);
        super.update(stockLogDTO);

    }

    /**
     * 根据ID删除修改记录
     *
     * @param ids 主键ID
     */

    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 查询出修改记录
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public List<StockLogDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 批量保存数据
     *
     * @param updateBatchStockLog 参数
     */
    @Override

    public void insertBatch(@RequestBody UpdateBatchStockLog updateBatchStockLog) {
        List<StockLogEntity> priceLogEntityList = new ArrayList<>();

        // TODO 查询库存通知设置
        String value = "50,60,70";
        String[] split = value.split(",");

        // 循环修改记录参数
        StockLogEntity stockLogEntity = null;
        for (GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO : updateBatchStockLog.getGoodsSpecStorageUpdateDTOList()) {
            GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(goodsSpecStorageUpdateDTO.getId());
            GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
            stockLogEntity = new StockLogEntity();
            stockLogEntity.setId(IdWorker.getId());
            stockLogEntity.setSku(goodsSpecStorageUpdateDTO.getId());
            stockLogEntity.setSpecSerial(goodsSpecDTO.getSpecSerial());
            stockLogEntity.setBrandName(goodsDTO.getBrandName());
            stockLogEntity.setGoodsName(goodsDTO.getGoodsName());
            stockLogEntity.setStoreId(goodsDTO.getStoreId());
            stockLogEntity.setStoreName(goodsDTO.getStoreName());
            stockLogEntity.setGoodsId(goodsDTO.getId());
            stockLogEntity.setBeforeRepertory(goodsSpecStorageUpdateDTO.getBeforeSpecStorage());
            stockLogEntity.setAfterRepertory(goodsSpecStorageUpdateDTO.getSpecStorage());
            stockLogEntity.setAfterRepertory(goodsSpecStorageUpdateDTO.getSpecStorage());
            // 判断 从MQ过来的数据中请求信息中是没有username的，要手动赋值
            if (StringUtils.isNotBlank(updateBatchStockLog.getUserName())) {
                stockLogEntity.setUpdater(updateBatchStockLog.getUserName());
                stockLogEntity.setCreator(updateBatchStockLog.getUserName());
            }
            priceLogEntityList.add(stockLogEntity);
            // TODO lixiang 暂时注释库存不足提醒
            Boolean flag = updateBatchStockLog.getType() == null || updateBatchStockLog.getType() != 1;
            if (goodsSpecDTO.getSpecShow() == 1 && flag) {
                for (String str : split) {
                    int aa = Integer.parseInt(str);
                    if (aa > goodsSpecStorageUpdateDTO.getSpecStorage()) {
                        this.sendMessage(goodsSpecStorageUpdateDTO.getId(), goodsDTO.getStoreName());
                        break;
                    }
                }
            }
        }
        insertBatch(priceLogEntityList);
    }

    private void sendMessage(Long specId, String storeName) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("specId", specId);
        pageModelDTO.setCollapseField("memberId");
        String[] strings = new String[2];
        strings[0] = "memberId";
        strings[1] = "specName";
        pageModelDTO.setFetchSourceIncludes(strings);

        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();
        if (CollectionUtils.isEmpty(jsonRsList)) {
            return;
        }
        List<CartDTO> collect = jsonRsList.stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
        List<Long> memberList = collect.stream().map(c -> c.getMemberId()).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>(16);
        String specName = collect.get(0).getSpecName();

        //获取用户信息Map
        List<MemberUmengTokenInfo> tokenInfos = memberService.selectUmengTokenByIds(memberList);
        Map<Long, String> memberMap = new HashMap(10);
        tokenInfos.stream().forEach(memberUmengTokenInfo -> {
            memberMap.put(memberUmengTokenInfo.getId(), memberUmengTokenInfo.getMemberName());
        });
        map.put("memberMap", memberMap);

        //站内信、短信参数
        Map<String, String> paramMap = new HashMap<>(16);
        paramMap.put("goodsName", specName);
        paramMap.put("goodsId", String.valueOf(collect.get(0).getGoodsId()));
        paramMap.put("specId", String.valueOf(collect.get(0).getSpecId()));
        map.put("paramMap", paramMap);
        // TODO: 2020/4/24/024  xuzhch  手机号待处理
        map.put("mobile", "");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSendName(storeName);
        messageDTO.setMessageCode(MessageCodeEnum.CART_STORAGE_REMIND.value());
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * @Author weixianchun
     * @Description 导出查询列表
     * @Param params
     * @Date 2019/12/5 9:56
     * @version 1.0
     */

    @Override
    public List<StockLogDTO> selectLogExport(@RequestParam Map<String, Object> params) {
        return baseDao.selectLogPage(params);
    }

}
