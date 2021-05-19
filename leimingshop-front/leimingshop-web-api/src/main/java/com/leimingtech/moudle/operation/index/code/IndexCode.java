/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.code;

/**
 * 操作模块入参日志码
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface IndexCode {

    /**
     * 访问首页商品分类
     */
    String INDEX_CUSTOM_CLASS_CODE = "200701";
    String INDEX_CUSTOM_CLASS_DESC = "访问首页商品分类";


    /**
     * 访问热词列表
     */
    String INDEX_HOT_KEY_CODE = "200702";
    String INDEX_HOT_KEY_DESC = "访问热词列表";

    /**
     * 访问首页商品推荐列表
     */
    String INDEX_RECOMMEND_GOODS_CODE = "200703";
    String INDEX_RECOMMEND_GOODS_DESC = "访问首页商品推荐列表";

    /**
     * 访问首页站点信息
     */
    String INDEX_SITE_CODE = "200704";
    String INDEX_SITE_GOODS_DESC = "访问首页站点信息";

    /**
     * 访问首页或者店铺首页轮播图
     */
    String INDEX_ADV_SLIDESHOW_CODE = "200705";
    String INDEX_ADV_SLIDESHOW_DESC = "访问首页或者店铺首页轮播图";

    /**
     * 访问首页或者店铺首页楼层
     */
    String INDEX_ADV_FLOOR_CODE = "200706";
    String INDEX_ADV_FLOOR_DESC = "访问首页或者店铺首页楼层";

    /**
     * 访问帮助中心-一级分类列表
     */
    String HELPCENTER_FIRST_CLASS_CODE = "200707";
    String HELPCENTER_FIRST_CLASS_DESC = "访问帮助中心一级分类列表";

    /**
     * 访问帮助中心-分类树分类集合
     */
    String HELPCENTER_CLASS_TREE_LIST_CODE = "200708";
    String HELPCENTER_CLASS_TREE_LIST_DESC = "访问帮助中心分类树分类集合";

    /**
     * 访问帮助中心-分类下的问题列表
     */
    String HELPCENTER_PAGE_LIST_CODE = "200709";
    String HELPCENTER_PAGE_LIST_DESC = "访问帮助中心分类下的问题列表";

    /**
     * 访问帮助中心-问题详情
     */
    String HELPCENTER_QUESTION_DETAIL_CODE = "200710";
    String HELPCENTER_QUESTION_DETAIL_DESC = "访问帮助中心问题详情";

    /**
     * 访问帮助中心-子分类集合
     */
    String HELPCENTER_CHILDREN_LIST_CODE = "200711";
    String HELPCENTER_CHILDREN_LIST_DESC = "访问帮助中心子分类集合";

    /**
     * 访问帮助中心-子分类集合
     */
    String INDEX_NAVIGATION_CODE = "200712";
    String INDEX_NAVIGATION_DESC = "访问帮助中心子分类集合";

}
