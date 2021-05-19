/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http.apache;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流响应处理器.
 */
public class InputStreamResponseHandler implements ResponseHandler<InputStream> {
    public static final ResponseHandler<InputStream> INSTANCE = new InputStreamResponseHandler();
    private static final int STATUS_CODE_300 = 300;

    @Override
    public InputStream handleResponse(final HttpResponse response) throws IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= STATUS_CODE_300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        return entity == null ? null : entity.getContent();
    }

}
