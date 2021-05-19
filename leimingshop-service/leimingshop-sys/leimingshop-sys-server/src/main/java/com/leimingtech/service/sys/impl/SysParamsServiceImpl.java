/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysParamsDao;
import com.leimingtech.dto.SysParamsDTO;
import com.leimingtech.entity.SysParamsEntity;
import com.leimingtech.redis.SysParamsRedis;
import com.leimingtech.service.sys.SysParamsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 参数管理
 *
 * @since 1.0.0
 */
@Service

public class SysParamsServiceImpl extends BaseServiceImpl<SysParamsDao, SysParamsEntity> implements SysParamsService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysParamsRedis sysParamsRedis;

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据page查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    @Override
    public PageData<SysParamsDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SysParamsEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysParamsDTO.class);
    }

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据list查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    @Override
    public List<SysParamsDTO> list(@RequestParam Map<String, Object> params) {
        List<SysParamsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysParamsDTO.class);
    }

    private QueryWrapper<SysParamsEntity> getWrapper(Map<String, Object> params) {
        String paramCode = (String) params.get("paramCode");

        QueryWrapper<SysParamsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("param_type", 1);
        wrapper.like(StringUtils.isNotBlank(paramCode), "param_code", paramCode);

        return wrapper;
    }

    /**
     * @return com.leimingtech.dto.SysMenuDTO
     * @Description 根据id查询
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 14:53 2019-05-27
     */

    @Override
    public SysParamsDTO get(@RequestParam("id") Long id) {
        SysParamsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysParamsDTO.class);
    }

    /**
     * @return void
     * @Description 保存参数信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:05 2019-05-27
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@RequestBody SysParamsDTO dto) {
        try {
            SysParamsEntity entity = ConvertUtils.sourceToTarget(dto, SysParamsEntity.class);
            insert(entity);

            sysParamsRedis.set(entity.getParamCode(), entity.getParamValue());
        } catch (Exception e) {
            logger.error("错误信息为" + e);
        }

    }

    /**
     * @return void
     * @Description 修改参数信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 15:07 2019-05-27
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody SysParamsDTO dto) {
        SysParamsEntity entity = ConvertUtils.sourceToTarget(dto, SysParamsEntity.class);
        updateById(entity);

        sysParamsRedis.set(entity.getParamCode(), entity.getParamValue());
    }

    /**
     * @return void
     * @Description 删除参数信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 15:09 2019-05-27
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(@RequestBody Long[] ids) {
        //删除Redis数据
        List<String> paramCodeList = baseDao.getParamCodeList(ids);
        String[] paramCodes = paramCodeList.toArray(new String[paramCodeList.size()]);
        sysParamsRedis.delete(paramCodes);

        //逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 根据参数编码，获取参数的value值
     *
     * @param paramCode 参数编码
     */

    @Override
    public String getValue(String paramCode) {
        String paramValue = sysParamsRedis.get(paramCode);
        if (paramValue == null) {
            paramValue = baseDao.getValueByCode(paramCode);

            sysParamsRedis.set(paramCode, paramValue);
        }
        return paramValue;
    }

    /**
     * 根据参数编码，更新value
     *
     * @param paramCode  参数编码
     * @param paramValue 参数值
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateValueByCode(String paramCode,
                                 String paramValue) {
        int count = baseDao.updateValueByCode(paramCode, paramValue);
        sysParamsRedis.set(paramCode, paramValue);

        return count;
    }

}