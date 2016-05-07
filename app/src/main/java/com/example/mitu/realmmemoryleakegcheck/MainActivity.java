package com.example.mitu.realmmemoryleakegcheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    EditText etName, etPhone;
    ContactModel contactModel;
    Button addbutton,button;
    private Realm myRealm;
    ListView lvContactList;
    //public  RealmResults<ContactModel> results1;
    protected io.realm.RealmResults<ContactModel> results1;
    LinearLayout linearLayout;
    private ContactAdapter contactAdapter;


    public int _id=0;
    long position_forUpdate= -1 ;
    int delete =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.editText);
        etPhone = (EditText) findViewById(R.id.editText2);
        lvContactList = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button2);
        addbutton = (Button) findViewById(R.id.addcontact);
        linearLayout = (LinearLayout) findViewById(R.id.layoutaddcontact);
        linearLayout.setVisibility(View.GONE);

        myRealm = Realm.getDefaultInstance();


        contactModel = new ContactModel();

        ///Query
        results1 = myRealm.where(ContactModel.class).findAll();

        for(ContactModel c:results1) {
            Log.d("results1", c.getmName() + c.getmPhone() + " "+c.getmId());
        }
        contactAdapter = new ContactAdapter();

        lvContactList.setAdapter(contactAdapter);

        lvContactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                _id = -1;
                position_forUpdate = id+1;


                String name = results1.get(position).getmName();
                String phone = results1.get(position).getmPhone();
                addbutton.setVisibility(view.GONE);
                linearLayout.setVisibility(view.VISIBLE);
                etName.setText(name+"");
                etPhone.setText(phone+"");

                Log.d("Position ",position+"");
                Log.d("id ",id+"");
                return true;
            }
        });
    }

    public  void addContact(View v){



        long id1;


        if (_id > -1) {
            id1 = results1.size() == 0 ? 1 : results1.size() + 1;
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            myRealm.beginTransaction();
            contactModel.setmName(name);
            contactModel.setmPhone(phone);
            contactModel.setmId( id1 );
            myRealm.copyToRealm(contactModel);


        }else if (_id == -1){


            myRealm.beginTransaction();
            contactModel.setmName(etName.getText().toString());
            contactModel.setmPhone(etPhone.getText().toString());
            contactModel.setmId(position_forUpdate);
            myRealm.copyToRealmOrUpdate(contactModel);
            position_forUpdate = -1;
            // Log.d("Update","_id "+_id+"Previous "+getname+" ,"+getPhone+","+id2+" Now :"+contactModel.getmName()+","+contactModel.getmPhone()+","+contactModel.getmId());
            _id = 0;
        }
        myRealm.commitTransaction();


        linearLayout.setVisibility(v.GONE);
        addbutton.setVisibility(v.VISIBLE);
        results1 = myRealm.where(ContactModel.class).findAll();
        contactAdapter = new ContactAdapter();

        lvContactList.setAdapter(contactAdapter);

    }

    public void newcontact(View v){
        addbutton.setVisibility(v.GONE);
        linearLayout.setVisibility(v.VISIBLE);

    }

    public void delete(View view){

        final  ContactModel  d = results1.get((int) position_forUpdate-1);
        delete = 1;
        Log.d("ID jsdkfjsdk",d.getmId()+"");

        myRealm.beginTransaction();
        d.removeFromRealm();
        myRealm.commitTransaction();
        results1 = myRealm.where(ContactModel.class).findAll();
        contactAdapter = new ContactAdapter();

        lvContactList.setAdapter(contactAdapter);
        // myRealm.close();

        linearLayout.setVisibility(view.GONE);
        addbutton.setVisibility(view.VISIBLE);


        position_forUpdate = -1;

    }




    class ContactAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (delete == 1){
                delete = 0;
                return results1.size()-1;

            }else {
                return results1.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return results1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.layout_phonebook,parent,false);

            TextView tvName = (TextView) convertView.findViewById(R.id.textView);
            TextView tvPhone = (TextView) convertView.findViewById(R.id.textView2);

            ContactModel ch = (ContactModel) getItem(position);
            tvName.setText(ch.getmName());
            tvPhone.setText(ch.getmPhone());

            return convertView;
        }
    }
}
