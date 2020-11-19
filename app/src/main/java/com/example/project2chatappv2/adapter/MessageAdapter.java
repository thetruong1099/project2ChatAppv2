package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.MessageModel;
import com.example.project2chatappv2.model.UserModel;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<MessageModel> listMess;
    private List<UserModel> listMember;
    private String imageurl;
    private String userID;

    public MessageAdapter(Context context, List<MessageModel> listMess, String imageurl, String userID) {
        this.context = context;
        this.listMess = listMess;
        this.imageurl = imageurl;
        this.userID = userID;
    }

    public MessageAdapter(Context context, List<MessageModel> listMess, List<UserModel> listMember, String userID) {
        this.context = context;
        this.listMess = listMess;
        this.userID = userID;
        this.listMember = listMember;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;
        private ImageView image_profile;
        private TextView tvTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tvMessage);
            image_profile = itemView.findViewById(R.id.image_profile);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(listMess.get(position).getSender().equals(userID)){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel messageModel = listMess.get(position);

        for (int i = 0; i<listMember.size(); i++){
            if (messageModel.getSender().equals(listMember.get(i).getId())){
                imageurl = listMember.get(i).getImageURL();
                break;
            }
        }

        holder.tvTime.setText(getDate(messageModel.getTime()));

        holder.tvMessage.setText(messageModel.getMessage());

        if(imageurl.equals("default")){
            holder.image_profile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(context).load(imageurl).into(holder.image_profile);
        }

    }

    private String getDate(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return listMess.size();
    }


}
