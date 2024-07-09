package handler;

import java.io.*;

public class Handler {

    /**
     * Writes a string to an output stream
     * @param str : String to write
     * @param os : Output Stream to write to
     * @throws IOException : if there is an error writing to the output stream
     */
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    /**
     * Reads a string from an input stream
     * @param is : Input Stream to read from
     * @return : String read from input stream
     * @throws IOException : if there is an error reading from the input stream
     */
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
