/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.settle.ReturnBillDao;
import com.leimingtech.modules.dto.settle.ReturnBillDTO;
import com.leimingtech.modules.entity.settle.ReturnBillEntity;
import com.leimingtech.modules.service.settle.ReturnBillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 退货结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class ReturnBillServiceImpl extends BaseServiceImpl<ReturnBillDao, ReturnBillEntity> implements ReturnBillService {
    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<ReturnBillDTO> page(@RequestParam Map<String, Object> params) {
        IPage<ReturnBillEntity> page = baseDao.selectPage(
                getPage(params, "manage_time", false),
                getWrapper(params)
        );

        return getPageData(page, ReturnBillDTO.class);
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<ReturnBillDTO> list(@RequestParam Map<String, Object> params) {
        List<ReturnBillEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ReturnBillDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<ReturnBillEntity> getWrapper(Map<String, Object> params) {
        String billTotalId = (String) params.get("billTotalId");

        QueryWrapper<ReturnBillEntity> wrapper = new QueryWrapper<>();
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

    public ReturnBillDTO get(Long id) {
        ReturnBillEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ReturnBillDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody ReturnBillDTO dto) {
        ReturnBillEntity entity = ConvertUtils.sourceToTarget(dto, ReturnBillEntity.class);

        insert(entity);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody ReturnBillDTO dto) {
        ReturnBillEntity entity = ConvertUtils.sourceToTarget(dto, ReturnBillEntity.class);

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
        //logicDelete(ids, ReturnBillEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 批量保存退款订单
     *
     * @param returnBillDTOList
     */

    @Override
    public void saveBatch(@RequestBody List<ReturnBillDTO> returnBillDTOList) {
        insertBatch(ConvertUtils.sourceToTarget(returnBillDTOList, ReturnBillEntity.class));
    }
}