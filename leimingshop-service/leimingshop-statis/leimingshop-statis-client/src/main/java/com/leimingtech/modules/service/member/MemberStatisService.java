/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;


import com.leimingtech.modules.dto.member.MemberGradeShowDTO;
import com.leimingtech.modules.dto.member.MemberStatisDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MemberStatisService
 * @Description 会员统计接口
 * @Author xuzhch
 * @Date 2019年12月6日
 * @Version 1.0
 **/

public interface MemberStatisService {

    /**
     * 保存会员统计信息
     *
     * @param params 日期字符串
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2020年09月18日
     */

    void saveMemberStatis(@RequestParam(required = false) String params) throws ParseException;

    /**
     * 保存会员等级统计信息
     *
     * @param params 日期字符串
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2020年09月18日
     */

    void saveMemberGradeStatis(@RequestParam(required = false) String params) throws ParseException;

    /**
     * 会员增长统计展示
     *
     * @param params Map {
     *               (name = "timeType", value = "时间类型(1:年,2:月,3:日)", paramType = "query",defaultValue = "3",dataType = "int" ),
     *               (name = "startDate", value = "查询开始时间", paramType = "query", dataType = "Date" ),
     *               (name = "endDate", value = "查询结束时间", paramType = "query", dataType = "Date" )}
     * @return list
     * @throws ParseException 日期转化异常
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberStatisDTO> memberGrowStatis(@RequestParam Map params) throws ParseException;

    /**
     * 会员等级占比
     *
     * @param params 查询条件
     * @return map
     * @throws ParseException 日期转化异常
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, List<MemberGradeShowDTO>> memberGradeProportion(@RequestParam Map params) throws ParseException;

    /**
     * 会员来源列表统计
     *
     * @param params 查询条件
     * @return list
     * @throws ParseException 日期转换异常
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberStatisDTO> memberSource(@RequestParam Map params) throws ParseException;
}
