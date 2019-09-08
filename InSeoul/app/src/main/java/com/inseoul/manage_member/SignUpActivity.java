package com.inseoul.manage_member;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.inseoul.R;
import com.inseoul.Server.RegisterRequest;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private Button sign_up_submit;
    private String userID;
    private String userPW;
    private String userEMAIL;
    private String userName;
    private AlertDialog dialog;
    private boolean validate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }
    public void init(){
        final EditText idText =(EditText) findViewById( R.id.id);
        final EditText pwText = (EditText) findViewById(R.id.pw);
        final EditText nmText = findViewById(R.id.name);
        pwText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final EditText emailText = (EditText)findViewById(R.id.email);
        final Button validateButton = findViewById(R.id.validateBtn);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = idText.getText().toString();
                if(validate){
                    return;
                }
                if(userID.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    dialog = builder.setMessage("빈칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success= jsonResponse.getBoolean("success");
                            if(success)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디 입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate= true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디 입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                queue.add(validateRequest);
            }
        });


                sign_up_submit = (Button)findViewById(R.id.sign_up_submit);
                sign_up_submit.setOnClickListener(new Button.OnClickListener(){

                ////////// Connect DB //////////
                @Override
                public void onClick(View view) {
                    userID = idText.getText().toString();
                    userPW = pwText.getText().toString();
                    userEMAIL = emailText.getText().toString();
                    userName = nmText.getText().toString();


                    if(!validate)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        dialog = builder.setMessage("먼저 중복확인을 해주세요.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    if(userID.equals("")||userPW.equals("")||userEMAIL.equals(""))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        dialog = builder.setMessage("빈 칸 없이 입력해주세요")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try
                            {
                                Log.d("d",response);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success= jsonResponse.getBoolean("success");
                                if(success)
                                {
                                    Toast.makeText(SignUpActivity.this,"회원가입이 완료 되었습니다",Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                                else{
                                    Toast.makeText(SignUpActivity.this,"회원가입이 실패 하였습니다.",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPW, userEMAIL, userName, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                    queue.add(registerRequest);


                ////////////////////////////////
                finish();
            }
        });
    }
}
