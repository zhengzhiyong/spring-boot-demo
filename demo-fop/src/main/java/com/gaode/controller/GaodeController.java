package com.gaode.controller;

import com.gaode.handler.GaodeManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController(value = "/gaode")
public class GaodeController {

 @Resource
 private GaodeManager gaodeManager;

  @GetMapping("/demo")
  public String gaode(){
    gaodeManager.doAction();
    return "success!";
  }
}
