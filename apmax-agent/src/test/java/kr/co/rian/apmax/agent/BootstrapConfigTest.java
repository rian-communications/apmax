package kr.co.rian.apmax.agent;


import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BootstrapConfigTest {

  @Test
  void shouldGetPropertiesInClassPath() {
    final URL url = ClassLoader.getSystemResource("apmax-agent-config.properties");
    assertTrue(new File(url.getFile()).exists());
  }
  
}
