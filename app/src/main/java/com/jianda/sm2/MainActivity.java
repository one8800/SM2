package com.jianda.sm2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jianda.sm2.sm.SM2Utils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String content = "张群***!!!abcABCが明らかにならず";
        String pubk = "04F6E0C3345AE42B51E06BF50B98834988D54EBC7460FE135A48171BC0629EAE205EEDE253A530608178A98F1E19BB737302813BA39ED3FA3C51639D7A20C7391A";
        String prik = "3690655E33D5EA3D9A4AE1A1ADD766FDEA045CDEAA43A9206FB8C430CEFE0D94";
        String encrypt = SM2Utils.encrypt(pubk, content);
        String decrypt = SM2Utils.decrypt(prik, encrypt);
        Log.d(TAG, "密文:" + encrypt);
        Log.d(TAG, "明文:" + decrypt);
    }
}
