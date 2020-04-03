/**
 *
 */
package com.example.getbilibilicover;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author 18796
 *
 */
public class IntentSvc extends IntentService {

    public IntentSvc() {
        super("ittsvc");
        // TODO 自动生成的构造函数存根
    }

    @Override
    public void onDestroy() {
//		unregisterReceiver(MainActivity.receiver);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO 自动生成的方法存根
        String bid = intent.getStringExtra("bid");
        Intent intent2 = new Intent();
        try {
            ImageGet img = new ImageGet(bid);
            String url = img.GetImageUrl();
            intent2.setAction("img_url");
            intent2.putExtra("url", url);
            sendBroadcast(intent2);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            intent2.setAction("img_url");
            intent2.putExtra("url", e.getMessage());
            sendBroadcast(intent2);
        }

    }


}
