/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.address;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.code.MemberStatusCode;
import com.leimingtech.modules.dao.address.MemberAddressDao;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.entity.address.MemberAddressEntity;
import com.leimingtech.modules.enums.member.MemberAddressEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 会员地址表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Service
public class MemberAddressServiceImpl extends CrudServiceImpl<MemberAddressDao, MemberAddressEntity, MemberAddressDTO> implements MemberAddressService {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(MemberAddressServiceImpl.class);

    @Override
    public QueryWrapper<MemberAddressEntity> getWrapper(Map<String, Object> params) {
        Object id = params.get("id");
        Object memberId = params.get("memberId");
        QueryWrapper<MemberAddressEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(null != id, "id", id);
        wrapper.eq(null != memberId, "member_id", memberId);
        wrapper.orderByDesc("default_flag", "last_selected");
        wrapper.isNotNull("province_id");
        return wrapper;
    }

    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<MemberAddressDTO> page(@RequestParam Map<String, Object> params) {

        PageData<MemberAddressDTO> page = super.page(params);
        List<MemberAddressDTO> addressDTOList = page.getList();
        return page;

    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override

    public List<MemberAddressDTO> list(@RequestParam Map<String, Object> params) {
        List<MemberAddressEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, MemberAddressDTO.class);
    }

    /**
     * 根据id修改
     *
     * @param id
     * @return
     */
    @Override

    public MemberAddressDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存地址
     *
     * @param memberAddressDTO
     */
    @Override

    public void save(@RequestBody MemberAddressDTO memberAddressDTO) {

        MemberAddressEntity memberAddressEntity = ConvertUtils.sourceToTarget(memberAddressDTO, MemberAddressEntity.class);
        insert(memberAddressEntity);
        //设为用户默认地址
        if (memberAddressEntity.getDefaultFlag() == MemberAddressEnum.IS_DEFAULT.value()) {
            this.updateDefaultFlag(memberAddressEntity.getId(), MemberAddressEnum.IS_DEFAULT.value(), memberAddressEntity.getMemberId());
        }

    }

    /**
     * 修改地址
     *
     * @param memberAddressDTO
     */
    @Override

    public void update(@RequestBody MemberAddressDTO memberAddressDTO) {
        super.update(memberAddressDTO);
    }

    /**
     * 逻辑删除
     *
     * @param ids
     */
    @Override

    public void logicDelete(@RequestBody Long[] ids) {

        super.logicDelete(ids);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id删除地址(H5)
     * @Date :2019/7/18 18:48
     * @Param id
     * @Version V1.0
     **/

    @Override
    public void deleteById(@RequestParam("id") Long id) {
        super.deleteById(id);
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 获取用户的默认地址
     * @Date: 2019/6/14 16:45
     * @Version: V1.0
     */
    @Override

    public MemberAddressDTO findDefalutAddress(@RequestParam("memberId") Long memberId) {

        // 封装查询条件
        QueryWrapper<MemberAddressEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        wrapper.eq("default_flag", MemberAddressEnum.IS_DEFAULT.value());

        // 查询封装返回
        MemberAddressEntity memberAddressEntity = baseDao.selectOne(wrapper);

        return ConvertUtils.sourceToTarget(memberAddressEntity, MemberAddressDTO.class);
    }

    /**
     * 设置用户默认地址
     *
     * @param id
     * @param defaultFlag
     */

    @Override
    public void updateDefaultFlag(@RequestParam("id") Long id,
                                  @RequestParam("defaultFlag") Integer defaultFlag,
                                  @RequestParam("memberId") Long memberId) {

        //修改会员地址的状态为非默认
        baseDao.updateDefaultFlagByMemberId(memberId, MemberAddressEnum.IS_NOT_DEFAULT.value());
        baseDao.updateDefaultFlag(id, defaultFlag);
    }


    @Override
    public void updateAddressLastSelected(@RequestParam("addressId") Long addressId, @RequestParam("memberId") Long memberId) {
        //修改会员地址的状态为未选中
        baseDao.updateLastSelectedByMemberId(memberId, MemberAddressEnum.UNSELECTED.value());
        //设置选中状态
        baseDao.updateSettingSelected(addressId, MemberAddressEnum.LAST_SELECTED.value());

    }


    @Override
    public MemberAddressDTO getLastSelected(@RequestParam("memberId") Long memberId) {
        MemberAddressEntity memberAddressEntity = null;
        try {
            memberAddressEntity = baseDao.selectOne(Wrappers.<MemberAddressEntity>lambdaQuery()
                    .eq(MemberAddressEntity::getMemberId, memberId)
                    .eq(MemberAddressEntity::getLastSelected, MemberAddressEnum.LAST_SELECTED.value())
                    .eq(MemberAddressEntity::getDelFlag, DelFlagEnum.NORMAL.value())
            );
            if (BeanUtil.isEmpty(memberAddressEntity)) {
                List<MemberAddressEntity> memberAddressEntities = baseDao.selectList(Wrappers.<MemberAddressEntity>lambdaQuery()
                        .eq(MemberAddressEntity::getMemberId, memberId)
                        .eq(MemberAddressEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
                if (CollectionUtils.isNotEmpty(memberAddressEntities)) {
                    memberAddressEntity = memberAddressEntities.get(0);
                    this.updateAddressLastSelected(memberAddressEntity.getId(), memberId);
                }
            }
        } catch (Exception e) {
            logger.warn(MemberStatusCode.GET_MEMBER_LAST_SELECTED_EXCEPTION.getCode(),
                    MemberStatusCode.GET_MEMBER_LAST_SELECTED_EXCEPTION.getMessage(), e.getCause());
            return null;
        }
        return ConvertUtils.sourceToTarget(memberAddressEntity, MemberAddressDTO.class);
    }
}
