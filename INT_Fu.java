
public class INT_Fu 
{
	static long pc;
	static String opcode;
	static String destRegName;
	
	static String src1;	
	static String src2;			
	static int literalValue;	
	static int resultOfINTFu;
	
	static int src1Value;
	static int src2Value;
	static boolean stallFlag;
	
	

	static void flush()
	{
		pc = 0;
		opcode = "NOP";
		destRegName = null;
		
		src1 = null;
		src2 = null;					
		literalValue = 0;	
		resultOfINTFu = 0;	
		
		src1Value=0;
		src2Value=0;
		stallFlag=false;
	
	}
	
	public static void LoadDataInINT_Fu()
	{		
		
		IQEntry entry=IQ.Dequeue("INT");
		if(entry!=null)
		{
			flush();
			pc = entry.pc;
			opcode = entry.getOpcode();
			destRegName = entry.getDestRegName();
		
			src1 = entry.getSrc1();
			src2  = entry.getSrc2();				
			literalValue = entry.getLiteralValue();				
	
			src1Value = entry.getSrc1Value(entry.src1);
			src2Value = entry.getSrc2Value(entry.src2);
			stallFlag=false;
		}
		else
		{
			flush();
			stallFlag=true;
		}
		
	}
	
	public static void doINT_FU()
	{
	
		LoadDataInINT_Fu();
		if(stallFlag==false)
		{
			if(opcode.equalsIgnoreCase("ADD"))
			{
				resultOfINTFu = src1Value + src2Value;
			}
			else if(opcode.equalsIgnoreCase("SUB"))
			{
				resultOfINTFu = src1Value - src2Value;
			}
			else if(opcode.equalsIgnoreCase("OR"))
			{
				resultOfINTFu = src1Value | src2Value;
			}
			else if(opcode.equalsIgnoreCase("AND"))
			{
				resultOfINTFu = src1Value & src2Value;
			}
			else if(opcode.equalsIgnoreCase("EX-OR"))
			{
				resultOfINTFu = src1Value ^ src2Value;
			}
			else if(opcode.equalsIgnoreCase("MOVC"))
			{
				resultOfINTFu = literalValue;
			}
			else if (opcode.equalsIgnoreCase("MOV"))
			{
				resultOfINTFu = src1Value;
			}
			else if (opcode.equalsIgnoreCase("JUMP") || opcode.equalsIgnoreCase("BAL"))
			{
				
				resultOfINTFu = src1Value + literalValue;
				
				
			}
			else if(opcode.equalsIgnoreCase("BZ") || opcode.equalsIgnoreCase("BNZ") )
			{			
				resultOfINTFu =(int) pc + literalValue;
			}
			
			ROB.markROBEntryCompleted(pc, resultOfINTFu);
			if(opcode.equalsIgnoreCase("BAL"))
			{
				ROB.markROBEntryCompleted(pc, (int)pc +1 );
			}
		 	
			if(Decode.R2RInstructions.contains(opcode))
			{
				if(resultOfINTFu == 0)
				{
					RegFileClass.setZeroFlag(true);
				}
				else 
				{
						RegFileClass.setZeroFlag(false);
				}
			}
			
			if(!Decode.CFInstructions.contains(opcode))
			{
				IQ.broadcast(destRegName, resultOfINTFu);
				LSQ.broadcast(destRegName, resultOfINTFu);
				RegFileClass.setPhysicalRegisterValid(destRegName);
			}
		}		
	}
	
	
	
	
	
	public static long getPc() {
		return pc;
	}

	public static void setPc(long pc) {
		INT_Fu.pc = pc;
	}

	public static String getOpcode() {
		return opcode;
	}

	public static void setOpcode(String opcode) {
		INT_Fu.opcode = opcode;
	}

	public static String getDestRegName() {
		return destRegName;
	}

	public static void setDestRegName(String destRegName) {
		INT_Fu.destRegName = destRegName;
	}

	public static String getSrc1() {
		return src1;
	}

	public static void setSrc1(String src1) {
		INT_Fu.src1 = src1;
	}

	public static String getSrc2() {
		return src2;
	}

	public static void setSrc2(String src2) {
		INT_Fu.src2 = src2;
	}

	public static int getLiteralValue() {
		return literalValue;
	}

	public static void setLiteralValue(int literalValue) {
		INT_Fu.literalValue = literalValue;
	}

	public static int getResultOfINTFu() {
		return resultOfINTFu;
	}

	public static void setResultOfINTFu(int resultOfINTFu) {
		INT_Fu.resultOfINTFu = resultOfINTFu;
	}

	public static int getSrc1Value() {
		return src1Value;
	}

	public static void setSrc1Value(int src1Value) {
		INT_Fu.src1Value = src1Value;
	}

	public static int getSrc2Value() {
		return src2Value;
	}

	public static void setSrc2Value(int src2Value) {
		INT_Fu.src2Value = src2Value;
	}

	public static boolean isStallFlag() {
		return stallFlag;
	}

	public static void setStallFlag(boolean stallFlag) {
		INT_Fu.stallFlag = stallFlag;
	}

	public static void Display() 
	{
		System.out.println();
		String st =  "Execute [PC=" + pc + ",  opcode=" + opcode + ",  destRegName=" + destRegName + 
				",  src1=" + src1 + ",  src1Value="+ src1Value + ",  src2=" + src2 + ",  src2Value=" + src2Value + 
				",  resultOfExecute=" + resultOfINTFu
				+ ",  literalValue=" + literalValue +"]";
		System.out.println(st);
	}

}
