/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.sys;

import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.service.sys.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 资源管理
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("resource")
@Api(tags = "资源管理")
public class SysResourceController {
    @Autowired
    private SysResourceService sysResourceService;

    /**
     * 获取所有资源列表
     */
    @GetMapping("list")
    @ApiOperation("获取所有资源列表")
    public List<ResourceBO> list() {
        return sysResourceService.getResourceList();
    }

    /**
     * 获取所有资源列表
     */
    @GetMapping("listByUser")
    @ApiOperation("获取用户所有资源列表")
    public List<ResourceBO> listByUser(@ApiIgnore UserDetail user) {
        return sysResourceService.getUserResourceList(user.getId());
    }

}