package com.itmuch.cloud.study.user.controller;

import com.google.common.collect.Maps;
import com.itmuch.cloud.study.user.feign.UserFeignClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;//restapi 支持
import org.springframework.web.client.RestTemplate;

import com.itmuch.cloud.study.user.entity.User;
import com.itmuch.cloud.study.user.feign.UserFeignClient;

import java.util.List;
import java.util.Map;

@RestController
public class MovieController2 {
  private static final Logger LOGGER = LoggerFactory.getLogger(MovieController2.class);

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private DiscoveryClient discoveryClient;
  @Autowired
  private LoadBalancerClient loadBalancerClient;
  @Autowired
  private UserFeignClient userFeignClient;

  // 使用feifn的风格封装请求service
  @HystrixCommand(fallbackMethod = "findByIdFallback",
          commandProperties = {
          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000"),
          @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds",value = "10000")

  }, threadPoolProperties = {
          @HystrixProperty(name="coreSize",value = "1"),
          @HystrixProperty(name="maxQueueSize",value = "10")
  })
  @GetMapping("/feign/user/{id}")
  public User feignFindById2(@PathVariable Long id) {
    User user=this.userFeignClient.findById(id);
    user.setUsername("这个feign请求名字");
    user.setName("这个feign请求名字");
    return user;
  }
  public User findByIdFallback(Long id) {
    User user = new User();
    user.setId(-1L);
    user.setName("默认用户");
    return user;
  }
  @GetMapping("/user/{id}")
  public User findById(@PathVariable Long id) {
    return this.restTemplate.getForObject("http://demo-provider-user/" + id, User.class);
  }
  @GetMapping("/user2/{username}")
  public User findById(@PathVariable String username) {
    return this.restTemplate.getForObject("http://localhost:8000/name/" + username, User.class);
  }

  /**
   * 测试URL：http://localhost:8010/user/get1?id=1&username=张三
   * @param user user
   * @return 用户信息
   */
  @GetMapping("/user/get1")
  public User get1(User user) {
    return this.userFeignClient.get1(user.getId(), user.getUsername());
  }

  /**
   * 测试URL：http://localhost:8010/user/get2?id=1&username=张三
   * @param user user
   * @return 用户信息
   */
  @GetMapping("/user/get2")
  public User get2(User user) {
    Map<String, Object> map = Maps.newHashMap();
    map.put("id", user.getId());
    map.put("username", user.getUsername());
    return this.userFeignClient.get2(map);
  }

  /**
   * 测试URL:http://localhost:8010/user/post?id=1&username=张三
   * @param user user
   * @return 用户信息
   */
  @GetMapping("/user/post")
  public User post(User user) {
    return this.userFeignClient.post(user);
  }

  /**
   * 查询microservice-provider-user服务的信息并返回
   * @return microservice-provider-user服务的信息
   */
  @GetMapping("/user-instance")
  public List<ServiceInstance> showInfo() {
    List<ServiceInstance> list_si=this.discoveryClient.getInstances("demo-provider-user");
    ServiceInstance si=list_si.get(0);
    System.out.println(si.getHost());
    return list_si;
  }
  @GetMapping("/log-user-instance")
  public void logUserInstance() {
    ServiceInstance serviceInstance = this.loadBalancerClient.choose("demo-provider-user");
    // 打印当前选择的是哪个节点
    MovieController2.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
  }

}
