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
import com.leimingtech.dao.SysLogLoginDao;
import com.leimingtech.dto.SysLogLoginDTO;
import com.leimingtech.entity.SysLogLoginEntity;
import com.leimingtech.service.sys.SysLogLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 登录日志
 *
 * @since 1.0.0
 */
@Service

public class SysLogLoginServiceImpl extends BaseServiceImpl<SysLogLoginDao, SysLogLoginEntity> implements SysLogLoginService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<SysLogLoginDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SysLogLoginEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysLogLoginDTO.class);
    }

    /**
     * 导出日志信息
     *
     * @param params
     * @return
     */

    @Override
    public List<SysLogLoginDTO> list(@RequestParam Map<String, Object> params) {
        List<SysLogLoginEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysLogLoginDTO.class);
    }

    private QueryWrapper<SysLogLoginEntity> getWrapper(Map<String, Object> params) {
        String status = (String) params.get("status");
        String creator = (String) params.get("creator");

        QueryWrapper<SysLogLoginEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);
        wrapper.like(StringUtils.isNotBlank(creator), "creator", creator);

        return wrapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysLogLoginEntity entity) {
        insert(entity);
    }

}
