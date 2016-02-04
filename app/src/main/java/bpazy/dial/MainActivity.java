package bpazy.dial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button button;
    private TextView textView;
    private BroadcastReceiverUI receiverUI;
    private WifiManager wifiManager;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(true);
                textView.setText("");
                TextView textView = new TextView(MainActivity.this);
                textView.setText("请稍候...");
                toast.setView(textView);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                UtilsHelpers.sendMessage("10001", "xykdmm");
                button.setClickable(false);
            }
        });
    }

    private void initView() {
        button = (Button) findViewById(R.id.button_connect);
        textView = (TextView) findViewById(R.id.text_view);
        toast = new Toast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverUI);
    }


    @Override
    protected void onResume() {
        super.onResume();
        init_BroadcastReceiverUI();
    }

    private void init_BroadcastReceiverUI() {
        IntentFilter filter = new IntentFilter("toMainActivity");
        receiverUI = new BroadcastReceiverUI();
        this.registerReceiver(receiverUI, filter);
    }

    public void turnToSetting(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    public class BroadcastReceiverUI extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            button.setClickable(true);
            int returnCode = intent.getExtras().getInt("data");
            switch (returnCode) {
                case CRouter.SUCCESS:
                    textView.setText("执行成功");
                    break;
                default:
                    textView.setText("执行失败,错误代码: " + returnCode);
            }
        }
    }
}