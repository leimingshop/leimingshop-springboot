/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.stopword;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;

/**
 * 广告禁语管理
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@Data
@TableName("lmshop_stop_word")
public class StopWordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;
    /**
     * 敏感词编码
     */
    private String banCode;

    public StopWordEntity() {
    }

    public StopWordEntity(String name) {
        this.name = name;
    }

    public StopWordEntity(String name, String banCode) {
        this.name = name;
        this.banCode = banCode;
    }
}
