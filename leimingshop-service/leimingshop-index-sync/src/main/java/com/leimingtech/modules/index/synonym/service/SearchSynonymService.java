/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.synonym.service;

import java.util.List;

/**
 * 搜索同义词Service
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/13 17:06
 **/
public interface SearchSynonymService {

    /**
     * 进行搜索同义词的同步操作
     *
     * @date 2019/12/13 17:10
     * @author lixiangx@leimingtech.com
     **/
    void synchSynonym();

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
