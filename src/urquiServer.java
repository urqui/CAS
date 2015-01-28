/**
 *
 * @author Kinesis Identity Security System Inc.
 */
package org.jasig.cas.adaptors.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class urquiServer {

    // HTTP POST request
    public static byte[] sendPost(byte[] ciphertext, byte[] iv, byte[] id, String url, int len) throws Exception {

        final String USER_AGENT = "Mozilla/5.0";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/text");
        con.setRequestProperty("Content-Length", Integer.toString(len));

        // combine byte arrays into 1.
        byte[] postData = new byte[ciphertext.length + iv.length + id.length];
        // copy a to result
        System.arraycopy(iv, 0, postData, 0, iv.length);
        // copy b to result
        System.arraycopy(id, 0, postData, iv.length, id.length);
        // copy b to result
        System.arraycopy(ciphertext, 0, postData, iv.length + id.length, ciphertext.length);

        // Send post request
        con.setDoOutput(true);
        OutputStream wr = null;
        try {
            wr = con.getOutputStream();
            wr.write(postData);

            wr.flush();
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException logOrIgnore) {
                }
            }
        }

        int responseCode = con.getResponseCode();
        ByteArrayOutputStream bais;

        DataInputStream in = new DataInputStream(con.getInputStream());
        byte[] f = new byte[128];
        bais = new ByteArrayOutputStream();
        int n;
        while ((n = in.read(f)) > 0) {
            bais.write(f, 0, n);
        }

        byte[] result = urquiCommon.hex2bin(bais.toString());

        return result;

    } // end function
}  // end class
