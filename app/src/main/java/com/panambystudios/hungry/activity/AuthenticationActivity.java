package com.panambystudios.hungry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.panambystudios.hungry.R;
import com.panambystudios.hungry.helper.ConfigFirebase;
import com.panambystudios.hungry.helper.UsuarioDatabase;
import com.panambystudios.hungry.helper.UsuarioFirebase;

public class AuthenticationActivity extends AppCompatActivity {

    private Button botaoAcesso;
    private EditText campoEmail, campoSenha, campoNome, campoEndereco, campoTelefone;
    private Switch tipoAcesso, tipoUsuario;
    private LinearLayout linearTipoUsuario;

    private FirebaseAuth authentication;
    private DatabaseReference referenceDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
//        getSupportActionBar().hide();


        inicializaComponentes();
        authentication = ConfigFirebase.getFirebaseAuthentication();
        //authentication.signOut();

        //Verificar usuario logado
        verificarUsuarioLogado();



        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){//Empresa
                    linearTipoUsuario.setVisibility(View.VISIBLE);
                    campoNome.setVisibility(View.VISIBLE);
                    campoEndereco.setVisibility(View.VISIBLE);
                    campoTelefone.setVisibility(View.VISIBLE);
                }else {//Usuário
                    linearTipoUsuario.setVisibility(View.GONE);
                    campoNome.setVisibility(View.GONE);
                    campoEndereco.setVisibility(View.GONE);
                    campoTelefone.setVisibility(View.GONE);
                }
            }
        });

        botaoAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                String nome = campoNome.getText().toString();
                String endereco = campoEndereco.getText().toString();
                String telefone = campoTelefone.getText().toString();

                DatabaseReference empresas = referenceDatabase.child("empresas");
                DatabaseReference usuarios = referenceDatabase.child("usuarios");
                UsuarioDatabase usuarioDatabase = new UsuarioDatabase(nome, email, endereco, telefone);
//                FirebaseUser usuarioAtual = authentication.getCurrentUser();
//                String tipoUsuario = usuarioAtual.getDisplayName();

                if (getTipoUsuario().equals("E")){//Empresa
                    empresas.push().setValue(usuarioDatabase);
                }else{//Usuário
                    usuarios.push().setValue(usuarioDatabase);
                }


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
                                        String tipoUsuario = getTipoUsuario();
                                        UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                        abrirTelaPrincipal(tipoUsuario);
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
                                        String tipoUsuatio = task.getResult().getUser().getDisplayName();
                                        abrirTelaPrincipal(tipoUsuatio);
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
            String tipoUsuario = usuarioAtual.getDisplayName();
            abrirTelaPrincipal(tipoUsuario);
        }
    }

    private String getTipoUsuario(){
        return tipoUsuario.isChecked() ? "E":"U";
    }

    private void abrirTelaPrincipal(String tipoUsuario){
        if (tipoUsuario.equals("E")){//Empresa
            startActivity(new Intent(getApplicationContext(), CompanyActivity.class));
        }else{//Usuário
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    private void inicializaComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        campoNome = findViewById(R.id.editTextName);
        campoEndereco = findViewById(R.id.editTextEndereco);
        campoTelefone = findViewById(R.id.editTextTelefone);
        botaoAcesso = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
        tipoUsuario = findViewById(R.id.switchTipoUsuario);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);
    }

    public void verificaBotao(View view){
        if (tipoAcesso.isChecked()){
            botaoAcesso.setText("CADASTRAR");
        }else{
            botaoAcesso.setText("ACESSAR");
        }
    }
}