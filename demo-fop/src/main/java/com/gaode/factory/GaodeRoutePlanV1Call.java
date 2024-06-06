package com.gaode.factory;

import com.gaode.convert.GaodeResultConvert;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Order(2)
@Component
public class GaodeRoutePlanV1Call extends AbstractGaodeRouteHandler {

  @Override
  protected void checkParams(Map<String, Object> params) {
  }

  @Override
  protected Integer getRedisCallableStatus() {
    return null;
  }

  @Override
  protected boolean setRedisCallableStatusEnabled() {
    return true;
  }

  @Override
  protected boolean setRedisCallableStatusDisabled() {
    return false;
  }

  @Override
  protected RoutePathRequest convertGaodeParams(Map<String, Object> paramsMap) {
    return new RoutePathRequest();
  }

  @Override
  protected GaodeResult convertGaodeResult(GaodeApiResult apiResult, RoutePathRequest request) {
    try {
      //接口调用量已达到上限
      if (apiResult.getResult() == 10003) {
        //设置不可缓存状态为不可用状态
        setRedisCallableStatusDisabled();
        //返回提示接口调用量已达上限
        return apiUpperLimitCall();
      }
      //超时重试逻辑
      int retryTimes = 3;
      while ("TIME_OUT".equals(apiResult.getMessage()) && retryTimes-- >= 0) {
        apiResult = apiCall(request);
      }
      return GaodeResultConvert.convertResult(apiResult);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected GaodeApiResult apiCall(RoutePathRequest request) {
    return new GaodeApiResult();
  }

  @Override
  protected GaodeResult apiUpperLimitCall() {
    return null;
  }

}
