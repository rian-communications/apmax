package kr.co.rian.apmax.agent.test;

import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class MockServletASM extends HttpServlet {
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    System.out.println();
    System.out.println();

    System.out.println("HTTP request by GET method!");
    final String shouted = shout(123, "hello");
    System.out.println(shouted);

    System.out.println();
    System.out.println();
  }
  
  public String shout(int count, Object b) {
    final String format = "I shout %dth!!! === %s";
    return String.format(format, count, b.toString());
  }
  
  public static void main(String[] args) throws ServletException, IOException, InterruptedException {
    final MockServletASM mockServletASM = new MockServletASM();
    final MockGetReq request = new MockGetReq(Mockito.mock(HttpServletRequest.class));
    final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
  
    System.out.println("ServletRequest.service(req, res) start!");
    mockServletASM.service(
        request,
        response
    );
    System.out.println("ServletRequest.service(req, res) end!");
  }
  
  private static class MockGetReq extends HttpServletRequestWrapper {
    
    private final Map<String, String> parameters = new HashMap<String, String>(2);
    private final Hashtable<String, String> headers = new Hashtable<String, String>();
    
    public MockGetReq(HttpServletRequest request) {
      super(request);
      parameters.put("say", "hello");
      parameters.put("hi", "bye");
      
      headers.put("head", "merry");
      headers.put("arms", "8");
    }
    
    @Override
    public String getRequestURI() {
      return "/mock/sample-uri/";
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Map getParameterMap() {
      return parameters;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getHeaderNames() {
      return headers.keys();
    }
    
    @Override
    public String getHeader(String name) {
      return headers.get(name);
    }
    
    @Override
    public String getMethod() {
      return "GET";
    }
  }
  
}
