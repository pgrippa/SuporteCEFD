package br.ufes.cefd.suportcefd;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.domain.Person;
import br.ufes.cefd.suportcefd.domain.Service;
import br.ufes.cefd.suportcefd.utils.ServiceAdapter;
import br.ufes.cefd.suportcefd.utils.ServiceCursorAdapter;
import br.ufes.cefd.suportcefd.utils.DividerItemDecoration;
import br.ufes.cefd.suportcefd.utils.RecyclerTouchListener;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Service> serviceList;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        person = (Person) this.getIntent().getExtras().getSerializable("person");
        String type = person.getType();

        ServiceDAO serviceDAO = new ServiceDAO(getApplicationContext());

        if(type.equals("admin")){
            serviceList = serviceDAO.getServices();
        }else{
            serviceList = serviceDAO.getPersonServices(person.getId());
        }


        TextView empty = (TextView) findViewById(R.id.t_empty);
        if(serviceList == null || serviceList.isEmpty()){
            empty.setText(getString(R.string.t_empty));
        }else{
            empty.setText("");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(R.string.t_listar_servico);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.c_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new SlideInUpAnimator());


        //ServiceCursorAdapter cursorAdapter = new ServiceCursorAdapter(this, cursor);
        ServiceAdapter adapter = new ServiceAdapter(this, serviceList);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        serviceDAO.close();
    }

    private void clickItem(int position){
        Service e = serviceList.get(position);

        Intent it = new Intent(List.this, FullService.class);
        it.putExtra("service",e);
        it.putExtra("person",person);
        startActivity(it);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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