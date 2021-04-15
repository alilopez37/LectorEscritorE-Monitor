package main.models;

import java.util.Observable;
import java.util.concurrent.ThreadLocalRandom;

public class Escritor extends Observable implements Runnable{
    private Monitor monitor;

    public Escritor(Monitor monitor){
        this.monitor = monitor;
    }

    @Override
    public void run(){
        this.setChanged();
        this.notifyObservers("1");

        monitor.iniciarEscritura();
        this.setChanged();
        this.notifyObservers("2");
        System.out.println(Thread.currentThread().getName() + ": ESCRIBIENDO");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000) + 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitor.finalizarEscritura();
        this.setChanged();
        this.notifyObservers("3");
    }
}