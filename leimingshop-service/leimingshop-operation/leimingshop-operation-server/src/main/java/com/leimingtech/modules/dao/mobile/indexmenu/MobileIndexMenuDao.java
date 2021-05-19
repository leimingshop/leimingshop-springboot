/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.mobile.indexmenu;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.mobile.indexmenu.MobileIndexMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 移动首页菜单管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@Mapper
public interface MobileIndexMenuDao extends BaseDao<MobileIndexMenuEntity> {

    /**
     * 功能描述:
     * 〈编辑移动首页菜单〉
     *
     * @param entity 编辑移动首页菜单实体
     * @return 返回编辑成功数量
     * @author : 刘远杰
     */
    int editMobileIndexMenu(MobileIndexMenuEntity entity);

    /**
     * 查询分类id
     *
     * @param url
     * @return
     */
    Long getClassIdByCustomId(String url);
}
