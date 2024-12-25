package com.xkcoding.conifg;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.springframework.cloud.client.ServiceInstance;

import java.util.*;

public class NacosServiceDiscoveryV2 extends NacosServiceDiscovery {

  public NacosServiceDiscoveryV2(NacosDiscoveryProperties discoveryProperties, NacosServiceManager nacosServiceManager, NamingService otherNamingService) {
    super(discoveryProperties, nacosServiceManager);
    this.discoveryProperties = discoveryProperties;
    this.nacosServiceManager = nacosServiceManager;
    this.otherNamingService = otherNamingService;
  }

  private NacosDiscoveryProperties discoveryProperties;
  private NacosServiceManager nacosServiceManager;
  private final NamingService otherNamingService;

  // 重写该方法
  public List<ServiceInstance> getInstances(String serviceId) throws NacosException {
    String group = this.discoveryProperties.getGroup();
    // 优先保证同分组下的服务调用
    List<Instance> instances = this.namingService().selectInstances(serviceId, group, true);

    // 如果同分组下找不到服务,那么就从默认分组下找服务
    if (CollUtil.isEmpty(instances)) {
      String groupName = "DEFAULT_GROUP";
      instances = selectInstancesFromOtherGroup(serviceId, groupName);
    }

    // 如果同命名空间下找不到，就到其他命名空间中招服务
    if (CollUtil.isEmpty(instances)) {
      String groupName = "DEFAULT_GROUP";
      instances = selectInstancesFromOtherNameSpace(serviceId, groupName);
    }
    return hostToServiceInstanceList(instances, serviceId);
  }

  // 如果同分组下找不到服务,那么就从默认分组下找服务
  private List<Instance> selectInstancesFromOtherGroup(String serviceId, String groupName) throws NacosException {
    return this.namingService().selectInstances(serviceId, groupName, true);
  }

  // 如果同命名空间下找不到，就到其他命名空间中招服务
  private List<Instance> selectInstancesFromOtherNameSpace(String serviceId, String groupName) throws NacosException {
    return this.otherNamingService.selectInstances(serviceId, groupName, true);
  }

  public List<String> getServices() throws NacosException {
    String group = this.discoveryProperties.getGroup();
    ListView<String> services = this.namingService().getServicesOfServer(1, Integer.MAX_VALUE, group);
    return services.getData();
  }

  public static List<ServiceInstance> hostToServiceInstanceList(List<Instance> instances, String serviceId) {
    List<ServiceInstance> result = new ArrayList(instances.size());
    Iterator var3 = instances.iterator();

    while (var3.hasNext()) {
      Instance instance = (Instance) var3.next();
      ServiceInstance serviceInstance = hostToServiceInstance(instance, serviceId);
      if (serviceInstance != null) {
        result.add(serviceInstance);
      }
    }

    return result;
  }

  public static ServiceInstance hostToServiceInstance(Instance instance, String serviceId) {
    if (instance != null && instance.isEnabled() && instance.isHealthy()) {
      NacosServiceInstance nacosServiceInstance = new NacosServiceInstance();
      nacosServiceInstance.setHost(instance.getIp());
      nacosServiceInstance.setPort(instance.getPort());
      nacosServiceInstance.setServiceId(serviceId);
      nacosServiceInstance.setInstanceId(instance.getInstanceId());
      Map<String, String> metadata = new HashMap();
      metadata.put("nacos.instanceId", instance.getInstanceId());
      metadata.put("nacos.weight", instance.getWeight() + "");
      metadata.put("nacos.healthy", instance.isHealthy() + "");
      metadata.put("nacos.cluster", instance.getClusterName() + "");
      if (instance.getMetadata() != null) {
        metadata.putAll(instance.getMetadata());
      }

      metadata.put("nacos.ephemeral", String.valueOf(instance.isEphemeral()));
      nacosServiceInstance.setMetadata(metadata);
      if (metadata.containsKey("secure")) {
        boolean secure = Boolean.parseBoolean((String) metadata.get("secure"));
        nacosServiceInstance.setSecure(secure);
      }

      return nacosServiceInstance;
    } else {
      return null;
    }
  }

  private NamingService namingService() {
    return this.nacosServiceManager.getNamingService();
  }

}

