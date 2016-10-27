package br.ufes.cefd.suportcefd.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.db.PersonDAO;
import br.ufes.cefd.suportcefd.domain.Person;

public class NewUser extends AppCompatActivity {
    String name;
    String telephone;
    String email;
    String password;
    EditText rp;
    EditText p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_user);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarnewuser);
        toolbar.setTitle(getString(R.string.t_cadastrar_usuario));*/
        ///setSupportActionBar(toolbar);
        p = (EditText) findViewById(R.id.u_password);
        rp = (EditText) findViewById(R.id.u_repeatpass);
        rp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    EditText et = (EditText) view;
                            et.setError(getString(R.string.u_neq_password));
                    et.requestFocus();

                    p.setError(getString(R.string.u_neq_password));
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void createUser(View v){
        EditText n = (EditText) findViewById(R.id.u_name);
        name = n.getText().toString();

        if(name.isEmpty()){
            n.setError(getString(R.string.error_field_required));
            return;
        }

        EditText t = (EditText) findViewById(R.id.u_telefone);
        telephone = t.getText().toString();

        if(telephone.isEmpty()){
            t.setError(getString(R.string.error_field_required));
            return;
        }

        EditText e = (EditText) findViewById(R.id.u_email);
        email = e.getText().toString();

        if(!email.contains("@")){
            e.requestFocus();
            e.setError(getString(R.string.error_invalid_email));
            return;
        }else if(email.isEmpty()){
            e.setError(getString(R.string.error_field_required));
            return;
        }

        password = p.getText().toString();

        if(password.isEmpty()){
            e.setError(getString(R.string.error_field_required));
            return;
        }

        EditText rp = (EditText) findViewById(R.id.u_repeatpass);
        String rep = rp.getText().toString();

        if(!password.equals(rep)){
            return;
        }

        PersonDAO dao = new PersonDAO(getApplicationContext());

        Person person = new Person(name, telephone, email, password, "user");

        dao.putPerson(person);

        Toast.makeText(getBaseContext(), "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();

        intent.putExtra("email",email);
        setResult(RESULT_OK,intent);

        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
