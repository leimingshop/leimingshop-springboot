//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.exception;

import java.io.Serializable;

public interface ServiceErrorCode extends Serializable {
    String getCode();

    String getMessage();

    boolean isClientError();

    boolean isServerError();

    int getHttpStatus();
}
