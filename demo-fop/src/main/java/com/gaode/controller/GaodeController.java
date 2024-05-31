package com.gaode.controller;

import com.gaode.handler.GaodeHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController(value = "/gaode")
public class GaodeController {

 @Resource
 private GaodeHandler gaodeHandler;

  @GetMapping("/demo")
  public String gaode(){
    gaodeHandler.doAction();
    return "success!";
  }
}
