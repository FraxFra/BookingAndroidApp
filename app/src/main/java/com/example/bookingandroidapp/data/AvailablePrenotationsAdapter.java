package com.example.bookingandroidapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingandroidapp.Connections.PrenotationBookerTask;
import com.example.bookingandroidapp.Connections.PrenotationsLoaderTask;
import com.example.bookingandroidapp.R;
import com.example.bookingandroidapp.activities.HomeActivity;

import java.util.List;

public class AvailablePrenotationsAdapter extends RecyclerView.Adapter<AvailablePrenotationsAdapter.PrenotazioneViewHolder> {

    private final List<Slot> mPrenotazioni;
    private final Context mContext;
    private final RecyclerView mrecyclerView;
    private final ProgressBar mprogressBar;
    private final TextView memptyView;

    public AvailablePrenotationsAdapter(List<Slot> prenotazioni, Context context, RecyclerView recyclerView, ProgressBar progressBar, TextView emptyView) {
        mPrenotazioni = prenotazioni;
        mContext = context;
        mrecyclerView = recyclerView;
        mprogressBar = progressBar;
        memptyView = emptyView;
    }

    @NonNull
    @Override
    public PrenotazioneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_available_prenotations, parent, false);
        return new PrenotazioneViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PrenotazioneViewHolder holder, int position) {
        Slot prenotazione = mPrenotazioni.get(position);
        holder.mTeacherTextView.setText("Prof. " + prenotazione.TeacherName);
        holder.mSubjectTextView.setText(String.valueOf(prenotazione.SubjectName));
        holder.mDataTextView.setText(String.valueOf(prenotazione.WeekDate));
        holder.mSTimeTextView.setText(String.valueOf(prenotazione.StartTime));
        holder.mETimeTextView.setText(String.valueOf(prenotazione.EndTime));
        holder.prenotatiButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Conferma prenotazione");
            builder.setMessage("Vuoi davvero prenotarti per questa ripetizione?");
            builder.setPositiveButton("Sì", (dialog, which) -> {
                PrenotationBookerTask p = new PrenotationBookerTask(prenotazione.SlotId, prenotazione.SubjectName, prenotazione.TeacherId, mContext);
                p.execute();
                PrenotationsLoaderTask pr = new PrenotationsLoaderTask(mContext, mrecyclerView, mprogressBar, memptyView);
                pr.execute();
            });
            builder.setNegativeButton("No", null);
            builder.show();
        });
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
        public Button prenotatiButton;

        public PrenotazioneViewHolder(@NonNull View itemView) {
            super(itemView);

            mTeacherTextView = itemView.findViewById(R.id.teacher_text_view);
            mSubjectTextView = itemView.findViewById(R.id.subject_text_view);
            mDataTextView = itemView.findViewById(R.id.data_text_view);
            mSTimeTextView = itemView.findViewById(R.id.stime_text_view);
            mETimeTextView = itemView.findViewById(R.id.etime_text_view);
            prenotatiButton = itemView.findViewById(R.id.prenotati_button);
        }
    }
}





