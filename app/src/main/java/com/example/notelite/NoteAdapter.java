package com.example.notelite;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NoteAdapter extends FirebaseRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;


    public NoteAdapter(Context context, @NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int i, @NonNull Note note) {
        holder.tvTitle.setText(note.getTitle());
        holder.tvDate.setText(note.getDate());
        holder.tvContent.setText(note.getContent());

        String key = getRef(i).getKey();

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v1) {
                AlertDialog.Builder add = new AlertDialog.Builder(context);
                add.setTitle("Update Note");
                View v = LayoutInflater.from(context)
                        .inflate(R.layout.add_note_form, null, false);
                add.setView(v);

                TextInputEditText etTitle, etDate, etContent;
                etTitle = v.findViewById(R.id.etNoteTitle);
                etDate = v.findViewById(R.id.etNoteDate);
                etContent = v.findViewById(R.id.etNoteContent);


                etTitle.setText(note.getTitle());
                etDate.setText(note.getDate());
                etContent.setText(note.getContent());

                add.setPositiveButton("Update Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = etTitle.getText().toString().trim();
                        String date = etDate.getText().toString().trim();
                        String content = etContent.getText().toString().trim();

                        HashMap<String, Object> record = new HashMap<>();
                        record.put("title", title);
                        record.put("date", date);
                        record.put("content", content);

                        assert key != null;
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Notes")
                                .child(key)
                                .setValue(record)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Note updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


                add.setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Notes")
                                .child(key)
                                .removeValue()

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                add.show();
                return false;
            }
        });
    }




    @NonNull

    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_note_design, parent, false);
        return new NoteViewHolder(v);
    }




    public void updateOptions(FirebaseRecyclerOptions<Note> options) {
        updateOptions(options);
    }




    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvContent;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}

