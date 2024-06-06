package com.gaode.handler;

import com.gaode.factory.AbstractGaodeRouteHandler;
import com.gaode.factory.GaodeResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GaodeManager {
  /**
   * 自动装配 ioc 容器中的类型为（AbstractGaodeFactory）的 bean 链条 ，也可用 @Autowired 替代
   */
  @Resource
  private List<AbstractGaodeRouteHandler> handlerList;

  public Boolean doAction() {
    Map<String, Object> params = new HashMap<>();
    params.put("abc", "abc");

    for (int i = 0; i < handlerList.size(); i++) {
      AbstractGaodeRouteHandler handler = handlerList.get(i);
      GaodeResult gaodeResult = handler.doAction(params);
      if (gaodeResult != null) {
        if (gaodeResult.isSuccess())
          //处理成功
          return true;
        else if (gaodeResult.getResult() != 10003) {
          //处理失败
          return false;
        } else {
          //10003
          if (handlerList.size() - 1 == i) {
            //当前最后一个处理器胃，后续没有处理器了，直接返回
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * 提供扩展：手动配置生效的链条
   */
 /* public void addInterceptors(List<AbstractGaodeFactory> gaodeFactoryList) {
    this.gaodeFactoryList = gaodeFactoryList;
  }*/
}
