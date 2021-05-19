/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.group;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拼团记录用户管理表
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_group_member")
public class GroupMemberEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 用户名称
     */
    private String memberName;
    /**
     * 用户头像
     */
    private String memberImage;
    /**
     * 拼团活动id
     */
    private Long groupId;
    /**
     * 拼团记录id
     */
    private Long groupRecordId;
}
