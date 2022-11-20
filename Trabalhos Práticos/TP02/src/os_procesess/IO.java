package os_procesess;
import java.io.*;
import java.nio.charset.*;

public class IO {
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in, Charset.forName("ISO-8859-1")));
    private static String charset = "ISO-8859-1";

    public static int readInt() {
        int x = -1;

        try {
            x = Integer.parseInt(readString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return x;
    }

    public static int readInt(String str) {
        try {
           PrintStream out = new PrintStream(System.out, true, charset);
           out.print(str);
        } catch(UnsupportedEncodingException uee) { 
            uee.printStackTrace();
        }

        return readInt();
    }

    public static String readString() {
        String s = "";
        char tmp;
        try {
            do {
                tmp = (char)in.read();

                if(tmp != '\n' && tmp != ' ' && tmp != 13) {
                    s += tmp;
                }

            } while(tmp != '\n' && tmp != ' ');
        } catch(IOException ioe) {
           ioe.printStackTrace();
        }

        return s;
     }

    public static String readString(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch(UnsupportedEncodingException uee) { 
            uee.printStackTrace();
        }
        return readString();
    }

    public static char readChar() {
        char resp = ' ';
        try {
            resp = (char)in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
    }

    public static char readChar(String str) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch(UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return readChar();
    }    

    public static double readDouble(){
        double d = -1;
        try { 
            d = Double.parseDouble(readString().trim().replace(",","."));
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return d;
    }
  
    public static double readDouble(String str){
        try {
            PrintStream out = new PrintStream(System.out, true, charset);
            out.print(str);
        } catch(UnsupportedEncodingException uee) { uee.printStackTrace(); }

        return readDouble();
    }

    public static float readFloat(){
        return (float) readDouble();
    }
  
    public static float readFloat(String str){
        return (float) readDouble(str);
    }

}
