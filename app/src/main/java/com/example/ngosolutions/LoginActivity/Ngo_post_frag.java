package com.example.ngosolutions.LoginActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ngosolutions.R;

import com.example.ngosolutions.adapter.AdapterNgoPost;
import com.example.ngosolutions.adapter.AdapterPost;
import com.example.ngosolutions.models.ModalNgoPost;
import com.example.ngosolutions.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass
 */
public class Ngo_post_frag extends Fragment {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModalNgoPost> postList;
    AdapterNgoPost adapterPost;
    EditText editText;

    public Ngo_post_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ngo_post_frag, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        editText = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.postRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        postList = new ArrayList<>();
        loadPosts();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchUser(editable.toString());
            }
        });
        return view;
    }
    private void searchUser(final String query) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("post_app");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                postList.clear();
                for (DataSnapshot ds: datasnapshot.getChildren()){
                    ModalNgoPost modelPost = ds.getValue(ModalNgoPost.class);

                    if(modelPost.getpTitle().toLowerCase().contains(query.toLowerCase()) ||
                            modelPost.getpDesec().toLowerCase().contains(query.toLowerCase())){
                        postList.add(modelPost);
                    }
                    adapterPost =new AdapterNgoPost(getActivity(),postList);
                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("post_app");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ModalNgoPost modelPost = ds.getValue(ModalNgoPost.class);
                    postList.add(modelPost);
                    adapterPost =new AdapterNgoPost(getActivity(),postList);
                    recyclerView.setAdapter(adapterPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {
                Toast.makeText(getActivity(), ""+databaseerror.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}