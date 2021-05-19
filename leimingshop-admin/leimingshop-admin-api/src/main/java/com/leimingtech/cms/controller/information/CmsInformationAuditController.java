/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.information;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.dto.audit.CmsAuditDTO;
import com.leimingtech.modules.dto.audit.CmsAuditSaveDTO;
import com.leimingtech.modules.service.audit.CmsAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 资讯审核管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2020-03-24
 */
@RestController
@RequestMapping("cms/informationaudit")
@Api(tags = "cms资讯审核管理")
public class CmsInformationAuditController {

    @Autowired
    private CmsAuditService cmsAuditService;

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<CmsAuditDTO> get(@PathVariable("id") Long id) {
        CmsAuditDTO data = cmsAuditService.get(id);
        return new Result<CmsAuditDTO>().ok(data);
    }

    @PostMapping("save")
    @ApiOperation("保存")
    public Result save(@RequestBody CmsAuditSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        cmsAuditService.save(dto);
        return new Result();
    }


}