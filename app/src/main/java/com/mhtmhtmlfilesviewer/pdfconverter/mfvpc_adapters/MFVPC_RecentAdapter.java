/**
 *
 */
package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_adapters;

import android.content.Context;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mhtmhtmlfilesviewer.pdfconverter.R;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Sharefilepath;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UtilsFiles;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_RecentFileModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MFVPC_RecentAdapter extends RecyclerView.Adapter<MFVPC_RecentAdapter.ViewHolder> {
    Context context;
    ArrayList<MFVPC_RecentFileModel> recentList;
    MFVPC_Sharefilepath sharefilepath;

    @SuppressWarnings("deprecation")
    public MFVPC_RecentAdapter(Context context, ArrayList<MFVPC_RecentFileModel> arrayList, MFVPC_Sharefilepath sharefilepath) {
        this.context = context;
        this.recentList = arrayList;
        this.sharefilepath = sharefilepath;
        Log.i("MFVPC_RecentAdapter", " arrayList " + arrayList.size());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mfvpc_recent_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        final MFVPC_RecentFileModel fileModel = recentList.get(i);

        if (fileModel != null) {

            Log.i("MFVPC_RecentAdapter", "MFVPC_RecentFileModel" + fileModel.dirPath);




            holder.recentname.setText(fileModel.mhtFileName);
            holder.recentsize.setText(sizeoffile(new File(fileModel.mhtFilePath)));
            holder.recenttime.setText(lastmodyfy(new File(fileModel.mhtFilePath)));

            holder.mainll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        sharefilepath.RecentFilemodel(recentList.get(i));
                        String name = MFVPC_UtilsFiles.getFileNameWithoutExtention(new File(fileModel.mhtFilePath));
                        sharefilepath.shareItem(name, null, null, new File(fileModel.mhtFilePath));
                    }catch ( Exception e){
                        sharefilepath.shareItem(null, null, null, new File(fileModel.mhtFilePath));
                    }


                }
            });


            holder.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharefilepath.shareItem(new File(fileModel.dirPath));

                }
            });
        }


    }

    public String lastmodyfy(File file){
        String formattedDateString = null;
        try {
            Date lastModDate = new Date(file.lastModified());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
             formattedDateString = formatter.format(lastModDate);
        }catch ( Exception e){

        }

        return formattedDateString;
    }
    public String sizeoffile(File file){
        Long uncompressedFileLength;
        String uncompressedFileSize = null;
        try {
            uncompressedFileLength = Long.valueOf(file.length());
            uncompressedFileSize = Formatter.formatShortFileSize(context, uncompressedFileLength.longValue());

        }catch ( Exception e){

        }
        return uncompressedFileSize;
    }


    @Override
    public int getItemCount() {
        return recentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recentname, recentsize,recenttime;
        LinearLayout close;
        LinearLayout mainll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recentname = (TextView) itemView.findViewById(R.id.recent_name);
            recentsize = (TextView) itemView.findViewById(R.id.recent_size);
            recenttime = (TextView) itemView.findViewById(R.id.recent_time);
            close = (LinearLayout) itemView.findViewById(R.id.recent_delete);
            mainll = (LinearLayout) itemView.findViewById(R.id.recent_ll);
        }


    }

}