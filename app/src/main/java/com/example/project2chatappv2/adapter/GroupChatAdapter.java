package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.GroupChatModel;
import com.example.project2chatappv2.view.activity.GroupChatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {

    private Context context;
    private List<GroupChatModel> groupChatModelList;

    public GroupChatAdapter(Context context, List<GroupChatModel> groupChatModelList) {
        this.context = context;
        this.groupChatModelList = groupChatModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageProfile;
        private TextView tvUsername;
        private TextView tvSender;
        private TextView tvMessage;
        private TextView tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);
        return new GroupChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupChatModel groupChatModel = groupChatModelList.get(position);
        if(groupChatModel.getImageURL().equals("default")){
            holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(groupChatModel.getImageURL()).into(holder.imageProfile);
        }
        holder.tvUsername.setText(groupChatModel.getGroupName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupChatActivity.class);
                intent.putExtra("groupID", groupChatModel.getGroupID());
                intent.putExtra("imageURL", groupChatModel.getImageURL());
                intent.putExtra("groupName", groupChatModel.getGroupName());
                intent.putStringArrayListExtra("member", groupChatModel.getMember());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupChatModelList.size();
    }


}
