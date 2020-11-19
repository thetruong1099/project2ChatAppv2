package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.MessageActivity;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiverMessageAdapter extends RecyclerView.Adapter<ReceiverMessageAdapter.ViewHolder> {

    private List<UserModel> listUser;
    private List<MessageModel> listMessage;
    private String myID;
    private Context context;


    public ReceiverMessageAdapter(List<UserModel> listUser, Context context, List<MessageModel> listMessage, String myID) {
        this.listUser = listUser;
        this.context = context;
        this.listMessage = listMessage;
        this.myID = myID;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageProfile;
        private TextView tvUsername;
        private TextView tvMessgae;
        private TextView tvTime;
        private TextView tvIsMe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            imageProfile = itemView.findViewById(R.id.image_profile);
            tvMessgae = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvIsMe = itemView.findViewById(R.id.tvIsMe);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.receiver_message_item, parent, false);
        return new ReceiverMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel user= listUser.get(position);
        MessageModel lastMessage = listMessage.get(position);

        holder.tvUsername.setText(user.getUsername());

        if(user.getImageURL().equals("default")){
            holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.imageProfile);
        }

        if (lastMessage.getSender().equals(myID)) holder.tvIsMe.setVisibility(View.VISIBLE);

        holder.tvMessgae.setText(lastMessage.getMessage());
        holder.tvTime.setText(getDate(lastMessage.getTime())+" h");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("image_profile", user.getImageURL());
                context.startActivity(intent);
            }
        });

    }

    private String getDate(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy,  hh", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


}
