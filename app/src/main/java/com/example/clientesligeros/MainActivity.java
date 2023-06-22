package com.example.clientesligeros;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText edTxt;

    MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        edTxt = findViewById(R.id.edTxt);
        myThread = new MyThread();
        new Thread(myThread).start();
    }

    private class MyThread implements Runnable {
        private volatile String msg="";
        Socket socket;
        DataOutputStream dos;


        @Override
        public void run() {
            try {
                socket = new Socket("192.168.137.190", 5678);
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(msg);
                dos.close();
                dos.flush();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendMsgParam(String msg){
            this.msg = msg;
            run();
        }
    }

    public void btnClikcSnd(View v){
        String msg = edTxt.getText().toString();
        myThread.sendMsgParam(msg);
    }

}