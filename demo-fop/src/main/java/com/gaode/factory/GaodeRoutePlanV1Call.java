package com.gaode.factory;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(2)
@Component
public class GaodeRoutePlanCall extends AbstractGaodeRouteHandler<Map<String, Object>,RoutePathRequest,GaodeApiResult,GaodeResult> {

    @Override
    protected void checkParams(Map<String, Object> params) {
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
    protected GaodeApiResult apiCall(RoutePathRequest request) {
        return new GaodeApiResult();
    }

}
