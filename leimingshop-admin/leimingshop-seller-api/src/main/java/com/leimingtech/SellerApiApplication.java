/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@ComponentScan(basePackages = {"com.leimingtech"}, nameGenerator = BeanUniqueNameGenerator.class,
//        //排除包
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = {
//                ScanExcludeTypeFilter.class,
//        })}
//)
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@ServletComponentScan
@SpringBootApplication
public class SellerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SellerApiApplication.class, args);
    }

}
