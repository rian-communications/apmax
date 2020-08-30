package kr.co.rian.apmax.agent;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

class BootstrapConfigTest {

  @Test
  public void shouldGetPropertiesInClassPath() {
    final URL url = ClassLoader.getSystemResource("apmax-agent-config.properties");
    
    assert new File(url.getFile()).exists();
  
  }
  
}
