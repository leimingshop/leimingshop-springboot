/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.search;

import com.leimingtech.modules.constants.mongodb.CollectionName;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户搜索记录保存对象
 *
 * @author xuzhch
 * @className SearchRecordMongoEntity
 * @description
 * @date 2020/3/10/010
 */
@Data
@Document(collection = CollectionName.BROWSE_RECORD)
public class SearchRecordMongoEntity implements Serializable {
    private Long id;
    private Long memberId;
    private String searchIp;
    private String keyword;
    private Date createTime;
    private Date createDayTime;
    private Date createMonthTime;

}
