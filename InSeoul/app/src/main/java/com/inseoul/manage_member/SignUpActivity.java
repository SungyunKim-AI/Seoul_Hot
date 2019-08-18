package com.inseoul.manage_member;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.inseoul.R;

public class SignUpActivity extends AppCompatActivity {

    private Button sign_up_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }
    public void init(){
        sign_up_submit = (Button)findViewById(R.id.sign_up_submit);
        sign_up_submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                ////////// Connect DB //////////


                ////////////////////////////////
                finish();
            }
        });
    }
}
