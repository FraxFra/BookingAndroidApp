package com.example.bookingandroidapp.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingandroidapp.R;

import java.util.List;

public class AvailablePrenotationsAdapter extends RecyclerView.Adapter<AvailablePrenotationsAdapter.PrenotazioneViewHolder> {

    private final List<Slot> mPrenotazioni;

    public AvailablePrenotationsAdapter(List<Slot> prenotazioni) {
        mPrenotazioni = prenotazioni;
    }

    @NonNull
    @Override
    public PrenotazioneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_available_prenotations, parent, false);
        return new PrenotazioneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrenotazioneViewHolder holder, int position) {
        Slot prenotazione = mPrenotazioni.get(position);
        holder.mTeacherTextView.setText(String.valueOf(prenotazione.TeacherId));
        holder.mSubjectTextView.setText(String.valueOf(prenotazione.SubjectName));
        holder.mDataTextView.setText(String.valueOf(prenotazione.WeekDate));
        holder.mSTimeTextView.setText(String.valueOf(prenotazione.StartTime));
        holder.mETimeTextView.setText(String.valueOf(prenotazione.EndTime));
    }

    @Override
    public int getItemCount() {
        return mPrenotazioni.size();
    }

    public static class PrenotazioneViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTeacherTextView;
        private final TextView mSubjectTextView;
        private final TextView mDataTextView;
        private final TextView mSTimeTextView;
        private final TextView mETimeTextView;

        public PrenotazioneViewHolder(@NonNull View itemView) {
            super(itemView);

            mTeacherTextView = itemView.findViewById(R.id.teacher_text_view);
            mSubjectTextView = itemView.findViewById(R.id.subject_text_view);
            mDataTextView = itemView.findViewById(R.id.data_text_view);
            mSTimeTextView = itemView.findViewById(R.id.stime_text_view);
            mETimeTextView = itemView.findViewById(R.id.etime_text_view);
        }
    }
}





