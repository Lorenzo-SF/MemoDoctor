package com.memodoctor.memodoctor.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.memodoctor.memodoctor.R;
import com.memodoctor.memodoctor.adapter.MemoTypeSpinnerItemAdapter;
import com.memodoctor.memodoctor.application.MemoDoctorApplication;
import com.memodoctor.memodoctor.model.MemoTask;
import com.memodoctor.memodoctor.model.MemoType;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

public class ManageTaskActivity extends AppCompatActivity {
    private RealmResults<MemoType> MemoTypes;
    private MemoTask MemoTask;
    private int MemoTaskId = -1;
    private int MemoTypeId = -1;
    private Spinner TypesSpinner;
    private EditText MemoTaskDate;
    private EditText MemoTaskTime;
    private EditText MemoTaskObservations;
    private DatePickerDialog Fecha;
    private TimePickerDialog Hora;
    private TextView TitleTask;
    private Button AddTask;
    private Button DeleteTask;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);

        realm = Realm.getDefaultInstance();
        MemoTypes = realm.where(MemoType.class).findAll();

        LoadLayout();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){ // Si viene bundle, es edicion. De lo contrario es creacion.
            MemoTaskId = bundle.getInt("MemoTaskId");
            setTitle("Editar Cita");
            TitleTask.setText("Editar Cita");
            DeleteTask.setVisibility(View.VISIBLE);
            LoadMemoTask();
        }
        else
        {
            setTitle("Nueva Cita");
            TitleTask.setText("Nueva Cita");
            DeleteTask.setVisibility(View.INVISIBLE);
        }

        LoadListeners();
    }

    private void LoadMemoTask(){
        if(MemoTaskId == -1) { return; }

        MemoTask = realm.where(MemoTask.class).equalTo("Id", MemoTaskId).findAll().get(0);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        cal.setTime(MemoTask.GetMemoDate());
        int actualDay = cal.get(Calendar.DATE);
        int actualMonth = cal.get(Calendar.MONTH);
        actualMonth = actualMonth == 12 ? 1 : actualMonth;
        int actualYear = cal.get(Calendar.YEAR);
        actualYear = actualMonth == 12 ? actualYear + 1 : actualYear;

        int actualHour = cal.get(Calendar.HOUR_OF_DAY);
        int actualMinute = cal.get(Calendar.MINUTE);

        MemoTaskDate.setText((actualDay < 10 ? "0" + actualDay : actualDay) + "/" + (actualMonth < 10 ? "0" + actualMonth : actualMonth) + "/" + actualYear);
        MemoTaskTime.setText((actualHour < 10 ? "0" + actualHour : actualHour) + ":" + (actualMinute < 10 ? "0" + actualMinute : actualMinute));
        MemoTaskObservations.setText(MemoTask.GetObservations());
        MemoTypeId = MemoTask.GetMemoType().GetId() - 1;
    }

    private void LoadLayout(){
        TitleTask = TitleTask == null ? (TextView) findViewById(R.id.TitleTask) : TitleTask;

        TypesSpinner = TypesSpinner == null ? (Spinner) findViewById(R.id.typesSpinner) : TypesSpinner;
        MemoTypeSpinnerItemAdapter adapter = new MemoTypeSpinnerItemAdapter(this, R.layout.memotype_item, MemoTypes);
        TypesSpinner.setAdapter(adapter);
        if(MemoTypeId != -1){
            TypesSpinner.setSelection(MemoTypeId);
        }

        MemoTaskDate = MemoTaskDate == null? (EditText) findViewById(R.id.MemoTaskDate) : MemoTaskDate;
        MemoTaskTime = MemoTaskTime == null ? (EditText) findViewById(R.id.MemoTaskTime) : MemoTaskTime;
        MemoTaskObservations = MemoTaskObservations == null ? (EditText) findViewById(R.id.MemoTaskObservations) : MemoTaskObservations;
        AddTask = AddTask == null ? (Button) findViewById(R.id.AddTask) : AddTask;
        DeleteTask =  DeleteTask == null ? (Button) findViewById(R.id.DeleteTask) : DeleteTask;
    }

    private void LoadListeners() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
        Date date = calendar.getTime();
        final int actualDay = calendar.get(Calendar.DATE);
        final int actualMonth = calendar.get(Calendar.MONTH);
        final int actualYear = calendar.get(Calendar.YEAR);
        final int actualHour = calendar.get(Calendar.HOUR);
        final int actualMinute = calendar.get(Calendar.MINUTE);

        Fecha = new DatePickerDialog(ManageTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                SetFecha(year, month, day);
            }
        }, actualYear, actualMonth, actualDay);

        Hora = new TimePickerDialog(ManageTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                SetHora(hour, minute);
            }
        }, actualHour, actualMinute, true);

        // LISTENER DEL CAMPO FECHA
        MemoTaskDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Fecha.show();
                return true;
            }
        });

        // LISTENER DEL CAMPO HORA
        MemoTaskTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Hora.show();
                return true;
            }
        });

        AddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidateForm()){
                    String[] date = MemoTaskDate.getText().toString().split("/");
                    String[] hour = MemoTaskTime.getText().toString().split(":");
                    TimeZone timeZone = TimeZone.getTimeZone("Europe/Madrid");

                    final String o = MemoTaskObservations.getText().toString();
                    final MemoType type = MemoTypes.get((int) TypesSpinner.getSelectedItemId());
                    final Calendar cal = Calendar.getInstance(timeZone);

                    cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]));

                    try {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                final MemoTask mt;
                                if(MemoTaskId == -1){
                                    mt = new MemoTask(o, cal.getTime(), type);
                                }
                                else{
                                    mt = MemoTask;
                                    mt.SetObservations(o);
                                    mt.SetMemoDate(cal.getTime());
                                    mt.SetMemoType(type);
                                }
                                realm.copyToRealmOrUpdate(mt);
                            }
                        });

                        setResult(RESULT_OK, new Intent());
                        finish();
                    } catch (Exception e) {
                        setResult(RESULT_CANCELED, new Intent());
                        finish();
                    }
                }
            }
        });

        DeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        MemoTask.deleteFromRealm();

                        setResult(RESULT_OK, new Intent());
                        finish();
                    }
                });
            }
        });
    }

    private void SetFecha(int year, int month, int day){
        month = month == 12 ? 1 : month + 1;
        String d = day < 10? "0" + day : Integer.toString(day);
        String m = month < 10? "0" + month : Integer.toString(month);

        MemoTaskDate.setText(d + "/" + m + "/" + year);
    }

    private void SetHora(int hour, int minute){
        String h = hour < 10? "0" + hour : Integer.toString(hour);
        String m = minute < 10? "0" + minute : Integer.toString(minute);

        MemoTaskTime.setText(h + ":" + m);
    }

    private boolean ValidateForm(){
        int typeId = (int) TypesSpinner.getSelectedItemId() + 1;
        String f = MemoTaskDate.getText().toString().trim();
        String h = MemoTaskTime.getText().toString().trim();

        if(typeId == 1){
            MemoDoctorApplication.ShowToast(ManageTaskActivity.this, "Seleccione un tipo de cita", Toast.LENGTH_LONG);
            return false;
        } else if(IsNullOrEmpty(f)){
            MemoDoctorApplication.ShowToast(ManageTaskActivity.this, "Indique fecha", Toast.LENGTH_LONG);
            return false;
        } else if(IsNullOrEmpty(h)){
            MemoDoctorApplication.ShowToast(ManageTaskActivity.this, "Indique hora", Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

    public static boolean IsNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
