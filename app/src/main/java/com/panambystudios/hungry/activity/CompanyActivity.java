package com.panambystudios.hungry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.panambystudios.hungry.R;
import com.panambystudios.hungry.helper.ConfigFirebase;

public class CompanyActivity extends AppCompatActivity {

    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        authentication = ConfigFirebase.getFirebaseAuthentication();

        //Configurações TollBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hungry - Empresa");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                break;
            case R.id.menuConfiguracoes:
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto:
                abrirNovoProduto();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirNovoProduto() {
        startActivity(new Intent(CompanyActivity.this, NewProductCompanyActivity.class));
    }

    private void abrirConfiguracoes() {
        startActivity(new Intent(CompanyActivity.this, ConfigCompanyActivity.class));
    }

    private void deslogarUsuario() {
        try {
            authentication.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}