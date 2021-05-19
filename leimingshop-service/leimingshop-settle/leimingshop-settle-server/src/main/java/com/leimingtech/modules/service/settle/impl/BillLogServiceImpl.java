/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.settle.BillLogDao;
import com.leimingtech.modules.dto.settle.BillLogDTO;
import com.leimingtech.modules.entity.settle.BillLogEntity;
import com.leimingtech.modules.service.settle.BillLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 对账操作记录
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class BillLogServiceImpl extends BaseServiceImpl<BillLogDao, BillLogEntity> implements BillLogService {


    /**
     * 分页查询操作记录
     *
     * @param params 查询参数
     * @return 返回操作记录分页信息
     */
    @Override

    public PageData<BillLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<BillLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, BillLogDTO.class);
    }

    /**
     * 查询操作记录
     *
     * @param params 查询条件
     * @return 返回操作记录
     */
    @Override

    public List<BillLogDTO> list(@RequestParam Map<String, Object> params) {
        List<BillLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, BillLogDTO.class);
    }

    private QueryWrapper<BillLogEntity> getWrapper(Map<String, Object> params) {
        String billId = (String) params.get("billId");

        QueryWrapper<BillLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(billId), "bill_id", billId);

        return wrapper;
    }

    /**
     * 根据ID获取信息
     *
     * @param id
     * @return
     */
    @Override

    public BillLogDTO get(Long id) {
        BillLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, BillLogDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody BillLogDTO dto) {
        BillLogEntity entity = ConvertUtils.sourceToTarget(dto, BillLogEntity.class);

        insert(entity);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody BillLogDTO dto) {
        BillLogEntity entity = ConvertUtils.sourceToTarget(dto, BillLogEntity.class);

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

        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


}