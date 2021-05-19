/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.transport.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.transport.TransportCompanyDao;
import com.leimingtech.modules.dto.transport.TransportCompanyDTO;
import com.leimingtech.modules.entity.transport.TransportCompanyEntity;
import com.leimingtech.modules.service.transport.TransportCompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 物流公司表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */
@SuppressWarnings("ALL")
@Service
@Transactional(rollbackFor = Exception.class)

public class TransportCompanyServiceImpl extends BaseServiceImpl<TransportCompanyDao, TransportCompanyEntity> implements TransportCompanyService {

    @Override

    public PageData<TransportCompanyDTO> page(@RequestParam Map<String, Object> params) {
        IPage<TransportCompanyEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, TransportCompanyDTO.class);
    }

    @Override

    public List<TransportCompanyDTO> list(@RequestParam Map<String, Object> params) {
        List<TransportCompanyEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, TransportCompanyDTO.class);
    }

    private QueryWrapper<TransportCompanyEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<TransportCompanyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public TransportCompanyDTO get(Long id) {
        TransportCompanyDTO transportCompanyDTO = new TransportCompanyDTO();
        TransportCompanyEntity entity = baseDao.selectById(id);
        BeanCopier.create(TransportCompanyEntity.class, TransportCompanyDTO.class, false).copy(entity, transportCompanyDTO, null);
        return transportCompanyDTO;
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")

    @Override
    public void updateBatchByCompanyId(@RequestBody List<TransportCompanyDTO> dtoList) {
        List<TransportCompanyEntity> transportCompanyEntities = ConvertUtils.sourceToTarget(dtoList, TransportCompanyEntity.class);
        super.updateBatchById(transportCompanyEntities);

    }

}
