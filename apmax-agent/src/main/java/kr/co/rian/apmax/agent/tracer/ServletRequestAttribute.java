package kr.co.rian.apmax.agent.tracer;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ServletRequestAttribute {
  
  private static final String ANONYMOUS = "APMAX/anonymous";
  
  private final String method;
  private final String uri;
  private final Map<String, String> parameters;
  private final String userAgent;

  private String identifier = ANONYMOUS;
  
  private static class Parameter extends HashMap<String, String> {
    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (!(o instanceof Parameter)) return false;
      
      final Parameter target = (Parameter) o;
      for (final Entry<String, String> entry : this.entrySet()) {
        final String targetValue = target.get(entry.getKey());
        if (targetValue != null && !targetValue.equals(entry.getValue())) {
          return false;
        }
      }
      
      return true;
    }
  }

}
