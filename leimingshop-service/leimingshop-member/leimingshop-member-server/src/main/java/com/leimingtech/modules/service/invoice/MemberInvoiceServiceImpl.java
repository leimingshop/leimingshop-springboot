/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.invoice;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.invoice.MemberInvoiceDao;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import com.leimingtech.modules.dto.invoice.MemberInvoiceNameDTO;
import com.leimingtech.modules.entity.invoice.MemberInvoiceEntity;
import com.leimingtech.modules.enums.invoice.InvoiceStatusCode;
import net.sf.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-26
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class MemberInvoiceServiceImpl extends BaseServiceImpl<MemberInvoiceDao, MemberInvoiceEntity> implements MemberInvoiceService {

    private static final Integer MAX_INVOICE_COUNT = 5;

    /**
     * 分页查询发票信息列表
     *
     * @param params 查询条件
     * @return 发票信息列表分页数据
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public PageData<MemberInvoiceDTO> page(@RequestParam Map<String, Object> params) {
        IPage<MemberInvoiceEntity> page = baseDao.selectPage(
                getPage(params, null, false),
                getWrapper(params)
        );

        return getPageData(page, MemberInvoiceDTO.class);
    }

    /**
     * 查询发票信息列表
     *
     * @param params 查询条件
     * @return list 发票信息列表
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public List<MemberInvoiceDTO> list(@RequestParam Map<String, Object> params) {
        List<MemberInvoiceEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, MemberInvoiceDTO.class);
    }

    private QueryWrapper<MemberInvoiceEntity> getWrapper(Map<String, Object> params) {
        Object id = params.get("id");

        QueryWrapper<MemberInvoiceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq(null != params.get("memberId"), "member_id", params.get("memberId"));
        wrapper.orderByDesc("default_flag", "update_date");

        return wrapper;
    }

    /**
     * 根据发票信息ID获取用户发票信息
     *
     * @param id 发票信息ID
     * @return 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override

    public MemberInvoiceDTO get(Long id) {
        MemberInvoiceEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, MemberInvoiceDTO.class);
    }

    /**
     * 保存发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody MemberInvoiceDTO dto) {
        MemberInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, MemberInvoiceEntity.class);

        insert(entity);
    }

    /**
     * 修改发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody MemberInvoiceDTO dto) {
        MemberInvoiceEntity entity = ConvertUtils.sourceToTarget(dto, MemberInvoiceEntity.class);

        updateById(entity);
    }

    /**
     * 根据发票ID数组删除发票信息
     *
     * @param ids 发票ID数组
     * @author xuzhch
     * @date 2020年09月18日
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, MemberInvoiceEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 获取发票抬头名称列表
     *
     * @param memberId 用户ID
     * @return 发票抬头名称列表
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public List<MemberInvoiceNameDTO> getInvoiceNames(Long memberId) {
        return baseDao.selectInvoiceNames(memberId);
    }

    /**
     * 保存或修改发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public void saveOrUpdate(@RequestBody MemberInvoiceDTO dto) {
        MemberInvoiceEntity memberInvoiceEntity = new MemberInvoiceEntity();
        BeanCopier.create(MemberInvoiceDTO.class, MemberInvoiceEntity.class, false)
                .copy(dto, memberInvoiceEntity, null);

        MemberInvoiceEntity entity = baseDao.selectOne(Wrappers.<MemberInvoiceEntity>lambdaQuery()
                .eq(MemberInvoiceEntity::getCompany, dto.getCompany())
                .eq(MemberInvoiceEntity::getDutyParagraph, dto.getDutyParagraph())
                .eq(MemberInvoiceEntity::getMemberId, dto.getMemberId())
                .eq(MemberInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (!BeanUtil.isEmpty(entity)) {
            memberInvoiceEntity.setId(entity.getId());
        }
        this.changeDefaultInvoice(memberInvoiceEntity);
        //编辑
        if (null != memberInvoiceEntity.getId()) {
            super.update(Wrappers.<MemberInvoiceEntity>lambdaUpdate()
                    .set(MemberInvoiceEntity::getCompany, memberInvoiceEntity.getCompany())
                    .set(MemberInvoiceEntity::getDutyParagraph, memberInvoiceEntity.getDutyParagraph())
                    .set(MemberInvoiceEntity::getRegisteredAddress, memberInvoiceEntity.getRegisteredAddress())
                    .set(MemberInvoiceEntity::getOfficePhone, memberInvoiceEntity.getOfficePhone())
                    .set(MemberInvoiceEntity::getBankName, memberInvoiceEntity.getBankName())
                    .set(MemberInvoiceEntity::getBankNumber, memberInvoiceEntity.getBankNumber())
                    .set(null != memberInvoiceEntity.getDefaultFlag(), MemberInvoiceEntity::getDefaultFlag, memberInvoiceEntity.getDefaultFlag())
                    .set(null != memberInvoiceEntity.getVatFlag(), MemberInvoiceEntity::getVatFlag, memberInvoiceEntity.getVatFlag())
                    .eq(MemberInvoiceEntity::getId, memberInvoiceEntity.getId())
            );
            return;
        }
        //查询是否超出最大限制（5个）
        Integer count = baseDao.selectCount(Wrappers.<MemberInvoiceEntity>lambdaQuery()
                .eq(MemberInvoiceEntity::getMemberId, dto.getMemberId())
                .eq(MemberInvoiceEntity::getDelFlag, 0));
        if (count < MAX_INVOICE_COUNT) {
            baseDao.insert(memberInvoiceEntity);
        } else {
            throw new ServiceException(InvoiceStatusCode.INVOICE_OUT_OF_MAX_COUNT);
        }
    }

    private void changeDefaultInvoice(MemberInvoiceEntity memberInvoiceEntity) {
        if (null != memberInvoiceEntity.getDefaultFlag() && memberInvoiceEntity.getDefaultFlag() == 1) {
            super.update(Wrappers.<MemberInvoiceEntity>lambdaUpdate()
                    .set(MemberInvoiceEntity::getDefaultFlag, 0)
                    .eq(MemberInvoiceEntity::getMemberId, memberInvoiceEntity.getMemberId()));
        }
    }

    /**
     * 获取默认发票信息
     *
     * @param memberId 用户ID
     * @return 默认发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public MemberInvoiceDTO getDefaultInvoice(Long memberId) {
        MemberInvoiceEntity memberInvoiceEntity = baseDao.selectOne(Wrappers.<MemberInvoiceEntity>lambdaQuery()
                .eq(MemberInvoiceEntity::getMemberId, memberId)
                .eq(MemberInvoiceEntity::getDefaultFlag, 1)
                .eq(MemberInvoiceEntity::getDelFlag, 0));
        return ConvertUtils.sourceToTarget(memberInvoiceEntity, MemberInvoiceDTO.class);
    }

    /**
     * 修改用户发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public void changeInvoiceInfo(@RequestBody MemberInvoiceDTO dto) {
        MemberInvoiceEntity memberInvoiceEntity = new MemberInvoiceEntity();
        BeanCopier.create(MemberInvoiceDTO.class, MemberInvoiceEntity.class, false)
                .copy(dto, memberInvoiceEntity, null);
        this.changeDefaultInvoice(memberInvoiceEntity);
        MemberInvoiceEntity entity = baseDao.selectOne(Wrappers.<MemberInvoiceEntity>lambdaQuery()
                .eq(MemberInvoiceEntity::getCompany, dto.getCompany())
                .eq(MemberInvoiceEntity::getDutyParagraph, dto.getDutyParagraph())
                .eq(MemberInvoiceEntity::getMemberId, dto.getMemberId())
                .eq(MemberInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        if (!BeanUtil.isEmpty(entity)) {
            memberInvoiceEntity.setId(entity.getId());
            baseDao.updateById(memberInvoiceEntity);
            return;
        }
        List<MemberInvoiceEntity> memberInvoiceEntities = baseDao.selectList(Wrappers.<MemberInvoiceEntity>lambdaQuery()
                .eq(MemberInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL.value())
                .eq(MemberInvoiceEntity::getMemberId, dto.getMemberId())
                .orderByDesc(MemberInvoiceEntity::getDefaultFlag, MemberInvoiceEntity::getUpdateDate));
        //最多只能有5条记录
        if (CollectionUtils.isEmpty(memberInvoiceEntities) || memberInvoiceEntities.size() < MAX_INVOICE_COUNT) {
            baseDao.insert(memberInvoiceEntity);
            return;
        } else {
            if (null == memberInvoiceEntity.getId()) {
                MemberInvoiceEntity invoiceEntity = memberInvoiceEntities.get(memberInvoiceEntities.size() - 1);
                memberInvoiceEntity.setId(invoiceEntity.getId());
            }
            baseDao.updateById(memberInvoiceEntity);
            return;
        }
    }

    /**
     * 根据发票表ID设置为默认发票
     *
     * @param invoiceId 发票表ID
     * @param memberId  用户ID
     * @author xuzhch
     * @date 2020年09月18日
     */

    @Override
    public void settingDefault(@RequestParam("invoiceId") Long invoiceId, @RequestParam("memberId") Long memberId) {
        MemberInvoiceEntity memberInvoiceEntity = new MemberInvoiceEntity();
        memberInvoiceEntity.setMemberId(memberId);
        memberInvoiceEntity.setDefaultFlag(DelFlagEnum.DEL.value());
        this.changeDefaultInvoice(memberInvoiceEntity);
        super.update(Wrappers.<MemberInvoiceEntity>lambdaUpdate().set(MemberInvoiceEntity::getDefaultFlag, DelFlagEnum.DEL.value())
                .eq(MemberInvoiceEntity::getMemberId, memberId)
                .eq(MemberInvoiceEntity::getId, invoiceId)
                .eq(MemberInvoiceEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }
}
