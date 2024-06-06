package com.gaode.factory;

/**
 *
 * @param <ORIGINAL_REQUEST> 原始请求参数
 * @param <API_REQUEST> API接口请求参数
 * @param <API_RESPONSE> API接口返回值
 * @param <RESULT_DATA> 最终返回结果
 */
public abstract class AbstractHandler<ORIGINAL_REQUEST, API_REQUEST, API_RESPONSE, RESULT_DATA> {

  /**
   * 业务执行逻辑封装
   * @param originalRequest
   * @return
   */
  public RESULT_DATA doAction(ORIGINAL_REQUEST originalRequest) {
      //1、参数检查
      checkParams(originalRequest);
      //2、调用量获取
      Integer redisCallableStatus = getRedisCallableStatus();
      //3、调用量检查
      boolean redisCallableFlag = checkAndSetRedisCallableStatus(redisCallableStatus);
      //4、是否可调用
      if (!redisCallableFlag) {
        return apiUpperLimitCall();
      }
      API_REQUEST apiRequest = convertGaodeParams(originalRequest);
      API_RESPONSE apiResponse = apiCall(apiRequest);
      RESULT_DATA result = convertGaodeResult(apiResponse, apiRequest);
      return result;
  }

  /**
   * 原始入参检查
   * @param originalRequest
   */
  protected abstract void checkParams(ORIGINAL_REQUEST originalRequest);

  /**
   * 判断缓存并设置初始值
   * @param redisCallableStatus
   * @return
   */
  protected boolean checkAndSetRedisCallableStatus(Integer redisCallableStatus) {
    return (null != redisCallableStatus && redisCallableStatus == 1)
      || (null == redisCallableStatus && setRedisCallableStatusEnabled());
  }

  /**
   * 获取缓存状态值
   * @return
   */
  protected abstract Integer getRedisCallableStatus();

  /**
   * 设置缓存状态值为可用状态
   * @return
   */
  protected abstract boolean setRedisCallableStatusEnabled();

  /**
   * 设置缓存状态值为不可用状态
   * @return
   */
  protected abstract boolean setRedisCallableStatusDisabled();

  /**
   * 封装原始入参为API接口的请求参数
   * @return
   */
  protected abstract API_REQUEST convertGaodeParams(ORIGINAL_REQUEST originalRequest);

  /**
   * 封装API接口返回的结果
   * @param apiResponse
   * @param apiRequest
   * @return
   */
  protected abstract RESULT_DATA convertGaodeResult(API_RESPONSE apiResponse,API_REQUEST apiRequest);

  /**
   * 调用API接口
   * @return
   */
  protected abstract API_RESPONSE apiCall(API_REQUEST apiRequest);

  /**
   * API接口调用量达到上限，返回具体的信息
   * @return
   */
  protected abstract RESULT_DATA apiUpperLimitCall();
}
