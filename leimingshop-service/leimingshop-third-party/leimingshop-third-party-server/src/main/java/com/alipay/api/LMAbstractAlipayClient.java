//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alipay.api;

import com.alipay.api.internal.parser.json.ObjectJsonParser;
import com.alipay.api.internal.parser.xml.ObjectXmlParser;
import com.alipay.api.internal.util.*;
import com.alipay.api.internal.util.file.FileUtils;
import com.alipay.api.internal.util.file.IOUtils;
import com.alipay.api.internal.util.json.JSONWriter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LMAbstractAlipayClient implements AlipayClient {
    public static final int REPORT_DELAY = 86400;
    private static final String BATCH_API_DEFAULT_SPLIT = "#S#";

    static {
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }

    private String serverUrl;
    private String appId;
    private String prodCode;
    private String format;
    private String signType;
    private String encryptType;
    private String charset;
    private int connectTimeout;
    private int readTimeout;
    private String proxyHost;
    private int proxyPort;
    private SignChecker signChecker;
    private String appCertSN;
    private String alipayCertSN;
    private String alipayRootCertSN;
    private String rootCertContent;
    private X509Certificate cert;
    private ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap;
    private ConcurrentHashMap<String, String> alipayPublicKeyMap;

    public LMAbstractAlipayClient(String serverUrl, String appId, String format, String charset, String signType) {
        this.format = "json";
        this.signType = "RSA";
        this.encryptType = "AES";
        this.connectTimeout = 3000;
        this.readTimeout = 15000;
        this.serverUrl = serverUrl;
        this.appId = appId;
        if (!StringUtils.isEmpty(format)) {
            this.format = format;
        }

        this.charset = charset;
        if (!StringUtils.isEmpty(signType)) {
            this.signType = signType;
        }

    }

    public LMAbstractAlipayClient(String serverUrl, String appId, String format, String charset, String signType, String proxyHost, int proxyPort) {
        this(serverUrl, appId, format, charset, signType);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    public LMAbstractAlipayClient(String serverUrl, String appId, String format, String charset, String signType, String encryptType) {
        this(serverUrl, appId, format, charset, signType);
        if (!StringUtils.isEmpty(encryptType)) {
            this.encryptType = encryptType;
        }

    }

    public LMAbstractAlipayClient(String serverUrl, String appId, String format, String charset, String signType, String certPath, String alipayPublicCertPath, String rootCertPath, String proxyHost, int proxyPort, String encryptType) throws AlipayApiException {
        this(serverUrl, appId, format, charset, signType);
        if (!StringUtils.isEmpty(encryptType)) {
            this.encryptType = encryptType;
        }

        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        try {

            this.rootCertContent = IOUtils.toString(LMAbstractAlipayClient.class.getResourceAsStream(rootCertPath), charset);
            this.alipayRootCertSN = AntCertificationUtil.getRootCertSN(this.rootCertContent);
            if (StringUtils.isEmpty(this.alipayRootCertSN)) {
                throw new AlipayApiException("AlipayRootCert Is Invalid");
            } else {
                this.cert = this.getCert(certPath);
                X509Certificate alipayPublicCert = this.getCert(alipayPublicCertPath);
                this.appCertSN = this.getCertSN(this.cert);
                if (StringUtils.isEmpty(this.appCertSN)) {
                    throw new AlipayApiException("AppCert Is Invalid");
                } else {
                    this.alipayCertSN = this.getCertSN(alipayPublicCert);
                    ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap = new ConcurrentHashMap();
                    alipayPublicCertMap.put(this.alipayCertSN, alipayPublicCert);
                    this.alipayPublicCertMap = alipayPublicCertMap;
                    PublicKey publicKey = alipayPublicCert.getPublicKey();
                    Encoder encoder = Base64.getEncoder();
                    encoder.encodeToString(publicKey.getEncoded());
                    ConcurrentHashMap<String, String> alipayPublicKeyMap = new ConcurrentHashMap();
                    alipayPublicKeyMap.put(this.alipayCertSN, encoder.encodeToString(publicKey.getEncoded()));
                    this.alipayPublicKeyMap = alipayPublicKeyMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fillMD5(String md5) {
        return md5.length() == 32 ? md5 : fillMD5("0" + md5);
    }

    private X509Certificate getCert(String certPath) throws AlipayApiException {
        InputStream inputStream = null;

        X509Certificate var5;
        try {
            inputStream = LMAbstractAlipayClient.class.getResourceAsStream(certPath);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            var5 = cert;
        } catch (CertificateException var15) {
            throw new AlipayApiException(var15);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var16) {
                throw new AlipayApiException(var16);
            }

        }

        return var5;
    }

    private String getCertSN(X509Certificate cf) throws AlipayApiException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((cf.getIssuerX500Principal().getName() + cf.getSerialNumber()).getBytes());
            String certSN = (new BigInteger(1, md.digest())).toString(16);
            certSN = fillMD5(certSN);
            return certSN;
        } catch (NoSuchAlgorithmException var4) {
            throw new AlipayApiException(var4);
        }
    }

    private String readFileToString(String rootCertPath) throws AlipayApiException {
        try {
            File file = new File(rootCertPath);
            String client = FileUtils.readFileToString(file);
            return client;
        } catch (IOException var4) {
            throw new AlipayApiException(var4);
        }
    }

    private boolean verifyCert(String cert) {
        return AntCertificationUtil.isTrusted(cert, this.rootCertContent);
    }

    @Override
    public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request) throws AlipayApiException {
        return this.certificateExecute(request, (String) null);
    }

    @Override
    public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken) throws AlipayApiException {
        return this.certificateExecute(request, accessToken, (String) null);
    }

    @Override
    public <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
        return this._certificateExecute(request, accessToken, appAuthToken);
    }

    public <T extends AlipayResponse> T _certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
        AlipayResponse tRsp = null;

        try {
            long beginTime = System.currentTimeMillis();
            Map<String, Object> rt = this.doPost(request, accessToken, appAuthToken, this.appCertSN);
            Map<String, Long> costTimeMap = new HashMap();
            if (rt.containsKey("prepareTime")) {
                costTimeMap.put("prepareCostTime", (Long) ((Long) rt.get("prepareTime")) - beginTime);
                if (rt.containsKey("requestTime")) {
                    costTimeMap.put("requestCostTime", (Long) ((Long) rt.get("requestTime")) - (Long) ((Long) rt.get("prepareTime")));
                }
            }

            AlipayParser<T> parser = null;
            if ("xml".equals(this.format)) {
                parser = new ObjectXmlParser(request.getResponseClass());
            } else {
                parser = new ObjectJsonParser(request.getResponseClass());
            }

            ResponseEncryptItem responseItem = this.decryptResponse(request, rt, (AlipayParser) parser);
            tRsp = ((AlipayParser) parser).parse(responseItem.getRealContent());
            tRsp.setBody(responseItem.getRealContent());
            this.checkResponseCertSign(request, (AlipayParser) parser, responseItem.getRespContent(), tRsp.isSuccess());
            if (costTimeMap.containsKey("requestCostTime")) {
                costTimeMap.put("postCostTime", System.currentTimeMillis() - (Long) ((Long) rt.get("requestTime")));
            }

            tRsp.setParams((AlipayHashMap) rt.get("textParams"));
            if (!tRsp.isSuccess()) {
                AlipayLogger.logErrorScene(rt, tRsp, "", costTimeMap);
            } else {
                AlipayLogger.logBizSummary(rt, tRsp, costTimeMap);
            }

            return (T) tRsp;
        } catch (RuntimeException var11) {
            AlipayLogger.logBizError(var11);
            throw new AlipayApiException(var11);
        } catch (AlipayApiException var12) {
            AlipayLogger.logBizError(var12);
            throw new AlipayApiException(var12);
        }
    }

    private <T extends AlipayResponse> void checkResponseCertSign(AlipayRequest<T> request, AlipayParser<T> parser, String responseBody, boolean responseIsSucess) throws AlipayApiException {
        CertItem certItem = parser.getCertItem(request, responseBody);
        if (certItem == null) {
            throw new AlipayApiException("cert check fail: Body is Empty!");
        } else if (certItem.getCert() == null && responseIsSucess && !"alipay.open.app.alipaycert.download".equals(request.getApiMethodName())) {
            throw new AlipayApiException("cert check fail: ALIPAY_CERT_SN is Empty!");
        } else {
            String alipayPublicKey = null;
            if (certItem.getCert() != null) {
                if (!this.alipayPublicCertMap.containsKey(certItem.getCert())) {
                    AlipayOpenAppAlipaycertDownloadRequest alipayRequest = new AlipayOpenAppAlipaycertDownloadRequest();
                    alipayRequest.setBizContent("{\"alipay_cert_sn\":\"" + certItem.getCert() + "\"  }");
                    Map<String, Object> rt = this.doPost(alipayRequest, (String) null, (String) null, this.appCertSN);
                    String respContent = rt.get("rsp").toString();
                    AlipayParser<AlipayOpenAppAlipaycertDownloadResponse> parserCert = null;
                    parserCert = new ObjectJsonParser(alipayRequest.getResponseClass());
                    AlipayOpenAppAlipaycertDownloadResponse alipayResponse = (AlipayOpenAppAlipaycertDownloadResponse) parserCert.parse(respContent);
                    if (!alipayResponse.isSuccess()) {
                        throw new AlipayApiException("֧������Կ֤��У��ʧ�ܣ���ȷ���Ƿ�Ϊ֧����ǩ������Ч��Կ֤��");
                    }

                    String alipayCertContent = alipayResponse.getAlipayCertContent();
                    Decoder decoder = Base64.getDecoder();
                    ByteArrayInputStream inputStream = null;

                    try {
                        byte[] alipayCert = decoder.decode(alipayCertContent);
                        String alipayPublicCertStr = new String(alipayCert);
                        if (!this.verifyCert(alipayPublicCertStr)) {
                            throw new AlipayApiException("֧������Կ֤��У��ʧ�ܣ���ȷ���Ƿ�Ϊ֧����ǩ������Ч��Կ֤��");
                        }

                        inputStream = new ByteArrayInputStream(alipayCert);
                        CertificateFactory cf = CertificateFactory.getInstance("X.509");
                        X509Certificate alipayPublicCertNew = (X509Certificate) cf.generateCertificate(inputStream);
                        String alipayCertSNNew = this.getCertSN(alipayPublicCertNew);
                        this.alipayPublicCertMap.put(alipayCertSNNew, alipayPublicCertNew);
                        PublicKey publicKey = alipayPublicCertNew.getPublicKey();
                        Encoder encoder = Base64.getEncoder();
                        String alipayPublicKeyNew = encoder.encodeToString(publicKey.getEncoded());
                        this.alipayPublicKeyMap.put(alipayCertSNNew, alipayPublicKeyNew);
                    } catch (CertificateException var30) {
                        throw new AlipayApiException(var30);
                    } finally {
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        } catch (IOException var31) {
                            throw new AlipayApiException(var31);
                        }

                    }
                } else if (!this.alipayPublicKeyMap.containsKey(certItem.getCert())) {
                    PublicKey publicKey = ((X509Certificate) this.alipayPublicCertMap.get(certItem.getCert())).getPublicKey();
                    Encoder encoder = Base64.getEncoder();
                    encoder.encodeToString(publicKey.getEncoded());
                    this.alipayPublicKeyMap.put(certItem.getCert(), encoder.encodeToString(publicKey.getEncoded()));
                }

                if (!this.alipayPublicCertMap.containsKey(certItem.getCert())) {
                    throw new AlipayApiException("cert check fail: check Cert and Data Fail! CertSN non-existent");
                }

                alipayPublicKey = (String) this.alipayPublicKeyMap.get(certItem.getCert());
                if (responseIsSucess || !responseIsSucess && !StringUtils.isEmpty(certItem.getSign())) {
                    this.signChecker = new DefaultSignChecker(alipayPublicKey);
                    boolean rsaCheckContent = this.signChecker.checkCert(certItem.getSignSourceDate(), certItem.getSign(), this.signType, this.charset, alipayPublicKey);
                    if (!rsaCheckContent) {
                        if (StringUtils.isEmpty(certItem.getSignSourceDate()) || !certItem.getSignSourceDate().contains("\\/")) {
                            throw new AlipayApiException("cert check fail: check Cert and Data Fail!");
                        }

                        String srouceData = certItem.getSignSourceDate().replace("\\/", "/");
                        boolean jsonCheck = this.getSignChecker().check(srouceData, certItem.getSign(), this.signType, this.charset);
                        if (!jsonCheck) {
                            throw new AlipayApiException("cert check fail: check Cert and Data Fail��JSON also��");
                        }
                    }
                }
            }

        }
    }

    @Override
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
        return this.execute(request, (String) null);
    }

    @Override
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken) throws AlipayApiException {
        return this.execute(request, accessToken, (String) null);
    }

    @Override
    public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
        AlipayParser<T> parser = null;
        if ("xml".equals(this.format)) {
            parser = new ObjectXmlParser(request.getResponseClass());
        } else {
            parser = new ObjectJsonParser(request.getResponseClass());
        }

        return (T) this._execute(request, (AlipayParser) parser, accessToken, appAuthToken);
    }

    @Override
    public BatchAlipayResponse execute(BatchAlipayRequest request) throws AlipayApiException {
        long beginTime = System.currentTimeMillis();
        Map<String, Object> rt = this.doPost(request);
        if (rt == null) {
            return null;
        } else {
            Map<String, Long> costTimeMap = new HashMap();
            if (rt.containsKey("prepareTime")) {
                costTimeMap.put("prepareCostTime", (Long) ((Long) rt.get("prepareTime")) - beginTime);
                if (rt.containsKey("requestTime")) {
                    costTimeMap.put("requestCostTime", (Long) ((Long) rt.get("requestTime")) - (Long) ((Long) rt.get("prepareTime")));
                }
            }

            AlipayParser<BatchAlipayResponse> parser = null;
            if ("xml".equals(this.format)) {
                parser = new ObjectXmlParser(request.getResponseClass());
            } else {
                parser = new ObjectJsonParser(request.getResponseClass());
            }

            BatchAlipayResponse batchAlipayResponse = null;

            try {
                ResponseEncryptItem responseItem = this.decryptResponse(request, rt, (AlipayParser) parser);
                batchAlipayResponse = (BatchAlipayResponse) ((AlipayParser) parser).parse(responseItem.getRealContent());
                batchAlipayResponse.setBody(responseItem.getRealContent());
                this.checkResponseSign(request, (AlipayParser) parser, responseItem.getRespContent(), batchAlipayResponse.isSuccess());
                if (costTimeMap.containsKey("requestCostTime")) {
                    costTimeMap.put("postCostTime", System.currentTimeMillis() - (Long) ((Long) rt.get("requestTime")));
                }

                List<AlipayParser<?>> parserList = new ArrayList();
                List<AlipayRequestWrapper> requestList = request.getRequestList();
                int i;
                if ("xml".equals(this.format)) {
                    for (i = 0; i < requestList.size(); ++i) {
                        parserList.add(new ObjectXmlParser(((AlipayRequestWrapper) requestList.get(i)).getAlipayRequest().getResponseClass()));
                    }
                } else {
                    for (i = 0; i < requestList.size(); ++i) {
                        parserList.add(new ObjectJsonParser(((AlipayRequestWrapper) requestList.get(i)).getAlipayRequest().getResponseClass()));
                    }
                }

                if (!batchAlipayResponse.isSuccess()) {
                    return batchAlipayResponse;
                } else {
                    String[] responseArray = batchAlipayResponse.getResponseBody().split("#S#");

                    for (int index = 0; index < responseArray.length; ++index) {
                        AlipayResponse alipayResponse = ((AlipayParser) parserList.get(index)).parse(responseArray[index]);
                        alipayResponse.setBody(responseArray[index]);
                        batchAlipayResponse.addResponse(alipayResponse);
                    }

                    AlipayLogger.logBizDebug((String) rt.get("rsp"));
                    return batchAlipayResponse;
                }
            } catch (RuntimeException var14) {
                AlipayLogger.logBizError((String) rt.get("rsp"), costTimeMap);
                throw var14;
            } catch (AlipayApiException var15) {
                AlipayLogger.logBizError((String) rt.get("rsp"), costTimeMap);
                throw new AlipayApiException(var15);
            }
        }
    }

    private Map<String, Object> doPost(BatchAlipayRequest request) throws AlipayApiException {
        Map<String, Object> result = new HashMap();
        RequestParametersHolder requestHolder = this.getRequestHolderWithSign(request);
        String url = this.getRequestUrl(requestHolder);
        if (AlipayLogger.isBizDebugEnabled()) {
            AlipayLogger.logBizDebug(this.getRedirectUrl(requestHolder));
        }

        result.put("prepareTime", System.currentTimeMillis());
        String rsp = null;

        try {
            rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), this.charset, this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
        } catch (IOException var7) {
            throw new AlipayApiException(var7);
        }

        result.put("requestTime", System.currentTimeMillis());
        result.put("rsp", rsp);
        result.put("textParams", requestHolder.getApplicationParams());
        result.put("protocalMustParams", requestHolder.getProtocalMustParams());
        result.put("protocalOptParams", requestHolder.getProtocalOptParams());
        result.put("url", url);
        return result;
    }

    private <T extends AlipayResponse> RequestParametersHolder getRequestHolderWithSign(BatchAlipayRequest request) throws AlipayApiException {
        List<AlipayRequestWrapper> requestList = request.getRequestList();
        if (requestList != null && !requestList.isEmpty()) {
            RequestParametersHolder requestHolder = new RequestParametersHolder();
            AlipayHashMap protocalMustParams = new AlipayHashMap();
            protocalMustParams.put("method", request.getApiMethodName());
            protocalMustParams.put("app_id", this.appId);
            protocalMustParams.put("sign_type", this.signType);
            protocalMustParams.put("charset", this.charset);
            protocalMustParams.put("version", request.getApiVersion());
            if (request.isNeedEncrypt()) {
                protocalMustParams.put("encrypt_type", this.encryptType);
            }

            AlipayHashMap protocalOptParams = new AlipayHashMap();
            protocalOptParams.put("format", this.format);
            protocalOptParams.put("alipay_sdk", "alipay-sdk-java-4.5.0.ALL");
            requestHolder.setProtocalOptParams(protocalOptParams);
            Long timestamp = System.currentTimeMillis();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            protocalMustParams.put("timestamp", df.format(new Date(timestamp)));
            requestHolder.setProtocalMustParams(protocalMustParams);
            AlipayHashMap appParams = new AlipayHashMap();
            StringBuilder requestBody = new StringBuilder();

            for (int index = 0; index < requestList.size(); ++index) {
                AlipayRequestWrapper alipayRequestWrapper = (AlipayRequestWrapper) requestList.get(index);
                AlipayRequest alipayRequest = alipayRequestWrapper.getAlipayRequest();
                Map<String, Object> apiParams = alipayRequest.getTextParams();
                apiParams.put("method", alipayRequest.getApiMethodName());
                apiParams.put("app_auth_token", alipayRequestWrapper.getAppAuthToken());
                apiParams.put("auth_token", alipayRequestWrapper.getAccessToken());
                apiParams.put("prod_code", alipayRequest.getProdCode());
                apiParams.put("notify_url", alipayRequest.getNotifyUrl());
                apiParams.put("return_url", alipayRequest.getReturnUrl());
                apiParams.put("terminal_info", alipayRequest.getTerminalInfo());
                apiParams.put("terminal_type", alipayRequest.getTerminalType());
                apiParams.put("batch_request_id", String.valueOf(index));

                try {
                    if (alipayRequest.getClass().getMethod("getBizContent") != null && StringUtils.isEmpty((String) appParams.get("biz_content")) && alipayRequest.getBizModel() != null) {
                        apiParams.put("biz_content", (new JSONWriter()).write(alipayRequest.getBizModel(), true));
                    }
                } catch (NoSuchMethodException var15) {
                } catch (SecurityException var16) {
                    AlipayLogger.logBizError(var16);
                }

                requestBody.append((new JSONWriter()).write(apiParams, false));
                if (index != requestList.size() - 1) {
                    requestBody.append("#S#");
                }
            }

            appParams.put("biz_content", requestBody.toString());
            String encryptContent;
            if (request.isNeedEncrypt()) {
                if (StringUtils.isEmpty((String) appParams.get("biz_content"))) {
                    throw new AlipayApiException("��ǰAPI��֧�ּ�������");
                }

                if (StringUtils.isEmpty(this.encryptType) || this.getEncryptor() == null) {
                    throw new AlipayApiException("API����Ҫ����ܣ������������Կ���ͺͼ�����");
                }

                encryptContent = this.getEncryptor().encrypt((String) appParams.get("biz_content"), this.encryptType, this.charset);
                appParams.put("biz_content", encryptContent);
            }

            requestHolder.setApplicationParams(appParams);
            if (!StringUtils.isEmpty(this.signType)) {
                encryptContent = AlipaySignature.getSignatureContent(requestHolder);

                System.out.println("sign params ================" + encryptContent);
                protocalMustParams.put("sign", this.getSigner().sign(encryptContent, this.signType, this.charset));
                System.out.println("sign ===========" + protocalMustParams.get("sign"));
            } else {
                protocalMustParams.put("sign", "");
            }

            return requestHolder;
        } else {
            throw new AlipayApiException("40", "client-error:api request list is empty");
        }
    }

    @Override
    public <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request) throws AlipayApiException {
        return this.pageExecute(request, "POST");
    }

    @Override
    public <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request, String httpMethod) throws AlipayApiException {
        RequestParametersHolder requestHolder = this.getRequestHolderWithSign(request, (String) null, (String) null, this.appCertSN);
        if (AlipayLogger.isBizDebugEnabled()) {
            AlipayLogger.logBizDebug(this.getRedirectUrl(requestHolder));
        }

        AlipayResponse rsp = null;

        try {
            Class<T> clazz = request.getResponseClass();
            rsp = (AlipayResponse) clazz.newInstance();
        } catch (InstantiationException var6) {
            AlipayLogger.logBizError(var6);
        } catch (IllegalAccessException var7) {
            AlipayLogger.logBizError(var7);
        }

        if ("GET".equalsIgnoreCase(httpMethod)) {
            rsp.setBody(this.getRedirectUrl(requestHolder));
        } else {
            String baseUrl = this.getRequestUrl(requestHolder);
            rsp.setBody(WebUtils.buildForm(baseUrl, requestHolder.getApplicationParams()));
        }

        return (T) rsp;
    }

    @Override
    public <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> request) throws AlipayApiException {
        RequestParametersHolder requestHolder = this.getRequestHolderWithSign(request, (String) null, (String) null, this.appCertSN);
        if (AlipayLogger.isBizDebugEnabled()) {
            AlipayLogger.logBizDebug(this.getSdkParams(requestHolder));
        }

        AlipayResponse rsp = null;

        try {
            Class<T> clazz = request.getResponseClass();
            rsp = (AlipayResponse) clazz.newInstance();
        } catch (InstantiationException var5) {
            AlipayLogger.logBizError(var5);
        } catch (IllegalAccessException var6) {
            AlipayLogger.logBizError(var6);
        }

        rsp.setBody(this.getSdkParams(requestHolder));
        return (T) rsp;
    }

    @Override
    public <TR extends AlipayResponse, T extends AlipayRequest<TR>> TR parseAppSyncResult(Map<String, String> result, Class<T> requsetClazz) throws AlipayApiException {
        TR tRsp = null;
        String rsp = (String) result.get("result");

        try {
            T request = (T) (AlipayRequest) requsetClazz.newInstance();
            Class<TR> responseClazz = request.getResponseClass();
            if (StringUtils.isEmpty(rsp)) {
                tRsp = (TR) (AlipayResponse) responseClazz.newInstance();
                tRsp.setCode("20000");
                tRsp.setSubCode("ACQ.SYSTEM_ERROR");
                tRsp.setSubMsg((String) result.get("memo"));
            } else {
                AlipayParser<TR> parser = null;
                if ("xml".equals(this.format)) {
                    parser = new ObjectXmlParser(responseClazz);
                } else {
                    parser = new ObjectJsonParser(responseClazz);
                }

                tRsp = (TR) ((AlipayParser) parser).parse(rsp);
                tRsp.setBody(rsp);
                this.checkResponseSign(request, (AlipayParser) parser, rsp, tRsp.isSuccess());
                if (!tRsp.isSuccess()) {
                    AlipayLogger.logBizError(rsp);
                }
            }

            return tRsp;
        } catch (RuntimeException var8) {
            AlipayLogger.logBizError(rsp);
            throw var8;
        } catch (AlipayApiException var9) {
            AlipayLogger.logBizError(rsp);
            throw new AlipayApiException(var9);
        } catch (InstantiationException var10) {
            AlipayLogger.logBizError(rsp);
            throw new AlipayApiException(var10);
        } catch (IllegalAccessException var11) {
            AlipayLogger.logBizError(rsp);
            throw new AlipayApiException(var11);
        }
    }

    private <T extends AlipayResponse> RequestParametersHolder getRequestHolderWithSign(AlipayRequest<?> request, String accessToken, String appAuthToken, String appCertSN) throws AlipayApiException {
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        AlipayHashMap appParams = new AlipayHashMap(request.getTextParams());

        try {
            if (request.getClass().getMethod("getBizContent") != null && StringUtils.isEmpty((String) appParams.get("biz_content")) && request.getBizModel() != null) {
                appParams.put("biz_content", (new JSONWriter()).write(request.getBizModel(), true));
            }
        } catch (NoSuchMethodException var12) {
        } catch (SecurityException var13) {
            AlipayLogger.logBizError(var13);
        }

        if (request.isNeedEncrypt()) {
            if (StringUtils.isEmpty((String) appParams.get("biz_content"))) {
                throw new AlipayApiException("��ǰAPI��֧�ּ�������");
            }

            if (StringUtils.isEmpty(this.encryptType) || this.getEncryptor() == null) {
                throw new AlipayApiException("API����Ҫ����ܣ������������Կ���ͺͼ�����");
            }

            String encryptContent = this.getEncryptor().encrypt((String) appParams.get("biz_content"), this.encryptType, this.charset);
            appParams.put("biz_content", encryptContent);
        }

        if (!StringUtils.isEmpty(appAuthToken)) {
            appParams.put("app_auth_token", appAuthToken);
        }

        requestHolder.setApplicationParams(appParams);
        if (StringUtils.isEmpty(this.charset)) {
            this.charset = "UTF-8";
        }

        AlipayHashMap protocalMustParams = new AlipayHashMap();
        protocalMustParams.put("method", request.getApiMethodName());
        protocalMustParams.put("version", request.getApiVersion());
        protocalMustParams.put("app_id", this.appId);
        protocalMustParams.put("sign_type", this.signType);
        protocalMustParams.put("terminal_type", request.getTerminalType());
        protocalMustParams.put("terminal_info", request.getTerminalInfo());
        protocalMustParams.put("notify_url", request.getNotifyUrl());
        protocalMustParams.put("return_url", request.getReturnUrl());
        protocalMustParams.put("charset", this.charset);
        if (request.isNeedEncrypt()) {
            protocalMustParams.put("encrypt_type", this.encryptType);
        }

        if (!StringUtils.isEmpty(appCertSN)) {
            protocalMustParams.put("app_cert_sn", appCertSN);
        }

        if (!StringUtils.isEmpty(this.alipayRootCertSN)) {
            protocalMustParams.put("alipay_root_cert_sn", this.alipayRootCertSN);
        }

        Long timestamp = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        protocalMustParams.put("timestamp", df.format(new Date(timestamp)));
        requestHolder.setProtocalMustParams(protocalMustParams);
        AlipayHashMap protocalOptParams = new AlipayHashMap();
        protocalOptParams.put("format", this.format);
        protocalOptParams.put("auth_token", accessToken);
        protocalOptParams.put("alipay_sdk", "alipay-sdk-java-4.5.0.ALL");
        protocalOptParams.put("prod_code", request.getProdCode());
        requestHolder.setProtocalOptParams(protocalOptParams);

        if (!StringUtils.isEmpty(this.signType)) {
            String signContent = AlipaySignature.getSignatureContent(requestHolder);
            System.out.println("signContent==========" + signContent);
            protocalMustParams.put("sign", this.getSigner().sign(signContent, this.signType, this.charset));

            System.out.println("sign==========" + protocalMustParams.get("sign"));
        } else {
            protocalMustParams.put("sign", "");
        }

        return requestHolder;
    }

    private String getRequestUrl(RequestParametersHolder requestHolder) throws AlipayApiException {
        StringBuffer urlSb = new StringBuffer(this.serverUrl);

        try {
            String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(), this.charset);
            String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), this.charset);
            urlSb.append("?");
            urlSb.append(sysMustQuery);
            if (sysOptQuery != null & sysOptQuery.length() > 0) {
                urlSb.append("&");
                urlSb.append(sysOptQuery);
            }
        } catch (IOException var5) {
            throw new AlipayApiException(var5);
        }

        return urlSb.toString();
    }

    private String getRedirectUrl(RequestParametersHolder requestHolder) throws AlipayApiException {
        StringBuffer urlSb = new StringBuffer(this.serverUrl);

        try {
            Map<String, String> sortedMap = AlipaySignature.getSortedMap(requestHolder);
            String sortedQuery = WebUtils.buildQuery(sortedMap, this.charset);
            urlSb.append("?");
            urlSb.append(sortedQuery);
        } catch (IOException var5) {
            throw new AlipayApiException(var5);
        }

        return urlSb.toString();
    }

    private String getSdkParams(RequestParametersHolder requestHolder) throws AlipayApiException {
        StringBuffer urlSb = new StringBuffer();

        try {
            Map<String, String> sortedMap = AlipaySignature.getSortedMap(requestHolder);
            String sortedQuery = WebUtils.buildQuery(sortedMap, this.charset);
            urlSb.append(sortedQuery);
        } catch (IOException var5) {
            throw new AlipayApiException(var5);
        }

        return urlSb.toString();
    }

    private <T extends AlipayResponse> T _execute(AlipayRequest<T> request, AlipayParser<T> parser, String authToken, String appAuthToken) throws AlipayApiException {
        long beginTime = System.currentTimeMillis();
        Map<String, Object> rt = this.doPost(request, authToken, appAuthToken, this.appCertSN);
        if (rt == null) {
            return null;
        } else {
            Map<String, Long> costTimeMap = new HashMap();
            if (rt.containsKey("prepareTime")) {
                costTimeMap.put("prepareCostTime", (Long) ((Long) rt.get("prepareTime")) - beginTime);
                if (rt.containsKey("requestTime")) {
                    costTimeMap.put("requestCostTime", (Long) ((Long) rt.get("requestTime")) - (Long) ((Long) rt.get("prepareTime")));
                }
            }

            AlipayResponse tRsp = null;

            try {
                ResponseEncryptItem responseItem = this.decryptResponse(request, rt, parser);
                tRsp = parser.parse(responseItem.getRealContent());
                tRsp.setBody(responseItem.getRealContent());
                this.checkResponseSign(request, parser, responseItem.getRespContent(), tRsp.isSuccess());
                if (costTimeMap.containsKey("requestCostTime")) {
                    costTimeMap.put("postCostTime", System.currentTimeMillis() - (Long) ((Long) rt.get("requestTime")));
                }
            } catch (RuntimeException var11) {
                AlipayLogger.logBizError((String) rt.get("rsp"), costTimeMap);
                throw var11;
            } catch (AlipayApiException var12) {
                AlipayLogger.logBizError((String) rt.get("rsp"), costTimeMap);
                throw new AlipayApiException(var12);
            }

            tRsp.setParams((AlipayHashMap) rt.get("textParams"));
            if (!tRsp.isSuccess()) {
                AlipayLogger.logErrorScene(rt, tRsp, "", costTimeMap);
            } else {
                AlipayLogger.logBizSummary(rt, tRsp, costTimeMap);
            }

            return (T) tRsp;
        }
    }

    private <T extends AlipayResponse> Map<String, Object> doPost(AlipayRequest<T> request, String accessToken, String appAuthToken, String appCertSN) throws AlipayApiException {
        Map<String, Object> result = new HashMap();
        RequestParametersHolder requestHolder = this.getRequestHolderWithSign(request, accessToken, appAuthToken, appCertSN);
        String url = this.getRequestUrl(requestHolder);
        if (AlipayLogger.isBizDebugEnabled()) {
            AlipayLogger.logBizDebug(this.getRedirectUrl(requestHolder));
        }

        result.put("prepareTime", System.currentTimeMillis());
        String rsp = null;

        try {
            if (request instanceof AlipayUploadRequest) {
                AlipayUploadRequest<T> uRequest = (AlipayUploadRequest) request;
                Map<String, FileItem> fileParams = AlipayUtils.cleanupMap(uRequest.getFileParams());
                rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), fileParams, this.charset, this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
            } else {
                rsp = WebUtils.doPost(url, requestHolder.getApplicationParams(), this.charset, this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
            }
        } catch (IOException var11) {
            throw new AlipayApiException(var11);
        }

        result.put("requestTime", System.currentTimeMillis());
        result.put("rsp", rsp);
        result.put("textParams", requestHolder.getApplicationParams());
        result.put("protocalMustParams", requestHolder.getProtocalMustParams());
        result.put("protocalOptParams", requestHolder.getProtocalOptParams());
        result.put("url", url);
        return result;
    }

    private <T extends AlipayResponse> void checkResponseSign(AlipayRequest<T> request, AlipayParser<T> parser, String responseBody, boolean responseIsSucess) throws AlipayApiException {
        if (this.getSignChecker() != null) {
            SignItem signItem = parser.getSignItem(request, responseBody);
            if (signItem == null) {
                throw new AlipayApiException("sign check fail: Body is Empty!");
            }

            if (responseIsSucess || !responseIsSucess && !StringUtils.isEmpty(signItem.getSign())) {
                boolean rsaCheckContent = this.getSignChecker().check(signItem.getSignSourceDate(), signItem.getSign(), this.signType, this.charset);
                if (!rsaCheckContent) {
                    if (StringUtils.isEmpty(signItem.getSignSourceDate()) || !signItem.getSignSourceDate().contains("\\/")) {
                        throw new AlipayApiException("sign check fail: check Sign and Data Fail!");
                    }

                    String srouceData = signItem.getSignSourceDate().replace("\\/", "/");
                    boolean jsonCheck = this.getSignChecker().check(srouceData, signItem.getSign(), this.signType, this.charset);
                    if (!jsonCheck) {
                        throw new AlipayApiException("sign check fail: check Sign and Data Fail��JSON also��");
                    }
                }
            }
        }

    }

    private <T extends AlipayResponse> ResponseEncryptItem decryptResponse(AlipayRequest<T> request, Map<String, Object> rt, AlipayParser<T> parser) throws AlipayApiException {
        String responseBody = (String) rt.get("rsp");
        String realBody = null;
        if (request.isNeedEncrypt()) {
            realBody = parser.decryptSourceData(request, responseBody, this.format, this.getDecryptor(), this.encryptType, this.charset);
        } else {
            realBody = (String) rt.get("rsp");
        }

        return new ResponseEncryptItem(responseBody, realBody);
    }

    void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    void setAppId(String appId) {
        this.appId = appId;
    }

    void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    void setFormat(String format) {
        this.format = format;
    }

    void setSignType(String signType) {
        this.signType = signType;
    }

    void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    void setCharset(String charset) {
        this.charset = charset;
    }

    void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    void setCert(X509Certificate cert) {
        this.cert = cert;
    }

    void setAlipayPublicCertMap(ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap) {
        this.alipayPublicCertMap = alipayPublicCertMap;
    }

    public abstract Signer getSigner();

    public abstract SignChecker getSignChecker();

    public abstract Encryptor getEncryptor();

    public abstract Decryptor getDecryptor();
}
