package kr.co.rian.apmax.agent.test;

import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestSimpleASM extends HttpServlet {
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    System.out.println("HTTP request by GET method!");
  }
  
  public static void main(String[] args) throws ServletException, IOException {
    System.out.println("Hello, ASM!");
    
    new TestSimpleASM().doGet(
        Mockito.mock(HttpServletRequest.class),
        Mockito.mock(HttpServletResponse.class)
    );
  }
  
}
