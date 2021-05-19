/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.grade;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.dto.grade.MemberGradeSaveDTO;
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

public interface MemberGradeService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<MemberGradeDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 列表
     *
     * @param params
     * @return
     */

    List<MemberGradeDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */

    MemberGradeSaveDTO getMember(Long id);

    /**
     * 新增用户等级
     *
     * @param memberGradeDTO
     */

    void save(@RequestBody MemberGradeSaveDTO memberGradeDTO);

    /**
     * 修改用户等级
     *
     * @param memberGradeDTO
     */

    void update(@RequestBody MemberGradeDTO memberGradeDTO);

    /**
     * 逻辑删除
     *
     * @param ids
     */

    void logicDelete(@RequestBody Long[] ids);

    /**
     * 修改等级状态
     *
     * @param id
     */

    void updateState(Long id);

    /**
     * 查询用户对应等级
     *
     * @param integration 用户积分
     * @return member grade dto
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberGradeDTO selectByMemberId(Integer integration);

    /**
     * 校验等级名称是否重复
     *
     * @param gradeName   等级名称
     * @param integration 等级积分
     * @param gradeId     等级id
     * @return 等级名称数量
     * @author xuzhch
     * @date 2020年09月18日
     */

    Integer findNameCount(@RequestParam(value = "gradeName", required = false) String gradeName,
                          @RequestParam(value = "integration", required = false) Integer integration,
                          @RequestParam(value = "id", required = false) Long gradeId);


}
