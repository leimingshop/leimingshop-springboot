/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.task;

/**
 * 定时任务接口，所有定时任务都要实现该接口
 *
 * @since 1.0.0
 */
public interface Job {

    /**
     * 执行定时任务接口
     *
     * @param params 参数，多参数使用JSON数据
     */
    void run(String params);
}
