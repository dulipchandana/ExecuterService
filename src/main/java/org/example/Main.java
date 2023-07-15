package org.example;

import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        //number of cpu
        int coreCount = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(coreCount);
        service.schedule(new Task(),10, TimeUnit.SECONDS);
        service.scheduleAtFixedRate(new Task(),15,10,TimeUnit.SECONDS);

        Future<Integer> future = service.submit(new CallableTask());
        System.out.println("future>>>" + future.get(30,TimeUnit.SECONDS));
    }


}
class Task implements Runnable{

    @Override
    public void run() {
        System.out.println(">>>>>"+Thread.currentThread().getName());
        try {
            CompletableFuture.supplyAsync(() -> first())
                    .thenApply(number -> second(number))
                    .thenAccept(num -> third(num)).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private int first(){
        return new Random().nextInt();
    }

    private int second(Integer num){
        System.out.println("second"+num);
        return num;
    }

    private int third(Integer num){
        System.out.println("third"+num);
        return num;
    }
}
class CallableTask implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(20000);
        return new Random().nextInt();
    }
}

