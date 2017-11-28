package com.dimitri.nffnn.example;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MultiThread extends Thread{
    private Thread thread;
    private String threadName;
    protected boolean Running = false;

    private Unit unit;

    public MultiThread(Unit unit, String name) {
        this.unit = unit;
        this.threadName = name;
    }

    public void run() {
        if (this.threadName.equals("Render") || this.threadName.equals("Update")) {
            while(this.Running) {
                if(threadName.equals("Update")){
                    unit.update();
                    if(Test.delay > 0) {
                        try {
                            Thread.sleep((long) Test.delay);
                        } catch (InterruptedException var3) {
                            var3.printStackTrace();
                        }
                    }
                }else if(threadName.equals("Render")){
                    unit.render();
                    try {
                        Thread.sleep(((long)(1.0/60.0)));
                    } catch (InterruptedException var3) {
                        var3.printStackTrace();
                    }
                }
            }

            if(InputHandler.isKey(KeyEvent.VK_D)){
                Test.delay /= 10;

            }else if(InputHandler.isKey(KeyEvent.VK_A)) {
                if (Test.delay < 1) {
                    Test.delay = 1;
                }
                Test.delay *= 10;
            }
        }
    }

    public void start() {
        this.Running = true;
        System.out.println("Started " + this.threadName);
        if (this.thread == null) {
            this.thread = new Thread(this, this.threadName);
            this.thread.start();
        }
    }
}
