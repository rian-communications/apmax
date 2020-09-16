package kr.co.rian.apmax.agent.asm.web.servlet;

import kr.co.rian.apmax.agent.Config;
import kr.co.rian.apmax.agent.chaser.SeizedBooty;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

public class HttpServletServiceMethodVisitor extends LocalVariablesSorter {
  
  public static final ThreadLocal<SeizedBooty.Builder> BOOTY = new ThreadLocal<SeizedBooty.Builder>();
  
  private static final String CLASS_INTERNAL_NAME =
      HttpServletServiceMethodVisitor.class.getName().replace('.', '/');
  
  public HttpServletServiceMethodVisitor(MethodVisitor visitor, int access, String descriptor) {
    super(Config.ASM_VERSION, access, descriptor, visitor);
  }
  
  @Override
  public void visitCode() {
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        CLASS_INTERNAL_NAME,
        "swipe",
        "(Ljavax/servlet/http/HttpServletRequest;)V",
        false);

    mv.visitCode();
  }
  
  @Override
 	public void visitInsn(int opcode) {
 		if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
      mv.visitMethodInsn(
          Opcodes.INVOKESTATIC,
          CLASS_INTERNAL_NAME,
          "emit",
          "()V",
          false);
 		}

 		mv.visitInsn(opcode);
 	}
 	
 	@Override
 	public void visitMaxs(int maxStack, int maxLocals) {
    mv.visitMethodInsn(
        Opcodes.INVOKESTATIC,
        CLASS_INTERNAL_NAME,
        "emit",
        "()V",
        false);
 		
    mv.visitMaxs(maxStack + 1, maxLocals);
 	}
 	
  public static void swipe(HttpServletRequest request) {
    final SeizedBooty.Builder builder = SeizedBooty.newBuilder();
    BOOTY.set(builder);
    
    builder.setId(Config.getId())
        .setTimestamp(System.currentTimeMillis())
        .setUri(request.getRequestURI());
    
    fillParametersAndHeaders(request, builder);
  }
  
  public static void emit() {
    final SeizedBooty.Builder builder = BOOTY.get();
    if (builder != null) {
      System.err.println("emit()");
//      final SeizedBooty webChaser = builder.build();
      BOOTY.remove();
    }
  }
  
  public static void addMethodStack(String signature, long elapsed) {
    final SeizedBooty.Builder builder = BOOTY.get();
    if (builder != null) {
      builder.addStacks(
          SeizedBooty.Stack.newBuilder()
              .setSignature(signature)
              .setElapsed((int) (builder.getTimestamp() - elapsed)).build()
      );
    }
  }
  
  private static void fillParametersAndHeaders(HttpServletRequest request, SeizedBooty.Builder builder) {
    @SuppressWarnings("unchecked") final Map<String, String> parameters = request.getParameterMap();
    builder.putAllParameters(parameters);
    
    @SuppressWarnings("unchecked") final Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      final String headerName = headerNames.nextElement();
      builder.putHeaders(headerName, request.getHeader(headerName));
    }
  }
  
}
