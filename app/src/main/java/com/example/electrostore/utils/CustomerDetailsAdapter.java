package com.example.electrostore.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electrostore.R;
import com.example.electrostore.activities.ProductDetailsActivity;
import com.example.electrostore.classes.Product;
import com.example.electrostore.classes.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomerDetailsAdapter extends RecyclerView.Adapter<CustomerDetailsAdapter.MyViewHolder> {
    private ArrayList<User> mylistvalues;
    private DatabaseReference userDB;
    private StorageReference storageReference;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, emailView, addressView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameDetailsRow);
            emailView = itemView.findViewById(R.id.emailDetailsRow);
            addressView = itemView.findViewById(R.id.addressDetailsRow);
        }
    }

    public CustomerDetailsAdapter(ArrayList<User> myDataset, Context context) {
        mylistvalues = myDataset;
        this.context = context;
    }

    @Override
    public CustomerDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.customerdetails_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        User user = mylistvalues.get(position);
        holder.nameView.setText("Name: " + user.getName());
        holder.emailView.setText("Email: " + user.getEmail());
        holder.addressView.setText("Address: " + user.getAddress());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("FAVOURITES PRODUCT", product.getName());
//                Intent intent = new Intent(context, ProductDetailsActivity.class);
//                intent.putExtra("PRODUCT_INTENT", product);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mylistvalues.size();
    }
}
