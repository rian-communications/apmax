package kr.co.rian.apmax.agent.asm;

import kr.co.rian.apmax.agent.config.Config;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.Arrays;

public class WebChaserAdapter extends ClassVisitor implements AgentLinkageAdapter {
  
  private final ClassWriter writer;
  
  public WebChaserAdapter(byte[] classfileBuffer) {
    super(Config.ASM_VERSION);
    
    final ClassReader reader = new ClassReader(classfileBuffer);
    this.cv = writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    reader.accept(this, ClassReader.EXPAND_FRAMES);
  }
  
  @Override
  public byte[] toByteArray() {
    return writer.toByteArray();
  }
  
  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    System.out.printf("visit(%d, %d, %s, %s, %s, %s)^\n", version, access, name, signature, superName, Arrays.toString(interfaces));
    super.visit(version, access, name, signature, superName, interfaces);
    
    System.out.printf("visit(%d, %d, %s, %s, %s, %s)$\n", version, access, name, signature, superName, Arrays.toString(interfaces));
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    System.out.printf("hi, visitMethod(%d, %s, %s, %s)!\n", access, name, descriptor, signature);
    
    final MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
    if (visitor == null) {
      return null;
    }
    
    System.out.println("called super.visitMethod(...)");
    
    return visitor;//new SimpleTargetMethodVisitor(access, descriptor, visitor);
  }
  
  
  public static class SimpleTargetMethodVisitor extends LocalVariablesSorter {
    
    protected SimpleTargetMethodVisitor(int access, String descriptor, MethodVisitor methodVisitor) {
      super(Config.ASM_VERSION, access, descriptor, methodVisitor);
      System.out.printf("SimpleTargetMethodVisitor.<init>(%d, %s)\n", access, descriptor, methodVisitor);
    }
    
    @Override
    public void visitCode() {
      System.out.println("hi, visitCode()^");
      super.visitCode();
      
      System.out.println("hi, visitCode()$");
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
      System.out.printf("hi, visitMethodInsn(%d, %s, %s, %s, %s)^\n", opcode, owner, name, descriptor);
      super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
      System.out.printf("hi, visitMethodInsn(%d, %s, %s, %s, %s)$\n", opcode, owner, name, descriptor);
    }
    
    
    
    @Override
    public void visitEnd() {
      System.out.println("visitEnd()^");
      super.visitEnd();
      System.out.println("visitEnd()$");
    }
    
    @Override
    public void visitAttribute(Attribute attribute) {
      System.out.printf("SimpleTargetMethodVisitor.visitAttribute(%s)^\n", attribute.type);
      super.visitAttribute(attribute);
      System.out.printf("SimpleTargetMethodVisitor.visitAttribute(%s)$\n", attribute.type);
    }
    
    @Override
    public void visitParameter(String name, int access) {
      System.out.printf("SimpleTargetMethodVisitor.visitParameter(%s, %d)^\n", name, access);
      super.visitParameter(name, access);
      System.out.printf("SimpleTargetMethodVisitor.visitParameter(%s, %d)$\n", name, access);
    }
    
    
    @Override
    public void visitInsn(int opcode) {
      System.out.printf("hi, visitInsn(%d)^\n", opcode);
      if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
        super.visitInsn(opcode);
        
      }
      
      mv.visitInsn(opcode);
      
      System.out.printf("hi, visitInsn(%d)$\n", opcode);
    }
  }
}
