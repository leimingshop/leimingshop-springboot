/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.invoice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.invoice.InvoiceInfoDao;
import com.leimingtech.modules.dto.invoice.InvoiceInfoDTO;
import com.leimingtech.modules.entity.invoice.InvoiceInfoEntity;
import com.leimingtech.modules.service.invoice.InvoiceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 发票信息表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */
@Service
@Transactional

public class InvoiceInfoServiceImpl extends BaseServiceImpl<InvoiceInfoDao, InvoiceInfoEntity> implements InvoiceInfoService {

    @Override

    public PageData<InvoiceInfoDTO> page(@RequestParam Map<String, Object> params) {
        IPage<InvoiceInfoEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, InvoiceInfoDTO.class);
    }

    @Override

    public List<InvoiceInfoDTO> list(Map<String, Object> params) {
        List<InvoiceInfoEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, InvoiceInfoDTO.class);
    }

    private QueryWrapper<InvoiceInfoEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<InvoiceInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public InvoiceInfoDTO get(Long id) {
        InvoiceInfoEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, InvoiceInfoDTO.class);
    }


    @Override
    public List<InvoiceInfoDTO> getDetailInvoiceID(Long invoiceId) {
        QueryWrapper<InvoiceInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("invoice_id", invoiceId);
        List<InvoiceInfoEntity> entityList = baseDao.selectList(wrapper);

        return ConvertUtils.sourceToTarget(entityList, InvoiceInfoDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody InvoiceInfoDTO dto) {
        InvoiceInfoEntity entity = ConvertUtils.sourceToTarget(dto, InvoiceInfoEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody InvoiceInfoDTO dto) {
        InvoiceInfoEntity entity = ConvertUtils.sourceToTarget(dto, InvoiceInfoEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, InvoiceInfoEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    @Override
    public void saveBatch(@RequestBody List<InvoiceInfoDTO> invoiceInfoDTOS) {
        List<InvoiceInfoEntity> invoiceInfoEntities = ConvertUtils.sourceToTarget(invoiceInfoDTOS, InvoiceInfoEntity.class);
        super.insertBatch(invoiceInfoEntities);
    }


    @Override
    public List<Long> selectGoodsIdByInvoiceId(Long id) {
        return baseDao.selectGoodsIdsByInvoiceId(id);
    }

}