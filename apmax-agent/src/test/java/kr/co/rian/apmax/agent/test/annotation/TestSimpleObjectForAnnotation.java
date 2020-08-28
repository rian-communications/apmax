package kr.co.rian.apmax.agent.test.annotation;

import lombok.extern.slf4j.Slf4j;

@TestSimpleTypeAnnotation
@Slf4j
public class TestSimpleObjectForAnnotation {

  public void sample(String args) {
    logger.debug("TestSimpleObjectForAnnotation.sample(String): {}", args);
  }
  
}
