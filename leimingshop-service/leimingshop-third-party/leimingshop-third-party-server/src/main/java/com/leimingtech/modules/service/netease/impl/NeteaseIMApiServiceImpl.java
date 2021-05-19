/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.netease.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dto.netease.NeteaseUserInfoDTO;
import com.leimingtech.modules.service.BaseServiceImpl;
import com.leimingtech.modules.service.netease.NeteaseIMApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 网易云信IM 接口
 */
@Slf4j
@Service
@Transactional
@Api
public class NeteaseIMApiServiceImpl extends BaseServiceImpl implements NeteaseIMApiService {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(NeteaseIMApiServiceImpl.class);

    /**
     * 创建用户 token
     *
     * @param neteaseUserInfoDTO
     * @return token
     * @
     */

    @ApiOperation("createUserToken")
    @Override
    public String createUserToken(@RequestBody NeteaseUserInfoDTO neteaseUserInfoDTO) {

        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.CREATE_USER_TOKEN, JSON.toJSONString(neteaseUserInfoDTO, SerializerFeature.QuoteFieldNames));
        } catch (IOException e) {
            mlogger.error("", "createUserToken request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            return ((JSONObject) jsonObject.getJSONObject("info")).getString("token");
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return null;
        }
    }

    /**
     * 修改用户token
     *
     * @param accid
     * @param token
     * @return 修改是否成功
     * @
     */

    @ApiOperation("updateUserToken")
    @Override
    public boolean updateUserToken(@RequestParam String accid, @RequestParam(required = false) String token) {
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("accid", accid);
        if (!StringUtils.isEmpty(token)) {
            paramMap.put("token", token);
        }
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.UPDARE_USER_TOKEN, paramMap);

        } catch (IOException e) {
            mlogger.error("", "updateUserToken request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            return true;
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return false;
        }
    }

    /**
     * 刷新用户token
     *
     * @param accid
     * @return
     * @
     */

    @ApiOperation("refreshUserToken")
    @Override
    public String refreshUserToken(@RequestParam String accid) {
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("accid", accid);
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.REFRESH_USER_TOKEN, paramMap);
        } catch (IOException e) {
            mlogger.error("", "refreshUserToken request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            return ((JSONObject) jsonObject.getJSONObject("info")).getString("token");
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return null;
        }
    }

    /**
     * 锁定用户
     *
     * @param accid
     * @param needkick
     * @param kickNotifyExt
     * @return
     * @
     */

    @ApiOperation("blockUser")
    @Override
    public boolean blockUser(@RequestParam String accid, @RequestParam(required = false) String needkick, @RequestParam(required = false) String kickNotifyExt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accid", accid);
        if (!StringUtils.isEmpty(needkick)) {
            paramMap.put("needkick", needkick);
        }
        if (!StringUtils.isEmpty(kickNotifyExt)) {
            paramMap.put("kickNotifyExt", kickNotifyExt);
        }
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.BLOCK_USER, paramMap);
        } catch (IOException e) {
            mlogger.error("", "blockUser request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            return true;
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return false;
        }
    }

    /**
     * 解锁用户
     *
     * @param accid
     * @return
     * @
     */

    @ApiOperation("unBlockUser")
    @Override
    public boolean unBlockUser(@RequestParam String accid) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accid", accid);
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.UNBLOCK_USER, paramMap);
        } catch (IOException e) {
            mlogger.error("", "unBlockUser request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            return true;
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return false;
        }
    }

    /**
     * 修改用信息
     *
     * @param neteaseUserInfoDTO
     * @return
     * @
     */

    @ApiOperation("updateUserInfo")
    @Override
    public boolean updateUserInfo(@RequestBody NeteaseUserInfoDTO neteaseUserInfoDTO) {
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.UPDATE_USER_INFO, JSON.toJSONString(neteaseUserInfoDTO, SerializerFeature.QuoteFieldNames));
        } catch (IOException e) {
            mlogger.error("", "updateUserInfo request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            return true;
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return false;
        }
    }

    /**
     * 批量获取用户信息
     *
     * @param accids
     * @return
     * @
     */

    @ApiOperation("getUserInfos")
    @Override
    public List<NeteaseUserInfoDTO> getUserInfos(@RequestParam List<String> accids) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accids", JSON.toJSON(accids));
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.GET_USER_INFOS, paramMap);
        } catch (IOException e) {
            mlogger.error("", "getUserInfos request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            JSONArray uinfos = jsonObject.getJSONArray("uinfos");
            return uinfos.toJavaList(NeteaseUserInfoDTO.class);
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return null;
        }
    }

    /**
     * 批量获取用户信息
     *
     * @param accid
     * @return
     * @
     */

    @ApiOperation("getUserInfo")
    @Override
    public NeteaseUserInfoDTO getUserInfo(String accid) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accids", "[" + accid + "]");
        String resultContent = null;
        try {
            resultContent = this.formPost(NeteaseAPI.GET_USER_INFOS, paramMap);
        } catch (IOException e) {
            mlogger.error("", "getUserInfo request error", e);
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(resultContent);
        if (jsonObject.getIntValue("code") == 200) {
            JSONArray uinfos = jsonObject.getJSONArray("uinfos");
            List<NeteaseUserInfoDTO> list = uinfos.toJavaList(NeteaseUserInfoDTO.class);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        } else {
            mlogger.error(jsonObject.getIntValue("code") + "", jsonObject.getString("desc"));
            return null;
        }
    }
}

