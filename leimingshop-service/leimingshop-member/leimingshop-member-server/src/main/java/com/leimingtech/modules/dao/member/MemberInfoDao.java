/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.member;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.member.MemberUpdateDTO;
import com.leimingtech.modules.entity.member.MemberInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会员详细信息表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Mapper
public interface MemberInfoDao extends BaseDao<MemberInfoEntity> {

    /**
     * 根据用户id修改
     *
     * @param memberUpdateDTO
     */
    void updateByMemberId(MemberUpdateDTO memberUpdateDTO);


    /**
     * 根据分值查询人数
     *
     * @param min 最小积分
     * @param max 最大积分
     * @return long
     * @author xuzhch
     * @date 2020年09月18日
     */
    Long selectMemberNum(@Param("min") Integer min, @Param("max") Integer max);

    /**
     * 修改会员成长值
     *
     * @param memberId:   会员ID
     * @param gradePoint: 会员等级积分（成长值）
     * @date 2019/12/26 10:34
     * @author lixiangx@leimingtech.com
     **/
    void updateGradePoint(@Param("memberId") Long memberId, @Param("gradePoint") Integer gradePoint);
}
