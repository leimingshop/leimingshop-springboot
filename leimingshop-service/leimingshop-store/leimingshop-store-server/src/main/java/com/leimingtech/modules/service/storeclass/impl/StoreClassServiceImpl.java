/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.storeclass.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.storeclass.StoreClassDao;
import com.leimingtech.modules.dto.goodsclass.GoodsClassQueryNameDTO;
import com.leimingtech.modules.dto.store.UpdateStoreInfo;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO;
import com.leimingtech.modules.entity.storeclass.StoreClassEntity;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.storeclass.StoreClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 店铺分类表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreClassServiceImpl extends CrudServiceImpl<StoreClassDao, StoreClassEntity, StoreClassDTO> implements StoreClassService {

    @Autowired
    private StoreClassDao storeClassDao;

    @Autowired
    private GoodsClassService goodsClassService;

    @Override
    public QueryWrapper<StoreClassEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<StoreClassDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreClassDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 新增数据
     *
     * @param storeClassDTOList 参数实体
     */

    @Override

    public void save(@RequestBody List<StoreClassDTO> storeClassDTOList) {
        List<StoreClassEntity> storeClassEntityList = ConvertUtils.sourceToTarget(storeClassDTOList, StoreClassEntity.class);
        super.insertBatch(storeClassEntityList);
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody StoreClassDTO dto) {
        super.update(dto);
    }


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据店铺ID 获取 店铺分类
     *
     * @param storeId 店铺ID
     * @return
     */
    @Override

    public List<StoreClassDTO> findByStoreId(@RequestParam("storeId") Long storeId) {
        return storeClassDao.findByStoreId(storeId);
    }

    @Override

    public void updateBatch(@RequestBody UpdateStoreInfo dto) {
        storeClassDao.deleteByStoreId(dto.getFindStoreDTO().getId());
        Long[] classId = dto.getClassId();
        List<StoreClassEntity> storeClassEntityList = new ArrayList<>();
        if (classId.length > 0) {
            for (Long id : classId) {
                StoreClassEntity storeClassEntity = new StoreClassEntity();
                storeClassEntity.setStoreId(dto.getFindStoreDTO().getId());
                storeClassEntity.setClassId(id);
                storeClassEntityList.add(storeClassEntity);
            }
            insertBatch(storeClassEntityList);
        }
    }

    /**
     * 查询店铺关联的商品分类
     *
     * @param storeId
     * @return
     * @author xuzhch
     */

    @Override
    public List<StoreGoodsClassDTO> selectStoreClass(Long storeId) {
        List<StoreClassEntity> storeClassEntities = baseDao.selectList(new QueryWrapper<StoreClassEntity>().eq("store_id", storeId));
        List<Long> classIds = new ArrayList<>();
        for (StoreClassEntity storeClassEntity : storeClassEntities) {
            classIds.add(storeClassEntity.getClassId());
        }
        if (null == classIds) {
            return null;
        }
        List<GoodsClassQueryNameDTO> goodsClassQueryNameDTOList = goodsClassService.selectListByClassId(classIds);
        return ConvertUtils.sourceToTarget(goodsClassQueryNameDTOList, StoreGoodsClassDTO.class);
    }


    /**
     * 根据分类ID 删除当前分类关联的店铺
     *
     * @param ids 分类ID
     */

    @Override
    public void deleteByClassId(@RequestBody Long[] ids) {
        baseDao.deleteByClassId(ids);


    }

    /**
     * 根据店铺ID 删除分类
     *
     * @param storeId
     */

    @Override
    public void deleteByStoreId(@RequestParam("storeId") Long storeId) {
        baseDao.deleteByStoreId(storeId);

    }

}