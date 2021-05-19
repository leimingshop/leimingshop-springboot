/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysLogErrorDao;
import com.leimingtech.dto.SysLogErrorDTO;
import com.leimingtech.entity.SysLogErrorEntity;
import com.leimingtech.service.sys.SysLogErrorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 异常日志
 *
 * @since 1.0.0
 */
@Service

public class SysLogErrorServiceImpl extends BaseServiceImpl<SysLogErrorDao, SysLogErrorEntity> implements SysLogErrorService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<SysLogErrorDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SysLogErrorEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysLogErrorDTO.class);
    }

    /**
     * 导出日志信息
     *
     * @param params
     * @return
     */

    @Override
    public List<SysLogErrorDTO> list(@RequestParam Map<String, Object> params) {
        List<SysLogErrorEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysLogErrorDTO.class);
    }

    private QueryWrapper<SysLogErrorEntity> getWrapper(Map<String, Object> params) {
        String module = (String) params.get("module");

        QueryWrapper<SysLogErrorEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(module), "module", module);
        return wrapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysLogErrorEntity entity) {
        insert(entity);
    }

}
