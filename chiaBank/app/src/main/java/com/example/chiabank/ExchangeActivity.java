package com.example.chiabank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExchangeActivity extends AppCompatActivity {
    private String coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exchange);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 接受資料
        Bundle bundle = this.getIntent().getExtras();
        // 點到BtnA Textview顯示Apple, 點到BtnB TextView顯示Banana
        if(bundle != null){
             coin = String.format(bundle.getString("COIN"));

            // set name
            TextView title = (TextView) findViewById(R.id.title);
            Button tontdButton = (Button) findViewById(R.id.toNTD);
            Button ntdtoButton = (Button)findViewById(R.id.NTDto);


            if(coin.equals("USD")){
                title.setText("美金換匯");
                tontdButton.setText("美金換台幣");
                ntdtoButton.setText("新台幣換美金");
            }else{
                title.setText("日圓換匯");
                tontdButton.setText("日圓換台幣");
                ntdtoButton.setText("新台幣換日圓");
            }
        }
    }

    public  void changeCoin(View view){
        EditText et_amount = (EditText) findViewById(R.id.et_amount);
        EditText et_rate = (EditText) findViewById(R.id.et_rate);

        Intent intent = new Intent();

        if(view.getId() == R.id.toNTD){
            intent.putExtra("ACTION","toNTD");
        }else{
            intent.putExtra("ACTION","NTDto");
        }

        intent.putExtra("COIN", coin);
        intent.putExtra("AMOUNT", Double.parseDouble(et_amount.getText().toString()));
        intent.putExtra("RATE", Double.parseDouble(et_rate.getText().toString()));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}