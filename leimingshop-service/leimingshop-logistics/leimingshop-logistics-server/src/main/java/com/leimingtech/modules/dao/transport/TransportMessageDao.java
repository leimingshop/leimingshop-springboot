/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.transport;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.transport.TransportMessageDTO;
import com.leimingtech.modules.entity.transport.TransportMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 物流信息表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */
@Mapper
public interface TransportMessageDao extends BaseDao<TransportMessageEntity> {

    /**
     * 查询正在投递中的物流单号
     *
     * @return 物流信息
     * @author: SWH ab4856812@163.com
     * @date: 2019/8/13 22:10
     * @version: V1.0
     */
    List<TransportMessageDTO> getProgressList();
}
