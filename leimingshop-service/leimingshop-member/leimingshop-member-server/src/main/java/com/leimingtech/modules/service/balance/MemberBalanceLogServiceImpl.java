/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.balance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.balance.MemberBalanceLogDao;
import com.leimingtech.modules.dto.balance.MemberBalanceLogDTO;
import com.leimingtech.modules.entity.balance.MemberBalanceLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 用户余额明细表
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class MemberBalanceLogServiceImpl extends BaseServiceImpl<MemberBalanceLogDao, MemberBalanceLogEntity> implements MemberBalanceLogService {
    private static final String PARAMS_NAME_MEMBER_ID = "memberId";

    @Override

    public PageData<MemberBalanceLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<MemberBalanceLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, MemberBalanceLogDTO.class);
    }

    @Override

    public List<MemberBalanceLogDTO> list(Map<String, Object> params) {
        List<MemberBalanceLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, MemberBalanceLogDTO.class);
    }

    private QueryWrapper<MemberBalanceLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        QueryWrapper<MemberBalanceLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        if (null != params.get(PARAMS_NAME_MEMBER_ID)) {
            Long memberId = Long.valueOf(params.get(PARAMS_NAME_MEMBER_ID).toString());
            wrapper.eq("member_id", memberId);
        }

        return wrapper;
    }

    @Override

    public MemberBalanceLogDTO get(Long id) {
        MemberBalanceLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, MemberBalanceLogDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody MemberBalanceLogDTO dto) {
        MemberBalanceLogEntity entity = ConvertUtils.sourceToTarget(dto, MemberBalanceLogEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody MemberBalanceLogDTO dto) {
        MemberBalanceLogEntity entity = ConvertUtils.sourceToTarget(dto, MemberBalanceLogEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, MemberBalanceLogEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    @Override
    public void updateBatch(@RequestBody List<MemberBalanceLogDTO> balanceLogDTOList) {
        List<MemberBalanceLogEntity> memberBalanceLogEntities = Optional.of(
                ConvertUtils.sourceToTarget(balanceLogDTOList, MemberBalanceLogEntity.class))
                .orElse(new ArrayList<MemberBalanceLogEntity>());
        super.insertBatch(memberBalanceLogEntities);
    }
}
