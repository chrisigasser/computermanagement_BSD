package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer{
    Timer t;
    int hour, minute, second;

    @FXML
    Button btn_start;

    @FXML
    Button btn_stop;

    @FXML
    Label label_time;

    @FXML
    Label label_time_marker;

    @FXML
    protected void buttonPressed(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        this.hour = now.getHour();
        this.minute = now.getMinute();
        this.second = now.getSecond();



        String text = hour+":"+minute+":"+second;
        label_time.setText(text);
        /*t = new Timer();
        t.addObserver(this);
        new Thread(t).start();*/
    }

    @FXML
    protected void stopButtonPressed(){
        LocalDateTime now = LocalDateTime.now();
        int endyear = now.getYear();
        int endmonth = now.getMonthValue();
        int endday = now.getDayOfMonth();
        int endhour = now.getHour();
        int endminute = now.getMinute();
        int endsecond = now.getSecond();

        label_time_marker.setText("Elapsed:");
        String text = (endhour-this.hour)+":"+(endminute - this.minute)+":"+(endsecond-this.second);
        label_time.setText(text);

        String hostname = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }

        PostRequest myPostRequest = new PostRequest(hostname,hour+":"+minute+":"+second, endhour+":"+endminute+":"+endsecond);
        myPostRequest.makeHTTPPOSTRequest();
    }

    @Override
    public void update(Observable o, Object arg) {
        label_time.setText(Timer.getCurtime());
    }
}
