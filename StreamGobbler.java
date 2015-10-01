//As mentioned in TreeHouse.java, all credit for this object's design goes to 
//the individual who posted the response it came from on this thread of Stack Overflow: 
//
//http://stackoverflow.com/questions/236737/making-a-system-call-that-returns-the-stdout-output-as-a-string/236873#236873
//

import java.util.*;
import java.io.*;

class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    StringBuffer output = new StringBuffer();

    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println(type + " " + line);
                output.append(line+"\n");
            } catch (IOException ioe)
              {
                ioe.printStackTrace();  
              }
    }
    public String getOutput()
    {
        return this.output.toString();
    }
}
