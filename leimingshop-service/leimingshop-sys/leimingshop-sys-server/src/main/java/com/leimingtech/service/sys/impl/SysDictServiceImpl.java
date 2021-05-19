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
import com.leimingtech.dao.SysDictDao;
import com.leimingtech.dto.SysDictDTO;
import com.leimingtech.entity.SysDictEntity;
import com.leimingtech.service.sys.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @since 1.0.0
 */
@Service

public class SysDictServiceImpl extends BaseServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {
    /**
     * 条件构造器
     *
     * @param params 分页参数
     * @return
     */

    @Override

    public PageData<SysDictDTO> page(@RequestParam Map<String, Object> params) {
        QueryWrapper<SysDictEntity> wrapper = getWrapper(params);
        wrapper.eq("pid", Constant.DICT_ROOT);

        IPage<SysDictEntity> page = baseDao.selectPage(
                getPage(params, "sort", true),
                wrapper
        );

        return getPageData(page, SysDictDTO.class);
    }

    /**
     * 查询字典分类
     *
     * @param params 查询参数青蛙
     * @return
     */
    @Override

    public List<SysDictDTO> list(@RequestParam Map<String, Object> params) {
        List<SysDictEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysDictDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params 查询条件
     * @return
     */

    private QueryWrapper<SysDictEntity> getWrapper(Map<String, Object> params) {
        String pid = (String) params.get("pid");
        String dictType = (String) params.get("dictType");
        String dictName = (String) params.get("dictName");
        String dictValue = (String) params.get("dictValue");

        QueryWrapper<SysDictEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        if (StringUtils.isNotBlank(pid)) {
            wrapper.eq(StringUtils.isNotBlank(pid), "pid", Long.parseLong(pid));
        }
        wrapper.eq(StringUtils.isNotBlank(dictType), "dict_type", dictType);
        wrapper.like(StringUtils.isNotBlank(dictName), "dict_name", dictName);
        wrapper.like(StringUtils.isNotBlank(dictValue), "dict_value", dictValue);

        return wrapper;
    }

    /**
     * 根据ID查询信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public SysDictDTO get(Long id) {
        SysDictEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysDictDTO.class);
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody SysDictDTO dto) {
        SysDictEntity entity = ConvertUtils.sourceToTarget(dto, SysDictEntity.class);

        insert(entity);
    }

    /**
     * 修改数据信息
     *
     * @param dto 参数实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody SysDictDTO dto) {
        SysDictEntity entity = ConvertUtils.sourceToTarget(dto, SysDictEntity.class);

        updateById(entity);
    }

    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}