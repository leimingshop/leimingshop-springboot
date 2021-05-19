/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.search.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.search.SynonymDao;
import com.leimingtech.dto.synonym.SynonymDTO;
import com.leimingtech.dto.synonym.SynonymSaveDTO;
import com.leimingtech.entity.search.SynonymEntity;
import com.leimingtech.service.search.SynonymService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 同义词ServiceImpl
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:50
 **/
@Service
@Transactional

public class SynonymServiceImpl extends BaseServiceImpl<SynonymDao, SynonymEntity> implements SynonymService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */
    @Override

    public PageData<SynonymDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SynonymEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SynonymDTO.class);
    }

    /**
     * 获取结果集
     *
     * @param params
     * @return
     */
    @Override

    public List<SynonymDTO> list(Map<String, Object> params) {
        List<SynonymEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SynonymDTO.class);
    }

    private QueryWrapper<SynonymEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<SynonymEntity> wrapper = new QueryWrapper<>();
        String id = (String) params.get("id");
        String name = (String) params.get("name");
        String state = (String) params.get("state");
        if (id != null) {
            wrapper.eq("id", Long.parseLong(id));
        }
        if (name != null) {
            wrapper.like("name", name);
        }
        if (state != null) {
            wrapper.eq("state", Integer.parseInt(state));
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

    public SynonymDTO get(Long id) {
        SynonymEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SynonymDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody SynonymSaveDTO dto) {
        SynonymEntity entity = ConvertUtils.sourceToTarget(dto, SynonymEntity.class);

        insert(entity);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody SynonymSaveDTO dto) {
        SynonymEntity entity = ConvertUtils.sourceToTarget(dto, SynonymEntity.class);

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
        //logicDelete(ids, ShopSynonymPO.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 查询全部同义词数据
     *
     * @return 全部同义词数据集合
     * @date 2019/12/10 10:36
     * @author lixiangx@leimingtech.com
     **/

    @Override
    public List<SynonymDTO> listForEs() {
        List<SynonymEntity> synonymList = baseDao.findAll();
        return ConvertUtils.sourceToTarget(synonymList, SynonymDTO.class);
    }

    /**
     * 功能描述:
     * <修改同义词启用停用状态>
     *
     * @param state 0:停用，1：启用
     * @param id    主键id
     * @return
     * @date 2020/3/18 11:07
     * @author weixianchun
     **/

    @Override
    public int updateStatus(@RequestParam("state") Integer state, @RequestParam("id") Long id) {
        SynonymEntity synonymEntity = new SynonymEntity();
        synonymEntity.setId(id);
        synonymEntity.setState(state);
        return baseDao.updateById(synonymEntity);
    }

    @Override

    public void updateBatch(@RequestBody List<SynonymSaveDTO> synonymSaveDTOList) {
        List<SynonymEntity> synonymEntities = ConvertUtils.sourceToTarget(synonymSaveDTOList, SynonymEntity.class);
        for (SynonymEntity synonymEntity : synonymEntities) {
            updateById(synonymEntity);
        }
    }
}
