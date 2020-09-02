package kr.co.rian.apmax.agent.tracer;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ServletTracer {
  
  private final String appName;
  private String userIp;
  private String userAgent;
  private String requestUri;
  private Map<String, String> requestParameters;

  private List<ServletTracer> children = new ArrayList<ServletTracer>();
  
}
