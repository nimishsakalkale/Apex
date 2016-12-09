
public class LS_Fu_stage_3 
{
	static long pc;
	static String opcode;
	static String destRegName;
	
	static String src1;	
	static String src2;		
	static String src3ForStore;	
	static int literalValue;	
	
	static int src1Value;
	static int src2Value;
	static int src3ForStoreValue;
	static  boolean stallFlag;
	
	static int resultOfLS_Fu_stage_3;
	
	static
	{
		stallFlag = true;
	}
	
	static void flush()
	{
		pc = 0;
		opcode = "NOP";
		destRegName = null;
		
		src1 = null;
		src2 = null;		
		src3ForStore = null;		
		literalValue = 0;	
		
		src1Value=0;
		src2Value=0;
		src3ForStoreValue=0;
		stallFlag=true;
		
		resultOfLS_Fu_stage_3=0;
		
	}
	
	public static void LoadDataInLS_Fu_stage_3()
	{
		if(LS_Fu_stage_2.isStallFlag()!=true)
		{
			flush();
			pc = LS_Fu_stage_2.pc;
			opcode = LS_Fu_stage_2.getOpcode();
			destRegName = LS_Fu_stage_2.getDestRegName();
		
			src1 = LS_Fu_stage_2.getSrc1();
			src2  = LS_Fu_stage_2.getSrc2();				
			literalValue = LS_Fu_stage_2.getLiteralValue();				
			src3ForStore=LS_Fu_stage_2.getSrc3ForStore();
			
			src1Value = LS_Fu_stage_2.getSrc1Value();
			src2Value = LS_Fu_stage_2.getSrc2Value();
			src3ForStoreValue=LS_Fu_stage_2.getSrc3ForStoreValue();
			
			stallFlag=false;
		}
		else
		{
			flush();
			stallFlag=true;
		}
	}

	
	public static void doLS_Fu_stage_3()
	{
		int resultOfExecute = 0;
		LoadDataInLS_Fu_stage_3();
		int readValue = 0;
	//	ROB.retireValidInstructionsFromHead();
		if(stallFlag == false)
		{
			if(opcode.equalsIgnoreCase("LOAD"))
			{
				
				 if(src2== null || src2 == "")
				 {
					 resultOfExecute = src1Value + literalValue;
				 }
				 else
				 {
					 resultOfExecute = src1Value + src2Value;
				 }
				 
				Integer readValueFromROB  = ROB.getAddressFromROB("" + resultOfExecute);
				if(readValueFromROB == null)
				{
					readValue = MemoryClass.GetValueFromMemory( (int)resultOfExecute);
				}
				else
				{
					readValue = readValueFromROB.intValue();
				}
				
			//	ROB.retireValidInstructionsFromHead();
				ROB.markROBEntryCompleted(pc, readValue);
				RegFileClass.setPhysicalRegisterValid(destRegName);
				
				//retire instructions.
		
				IQ.broadcast(destRegName, readValue);
				LSQ.broadcast(destRegName, readValue);

				
			}
			if(opcode.equalsIgnoreCase("STORE"))
			{
				
				 if(src3ForStore== null || src3ForStore == "")
				 {
					 resultOfExecute = src2Value + literalValue ;
				 }
				 else
				 {
					 resultOfExecute = src2Value + src3ForStoreValue ; 
				 }
				 
				ROB.markROBEntryCompletedForStore(pc, resultOfExecute, src1Value);
			//	ROB.retireValidInstructionsFromHead();
			}
						
		}
	}
	
	public static void Display() 
	{
		System.out.println();
		String st =  "LSFU3 [PC=" + pc + ",  opcode=" + opcode + ",  destRegName=" + destRegName + 
				",  src1=" + src1 + ",  src1Value="+ src1Value + ",  src2=" + src2 + ",  src2Value=" + src2Value + ", src3=" + src3ForStore + ",  src3Value=" + src3ForStoreValue +
				",  resultOfLS_Fu_Stage_3=" + resultOfLS_Fu_stage_3
				+ ",  literalValue=" + literalValue +"]";
		System.out.println(st);
	}

}
