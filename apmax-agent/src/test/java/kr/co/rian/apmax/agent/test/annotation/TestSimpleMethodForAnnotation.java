package kr.co.rian.apmax.agent.test.annotation;

public class TestSimpleMethodForAnnotation {
  
  @TestSimpleMethodAnnotation(value = "test", number = 12345)
  public void simple(String args) {
    System.out.printf("TestSimpleMethodForAnnotation.simple(String): %d", args);
  }
  
}
