
public class LS_Fu_stage_1 
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
	
	public static void LoadDataInLS_Fu_stage_1()
	{
		LSQEntry entry=LSQ.LSQDequeue();
		if(entry!=null)
		{
			flush();
			pc = entry.pc;
			opcode = entry.getOpcode();
			destRegName = entry.getDestRegName();
		
			src1 = entry.getSrc1();
			src2  = entry.getSrc2();				
			literalValue = entry.getLiteralValue();				
			src3ForStore=entry.getSrc3ForStore();
			
			src1Value = entry.getSrc1Value();
			src2Value = entry.getSrc2Value();
			src3ForStoreValue=entry.getSrc3ForStoreValue();
			stallFlag=false;
		}
		else
		{
			flush();
			stallFlag=true;
		}
	}
	
	public static void doLS_Fu_stage_1()
	{
		LoadDataInLS_Fu_stage_1();
	
	}

	public static long getPc() {
		return pc;
	}

	public static void setPc(long pc) {
		LS_Fu_stage_1.pc = pc;
	}

	public static String getOpcode() {
		return opcode;
	}

	public static void setOpcode(String opcode) {
		LS_Fu_stage_1.opcode = opcode;
	}

	public static String getDestRegName() {
		return destRegName;
	}

	public static void setDestRegName(String destRegName) {
		LS_Fu_stage_1.destRegName = destRegName;
	}

	public static String getSrc1() {
		return src1;
	}

	public static void setSrc1(String src1) {
		LS_Fu_stage_1.src1 = src1;
	}

	public static String getSrc2() {
		return src2;
	}

	public static void setSrc2(String src2) {
		LS_Fu_stage_1.src2 = src2;
	}

	public static String getSrc3ForStore() {
		return src3ForStore;
	}

	public static void setSrc3ForStore(String src3ForStore) {
		LS_Fu_stage_1.src3ForStore = src3ForStore;
	}

	public static int getLiteralValue() {
		return literalValue;
	}

	public static void setLiteralValue(int literalValue) {
		LS_Fu_stage_1.literalValue = literalValue;
	}

	public static int getSrc1Value() {
		return src1Value;
	}

	public static void setSrc1Value(int src1Value) {
		LS_Fu_stage_1.src1Value = src1Value;
	}

	public static int getSrc2Value() {
		return src2Value;
	}

	public static void setSrc2Value(int src2Value) {
		LS_Fu_stage_1.src2Value = src2Value;
	}

	public static int getSrc3ForStoreValue() {
		return src3ForStoreValue;
	}

	public static void setSrc3ForStoreValue(int src3ForStoreValue) {
		LS_Fu_stage_1.src3ForStoreValue = src3ForStoreValue;
	}

	public static boolean isStallFlag() {
		return stallFlag;
	}

	public static void setStallFlag(boolean stallFlag) {
		LS_Fu_stage_1.stallFlag = stallFlag;
	}

	public static void Display() 
	{
		System.out.println();
		String st =  "LSFU1 [PC=" + pc + ",  opcode=" + opcode + ",  destRegName=" + destRegName + 
				",  src1=" + src1 + ",  src1Value="+ src1Value + ",  src2=" + src2 + ",  src2Value=" + src2Value + ", src3=" + src3ForStore + ",  src3Value=" + src3ForStoreValue +
				",  literalValue=" + literalValue +"]";
		System.out.println(st);
	}

}
