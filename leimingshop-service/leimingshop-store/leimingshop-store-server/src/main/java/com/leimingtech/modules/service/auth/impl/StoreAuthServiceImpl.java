/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.auth.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dao.auth.StoreAuthDao;
import com.leimingtech.modules.dto.auth.StoreAuthDTO;
import com.leimingtech.modules.entity.auth.StoreAuthEntity;
import com.leimingtech.modules.service.auth.StoreAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 店铺认证信息表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreAuthServiceImpl extends CrudServiceImpl<StoreAuthDao, StoreAuthEntity, StoreAuthDTO> implements StoreAuthService {

    @Autowired
    private StoreAuthDao storeAuthDao;

    /**
     * 条件构造器
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public QueryWrapper<StoreAuthEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreAuthEntity> wrapper = new QueryWrapper<>();
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

    public PageData<StoreAuthDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreAuthDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    @Override

    public void save(@RequestBody StoreAuthDTO dto) {
        super.save(dto);
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody StoreAuthDTO dto) {
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
     * 根据店铺ID 查询店铺认证信息
     *
     * @param storeId 店铺ID
     * @return
     */
    @Override

    public StoreAuthDTO findByStoreId(@RequestParam("storeId") Long storeId) {
        return storeAuthDao.findByStoreId(storeId);
    }

    /**
     * 根据店铺ID 删除店铺认证信息
     *
     * @param ids 店铺ID
     */

    @Override
    public void deleteByStoreId(@RequestBody Long[] ids) {

        baseDao.deleteByStoreId(ids);
    }

    /**
     * 获取店铺公司id
     *
     * @param storeId
     * @return
     */

    @Override
    public Long getStoreAuthId(@RequestParam("storeId") Long storeId) {
        return baseDao.findAuthIdByStoreId(storeId);
    }

}