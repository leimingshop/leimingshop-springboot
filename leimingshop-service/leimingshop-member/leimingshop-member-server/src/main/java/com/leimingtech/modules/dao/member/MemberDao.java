/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.member.*;
import com.leimingtech.modules.entity.member.MemberEntity;
import com.leimingtech.modules.vo.member.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会员表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Mapper
public interface MemberDao extends BaseDao<MemberEntity> {

    /**
     * 查询列表
     *
     * @param params 查询条件
     * @return 用户信息集合
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberVo> getList(@Param("params") Map<String, Object> params);

    /**
     * 分页
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return page 用户信息集合
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberVo> getPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 导出会员列表查询
     *
     * @param params 导出条件
     * @return 列表数据
     * @author weixianchun
     * @date 2020/1/14 13:54
     */
    List<EasyMemberExcelDTO> findListExport(Page page, @Param("params") Map<String, Object> params);

    /**
     * 根据id查询用户详情
     *
     * @param id 用户ID
     * @return vo 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberVo selectMemberById(Long id);


    /**
     * 根据用户名称返回用户信息
     *
     * @param username 用户名
     * @return entity 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberEntity selectMemberByUserName(@Param("username") String username);


    /**
     * 根据name查询用户认证信息
     *
     * @param userName 用户名
     * @return 用户认证信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    SecurityDTO selectSecurityUserInfo(@Param("userName") String userName);

    /**
     * 根据用户名或手机号查询是否存在
     *
     * @param memberMobile 手机号
     * @return boolean  是否存在此用户结果
     * @author xuzhch
     * @date 2020年09月18日
     */
    Boolean selectMemberByUsermaneOrMobile(String memberMobile);

    /**
     * 根据邮箱查询是否被注册过
     *
     * @param memberEmail 邮箱
     * @return 查询用户是否存在
     * @author xuzhch
     * @date 2020年09月18日
     */
    Boolean selectMemberByMemberEmail(String memberEmail);


    /**
     * 验证用户名密码
     *
     * @param memberName 用户名
     * @return entity
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberEntity findByName(@Param("memberName") String memberName);


    /**
     * 根据手机号修改密码
     *
     * @param mobile 手机号
     * @param passwd 密码
     * @author xuzhch
     * @date 2020年09月18日
     */
    void updatePasswordByMobile(@Param("mobile") String mobile, @Param("passwd") String passwd);

    /**
     * 根据Unionid查询
     *
     * @param unionid 微信unionID
     * @return entity 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberEntity selectByUnionid(@Param("unionid") String unionid);

    /**
     * 根据微博uid查询
     *
     * @param weiboUid 微博UID
     * @return entity 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberEntity selectByWeiboUid(@Param("weiboUid") String weiboUid);

    /**
     * 根据id查询数据
     *
     * @param id 用户ID
     * @return 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberUpdateDTO selectMemberUpdateDTO(Long id);

    /**
     * 根据手机号查询用户昵称、头像、用户名
     *
     * @param memberMobile 手机号
     * @return MemberPhoneDTO 用户昵称、头像、用户名
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberPhoneDTO selectByMobile(String memberMobile);

    /**
     * 批量查询用户手机号
     *
     * @param memberList 用户ID集合
     * @return 用户信息集合
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberDTO> selectPhoneListById(@Param("list") List<Long> memberList);

    /**
     * 查询一段时间内用户数量分析结果
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 首页用户数据
     * @author xuzhch
     * @date 2020年09月18日
     */
    IndexMemberDataDTO selectIndexMemberData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    /**
     * 根据用户名分页查询用户信息
     *
     * @param page   分页信息
     * @param params nikeName 用户昵称
     * @return list
     * @author pixiaoyong
     * @date 2020年09月18日
     * @Date :2020/4/7 17:34
     */
    List<MemberEntity> selectMemberList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 根据用户ID集合查询友盟token信息
     *
     * @param memberIds 用户ID集合
     * @return list 用户友盟token信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberUmengTokenInfo> selectUmengTokenByIds(@Param("memberIds") List<Long> memberIds);

    /**
     * 查询用户标签信息
     *
     * @param memberId the member id
     * @return MemberLabelDTO 用户标签信息
     * @author xuzhch
     * @date 2020年09月18日
     */
    MemberLabelDTO getUserInfoById(@Param("memberId") Long memberId);
}
