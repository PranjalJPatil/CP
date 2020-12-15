package com.example.firebaseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


class DriverAdapter extends FirestoreRecyclerAdapter<driverdetails,DriverAdapter.DriverHolder>{
    private OnItemClickListener listener;
    public DriverAdapter(@NonNull FirestoreRecyclerOptions<driverdetails> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull DriverHolder holder, int position, @NonNull driverdetails model) {
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.price.setText(String.valueOf(model.price));
    }
    @NonNull
    @Override
    public DriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_list,
                parent, false);
        return new DriverHolder(v);
    }
    class DriverHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView time;
        TextView price;
        public DriverHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION&&listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}