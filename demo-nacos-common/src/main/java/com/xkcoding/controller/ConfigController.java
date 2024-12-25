package com.xkcoding.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

  @Value("${name}")
  private String name;

  /**
   * http://localhost:8080/config/get
   */
  @RequestMapping("/get")
  public String get() {
    return name;
  }
}
