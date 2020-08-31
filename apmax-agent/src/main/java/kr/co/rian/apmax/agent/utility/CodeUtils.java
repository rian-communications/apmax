package kr.co.rian.apmax.agent.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CodeUtils {
  
  private CodeUtils() {
  }
  
  public static Set<String> splitAndToSet(final String values) {
    return new HashSet<String>(Arrays.asList(values.split("[\\s,|]+")));
  }
  
}
