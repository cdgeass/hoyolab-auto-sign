package io.github.cdgeass;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class HoyolabClient {

    private static final Map<CharSequence, CharSequence> BBS_HEADERS = new HashMap<>();
    private static final Map<CharSequence, CharSequence> YS_HEADERS = new HashMap<>();
    private static final Map<CharSequence, CharSequence> SR_HEADERS = new HashMap<>();
    private static final Map<CharSequence, CharSequence> ZZZ_HEADERS = new HashMap<>();

    static {
        BBS_HEADERS.put("Accept", "application/json;charset=utf-8, text/plain, */*");
        BBS_HEADERS.put("Accept-Encoding", "gzip, deflate, br");
        BBS_HEADERS.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        BBS_HEADERS.put("Connection", "keep-alive");
        BBS_HEADERS.put("Host", "bbs-api-os.mihoyo.com");
        BBS_HEADERS.put("Origin", "https://www.hoyolab.com");
        BBS_HEADERS.put("Referer", "https://www.hoyolab.com/");

        YS_HEADERS.put("Accept", "application/json, text/plain, */*");
        YS_HEADERS.put("Accept-Encoding", "gzip, deflate, br");
        YS_HEADERS.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        YS_HEADERS.put("Connection", "keep-alive");
        YS_HEADERS.put("Host", "hk4e-api-os.mihoyo.com");
        YS_HEADERS.put("Origin", "https://webstatic-sea.mihoyo.com");
        YS_HEADERS.put("Referer", "https://webstatic-sea.mihoyo.com/");

        SR_HEADERS.put("Accept", "application/json, text/plain, */*");
        SR_HEADERS.put("Accept-Encoding", "gzip, deflate, br");
        SR_HEADERS.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        SR_HEADERS.put("Connection", "keep-alive");
        SR_HEADERS.put("Host", "sg-public-api.hoyolab.com");
        SR_HEADERS.put("Origin", "https://act.hoyolab.com");
        SR_HEADERS.put("Referer", "https://act.hoyolab.com/");
        SR_HEADERS.put("Sec-Fetch-Dest", "empty");
        SR_HEADERS.put("Sec-Fetch-Mode", "cors");
        SR_HEADERS.put("Sec-Fetch-Site", "same-site");
        SR_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");

        ZZZ_HEADERS.put("Accept", "application/json, text/plain, */*");
        ZZZ_HEADERS.put("Accept-Encoding", "gzip, deflate, br, zstd");
        ZZZ_HEADERS.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        ZZZ_HEADERS.put("Connection", "keep-alive");
        ZZZ_HEADERS.put("Host", "sg-public-api.hoyolab.com");
        ZZZ_HEADERS.put("Origin", "https://act.hoyolab.com");
        ZZZ_HEADERS.put("Referer", "https://act.hoyolab.com/");
        ZZZ_HEADERS.put("Sec-Fetch-Dest", "empty");
        ZZZ_HEADERS.put("Sec-Fetch-Mode", "cors");
        ZZZ_HEADERS.put("Sec-Fetch-Site", "same-site");
        ZZZ_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");
        ZZZ_HEADERS.put("x-rpc-signgame", "zzz");
    }

    @Inject
    @Client("https://bbs-api-os.hoyolab.com")
    HttpClient bbsClient;

    @Inject
    @Client("https://sg-hk4e-api.hoyolab.com")
    HttpClient ysClient;

    @Inject
    @Client("https://sg-public-api.hoyolab.com")
    HttpClient srClient;

    @Inject
    @Client("https://sg-public-api.hoyolab.com")
    HttpClient zzzClient;

    public HttpResponse<Map> getUserInfo(String cookie) {
        var httpRequest = HttpRequest.GET("/community/user/wapi/getUserFullInfo")
                .headers(BBS_HEADERS)
                .header("cookie", cookie);
        return bbsClient.toBlocking().exchange(httpRequest, Map.class);
    }

    public HttpResponse<Map> getYsSignInfo(String cookie, String actId) {
        var httpRequest = HttpRequest.GET("/event/sol/info")
                .headers(YS_HEADERS)
                .header("Cookie", cookie);
        httpRequest.getParameters()
                .add("lang", "zh-cn")
                .add("act_id", actId);
        return ysClient.toBlocking().exchange(httpRequest, Map.class);
    }

    public HttpResponse<Map> signYs(String cookie, String actId) {
        Map<String, Object> body = new HashMap<>();
        body.put("act_id", actId);
        var httpRequest = HttpRequest.POST("/event/sol/sign", body)
                .headers(YS_HEADERS)
                .header("Cookie", cookie);
        httpRequest.getParameters().add("lang", "zh-cn");
        return ysClient.toBlocking().exchange(httpRequest, Map.class);
    }

    public HttpResponse<Map> getSrSignInfo(String cookie, String actId) {
        var httpRequest = HttpRequest.GET("/event/luna/hkrpg/os/info")
                .headers(SR_HEADERS)
                .header("Cookie", cookie);
        httpRequest.getParameters()
                .add("lang", "zh-cn")
                .add("act_id", actId);
        return srClient.toBlocking().exchange(httpRequest, Map.class);
    }

    public HttpResponse<Map> signSr(String cookie, String actId) {
        Map<String, Object> body = new HashMap<>();
        body.put("act_id", actId);
        var httpRequest = HttpRequest.POST("/event/luna/hkrpg/os/sign", body)
                .headers(SR_HEADERS)
                .header("cookie", cookie);
        httpRequest.getParameters().add("lang", "zh-cn");
        return srClient.toBlocking().exchange(httpRequest, Map.class);
    }

    public HttpResponse<Map> getZzzSignInfo(String cookie, String actId) {
        var httpRequest = HttpRequest.GET("/event/luna/zzz/os/info")
                .headers(ZZZ_HEADERS)
                .header("Cookie", cookie);
        httpRequest.getParameters()
                .add("lang", "zh-cn")
                .add("act_id", actId);
        return zzzClient.toBlocking().exchange(httpRequest, Map.class);
    }

    public HttpResponse<Map> signZzz(String cookie, String actId) {
        Map<String, Object> body = new HashMap<>();
        body.put("act_id", actId);
        var httpRequest = HttpRequest.POST("/event/luna/zzz/os/sign", body)
                .headers(ZZZ_HEADERS)
                .header("Cookie", cookie);
        httpRequest.getParameters().add("lang", "zh-cn");
        return zzzClient.toBlocking().exchange(httpRequest, Map.class);
    }
}
