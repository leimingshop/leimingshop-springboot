/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dao.goods.spec.GoodsSpecAttrValurAttrDao;
import com.leimingtech.modules.dao.goods.spec.GoodsSpecDao;
import com.leimingtech.modules.dto.spec.GoodsSpecAttrValurAttrDTO;
import com.leimingtech.modules.dto.spec.GoodsSpecListAttrValurDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: tyl
 * @Description:
 * @Date :2019/6/5 9:20
 * @Version V1.0
 **/
@Slf4j
@Service

public class GoodsSpecAttrValurAttrServiceImpl extends CrudServiceImpl<GoodsSpecDao, GoodsSpecEntity, GoodsSpecAttrValurAttrDTO> implements GoodsSpecAttrValurAttrService {


    @Autowired
    private GoodsSpecAttrValurAttrDao goodsSpecAttrValurAttrDao;


    @Override

    public PageData<GoodsSpecAttrValurAttrDTO> findListAttrRel(Map<String, Object> params) {
        IPage<GoodsSpecEntity> page = getPage(params, "", false);
        List<GoodsSpecAttrValurAttrDTO> goodsSpecAttrValurAttrDTOList = goodsSpecAttrValurAttrDao.findListAttrRel(page);
        return getPageData(goodsSpecAttrValurAttrDTOList, page.getTotal(), GoodsSpecAttrValurAttrDTO.class);
    }

    @Override

    public PageData<GoodsSpecListAttrValurDTO> findListSpecAttrRel(@RequestParam Map<String, Object> params) {
        IPage<GoodsSpecEntity> page = getPage(params, "", false);
        List<GoodsSpecListAttrValurDTO> goodsSpecAttrValurAttrDTOList = goodsSpecAttrValurAttrDao.findListSpecAttrRel(page);
        return getPageData(goodsSpecAttrValurAttrDTOList, page.getTotal(), GoodsSpecListAttrValurDTO.class);
    }


    @Override
    public QueryWrapper<GoodsSpecEntity> getWrapper(Map<String, Object> params) {
        //  QueryWrapper<GoodsSpecEntity> wrapper = new QueryWrapper<>();
        return null;
    }
}