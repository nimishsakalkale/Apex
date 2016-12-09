
public class LSQEntry 
{
	public long pc;	
	public int memoryAddress;
	public int memoryValue;
	public boolean lsqEntryStatus;
	
	public String opcode;
	public String destRegName;
	
	public String src1;	
	public String src2;		
	public String src3ForStore;	
	

	int src1Value;
	int src2Value;
	int src3ForStoreValue;	
	public int literalValue;	
	public boolean entryValid;

	public boolean isLSQEntryValid(LSQEntry ent)
	{
		entryValid=false;		
		//check the values in valid table
		if(ent.opcode.equals("LOAD"))
		{
			if(ent.src2 != null)			// to check if src2 is a physical register
			{
				if(RegFileClass.isPhysicalRegisterValid(ent.src1) && RegFileClass.isPhysicalRegisterValid(ent.src2) )				
				{
					entryValid=true;				
				}
				else
				{
					entryValid=false;
				}
			}
			else							// src2 is a LITERAL value
			{
				if(RegFileClass.isPhysicalRegisterValid(ent.src1))
				{
					entryValid=true;
				}
				else
				{
					entryValid=false;
				}
			}
		} else if(ent.opcode.equals("STORE")){

			if(ent.src3ForStore != null)				//src1,src2,src3 are registers
			{
				if(RegFileClass.isPhysicalRegisterValid(ent.src1) && RegFileClass.isPhysicalRegisterValid(ent.src2) && RegFileClass.isPhysicalRegisterValid(ent.src3ForStore) )				
				{
					entryValid=true;				
				}
				else
				{
					entryValid=false;
				}
			}
			else										//src3 is a literal
			{
				if(RegFileClass.isPhysicalRegisterValid(ent.src1) && RegFileClass.isPhysicalRegisterValid(ent.src2) )				
				{
					entryValid=true;				
				}
				else
				{
					entryValid=false;
				}
			}
		
		}
		return entryValid;
	}
	
	public void Display()
	{
		System.out.println(" pc="+pc+" opcode="+opcode+" destRegName= "+destRegName+
				" src1="+src1+" src2="+src2+" src3ForStore="+src3ForStore+
				" literal= "+literalValue);
	}
	
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	public String getDestRegName() {
		return destRegName;
	}
	public void setDestRegName(String destRegName) {
		this.destRegName = destRegName;
	}
	public String getSrc1() {
		return src1;
	}
	public void setSrc1(String src1) {
		this.src1 = src1;
	}
	public String getSrc2() {
		return src2;
	}
	public void setSrc2(String src2) {
		this.src2 = src2;
	}
	public String getSrc3ForStore() {
		return src3ForStore;
	}
	public void setSrc3ForStore(String src3ForStore) {
		this.src3ForStore = src3ForStore;
	}
	public int getSrc1Value() {
		return src1Value;
	}
	public void setSrc1Value(int src1Value) {
		this.src1Value = src1Value;
	}
	public int getSrc2Value() {
		return src2Value;
	}
	public void setSrc2Value(int src2Value) {
		this.src2Value = src2Value;
	}
	public int getSrc3ForStoreValue() {
		return src3ForStoreValue;
	}
	public void setSrc3ForStoreValue(int src3ForStoreValue) {
		this.src3ForStoreValue = src3ForStoreValue;
	}
	public int getLiteralValue() {
		return literalValue;
	}
	public void setLiteralValue(int literalValue) {
		this.literalValue = literalValue;
	}
	public boolean isEntryValid() {
		return entryValid;
	}
	public void setEntryValid(boolean entryValid) {
		this.entryValid = entryValid;
	}
	public long getPc() {
		return pc;
	}
	public void setPc(long pc) {
		this.pc = pc;
	}
	public long getMemoryAddress() {
		return memoryAddress;
	}
	public void setMemoryAddress(int memoryAddress) {
		this.memoryAddress = memoryAddress;
	}
	public int getMemoryValue() {
		return memoryValue;
	}
	public void setMemoryValue(int memoryValue) {
		this.memoryValue = memoryValue;
	}
	public boolean isLsqEntryStatus() {
		return lsqEntryStatus;
	}
	public void setLsqEntryStatus(boolean lsqEntryStatus) {
		this.lsqEntryStatus = lsqEntryStatus;
	}
	
	

}
