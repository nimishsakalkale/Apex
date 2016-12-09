
public class MUL_Fu
{
	static long pc;
	static String opcode;
	static String destRegName;
	
	static String src1;	
	static String src2;		
	static int literalValue;	
	static int resultOfMULFu;
		
	static int src1Value;
	static int src2Value;
	static boolean stallFlag;
	
	static int cycleConsumer;
	
	static
	{
		cycleConsumer = 1;
	}

	static void flush()
	{
		pc = 0;
		opcode = "NOP";
		destRegName = null;
		
		src1 = null;
		src2 = null;					
		literalValue = 0;	
		resultOfMULFu = 0;
		
		src1Value=0;
		src2Value=0;
		stallFlag=false;
	}
	
	public static void LoadDataInMUL_Fu()
	{
		IQEntry entry=IQ.Dequeue("MUL");
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
	
	public  static void doMUL_Fu()
	{
	//	ROB.retireValidInstructionsFromHead();
		if(cycleConsumer == 1)
		{
			LoadDataInMUL_Fu();
		}
		if(stallFlag == false)
		{
			//increment cycle consumer.
			incrementCycleConsumer();
			
			//only implement this logic when 4 cycles has been consumed i.e. cycle consumer again resets to 1
			if(cycleConsumer == 1)
			{
				resultOfMULFu = src1Value * src2Value;
				
				ROB.markROBEntryCompleted(pc, resultOfMULFu);
				RegFileClass.setPhysicalRegisterValid(destRegName);
				
				IQ.broadcast(destRegName, resultOfMULFu);
				LSQ.broadcast(destRegName, resultOfMULFu);

				flush();	
			}
			
		}
	}
	
	
	public static void incrementCycleConsumer()
	{
		cycleConsumer ++ ;
		if(cycleConsumer == 5)
		{
			cycleConsumer = 1;
		}
	}
	
	
	public static long getPc() {
		return pc;
	}

	public static void setPc(long pc) {
		MUL_Fu.pc = pc;
	}

	public static String getOpcode() {
		return opcode;
	}

	public static void setOpcode(String opcode) {
		MUL_Fu.opcode = opcode;
	}

	public static String getDestRegName() {
		return destRegName;
	}

	public static void setDestRegName(String destRegName) {
		MUL_Fu.destRegName = destRegName;
	}

	public static String getSrc1() {
		return src1;
	}

	public static void setSrc1(String src1) {
		MUL_Fu.src1 = src1;
	}

	public static String getSrc2() {
		return src2;
	}

	public static void setSrc2(String src2) {
		MUL_Fu.src2 = src2;
	}

	public static int getLiteralValue() {
		return literalValue;
	}

	public static void setLiteralValue(int literalValue) {
		MUL_Fu.literalValue = literalValue;
	}

	public static int getResultOfMULFu() {
		return resultOfMULFu;
	}

	public static void setResultOfMULFu(int resultOfMULFu) {
		MUL_Fu.resultOfMULFu = resultOfMULFu;
	}

	public static int getSrc1Value() {
		return src1Value;
	}

	public static void setSrc1Value(int src1Value) {
		MUL_Fu.src1Value = src1Value;
	}

	public static int getSrc2Value() {
		return src2Value;
	}

	public static void setSrc2Value(int src2Value) {
		MUL_Fu.src2Value = src2Value;
	}

	public static boolean isStallFlag() {
		return stallFlag;
	}

	public static void setStallFlag(boolean stallFlag) {
		MUL_Fu.stallFlag = stallFlag;
	}
	
	

	public static void Display() 
	{
		System.out.println();
		String st =  "MUL FU [PC=" + pc + ",  opcode=" + opcode + ",  destRegName=" + destRegName + 
				",  src1=" + src1 + ",  src1Value="+ src1Value + ",  src2=" + src2 + ",  src2Value=" + src2Value + 
				",  resultOfMUL_Fu=" + resultOfMULFu
				+ ",  literalValue=" + literalValue +"]";
		System.out.println(st);
	}

}
