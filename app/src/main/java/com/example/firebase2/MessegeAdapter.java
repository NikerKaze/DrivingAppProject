package com.example.firebase2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessegeAdapter extends ArrayAdapter<Messege> {

    private Context mContext;
    private List<Messege> msgList;

    public MessegeAdapter(@NonNull Context context, @NonNull List<Messege> list) {
        super(context, 0, list);
        mContext=context;
        msgList=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem=convertView;
        if(listItem==null) {
            listItem=LayoutInflater.from(mContext).inflate(R.layout.item_messege, parent, false);
        }

        Messege currentMsg=msgList.get(position);

        TextView name=listItem.findViewById(R.id.tvName);;

        String uid=currentMsg.getSender();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ValueEventListener UserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getUid().equals(uid)) {
                    user = dataSnapshot.getValue(User.class);
                    name.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        db.addValueEventListener(UserListener);

        TextView message=listItem.findViewById(R.id.tvMsg);
        message.setText(currentMsg.getMessege());


        return listItem;
    }
}
