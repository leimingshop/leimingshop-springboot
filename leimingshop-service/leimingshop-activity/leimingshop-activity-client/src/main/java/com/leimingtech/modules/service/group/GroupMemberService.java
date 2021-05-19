/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.group;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.group.GroupMemberDTO;

import java.util.List;
import java.util.Map;

/**
 * 拼团记录用户管理
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */

public interface GroupMemberService {


    PageData<GroupMemberDTO> page(Map<String, Object> params);


    List<GroupMemberDTO> list(Map<String, Object> params);


    GroupMemberDTO get(Long id);


    void save(GroupMemberDTO dto);


    void update(GroupMemberDTO dto);


    void delete(Long[] ids);

    /**
     * 查询拼团记录id关联的会员列表
     *
     * @param id 拼团记录id
     * @return
     * @date 2020-03-30 17:39
     * @author huangkeyuan@leimingtech.com
     **/

    List<GroupMemberDTO> recordList(Long id);

    /**
     * 拼团失败删除会员记录
     *
     * @param memberId      会员id
     * @param groupRecordId 拼团记录id
     * @return
     * @date 2020-04-15 15:09
     * @author huangkeyuan@leimingtech.com
     **/

    void deleteMember(Long memberId, Long groupRecordId);

}
