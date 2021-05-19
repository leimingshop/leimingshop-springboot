/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.storagewarn;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.warning.SaveStorageWarningDTO;
import com.leimingtech.modules.dto.warning.StorageWarningDTO;
import com.leimingtech.modules.dto.warning.UpdateStorageWarningDTO;
import com.leimingtech.modules.service.warning.StorageWarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */
@RestController
@RequestMapping("storagewarning")
@Api(tags = "库存预警管理")
public class StorageWarningController {

    @Autowired
    private StorageWarningService storageWarningService;

    /**
     * 获取预警信息
     *
     * @return
     */
    @GetMapping
    @ApiOperation("信息")
    public Result<StorageWarningDTO> get(@ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<StorageWarningDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        StorageWarningDTO data = storageWarningService.get(sellerDetail.getStoreId());

        return new Result<StorageWarningDTO>().ok(data);
    }

    /**
     * 保存预警信息
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody SaveStorageWarningDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        dto.setStoreId(sellerDetail.getStoreId());
        storageWarningService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改预警信息
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody UpdateStorageWarningDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        storageWarningService.update(dto);

        return new Result().ok(null, "修改成功");
    }

}