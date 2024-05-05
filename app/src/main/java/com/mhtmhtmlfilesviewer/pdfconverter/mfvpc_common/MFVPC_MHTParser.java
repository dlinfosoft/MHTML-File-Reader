package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import androidx.core.os.EnvironmentCompat;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Decoder.BASE64Decoder;

public class MFVPC_MHTParser implements MFVPC_Constants {
    private File mhtFile;
    private File outputFolder;

    public MFVPC_MHTParser(File file, File file2) {
        this.mhtFile = file;
        this.outputFolder = file2;
    }

    private byte[] getBase64EncodedString(StringBuilder stringBuilder) throws IOException {
        return new BASE64Decoder().decodeBuffer(stringBuilder.toString());
    }

    private String getBoundary(BufferedReader bufferedReader) throws IOException {
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine == null) {
                return null;
            }
            readLine = readLine.trim();
        } while (!readLine.startsWith(MFVPC_Constants.BOUNDARY));
        String str = "\"";
        return readLine.substring(readLine.indexOf(str) + 1, readLine.lastIndexOf(str));
    }

    private String getCharSet(String str) {
        str = str.split("=")[1].trim();
        return str.substring(1, str.length() - 1);
    }

    private String getEncoding(String str) {
        return splitUsingColonSpace(str);
    }

    private String getFileName(String str, String str2) {
        Pattern compile = Pattern.compile("(\\w|_|-)+\\.\\w+");
        String str3 = "/";
        str2 = str2.toLowerCase().endsWith("jpeg") ? "jpg" : str2.split(str3)[1];
        if (str.endsWith(str3)) {
            str = "main";
        } else {
            Matcher matcher = compile.matcher(str.substring(str.lastIndexOf(str3) + 1));
            String str4 = "";
            while (matcher.find()) {
                str4 = matcher.group();
            }
            if (str4.trim().length() == 0) {
                str = EnvironmentCompat.MEDIA_UNKNOWN;
            } else {
                str2 = ".";
                return getUniqueName(str4.substring(0, str4.indexOf(str2)), str4.substring(str4.indexOf(str2) + 1, str4.length()));
            }
        }
        return getUniqueName(str, str2);
    }

    private byte[] getQuotedPrintableString(StringBuilder stringBuilder) {
        String str = "";
        return stringBuilder.toString().replaceAll(MFVPC_Constants.UTF8_BOM, str).replaceAll("=\n", str).getBytes();
    }

    private String getType(String str) {
        return splitUsingColonSpace(str);
    }

    private String getUniqueName(String str, String str2) {
        File file = this.outputFolder;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        String str3 = ".";
        stringBuilder.append(str3);
        stringBuilder.append(str2);
        File file2 = new File(file, stringBuilder.toString());
        if (!file2.exists()) {
            return file2.getAbsolutePath();
        }
        int i = 1;
        while (true) {
            File file3 = this.outputFolder;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(i);
            stringBuilder2.append(str3);
            stringBuilder2.append(str2);
            file = new File(file3, stringBuilder2.toString());
            if (!file.exists()) {
                return file.getAbsolutePath();
            }
            i++;
        }
    }

    private String splitUsingColonSpace(String str) {
        return str.split(":\\s*")[1].replaceAll(";", "");
    }


    private void writeBufferContentToFile(StringBuilder sb, String str, String str2, String str3) {
        byte[] bArr = new byte[0];
        if (!this.outputFolder.exists()) {
            this.outputFolder.mkdirs();
        }
        boolean z = true;
        z = true;
        if (str.equalsIgnoreCase("base64")) {
            try {
                bArr = getBase64EncodedString(sb);
            } catch (IOException e) {
                e.printStackTrace();
            }
            z = false;
        } else {
            bArr = str.equalsIgnoreCase("quoted-printable") ? getQuotedPrintableString(sb) : sb.toString().getBytes();
        }
        BufferedWriter bufferedWriter = null;
        BufferedOutputStream bufferedOutputStream = null;
        if (!z) {
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(str2));
                try {
                    bufferedOutputStream2.write(bArr);
                    bufferedOutputStream2.flush();
                    bufferedOutputStream2.close();
                } catch (Throwable th) {
                    th = th;
                    bufferedOutputStream = bufferedOutputStream2;
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {

                if (bufferedOutputStream != null) {
                }
                try {
                    throw th2;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } else {
            try {
                BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str2), str3));
                try {
                    bufferedWriter2.write(new String(bArr));
                    bufferedWriter2.flush();
                    bufferedWriter2.close();
                } catch (Throwable th3) {

                    bufferedWriter = bufferedWriter2;
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    throw th3;
                }
            } catch (Throwable th4) {
                if (bufferedWriter != null) {
                }
                try {
                    throw th4;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* JADX WARNING: Removed duplicated region for block: B:49:0x00c0  */
    public void decompress() {
        BufferedReader bufferedReader;
        Throwable th;
        StringBuilder sb = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(this.mhtFile));
            try {
                String boundary = getBoundary(bufferedReader);
                if (boundary != null) {
                    String str = "";
                    String str2 = str;
                    String str3 = "utf-8";
                    String str4 = str2;
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            bufferedReader.close();
                            return;
                        } else if (!MFVPC_Utils.isNullOrEmpty(readLine).booleanValue()) {
                            String trim = readLine.trim();
                            if (!MFVPC_Utils.isNullOrEmpty(trim).booleanValue()) {
                                if (trim.contains(boundary)) {
                                    if (sb != null) {
                                        writeBufferContentToFile(sb, str, str4, str3);
                                    }
                                    sb = new StringBuilder();
                                } else if (trim.startsWith("Content-Type")) {
                                    str2 = getType(trim);
                                } else if (trim.startsWith(MFVPC_Constants.CHAR_SET)) {
                                    str3 = getCharSet(trim);
                                } else if (trim.startsWith("Content-Transfer-Encoding")) {
                                    str = getEncoding(trim);
                                } else if (trim.startsWith("Content-Location")) {
                                    str4 = getFileName(trim.substring(trim.indexOf(":") + 1).trim(), str2);
                                } else if (sb != null) {
                                    sb.append(readLine + "\n");
                                }
                            }
                        }
                    }
                } else {
                    throw new Exception("Failed to find document 'boundary'... Aborting");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader != null) {
                }
                throw th;
            }
        } catch (Throwable th3) {
            bufferedReader = null;

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                throw th3;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


}
