package kr.co.rian.apmax.agent.asm.web.servlet;

import kr.co.rian.apmax.agent.asm.AgentLinkageAdapter;
import kr.co.rian.apmax.agent.config.Config;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ServletMonitoredAdapter extends ClassVisitor implements AgentLinkageAdapter {
  
  private final ClassWriter writer;
  private final String className;
  
  public ServletMonitoredAdapter(String className, byte[] classfileBuffer) {
    super(Config.ASM_VERSION);
    this.className = className;
    
    final ClassReader reader = new ClassReader(classfileBuffer);
    this.cv = writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    reader.accept(this, ClassReader.EXPAND_FRAMES);
  }
  
  @Override
  public byte[] toByteArray() {
    return writer.toByteArray();
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
    
    if (visitor != null
        && !"<init>".equals(name)
        && Opcodes.ACC_PUBLIC == access) {
      return new ServletMonitoredMethodVisitor(visitor, access, descriptor, className + "#" + name);
    }
    
    return visitor;
  }
  
}
