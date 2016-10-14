package br.ufes.cefd.suportcefd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button cadastrar = (Button) findViewById(R.id.b_cadastro);
        if (cadastrar != null) {
            cadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(MenuActivity.this, NewService.class);
                    startActivity(it);
                }
            });
        }

        Button listar = (Button) findViewById(R.id.b_listar);
        if (listar != null) {
            listar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it2 = new Intent(MenuActivity.this, List.class);
                    startActivity(it2);
                }
            });
        }
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
