/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.updatelog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.updatelog.UpdateLogDao;
import com.leimingtech.dto.updatelog.UpdateLogDTO;
import com.leimingtech.entity.updatelog.UpdateLogEntity;
import com.leimingtech.service.updatelog.UpdateLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Service
@Transactional

public class UpdateLogServiceImpl extends BaseServiceImpl<UpdateLogDao, UpdateLogEntity> implements UpdateLogService {

    @Override

    public PageData<UpdateLogDTO> page(@RequestParam Map<String, Object> params) {

        IPage<UpdateLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );


        return getPageData(page, UpdateLogDTO.class);
    }

    @Override

    public List<UpdateLogDTO> list(Map<String, Object> params) {
        List<UpdateLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, UpdateLogDTO.class);
    }

    private QueryWrapper<UpdateLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<UpdateLogEntity> wrapper = new QueryWrapper<>();
        wrapper.ge(StringUtils.isNotBlank((String) params.get("startCreateDate")), "save_date", params.get("startCreateDate"));
        wrapper.le(StringUtils.isNotBlank((String) params.get("endCreateDate")), "save_date", params.get("endCreateDate"));
        wrapper.like(StringUtils.isNotBlank((String) params.get("logTitle")), "log_title", params.get("logTitle"));
        wrapper.eq(StringUtils.isNotBlank((String) params.get("logType")), "log_type", params.get("logType"));
        //根据创建时间  降序排序
        wrapper.orderByDesc("create_date");

        return wrapper;
    }

    @Override

    public UpdateLogDTO get(Long id) {
        UpdateLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, UpdateLogDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody UpdateLogDTO dto) {
        UpdateLogEntity entity = ConvertUtils.sourceToTarget(dto, UpdateLogEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody UpdateLogDTO dto) {
        UpdateLogEntity entity = ConvertUtils.sourceToTarget(dto, UpdateLogEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, UpdateLogEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}