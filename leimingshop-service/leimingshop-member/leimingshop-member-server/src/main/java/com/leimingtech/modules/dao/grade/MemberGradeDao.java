/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.grade;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.entity.grade.MemberGradeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 会员等级表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Mapper
public interface MemberGradeDao extends BaseDao<MemberGradeEntity> {

    /**
     * 根据积分查询对应的等级
     *
     * @param integration
     * @return
     */
    MemberGradeEntity selectByMemberId(Integer integration);

    /**
     * 列表
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return 会员等级集合
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberGradeDTO> selectMemberGrade(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 获取当前等级的范围
     *
     * @param id
     * @return
     */
    Integer selectMinNum(Long id);

    /**
     * 校验等级名称是否重复
     *
     * @param gradeName   等级名称
     * @param integration 等级积分
     * @param gradeId     等级ID
     * @return
     */
    Integer findNameCount(@Param("gradeName") String gradeName,
                          @Param("integration") Integer integration,
                          @Param("gradeId") Long gradeId);

}
