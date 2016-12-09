
public class LS_Fu_stage_2 
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
	
	static{
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
	}
	
	public static void LoadDataInLS_Fu_stage_2()
	{
		//IQEntry entry=IQ.Dequeue("MEMORY");
		if(LS_Fu_stage_1.isStallFlag()!=true)
		{
			flush();
			pc = LS_Fu_stage_1.pc;
			opcode = LS_Fu_stage_1.getOpcode();
			destRegName = LS_Fu_stage_1.getDestRegName();
		
			src1 = LS_Fu_stage_1.getSrc1();
			src2  = LS_Fu_stage_1.getSrc2();				
			literalValue = LS_Fu_stage_1.getLiteralValue();				
			src3ForStore=LS_Fu_stage_1.getSrc3ForStore();
			
			src1Value = LS_Fu_stage_1.getSrc1Value();
			src2Value = LS_Fu_stage_1.getSrc2Value();
			src3ForStoreValue=LS_Fu_stage_1.getSrc3ForStoreValue();
			stallFlag=false;
		}
		else
		{
			flush();
			stallFlag=true;
		}
	}
	
	public static void doLS_Fu_stage_2()
	{
		//just aacts as cycle consumer in simiulator.
		
		if(INT_Fu.opcode.equalsIgnoreCase("JUMP"))
		{
			ROB.flushROBEntriesFromBranch((int)INT_Fu.pc);
			IQ.flushIQEntriesAfterBranchTaken((int) INT_Fu.pc);
			Simulator.flushDecode();
			Simulator.flushFetch();
			Simulator.PC = INT_Fu.resultOfINTFu;
		
		}
		
		if(INT_Fu.opcode.equalsIgnoreCase("BAL"))
		{
			ROB.flushROBEntriesFromBranch((int)INT_Fu.pc);
			IQ.flushIQEntriesAfterBranchTaken((int) INT_Fu.pc);
			Simulator.flushDecode();
			Simulator.flushFetch();
			Simulator.PC = INT_Fu.resultOfINTFu;
			//RegFileClass.setArchRegisterValue("X", (int)(INT_Fu.pc + 1));
		}
		
		if(INT_Fu.opcode.equalsIgnoreCase("BZ"))
		{
			if(INT_Fu.getSrc2Value() == 0)
			{
				ROB.flushROBEntriesFromBranch((int)INT_Fu.pc);
				IQ.flushIQEntriesAfterBranchTaken((int) INT_Fu.pc);
				Simulator.flushDecode();
				Simulator.flushFetch();
				Simulator.PC = INT_Fu.resultOfINTFu;
			}
		
		}
		
		if(INT_Fu.opcode.equalsIgnoreCase("BNZ"))
		{
			if(INT_Fu.getSrc2Value() != 0)
			{
				ROB.flushROBEntriesFromBranch((int)INT_Fu.pc);
				IQ.flushIQEntriesAfterBranchTaken((int) INT_Fu.pc);
				Simulator.flushDecode();
				Simulator.flushFetch();
				Simulator.PC = INT_Fu.resultOfINTFu;
			}
		
		}
		LoadDataInLS_Fu_stage_2();
		return;
	}

	
	public static long getPc() {
		return pc;
	}

	public static void setPc(long pc) {
		LS_Fu_stage_2.pc = pc;
	}

	public static String getOpcode() {
		return opcode;
	}

	public static void setOpcode(String opcode) {
		LS_Fu_stage_2.opcode = opcode;
	}

	public static String getDestRegName() {
		return destRegName;
	}

	public static void setDestRegName(String destRegName) {
		LS_Fu_stage_2.destRegName = destRegName;
	}

	public static String getSrc1() {
		return src1;
	}

	public static void setSrc1(String src1) {
		LS_Fu_stage_2.src1 = src1;
	}

	public static String getSrc2() {
		return src2;
	}

	public static void setSrc2(String src2) {
		LS_Fu_stage_2.src2 = src2;
	}

	public static String getSrc3ForStore() {
		return src3ForStore;
	}

	public static void setSrc3ForStore(String src3ForStore) {
		LS_Fu_stage_2.src3ForStore = src3ForStore;
	}

	public static int getLiteralValue() {
		return literalValue;
	}

	public static void setLiteralValue(int literalValue) {
		LS_Fu_stage_2.literalValue = literalValue;
	}

	public static int getSrc1Value() {
		return src1Value;
	}

	public static void setSrc1Value(int src1Value) {
		LS_Fu_stage_2.src1Value = src1Value;
	}

	public static int getSrc2Value() {
		return src2Value;
	}

	public static void setSrc2Value(int src2Value) {
		LS_Fu_stage_2.src2Value = src2Value;
	}

	public static int getSrc3ForStoreValue() {
		return src3ForStoreValue;
	}

	public static void setSrc3ForStoreValue(int src3ForStoreValue) {
		LS_Fu_stage_2.src3ForStoreValue = src3ForStoreValue;
	}

	public static boolean isStallFlag() {
		return stallFlag;
	}

	public static void setStallFlag(boolean stallFlag) {
		LS_Fu_stage_2.stallFlag = stallFlag;
	}

	public static void Display() 
	{
		System.out.println();
		String st =  "LSFU2 [PC=" + pc + ",  opcode=" + opcode + ",  destRegName=" + destRegName + 
				",  src1=" + src1 + ",  src1Value="+ src1Value + ",  src2=" + src2 + 
				",  src2Value=" + src2Value + "src3=" + src3ForStore + ", src3=" + src3ForStore + ",  src3Value=" + src3ForStoreValue +
				",  literalValue=" + literalValue +"]";
		System.out.println(st);
	}

}
