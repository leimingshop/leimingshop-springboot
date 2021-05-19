/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.aftersale.dao.AftersaleRefundRecordsDao;
import com.leimingtech.modules.aftersale.dto.AftersaleRefundRecordsDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleRefundRecordsEntity;
import com.leimingtech.modules.aftersale.service.AftersaleRefundRecordsService;
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
 * @Author: SWH ab4856812@163.com
 * @Description:售后退款日志
 * @Date: 2019/6/24 9:16
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class AftersaleRefundRecordsServiceImpl extends CrudServiceImpl<AftersaleRefundRecordsDao, AftersaleRefundRecordsEntity, AftersaleRefundRecordsDTO> implements AftersaleRefundRecordsService {

    @Override

    public PageData<AftersaleRefundRecordsDTO> page(@RequestParam Map<String, Object> params) {
        IPage<AftersaleRefundRecordsEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, AftersaleRefundRecordsDTO.class);
    }

    @Override

    public List<AftersaleRefundRecordsDTO> list(Map<String, Object> params) {
        List<AftersaleRefundRecordsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, AftersaleRefundRecordsDTO.class);
    }

    @Override
    public QueryWrapper<AftersaleRefundRecordsEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AftersaleRefundRecordsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.orderByDesc("create_date");
        return wrapper;
    }

    @Override

    public AftersaleRefundRecordsDTO get(Long id) {
        AftersaleRefundRecordsEntity entity = baseDao.selectById(id);
        AftersaleRefundRecordsDTO aftersaleRefundRecordsDTO = new AftersaleRefundRecordsDTO();
        BeanCopier.create(AftersaleRefundRecordsEntity.class, AftersaleRefundRecordsDTO.class, false).copy(entity, aftersaleRefundRecordsDTO, null);
        return aftersaleRefundRecordsDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public boolean saveRecords(@RequestBody AftersaleRefundRecordsDTO dto) {
        AftersaleRefundRecordsEntity entity = new AftersaleRefundRecordsEntity();
        BeanCopier.create(AftersaleRefundRecordsDTO.class, AftersaleRefundRecordsEntity.class, false).copy(dto, entity, null);
        return insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody AftersaleRefundRecordsDTO dto) {
        AftersaleRefundRecordsEntity entity = new AftersaleRefundRecordsEntity();
        BeanCopier.create(AftersaleRefundRecordsDTO.class, AftersaleRefundRecordsEntity.class, false).copy(dto, entity, null);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody String[] ids) {
        //逻辑删除
        //logicDelete(ids, AftersaleRefundRecordsEntity.class)

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<AftersaleRefundRecordsDTO> findPayFail() {
        return baseDao.findPayFail();
    }

}
