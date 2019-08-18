package com.inseoul.manage_member;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.inseoul.MainActivity;
import com.inseoul.R;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextView signUpBtn;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }

    public void init() {
        final EditText idText= (EditText)findViewById(R.id.input_id);
        final EditText pwText= (EditText)findViewById(R.id.input_pw);
        final Button signInBtn = (Button)findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                ////////// Log in Permission Check //////////
                String userID = idText.getText().toString();
                String userPW= pwText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            System.out.println("response: " + response);
//                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = new JSONObject(response).getBoolean("success");
                            System.out.println("success: " + success);
                            if(success)
                            {
                                AlertDialog.Builder builder= new AlertDialog.Builder(SignInActivity.this);
                                dialog=builder.setMessage("로그인에 성공하였습니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                Intent intent= new Intent(SignInActivity.this, MainActivity.class);
                                SignInActivity.this.startActivity(intent);
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder= new AlertDialog.Builder(SignInActivity.this);
                                dialog=builder.setMessage("로그인에 실패하였습니다.")
                                        .setNegativeButton("다시시도",null)
                                        .create();
                                dialog.show();

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                Login_Request loginRequest = new Login_Request(userID, userPW,responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                queue.add(loginRequest);



                ////////////////////////////////////////////

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

    @Override
    protected  void onStop() {
        super.onStop();

        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
}


