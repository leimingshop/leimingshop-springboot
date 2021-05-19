/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.setting;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.setting.reward.RewardSettingDTO;
import com.leimingtech.dto.setting.reward.point.PointUserRuleSetting;
import com.leimingtech.dto.setting.reward.rule.MoreRuleSettingDTO;
import com.leimingtech.enums.setting.PointSettingEnum;
import com.leimingtech.service.setting.PointSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 积分及成长值设置表
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@RestController
@RequestMapping("/point/setting")
@Api(tags = "积分及成长值设置表")
public class PointSettingController {

    @Autowired
    private PointSettingService pointSettingService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取奖励设置信息
     *
     * @date 2019/12/23 15:01
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("reward")
    @ApiOperation("奖励设置信息")
    public Result<RewardSettingDTO> findRewardSetting() {
        RewardSettingDTO reward = pointSettingService.findRewardByName(PointSettingEnum.REWARD_SETTING_NAME.value());
        return new Result<RewardSettingDTO>().ok(reward, "查询设置信息成功");
    }

    /**
     * 奖励奖励设置
     *
     * @param dto: 奖励设置DTO
     * @date 2019/12/23 14:31
     * @author lixiangx@leimingtech.com
     **/
    @PutMapping("reward")
    @ApiOperation("修改奖励设置")
    public Result update(@RequestBody RewardSettingDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        boolean result = pointSettingService.updateUniversal(JSON.toJSONString(dto), PointSettingEnum.REWARD_SETTING_NAME.value());
        if (result) {
            // 删除Redis缓存
            redisUtils.delete(PointSettingEnum.REWARD_SETTING_NAME.value());
            return new Result().ok(null, "奖励设置成功");
        } else {
            return new Result().error("奖励设置失败");
        }
    }


    /**
     * 获取更多规则设置
     *
     * @date 2019/12/23 15:01
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("more/rule")
    @ApiOperation("获取更多规则设置信息")
    public Result<MoreRuleSettingDTO> findMoreRuleSetting() {
        MoreRuleSettingDTO value = pointSettingService.findRuleByName(PointSettingEnum.MORE_RULE_NAME.value());
        return new Result<MoreRuleSettingDTO>().ok(value);
    }

    /**
     * 修改更多规则设置
     *
     * @param dto: 奖励设置DTO
     * @date 2019/12/23 14:31
     * @author lixiangx@leimingtech.com
     **/
    @PutMapping("more/rule")
    @ApiOperation("修改更多规则设置")
    public Result updateMoreRule(@RequestBody MoreRuleSettingDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        boolean result = pointSettingService.updateUniversal(JSON.toJSONString(dto), PointSettingEnum.MORE_RULE_NAME.value());

        if (result) {
            // 删除Redis缓存
            redisUtils.delete(PointSettingEnum.MORE_RULE_NAME.value());

            // 将更多规则设置保存到Redis中，定时清除过期成长值定时任务从Redis中获取清除规则
            Object obj = redisUtils.get(RedisKeys.getClearGrowthKey());
            if (obj == null) {
                redisUtils.set(RedisKeys.getClearGrowthKey(), JSON.toJSONString(dto), RedisUtils.NOT_EXPIRE);
            }

            return new Result().ok(null, "更多规则设置成功");
        } else {
            return new Result().error("更多规则设置失败");
        }
    }


    /**
     * 获取使用设置
     *
     * @date 2019/12/23 15:01
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("user")
    @ApiOperation("获取使用设置信息")
    public Result<PointUserRuleSetting> findUserSetting() {
        PointUserRuleSetting value = pointSettingService.findUserRuleByName(PointSettingEnum.USER_SETTING_NAME.value());
        return new Result<PointUserRuleSetting>().ok(value);
    }

    /**
     * 修改使用设置
     *
     * @param dto: 奖励设置DTO
     * @date 2019/12/23 14:31
     * @author lixiangx@leimingtech.com
     **/
    @PutMapping("user")
    @ApiOperation("修改使用设置")
    public Result updateUserSetting(@RequestBody PointUserRuleSetting dto) {
        // 效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        boolean result = pointSettingService.updateUniversal(JSON.toJSONString(dto), PointSettingEnum.USER_SETTING_NAME.value());
        if (result) {
            // 删除Redis缓存
            redisUtils.delete(PointSettingEnum.USER_SETTING_NAME.value());
            return new Result().ok(null, "使用设置成功");
        } else {
            return new Result().error("使用设置设置失败");
        }
    }
}