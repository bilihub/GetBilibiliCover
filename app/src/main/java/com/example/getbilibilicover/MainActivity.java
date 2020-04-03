package com.example.getbilibilicover;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText et1, et2;
    private Button bt1;
    private String bid, url;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        bt1 = (Button) findViewById(R.id.bt1);
        img = (ImageView) findViewById(R.id.img);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bt1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO 自动生成的方法存根
                bid = et1.getText().toString();
                if (bid.matches("^(b|B)(V|v)[A-Za-z0-9]+$")) {
                    Toast.makeText(MainActivity.this, "请稍候", Toast.LENGTH_SHORT).show();
                    Intent itt = new Intent();
                    itt.putExtra("bid", bid);
                    itt.setClass(MainActivity.this, IntentSvc.class);
                    startService(itt);
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("img_url");
                    getApplicationContext().registerReceiver(receiver, filter);

                } else {
                    Toast.makeText(MainActivity.this, "bv输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                img.setImageBitmap(bitmap);
            } else if (msg.what == 0) {
                Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    //BV1ME411M7hp
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO 自动生成的方法存根
            Log.i("receiver", "已经接受");
            if (intent != null) {
                url = intent.getStringExtra("url");
                et2.setText(url);
                new Thread() {
                    @Override
                    public void run() {
                        URL imageUrl;
                        try {
                            if (url.charAt(4) == 's') {
                                imageUrl = new URL(url);
                            } else {
                                StringBuffer url_buffer = new StringBuffer(url);
                                imageUrl = new URL(url_buffer.insert(4, 's').toString());
                            }
                            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(1000);
                            int code = connection.getResponseCode();
                            if (code == 200 || code == 304) {
                                InputStream inputStream = connection.getInputStream();
                                //使用工厂把网络的输入流生产Bitmap
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                //利用Message把图片发给Handler
                                Message msg = Message.obtain();
                                msg.obj = bitmap;
                                msg.what = 1;
                                handler.sendMessage(msg);
                                inputStream.close();
                            } else {
                                //服务启发生错误
                                Message msg = Message.obtain();
                                msg.obj = "获取失败";
                                handler.sendEmptyMessage(0);
                            }
                        } catch (IOException e) {
                            Message msg = Message.obtain();
                            msg.obj = e.getMessage();
                            msg.what = -1;
                            handler.sendMessage(msg);
                        }
                        super.run();
                    }
                }.start();

            }

        }

    };
}
