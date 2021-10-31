package com.example.childfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyAllChild extends AppCompatActivity {

    ImageView regArrowBackMyAllChild;

    String parentCurrentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    View viewHijab;
    RecyclerView.LayoutManager layoutManager;
    Query childReference;

    String tempParentFullPostID;

    private RecyclerView myRecyclerViewChild;
    FirebaseRecyclerAdapter<ChildModel, MyAllChildViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_child);



        Intent intent = getIntent();
        tempParentFullPostID = intent.getStringExtra("ParentID");


        regArrowBackMyAllChild = (ImageView) findViewById(R.id.arrowBackMyAllChild);
        regArrowBackMyAllChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (tempParentFullPostID == null) {
            childReference = FirebaseDatabase.getInstance().getReference()
                    .child("Child").orderByChild("ParentID").equalTo(parentCurrentId);


        }
        else {

            childReference = FirebaseDatabase.getInstance().getReference()
                    .child("Child").orderByChild("ParentID").equalTo(tempParentFullPostID);

        }

        myRecyclerViewChild = (RecyclerView) findViewById(R.id.myAllChildRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        myRecyclerViewChild.setLayoutManager(layoutManager);

        showList();


    }

    private void showList() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChildModel>()
                .setQuery(childReference, ChildModel.class).build();

        adapter = new FirebaseRecyclerAdapter<ChildModel, MyAllChildViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MyAllChild.MyAllChildViewHolder holder, int position,ChildModel childModel) {

                holder.textViewChildName.setText(childModel.getChildName());
                holder.textViewChildPhoneNumber.setText(childModel.getChildNumber());

                holder.imageViewDeleteChild.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.textViewChildName.getContext());
                        builder.setTitle("Delete Child");
                        builder.setMessage("Delete.....?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference().child("Child")
                                        .child(getRef(position).getKey()).removeValue();

                                dialogInterface.dismiss();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });


                        builder.show();
                    }
                });

                holder.ChildInterfaceClick(new ChildOnClickShowFullPost() {
                    @Override
                    public void onClick(View view, boolean isLongPressed) {

                        Intent intent = new Intent(MyAllChild.this,ShowFullDeatliesChild.class);


                        intent.putExtra("ChildName",childModel.getChildName());
                        intent.putExtra("ChildEmail", childModel.getChildEmail());
                        intent.putExtra("ChildPassword", childModel.getChildPassword());
                        intent.putExtra("ChildConfirmPassword",childModel.getChildConfirmPassword());
                        intent.putExtra("ChildNumber",childModel.getChildNumber());
                        intent.putExtra("Role", childModel.getRole());
                        intent.putExtra("ParentID",childModel.getParentID());
                        intent.putExtra("ChildID",childModel.getChildID());

                        Double latitude = childModel.getChildLatitude();
                        Double longitude = childModel.getChildLongitude();


                        intent.putExtra("ChildLongitude",longitude);
                        intent.putExtra("ChildLatitude",latitude);

                        startActivity(intent);

                    }
                });

            }


            @Override
            public MyAllChildViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_post, viewGroup, false);
                return new MyAllChildViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        myRecyclerViewChild.setAdapter(adapter);
    }


    public static class MyAllChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewChildName, textViewChildPhoneNumber;

        ImageView imageViewDeleteChild;

        public ChildOnClickShowFullPost childOnClickShowFullPost;

        public MyAllChildViewHolder(View itemView) {
            super(itemView);



            textViewChildName = (TextView) itemView.findViewById(R.id.childNameShowPost);
            textViewChildPhoneNumber = (TextView) itemView.findViewById(R.id.childNumberShowPost);


            imageViewDeleteChild = (ImageView) itemView.findViewById(R.id.adminDeleteMyChildShowPost);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            childOnClickShowFullPost.onClick(view, false);

        }


        public void ChildInterfaceClick(ChildOnClickShowFullPost childOnClickShowFullPost)
        {
            this.childOnClickShowFullPost = childOnClickShowFullPost;
        }
    }
}