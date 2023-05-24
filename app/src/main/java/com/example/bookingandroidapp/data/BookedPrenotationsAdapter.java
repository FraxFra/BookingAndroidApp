package com.example.bookingandroidapp.data;

import static com.example.bookingandroidapp.R.drawable.baseline_check_circle_outline_24;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingandroidapp.Connections.PrenotationBookingsLoaderTask;
import com.example.bookingandroidapp.Connections.PrenotationConfermationTask;
import com.example.bookingandroidapp.Connections.PrenotationDeleteTask;
import com.example.bookingandroidapp.R;

import java.util.List;

public class BookedPrenotationsAdapter extends RecyclerView.Adapter<BookedPrenotationsAdapter.BookingViewHolder> {

    private final List<Booking> mPrenotazioni;
    private final Context mContext;
    private final RecyclerView mrecyclerView;
    private final ProgressBar mprogressBar;
    private final TextView memptyView;

    public BookedPrenotationsAdapter(List<Booking> prenotazioni, Context context, RecyclerView recyclerView, ProgressBar progressBar, TextView emptyView) {
        mPrenotazioni = prenotazioni;
        mContext = context;
        mrecyclerView = recyclerView;
        mprogressBar = progressBar;
        memptyView = emptyView;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booked_prenotations, parent, false);
        return new BookingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking prenotazione = mPrenotazioni.get(position);
        holder.mTeacherTextView.setText("Prof. " + prenotazione.Teacher);
        holder.mSubjectTextView.setText(String.valueOf(prenotazione.Subject));
        holder.mDataTextView.setText(String.valueOf(prenotazione.SlotDate));
        holder.mSTimeTextView.setText(String.valueOf(prenotazione.SlotStart));
        holder.mETimeTextView.setText(String.valueOf(prenotazione.SlotEnd));
        if("Attiva".equals(prenotazione.BookingStatus))
        {
            holder.confermaButton.setVisibility(View.VISIBLE);
            holder.cancellaButton.setVisibility(View.VISIBLE);
            holder.statusImageView.setVisibility(View.GONE);
            holder.confermaButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Conferma prenotazione");
                builder.setMessage("Vuoi davvero confermare questa ripetizione?");
                builder.setPositiveButton("Sì", (dialog, which) -> {
                    PrenotationConfermationTask p = new PrenotationConfermationTask(mContext, String.valueOf(prenotazione.BookingId));
                    p.execute();
                    PrenotationBookingsLoaderTask pb = new PrenotationBookingsLoaderTask(mContext, mrecyclerView, mprogressBar, memptyView);
                    pb.execute();
                });
                builder.setNegativeButton("No", null);
                builder.show();
            });
            holder.cancellaButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Cancella prenotazione");
                builder.setMessage("Vuoi davvero disdire questa ripetizione?");
                builder.setPositiveButton("Sì", (dialog, which) -> {
                    PrenotationDeleteTask p = new PrenotationDeleteTask(mContext, String.valueOf(prenotazione.BookingId));
                    p.execute();
                    PrenotationBookingsLoaderTask pb = new PrenotationBookingsLoaderTask(mContext, mrecyclerView, mprogressBar, memptyView);
                    pb.execute();
                });
                builder.setNegativeButton("No", null);
                builder.show();
            });
        }
        else
        {
            holder.confermaButton.setVisibility(View.GONE);
            holder.cancellaButton.setVisibility(View.GONE);
            holder.statusImageView.setVisibility(View.VISIBLE);
            if("Effettuata".equals(prenotazione.BookingStatus))
            {
                holder.statusImageView.setText("PRENOTATO");
            }
            else if ("Disdetta".equals(prenotazione.BookingStatus))
            {
                holder.statusImageView.setText("DISDETTA");
            }
            else
            {
                holder.statusImageView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mPrenotazioni.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTeacherTextView;
        private final TextView mSubjectTextView;
        private final TextView mDataTextView;
        private final TextView mSTimeTextView;
        private final TextView mETimeTextView;
        public ImageButton confermaButton;
        public ImageButton cancellaButton;
        public TextView statusImageView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            mTeacherTextView = itemView.findViewById(R.id.teacher_text_viewB);
            mSubjectTextView = itemView.findViewById(R.id.subject_text_viewB);
            mDataTextView = itemView.findViewById(R.id.data_text_viewB);
            mSTimeTextView = itemView.findViewById(R.id.stime_text_viewB);
            mETimeTextView = itemView.findViewById(R.id.etime_text_viewB);
            confermaButton = itemView.findViewById(R.id.conferma_button);
            cancellaButton = itemView.findViewById(R.id.rimuovi_button);
            statusImageView = itemView.findViewById(R.id.booking_status);
        }
    }
}


