package com.example.chiabank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        intentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {


                        // 寫另一個Activity回傳後,得到回傳的資料之後的做法
                        if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                            amount = result.getData().getDoubleExtra("AMOUNT", 0);  // 回傳回來的輸入金額
                            String action = result.getData().getStringExtra("ACTION");   // 存提款
                            String coin = result.getData().getStringExtra("COIN");   // 外幣種類
                            TextView tv_ntdbalance = (TextView) findViewById(R.id.ntdbalance);   // 顯示台幣餘額
                            TextView tv_result = (TextView) findViewById(R.id.result);   // 顯示操作結果
                            double ntd_balance = Double.parseDouble(tv_ntdbalance.getText().toString());

                            tv_result.setText("交易成功");
                            tv_result.setTextColor(Color.parseColor("#33AA33"));


                            if (action.equals("deposit")) {  // 台幣存款
                                tv_ntdbalance.setText(String.valueOf(amount + ntd_balance));
                            } else if (action.equals("withdraw")) {   // 提款
                                if (ntd_balance >= amount) {
                                    tv_ntdbalance.setText(String.valueOf(ntd_balance - amount));
                                } else {
                                    tv_result.setText("餘額不足，交易失敗");
                                    tv_result.setTextColor(Color.parseColor("#AA3333"));
                                }

                            } else if (action.equals("toNTD")) {  // 外幣換台幣
                                double rate = result.getData().getDoubleExtra("RATE", 0);   // 取得匯率
                                if (coin.equals("USD")) {  // 美金頁面
                                    TextView tv_usdbalance = (TextView) findViewById(R.id.usdbalance);  // MainActivity顯示美金餘額
                                    double usdbalance = Double.parseDouble(tv_usdbalance.getText().toString());


                                    if (usdbalance >= amount) {  // 美金餘額足夠
                                        double r = amount * rate;
                                        tv_ntdbalance.setText(String.valueOf(ntd_balance + r));  // 台幣增加
                                        tv_usdbalance.setText(String.valueOf(usdbalance - amount));   // 美金減少
                                    } else {
                                        tv_result.setText("餘額不足,交易失敗");
                                        tv_result.setTextColor(Color.parseColor("#AA3333"));
                                    }
                                } else {  // 日圓頁面
                                    TextView tv_jpybalance = (TextView) findViewById(R.id.jpybalance);  // 顯示日幣餘額
                                    double jpyBalance = Double.parseDouble(tv_jpybalance.getText().toString());


                                    if (jpyBalance >= amount) {  // 日幣足夠
                                        double r = amount * rate;
                                        tv_ntdbalance.setText(String.valueOf(ntd_balance + r));   // 台幣增加
                                        tv_jpybalance.setText(String.valueOf(jpyBalance - amount));   // 日幣減少
                                    } else {
                                        tv_result.setText("餘額不足，交易失敗！");
                                        tv_result.setTextColor(Color.parseColor("#AA3333"));
                                    }
                                }

                            } else {   // 台幣換外幣
                                double rate = result.getData().getDoubleExtra("RATE", 0);   // 匯率
                                if (coin.equals("USD")) {   // 美金頁面
                                    TextView tv_usdbalance = (TextView) findViewById(R.id.usdbalance);   // 顯示美金
                                    double usdBalance = Double.parseDouble(tv_usdbalance.getText().toString());


                                    if (ntd_balance >= amount) {   // 台幣足夠
                                        double r = amount / rate;
                                        tv_ntdbalance.setText(String.valueOf(ntd_balance - amount));   // 台幣減少
                                        tv_usdbalance.setText(String.valueOf(usdBalance + r));   // 美金增加
                                    } else {
                                        tv_result.setText("餘額不足，交易失敗！");
                                        tv_result.setTextColor(Color.parseColor("#AA3333"));
                                    }
                                } else {   // 日圓按鈕
                                    TextView tv_jpybalance = (TextView) findViewById(R.id.jpybalance);  // 顯示日圓
                                    double jpyBalance = Double.parseDouble(tv_jpybalance.getText().toString());


                                    if (ntd_balance >= amount) {   // 台幣足夠
                                        double r = amount / rate;
                                        tv_ntdbalance.setText(String.valueOf(ntd_balance - amount));   // 台幣減少
                                        tv_jpybalance.setText(String.valueOf(jpyBalance + r));   // 日圓1增加
                                    } else {
                                        tv_result.setText("餘額不足，交易失敗！");
                                        tv_result.setTextColor(Color.parseColor("#AA3333"));
                                    }
                                }
                            }
                        }
                    }
                }
        );
    }

    public void GotoNTD(View view) {
        Intent i = new Intent(this, NTDActivity.class);
        intentActivityResultLauncher.launch(i);
    }

    public void Exchange(View view) {
        // 1.按下按鈕Ａ,傳送Apple 2.按下按鈕Ｂ,傳送Banana
        String coin;
        if (view.getId() == R.id.usdbutton) {
            coin = "USD";
        } else {
            coin = "JPY";
        }
        Intent intent = new Intent(this, ExchangeActivity.class);

        // 設定一個bundle來放資料
        Bundle bundle = new Bundle();
        bundle.putString("COIN", coin);

        // 利用intent攜帶bundle資料
        intent.putExtras(bundle);
        intentActivityResultLauncher.launch(intent);

    }


}