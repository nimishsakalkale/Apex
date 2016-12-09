import java.awt.DisplayMode;
import java.util.ArrayList;
import java.util.List;




public class ROB {

	// Circular Queue

	static List<ROBEntry> robList = new ArrayList<ROBEntry>();

	// Fields required to set before calling this
	// pc
	// opcode
	// destRegName
	// destArchReg

	// This will add entry to ROB
	static boolean enqueue(ROBEntry entry) {

		if (!isROBListFull()) 
		{
			robList.add(entry);
			return true;
		}

		return false;
	}

	// Thsi function returns value from ROB if not availabel returns null
	public static Integer getAddressFromROB(String memoryAddress)
	{
		
		if(!isROBListEmpty())
		{
			for(int counter = 0 ; counter < robList.size(); counter++)
			{
				ROBEntry tmpROBEntry  = robList.get(counter);
				if(tmpROBEntry.getMemAddress() != null && tmpROBEntry.getMemAddress().trim().equals(memoryAddress.trim()))
				{
					return tmpROBEntry.getMemAddressValue();
				}
			}
		}
		return null;
		
	}
	
	//This method is written to handle branch instructions . 
	static void flushROBEntriesFromBranch(int PC)
	{
		int robIndexOfPCInstruction = -1 ;
		
		List<ROBEntry> tmpRobList = new ArrayList<ROBEntry>();
		if(!isROBListEmpty())
		{
			for(ROBEntry tmpEntryObj : robList )
			{
				if(tmpEntryObj.pc > PC)
				{
						tmpRobList.add(tmpEntryObj);
				}
			}
		}
		
		if(!tmpRobList.isEmpty())
		{
			robList.removeAll(tmpRobList);
		}
		
		
		
	}
	
	// This function will retire instruction.
	static void retireValidInstructionsFromHead() {



		while (getHeadEntry() != null && getHeadEntry().isCompleted()) {
			ROBEntry entry = getHeadEntry();
			if(entry.getOpcode().equalsIgnoreCase("HALT"))
			{
				System.out.println();				
				System.out.println("Halt detected at Retirement from ROB at clock : " + ( Simulator.clock ));
				System.out.println("Contents of pipeline  stages before stoppng execution : ");
				Simulator.Display();
				System.out.println();
				
				System.exit(0);
				
			}
			else if(entry.getOpcode().equalsIgnoreCase("STORE")){
				MemoryClass.SetMemoryValue(Integer.parseInt(entry.getMemAddress()), entry.getMemAddressValue());
			}
			else
			{
				// write value to architectural register which is at head
				RegFileClass.setArchRegisterValue(entry.getDestArchReg(), entry.getValue());
			}
			// remove this entry
			robList.remove(0);

			// Mark rename table's tag
	//		RegFileClass.addEntryToAlliasTable(entry.getDestArchReg(), entry.getDestPhyRegName(), false);
			if(entry.getDestPhyRegName()!=null && entry.getDestArchReg() != null && entry.getDestPhyRegName().equals(RegFileClass.getAllocatedPhysicalRegister(entry.getDestArchReg())))
			{
				RegFileClass.updateSourceBitOfAlliasTable(entry.getDestArchReg(), false);
			}
			else
			{
			
				if(entry.getDestArchReg() != null && !entry.getDestArchReg().equals("X"))
				{
					RegFileClass.updateSourceBitOfAlliasTable(entry.getDestArchReg(), true);
				}
			}

		}

	}

	// FU will call this after its execution.
	// Either here we can put value in physical register or in FU logic
	static void markROBEntryCompleted(long pc,int value) 
	{
		for (ROBEntry entry : robList) 
		{
			if (pc == entry.getPc()) 
			{
				entry.setValue(value);
				entry.setCompleted(true);
				break;
			}
		}
	}
	
	static void markROBEntryCompletedForStore(long pc,int storeAddress , int storeValue) {
		for (ROBEntry entry : robList) {
			if (pc == entry.getPc()) 
			{
				entry.setMemAddress("" + storeAddress);
				entry.setMemAddressValue(storeValue);
				entry.setCompleted(true);
				break;
			}
		}
	}
	
	static void walkBack()
	{
		ROBEntry tmpROBEntry ;

		
		for(String key : RegFileClass.AliasTable.keySet())
		{
			RegFileClass.updateSourceBitOfAlliasTable(key, false);
			
		}
		for(int counter = 0; counter < robList.size();counter ++ )
		{
			tmpROBEntry = robList.get(counter);
			String archRegName = tmpROBEntry.getDestArchReg();
			String physRegName = tmpROBEntry.getDestPhyRegName();
			boolean isValidValueFromROB = true;
			RegFileClass.addEntryToAlliasTable(archRegName, physRegName, isValidValueFromROB);

		}
		
	
	}

	public static ROBEntry getHeadEntry() {
		if (isROBListEmpty())
			return null;
		return robList.get(0);
	}

	// This will give last entry at tail
	static ROBEntry getTail() {
		return robList.get(robList.size() - 1);
	}

	static boolean isROBListEmpty() {
		
		return robList.isEmpty();
	}

	static boolean isROBListFull() {
		if (robList.size() < 16) {
			return false;
		} else {
			return true;
		}
	}
	
	static int getROBEntryValue(String phyReg){
		for (ROBEntry entry : robList) {
			if (entry.getDestPhyRegName() == phyReg) {
				return entry.getValue();
				
			}
		}
		return 0;
	}
	
	//Provides output based on instruction has completed or not
	static boolean isROBEntryValid(String phyReg)
	{
		for (ROBEntry entry : robList) {
			if (entry.getDestPhyRegName() == phyReg) {
				return entry.isCompleted();
			}
		}
		
		return false;
	}
	
	static String getPhysRegName(int PC)
	{
		for(ROBEntry entry: robList)
		{
			if((int)entry.getPc() == PC)
			{
				return entry.getDestPhyRegName();
			}
		}
		return null;
	}
	
	static String getArchRegName(int PC)
	{
		for(ROBEntry entry: robList)
		{
			if((int)entry.getPc() == PC)
			{
				return entry.getDestArchReg();
			}
		}
		return null;
	}
	
	//If valid from ROB take ROB value or 
	static int getSourceRegValue(String archSrcReg){
		if(RegFileClass.isValidFromROB(archSrcReg))
		{
			if(RegFileClass.isPhysicalRegisterValid(RegFileClass.getAllocatedPhysicalRegister(archSrcReg)))
				return ROB.getROBEntryValue(RegFileClass.getAllocatedPhysicalRegister(archSrcReg));
			else
				return 0;
		}
		return RegFileClass.getArchRgisterValue(archSrcReg);

	}
	
	static void display()
	{
		System.out.println("ROB Display()");

		System.out.println("ROB Size="+robList.size());
		for(int i=0;i<robList.size();i++)
		{
			System.out.print("[PC:  ");
			System.out.print(robList.get(i).getPc());
			
			System.out.print(",  Opcode:  ");
			System.out.print(" "+robList.get(i).getOpcode());
			
			System.out.print(",  Dest Phy Register:  ");
			System.out.print(" "+robList.get(i).getDestPhyRegName());
			
			System.out.print(",  Value");
			System.out.print(" "+robList.get(i).getValue());
			
			System.out.print(",  Dest Arch Reg");
			System.out.print(" "+robList.get(i).getDestArchReg());
			
			System.out.print(", MemAddress ");
			System.out.print(" "+robList.get(i).getMemAddress());
			
			System.out.print(", MemValue ");
			System.out.print(" "+robList.get(i).getMemAddressValue());
			
			System.out.print(",  Completed:");
			System.out.print(" "+robList.get(i).isCompleted() + "]");
			System.out.println();
			
		}		
		System.out.println();
	}
}
