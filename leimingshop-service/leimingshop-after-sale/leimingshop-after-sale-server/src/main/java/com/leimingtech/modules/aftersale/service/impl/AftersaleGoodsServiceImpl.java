/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.aftersale.dao.AftersaleGoodsDao;
import com.leimingtech.modules.aftersale.dto.AftersaleGoodsDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleGoodsSaveDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleGoodsEntity;
import com.leimingtech.modules.aftersale.service.AftersaleGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 售后商品
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AftersaleGoodsServiceImpl extends CrudServiceImpl<AftersaleGoodsDao, AftersaleGoodsEntity, AftersaleGoodsDTO> implements AftersaleGoodsService {

    @Resource
    private AftersaleGoodsDao aftersaleGoodsDao;

    @Override
    public QueryWrapper<AftersaleGoodsEntity> getWrapper(Map<String, Object> params) {
        String id = params.get("id").toString();

        QueryWrapper<AftersaleGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "aftersale_sn", id);

        return wrapper;
    }

    /**
     * 保存售后商品
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody AftersaleGoodsSaveDTO dto) {
        AftersaleGoodsEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, AftersaleGoodsEntity.class);
        aftersaleGoodsDao.insert(reasonDescriptionEntity);
    }

    /**
     * 修改售后商品
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody AftersaleGoodsDTO dto) {
        super.update(dto);
    }

    /**
     * 删除售后商品
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID查询售后商品
     *
     * @param id
     * @return
     */
    @Override

    public AftersaleGoodsDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleGoodsDTO>
     * @Description 查询所有的售后商品列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */
    @Override

    public List<AftersaleGoodsDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleGoodsDTO>
     * @Description 分页查询所有的售后商品列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */
    @Override

    public PageData<AftersaleGoodsDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @param aftersaleGoodsSaveList:保存售后商品实体
     * @Author: SWH ab4856812@163.com
     * @Description:批量保存售后商品信息
     * @Date: 2019/6/21 14:58
     * @Version: V1.0
     */
    @Override

    public boolean batchSave(@RequestBody List<AftersaleGoodsSaveDTO> aftersaleGoodsSaveList) {
        List<AftersaleGoodsEntity> goodsSpecAttrEntitiesList = new ArrayList<>();
        for (AftersaleGoodsSaveDTO aftersaleGoodsSaveDTO : aftersaleGoodsSaveList) {
            AftersaleGoodsEntity aftersaleGoodsEntity = new AftersaleGoodsEntity();
            BeanCopier.create(AftersaleGoodsSaveDTO.class, AftersaleGoodsEntity.class, false).copy(aftersaleGoodsSaveDTO, aftersaleGoodsEntity, null);
            goodsSpecAttrEntitiesList.add(aftersaleGoodsEntity);
        }
        return super.insertBatch(goodsSpecAttrEntitiesList);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:批量保存售后商品信息
     * @Date: 2019/6/21 14:58
     * @Version: V1.0
     */
    @Override

    public AftersaleGoodsSaveDTO getAfterGoods(@RequestParam Map<String, Object> map) {
        AftersaleGoodsSaveDTO aftersaleGoodsSaveDTO = new AftersaleGoodsSaveDTO();
        String id = map.get("id").toString();
        String isGift = map.get("is_gift").toString();
        QueryWrapper<AftersaleGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "aftersale_sn", id);
        wrapper.eq(StringUtils.isNotBlank(isGift), "is_gift", isGift);
        AftersaleGoodsEntity aftersaleGoodsEntity = baseDao.selectOne(wrapper);
        BeanCopier.create(AftersaleGoodsEntity.class, AftersaleGoodsSaveDTO.class, false).copy(aftersaleGoodsEntity, aftersaleGoodsSaveDTO, null);
        return aftersaleGoodsSaveDTO;
    }

}
