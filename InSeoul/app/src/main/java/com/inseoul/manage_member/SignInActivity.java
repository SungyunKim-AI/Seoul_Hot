package com.inseoul.manage_member;

import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.inseoul.MainActivity;
import com.inseoul.R;
import com.inseoul.Server.Login_Request;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private TextView signUpBtn;
    private TextView notMember;
    private Dialog dialog;

    private EditText idText;
    private EditText pwText;
    private Button signInBtn;
    private  CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }

    public void init() {

        idText= (EditText)findViewById(R.id.input_id);
        pwText= (EditText)findViewById(R.id.input_pw);
        pwText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        signInBtn = (Button)findViewById(R.id.signInBtn);
        checkBox = (CheckBox)findViewById(R.id.autoLogin);

        idText.setOnEditorActionListener(this);
        pwText.setOnEditorActionListener(this);

        signInBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                permission();
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
        notMember = (TextView)findViewById(R.id.notMember);
        notMember.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void permission(){

        ////////// Log in Permission Check //////////
        String userID = idText.getText().toString();
        String userPW= pwText.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //SharedPreferences id = getSharedPreferences("InSeoul",0);
                //SharedPreferences.Editor editor = id.edit();
                try{
                    System.out.println("response: " + response);
                    //JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject success= jsonResponse.getJSONObject("response");
                    System.out.println("success: " + success);
                    if(success.getBoolean("success"))
                    {

//                        editor.putString("UserID", idText.getText().toString());
//                        editor.putString("UserName", success.getString("NAME"));
//                        editor.putString("UserEmail", success.getString("EMAIL"));
//                        editor.putInt("IDNum",  success.getInt("IDNUM"));

//                        Log.d("alert_name",success.getString("NAME"));
//                        Log.d("alert_name",success.getString("EMAIL"));

                        if(checkBox.isChecked()){
                            SaveSharedPreference.setUserID(SignInActivity.this, idText.getText().toString(),success.getInt("IDNUM"),true);
                            SaveSharedPreference.setUserInfo(SignInActivity.this,success.getString("NAME"),success.getString("EMAIL"));
                            successLog();
                        }else{
                            SaveSharedPreference.setUserID(SignInActivity.this, idText.getText().toString(),success.getInt("IDNUM"),false);
                            SaveSharedPreference.setUserInfo(SignInActivity.this,success.getString("NAME"),success.getString("EMAIL"));
                            successLog();
                        }
                        //editor.commit();
                    }
                    else
                    {
                        AlertDialog.Builder builder= new AlertDialog.Builder(SignInActivity.this);
                        dialog=builder.setMessage("아이디 또는 비밀번호가 맞지 않습니다")
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

    public void successLog(){
        Intent intent= new Intent(SignInActivity.this, MainActivity.class);
        SignInActivity.this.startActivity(intent);
        finish();
    }

    @Override
    protected  void onStop() {
        super.onStop();

        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        if (EditorInfo.IME_ACTION_NEXT == i){
            pwText.requestFocus();
        }else if (EditorInfo.IME_ACTION_DONE == i){
            permission();
        }
        else{
            return false;
        }
        return true;
    }
}


