package com.leveloper.sns_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; // 파이어베이스 연동을 위한 변수
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();  // 파이어베이스 연동 인스턴스 초기화

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.goToLoginButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed(){ // 회원가입 화면에서 뒤로가기 눌렀을 때 프로그램 종료하기 위함
        super.onBackPressed();
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid() ); // 프로그램 강제 종료
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) { // 여러 버튼이 있을 때 확장성을 위하여 switch문 사용
            switch (view.getId()) {
                case R.id.signUpButton:
                    signUp();
                    break;
                case R.id.goToLoginButton:
                    startLoginActivity();
                    break;
            }
        }
    };

    private void signUp() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.passwordCheckEditText)).getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    startToast("회원가입에 성공하였습니다.");
                                } else {
                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }

                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
