package com.gaode.factory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
@Order(1)
@Component
public class GaodeV2RoutePlanCall extends AbstractGaodeFactory {

    @Override
    protected Map<String, Object> checkParams(Map<String, Object> params) {
        return params;
    }

    @Override
    protected Integer getRedisCallableStatus() {
        return 1;
    }

    @Override
    protected boolean setRedisCallableStatus() {
        return true;
    }

    @Override
    protected RoutePathRequest convertGaodeParams(Map<String, Object> paramsMap) {
        return new RoutePathRequest();
    }

    @Override
    protected GaodeResult convertGaodeResult(GaodeApiResult gaodeResult) {
        return new GaodeResult(10003,false,"V2版本调用量已经使用完毕");
    }

    @Override
    protected GaodeApiResult callGaodeApi(RoutePathRequest request) {
        return new GaodeApiResult();
    }
}
