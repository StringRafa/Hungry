package com.panambystudios.hungry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.panambystudios.hungry.R;
import com.panambystudios.hungry.helper.ConfigFirebase;

public class AuthenticationActivity extends AppCompatActivity {

    private Button botaoAcesso;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportActionBar().hide();

        inicializaComponentes();
        authentication = ConfigFirebase.getFirebaseAuthentication();
        authentication.signOut();

        //Verificar usuario logado
        verificarUsuarioLogado();

        botaoAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()){
                    if (!senha.isEmpty()){

                        //Verifica estado do switch
                        if (tipoAcesso.isChecked()){ //Cadastro

                            authentication.createUserWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(AuthenticationActivity.this,
                                                "Cadastro realizado com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                            abrirTelaPrincipal();
                                    }else{

                                        String erroException = "";

                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e){
                                            erroException = "Digite uma senha mais forte";
                                        }catch (FirebaseAuthInvalidCredentialsException e){
                                            erroException = "Por favor, digite um e-mail válido!";
                                        }catch (FirebaseAuthUserCollisionException e){
                                            erroException = "Esta conta já foi cadastrada!";
                                        }catch (Exception e){
                                            erroException = "ao cadastrar usuário: " + e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(AuthenticationActivity.this,
                                                "Erro: " + erroException,
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }else{//Login

                            authentication.signInWithEmailAndPassword(
                                email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AuthenticationActivity.this,
                                                "Logado com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                        abrirTelaPrincipal();
                                    }else{
                                        Toast.makeText(AuthenticationActivity.this,
                                                "Erro ao fazer ligin:" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }else {
                        Toast.makeText(AuthenticationActivity.this,
                                "Preencha o Senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AuthenticationActivity.this,
                            "Preencha o E-mail!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void verificarUsuarioLogado() {
        FirebaseUser usuarioAtual = authentication.getCurrentUser();
        if (usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void inicializaComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcesso = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }
}