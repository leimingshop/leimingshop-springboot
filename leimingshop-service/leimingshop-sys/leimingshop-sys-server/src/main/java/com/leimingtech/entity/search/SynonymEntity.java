/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.search;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 同义词管理PO
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:30
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_synonym")
public class SynonymEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8202254938200810684L;
    /**
     * 词库名称
     */
    private String name;
    /**
     * 0:停用，1：启用
     */
    private Integer state;
}