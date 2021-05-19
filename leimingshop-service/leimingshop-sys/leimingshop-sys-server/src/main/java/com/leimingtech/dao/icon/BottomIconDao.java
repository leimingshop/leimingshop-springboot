/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.icon;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.icon.BottomIconDTO;
import com.leimingtech.entity.icon.BottomIconEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * app底部图片配置表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-9
 */
@Mapper
public interface BottomIconDao extends BaseDao<BottomIconEntity> {

    /**
     * 查询所有底部icon
     *
     * @return
     */
    List<BottomIconDTO> all();
}
