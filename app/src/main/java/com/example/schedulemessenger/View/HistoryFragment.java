package com.example.schedulemessenger.View;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.MyBroadcastReceiver;
import com.example.schedulemessenger.R;
import com.example.schedulemessenger.View.Adapter.MessageAdapter;
import com.example.schedulemessenger.ViewModel.MessageViewModel;
import com.example.schedulemessenger.databinding.FragmentHistoryBinding;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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
                messageAdapter = new MessageAdapter((ArrayList<Message>) messages);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(messageAdapter);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Message message = messageAdapter.getMessageAt(viewHolder.getAdapterPosition());
                int messageType = message.getMessageType();

                if (direction == ItemTouchHelper.RIGHT) {

                    if (messageType == 1) {

                        // To cancel a scheduled SMS
                        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
                        intent.putExtra("ID", message.getMessageId());
                        intent.putExtra("PHONE", message.getPhoneNumber());
                        intent.putExtra("TEXT", message.getMessageText());
                        intent.putExtra("TYPE", message.getMessageType());

                        String currentString = message.getPhoneNumber() + message.getMessageText() + message.getTimeString();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), currentString.hashCode(),
                                intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                    } else if (messageType == 2) {

                        // To cancel a WhatsApp message
                        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
                        intent.putExtra("ID", message.getMessageId());
                        intent.putExtra("PHONE", message.getPhoneNumber());
                        intent.putExtra("TEXT", message.getMessageText());
                        intent.putExtra("TYPE", message.getMessageType());
                        intent.putExtra("TIME_STRING", message.getTimeString());

                        String currentString = message.getPhoneNumber() + message.getMessageText() + message.getTimeString();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), currentString.hashCode(),
                                intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                    } else if (messageType == 3) {

                        // To cancel an email
                        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
                        intent.putExtra("ID", message.getMessageId());
                        intent.putExtra("PHONE", message.getPhoneNumber());
                        intent.putExtra("TEXT", message.getMessageText());
                        intent.putExtra("TYPE", message.getMessageType());
                        intent.putExtra("TIME_STRING", message.getTimeString());
                        intent.putExtra("SUBJECT", message.getInstaUsername());

                        String currentString = message.getPhoneNumber() + message.getMessageText() + message.getTimeString();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), currentString.hashCode(),
                                intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);

                    }

                }

                messageViewModel.deleteMessage(message);
                messageAdapter.notifyDataSetChanged();

                if (direction == ItemTouchHelper.RIGHT)
                    Toast.makeText(getActivity(), "Scheduled message cancelled", Toast.LENGTH_LONG).show();
                else if (direction == ItemTouchHelper.LEFT)
                    Toast.makeText(getActivity(), "Message note deleted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull
                    RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                // Swipe decor, which also informs user about result of their swiping actions
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                        .addSwipeLeftLabel("DELETE NOTE")
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_note)
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.deleteNoteColor))
                        .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), R.color.design_default_color_background))
                        .addSwipeRightLabel("CANCEL MESSAGE")
                        .addSwipeRightActionIcon(R.drawable.ic_cancel_alarm)
                        .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.cancelMessageColor))
                        .setSwipeRightLabelColor(ContextCompat.getColor(getContext(), R.color.design_default_color_background))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        }).attachToRecyclerView(mRecyclerView);

    }

}
