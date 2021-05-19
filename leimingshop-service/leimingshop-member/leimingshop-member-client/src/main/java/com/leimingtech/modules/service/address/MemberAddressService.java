/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.address;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 会员地址表
 *
 * @author DY
 * @email 1197793912@qq.como
 * @since 1.0.0 2019-05-10
 */

public interface MemberAddressService {

    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<MemberAddressDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询列表
     *
     * @param params
     * @return
     */

    List<MemberAddressDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */

    MemberAddressDTO get(Long id);

    /**
     * 保存地址
     *
     * @param memberAddressDTO
     */

    void save(@RequestBody MemberAddressDTO memberAddressDTO);

    /**
     * 修改地址
     *
     * @param memberAddressDTO
     */

    void update(@RequestBody MemberAddressDTO memberAddressDTO);

    /**
     * 删除地址
     *
     * @param ids
     */

    void logicDelete(@RequestBody Long[] ids);

    /**
     * 根据ID删除地址信息
     *
     * @param id 地址ID
     * @author xuzhch
     * @date 2020年09月18日
     */

    void deleteById(@RequestParam("id") Long id);

    /**
     * 获取用户的默认地址
     *
     * @param memberId 用户ID
     * @return MemberAddressDTO 用户默认地址
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberAddressDTO findDefalutAddress(@RequestParam("memberId") Long memberId);

    /**
     * 设置用户默认地址
     *
     * @param id          地址主键
     * @param defaultFlag 是否默认（ 默认为0:非默认，1:已默认）
     * @param memberId    用户ID
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updateDefaultFlag(@RequestParam("id") Long id, @RequestParam("defaultFlag") Integer defaultFlag,
                           @RequestParam("memberId") Long memberId);


    void updateAddressLastSelected(@RequestParam("addressId") Long addressId, @RequestParam("memberId") Long memberId);


    MemberAddressDTO getLastSelected(@RequestParam("memberId") Long memberId);
}
