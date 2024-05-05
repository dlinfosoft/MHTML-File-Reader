/**
 * 
 */
package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mhtmhtmlfilesviewer.pdfconverter.R;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Sharefilepath;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_MHTModel;

import java.util.ArrayList;


public  class MFVPC_MhtFilesAdapter extends RecyclerView.Adapter<MFVPC_MhtFilesAdapter.ViewHolder> {
    Context context;
    ArrayList<MFVPC_MHTModel> mhtModels=new ArrayList<>();
    MFVPC_Sharefilepath sharefilepath;





	@SuppressWarnings("deprecation")
	public MFVPC_MhtFilesAdapter(Context context, ArrayList<MFVPC_MHTModel> pdfModels, MFVPC_Sharefilepath sharefilepath) {
	    this.context=context;
	    this.mhtModels=pdfModels;
		this.sharefilepath= sharefilepath;


	}



	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mfvpc_mht_item_my, viewGroup, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
		holder.setIsRecyclable(true);
		final MFVPC_MHTModel pdfModel=mhtModels.get(pos);

		if(pdfModel!=null) {

		    holder.mhtName.setText(pdfModel.getPdfname());
		    holder.mhtsize.setText(pdfModel.getSize());
            holder.mhttime.setText(pdfModel.getDate());
            holder.mhtpath.setText(pdfModel.getPdffile().getAbsolutePath());

            holder.mht_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharefilepath.shareItem(pdfModel.getPdffile().getAbsolutePath(), pdfModel.getDate(), pdfModel.getSize(), pdfModel.getPdffile());

                }


            });


        }







	}
	public void filterList(ArrayList<MFVPC_MHTModel> filterdNames) {
		mhtModels=new ArrayList<>();
		Log.e("Master","addAll");
		mhtModels.addAll(filterdNames);
		notifyDataSetChanged();

	}



	@Override
	public int getItemCount() {
		return mhtModels.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView mhtName,mhtsize, mhttime,mhtpath;
		FrameLayout mht_ly;
		ImageView icon;
		LinearLayout bottom_ll;
		ImageView checkBox;

		LinearLayout shadow;
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			checkBox=(ImageView)itemView.findViewById(R.id.checkbox);
			mhtName=(TextView)itemView.findViewById(R.id.mhtname);
			bottom_ll=(LinearLayout) itemView.findViewById(R.id.bottom_ll);
			icon=(ImageView) itemView.findViewById(R.id.icon);
			mht_ly=(FrameLayout) itemView.findViewById(R.id.mhtll);
			mhtsize=(TextView)itemView.findViewById(R.id.mhtsize);
			mhttime=(TextView)itemView.findViewById(R.id.mhttime);
			mhtpath=(TextView)itemView.findViewById(R.id.mhtpath);



		}


	}

}