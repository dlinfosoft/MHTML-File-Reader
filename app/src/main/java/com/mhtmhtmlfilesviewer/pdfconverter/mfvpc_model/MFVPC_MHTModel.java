package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Date;

public class MFVPC_MHTModel implements Comparable<MFVPC_MHTModel> {
    String pdfname;
    File pdffile;
    String size;

    Date datemodify;

    public Date getDatemodify() {
        return datemodify;
    }

    public void setDatemodify(Date datemodify) {
        this.datemodify = datemodify;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Bitmap bitmap;


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    private  boolean isSelected;

    public File getPdffile() {
        return pdffile;
    }

    public void setPdffile(File pdffile) {
        this.pdffile = pdffile;
    }





    public String getPdfname() {
        return pdfname;
    }

    public void setPdfname(String pdfname) {
        this.pdfname = pdfname;
    }


    @Override
    public int compareTo(MFVPC_MHTModel o) {
        if (getDatemodify() == null || o.getDatemodify() == null)
            return 0;
        return getDatemodify().compareTo(o.getDatemodify());
    }
}
