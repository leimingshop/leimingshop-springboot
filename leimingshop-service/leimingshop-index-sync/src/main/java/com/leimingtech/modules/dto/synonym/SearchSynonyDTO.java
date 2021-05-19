/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.synonym;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 搜索同义词实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/16 10:02
 **/
@Data
@ToString
public class SearchSynonyDTO implements Serializable {
    private static final long serialVersionUID = -5956372376577006529L;

    /**
     * 主键ID
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 搜索关键字名称
     */
    @FieldInfo
    private String name;

    /**
     * 关键字全拼小写
     */
    @FieldInfo
    private String nameAllPinyin;

    /**
     * 关键字拼音首字母
     */
    @FieldInfo
    private String nameHeadPinyin;
}
