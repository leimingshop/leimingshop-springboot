/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.address.code;

/**
 * 地址信息入参码
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-19 16:05
 **/
public interface AddressCode {

    /**
     * 访问用户地址列表
     */
    String ADDRESS_LIST_CODE = "200101";
    String ADDRESS_LIST_DESC = "访问用户地址列表";

    /**
     * 访问用户地址详情
     */
    String ADDRESS_DETAIL_CODE = "200102";
    String ADDRESS_DETAIL_DESC = "访问用户地址详情";

    /**
     * 保存用户地址
     */
    String ADDRESS_SAVE_CODE = "200103";
    String ADDRESS_SAVE_DESC = "保存用户地址";

    /**
     * 访问修改地址
     */
    String ADDRESS_EDIT_CODE = "200104";
    String ADDRESS_EDIT_DESC = "访问修改地址";

    /**
     * 删除地址
     */
    String ADDRESS_DELETE_CODE = "200105";
    String ADDRESS_DELETE_DESC = "删除地址";


}
