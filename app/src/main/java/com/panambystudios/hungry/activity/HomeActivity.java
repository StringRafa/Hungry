package com.panambystudios.hungry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.panambystudios.hungry.R;
import com.panambystudios.hungry.helper.ConfigFirebase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth authentication;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inicializarComponentes();
        authentication = ConfigFirebase.getFirebaseAuthentication();

        //Configurações TollBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hungry");
        setSupportActionBar(toolbar);
    }

    private void inicializarComponentes() {
        searchView = findViewById(R.id.materialSearchView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        //Configurar botão de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

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
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirConfiguracoes() {
        startActivity(new Intent(HomeActivity.this, ConfigUsuarioActivity.class));
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