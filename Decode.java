import java.util.ArrayList;

/*
	It decodes the contents of lastly fetched instruction into opcode, destination registers and source registers. Also this stae does the register renaming.

*/

//class to perform decode operation 
public class Decode {
	
	static ArrayList<String> R2RInstructions ; 
	static  ArrayList<String>  MEMInstructions; 
	static ArrayList<String>  CFInstructions;
	
	static long PC;
	String opcode;
	String destRegName;
	String src1;
	String src1PhysRegName;
	String src2PhysRegName;
	String src3PhysRegName;
	int src1Value;
	String src2;
	int src2Value;
	String src3ForStore;
	int src3ForStoreValue;
	int literalValue;
	boolean stallFlag;
	

	static
	{
		//categoroze instructions as per their types

		R2RInstructions = new ArrayList<String>();
		MEMInstructions = new ArrayList<String>();
		CFInstructions = new ArrayList<String>();
		R2RInstructions.add("ADD");
	//	R2RInstructions.add("ADDC");
		R2RInstructions.add("SUB");
		R2RInstructions.add("AND");
		R2RInstructions.add("MUL");
		R2RInstructions.add("OR");
		R2RInstructions.add("MOVC");
		R2RInstructions.add("MOV");
		R2RInstructions.add("EX-OR");
		
		//Mem Instructions
		MEMInstructions.add("LOAD");
		MEMInstructions.add("STORE");
		
		//Branch Instructions
		CFInstructions.add("BZ");
		CFInstructions.add("BNZ");
		CFInstructions.add("JUMP");
		CFInstructions.add("BAL");
		CFInstructions.add("HALT");
	}

	public void LoadDataInDecode(Fetch fetchObj)
	{
		flush();
		Decode.PC = Fetch.PC;
	}

	public void doDecode(Fetch fetchObj)
	{
		

		String tmpSubString =null;
		String instruction = fetchObj.getCurrentIstruction();
		addUnusedRegistersToFreeList();
		LoadDataInDecode(fetchObj);
		//null instruction is assumed to be NOP
		if(instruction == null)
		{			
			stallFlag = true;
			return;
		}
		stallFlag = false;
		
		
		String str = instruction.trim();
		int opcodeIndex = str.indexOf(' ');
		
		if(opcodeIndex == -1)
		{
			if(str.equals("NOP"))
			{
				opcode = "NOP";
				return;
			}
			opcode = str;
		}
		else
		{
			//Set opcode value
			opcode = str.substring(0, opcodeIndex);
			tmpSubString = instruction.substring(opcodeIndex,instruction.length()).trim();
				
		}
	
		String destPhyReg="";
	
		//not for STORE
		if (!IQ.isFull() && !ROB.isROBListFull() && !LSQ.isFull()  && !RegFileClass.isFreeListEmpty()) 
		{

			// Add entry to ROB
			ROBEntry robEntry= new ROBEntry();
			
		//Decode logic for regiter to register instructions
		if(R2RInstructions.contains(opcode))
		{
			// Add entry to IQ
			IQEntry iqEntry = new IQEntry();
			String tmpReg;
			int destIndex = tmpSubString.indexOf(' ');
			tmpReg = tmpSubString.substring(0, destIndex).trim();
			
			destRegName = tmpReg;
		
			tmpSubString = tmpSubString.substring(destIndex + 1,tmpSubString.length()).trim();
			
			
			if(opcode.equalsIgnoreCase("MOVC"))
			{
				literalValue = Integer.parseInt(tmpSubString);
				 iqEntry.setLiteralValue(literalValue);
			}
			else if (opcode.equalsIgnoreCase("MOV"))
			{
				src1 = tmpSubString.trim();
				src1Value = getSourceRegValue(src1);
				
				iqEntry.setSrc1(RegFileClass.getAllocatedPhysicalRegister(src1));
				iqEntry.setSrc1Value(src1Value);
			}
			else
			{	
				int src1Index = tmpSubString.indexOf(' ');
				tmpReg = tmpSubString.substring(0, src1Index);
				src1 = tmpReg;
				src1Value = getSourceRegValue(src1);
				src1PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src1);
				iqEntry.setSrc1(src1PhysRegName);
				iqEntry.setSrc1Value(src1Value);
			
				tmpSubString = tmpSubString.substring(src1Index + 1,tmpSubString.length()).trim();
				
				src2 = tmpSubString.trim();
				src2Value = getSourceRegValue(src2);
				
				src2PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src2);
				iqEntry.setSrc2(src2PhysRegName);
				iqEntry.setSrc2Value(src2Value);
			
			}
			
			destPhyReg = assignFreeReg(destRegName);
			iqEntry.setDestRegName(destPhyReg);
			robEntry.setDestPhyRegName(destPhyReg);
			robEntry.setDestArchReg(destRegName);
			
			iqEntry.setPc(Decode.PC);
			iqEntry.setOpcode(opcode);
			

			IQ.Enqueue(iqEntry);
			
		}
		//Decode logic for mem instructions
		else if(MEMInstructions.contains(opcode))
		{
			// Add entry to IQ
				LSQEntry lsqEntry = new LSQEntry();
			
				if(opcode.equalsIgnoreCase("LOAD"))
				{
					String tmpReg;
					int destIndex = tmpSubString.indexOf(' ');
					tmpReg = tmpSubString.substring(0, destIndex).trim();
					destRegName = tmpReg;
					
					
					//System.out.println(dest);
					tmpSubString = tmpSubString.substring(destIndex + 1,tmpSubString.length()).trim();
					
					int src1Index = tmpSubString.indexOf(' ');
					tmpReg = tmpSubString.substring(0, src1Index);
					src1 = tmpReg;
					src1Value = getSourceRegValue(src1);
					
					
					src1PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src1);
					
					lsqEntry.setSrc1(src1PhysRegName);
					lsqEntry.setSrc1Value(src1Value);
					
					tmpSubString = tmpSubString.substring(src1Index + 1,tmpSubString.length()).trim();
							
					if(Utility.isInteger(tmpSubString))
					{
						literalValue = Integer.parseInt(tmpSubString); 
						lsqEntry.setLiteralValue(literalValue);
					}
					else
					{
						src2 = tmpSubString.trim();
						src2Value = getSourceRegValue(src2);
						
						
						src2PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src2);
						
						
						lsqEntry.setSrc2(src2PhysRegName);
						lsqEntry.setSrc2Value(src2Value);
					}
					
					destPhyReg = assignFreeReg(destRegName);
					lsqEntry.setDestRegName(destPhyReg);
					robEntry.setDestPhyRegName(destPhyReg);
					robEntry.setDestArchReg(destRegName);
				}
				//decode logic for store instruction
				if(opcode.equalsIgnoreCase("STORE"))
				{
					String tmpReg;
					int src1Index = tmpSubString.indexOf(' ');
					tmpReg = tmpSubString.substring(0, src1Index).trim();
					src1 = tmpReg;
					src1Value =getSourceRegValue(src1);
					
					src1PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src1);
					
					robEntry.setDestPhyRegName(src1PhysRegName);
					robEntry.setDestArchReg(src1);
					
					lsqEntry.setSrc1(src1PhysRegName);
					lsqEntry.setSrc1Value(src1Value);
	
					tmpSubString = tmpSubString.substring(src1Index + 1,tmpSubString.length()).trim();
					
					int src2Index = tmpSubString.indexOf(' ');
					tmpReg = tmpSubString.substring(0, src2Index);
					src2 = tmpReg;
					src2Value = getSourceRegValue(src2);
					
					src2PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src2);
					
					lsqEntry.setSrc2(src2PhysRegName);
					lsqEntry.setSrc2Value(src2Value);
	
					tmpSubString = tmpSubString.substring(src2Index + 1,tmpSubString.length()).trim();
	
					
					if(Utility.isInteger(tmpSubString))
					{
							literalValue = Integer.parseInt(tmpSubString); 
							lsqEntry.setLiteralValue(literalValue);
					}
					else
					{
							src3ForStore = tmpSubString.trim();
							src3ForStoreValue = getSourceRegValue(src3ForStore);
							
							src3PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src3ForStore);
							lsqEntry.setSrc3ForStore(src3PhysRegName);
							lsqEntry.setSrc3ForStoreValue(src3ForStoreValue);
					}
				}
				lsqEntry.setPc(Decode.PC);
				lsqEntry.setOpcode(opcode);
				
	
				LSQ.LSQEnqueue(lsqEntry);
			}
			else if(CFInstructions.contains(opcode))
			{
				// Add entry to IQ
				IQEntry iqEntry = new IQEntry();
				if(opcode.equalsIgnoreCase("BZ"))
				{
					literalValue = Integer.parseInt(tmpSubString);
					iqEntry.setLiteralValue(literalValue);
	
					
					src2Value = getSourceRegValue(ROB.getArchRegName((int)PC - 1));
					iqEntry.setSrc2Value(src2Value);
					iqEntry.setSrc2(ROB.getPhysRegName((int)PC - 1));
				}
				else if(opcode.equalsIgnoreCase("BNZ"))
				{
					literalValue = Integer.parseInt(tmpSubString);
					iqEntry.setLiteralValue(literalValue);
					
				
					src2Value = getSourceRegValue(ROB.getArchRegName((int)PC - 1));
					iqEntry.setSrc2Value(src2Value);
					iqEntry.setSrc2(ROB.getPhysRegName((int)PC - 1));
					
					
				}
				else if(opcode.equalsIgnoreCase("JUMP") || opcode.equalsIgnoreCase("BAL"))
				{
					String tmpReg;
					int src1Index = tmpSubString.indexOf(' ');
					tmpReg = tmpSubString.substring(0, src1Index);
					src1 = tmpReg;
					src1Value = getSourceRegValue(src1);
					
					if(src1.equals("X"))
					{
						iqEntry.setSrc1("X");
						iqEntry.setSrc1Value(src1Value);
					}
					else
					{
						src1PhysRegName = RegFileClass.getAllocatedPhysicalRegister(src1);
						iqEntry.setSrc1(src1PhysRegName);
						iqEntry.setSrc1Value(src1Value);
					}
					
					tmpSubString = tmpSubString.substring(src1Index + 1,tmpSubString.length()).trim();
					
					literalValue = Integer.parseInt(tmpSubString);
					iqEntry.setLiteralValue(literalValue);
					if(opcode.equalsIgnoreCase("BAL"))
					{
						iqEntry.setDestRegName("X");
						robEntry.setDestArchReg("X");
					}
					
					
				}
				iqEntry.setPc(Decode.PC);
				iqEntry.setOpcode(opcode);
				
	
				IQ.Enqueue(iqEntry);
			}
	
			
			
	
			robEntry.setPc(Decode.PC);
			robEntry.setOpcode(opcode);
					
			ROB.enqueue(robEntry);
	
			} 
			else 
			{
				stallFlag = true;
			}
			

		
	}
	
	void addUnusedRegistersToFreeList()
	{
		for(String phyReg: RegFileClass.validTable.keySet()){
			if(RegFileClass.validTable.get(phyReg) && !RegFileClass.getPhysicalRegisterAllocationStatus(phyReg)){
				RegFileClass.addToFreeList(phyReg);
			}
		}
		
	}
	
	//Check for free physical register and add Entry To alias table and mark register as invalid
	String assignFreeReg(String destArch)
	{
		String destPhyReg="";
		
		if(!RegFileClass.isFreeListEmpty()){
			//Mark last phy reg ass allocation=0
			
			RegFileClass.setPhysicalRegisterAllocationBit(RegFileClass.getAllocatedPhysicalRegister(destArch), false);
			destPhyReg= RegFileClass.getFreePhysReg();
			RegFileClass.addEntryToAlliasTable(destArch, destPhyReg, true);
			RegFileClass.setPhysicalRegisterInvalid(destPhyReg);
			RegFileClass.setPhysicalRegisterAllocationBit(destPhyReg, true);
		}
		return destPhyReg;
	}
	
	
	//If valid from ROB take ROB value or 
	int getSourceRegValue(String archSrcReg)
	{
		if(archSrcReg.equals("X"))
		{
			return RegFileClass.getArchRgisterValue("X");
		}
		if(RegFileClass.isValidFromROB(archSrcReg))
		{
			if(RegFileClass.isPhysicalRegisterValid(RegFileClass.getAllocatedPhysicalRegister(archSrcReg)))
				return ROB.getROBEntryValue(RegFileClass.getAllocatedPhysicalRegister(archSrcReg));
			else
				return 0;
		}
		return RegFileClass.getArchRgisterValue(archSrcReg);

	}
	
	
	//call this for source register
	boolean isFreePhyRegisterNeeded(String register)
	{
	 return !RegFileClass.isValidFromROB(register);
		 
	}

	public String getOpcode() {
		return opcode;
	}


	public String getDestRegName() {
		return destRegName;
	}

	public String getSrc1() {
		return src1;
	}

	public String getSrc2() {
		return src2;
	}

	public int getSrc1Value() {
		return src1Value;
	}

	public int getSrc2Value() {
		return src2Value;
	}
	

	public int getLiteralValue() {
		return literalValue;
	}


	public boolean getStallFlag() {
		return stallFlag;
	}


	public String getSrc3ForStore() {
		return src3ForStore;
	}

	public int getSrc3ForStoreValue() {
		return src3ForStoreValue;
	}

	public void flush()
	{
		PC = 0;
		opcode = "NOP";
		destRegName = null;
		src1 = null;
		src1Value = 0;
		src2 = null;
		src2Value = 0;
		src3ForStore = null;
		src3ForStoreValue = 0;
		literalValue = 0;
		stallFlag = false;
	}

	//this method is used to reset valid flag in case of brnach instructions.
	public  void resetValidFlag()
	{
		if(destRegName != null)
		{
			RegFileClass.setPhysicalRegisterValid(destRegName);
		}
	}
	
	public void Disaply() 
	{
		System.out.println();
		String destPhysRegName = null;
		;
		if(destRegName != "" && destRegName != null)
		{
			destPhysRegName = RegFileClass.getAllocatedPhysicalRegister(destRegName);
		}
		
		
			
		String st =  "Decode [PC=" + PC + ",  opcode=" + opcode + ", destRegName=" + destRegName + " , destPhysRegName= " + destPhysRegName + ", src1=" + src1 + " , src1PhysRegName " + src1PhysRegName +  ", src1Value="
				+ src1Value + ",  src2=" + src2 + " , src2PhysReName = " + src2PhysRegName +  ",  src2Value=" + src2Value + ", literalValue=" + literalValue + " ,  src3ForStore= " + src3ForStore + " , src3ForStorePhysRegName =  " + src3PhysRegName +" ,  src3ForStoreValue= " + 					src3ForStoreValue +  ",  isStalled= " + stallFlag + "]";
		System.out.println(st);
	}


	

}
