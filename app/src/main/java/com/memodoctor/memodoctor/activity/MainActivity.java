package com.memodoctor.memodoctor.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.memodoctor.memodoctor.R;
import com.memodoctor.memodoctor.adapter.MemoTaskListItemAdapter;
import com.memodoctor.memodoctor.application.MemoDoctorApplication;
import com.memodoctor.memodoctor.model.MemoTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private ListView ListTask;
    private RealmResults<MemoTask> MemoTasks;
    private LinearLayout MessageEmptyList;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        LoadListeners();
        LoadData();
        LoadMemoTask();
    }

    //EVENTOS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Ver para que es este metodo
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Aqui van las opciones del menu de los 3 puntos.
        // Metodo generico donde todas las opciones de ese menu vienen aqui y se hace un "switch" para ir enrutando cada boton a su sitio
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 1 => New Task; 2 => Edit Task
        if (resultCode == RESULT_OK && (requestCode == 1 || requestCode == 2)) {
            LoadData();
            LoadMemoTask();
        }
    }
    //END EVENTOS

    //LOGICA
    private void LoadListeners(){
        FloatingActionButton createTask = findViewById(R.id.createTask);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(new Intent(MainActivity.this, ManageTaskActivity.class), 1);
                }
                catch (Exception e){
                    MemoDoctorApplication.ShowToast(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void LoadData(){
        MemoTasks = realm.where(MemoTask.class).findAll();
    }

    private void LoadMemoTask(){
        ListTask = ListTask == null ? (ListView)findViewById(R.id.listTask) : ListTask;
        MessageEmptyList = MessageEmptyList == null ? (LinearLayout) findViewById(R.id.messageEmptyList) : MessageEmptyList;

        if (MemoTasks != null){
            MemoTaskListItemAdapter mtAdapter = new MemoTaskListItemAdapter(this, R.layout.memotask_item, MemoTasks);

            ListTask.setAdapter(mtAdapter);

            ListTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO: Aqui meter la navegacion a EDIT TASK

                    int memoTaskId = MemoTasks.get(position).GetId();

                    try {
                        Intent intent = new Intent(MainActivity.this, ManageTaskActivity.class);
                        intent.putExtra("MemoTaskId", memoTaskId);

                        startActivityForResult(intent, 2);
                    }
                    catch (Exception e){
                        MemoDoctorApplication.ShowToast(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    }
                }
            });

            if (MemoTasks.size() == 0){
                MessageEmptyList.setVisibility(View.VISIBLE);
            }
            else{
                MessageEmptyList.setVisibility(View.GONE);
            }
        }
    }
    //END LOGICA
}
