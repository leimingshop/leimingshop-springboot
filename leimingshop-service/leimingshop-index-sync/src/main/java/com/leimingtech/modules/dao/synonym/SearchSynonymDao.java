/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.synonym;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 搜索同步DAO
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/13 17:47
 **/
@Mapper
public interface SearchSynonymDao {

    /**
     * 获取所有展示类目名称集合
     *
     * @return 展示类目名称集合
     * @date 2019/12/13 17:27
     * @author lixiangx@leimingtech.com
     **/
    List<String> findGoodsClassNameList();

    /**
     * 查询品牌名称集合信息
     *
     * @return 品牌名称集合信息
     * @date 2019/12/13 17:34
     * @author lixiangx@leimingtech.com
     **/
    List<String> findBrandNameList();

    /**
     * 查询店铺名称集合信息
     *
     * @return 店铺名称集合数据
     * @date 2019/12/13 17:35
     * @author lixiangx@leimingtech.com
     **/
    List<String> findStoreNameList();
}
