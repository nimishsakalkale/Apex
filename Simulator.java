import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class Simulator {

	static long PC;
	static Fetch fObj;
	static Decode decodObj;
	//static Execute exObj;
	//static Mem memObj;
	//static WB WBObj;
	static int clock;
	
	//these flags are used to record status when firsr few instructions are being processed
	static boolean fetchProcesed;
	static boolean decodeProcesssed;
	//static boolean exProcesssed;
	//static boolean memProcessed;
	//static boolean WBProcessed;
	static boolean INT_FuProcessed;
	static boolean MUL_FuProcessed;
	static boolean LS_FU_S1_Processed;
	static boolean LS_FU_S2_Processed;
	static boolean LS_FU_S3_Processed;
	
	static 
	{
		// Initiallize objects
		initiallize();
	}
	
	public static void main(String args[]) throws IOException
	{
		
		initiallize();
//		if command line parameter for input file is specifiedd it will take from command line
//		otherwise it will set it as input.txt
//		if file not found it will rais exception in Fetch saying 'File not found.'
		
		if(args.length > 0)
		{
			Fetch.fileName = args[0];
		}
		else
		{
			Fetch.fileName = "H:/Study/Study/Workspace/Simulator/src/input.txt";
		}
		//int enteredChoice = 0;
		
		Scanner in= new Scanner(System.in);
		System.out.println();
		System.out.println(" available commands : ");
		System.out.println(" Initialize  ");
		System.out.println(" Simulate <n>: ");
		System.out.println(" Display : ");
		System.out.println();
		//Process commands till halt is detected.	
		while(true)
		{
			String command = in.nextLine();
			if(command.equalsIgnoreCase("Initialize"))
			{
				initiallize();
				System.out.println("Pipeline Initiallized");
				System.out.println();

			}
			else if(command.equalsIgnoreCase("Display"))
			{
				Display();
			}
			else if(command.indexOf(' ') != -1)
			{
				int index = command.indexOf(' ') ;
				String tempCommand = command.substring(0, index);
				if(tempCommand.equalsIgnoreCase("Simulate"))
				{
					String innerString  = command.substring(index + 1);
					if(Utility.isInteger(innerString))
					{
						int cycles = Integer.parseInt(innerString);
						runCycles(cycles);
						System.out.println();
						System.out.println("next " + cycles + " cycle simulated.");
						System.out.println("number of clocks porcessed :  " + clock );
						System.out.println();
					}
				}
			}
			else 
			{
				System.out.println("Command match not found.");
			}
		}
		
	}

	
	public static void runCycles(int cycles) throws IOException
	{
		
		for(int count=0; count < cycles; clock ++, count++)
		{
			ROB.retireValidInstructionsFromHead();
			if(LS_FU_S2_Processed)
			{
				LS_Fu_stage_3.doLS_Fu_stage_3();
			}
			if(LS_FU_S1_Processed)
			{
				LS_Fu_stage_2.doLS_Fu_stage_2();
				LS_FU_S2_Processed = true;
			}
			if(decodeProcesssed)
			{
				INT_Fu.doINT_FU();
				MUL_Fu.doMUL_Fu();
				LS_Fu_stage_1.doLS_Fu_stage_1();
				LS_FU_S1_Processed = true;
				
			}
			
			
			if(fetchProcesed)
			{
				decodObj.doDecode(fObj);
				decodeProcesssed = true;
			}
			if(! decodObj.getStallFlag())
			{				
				fObj.doFetch(PC);
				fetchProcesed = true;
				PC = PC +1;
			}
			
			/*
			if(WBObj.getOpcode() != null)
			{
				if(WBObj.getOpcode().equals("HALT"))
				{
					System.out.println();				
					System.out.println("Halt detected at WB stage at clock : " + ( Simulator.clock ));
					System.out.println("Contents of pipeline  stages before stoppng execution : ");
					Simulator.Display();
					System.out.println();
					
					System.exit(0);
				}
			}
			*/
		}
	}
	
	public  static void initiallize()
	{
		 PC = 20000;
	     	 clock = 0;
		 fObj = new Fetch();
		 decodObj = new Decode();
		 
		 //initiallize processed variables
		 fetchProcesed = false;
		 decodeProcesssed = false;
		 
		INT_FuProcessed=false;
		MUL_FuProcessed=false;
		LS_FU_S1_Processed=false;
		LS_FU_S2_Processed=false;
		LS_FU_S3_Processed=false;
		 
		 //set program counters appropriately
		 Fetch.PC = 20000;
		 Decode.PC = 0;
		 RegFileClass.Initialize();
		 
	}
	
	public static void Display()
	{
		System.out.println();
		System.out.println("clock = " + clock );
		fObj.Display();
		decodObj.Disaply();
		IQ.Display();
		LSQ.display();
		INT_Fu.Display();
		MUL_Fu.Display();
		LS_Fu_stage_1.Display();
		LS_Fu_stage_2.Display();
		LS_Fu_stage_3.Display();
		RegFileClass.displayFreeList();
		RegFileClass.displayAllocatedList();
		
		//ROB
		
		RegFileClass.Display();
		RegFileClass.displayValidBitsOfPhysicalReissters();		
		ROB.display();
		RegFileClass.displayAliasTableEntries();
		//MemoryClass.Display();
		MemoryClass.Display();
		System.out.println();	
	}
	
	public static void flushFetch()
	{
		fObj.flush();
	}
	
	public static void flushDecode()
	{
		decodObj.flush();
	}
	
	public static void resetValidFlagOfDecode()
	{
		decodObj.resetValidFlag();
	}
}
