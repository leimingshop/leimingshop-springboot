/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.store.StoreInvoiceDao;
import com.leimingtech.modules.dto.store.StoreGoodsInvoiceSettingDTO;
import com.leimingtech.modules.dto.store.StoreInvoiceAddressDTO;
import com.leimingtech.modules.dto.store.StoreInvoiceDTO;
import com.leimingtech.modules.entity.store.StoreInvoiceEntity;
import com.leimingtech.modules.service.store.StoreInvoiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 店铺发票设置表
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-27
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class StoreInvoiceServiceImpl extends BaseServiceImpl<StoreInvoiceDao, StoreInvoiceEntity> implements StoreInvoiceService {

    @Override

    public PageData<StoreInvoiceDTO> page(@RequestParam Map<String, Object> params) {
        IPage<StoreInvoiceEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, StoreInvoiceDTO.class);
    }

    @Override

    public List<StoreInvoiceDTO> list(Map<String, Object> params) {
        List<StoreInvoiceEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, StoreInvoiceDTO.class);
    }

    private QueryWrapper<StoreInvoiceEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreInvoiceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public StoreInvoiceDTO getInvoiceDetail(Long storeId) {
        QueryWrapper<StoreInvoiceEntity> wrapper = new QueryWrapper<StoreInvoiceEntity>();
        wrapper.eq("store_id", storeId);
        StoreInvoiceEntity entity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(entity, StoreInvoiceDTO.class);
    }

    @Override
    public boolean update(Wrapper<StoreInvoiceEntity> updateWrapper) {
        return false;
    }

    @Override

    public StoreInvoiceDTO get(Long id) {
        StoreInvoiceEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, StoreInvoiceDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody StoreInvoiceDTO dto) {
        StoreInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, StoreInvoiceEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody StoreInvoiceDTO dto) {
        StoreInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, StoreInvoiceEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, StoreInvoiceEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    @Override
    public StoreInvoiceAddressDTO getStoreInvoiceInfo(Long storeId) {
        StoreInvoiceAddressDTO storeInvoiceAddressDTO = new StoreInvoiceAddressDTO();
        StoreInvoiceEntity storeInvoiceEntity = baseDao.selectOne(Wrappers.<StoreInvoiceEntity>lambdaQuery()
                .eq(StoreInvoiceEntity::getStoreId, storeId)
                .eq(StoreInvoiceEntity::getDelFlag, 0));

        BeanCopier.create(StoreInvoiceEntity.class, StoreInvoiceAddressDTO.class, false)
                .copy(storeInvoiceEntity, storeInvoiceAddressDTO, null);
        return storeInvoiceAddressDTO;
    }

    /**
     * 获取店铺开票设置信息
     *
     * @param storeId
     * @return StoreGoodsInvoiceSettingDTO 店铺开票设置信息
     * @author xuzhch
     * @date 2020年5月14日11:52:47
     */

    @Override
    public StoreGoodsInvoiceSettingDTO getInvoiceSetting(Long storeId) {
        StoreInvoiceEntity storeInvoiceEntity = baseDao.selectOne(Wrappers.<StoreInvoiceEntity>lambdaQuery()
                .eq(StoreInvoiceEntity::getStoreId, storeId).eq(StoreInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL));
        if (BeanUtil.isEmpty(storeInvoiceEntity)) {
            return null;
        }
        StoreGoodsInvoiceSettingDTO settingDTO = new StoreGoodsInvoiceSettingDTO();
        BeanCopier.create(StoreInvoiceEntity.class, StoreGoodsInvoiceSettingDTO.class, false)
                .copy(storeInvoiceEntity, settingDTO, null);
        return settingDTO;
    }
}