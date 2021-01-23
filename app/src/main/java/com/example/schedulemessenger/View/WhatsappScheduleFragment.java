package com.example.schedulemessenger.View;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.MyBroadcastReceiver;
import com.example.schedulemessenger.ViewModel.MessageViewModel;
import com.example.schedulemessenger.databinding.FragmentWhatsappScheduleBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.schedulemessenger.R;

public class WhatsappScheduleFragment extends Fragment {

    private ArrayList<Message> mMessages = new ArrayList<>();

    //To allow us to take input in necessary format for a date
    String scheduledDate, ScheduledTime;
    // Will be generated due to ViewBinding
    private FragmentWhatsappScheduleBinding whatsappScheduleBinding;

    private long scheduledTimeInterval;
    private long finalSendingTime;
    private Message message1;

    private MessageViewModel messageViewModel;

    public WhatsappScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment, using ViewBinding
        whatsappScheduleBinding = FragmentWhatsappScheduleBinding.inflate(inflater, container, false);
        return whatsappScheduleBinding.getRoot();// Inflate the layout for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        whatsappScheduleBinding.dateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                dateInputHandler();
            }
        });

        whatsappScheduleBinding.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeInputHandler();
            }
        });

        whatsappScheduleBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Message set",
                        Toast.LENGTH_SHORT).show();

                String phoneNumber = whatsappScheduleBinding.phoneNumberEditText.getText().toString();
                String messageText = whatsappScheduleBinding.messageEditText.getText().toString();

                scheduledTimeInterval = calculateTimeInterval();
                finalSendingTime = scheduledTimeInterval + System.currentTimeMillis();

                message1 = new Message();
                message1.setPhoneNumber(phoneNumber); //PHONE
                message1.setMessageType(2); //TYPE
                message1.setMessageStatus("Pending"); //STATUS
                message1.setMessageText(messageText); //TEXT
                message1.setImageUri(""); //IMAGE
                message1.setInstaUsername(""); //INSTA_USERNAME
                message1.setTimeInterval(scheduledTimeInterval); //TIME_INTERVAL
                message1.setTimeString(scheduledDate + " " + ScheduledTime); //TIME_STRING
                messageViewModel.insertMessage(message1);

                scheduleWhatsappJobService();

            }
        });

    }

    private void scheduleWhatsappJobService() {
        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
        intent.putExtra("ID", message1.getMessageId());
        intent.putExtra("PHONE", message1.getPhoneNumber());
        intent.putExtra("TEXT", message1.getMessageText());
        intent.putExtra("TYPE", message1.getMessageType());
        intent.putExtra("TIME_STRING", message1.getTimeString());

        /**
         intent.putExtra("STATUS", message1.getMessageStatus());
         intent.putExtra("IMAGE", message1.getImageUri());
         intent.putExtra("INSTA_USERNAME", message1.getInstaUsername());
         intent.putExtra("TIME_INTERVAL", message1.getTimeInterval());
         */

        String currentString = message1.getPhoneNumber() + message1.getMessageText() + message1.getTimeString();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), currentString.hashCode(),
                intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, finalSendingTime, pendingIntent);

    }

    private long calculateTimeInterval() {

        String format = "MM/dd/yyyy hh:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date currentDateObject = null;
        Date scheduledDateObject = null;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR, hour);
        calendar1.set(Calendar.MINUTE, minute);
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DATE, day);
        CharSequence charSequence = DateFormat.format("MM/dd/yyyy hh:mm:ss a", calendar1);

        try {
            currentDateObject = sdf.parse(charSequence.toString());
            scheduledDateObject = sdf.parse(scheduledDate + " " + ScheduledTime);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        // To obtain difference between scheduled time and current time, in milliseconds
        Log.d("HHHH", String.valueOf(scheduledDateObject.getTime() - currentDateObject.getTime()));
        return scheduledDateObject.getTime() - currentDateObject.getTime();
    }

    //To allow us to take input in necessary format for time
    private void timeInputHandler() {

        //To get current time in hours and minutes
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR);
        int currentMinutes = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(getContext(), "Time set", Toast.LENGTH_LONG).show();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);
                calendar1.set(Calendar.SECOND, 00);
                CharSequence charSequence = DateFormat.format("hh:mm:ss a", calendar1);
                ScheduledTime = charSequence.toString();

            }
        }, currentHour, currentMinutes, true);

        timePickerDialog.show();
    }

    private void dateInputHandler() {

        //To get current date
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Toast.makeText(getContext(), "Date set", Toast.LENGTH_LONG).show();

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, dayOfMonth);
                        CharSequence charSequence = DateFormat.format("MM/dd/yyyy", calendar1);
                        scheduledDate = charSequence.toString();

                    }
                }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();
    }

    //To avoid memory leaks
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        whatsappScheduleBinding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        messageViewModel = new ViewModelProvider(getActivity()).get(MessageViewModel.class);
        messageViewModel.getAllMessages().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                mMessages = (ArrayList<Message>) messages;
            }
        });
    }

}