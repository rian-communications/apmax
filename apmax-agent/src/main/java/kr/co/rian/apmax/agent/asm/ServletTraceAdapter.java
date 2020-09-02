package kr.co.rian.apmax.agent.asm;

import kr.co.rian.apmax.agent.config.Config;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class ServletTraceAdapter extends ClassVisitor implements AgentLinkageAdapter {
  
  private final ClassWriter writer;

  public ServletTraceAdapter(byte[] classfileBuffer) {
    super(Config.ASM_VERSION);
    
    final ClassReader reader = new ClassReader(classfileBuffer);
    writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    reader.accept(this, ClassReader.EXPAND_FRAMES);
  }
  
  @Override
  public byte[] toByteArray() {
    return writer.toByteArray();
  }
  
  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    writer.visit(version, access, name, signature, superName, interfaces);
    
    System.out.println("hi, visit in class!");
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
  
    System.out.printf("hi, visitMethod(%d, %s, %s, %s)!\n", access, name, descriptor, signature);
    
    return new SimpleTargetMethodVisitor(access, descriptor, visitor);
  }
  
  
  public static class SimpleTargetMethodVisitor extends LocalVariablesSorter {
    
    protected SimpleTargetMethodVisitor(int access, String descriptor, MethodVisitor methodVisitor) {
      super(Config.ASM_VERSION, access, descriptor, methodVisitor);
    }
    
    @Override
    public void visitCode() {
      super.visitCode();
      
      System.out.println("hi, visitCode()!");
      
    }
    
    @Override
    public void visitInsn(int opcode) {
      if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
        super.visitInsn(opcode);

        System.out.println("hi, visitInsn(opcode) in true context !");
      }
      
      System.out.println("hi, visitInsn(opcode) in false context !");
      
      mv.visitInsn(opcode);
    }
  }
}
