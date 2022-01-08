package sample;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListenerWithExceptions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.nio.ByteBuffer;

// From the Labs

public class DataController<v> implements SerialPortMessageListenerWithExceptions {
    private static final byte[] DELIMITER = new byte[]{'\n'};

    // Semi-Global Variable
    private  int v;

    private final ObservableList<XYChart.Data<Number, Number>> dataPoints;

    public DataController() {
        this.dataPoints = FXCollections.observableArrayList();
    }

    public ObservableList<XYChart.Data<Number, Number>> getDataPoints() {
        return dataPoints;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }



    @Override

    public void serialEvent(SerialPortEvent serialPortEvent) {
    int number;
        try {
            if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED) {
                return;
            }
            var data = serialPortEvent.getReceivedData();
            //System.out.println(Arrays.toString(data));
             number = ByteBuffer.wrap(data).getInt();
        } catch (Exception e) {
            return;
        }


        System.out.println(number);
        v = number;


        var time = System.currentTimeMillis();
        var dataPoint = new XYChart.Data<Number, Number>(time, v);

        Platform.runLater(() -> this.dataPoints.add(dataPoint));

    }

    // Get Input Value From Arduino In Order To Use Globally (This part is my own)
    public int GetValue() {
        try {
            // Gives Time To Actually Obtain Value
            Thread.sleep(3000);
        }  catch (InterruptedException e) {
            System.out.println("Error in GetValue ()");
        }

        var valuee = v;


        return valuee;}



    @Override
    public void catchException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public byte[] getMessageDelimiter() {
        return DELIMITER;
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }
}