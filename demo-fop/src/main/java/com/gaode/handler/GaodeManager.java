package com.gaode.handler;

import com.gaode.factory.AbstractGaodeFactory;
import com.gaode.factory.GaodeResultDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GaodeHandler {
  /**
   * 自动装配 ioc 容器中的类型为（AbstractGaodeFactory）的 bean 链条 ，也可用 @Autowired 替代
   */
  @Resource
  private List<AbstractGaodeFactory> gaodeFactoryList;

  public Boolean doAction() {
    Map<String, Object> params = new HashMap<>();
    params.put("abc", "abc");
    for (AbstractGaodeFactory factory : gaodeFactoryList) {
      GaodeResultDto gaodeResultDto = factory.doAction(params);

      if (gaodeResultDto.isSuccess()) {
        //执行成功
        System.out.println(gaodeResultDto);
        break;
      } else if (gaodeResultDto.getResult() != 10003) {
        //发生异常
        System.out.println("其他异常导致");
        break;
      }
      //10003接口调用量超标
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
