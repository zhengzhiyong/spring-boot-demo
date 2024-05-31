package com.gaode.factory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(2)
@Component
public class GaodeV1RoutePlanCall extends AbstractGaodeFactory{

    @Override
    protected Map<String, Object> checkParams(Map<String, Object> params) {
        return params;
    }

    @Override
    protected Integer getRedisCallableStatus() {
        return null;
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
        return new GaodeResult(1,true,"success");
    }

    @Override
    protected GaodeApiResult callGaodeApi(RoutePathRequest request) {
        return new GaodeApiResult();
    }

}
