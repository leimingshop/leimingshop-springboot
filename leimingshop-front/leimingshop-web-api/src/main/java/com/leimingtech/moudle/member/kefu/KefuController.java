/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.kefu;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.netease.NeteaseUserInfoDTO;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.netease.NeteaseIMApiService;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kefu")
@Api(tags = "客服")
public class KefuController {

    @Autowired
    private NeteaseIMApiService neteaseIMApiService;

    @Autowired
    private MemberService memberService;

    /**
     * 获取登录用户的网易云信 通行证
     *
     * @return NeteaseAccountVO
     * @date
     * @author kuangweiguo@leimingtech.com
     **/
    @GetMapping("getNeteaseAccount")
    @ApiOperation(value = "客服-获取网易云信凭证")
    public Result<NeteaseAccountVO> getNeteaseAccount() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<NeteaseAccountVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户登录信息
        MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
        // 查询用户详情
        MemberDTO memberDTO = memberService.getById(userDetail.getId());
        NeteaseUserInfoDTO neteaseUserInfoDTO = neteaseIMApiService.getUserInfo(userDetail.getId() + "");
        if (neteaseUserInfoDTO == null) {
            neteaseUserInfoDTO = new NeteaseUserInfoDTO();
            neteaseUserInfoDTO.setAccid(memberDTO.getId() + "");
            neteaseUserInfoDTO.setName(memberDTO.getNickName());
            neteaseUserInfoDTO.setIcon(memberDTO.getMemberAvatar());
            neteaseUserInfoDTO.setToken(memberDTO.getMemberPasswd());
            neteaseIMApiService.createUserToken(neteaseUserInfoDTO);
        }
        NeteaseAccountVO neteaseAccountVO = new NeteaseAccountVO();
        neteaseAccountVO.setAccid(memberDTO.getId() + "");
        neteaseAccountVO.setToken(memberDTO.getMemberPasswd());
        return new Result<NeteaseAccountVO>().ok(neteaseAccountVO);
    }

    @Data
    @ApiModel(description = "NeteaseAccountVO")
    class NeteaseAccountVO {

        @ApiModelProperty("网易云通信ID")
        private String accid;

        @ApiModelProperty("网易云通信Token")
        private String token;
    }

}
