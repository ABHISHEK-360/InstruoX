package com.appdev.abhishek360.instruox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DocsAdapter
{

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

    static class  DocsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name_docs,purpose_docs;
        ImageButton download_docs;


        public DocsViewHolder(View itemView)
        {
            super(itemView);
            name_docs=(TextView) itemView.findViewById(R.id.docs_view_holder_name);
            purpose_docs=(TextView) itemView.findViewById(R.id.docs_view_holder_purpose);


            //download_docs=(Button) itemView.findViewById(R.id.events_card_view);


            download_docs=(ImageButton) itemView.findViewById(R.id.docs_view_holder_download);





        }
    }
}



