/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.price.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dao.price.PriceLogDao;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceUpdateDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.price.PriceLogDTO;
import com.leimingtech.modules.dto.price.SavePriceLogDTO;
import com.leimingtech.modules.dto.price.UpdateBatchPriceLog;
import com.leimingtech.modules.dto.price.UpdatePriceLogDTO;
import com.leimingtech.modules.entity.price.PriceLogEntity;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.price.PriceLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 价格修改记录管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */
@Service

public class PriceLogServiceImpl extends CrudServiceImpl<PriceLogDao, PriceLogEntity, PriceLogDTO> implements PriceLogService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(PriceLogServiceImpl.class);
    @Autowired

    private GoodsSpecService goodsSpecService;
    @Autowired

    private GoodsService goodsService;

    /**
     * 条件构造器
     *
     * @param params 分页参数
     * @return
     */
    @Override
    public QueryWrapper<PriceLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String goodsId = (String) params.get("goodsId");
        String storeId = (String) params.get("storeId");
        String sku = (String) params.get("sku");
        String storeName = (String) params.get("storeName");
        String goodsName = (String) params.get("goodsName");
        QueryWrapper<PriceLogEntity> wrapper = new QueryWrapper<>();
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

    public PageData<PriceLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<PriceLogEntity> page = getPage(params, "lpl.create_date", false);
        List<PriceLogDTO> priceLogDTOList = baseDao.selectLogPage(params);
        return new PageData<>(priceLogDTOList, page.getTotal());
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public PriceLogDTO get(@RequestParam("id") Long id) {
        return super.get(id);
    }

    /**
     * 保存价格修改记录
     *
     * @param dto 参数实体
     */
    @Override

    public void save(@RequestBody SavePriceLogDTO dto) {
        PriceLogDTO priceLogDTO = ConvertUtils.sourceToTarget(dto, PriceLogDTO.class);
        super.save(priceLogDTO);

    }

    /**
     * 修改价格记录
     *
     * @param dto 参数实体
     */
    @Override

    public void update(@RequestBody UpdatePriceLogDTO dto) {
        PriceLogDTO priceLogDTO = ConvertUtils.sourceToTarget(dto, PriceLogDTO.class);
        super.update(priceLogDTO);

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

    public List<PriceLogDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 批量更新数据
     *
     * @param updateBatchPriceLog 参数
     */
    @Override

    public void insertBatch(@RequestBody UpdateBatchPriceLog updateBatchPriceLog) {
        List<PriceLogEntity> savePriceLogDTOList = new ArrayList<>();
        // 查出商品信息
        GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(updateBatchPriceLog.getGoodsSpecPriceUpdateDTOList().get(0).getId());
        GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
        // 遍历规格信息
        // 循环修改记录参数
        PriceLogEntity priceLogEntity = null;
        for (GoodsSpecPriceUpdateDTO goodsSpecPriceUpdateDTO : updateBatchPriceLog.getGoodsSpecPriceUpdateDTOList()) {
            priceLogEntity = new PriceLogEntity();
            priceLogEntity.setGoodsName(goodsDTO.getGoodsName());
            priceLogEntity.setBrandName(goodsDTO.getBrandName());
            priceLogEntity.setStoreName(goodsDTO.getStoreName());
            priceLogEntity.setStoreId(goodsDTO.getStoreId());
            priceLogEntity.setSku(goodsSpecPriceUpdateDTO.getId());
            priceLogEntity.setSpecSerial(goodsSpecPriceUpdateDTO.getSpecSerial());
            priceLogEntity.setBeforeCostPrice(goodsSpecPriceUpdateDTO.getBeforeSpecCostPrice());
            priceLogEntity.setBeforeMarketPrice(goodsSpecPriceUpdateDTO.getBeforeSpecSellPrice());
            priceLogEntity.setGoodsId(goodsDTO.getId());
            priceLogEntity.setAfterMarketPrice(goodsSpecPriceUpdateDTO.getSpecSellPrice());
            priceLogEntity.setAfterCostPrice(goodsSpecPriceUpdateDTO.getSpecCostPrice());
            // 判断 从MQ过来的数据中请求信息中是没有username的，要手动赋值
            if (StringUtils.isNotBlank(updateBatchPriceLog.getUserName())) {
                priceLogEntity.setCreator(updateBatchPriceLog.getUserName());
                priceLogEntity.setUpdater(updateBatchPriceLog.getUserName());
            }

            savePriceLogDTOList.add(priceLogEntity);
        }
        insertBatch(savePriceLogDTOList);


    }

    /**
     * @Author weixianchun
     * @Description 导出查询列表
     * @Param params
     * @Date 2019/12/5 9:56
     * @Return java.util.List<com.leimingtech.modules.dto.price.PriceLogDTO>
     * @version 1.0
     */

    @Override
    public List<PriceLogDTO> selectLogExport(@RequestParam Map<String, Object> params) {
        return baseDao.selectLogPage(params);
    }

}
