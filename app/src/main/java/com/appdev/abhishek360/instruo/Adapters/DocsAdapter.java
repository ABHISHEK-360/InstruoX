package com.appdev.abhishek360.instruo.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appdev.abhishek360.instruo.R;

public class DocsAdapter {
    private String docsname;
    private String purpose;

    public String getDocsname() {
        return docsname;
    }

    public void setDocsname(String docsname) {
        this.docsname = docsname;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public static class  DocsViewHolder extends RecyclerView.ViewHolder {
        private TextView nameDoc, purposeDoc;
        private ImageButton downloadDoc;

        public TextView getNameDoc() {
            return nameDoc;
        }

        public TextView getPurposeDoc() {
            return purposeDoc;
        }

        public ImageButton getDownloadDoc() {
            return downloadDoc;
        }

        public DocsViewHolder(View itemView) {
            super(itemView);
            nameDoc = (TextView) itemView.findViewById(R.id.docs_view_holder_name);
            purposeDoc = (TextView) itemView.findViewById(R.id.docs_view_holder_purpose);
            downloadDoc = (ImageButton) itemView.findViewById(R.id.docs_view_holder_download);
        }
    }
}



