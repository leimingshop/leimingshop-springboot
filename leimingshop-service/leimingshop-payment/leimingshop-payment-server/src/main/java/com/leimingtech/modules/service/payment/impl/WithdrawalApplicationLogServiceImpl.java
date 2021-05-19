/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.payment.WithdrawalApplicationLogDao;
import com.leimingtech.modules.dto.payment.WithdrawalApplicationLogDTO;
import com.leimingtech.modules.entity.payment.WithdrawalApplicationLogEntity;
import com.leimingtech.modules.service.payment.WithdrawalApplicationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 提现日志表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */
@Service
@Transactional

public class WithdrawalApplicationLogServiceImpl extends BaseServiceImpl<WithdrawalApplicationLogDao, WithdrawalApplicationLogEntity> implements WithdrawalApplicationLogService {

    @Override

    public PageData<WithdrawalApplicationLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<WithdrawalApplicationLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, WithdrawalApplicationLogDTO.class);
    }

    @Override

    public List<WithdrawalApplicationLogDTO> list(Map<String, Object> params) {
        QueryWrapper<WithdrawalApplicationLogEntity> wrapper = getWrapper(params);
        wrapper.orderByAsc("create_date");
        List<WithdrawalApplicationLogEntity> entityList = baseDao.selectList(wrapper);

        return ConvertUtils.sourceToTarget(entityList, WithdrawalApplicationLogDTO.class);
    }

    private QueryWrapper<WithdrawalApplicationLogEntity> getWrapper(Map<String, Object> params) {
        Object id = params.get("id");
        Object applyId = params.get("applyId");


        QueryWrapper<WithdrawalApplicationLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(null != id, "id", id);
        wrapper.eq(null != applyId, "apply_id", applyId);

        return wrapper;
    }

    @Override

    public WithdrawalApplicationLogDTO get(Long id) {
        WithdrawalApplicationLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, WithdrawalApplicationLogDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody WithdrawalApplicationLogDTO dto) {
        WithdrawalApplicationLogEntity entity = ConvertUtils.sourceToTarget(dto, WithdrawalApplicationLogEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody WithdrawalApplicationLogDTO dto) {
        WithdrawalApplicationLogEntity entity = ConvertUtils.sourceToTarget(dto, WithdrawalApplicationLogEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, WithdrawalApplicationLogEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
