/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.member.*;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 会员表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */

public interface MemberService {

    /**
     * 分页查询会员管理列表
     *
     * @param params 查询条件
     * @return 会员管理列表分页数据
     * @author xuzhch
     * @date 2020年09月18日
     */

    PageData<MemberListDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出查询
     *
     * @param params 查询条件
     * @return 汇演列表
     * @author weixianchun
     * @date 2020/1/13 21:03
     */

    PageData<EasyMemberExcelDTO> export(@RequestParam Map<String, Object> params);

    /**
     * 站内信列表会员查询
     *
     * @param params 查询条件
     * @return 会员分页数据
     * @author : 刘远杰
     * @date 2020年09月18日
     */

    PageData<MemberDTO> pageMessage(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberVoDTO selectMemberById(Long id);

    /**
     * 根据id查询会员信息
     *
     * @param id 用户ID
     * @return 用户信息 by id
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberDTO getById(Long id);

    /**
     * 功能描述:
     * 〈获得会员密码〉
     *
     * @param id 会员id
     * @return 会员信息
     * @author : 刘远杰
     * @date 2020年09月18日
     */

    MemberDTO getMemberPassword(Long id);

    /**
     * 根据username查询认证用户信息
     *
     * @param username 用户名
     * @return 认证用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    SecurityDTO selectSecurityUserInfo(@RequestParam("username") String username);

    /**
     * 根据name查询用户id
     *
     * @param username 用户名
     * @return 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberDTO selectMemberByUserName(@RequestParam("username") String username);

    /**
     * 查询列表
     *
     * @param params 查询条件
     * @return list 会员列表信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberVoDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 保存用户信息
     *
     * @param dto 会员注册信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void saveMember(@RequestBody MemberRegisterDTO dto);

    /**
     * 查询用户手机号是否已经注册
     *
     * @param memberMobile 用户手机号
     * @return boolean 查询结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Boolean selectMemberByUsermaneOrMobile(@RequestParam("memberMobile") String memberMobile);

    /**
     * 邮箱是否已经注册
     *
     * @param memberEmail 用户手机邮箱
     * @return boolean 查询结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Boolean selectMemberByMemberEmail(@RequestParam("memberEmail") String memberEmail);

    /**
     * 注册用户保存
     *
     * @param params 用户信息
     * @return map 注册结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> saveMemberLongin(@RequestParam Map<String, Object> params);

    /**
     * 登录
     *
     * @param loginDTO 登录信息
     * @return dto 授权信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    AuthorizationDTO login(@RequestBody LoginDTO loginDTO);

    /**
     * 根据手机号修改密码
     *
     * @param params 密码信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updatePasswordByMobile(@RequestParam Map<String, Object> params);

    /**
     * 根据用户角色类型查询
     *
     * @param roleType 用户角色类型
     * @return 用户信息列表
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberDTO> selectAllMember(@RequestParam(value = "roleType", required = false) Integer roleType);


    /**
     * 修改用户信息
     *
     * @param memberDTO 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updateMember(@RequestBody MemberUpdateDTO memberDTO);

    /**
     * 会员编辑回显
     *
     * @param id 用户ID
     * @return 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberUpdateDTO selectMemberUpdateDTO(Long id);

    /**
     * 删除用户信息
     *
     * @param ids 用户ID数组
     * @author xuzhch
     * @date 2020年09月18日
     */

    void logicDelete(@RequestBody Long[] ids);


    /**
     * 重置密码
     *
     * @param id     会员id
     * @param mobile 手机号
     * @author xuzhch
     * @date 2020年09月18日
     */

    void resetPasswd(@RequestParam("id") Long id, @RequestParam("mobile") String mobile);

    /**
     * 修改用户状态
     *
     * @param id 用户id
     * @return 修改结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> updateState(Long id);

    /**
     * 根据id获取用户基本信息（PC端会员中心数据查询）
     *
     * @param id 用户ID
     * @return 会员信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberPersonCenterDTO selectMember(Long id);

    /**
     * 修改用户信息
     *
     * @param memberDTO 用户信息
     * @return map 修改结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> updatememberById(@RequestBody MemberDTO memberDTO);

    /**
     * 会员基础信息修改
     *
     * @param memberDTO 会员信息
     * @author lixiangx@leimingtech.com
     * @date 2019 /11/11 10:25
     */

    void updateBase(@RequestBody MemberDTO memberDTO);

    /**
     * 根据手机号查询用户信息
     *
     * @param memberMobile 手机号
     * @return 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberPhoneDTO selectByMobile(@RequestParam("memberMobile") String memberMobile);

    /**
     * 根据手机号查询用户详细信息
     *
     * @param mobile :
     * @return MemberVoDTO 用户详细信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberVoDTO getByMobile(@RequestParam("mobile") String mobile);

    /**
     * 更新用户信息
     *
     * @param dto :
     * @return Boolean 更新结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Boolean updateById(@RequestBody MemberDTO dto);

    /**
     * 根据对应的类型添加用户积分和成长值
     *
     * @param params 参数条件
     * @author xuzhch
     * @date 2020年09月18日
     */

    void savePoint(@RequestParam Map<String, Object> params);

    /**
     * 批量查询用户手机号
     *
     * @param memberList 用户ID集合
     * @return 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberDTO> selectPhoneListById(@RequestBody List<Long> memberList);

    /**
     * 首页>基础概况>会员数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexMemberDataDTO 首页用户数据
     * @author xuzhch
     * @date 2020 /4/7/007 12:02
     */

    IndexMemberDataDTO indexMemberData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr);


    /**
     * 根据用户名模糊匹配查询用户信息
     *
     * @param params nickName 用户名
     * @return page data
     * @author pixiaoyong cms新增方法
     * @date 2019/7/23 17:34
     */

    PageData<MemberDTO> selectByNickName(@RequestParam Map<String, Object> params);

    /**
     * 根据会员ID集合查询友盟token信息
     *
     * @param memberIds 会员ID集合
     * @return list 友盟token信息集合
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberUmengTokenInfo> selectUmengTokenByIds(@RequestBody List<Long> memberIds);

    /**
     * 根据微信unionid查询用户数量
     *
     * @param unionid 微信unionid
     * @return 用户数量
     * @author xuzhch
     * @date 2020年09月18日
     */

    Integer getCountByUnionId(String unionid);

    /**
     * PC端个人中心基本信息
     *
     * @param memberId 用户ID
     * @return MemberPersonCenterPcDTO 个人中心基本信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberPersonCenterPcDTO selectPcMemberDetail(Long memberId);


    /**
     * 修改邮箱验证码发送
     *
     * @param email    邮箱
     * @param memberId 会员ID
     * @param codeType 验证码类型
     * @author xuzhch
     * @date 2020年09月18日
     */

    void changeEmailSendCaptcha(@RequestParam("email") String email, @RequestParam("memberId") Long memberId, @RequestParam("codeType") String codeType);

    /**
     * 修改邮箱
     *
     * @param params 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void changeEmail(@RequestParam Map<String, String> params);

    /**
     * 删除友盟token
     *
     * @param deviceToken 友盟token
     * @author xuzhch
     * @date 2020年09月18日
     */

    void logoutDeviceToken(String deviceToken);

    /**
     * 删除友盟token
     *
     * @param memberId 用户ID
     * @author xuzhch
     * @date 2020年09月18日
     */

    void logout(Long memberId);

    /**
     * 微信登录
     *
     * @param code 微信code码
     * @return map
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> wechatLogin(@RequestParam("code") String code);

    /**
     * 用户绑定微信
     *
     * @param code      微信code码
     * @param mobile    手机号
     * @param validCode 验证码
     * @return map 绑定结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> wechatBind(@RequestParam(value = "code") String code,
                                   @RequestParam(value = "mobile") String mobile,
                                   @RequestParam(value = "validCode") String validCode);

    /**
     * 查询用户基本信息 （PC接口）
     *
     * @param memberId 用户ID
     * @return 用户基本信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberBaseInfoDTO selectPcMemberBaseInfo(Long memberId);

    /**
     * 绑定邮箱
     *
     * @param params 邮箱参数
     * @author xuzhch
     * @date 2020年09月18日
     */

    void bindEmail(@RequestParam Map<String, String> params);


    /**
     * 修改用户基本信息
     *
     * @param memberUpdateDTO 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updateMemberBaseInfo(@RequestBody MemberUpdateDTO memberUpdateDTO);

    /**
     * 根据用户ID获取用户标签信息
     *
     * @param memberId 用户ID
     * @return 用户标签信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberLabelDTO getUserInfoByUser(@RequestParam("memberId") Long memberId);
}
