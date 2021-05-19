/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.grade;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.grade.MemberGradeDao;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.dto.grade.MemberGradeSaveDTO;
import com.leimingtech.modules.entity.grade.MemberGradeEntity;
import com.leimingtech.modules.service.member.MemberInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 会员等级表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Service

public class MemberGradeServiceImpl extends CrudServiceImpl<MemberGradeDao, MemberGradeEntity, MemberGradeDTO> implements MemberGradeService {

    @Autowired

    private MemberInfoService memberInfoService;

    @Override
    public QueryWrapper<MemberGradeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<MemberGradeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<MemberGradeDTO> page(@RequestParam Map<String, Object> params) {

        //排序
        params.put(Constant.ORDER_FIELD, "integration");
        params.put(Constant.ORDER, "asc");

        //分页
        IPage<MemberGradeEntity> page = getPage(params, "", false);
        List<MemberGradeDTO> memberGradeDTOList = baseDao.selectMemberGrade(page, params);
        for (MemberGradeDTO memberGradeDTO : memberGradeDTOList) {
            Integer max = baseDao.selectMinNum(memberGradeDTO.getId());
            Long memberNum = memberInfoService.selectMemberNum(memberGradeDTO.getIntegration(), max);
            memberGradeDTO.setMemeberNum(memberNum);
            memberGradeDTO.setMaxIntegration(max);
        }
        //查询
        return getPageData(memberGradeDTOList, page.getTotal(), MemberGradeDTO.class);
    }

    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    @Override

    public List<MemberGradeDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override

    public MemberGradeSaveDTO getMember(Long id) {
        MemberGradeDTO memberGradeDTO = super.get(id);
        MemberGradeSaveDTO memberGradeSaveDTO = ConvertUtils.
                sourceToTarget(memberGradeDTO, MemberGradeSaveDTO.class);
        Integer max = baseDao.selectMinNum(id);
        memberGradeSaveDTO.setMaxIntegration(max);
        return memberGradeSaveDTO;
    }

    /**
     * 保存
     *
     * @param memberGradeSaveDTO
     */
    @Override

    public void save(@RequestBody MemberGradeSaveDTO memberGradeSaveDTO) {
        MemberGradeDTO memberGradeDTO = ConvertUtils.sourceToTarget(memberGradeSaveDTO, MemberGradeDTO.class);
        super.save(memberGradeDTO);
    }

    /**
     * 修改
     *
     * @param memberGradeDTO
     */
    @Override

    public void update(@RequestBody MemberGradeDTO memberGradeDTO) {
        super.update(memberGradeDTO);
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
     * 会员等级状态修改
     *
     * @param id
     */
    @Override

    public void updateState(Long id) {
        MemberGradeEntity memberGradeEntity = baseDao.selectById(id);
        String msg = "";
        if (ObjectUtil.isNotNull(memberGradeEntity)) {
            //todo 待添加逻辑
        } else {
            msg = "用户等级不存在";
        }
    }

    /**
     * 查询用户对应等级
     */

    @Override
    public MemberGradeDTO selectByMemberId(Integer integration) {
        MemberGradeEntity memberGradeEntity = baseDao.selectByMemberId(integration);
        return ConvertUtils.sourceToTarget(memberGradeEntity, MemberGradeDTO.class);
    }

    /**
     * 校验等级名称是否重复
     *
     * @param gradeName   等级名称
     * @param integration 等级积分
     * @param gradeId     等级ID
     * @return
     */
    @Override

    public Integer findNameCount(@RequestParam(value = "gradeName", required = false) String gradeName,
                                 @RequestParam(value = "integration", required = false) Integer integration,
                                 @RequestParam(value = "id", required = false) Long gradeId) {

        return baseDao.findNameCount(gradeName, integration, gradeId);
    }


}