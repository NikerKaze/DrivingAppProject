package com.example.firebase2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private List<User> userList;

    public UserAdapter(@NonNull Context context, @NonNull List<User> list) {
        super(context, 0, list);
        mContext=context;
        userList=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        }

        User currentUser = userList.get(position);

        TextView name = listItem.findViewById(R.id.tvName);
        name.setText(currentUser.getName());

        TextView phone = listItem.findViewById(R.id.tvPhone);
        phone.setText(currentUser.getPhone());

        return listItem;
    }
}
