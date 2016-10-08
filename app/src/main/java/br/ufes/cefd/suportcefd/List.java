package br.ufes.cefd.suportcefd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.ufes.cefd.suportcefd.model.Service;
import br.ufes.cefd.suportcefd.model.ServiceAdapter;
import br.ufes.cefd.suportcefd.utils.DividerItemDecoration;
import br.ufes.cefd.suportcefd.utils.RecyclerTouchListener;
import br.ufes.cefd.suportcefd.utils.Util;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class List extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        serviceList = Util.readFromFile(this, "services.csv");

        TextView empty = (TextView) findViewById(R.id.t_empty);
        if(serviceList.isEmpty()){
            empty.setText(getString(R.string.t_empty));
        }else{
            empty.setText("");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(R.string.t_listar_servico);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.c_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new SlideInUpAnimator());

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
    }


    private void clickItem(int position){
        Service e = serviceList.get(position);

        Intent it = new Intent(List.this, FullService.class);
        it.putExtra("patrimonio",e.getPatrimony().toString());
        it.putExtra("local",e.getLocal().toString());
        it.putExtra("responsavel",e.getResponsible().toString());
        it.putExtra("descricao",e.getDescription().toString());
        it.putExtra("tipo",e.getType().toString());
        it.putExtra("data",e.getEntryDate());
        it.putExtra("email", e.getEmail());
        it.putExtra("telefone",e.getTelephone());
        startActivity(it);
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