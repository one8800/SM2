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
        String pubk = "041CE86821708FEA92638FBF01F6CDCD45C0AF306B9D11449DF3CE2F131FB0CEEFC4E00668150B51F4CF9E64ADF297D4DE2C6203BACC8DDA99EA9B02DE2E05F4F1";
        String prik = "00979BA5510C2097BB9FF5FF528D9FAF3D6D38B29A6199F859674AA16C6CF86BC0";
        String encrypt = SM2Utils.encrypt(pubk, content);
        String decrypt = SM2Utils.decrypt(prik, encrypt);
        Log.d(TAG, "密文:" + encrypt);
        Log.d(TAG, "明文:" + decrypt);
    }
}
