package br.ufes.cefd.suportcefd.main;

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
import java.util.Calendar;

import br.ufes.cefd.suportcefd.R;
import br.ufes.cefd.suportcefd.utils.adapter.FullService;
import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.adapter.ServiceAdapter;
import br.ufes.cefd.suportcefd.utils.adapter.DividerItemDecoration;
import br.ufes.cefd.suportcefd.utils.RecyclerTouchListener;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Service> serviceList;
    private Person person;
    private ServiceAdapter adapter;
    private int pos = 0;
    private static int RESULT_EDIT = 28;
    private TextView empty;
    private MenuItem active;
    private MenuItem inactive;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        handleIntent(getIntent());

        person = (Person) this.getIntent().getExtras().getSerializable(getString(R.string.lsx_person));

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(R.string.t_listar_servico);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadServices(getString(R.string.ls_active));

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

    private void loadServices(String filter) {
        String type = person.getType();

        ServiceDAO serviceDAO = new ServiceDAO(getApplicationContext());

        if (filter.equals(getString(R.string.ls_active))) {
            if (type.equals(Person.TYPE.ADMIN.name())) {
                serviceList = serviceDAO.getActiveServices(true);
            } else {
                serviceList = serviceDAO.getPersonServices(person.getId(), true);
            }
            toolbar.setTitle(getString(R.string.lst_active));

        } else if (filter.equals(getString(R.string.ls_inactive))) {
            if (type.equals(Person.TYPE.ADMIN.name())) {
                serviceList = serviceDAO.getActiveServices(false);
            } else {
                serviceList = serviceDAO.getPersonServices(person.getId(), false);
            }

            toolbar.setTitle(getString(R.string.lst_inactive));
        } else {
            if (type.equals(Person.TYPE.ADMIN.name())) {
                serviceList = serviceDAO.getServices();
            } else {
                serviceList = serviceDAO.getPersonAllServices(person.getId());
            }

            toolbar.setTitle(getString(R.string.lst_all));
        }

        if (serviceList == null) {
            serviceList = new ArrayList<>();
        }

        empty = (TextView) findViewById(R.id.t_empty);
        if (serviceList == null || serviceList.isEmpty()) {
            //empty.setText(getString(R.string.t_empty));
            empty.setVisibility(View.VISIBLE);
        } else {
            //empty.setText("");
            empty.setVisibility(View.GONE);
        }

        if (adapter != null) {
            adapter.swap(serviceList);
        }
    }

    private void clickItem(int position) {
        Service e = serviceList.get(position);

        Intent it = new Intent(List.this, FullService.class);
        it.putExtra(getString(R.string.lsx_service), e);
        it.putExtra(getString(R.string.lsx_person), person);
        startActivityForResult(it, RESULT_EDIT);

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

            if (query.isEmpty()) {
                loadServices(getString(R.string.ls_active));
            } else {
                ArrayList<Service> list = serviceDAO.searchService(query);

                if (list != null) {
                    adapter.swap(list);
                    empty.setVisibility(View.GONE);
                } else {
                    adapter.swap(new ArrayList<Service>());
                    //empty.setText(getString(R.string.t_empty));
                    empty.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_EDIT) {
            if (resultCode == RESULT_OK) {
                long id = data.getExtras().getLong(getString(R.string.lsx_id));

                Service s = serviceList.get(pos);

                s.setActive(Service.INACTIVE);
                s.setRelease(Calendar.getInstance().getTime());

                ServiceDAO dao = new ServiceDAO(getApplicationContext());
                dao.updateService(id, s);

                if (active.isChecked()) {
                    //serviceList.remove(pos);
                    loadServices(getString(R.string.ls_active));
                }

                if (serviceList == null || serviceList.isEmpty()) {
                    //empty.setText(getString(R.string.t_empty));
                    empty.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finish();
                break;

            case R.id.filterAll:
                item.setChecked(true);
                loadServices(getString(R.string.ls_all));
                break;

            case R.id.filterActive:
                item.setChecked(true);
                loadServices(getString(R.string.ls_active));
                break;

            case R.id.filterInactive:
                item.setChecked(true);
                loadServices(getString(R.string.ls_inactive));
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
                if (active.isChecked()) {
                    loadServices(getString(R.string.ls_active));
                } else if (inactive.isChecked()) {
                    loadServices(getString(R.string.ls_inactive));
                } else {
                    loadServices(getString(R.string.ls_all));
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
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int pos = llm.findFirstVisibleItemPosition();

                    if (pos > 0) {
                        recyclerView.smoothScrollToPosition(--pos);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int pos = llm.findLastVisibleItemPosition();

                    if (pos < recyclerView.getChildCount()) {
                        recyclerView.smoothScrollToPosition(++pos);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}