package com.etesa.app.viewreport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etesa.app.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private List<ViewReport> viewReportList = new ArrayList<>();

    public void setData(List<ViewReport> viewReportList) {
        this.viewReportList = viewReportList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_student_report_recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewReport viewReport = viewReportList.get(position);

        holder.studentRollNo.setText(viewReport.getRollNumber());
        holder.studentAttendanceMarkStatus.setText(viewReport.getIsPresent() ? "Present" : "Absent");
    }

    @Override
    public int getItemCount() {
        return viewReportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView studentRollNo;
        private MaterialTextView studentAttendanceMarkStatus;
        public ViewHolder(View itemView) {
            super(itemView);

            studentRollNo = itemView.findViewById(R.id.studentRollNo);
            studentAttendanceMarkStatus = itemView.findViewById(R.id.studentAttendanceMarkStatus);
        }
    }
}
