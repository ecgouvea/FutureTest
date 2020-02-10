package futures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        System.out.println("begin");
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int waitSeconds = 10, id = 1; waitSeconds > 0; waitSeconds--, id++) {
            executorService.execute(new Task(id, waitSeconds));
        }

        System.out.println("Future<?> future = executorService.submit");
        Future<?> future = executorService.submit(new Task(11, 10));

        System.out.println("future.cancel");
        future.cancel(true);

        System.out.println("executorService.shutdown");
        executorService.shutdown();

        boolean terminated = false;

        try {
            long init = System.currentTimeMillis();
            System.out.println("executorService.awaitTermination");
            terminated = executorService.awaitTermination(60, TimeUnit.SECONDS);
            System.out.printf("executorService.awaitTermination time elapsed: %d seconds\n", (System.currentTimeMillis() - init) / 1000);
        } catch (InterruptedException e) {
            System.out.println("executorService.awaitTermination exception");
            e.printStackTrace();
        } finally {
            System.out.printf("executorService.awaitTermination terminated:%s\n", terminated);
        }

        System.out.println("end");
    }

    private static class Task implements Runnable {
        private int id;
        private int waitSeconds;

        public Task(int id, int waitSeconds) {
            this.id = id;
            this.waitSeconds = waitSeconds;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(waitSeconds * 1000);
            } catch (InterruptedException e) {
                System.out.printf("Thread %d was interrupted\n", id);
                e.printStackTrace();
            }
            System.out.printf(
                    "Task %d running in thread %s waited %d seconds\n",
                    id, Thread.currentThread().getName(), waitSeconds
            );
        }
    }
}
