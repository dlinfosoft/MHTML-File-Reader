package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model;



import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Utils;

import org.apache.http.HttpHost;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public class MFVPC_Attachment {
    private static final String CONTENT_LOCATION = "Content-Location";
    private MimeBodyPart mimeBodyPart;

    public MFVPC_Attachment(MimeBodyPart mimeBodyPart) {
        this.mimeBodyPart = mimeBodyPart;
    }

    public String getFileName() throws MessagingException {
        String fileName = this.mimeBodyPart.getFileName();
        if (fileName == null) {
            String str = "Content-Location";
            String[] header = this.mimeBodyPart.getHeader(str);
            if (header == null || header.length <= 0) {
                String contentID = this.mimeBodyPart.getContentID();
                if (contentID != null) {
                    fileName = contentID;
                }
            } else {
                fileName = this.mimeBodyPart.getHeader(str)[0];
                if (fileName != null && fileName.contains(HttpHost.DEFAULT_SCHEME_NAME) && !fileName.contains("htm") && this.mimeBodyPart.isMimeType("text/html")) {
                    return "index.htm";
                }
                try {
                    str = new URL(fileName).getFile();
                    fileName = str.substring(str.lastIndexOf(47) + 1);
                } catch (MalformedURLException unused) {
                    if (!MFVPC_Utils.isNullOrEmpty(fileName).booleanValue()) {
                        try {
                            fileName = fileName.substring(fileName.lastIndexOf(47) + 1);
                            return fileName;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return fileName;
                        }
                    }
                    return fileName;
                }
            }
        }
        return fileName;
    }

    public MimeBodyPart getMimeBodyPart() {
        return this.mimeBodyPart;
    }

    public String getMimeType() throws MessagingException {
        String contentType = this.mimeBodyPart.getContentType();
        int indexOf = contentType.indexOf(59);
        return indexOf != -1 ? contentType.substring(0, indexOf) : contentType;
    }

    public void saveFile(File file) throws IOException, MessagingException {
        this.mimeBodyPart.saveFile(file);
    }

    public void saveFile(String str) throws IOException, MessagingException {
        this.mimeBodyPart.saveFile(str);
    }
}
