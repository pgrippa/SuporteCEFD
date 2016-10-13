package br.ufes.cefd.suportcefd;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import br.ufes.cefd.suportcefd.db.ServiceDAO;
import br.ufes.cefd.suportcefd.utils.ServiceCursorAdapter;
import br.ufes.cefd.suportcefd.utils.DividerItemDecoration;
import br.ufes.cefd.suportcefd.utils.RecyclerTouchListener;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class List extends AppCompatActivity {
    private RecyclerView recyclerView;
    //ArrayList<Service> serviceList;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        //serviceList = Util.readFromFile(this, "services.csv");
        ServiceDAO serviceDAO = new ServiceDAO(getApplicationContext());
        serviceDAO.open("read");

        cursor = serviceDAO.getServices();

        TextView empty = (TextView) findViewById(R.id.t_empty);
        if(cursor.getCount()==0){
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





        ServiceCursorAdapter cursorAdapter = new ServiceCursorAdapter(this, cursor);
        //ServiceAdapter adapter = new ServiceAdapter(this, serviceList);
        recyclerView.setAdapter(cursorAdapter);

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
        //Service e = serviceList.get(position);


        String[] columns = {"_id", "patrimony", "type", "local", "responsible", "telephone", "email", "entrydate", "description"};

        int[] cols = new int[columns.length];

        for(int i = 0; i<columns.length; i++){
            cols[i] = cursor.getColumnIndex(columns[i]);
        }

        if(cursor.moveToPosition(position)) {
            Intent it = new Intent(List.this, FullService.class);
            it.putExtra("id", cursor.getInt(cols[0]));
            it.putExtra("patrimonio", cursor.getString(cols[1]));
            it.putExtra("tipo", cursor.getString(cols[2]));
            it.putExtra("local", cursor.getString(cols[3]));
            it.putExtra("responsavel", cursor.getString(cols[4]));
            it.putExtra("telefone", cursor.getString(cols[5]));
            it.putExtra("email", cursor.getString(cols[6]));
            it.putExtra("data", cursor.getString(cols[7]));
            it.putExtra("descricao", cursor.getString(cols[8]));
            startActivity(it);
        }
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