/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.goods.GoodsInfoDao;
import com.leimingtech.modules.dto.goods.GoodsInfoDTO;
import com.leimingtech.modules.entity.goods.GoodsInfoEntity;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品附加信息表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Service

@Slf4j
public class GoodsInfoServiceImpl extends CrudServiceImpl<GoodsInfoDao, GoodsInfoEntity, GoodsInfoDTO> implements GoodsInfoService {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Override
    public QueryWrapper<GoodsInfoEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<GoodsInfoDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override

    public List<GoodsInfoDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override

    public GoodsInfoDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody GoodsInfoDTO dto) {
        super.save(dto);
    }

    /**
     * 修改用户基本信息
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody GoodsInfoDTO dto) {
        super.update(dto);
    }

    /**
     * 删除用户
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据商品id获取商品详情
     *
     * @param goodsId
     * @return
     */

    @Override
    public GoodsInfoDTO getGoodsInfo(Long goodsId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("goods_id", goodsId);
        GoodsInfoEntity goodsInfoEntity = baseDao.selectOne(wrapper);

        return ConvertUtils.sourceToTarget(goodsInfoEntity, GoodsInfoDTO.class);
    }

    /**
     * 修改商品售出数量
     *
     * @param goodsInfoDTOList
     */

    @Override
    public Integer updateList(@RequestBody List<GoodsInfoDTO> goodsInfoDTOList) {
        return baseDao.updateSaleList(goodsInfoDTOList);
    }

    /**
     * 修改评论次数
     *
     * @param goodsId
     */

    @Override
    public void updateEvalusteNum(@RequestParam("goodsId") Long goodsId) {
        // 更新商品Info表
        GoodsInfoDTO goodsInfoDTO = get(goodsId);
        if (goodsInfoDTO != null) {
            goodsInfoDTO.setCommentNum(goodsInfoDTO.getCommentNum() + 1);
            Integer count = baseDao.updateEvalusteNum(goodsInfoDTO);
            Map<String, Object> map = new HashMap<>(5);
            map.put("goodsId", goodsId);
            map.put("evaluateNum", goodsInfoDTO.getCommentNum());
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_ES_EVALUATE_UPDATE, JSONObject.toJSONString(map));
            if (count == 0) {
                log.error("修改评论次数失败，开始重试，{}", goodsInfoDTO);
                throw new ServiceException("修改评论次数失败");
            }
        } else {
            throw new ServiceException(GoodsStatusCode.UPDATE_EVALUATE_NUM_FAIL);
        }

    }

    /**
     * 功能描述 批量保存
     *
     * @param * @param goodsInfoDTOList
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */
    @Override

    public void saveBatch(@RequestParam("goodsInfoDTOList") List<GoodsInfoDTO> goodsInfoDTOList) {

        baseDao.insertBatch(goodsInfoDTOList);
    }

    /**
     * 功能描述 查找销量排行前二十的产品
     *
     * @author lishuo
     * @date 2020/7/31 15:56
     * @return: java.util.List<java.lang.String>
     **/
    @Override

    public List<String> findTopSaleId() {
        QueryWrapper<GoodsInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sale_num").last("limit 20");
        List<GoodsInfoEntity> goodsInfoEntities = baseDao.selectList(queryWrapper);
        List<Long> collect = goodsInfoEntities.stream().map(GoodsInfoEntity::getId).collect(Collectors.toList());
        List<String> ids = new ArrayList<>();
        collect.forEach(s -> ids.add(s.toString()));
        return ids;
    }

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    @Override
    public void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds) {
        baseDao.delete(Wrappers.<GoodsInfoEntity>lambdaQuery()
                .in(GoodsInfoEntity::getGoodsId, goodsIds)
                .eq(GoodsInfoEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }
}
