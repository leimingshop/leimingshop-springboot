/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.area;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.area.*;
import com.leimingtech.entity.area.AreaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 地区管理dao
 * @Date :2019/5/28 11:01
 * @Version V1.0
 **/
@Mapper
public interface AreaDao extends BaseDao<AreaEntity> {

    /**
     * @Author: weixianchun
     * @Description: 根据父id查询地区
     * @Date :2019/5/27 17:07
     * @Param parentId
     * @Version V1.0
     **/
    List<AreaDTO> getAreasListByParentId(@Param("areaParentId") Long areaParentId);

    /**
     * 功能描述:
     * 〈g根据地区层级获取所有地区数据
     *
     * @param areaDeep 地区层级
     * @author : 刘远杰
     */
    List<AreaJSDTO> getAreaByAeraDeep(Integer areaDeep);

    /**
     * 功能描述:
     * 查询全部地址
     *
     * @author : swh
     */
    List<AreaJSDTO> getAreaAll();

    /**
     * 查询是否有子地区
     *
     * @param id
     * @return
     */
    Integer selectArea(Long id);

    /**
     * 查询所有地区
     *
     * @return
     */
    List<AreaTreeDTO> selectAll();

    AreaDTO selectParnetId(Long parentId);

    /**
     * 地区分页
     *
     * @param params
     * @return
     */
    List<AreaPageDTO> findPage(Map params);

    /**
     * 查询所有与大区
     *
     * @return
     */
    List<RegionDTO> regionList();

    /**
     * 功能描述：
     * <查询所有与大区转为地区>
     *
     * @param
     * @return
     * @date 2020/4/22 13:07
     * @author 刘远杰
     **/
    List<AreaAndRegionTreeDTO> regionAsAreaList();

    /**
     * 功能描述：
     * <查询所有与地区转为地区>
     *
     * @param
     * @return
     * @date 2020/4/22 13:07
     * @author 刘远杰
     **/
    List<AreaAndRegionTreeDTO> areaAsAreaList();

    /**
     * 功能描述：
     * <查询除四级地区以外地区转为地区>
     *
     * @param
     * @return
     * @date 2020/4/22 13:07
     * @author 刘远杰
     **/
    List<AreaAndRegionTreeDTO> threeAreaAsAreaList();

    List<AreaPcDTO> selectCurrentAndChildren(Long parentId);
}
