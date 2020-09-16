package kr.co.rian.apmax.agent;


import kr.co.rian.apmax.agent.Config;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigTest {
  
  @Test
  void shouldGetPropertiesInClassPath() {
    final URL url = ClassLoader.getSystemResource("agent-config.properties");
    assertTrue(new File(url.getFile()).exists());
  }
  
  @Test
  void shouldGetPropertyValue() {
    Config.configure();
    assertEquals(3506, Config.getServerPort());
  }
  
}
