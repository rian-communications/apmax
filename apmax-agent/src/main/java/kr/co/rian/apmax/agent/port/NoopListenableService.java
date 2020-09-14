package kr.co.rian.apmax.agent.port;

import com.google.common.util.concurrent.ListenableFuture;
import kr.co.rian.apmax.agent.Commons.Noop;
import kr.co.rian.apmax.agent.config.Config;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoopListenableService {
  
  private static final NoopListener listener = new NoopListener();
  private static final Executor executor =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  
  private NoopListenableService() {
  }
  
  public static void listening(ListenableFuture<Noop> streamObserver) {
    //streamObserver.addListener(listener, executor);
  }
  
  private static class NoopListener implements Runnable {
    @Override
    public void run() {
      /*if (Config.isDebugMode()) {
        System.out.printf("[%s] completed. \n", Thread.currentThread().getName());
      }*/
    }
  }
  
}
