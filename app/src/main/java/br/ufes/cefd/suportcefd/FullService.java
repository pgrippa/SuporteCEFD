package br.ufes.cefd.suportcefd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import br.ufes.cefd.suportcefd.utils.Util;

/**
 * Created by pgrippa on 17/09/16.
 */
public class FullService extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullservice);

        String patrimonio = this.getIntent().getExtras().getString("patrimonio");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarfull);


        if(toolbar != null){
            toolbar.setTitle("Chamado nº "+patrimonio);
            setSupportActionBar(toolbar);
        }else{
            System.out.println("ToolBar NULL!");
        }

        String local = this.getIntent().getExtras().getString("local");
        String responsavel = this.getIntent().getExtras().getString("responsavel");
        String descricao = this.getIntent().getExtras().getString("descricao");
        String tipo = this.getIntent().getExtras().getString("tipo");
        String data = this.getIntent().getExtras().getString("data");
        String email = this.getIntent().getExtras().getString("email");
        String telefone = this.getIntent().getExtras().getString("telefone");

        ImageView imageView = (ImageView) findViewById(R.id.i_ftipo);

        TextView p = (TextView) findViewById(R.id.l_fpatrimonio);
        p.setText(patrimonio);

        final EditText l = (EditText) findViewById(R.id.txlocal);
        l.setText(local);

        final EditText r = (EditText) findViewById(R.id.txresponsavel);
        r.setText(responsavel);

        final EditText d = (EditText) findViewById(R.id.txdescricao);
        d.setText(descricao);

        final EditText e = (EditText) findViewById(R.id.txemail1);
        e.setText(email);

        final EditText t = (EditText) findViewById(R.id.txtel);
        t.setText(telefone);

        TextView dt = (TextView) findViewById(R.id.t_data);
        dt.setText(data);

        Util.setIconByType(this,imageView,tipo);

        Button close = (Button) findViewById(R.id.closebutton);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button edit = (Button) findViewById(R.id.editbutton);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.setEnabled(true);
                r.setEnabled(true);
                d.setEnabled(true);
                e.setEnabled(true);
                t.setEnabled(true);
            }
        });
    }

}
