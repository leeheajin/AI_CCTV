package com.example.aicctv;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Contact extends AppCompatActivity {

    private EditText name, number;
    private Button addbutton, delbutton;
    private SearchView searchView;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("00gpwls00");

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList = new ArrayList<String>();

    private int selectedPosition;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        number = (EditText) findViewById(R.id.numberinput);

        listView = (ListView) findViewById(R.id.addresslist);

        initDatabase();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        listView.setAdapter(adapter);

        //searchView 구현 코드
        searchView = (SearchView)findViewById(R.id.addressSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        //추가 버튼 작동 코드
        addbutton = (Button) findViewById(R.id.btn_add);
        addbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상위 Value와 number의 Value값을 동일하게 저장
                databaseReference.child("Contact_number").child(number.getText().toString()).child("number").setValue(number.getText().toString());
                //databaseReference.child("Contact_number").push().child("number").setValue(number.getText().toString());
                //databaseReference.child("Contact_number").child(name.getText().toString()).setValue(number.getText().toString());
                number.setText("");
            }
        });

        //list의 항목들 선택 코드
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //선택한 항목의 position 저장한 후 listView에 선택 설정
                selectedPosition = position;
                listView.setItemChecked(selectedPosition, true);
                Toast.makeText(Contact.this, arrayList.get(selectedPosition)+" 선택", Toast.LENGTH_SHORT).show();
            }
        });


        //삭제 버튼 작동 코드
        delbutton = (Button) findViewById(R.id.btn_del);
        delbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //선택되어 있는 항목 제거
                databaseReference.child("Contact_number").child(arrayList.get(selectedPosition)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Contact.this, "선택 항목 삭제", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //firebase의 database를 listview로 표현
        databaseReference.child("Contact_number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot numberData : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.
                    String msg2 = (String) numberData.child("number").getValue();
                    adapter.add(msg2);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        });
    }
    //불러올 database 초기화 코드
    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
//        mReference = mDatabase.getReference("log");
//        mReference.child("log").setValue("check");
        mChild = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {           }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        };
//        mReference.addChildEventListener(mChild);
    }


}
