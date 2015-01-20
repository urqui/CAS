package org.jasig.cas.adaptors.jdbc;

/**
 *
 * @author Kinesis Identity Security System Inc.
 */
public class urquiCheck {
    
    String msg;
    
    public urquiCheck() {
        
    }
   
    public  boolean validate(String myid, String mykey, String myrqui, String myurqui, String myurl) throws Exception  {
           
        // combine data to be sent into 1 variable.
        StringBuilder plaintext = new StringBuilder();
        plaintext.append(myid);
        plaintext.append(myrqui);
        plaintext.append(myurqui);

        String txData = plaintext.toString();  // convert back to string.
        
        Encryptor myenc = new Encryptor(mykey);  //  initialize encryption with key.

        byte ciphertext[] = myenc.encrypt(txData);  // encrypt text for transmission

        byte result[] = urquiServer.sendPost(ciphertext, myenc.iv, myid.getBytes(), myurl, 56); // send data

        String myresult = myenc.decrypt(result);   // decrypt response.

        if (txData.equals(myresult.substring(0, 22))) {  // incoming should be same as outgoing. 
            byte rtnBytes[] = myresult.getBytes();   // translate into bytes for next test.

            if (rtnBytes[22] == 0x01) {   // test indicator byte if valid return.
                msg = "Valid URQUi number";
                return true;
            } else { 
                msg = "Invalid URQUi number";
                return false;
            }
        } else {
            msg = "Data interferred with duing transmission";
            return false;
        }
            
    }
}
