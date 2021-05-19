/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.group;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.group.GroupRecordDTO;
import com.leimingtech.modules.entity.group.GroupRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 拼团记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Mapper
public interface GroupRecordDao extends BaseDao<GroupRecordEntity> {

    /**
     * 查询h5拼团记录列表
     *
     * @return
     * @date 2020-03-31 18:49
     * @author huangkeyuan@leimingtech.com
     **/
    List<GroupRecordDTO> selectH5GroupRecord(@RequestParam Map<String, Object> params);

}
