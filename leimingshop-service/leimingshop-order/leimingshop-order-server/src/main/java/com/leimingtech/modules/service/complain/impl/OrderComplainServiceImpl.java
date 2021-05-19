/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.complain.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.complain.OrderComplainDao;
import com.leimingtech.modules.dto.complain.OrderComplainDTO;
import com.leimingtech.modules.dto.complain.OrderComplainDetailDTO;
import com.leimingtech.modules.dto.complain.OrderComplainPageDTO;
import com.leimingtech.modules.dto.complain.SaveOrderComplainDTO;
import com.leimingtech.modules.dto.store.PageStoreDTO;
import com.leimingtech.modules.entity.complain.OrderComplainEntity;
import com.leimingtech.modules.enums.complain.OrderComplainEnum;
import com.leimingtech.modules.service.complain.OrderComplainService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.store.StoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Service
@Transactional

public class OrderComplainServiceImpl extends BaseServiceImpl<OrderComplainDao, OrderComplainEntity> implements OrderComplainService {

    @Autowired

    private OrderService orderService;

    @Autowired
    private StoreService storeService;

    /**
     * H5投诉分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<OrderComplainPageDTO> page(@RequestParam Map<String, Object> params) {
        params.put("showFlag", OrderComplainEnum.SHOW_FLAG_YES.value());

        IPage<OrderComplainEntity> page = getPage(params, Constant.CREATE_DATE, false);
        List<OrderComplainPageDTO> list = baseDao.h5Page(params);

        return new PageData<>(list, page.getTotal());
    }

    /**
     * admin 端投诉分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<OrderComplainDTO> adminPage(@RequestParam Map<String, Object> params) {

        IPage<OrderComplainEntity> page = baseDao.selectPage(
                getPage(params, "status asc,create_date desc", true),
                getWrapper(params)
        );
        return getPageData(page, OrderComplainDTO.class);
    }

    /**
     * 条件查询所有信息
     *
     * @param params
     * @return
     */
    @Override

    public List<OrderComplainDetailDTO> list(@RequestParam Map<String, Object> params) {
        List<OrderComplainDetailDTO> entityList = baseDao.excel(params);
        entityList.stream().forEach(a -> {
            PageStoreDTO storeDTO = storeService.findStoreInfoById(a.getStoreId());
            a.setStoreAccount(storeDTO.getAccount());
        });
        return entityList;
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<OrderComplainEntity> getWrapper(Map<String, Object> params) {
        String memberId = (String) params.get("memberId");
        String showFlag = (String) params.get("showFlag");
        String account = (String) params.get("account");
        String phone = (String) params.get("phone");
        String type = (String) params.get("type");
        String source = (String) params.get("source");
        String verdict = (String) params.get("verdict");
        String disposePerson = (String) params.get("disposePerson");
        String disposeStartDate = (String) params.get("disposeStartDate");
        String disposeEndDate = (String) params.get("disposeEndDate");
        String createStartDate = (String) params.get("createStartDate");
        String createEndDate = (String) params.get("createEndDate");
        if (StringUtils.isNotBlank(disposeStartDate)) {
            disposeStartDate = disposeStartDate + " 00:00:00";
        }
        if (StringUtils.isNotBlank(disposeEndDate)) {
            disposeEndDate = disposeEndDate + " 23:59:59";
        }

        if (StringUtils.isNotBlank(createStartDate)) {
            createStartDate = createStartDate + " 00:00:00";
        }
        if (StringUtils.isNotBlank(createEndDate)) {
            createEndDate = createEndDate + " 23:59:59";
        }
        String status = (String) params.get("status");

        QueryWrapper<OrderComplainEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);
        wrapper.eq(StringUtils.isNotBlank(showFlag), "show_flag", showFlag);
        wrapper.eq(StringUtils.isNotBlank(account), "account", account);
        wrapper.eq(StringUtils.isNotBlank(phone), "phone", phone);
        wrapper.eq(StringUtils.isNotBlank(type), "type", type);
        wrapper.eq(StringUtils.isNotBlank(source), "source", source);
        wrapper.eq(StringUtils.isNotBlank(verdict), "verdict", verdict);
        wrapper.ge(StringUtils.isNotBlank(disposeStartDate), "dispose_date", disposeStartDate);
        wrapper.le(StringUtils.isNotBlank(disposeEndDate), "dispose_date", disposeEndDate);
        wrapper.ge(StringUtils.isNotBlank(createStartDate), "create_date", createStartDate);
        wrapper.le(StringUtils.isNotBlank(createEndDate), "create_date", createEndDate);
        wrapper.like(StringUtils.isNotBlank(disposePerson), "dispose_person", disposePerson);
        return wrapper;
    }

    /**
     * 投诉详情
     *
     * @param id
     * @return
     */
    @Override

    public OrderComplainDTO get(Long id) {
        OrderComplainEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, OrderComplainDTO.class);
    }


    /**
     * admin投诉详情
     *
     * @param id
     * @return
     */
    @Override

    public OrderComplainDetailDTO info(Long id) {
        OrderComplainDetailDTO dto = baseDao.info(id);
        // 查询店铺信息
        PageStoreDTO storeDTO = storeService.findStoreInfoById(dto.getStoreId());
        dto.setStoreStatus(storeDTO.getIsEnable());
        dto.setStoreAccount(storeDTO.getAccount());
        dto.setGradeName(storeDTO.getGradeName());
        return dto;
    }

    /**
     * 新增投诉
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody SaveOrderComplainDTO dto) {
        OrderComplainEntity entity = ConvertUtils.sourceToTarget(dto, OrderComplainEntity.class);
        entity.setShowFlag(OrderComplainEnum.SHOW_FLAG_YES.value());
        entity.setStatus(OrderComplainEnum.STATUS_NO.value());
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody OrderComplainDTO dto) {
        OrderComplainEntity entity = ConvertUtils.sourceToTarget(dto, OrderComplainEntity.class);

        updateById(entity);
    }

    /**
     * 用户删除投诉
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        baseDao.updateBatch(ids);
    }

    /**
     * 根据订单ID 查询投诉详情
     *
     * @param orderId
     * @param memberId
     */

    @Override
    public OrderComplainDTO orderComInfo(@RequestParam("orderId") Long orderId,
                                         @RequestParam("memberId") Long memberId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("del_flag", 0);
        OrderComplainEntity entity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(entity, OrderComplainDTO.class);
    }
}
