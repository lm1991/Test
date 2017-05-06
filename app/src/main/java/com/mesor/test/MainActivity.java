package com.mesor.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mesor.test.framework.SharedFragmentActivity;
import com.mesor.test.home.HomeFragment;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity {

    String app_id = "";

    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iwxapi = WXAPIFactory.createWXAPI(this, app_id, true);
        iwxapi.registerApp(app_id);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send oauth request
//                final SendAuth.Req req = new SendAuth.Req();
//                req.scope = "snsapi_userinfo";
//                req.state = "wechat_sdk_demo_test";
//                iwxapi.sendReq(req);
                SharedFragmentActivity.startFragmentActivity(MainActivity.this, HomeFragment.class, null);
            }
        });
    }
}
