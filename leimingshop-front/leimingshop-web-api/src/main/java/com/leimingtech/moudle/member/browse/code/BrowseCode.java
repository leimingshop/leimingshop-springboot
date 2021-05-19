/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.browse.code;

/**
 * 浏览记录入参码
 *
 * @author xuzhch
 * @return
 * @date 2020-05-19 16:05
 **/
public interface BrowseCode {


    /**
     * 访问用户足迹列表
     */
    String BROWSE_PAGE_CODE = "200301";
    String BROWSE_PAGE_DESC = "访问用户足迹列表";
    /**
     * 访问用户中心足迹列表
     */
    String BROWSE_LIST_CODE = "200302";
    String BROWSE_LIST_DESC = "访问用户中心足迹列表";
    /**
     * 访问新增浏览记录
     */
    String BROWSE_SAVE_CODE = "200303";
    String BROWSE_SAVE_DESC = "访问新增浏览记录";
    /**
     * 访问删除浏览记录
     */
    String BROWSE_DETELE_CODE = "200304";
    String BROWSE_DETELE_DESC = "访问删除浏览记录";
}
