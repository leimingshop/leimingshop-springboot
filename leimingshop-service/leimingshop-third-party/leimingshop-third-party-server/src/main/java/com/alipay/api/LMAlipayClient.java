//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.alipay.api;

import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;

public class LMAlipayClient extends LMAbstractAlipayClient {


    private String privateKey;
    private String encryptKey;
    private String alipayPublicKey;
    private Signer signer;
    private SignChecker signChecker;
    private Encryptor encryptor;
    private Decryptor decryptor;
    private X509Certificate cert;
    private ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap;

    public LMAlipayClient(String serverUrl, String appId, String privateKey) {
        super(serverUrl, appId, (String) null, (String) null, (String) null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public LMAlipayClient(String serverUrl, String appId, String privateKey, String format) {
        super(serverUrl, appId, format, (String) null, (String) null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public LMAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset) {
        super(serverUrl, appId, format, charset, (String) null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
    }

    public LMAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey) {
        super(serverUrl, appId, format, charset, (String) null);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
    }

    public LMAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType) {
        super(serverUrl, appId, format, charset, signType);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
    }

    public LMAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType, String proxyHost, int proxyPort) {
        super(serverUrl, appId, format, charset, signType, proxyHost, proxyPort);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
    }

    public LMAlipayClient(String serverUrl, String appId, String privateKey, String format, String charset, String alipayPublicKey, String signType, String encryptKey, String encryptType) {
        super(serverUrl, appId, format, charset, signType, encryptType);
        this.privateKey = privateKey;
        this.signer = new DefaultSigner(privateKey);
        this.alipayPublicKey = alipayPublicKey;
        this.signChecker = new DefaultSignChecker(alipayPublicKey);
        this.encryptor = new DefaultEncryptor(encryptKey);
        this.decryptor = new DefaultDecryptor(encryptKey);
    }

    public LMAlipayClient(CertAlipayRequest certAlipayRequest) throws AlipayApiException {
        super(certAlipayRequest.getServerUrl(), certAlipayRequest.getAppId(), certAlipayRequest.getFormat(), certAlipayRequest.getCharset(), certAlipayRequest.getSignType(), certAlipayRequest.getCertPath(), certAlipayRequest.getAlipayPublicCertPath(), certAlipayRequest.getRootCertPath(), certAlipayRequest.getProxyHost(), certAlipayRequest.getProxyPort(), certAlipayRequest.getEncryptType());
        this.privateKey = certAlipayRequest.getPrivateKey();
        this.signer = new DefaultSigner(certAlipayRequest.getPrivateKey());
        this.encryptor = new DefaultEncryptor(certAlipayRequest.getEncryptor());
        this.decryptor = new DefaultDecryptor(certAlipayRequest.getEncryptor());
    }

    public static LMAlipayClient.Builder builder(String serverUrl, String appId, String privateKey) {
        return new LMAlipayClient.Builder(serverUrl, appId, privateKey);
    }

    @Override
    public Signer getSigner() {
        return this.signer;
    }

    @Override
    public SignChecker getSignChecker() {
        return this.signChecker;
    }

    @Override
    public Encryptor getEncryptor() {
        return this.encryptor;
    }

    @Override
    public Decryptor getDecryptor() {
        return this.decryptor;
    }

    public X509Certificate getCert() {
        return this.cert;
    }

    public ConcurrentHashMap<String, X509Certificate> getAlipayPublicCertMap() {
        return this.alipayPublicCertMap;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        if (this.signer == null) {
            this.signer = new DefaultSigner(privateKey);
        }

    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
        if (this.encryptor == null) {
            this.encryptor = new DefaultEncryptor(encryptKey);
        }

        if (this.decryptor == null) {
            this.decryptor = new DefaultDecryptor(encryptKey);
        }

    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
        if (this.signChecker == null) {
            this.signChecker = new DefaultSignChecker(alipayPublicKey);
        }

    }

    public static class Builder {
        private LMAlipayClient client;

        Builder(String serverUrl, String appId, String privateKey) {
            this.client = new LMAlipayClient(serverUrl, appId, privateKey);
        }

        public LMAlipayClient build() {
            return this.client;
        }

        public LMAlipayClient.Builder prodCode(String prodCode) {
            this.client.setProdCode(prodCode);
            return this;
        }

        public LMAlipayClient.Builder format(String format) {
            this.client.setFormat(format);
            return this;
        }

        public LMAlipayClient.Builder signType(String signType) {
            this.client.setSignType(signType);
            return this;
        }

        public LMAlipayClient.Builder encryptType(String encryptType) {
            this.client.setEncryptType(encryptType);
            return this;
        }

        public LMAlipayClient.Builder encryptKey(String encryptKey) {
            this.client.setEncryptKey(encryptKey);
            return this;
        }

        public LMAlipayClient.Builder alipayPublicKey(String alipayPublicKey) {
            this.client.setAlipayPublicKey(alipayPublicKey);
            return this;
        }

        public LMAlipayClient.Builder charset(String charset) {
            this.client.setCharset(charset);
            return this;
        }

        public LMAlipayClient.Builder connectTimeout(int connectTimeout) {
            this.client.setConnectTimeout(connectTimeout);
            return this;
        }

        public LMAlipayClient.Builder readTimeout(int readTimeout) {
            this.client.setReadTimeout(readTimeout);
            return this;
        }

        public LMAlipayClient.Builder proxyHost(String proxyHost) {
            this.client.setProxyHost(proxyHost);
            return this;
        }

        public LMAlipayClient.Builder proxyPort(int proxyPort) {
            this.client.setProxyPort(proxyPort);
            return this;
        }

        public LMAlipayClient.Builder cert(X509Certificate cert) {
            this.client.setCert(cert);
            return this;
        }

        public LMAlipayClient.Builder alipayPublicCertMap(ConcurrentHashMap<String, X509Certificate> alipayPublicCertMap) {
            this.client.setAlipayPublicCertMap(alipayPublicCertMap);
            return this;
        }

    }
}
