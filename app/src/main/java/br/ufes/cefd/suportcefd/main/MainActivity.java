package br.ufes.cefd.suportcefd.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.db.PersonDAO;
import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Person;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    Person person;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        person = (Person) this.getIntent().getExtras().getSerializable(getString(R.string.sp_person));

        prefs = getSharedPreferences(getString(R.string.sp_user), Context.MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(person.getType().equals("user")){
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_g);
            item.setVisible(false);
        }

        TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.dr_name);


        name.setText(person.getName());

        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.dr_email);
        email.setText(person.getEmail());
    }

    public void newService(View v) {
        Intent it = new Intent(MainActivity.this, NewService.class);
        it.putExtra(getString(R.string.sp_person), person);
        startActivity(it);
    }

    public void showList(View v) {
        Intent it = new Intent(MainActivity.this, List.class);
        it.putExtra(getString(R.string.sp_person), person);
        startActivity(it);
    }

    @Override
    public void finish() {

        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean("loaded",false);
        ed.commit();
        PersonDAO pd = new PersonDAO(this);
        ServiceDAO sd = new ServiceDAO(this);
        pd.clean();
        sd.clean();

        super.finish();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, Settings.class);
            i.putExtra("type",person.getType());
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_manage:
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
                break;

            case R.id.nav_logout:
                SharedPreferences prefs = getSharedPreferences(getString(R.string.sp_user), Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putBoolean(getString(R.string.sp_logged),false);
                ed.putBoolean("loaded",false);
                ed.commit();
                Intent it = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(it);
                finish();
                break;

            case R.id.nav_adduser:
                Intent it2 = new Intent(MainActivity.this, NewUser.class);
                startActivity(it2);
                break;

            case R.id.nav_exit:
                finish();
                break;

            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
