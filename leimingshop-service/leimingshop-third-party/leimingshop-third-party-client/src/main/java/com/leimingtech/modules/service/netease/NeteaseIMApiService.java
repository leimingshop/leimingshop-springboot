/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.netease;


import com.leimingtech.modules.dto.netease.NeteaseUserInfoDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;


public interface NeteaseIMApiService {


    /**
     * 创建用户 token
     *
     * @param neteaseUserInfoDTO
     * @return token
     * @throws IOException
     */

    @ApiOperation("createUserToken")
    String createUserToken(@RequestBody NeteaseUserInfoDTO neteaseUserInfoDTO);

    /**
     * 修改用户token
     *
     * @param accid
     * @param token
     * @return 修改是否成功
     * @throws IOException
     */

    @ApiOperation("updateUserToken")
    boolean updateUserToken(@RequestParam String accid, @RequestParam(required = false) String token);

    /**
     * 刷新用户token
     *
     * @param accid
     * @return
     * @throws IOException
     */

    @ApiOperation("refreshUserToken")
    String refreshUserToken(@RequestParam String accid);

    /**
     * 锁定用户
     *
     * @param accid
     * @param needkick
     * @param kickNotifyExt
     * @return
     * @throws IOException
     */

    @ApiOperation("blockUser")
    boolean blockUser(@RequestParam String accid, @RequestParam(required = false) String needkick, @RequestParam(required = false) String kickNotifyExt);

    /**
     * 解锁用户
     *
     * @param accid
     * @return
     * @throws IOException
     */

    @ApiOperation("unBlockUser")
    boolean unBlockUser(@RequestParam String accid);

    /**
     * 修改用信息
     *
     * @param neteaseUserInfoDTO
     * @return
     * @throws IOException
     */

    @ApiOperation("updateUserInfo")
    boolean updateUserInfo(@RequestBody NeteaseUserInfoDTO neteaseUserInfoDTO);

    /**
     * 批量获取用户信息
     *
     * @param accids
     * @return
     * @throws IOException
     */

    @ApiOperation("getUserInfos")
    List<NeteaseUserInfoDTO> getUserInfos(@RequestParam List<String> accids);


    /**
     * 批量单个用户信息
     *
     * @param accid
     * @return
     * @throws IOException
     */

    @ApiOperation("getUserInfo")
    NeteaseUserInfoDTO getUserInfo(@RequestParam String accid);

}
