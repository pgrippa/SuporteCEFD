package br.ufes.cefd.suportcefd.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.webservice.Tasks;

public class NewUser extends AppCompatActivity {
    String name;
    String telephone;
    String email;
    String password;
    EditText rp;
    EditText p;
    EditText n;
    EditText t;
    EditText e;
    Tasks tasks;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_user);

        init();

    }

    private void init(){
        Bundle b = this.getIntent().getExtras();
        if(b!=null){
            person = (Person) b.getSerializable(getString(R.string.sp_person));
        }

        n = (EditText) findViewById(R.id.u_name);
        t = (EditText) findViewById(R.id.u_telefone);
        e = (EditText) findViewById(R.id.u_email);
        p = (EditText) findViewById(R.id.u_password);
        rp = (EditText) findViewById(R.id.u_repeatpass);

        rp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    EditText et = (EditText) view;
                    if(!et.getText().toString().equals(p.getText().toString())) {
                        et.setError(getString(R.string.u_neq_password));
                        et.requestFocus();

                        p.setError(getString(R.string.u_neq_password));
                    }
                }
            }
        });

        if(person!=null){
            n.setText(person.getName());
            t.setText(person.getTelephone());
            e.setText(person.getEmail());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void createUser(View v){
        name = n.getText().toString();

        if(name.isEmpty()){
            n.setError(getString(R.string.error_field_required));
            return;
        }

        telephone = t.getText().toString();

        if(telephone.isEmpty()){
            t.setError(getString(R.string.error_field_required));
            return;
        }

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

        String rep = rp.getText().toString();

        if(!password.equals(rep)){
            return;
        }

        Person person = new Person(name, telephone, email, password, "user");

        tasks = new Tasks(this);
        tasks.execNewUser(person);

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
