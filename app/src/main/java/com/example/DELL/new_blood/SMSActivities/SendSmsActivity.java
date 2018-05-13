package com.example.DELL.new_blood.SMSActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.DELL.new_blood.R;

public class SendSmsActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    Button sendSMSButton;

    String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);


        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        sendSMSButton = findViewById(R.id.sendSMSButton);

        sendSMSButton.setOnClickListener(sendSMSListener);
    }

    View.OnClickListener sendSMSListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            phoneNumber = phoneNumberEditText.getText().toString();

            startNextActivity(phoneNumber);
        }
    };

    private void startNextActivity(String phone)
    {

        Intent intent = new Intent(SendSmsActivity.this,VerifyNumberActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber",phone);
        intent.putExtras(bundle);

        startActivity(intent);

        finish();
    }
}
