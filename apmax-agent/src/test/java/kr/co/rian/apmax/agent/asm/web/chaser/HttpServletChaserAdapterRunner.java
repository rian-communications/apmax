package kr.co.rian.apmax.agent.asm.web.chaser;

import jdk.nashorn.internal.codegen.types.Type;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

class HttpServletChaserAdapterRunner {
  
  public static void main(String[] args) throws IOException {
    final ClassLoader classLoader = HttpServletChaserAdapterRunner.class.getClassLoader();
    final URL url = classLoader
        .getResource(Type.getInternalName(MockHttpServletChaserTarget.class) + ".class");
    
    final FileInputStream in = new FileInputStream(url.getFile());
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    int i;
    while ((i = in.read()) != -1) {
      out.write(i);
    }
    
    final byte[] bytes = out.toByteArray();
    final HttpServletChaserAdapter adapter = new HttpServletChaserAdapter(bytes);
  
    final FileOutputStream classOut =
        new FileOutputStream(url.getPath().replaceFirst("\\.class", "BCI\\.class"));
    classOut.write(adapter.toByteArray());
    classOut.close();
  }
  
}
