package br.ufes.cefd.suportcefd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufes.cefd.suportcefd.db.PersonDAO;
import br.ufes.cefd.suportcefd.domain.Person;

public class NewUser extends AppCompatActivity {
    String name;
    String telephone;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void createUser(View v){
        EditText n = (EditText) findViewById(R.id.u_name);
        name = n.getText().toString();

        EditText t = (EditText) findViewById(R.id.u_telefone);
        telephone = t.getText().toString();

        EditText e = (EditText) findViewById(R.id.u_email);
        email = e.getText().toString();

        EditText p = (EditText) findViewById(R.id.u_password);
        password = p.getText().toString();

        EditText rp = (EditText) findViewById(R.id.u_repeatpass);
        String rePassword = rp.getText().toString();

        if(!password.equals(rePassword)){
            p.setError("Senhas devem ser iguais");
            p.setError("");
        }

        PersonDAO dao = new PersonDAO(getApplicationContext());

        Person person = new Person(name, telephone, email, password, "user");

        System.out.println("Person: "+ person.getName()+"\n"+ person.getEmail()+"\n"+ person.getPassword());

        dao.putPerson(person);

        Toast.makeText(getBaseContext(), "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

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
