package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;




import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_Attachment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

public class MFVPC_MHTUnpack {
    private MFVPC_MHTUnpack() {
    }

    private static Collection<MFVPC_Attachment> extractMimePart(Object obj) throws IOException, MessagingException {
        if (obj instanceof Multipart) {
            return handleMultipart((Multipart) obj);
        }
        if (obj instanceof MimeBodyPart) {
            return handleMimeBodyPart((MimeBodyPart) obj);
        }
        return null;
    }

    private static Collection<MFVPC_Attachment> handleMimeBodyPart(MimeBodyPart mimeBodyPart) throws IOException, MessagingException {
        if (mimeBodyPart.getContent() instanceof Multipart) {
            return handleMultipart((Multipart) mimeBodyPart.getContent());
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new MFVPC_Attachment(mimeBodyPart));
        return arrayList;
    }

    private static Collection<MFVPC_Attachment> handleMultipart(Multipart multipart) throws MessagingException, IOException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < multipart.getCount(); i++) {
            Collection<MFVPC_Attachment> extractMimePart = extractMimePart(multipart.getBodyPart(i));
            if (extractMimePart != null) {
                arrayList.addAll(extractMimePart);
            }
        }
        return arrayList;
    }

    public static Collection<MFVPC_Attachment> unpack(File file) throws IOException, MessagingException {
        return unpack(new FileInputStream(file));
    }

    public static Collection<MFVPC_Attachment> unpack(InputStream inputStream) throws MessagingException, IOException {
        return extractMimePart(new MimeMessage(Session.getDefaultInstance(new Properties()), inputStream).getContent());
    }

    public static Collection<MFVPC_Attachment> unpack(byte[] bArr) throws MessagingException, IOException {
        return unpack(new ByteArrayInputStream(bArr));
    }
}
