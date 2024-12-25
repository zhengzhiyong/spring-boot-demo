package com.xkcoding.conifg;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureBefore({NacosDiscoveryAutoConfiguration.class})
public class NacosDiscoveryAutoConfigurationV2 {

  @Bean
  @ConditionalOnMissingBean
  public NacosServiceDiscovery nacosServiceDiscovery(NacosDiscoveryProperties nacosDiscoveryProperties, NacosServiceManager nacosServiceManager,@Qualifier("otherNamingService") NamingService otherNamingService) {
    return new NacosServiceDiscoveryV2(nacosDiscoveryProperties, nacosServiceManager, otherNamingService);
  }

  @Bean("otherNamingService")
  public NamingService otherNamingService() throws NacosException {
    Properties properties = new Properties();
    String serverAddr = "0.0.0.0:8848"; // 修改为你的 Nacos 服务地址
    String namespace = "otherNameSpace"; // 修改为你的 Nacos 命名空间
    properties.put("serverAddr", serverAddr);
    properties.put("namespace", namespace);
    return NacosFactory.createNamingService(properties);
  }
}
