/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.store.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.store.StoreInfoDTO;
import com.leimingtech.modules.dto.store.StoreInvoiceAddressDTO;
import com.leimingtech.modules.service.store.StoreInvoiceService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.moudle.store.code.PcStoreCode;
import com.leimingtech.moudle.store.vo.store.FindEvaluateStoreVO;
import com.leimingtech.moudle.store.vo.store.StoreInfoVO;
import com.leimingtech.moudle.store.vo.store.StoreInvoiceAddressVo;
import com.leimingtech.moudle.store.vo.store.StoreRecommendVO;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("store")
@Api(tags = "店铺")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreInvoiceService storeInvoiceService;

    /**
     * 店铺详情
     *
     * @param storeId
     * @return
     */
    @GetMapping("info/{storeId}")
    @ApiOperation("店铺详情")
    @LogContext(code = PcStoreCode.STORE_INFO_CODE, note = PcStoreCode.STORE_INFO_DESC)
    public Result<StoreInfoVO> selectInfo(@PathVariable("storeId") Long storeId) {
        AssertUtils.isNull(storeId, "店铺Id不能为空");
        // 获取用户信息
        Long userId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            userId = SecurityCurrentUser.getUserDetail().getId();
        }
        StoreInfoDTO storeInfoDTO = storeService.selectStoreInfo(storeId, userId);
        StoreInfoVO storeInfoVO = ConvertUtils.sourceToTarget(storeInfoDTO, StoreInfoVO.class);
        storeInfoVO.setFindEvaluateStoreDTO(ConvertUtils.sourceToTarget(storeInfoDTO.getFindEvaluateStoreDTO(), FindEvaluateStoreVO.class));

        return new Result<StoreInfoVO>().ok(storeInfoVO, "查询成功");
    }

    /**
     * 推荐专区
     *
     * @param storeId
     * @return
     */
    @GetMapping("recommend")
    @ApiOperation("推荐专区")
    @LogContext(code = PcStoreCode.STORE_RECOMMEND_CODE, note = PcStoreCode.STORE_RECOMMEND_DESC)
    public Result<List<StoreRecommendVO>> recommend(@RequestParam("storeId") Long storeId) {
        AssertUtils.isNull(storeId, "店铺Id不能为空");
        // TODO 推荐待查询
        List<StoreRecommendVO> list = new ArrayList<>();
        StoreRecommendVO storeInfoDTO = new StoreRecommendVO();
        storeInfoDTO.setImgUrl("/group1/M00/00/53/wKgAD16esvOANdedAAAovk504kA974.png");
        storeInfoDTO.setName("特惠推荐");
        storeInfoDTO.setTitle("我猜得到你的需要");
        list.add(storeInfoDTO);

        StoreRecommendVO recommendOne = new StoreRecommendVO();
        recommendOne.setImgUrl("/group1/M00/00/54/wKgAD16g_y6AYYYNAABBuQPpf1o55.jpeg");
        recommendOne.setName("人气推荐");
        recommendOne.setTitle("人气好物榜");
        list.add(recommendOne);

        StoreRecommendVO recommendTwo = new StoreRecommendVO();
        recommendTwo.setImgUrl("/group1/M00/00/52/wKgAD16Zs_CAbWYaAAARtfPtTEA482.png");
        recommendTwo.setName("编辑精心整理推荐");
        recommendTwo.setTitle("我猜得到你的需要");
        list.add(recommendTwo);

        StoreRecommendVO recommendThree = new StoreRecommendVO();
        recommendThree.setImgUrl("/group1/M00/00/3D/wKgAD13uEZ-AdAU8AAAGprbL2H4756.png");
        recommendThree.setName("领券中心");
        recommendThree.setTitle("发现更多超值优惠券");
        list.add(recommendThree);


        return new Result<List<StoreRecommendVO>>().ok(list);
    }

    /**
     * 店铺收票地址
     *
     * @param storeId
     * @return
     */
    @GetMapping("invoice/info/{storeId}")
    @ApiOperation("店铺发票设置信息")
    public Result<StoreInvoiceAddressVo> getStoreInvoiceInfo(@PathVariable("storeId") Long storeId) {
        StoreInvoiceAddressDTO storeInvoiceAddressDTO = storeInvoiceService.getStoreInvoiceInfo(storeId);
        StoreInvoiceAddressVo storeInvoiceAddressVo = ConvertUtils.sourceToTarget(storeInvoiceAddressDTO, StoreInvoiceAddressVo.class);
        return new Result<StoreInvoiceAddressVo>().ok(storeInvoiceAddressVo);
    }


}
