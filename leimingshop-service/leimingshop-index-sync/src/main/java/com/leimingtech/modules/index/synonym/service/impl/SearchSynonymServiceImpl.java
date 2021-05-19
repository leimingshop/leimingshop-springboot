/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.synonym.service.impl;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.PinyinHelperUtil;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.dao.synonym.SearchSynonymDao;
import com.leimingtech.modules.dto.synonym.SearchSynonyDTO;
import com.leimingtech.modules.index.synonym.service.SearchSynonymService;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索同义词实现类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/13 17:09
 **/
@Slf4j
@Service
public class SearchSynonymServiceImpl implements SearchSynonymService {

    @Autowired
    private SearchSynonymDao searchSynonymDao;

    @Autowired
    private EsDataUtils esDataUtils;

    /**
     * 进行搜索同义词的同步操作
     *
     * @date 2019/12/13 17:10
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public void synchSynonym() {
        List<String> nameList = new ArrayList<>();

        List<SearchSynonyDTO> dataList = new ArrayList<>();

        // 1、查询展示类目数据（主要获取展示类目的名称）
        List<String> goodsClassNameList = this.findGoodsClassNameList();
        nameList.addAll(goodsClassNameList);

        // 2、查询品牌名称集合数据
        List<String> brandNameList = this.findBrandNameList();
        nameList.addAll(brandNameList);

        // 3、查询店铺名称集合数据
        List<String> storeNameList = this.findStoreNameList();
        nameList.addAll(storeNameList);

        // 4、查询热门搜索词排行前10条

        // 循环遍历获取名称集合的汉语拼音数据
        nameList.forEach(name -> {
            SearchSynonyDTO searchSynonyDTO = new SearchSynonyDTO();
            searchSynonyDTO.setId(IdGenerator.defaultSnowflakeId());
            searchSynonyDTO.setName(name);
            searchSynonyDTO.setNameAllPinyin(PinyinHelperUtil.getPinYinAllChar(name));
            searchSynonyDTO.setNameHeadPinyin(PinyinHelperUtil.getPinYinHeadChar(name));
            dataList.add(searchSynonyDTO);
        });

        //list 空值判断
        if (CollectionUtils.isEmpty(dataList)) {
            log.info("暂无数据更新");
            return;
        }

        esDataUtils.saveDataBatch(IndexContant.SEARCH_KEYWORD_INDEX_NAME, "id",
                JSON.toJSONString(dataList), SearchSynonyDTO.class);
    }

    /**
     * 获取所有展示类目名称集合
     *
     * @return 展示类目名称集合
     * @date 2019/12/13 17:27
     * @author lixiangx@leimingtech.com
     **/
    @Override
//    @DataSource("operation")
    public List<String> findGoodsClassNameList() {
        return searchSynonymDao.findGoodsClassNameList();
    }

    /**
     * 查询品牌名称集合信息
     *
     * @return 品牌名称集合信息
     * @date 2019/12/13 17:34
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public List<String> findBrandNameList() {
        return searchSynonymDao.findBrandNameList();
    }

    /**
     * 查询店铺名称集合信息
     *
     * @return 店铺名称集合数据
     * @date 2019/12/13 17:35
     * @author lixiangx@leimingtech.com
     **/
    @Override
//    @DataSource("store")
    public List<String> findStoreNameList() {
        return searchSynonymDao.findStoreNameList();
    }
}
