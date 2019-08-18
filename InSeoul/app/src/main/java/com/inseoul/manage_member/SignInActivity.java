package com.inseoul.manage_member;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.inseoul.MainActivity;
import com.inseoul.R;

public class SignInActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextView signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }

    public void init() {
        signInBtn = (Button)findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                ////////// Log in Permission Check //////////


                /////////////////////////////////////////////
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUpBtn = (TextView)findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
