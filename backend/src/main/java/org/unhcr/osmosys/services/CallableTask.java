package org.unhcr.osmosys.services;

import java.util.concurrent.Callable;

public class CallableTask implements Callable<Long> {
        private int id;
        public CallableTask(int id) {
                this.id = id;
        }
        public Long call() {
                System.out.println("inicio");
                System.out.println("inicio"+this.id);
                long summation = 0;
                for (int i = 1; i <= id; i++) {
                        summation += i;
                        try {
                                Thread.sleep(100);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
                System.out.println("result"+summation);
                return summation;
        }
}