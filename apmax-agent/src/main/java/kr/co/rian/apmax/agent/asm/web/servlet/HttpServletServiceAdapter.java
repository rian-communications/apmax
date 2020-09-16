package kr.co.rian.apmax.agent.asm.web.servlet;

import kr.co.rian.apmax.agent.asm.AgentLinkageAdapter;
import kr.co.rian.apmax.agent.Config;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class HttpServletServiceAdapter extends ClassVisitor implements AgentLinkageAdapter {
  
  private final ClassWriter writer;
  
  public HttpServletServiceAdapter(byte[] classfileBuffer) {
    super(Config.ASM_VERSION);

    final ClassReader reader = new ClassReader(classfileBuffer);
    this.cv = this.writer
        = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

    reader.accept(this, ClassReader.EXPAND_FRAMES);
  }
  
  @Override
  public byte[] toByteArray() {
    return writer.toByteArray();
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
    
    if (visitor != null &&
        "service".equals(name) &&
        "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V"
            .equals(descriptor)) {
      return new HttpServletServiceMethodVisitor(visitor, access, descriptor);
    }
    
    return visitor;
  }
  
}
