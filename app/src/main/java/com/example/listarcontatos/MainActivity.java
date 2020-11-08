package com.example.listarcontatos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spnNome;
    private Button btnContatos;
    private Cursor cursor;
    private boolean carregouContatos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spnNome=(Spinner)findViewById(R.id.spnNome);
        btnContatos = (Button)findViewById(R.id.btnContatos);

        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.READ_CONTACTS},1);

        spnNome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicaoSelecionada, long l) {
                spnNome.setSelection(posicaoSelecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        btnContatos.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (carregouContatos == false)
                {
                    Uri caminho = ContactsContract.Contacts.CONTENT_URI;
                    ContentResolver contentResolver = MainActivity.this.getContentResolver();
                    cursor = contentResolver.query(caminho, null, null, null,ContactsContract.Contacts.DISPLAY_NAME);
                    if (cursor.getCount() > 0)
                    {
                        List<String> nomesContatos = new ArrayList<String>();
                        while (cursor.moveToNext())
                        {
                                String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                nomesContatos.add(nome);
                                String contatoId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                                Cursor numero = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contatoId, null, null);
                                numero.moveToNext();
                            
                        }
                        ArrayAdapter<String> dataAdapterNomes = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_item, nomesContatos);
                        dataAdapterNomes.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spnNome.setAdapter(dataAdapterNomes);
                        carregouContatos = true;
                    }
                }
            }
        });
    }
}
