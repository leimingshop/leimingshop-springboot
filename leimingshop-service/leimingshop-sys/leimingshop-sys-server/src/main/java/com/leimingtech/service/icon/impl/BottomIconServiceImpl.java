/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.icon.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.icon.BottomIconDao;
import com.leimingtech.dto.icon.BottomIconDTO;
import com.leimingtech.dto.icon.BottomIconUpdateDTO;
import com.leimingtech.entity.icon.BottomIconEntity;
import com.leimingtech.service.icon.BottomIconService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * app底部图片配置表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-9
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BottomIconServiceImpl extends CrudServiceImpl<BottomIconDao, BottomIconEntity, BottomIconDTO> implements BottomIconService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */
    @Override

    public PageData<BottomIconDTO> page(@RequestParam Map<String, Object> params) {
        return null;
    }

    /**
     * 获取结果集
     *
     * @param params
     * @return
     */
    @Override

    public List<BottomIconDTO> list(@RequestParam Map<String, Object> params) {
        List<BottomIconEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, BottomIconDTO.class);
    }

    @Override
    public QueryWrapper<BottomIconEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<BottomIconEntity> wrapper = new QueryWrapper<>();
        if (params.get("id") != null) {
            Long id = Long.parseLong(params.get("id").toString());
            wrapper.eq(true, "id", id);
        }
        if (params.get("sortDesc") != null) {
            wrapper.orderByDesc("sort");
        }
        return wrapper;
    }

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     */
    @Override

    public BottomIconDTO get(Long id) {
        BottomIconEntity bottomIconEntity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(bottomIconEntity, BottomIconDTO.class);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean update(@RequestBody BottomIconUpdateDTO dto) {

        BottomIconEntity bottomIconEntity = ConvertUtils.sourceToTarget(dto, BottomIconEntity.class);

        return updateById(bottomIconEntity);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 查询所有底部icon
     *
     * @return
     */

    @Override
    public List<BottomIconDTO> all() {
        return baseDao.all();
    }


}
