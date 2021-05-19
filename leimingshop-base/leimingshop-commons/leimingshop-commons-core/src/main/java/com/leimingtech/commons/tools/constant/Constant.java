/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.constant;

/**
 * 常量
 *
 * @since 1.0.0
 */
public interface Constant {

    /**
     * 成功
     */
    int SUCCESS = 1;

    /**
     * 失败
     */
    int FAIL = 0;

    /**
     * OK
     */
    String OK = "OK";

    /**
     * 用户标识
     */
    String USER_KEY = "userId";

    /**
     * 用户名称标识
     */
    String USER_NAME_KEY = "userName";

    /**
     * seller 用户标识
     **/

    String SELLER_USER_KEY = "seller_userId";

    /**
     * 菜单根节点标识
     */
    Long MENU_ROOT = 0L;

    /**
     * 部门根节点标识
     */
    Long DEPT_ROOT = 0L;

    /**
     * 数据字典根节点标识
     */
    Long DICT_ROOT = 0L;

    /**
     * 升序
     */
    String ASC = "asc";

    /**
     * 降序
     */
    String DESC = "desc";

    /**
     * 删除字段名
     */
    String DEL_FLAG = "del_flag";

    /**
     * 创建时间字段名
     */
    String CREATE_DATE = "create_date";

    /**
     * 更新时间字段名
     */
    String UPDATE_DATE = "update_date";

    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sqlFilter";

    /**
     * 当前页码
     */
    String PAGE = "page";

    /**
     * 每页显示记录数
     */
    String LIMIT = "limit";

    /**
     * 排序字段
     */
    String ORDER_FIELD = "orderField";

    /**
     * 排序方式
     */
    String ORDER = "order";

    /**
     * token header
     */
//    String TOKEN_HEADER = "token";

    /**
     * H5 token header
     */
    String H5_TOKEN_HRADER = "Authorization";

    /**
     * admin token header
     */
    String ADMIN_TOKEN_HRADER = "ADMIN-TOKEN";

    /**
     * seller token header
     */
    String SELLER_TOKEN_HEADER = "SELLER-TOKEN";

    /**
     * CODE
     */
    String CODE = "code";
}