/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.check;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.goods.check.GoodsCheckDao;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import com.leimingtech.modules.entity.goods.check.GoodsCheckEntity;
import com.leimingtech.modules.enums.goods.GoodsSpecStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品审核表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-13
 */
@Service

public class GoodsCheckServiceImpl extends CrudServiceImpl<GoodsCheckDao, GoodsCheckEntity, GoodsCheckDTO> implements GoodsCheckService {

    @Override
    public QueryWrapper<GoodsCheckEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsCheckEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<GoodsCheckDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override

    public List<GoodsCheckDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override

    public GoodsCheckDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody GoodsCheckDTO dto) {
        super.save(dto);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody GoodsCheckDTO dto) {
        super.update(dto);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据商品id查询原因
     *
     * @param goodsId
     * @return
     */

    @Override
    public GoodsCheckDTO selectByGoodsId(Long goodsId) {
        GoodsCheckEntity checkEntity = this.baseDao.selectOne(Wrappers.<GoodsCheckEntity>lambdaQuery()
                .eq(GoodsCheckEntity::getGoodsId, goodsId)
                .orderByDesc(GoodsCheckEntity::getCreateDate));
        return ConvertUtils.sourceToTarget(checkEntity, GoodsCheckDTO.class);
    }

    /**
     * 根据商品id查操作记录
     *
     * @param goodsId
     * @return
     */

    @Override
    public List<GoodsCheckDTO> selectByGoodsIdTocheck(Long goodsId) {
        List<GoodsCheckEntity> goodsCheckEntities = this.baseDao.selectList(Wrappers.<GoodsCheckEntity>lambdaQuery()
                .eq(GoodsCheckEntity::getGoodsId, goodsId));
        List<GoodsCheckDTO> goodsCheckDTOList = ConvertUtils.sourceToTarget(goodsCheckEntities, GoodsCheckDTO.class);
        return goodsCheckDTOList;
    }

    /**
     * 保存商户操作记录，并插入系统审核记录
     *
     * @param dto
     * @return
     */
    @Override

    public void saveBatch(@RequestBody GoodsCheckDTO dto, @RequestParam("status") int status) {
        GoodsCheckEntity goodsCheckEntity = new GoodsCheckEntity();
        BeanCopier.create(GoodsCheckDTO.class, GoodsCheckEntity.class, false)
                .copy(dto, goodsCheckEntity, null);
        goodsCheckEntity.setOperationType(3);
        goodsCheckEntity.setCreator("系统");
        // status :  1.商品审核开关关闭的商品   2.自营店铺的商品
        int checkSwitchOff = 1;
        if (status == checkSwitchOff) {
            goodsCheckEntity.setRemarks("审核开关关闭无需审核");
        } else if (status == GoodsSpecStatusEnum.STORE_NO_AUDIT.value()) {
            goodsCheckEntity.setRemarks("自营店铺无需审核");
        }
        GoodsCheckEntity checkEntity = ConvertUtils.sourceToTarget(dto, GoodsCheckEntity.class);
        List<GoodsCheckEntity> list = new ArrayList();
        list.add(checkEntity);
        list.add(goodsCheckEntity);
        super.insertBatch(list);
    }

    @Override

    public void insertBatch(@RequestBody List<GoodsCheckDTO> goodsCheckDTOList) {
        List<GoodsCheckDTO> goodsCheckListDTO = new ArrayList<>(goodsCheckDTOList.size() * 2);
        goodsCheckListDTO.addAll(goodsCheckDTOList);
        for (GoodsCheckDTO goodsCheckDTO : goodsCheckDTOList) {
            GoodsCheckDTO goodsCheck = new GoodsCheckDTO();
            goodsCheck.setId(IdWorker.getId());
            goodsCheck.setOperationType(3);
            goodsCheck.setCreator("系统");
            goodsCheck.setGoodState(goodsCheckDTO.getGoodState());
            goodsCheck.setCreator(goodsCheckDTO.getCreator());
            goodsCheck.setCreateDate(goodsCheckDTO.getCreateDate());
            goodsCheck.setUpdater(goodsCheckDTO.getUpdater());
            goodsCheck.setUpdateDate(goodsCheckDTO.getUpdateDate());
            goodsCheck.setGoodsName(goodsCheckDTO.getGoodsName());
            goodsCheck.setGoodsId(goodsCheckDTO.getGoodsId());
            if (goodsCheckDTO.getStatus() != null && goodsCheckDTO.getStatus() == 1) {
                goodsCheck.setRemarks("审核开关关闭无需审核");
            } else if (goodsCheckDTO.getStatus() != null && goodsCheckDTO.getStatus() == 2) {
                goodsCheck.setRemarks("自营店铺无需审核");
            }
            goodsCheckListDTO.add(goodsCheck);
        }
        baseDao.insertBatch(goodsCheckListDTO);


    }

}
