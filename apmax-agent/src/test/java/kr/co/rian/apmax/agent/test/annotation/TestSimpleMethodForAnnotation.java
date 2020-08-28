package kr.co.rian.apmax.agent.test.annotation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSimpleMethodForAnnotation {
  
  @TestSimpleMethodAnnotation(value = "test", number = 12345)
  public void simple(String args) {
    logger.debug("TestSimpleMethodForAnnotation.simple(String): {}", args);
  }
  
}
