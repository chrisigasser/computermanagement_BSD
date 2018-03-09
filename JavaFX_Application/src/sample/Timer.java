package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.time.LocalDateTime;
import java.util.Date;

public class Timer extends java.util.Observable implements Runnable{
    private static int hours = 0;
    private static int minutes = 0;
    private static int seconds = 0;


    public static String getCurtime() {
        return hours+":"+minutes+":"+seconds;
    }

    public void updateTime(){
        seconds+=1;
        setChanged();
        notifyObservers();
        try {
            Thread.sleep(1000);
            this.updateTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        updateTime();
    }
}
