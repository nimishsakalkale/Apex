
public class IQEntry 
{
		
	public long pc;
	public String opcode;
	public String destRegName;
	
	public String src1;	
	public String src2;		
	public String src3ForStore;	
	

	 int src1Value;
	 int src2Value;
	 int src3ForStoreValue;
	
	public int literalValue;	
	public String fuType;					// will be set as per opcode	
	public boolean entryValid;				// IQEntry is valid or not. Only setter is generated. Getter is custom.
	
	public long getPc() {
		return pc;
	}
	public void setPc(long pc) {
		this.pc = pc;
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
	public int getLiteralValue() {
		return literalValue;
	}
	public void setLiteralValue(int literalValue) {
		this.literalValue = literalValue;
	}
	public String getFuType() {
		return fuType;
	}
	public void setFuType(String fuType) {
		this.fuType = fuType;
	}	
	
	public void setEntryValid(boolean entryValid) {
		this.entryValid = entryValid;
	}	
	
	public int getSrc1Value(String src1){
		return src1Value;
	}
	public int getSrc2Value(String src2){
		return src2Value;
	}
	public int getSrc3ForStoreValue(String src3ForStore){
		return src3ForStoreValue;
	}
	
	public void setSrc1Value(int value) {
		src1Value = value;
	}
	public void setSrc2Value(int value) {
		src2Value = value;
	}
	public void setSrc3ForStoreValue(int value) {
		src3ForStoreValue = value;
	}
	
	
	//this method will be called by dequeue method of IQ class.
	//this method will return true if a valid IQEntry exist in IQ.
	public boolean isIQEntryValid(IQEntry ent)
	{
		entryValid=false;		
		//check the values in valid table
		
		if(ent.opcode.equals("MOVC"))
		{
			entryValid=true;
		}
		
		else if(ent.opcode.equals("MOV"))
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
		
		else if(ent.opcode.equals("LOAD"))
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
		}
		else if (ent.opcode.equals("STORE"))
		{
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
		else if(ent.opcode.equals("ADD")||ent.opcode.equals("SUB")||ent.opcode.equals("MUL")
				||ent.opcode.equals("AND")||ent.opcode.equals("OR")||ent.opcode.equals("EX-OR"))
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
		else if(ent.opcode.equals("JUMP") || ent.opcode.equals("BAL"))
		{
			entryValid=true;
		}
		else if(ent.opcode.equals("BZ") || ent.opcode.equals("BNZ"))
		{
			if(RegFileClass.isPhysicalRegisterValid(ent.src2) )				
			{
				entryValid=true;				
			}
			else
			{
				entryValid=false;
			}
		}
		else if(ent.opcode.equals("HALT"))
		{
			entryValid=true;
		}

		return entryValid;
	}
	
	
	
	
	public void Display(){
		System.out.println(" pc="+pc+" opcode="+opcode+" destRegName= "+destRegName+
				" src1="+src1+" src2="+src2+" src3ForStore="+src3ForStore+
				" literal= "+literalValue+" fuType="+fuType);
	}
		

}
