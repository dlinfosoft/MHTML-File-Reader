package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;


import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_RecentFileModel;

import java.io.File;

public interface MFVPC_Sharefilepath {
    void shareItem(File path);
    void RecentFilemodel(MFVPC_RecentFileModel path);
    void shareItem(String name, String date, String size, File path);

}
