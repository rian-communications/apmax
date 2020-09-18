package kr.co.rian.apmax.agent.asm.web.chaser;

import kr.co.rian.apmax.agent.asm.ASMTestTransformer;
import org.junit.jupiter.api.Test;

class HttpServletChaserAdapterTest {
  
  @Test
  void shouldBCI() {
    ASMTestTransformer.transform(
        MockHttpServletChaserTarget.class,
        HttpServletChaserAdapter.class,
        new Class<?>[]{String.class, byte[].class}
    );
    
    
  }
  
}
