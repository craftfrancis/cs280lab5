//As mentioned in gitStati.java, all credit for this object's design goes to 
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
		//Escape sequences to color the terminal output.
		final String ANSI_RESET = "\u001B[0m";
		final String ANSI_RED = "\u001B[31m";
		final String ANSI_GREEN = "\u001B[32m";

		//Strings to identify in the git status messages.
		final String NOT_COMMITTED = "> Changes not staged for commit:";
		final String TO_COMMIT = "> Changes to be committed:";
		final String UNTRACKED = "> Untracked files:";

        try
        {
        	//To read the git status output.
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;

			//To hold the lines of output.
            ArrayList<String> linesToPrint = new ArrayList<String>();

			//Populate the ArrayList.
            while ( (line = br.readLine()) != null) {
            	linesToPrint.add(type + " " + line);
            	output.append(line+"\n");
			}

			for (int i = 0; i < linesToPrint.size(); i++) {
				if (linesToPrint.get(i).equals(NOT_COMMITTED)) {
					//The following print statements move past the lines that precede 
					//the actual changes yet to be committed.
					System.out.println(linesToPrint.get(i));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 1));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 2));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 3));
            		output.append(line+"\n");

					//The first line detailing an uncommitted change.
					//This is printed in red.
					System.out.println(ANSI_RED + linesToPrint.get(i + 4) + ANSI_RESET);
            		output.append(line+"\n");

            		i += 5; //Properly updating the line counter for our purpose.

					//Until a new section in the status is reached.
            		while(!linesToPrint.get(i).equals(TO_COMMIT) && !linesToPrint.get(i).equals(UNTRACKED) 
            				&& !linesToPrint.get(i).equals("> ")) {
            			//Continuing printing in red.
						System.out.println(ANSI_RED + linesToPrint.get(i) + ANSI_RESET);
            			output.append(line+"\n");

						//Update as needed.
            			i++;
					}

					//Backtrack one increment so that we do not skip a line of output.
					i--;

				} else if (linesToPrint.get(i).equals(TO_COMMIT)) {
					//The following print statements move past the lines that precede 
					//the actual changes to be committed.
					System.out.println(linesToPrint.get(i));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 1));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 2));
            		output.append(line+"\n");

					//The first line detailing a change to be committed.
					//This is printed in green.
					System.out.println(ANSI_GREEN + linesToPrint.get(i + 3) + ANSI_RESET);
            		output.append(line+"\n");

            		i += 4; //Properly updating the line counter for our purpose.

            		//Until a new section in the status is reached.
            		while(!linesToPrint.get(i).equals(NOT_COMMITTED) && !linesToPrint.get(i).equals(UNTRACKED) 
            				&& !linesToPrint.get(i).equals("> ")) {
            			//Continuing printing in green.
						System.out.println(ANSI_GREEN + linesToPrint.get(i) + ANSI_RESET);
            			output.append(line+"\n");

						//Update as needed.
            			i++;
					}

					//Backtrack one increment so that we do not skip a line of output.
					i--;

				} else if (linesToPrint.get(i).equals(UNTRACKED)) {
					//The following print statements move past the lines that precede 
					//the names of untracked files.
					System.out.println(linesToPrint.get(i));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 1));
            		output.append(line+"\n");

            		System.out.println(linesToPrint.get(i + 2));
            		output.append(line+"\n");

					//The first line listing an untracked file.
					//This is printed in red.
					System.out.println(ANSI_RED + linesToPrint.get(i + 3) + ANSI_RESET);
            		output.append(line+"\n");

            		i += 4; //Properly updating the line counter for our purpose.

            		//Until a new section in the status is reached.
            		while(!linesToPrint.get(i).equals(NOT_COMMITTED) && !linesToPrint.get(i).equals(TO_COMMIT) 
            				&& !linesToPrint.get(i).equals("> ")) {
            			//Continuing printing in red.
						System.out.println(ANSI_RED + linesToPrint.get(i) + ANSI_RESET);
            			output.append(line+"\n");

						//Update as needed.
            			i++;
					}

					//Backtrack one increment so that we do not skip a line of output.
					i--;
				} else {
					//If none of these headings have been reached, print the line normally.
					System.out.println(linesToPrint.get(i));
					output.append(line+"\n");
				}
			}

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
