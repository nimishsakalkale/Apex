import java.io.*;

/*
	This class fetches contets of instruction indicated by program counter from file.
	Method names are self-explainatory.
*/
public class Fetch {

	String currentIstruction;
	static long PC;
	static String fileName;

	//blank lines in input file are considered as NOPs.	
	public void doFetch( long PC) throws IOException
	{
		try
		{
		
			Fetch.PC = PC;
			FileInputStream fstream = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String str = null;
			long count = PC - 20000;
			for(long counter = 0; counter <= count; counter++ )
			{
				str = br.readLine();
			}
			if(str == null || str.equals(""))
			{
				str = "NOP";
			}
			currentIstruction = str;
		}
		catch(Exception e)
		{
			System.out.println("input file not found. Please check the supplied command line parameter.\nAnd run program again.");
			System.exit(0);
		}
	}

	// retuns current instruction
	public String getCurrentIstruction() {
		
		return currentIstruction;
	}

	public void flush()
	{
		PC = 0;
		currentIstruction = "NOP";
	}

	public void Display() 
	{
		System.out.println();
		String st =  "Fetch [PC=" + PC + ",currentIstruction=" + currentIstruction + "]";
		System.out.println(st);
	}

	
}
