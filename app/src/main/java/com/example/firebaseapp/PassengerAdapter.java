package com.example.firebaseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class PassengerAdapter extends FirestoreRecyclerAdapter<PassengerDetails,PassengerAdapter.PassengerHolder> {
    private OnItemClickListener listener;
    public PassengerAdapter(@NonNull FirestoreRecyclerOptions<PassengerDetails> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull PassengerAdapter.PassengerHolder holder, int position, @NonNull PassengerDetails model) {
        holder.name.setText(model.getfName());
        holder.phone.setText(model.getphoneno());
    }
    @NonNull
    @Override
    public PassengerAdapter.PassengerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_list,
                parent, false);
        return new PassengerAdapter.PassengerHolder(v);
    }



    class PassengerHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phone;

        public PassengerHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById (R.id.name);
            phone = itemView.findViewById(R.id.phoneno);
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
        void onItemClick(DocumentSnapshot documentSnapshot, int position);

    }
    public void setOnItemClickListener(PassengerAdapter.OnItemClickListener listener){
        this.listener=listener;
    }
}
