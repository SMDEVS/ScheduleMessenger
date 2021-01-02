package com.example.schedulemessenger.View;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.ViewModel.MessageViewModel;
import com.example.schedulemessenger.databinding.FragmentHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding fragmentHistoryBinding;
    private MessageViewModel messageViewModel;
    private RecyclerView mRecyclerView;

    private MessageAdapter messageAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater, container, false);

        mRecyclerView = fragmentHistoryBinding.recyclerView;

        return fragmentHistoryBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        messageViewModel = new ViewModelProvider(getActivity()).get(MessageViewModel.class);
        messageViewModel.getAllMessages().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                Toast.makeText(getActivity(), "Hello from onChanged method!", Toast.LENGTH_SHORT).show();
                MessageAdapter adapter = new MessageAdapter((ArrayList<Message>) messages);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(adapter);
            }
        });
    }

}