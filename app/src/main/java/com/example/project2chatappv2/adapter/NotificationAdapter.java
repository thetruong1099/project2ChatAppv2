package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.FriendsViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    private Context context;
    private List<UserModel> listUser;
    private String myID;

    private UserViewModel userViewModel;
    private FriendsViewModel friendsViewModel;

    public NotificationAdapter(Context context, List<UserModel> listUser, String myID) {
        this.context = context;
        this.listUser = listUser;
        this.myID = myID;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageProfile;
        private TextView tvUsername;
        private Button btnDecline;
        private Button btnAccept;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UserModel userModel = listUser.get(position);
        holder.tvUsername.setText(userModel.getUsername());
        if(userModel.getImageURL().equals("default")){
            holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(userModel.getImageURL()).into(holder.imageProfile);
        }

        friendsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(FriendsViewModel.class);

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsViewModel.isFriend(myID, userModel.getId());
                friendsViewModel.isFriend(userModel.getId(), myID);
                friendsViewModel.removeInvitationFriendsList(myID, userModel.getId());
            }
        });
        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendsViewModel.removeInvitationFriendsList(myID, userModel.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


}
