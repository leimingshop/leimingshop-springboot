/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.settle.OrderBillDao;
import com.leimingtech.modules.dto.settle.OrderBillDTO;
import com.leimingtech.modules.entity.settle.OrderBillEntity;
import com.leimingtech.modules.service.settle.OrderBillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 订单结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)

public class OrderBillServiceImpl extends BaseServiceImpl<OrderBillDao, OrderBillEntity> implements OrderBillService {
    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<OrderBillDTO> page(@RequestParam Map<String, Object> params) {
        IPage<OrderBillEntity> page = baseDao.selectPage(
                getPage(params, "pay_time", false),
                getWrapper(params)
        );

        return getPageData(page, OrderBillDTO.class);
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<OrderBillDTO> list(@RequestParam Map<String, Object> params) {
        List<OrderBillEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, OrderBillDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<OrderBillEntity> getWrapper(Map<String, Object> params) {
        String billTotalId = (String) params.get("billTotalId");

        QueryWrapper<OrderBillEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(billTotalId), "bill_total_id", billTotalId);

        return wrapper;
    }

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */
    @Override

    public OrderBillDTO get(Long id) {
        OrderBillEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, OrderBillDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody OrderBillDTO dto) {
        OrderBillEntity entity = ConvertUtils.sourceToTarget(dto, OrderBillEntity.class);

        insert(entity);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody OrderBillDTO dto) {
        OrderBillEntity entity = ConvertUtils.sourceToTarget(dto, OrderBillEntity.class);

        updateById(entity);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, OrderBillEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 批量保存结算订单
     *
     * @param orderBillDTOList
     */

    @Override
    public void saveBatch(@RequestBody List<OrderBillDTO> orderBillDTOList) {
        List<OrderBillEntity> orderBillEntities = ConvertUtils.sourceToTarget(orderBillDTOList, OrderBillEntity.class);

        insertBatch(orderBillEntities);
    }
}