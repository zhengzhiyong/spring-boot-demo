package com.gaode.factory;

import java.util.Map;

public abstract class AbstractGaodeFactory {
    public GaodeResultDto doAction(Map<String, Object> params) {
        Map<String, Object> paramsMap = checkParams(params);
        Integer redisCallableStatus = getRedisCallableStatus();
        boolean redisCallableFlag = checkAndSetRedisCallableStatus(redisCallableStatus);
        //if (redisCallableFlag && getRedisCallableStatus() == 1) {
        if (redisCallableFlag){
            RoutePathRequest request = convertGaodeParams(paramsMap);
            GaodeApiResult gaodeResult = callGaodeApi(request);
            GaodeResult result = convertGaodeResult(gaodeResult);
            if (result.isSuccess()) {
                return new GaodeResultDto(0, true, "success");
            }
            if (result.getResult() == 10003) {
                return new GaodeResultDto(10003, false, "今日调用超标，去寻找下一个执行器");
            }
            return new GaodeResultDto(0, false, "has an error :xxxxx");
        } else {
            return new GaodeResultDto(10003, false, "今日调用超标，去寻找下一个执行器");
        }

    }

    protected abstract Map<String, Object> checkParams(Map<String, Object> params);

    protected boolean checkAndSetRedisCallableStatus(Integer redisCallableStatus) {
        //return ((null == redisCallableStatus && setRedisCallableStatus()) || redisCallableStatus == 1);
        return (null !=  redisCallableStatus && redisCallableStatus == 1) || (null == redisCallableStatus && setRedisCallableStatus());
    }

    protected abstract Integer getRedisCallableStatus();

    protected abstract boolean setRedisCallableStatus();

    protected abstract RoutePathRequest convertGaodeParams(Map<String, Object> paramsMap);

    protected abstract GaodeResult convertGaodeResult(GaodeApiResult gaodeResult);

    protected abstract GaodeApiResult callGaodeApi(RoutePathRequest request);
}
