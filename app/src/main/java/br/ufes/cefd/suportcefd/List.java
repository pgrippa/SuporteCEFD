package br.ufes.cefd.suportcefd;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.ServiceAdapter;
import br.ufes.cefd.suportcefd.utils.DividerItemDecoration;
import br.ufes.cefd.suportcefd.utils.RecyclerTouchListener;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Service> serviceList;
    private Person person;
    private ServiceAdapter adapter;
    private int pos=0;
    private static int RESULT_EDIT = 28;
    private TextView empty;
    private MenuItem all;
    private MenuItem active;
    private MenuItem inactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        handleIntent(getIntent());

        person = (Person) this.getIntent().getExtras().getSerializable("person");
        /*String type = person.getType();

        ServiceDAO serviceDAO = new ServiceDAO(getApplicationContext());

        if(type.equals("admin")){
            serviceList = serviceDAO.getServices();
        }else{
            serviceList = serviceDAO.getPersonAllServices(person.getId());
        }


        TextView empty = (TextView) findViewById(R.id.t_empty);
        if(serviceList == null || serviceList.isEmpty()){
            empty.setText(getString(R.string.t_empty));
        }else{
            empty.setText("");
        }*/

        loadServices("active");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(R.string.t_listar_servico);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.c_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        adapter = new ServiceAdapter(this, serviceList);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                pos = position;
                clickItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void loadServices(String filter){
        String type = person.getType();

        ServiceDAO serviceDAO = new ServiceDAO(getApplicationContext());

        if(filter.equals("active")){
            if(type.equals("admin")){
                serviceList = serviceDAO.getActiveServices(true);
            }else{
                serviceList = serviceDAO.getPersonServices(person.getId(), true);
            }
        }else if (filter.equals("inactive")){
            if(type.equals("admin")){
                serviceList = serviceDAO.getActiveServices(false);
            }else{
                serviceList = serviceDAO.getPersonServices(person.getId(), false);
            }
        }else{
            if(type.equals("admin")){
                serviceList = serviceDAO.getServices();
            }else{
                serviceList = serviceDAO.getPersonAllServices(person.getId());
            }
        }

        if(serviceList == null){
            serviceList = new ArrayList<>();
        }

        empty = (TextView) findViewById(R.id.t_empty);
        if(serviceList == null || serviceList.isEmpty()){
            System.out.println("LIST NULL ");
            //empty.setText(getString(R.string.t_empty));
            empty.setVisibility(View.VISIBLE);
        }else{
            System.out.println("LIST SIZE: "+serviceList.size());
            //empty.setText("");
            empty.setVisibility(View.GONE);
        }

        if(adapter!=null) {
            adapter.swap(serviceList);
        }
    }

    private void clickItem(int position){
        Service e = serviceList.get(position);

        System.out.println("SERVICE ACTIVE: "+e.getActive());

        Intent it = new Intent(List.this, FullService.class);
        it.putExtra("service",e);
        it.putExtra("person",person);
        startActivityForResult(it,RESULT_EDIT);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            ServiceDAO serviceDAO = new ServiceDAO(getApplicationContext());

            System.out.println("QUERRY: "+query);
            if (query.isEmpty()){
                loadServices("active");
            }else {
                ArrayList<Service> list = serviceDAO.searchService(query);

                if (list != null) {
                    adapter.swap(list);
                    empty.setVisibility(View.GONE);
                }else{
                    adapter.swap(new ArrayList<Service>());
                    //empty.setText(getString(R.string.t_empty));
                    empty.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RESULT_EDIT){
            if(resultCode == RESULT_OK){
                long id = data.getExtras().getLong("id");

                Service s = serviceList.get(pos);

                s.setActive(0);

                ServiceDAO dao = new ServiceDAO(getApplicationContext());
                dao.updateService(id,s);

                if(active.isChecked()){
                    serviceList.remove(pos);
                }
                adapter.swap(serviceList);

                if(serviceList == null || serviceList.isEmpty()) {
                    //empty.setText(getString(R.string.t_empty));
                    empty.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                finish();
                break;

            case R.id.filterAll:
                item.setChecked(true);
                loadServices("all");
                break;

            case R.id.filterActive:
                item.setChecked(true);
                loadServices("active");
                break;

            case R.id.filterInactive:
                item.setChecked(true);
                loadServices("inactive");
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);


        all = menu.findItem(R.id.filterAll);
        active = menu.findItem(R.id.filterActive);
        inactive = menu.findItem(R.id.filterInactive);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.search);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                if(active.isChecked()){
                    loadServices("active");
                }else if(inactive.isChecked()){
                    loadServices("inactive");
                }else{
                    loadServices("all");
                }

                return true;
            }
        });

        SearchView searchView =
                (SearchView) item.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        ListView lv;
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //Toast.makeText(getBaseContext(), "volume up button pressed", Toast.LENGTH_SHORT).show();
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int pos = llm.findFirstVisibleItemPosition();

                    if(pos>0) {
                        recyclerView.smoothScrollToPosition(--pos);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //Toast.makeText(getBaseContext(), "volume down button pressed", Toast.LENGTH_SHORT).show();
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int pos = llm.findLastVisibleItemPosition();

                    if(pos < recyclerView.getChildCount()){
                        recyclerView.smoothScrollToPosition(++pos);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}