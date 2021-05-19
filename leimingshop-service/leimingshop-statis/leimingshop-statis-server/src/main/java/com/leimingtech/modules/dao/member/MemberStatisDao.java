/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.member;

import com.leimingtech.modules.dto.member.MemberGradeNameDTO;
import com.leimingtech.modules.entity.member.MemberGradeStatisEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 会员统计DB查询接口
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Mapper
public interface MemberStatisDao {
    /**
     * 查询会员等级
     *
     * @return list 会员等级数据
     * @author xuzhch
     * @date 2020 -09-18
     */
    List<MemberGradeNameDTO> selectMemberGrade();

    /**
     * 统计会员等级人数
     *
     * @param gradeNameList 统计结果
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberGradeStatisEntity> selectMemberGradeStatis(@Param("gradeNameList") List<MemberGradeNameDTO> gradeNameList);

    /**
     * 查询会员来源人数
     *
     * @param date 日期
     * @return list
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<Map<String, Object>> selectMemberAddCount(String date);
}
