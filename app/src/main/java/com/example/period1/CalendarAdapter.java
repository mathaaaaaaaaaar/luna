package com.example.period1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.period1.Event;
import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private ArrayList<LocalDate> selectedPeriodDates = new ArrayList<>();

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.1);
        else // week view
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if (date == null) {
            holder.dayOfMonth.setText("");
            holder.parentView.setBackgroundColor(Color.WHITE); // Set background to transparent for empty cells
        } else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
                // Highlight selected date
            } else {
                holder.parentView.setBackgroundColor(Color.WHITE); // Reset background color
            }
            // Check if this date has events logged
            if (hasEventsForDate(date)) {
                // Change cell color for dates with events (e.g., change the color to pink)
                holder.parentView.setBackgroundColor(Color.parseColor("#FFC5CB")); // Pink color
                //
            }

            // Set click listener for the cell
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onItemClick(position, date);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public boolean hasEventsForDate(LocalDate date) {
        ArrayList<Event> events = Event.eventsForDate(date);
        return !events.isEmpty();
    }

    public void highlightPeriod(ArrayList<LocalDate> selectedPeriodDates) {
        for (LocalDate date : selectedPeriodDates) {
            int position = days.indexOf(date);
            if (position != -1) {
                // Highlight the cell for the selected date
                notifyItemChanged(position);
            }
        }
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
