import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.RepaintManager;


//This class contains data for register file , special register X , valid bits for architectural registers. 
//Method names are self explainotary.
public class RegFileClass 
{
	static class AliasTableEntry
	{
		String archRegName;
		String physRegName;
		boolean isValidValueFromROB; //  false if valid value is from architectural register, true then valid value is in ROB registers.
		
		AliasTableEntry(String archRegName, String physRegName, boolean isValidValueFromROB) 
		{
				this.archRegName = archRegName;
				this.physRegName = physRegName;
				this.isValidValueFromROB = isValidValueFromROB;
		}

		@Override
		public String toString() {
			return "AliasTableEntry [archRegName=" + archRegName + ", physRegName=" + physRegName
					+ ", isValidValueFromROB=" + isValidValueFromROB + "]";
		}
	}
	//Architectural Registers
	static Map <String,Integer> archRegisterFile ;
	static Map <String,Boolean> PSW ;
	static Map <String, Boolean> validTable; 				//valid table for physical registers
	static Map  <String, AliasTableEntry>  AliasTable; 		//  Allias table
	static Map <String,Boolean> AllocatedList; 				// allocation list
	static ArrayList<Integer> FreeList; 					// Free List. Indices stand for indices of free physical reister.	
//	static Map  <String,Integer> physicalRegrsiterValues ;
	static Map <String, Boolean> Renamed;
	static 
	{
		Initialize();
	}
	

	public static void Initialize()
	{
		
		//Architectural Registers
		 archRegisterFile = new HashMap<String, Integer>();
		 archRegisterFile.put("R0", 0);
		 archRegisterFile.put("R1", 0);
		 archRegisterFile.put("R2", 0);
		 archRegisterFile.put("R3", 0);
		 archRegisterFile.put("R4", 0);
		 archRegisterFile.put("R5", 0);
		 archRegisterFile.put("R6", 0);
		 archRegisterFile.put("R7", 0);
		 archRegisterFile.put("X", 0);
		 
		 validTable = new HashMap<String, Boolean>();
		 validTable.put("P0", true);
		 validTable.put("P1", true);
		 validTable.put("P2", true);
		 validTable.put("P3", true);
		 validTable.put("P4", true);
		 validTable.put("P5", true);
		 validTable.put("P6", true);
		 validTable.put("P7", true);
		 validTable.put("P8", false);
		 validTable.put("P9", false);
		 validTable.put("P10", false);
		 validTable.put("P11", false);
		 validTable.put("P12", false);
		 validTable.put("P13", false);
		 validTable.put("P14", false);
		 validTable.put("P15", false);
		 validTable.put("X", false);
		 
		 PSW = new HashMap<String, Boolean>();
		 PSW.put("Z", false);
//		 PSW.put("C", false);
//		 PSW.put("O", false);
		 
		 
		 
//		AraryLlist initiallizations : iNDEX IS FOR PHYSICAL REGISTERS
		 FreeList = new ArrayList<Integer>();
		 
		 FreeList.add(8);
		 FreeList.add(9);
		 FreeList.add(10);
		 FreeList.add(11);
		 FreeList.add(12);
		 FreeList.add(13);
		 FreeList.add(14);
		 FreeList.add(15);
		 
		 // Allocated list
		 AllocatedList = new HashMap<String, Boolean>();
		 AllocatedList.put("P0", true);
		 AllocatedList.put("P1", true);
		 AllocatedList.put("P2", true);
		 AllocatedList.put("P3", true);
		 AllocatedList.put("P4", true);
		 AllocatedList.put("P5", true);
		 AllocatedList.put("P6", true);
		 AllocatedList.put("P7", true);
		 AllocatedList.put("P8", false);
		 AllocatedList.put("P9", false);
		 AllocatedList.put("P10", false);
		 AllocatedList.put("P11", false);
		 AllocatedList.put("P12", false);
		 AllocatedList.put("P13", false);
		 AllocatedList.put("P14", false);
		 AllocatedList.put("P15", false);
		 
		 //RenamedList
		Renamed = new HashMap<String, Boolean>();
		Renamed.put("P0", false);
		Renamed.put("P1", false);
		Renamed.put("P2", false);
		Renamed.put("P3", false);
		Renamed.put("P4", false);
		Renamed.put("P5", false);
		Renamed.put("P6", false);
		Renamed.put("P7", false);
		Renamed.put("P8", false);
		Renamed.put("P9", false);
		Renamed.put("P10", false);
		Renamed.put("P11", false);
		Renamed.put("P12", false);
		Renamed.put("P13", false);
		Renamed.put("P14", false);
		Renamed.put("P15", false);
		
		//Initiallizing Alias table
		AliasTable = new HashMap<String, RegFileClass.AliasTableEntry>();
		AliasTable.put("R0", new AliasTableEntry("R0", "P0", false));
		AliasTable.put("R1", new AliasTableEntry("R1", "P1", false));
		AliasTable.put("R2", new AliasTableEntry("R2", "P2", false));
		AliasTable.put("R3", new AliasTableEntry("R3", "P3", false));
		AliasTable.put("R4", new AliasTableEntry("R4", "P4", false));
		AliasTable.put("R5", new AliasTableEntry("R5", "P5", false));
		AliasTable.put("R6", new AliasTableEntry("R6", "P6", false));
		AliasTable.put("R7", new AliasTableEntry("R7", "P7", false));

	} 
	public static void Display()
	{
			System.out.println();
			System.out.print("Register File : ");
			System.out.print("[R0 " + ": " + archRegisterFile.get("R0") + ", ");
			System.out.print(" R1 " + ": " + archRegisterFile.get("R1") + ", ");
			System.out.print(" R2 " + ": " + archRegisterFile.get("R2") + ", ");
			System.out.print(" R3 " + ": " + archRegisterFile.get("R3") + ", ");
			System.out.print(" R4 " + ": " + archRegisterFile.get("R4") + ", ");
			System.out.print(" R5 " + ": " + archRegisterFile.get("R5") + ", ");
			System.out.print(" R6 " + ": " + archRegisterFile.get("R6") + ", ");
			System.out.print(" R7 " + ": " + archRegisterFile.get("R7") + ", ");
			System.out.print(" X " + ": " + archRegisterFile.get("X") + "] ");			
		
			System.out.println("");
			System.out.print("PSW           : ");
			System.out.print("[Z " + ": " + PSW.get("Z") + "] ");
			
			System.out.println("");
			
	
	}
	
	public static void setZeroFlag(Boolean b)
	{
		PSW.put("Z", b);
		
	}

	
	public static void flush()
	{
		Initialize();
	}
	/*public static void setOverflowFlag(Boolean b)
	{
		PSW.put("O", b);
		
	}
	
	public static void setCarryFlag(Boolean b)
	{
		PSW.put("C", b);
		
	}
	*/
	
	/*
	public static boolean getOverflowFlag()
	{
		return PSW.get("O");
	}
	
	public static boolean getCarryFlag()
	{
		return PSW.get("C");
	}
	*/
	public static boolean getZeroFlag()
	{
		return PSW.get("Z");
	}
	
	public static void setPhysicalRegisterInvalid(String register)
	{
		validTable.put(register, false);
	}
	
	public static void setPhysicalRegisterValid(String register)
	{
		validTable.put(register,true);
	}
	
	public static boolean isPhysicalRegisterValid(String register)
	{
		return validTable.get(register);
	}
	
	
	/*
	 * Free list methods
	 */
	
	public static boolean isFreeListEmpty()
	{
		return FreeList.isEmpty();
	}
	
	public static void addToFreeList(String phyRegName)
	{
		int indexOfPhysReg = Integer.parseInt(phyRegName.substring(1));
		if(! FreeList.contains(indexOfPhysReg))
		{
			FreeList.add(indexOfPhysReg);
		}
	}
	
	/*
	 * this function returns null if no physical register is available
	 * otherwise returns physical register name
	 * it mimics queue structure
	 */
	public static String getFreePhysReg()
	{
		if(!isFreeListEmpty())
		{
			int physRegIndex = FreeList.get(0);
			String physRegName = "P" + physRegIndex;
			FreeList.remove(0);
			return physRegName;
		}
		
		return null;
	}
	
	
	
	
	public static int getArchRgisterValue(String archRegName)
	{
		return archRegisterFile.get(archRegName);
	}
	
	public static void setArchRegisterValue(String archRegName, int value)
	{
		archRegisterFile.put(archRegName, value);
	}
	/*
	 * Allias Table Methods
	 * 
	 * Method To set sourceFlag of physical register
	 * Method to take rename table entry
	 * Method to give value of physical register value depending on source of valid value.
	 */
	
	public static void addEntryToAlliasTable(String archRegName, String physRegName, boolean isValidValueFromROB)
	{
		AliasTableEntry entryObj =  new AliasTableEntry(archRegName, physRegName, isValidValueFromROB);
		AliasTable.put(archRegName, entryObj);
		
	}
	
	public static AliasTableEntry getAliasTableEntry(String archRegName)
	{
		return AliasTable.get(archRegName);
	}
	public static void updateSourceBitOfAlliasTable(String archRegName, boolean isValidValueFromROB)
	{
		AliasTableEntry tmpAliasTableEntryObj = getAliasTableEntry(archRegName);
		tmpAliasTableEntryObj.isValidValueFromROB = isValidValueFromROB;
		AliasTable.put(archRegName, tmpAliasTableEntryObj);
		
	}
	
	public static boolean isValidFromROB(String archRegName)
	{
		if(archRegName.equals ("X"))
			return true;
		return AliasTable.get(archRegName).isValidValueFromROB;
	}
	
	public static String getAllocatedPhysicalRegister(String archRegName)
	{
		return AliasTable.get(archRegName).physRegName;
	}
	/*
	 * allocated List methods
	 */
	
	public static  void setPhysicalRegisterAllocationBit( String physRegName, boolean allocate)
	{
		AllocatedList.put(physRegName,allocate);
	}
	
	public static  boolean getPhysicalRegisterAllocationStatus( String physRegName)
	{
		return AllocatedList.get(physRegName);
	}
	
	/*
	 * renmaed methods
	 */
	
	public static void setReanmedBit(String physRegName)
	{
		Renamed.put(physRegName, true);
		
	}
	public static void resetReanmedBit(String physRegName)
	{
		Renamed.put(physRegName, false);
		
	}
	
	
	public static void DisplayArchRegisterValues()
	{
		System.out.println("");
		System.out.print("Pysical Register Values    : ");
		System.out.print("[R0 " + ": " + archRegisterFile.get("R0") + ", ");
		System.out.print(" R1 " + ": " + archRegisterFile.get("R1") + ", ");
		System.out.print(" R2 " + ": " + archRegisterFile.get("R2") + ", ");
		System.out.print(" R3 " + ": " + archRegisterFile.get("R3") + ", ");
		System.out.print(" R4 " + ": " + archRegisterFile.get("R4") + ", ");
		System.out.print(" R5 " + ": " + archRegisterFile.get("R5") + ", ");
		System.out.print(" R6 " + ": " + archRegisterFile.get("R6") + ", ");
		System.out.print(" R7 " + ": " + archRegisterFile.get("R7") + "]");
		
	}
	
	
	public static void displayAllocatedList()
	{
		System.out.println("");
		System.out.print("Allcation list Values    : ");
		System.out.print("[P0 " + ": " + AllocatedList.get("P0") + ", ");
		System.out.print(" P1 " + ": " + AllocatedList.get("P1") + ", ");
		System.out.print(" P2 " + ": " + AllocatedList.get("P2") + ", ");
		System.out.print(" P3 " + ": " + AllocatedList.get("P3") + ", ");
		System.out.print(" P4 " + ": " + AllocatedList.get("P4") + ", ");
		System.out.print(" P5 " + ": " + AllocatedList.get("P5") + ", ");
		System.out.print(" P6 " + ": " + AllocatedList.get("P6") + ", ");
		System.out.print(" P7 " + ": " + AllocatedList.get("P7") + ", ");
		System.out.print(" P8 " + ": " + AllocatedList.get("P8") + ", ");
		System.out.print(" P9 " + ": " + AllocatedList.get("P9") + ", ");
		System.out.print(" P10 " + ": " + AllocatedList.get("P10") + ", ");
		System.out.print(" P11 " + ": " + AllocatedList.get("P11") + ", ");
		System.out.print(" P12 " + ": " + AllocatedList.get("P12") + ", ");
		System.out.print(" P13 " + ": " + AllocatedList.get("P13") + ", ");
		System.out.print(" P14 " + ": " + AllocatedList.get("P14") + ", ");
		System.out.print(" P15 " + ": " + AllocatedList.get("P15") + "] ");
	}
	
	public static void displayAliasTableEntries()
	{
		System.out.println("");
		System.out.print("Alias Table Entries   : ");
		System.out.println("[R0 " + ": " + AliasTable.get("R0") + ", ");
		System.out.println("[R1 " + ": " + AliasTable.get("R1") + ", ");
		System.out.println("[R2 " + ": " + AliasTable.get("R2") + ", ");
		System.out.println("[R3 " + ": " + AliasTable.get("R3") + ", ");
		System.out.println("[R4 " + ": " + AliasTable.get("R4") + ", ");
		System.out.println("[R5 " + ": " + AliasTable.get("R5") + ", ");
		System.out.println("[R6 " + ": " + AliasTable.get("R6") + ", ");
		System.out.println("[R7 " + ": " + AliasTable.get("R7") + ", ");
	}
	
	public static void displayRenamedTableEntries()
	{
		System.out.println("");
		System.out.print("Renamed table Values    : ");
		System.out.print("[P0 " + ": " + Renamed.get("P0") + ", ");
		System.out.print(" P1 " + ": " + Renamed.get("P1") + ", ");
		System.out.print(" P2 " + ": " + Renamed.get("P2") + ", ");
		System.out.print(" P3 " + ": " + Renamed.get("P3") + ", ");
		System.out.print(" P4 " + ": " + Renamed.get("P4") + ", ");
		System.out.print(" P5 " + ": " + Renamed.get("P5") + ", ");
		System.out.print(" P6 " + ": " + Renamed.get("P6") + ", ");
		System.out.print(" P7 " + ": " + Renamed.get("P7") + ", ");
		System.out.print(" P8 " + ": " + Renamed.get("P8") + ", ");
		System.out.print(" P9 " + ": " + Renamed.get("P9") + ", ");
		System.out.print(" P10 " + ": " + Renamed.get("P10") + ", ");
		System.out.print(" P11 " + ": " + Renamed.get("P11") + ", ");
		System.out.print(" P12 " + ": " + Renamed.get("P12") + ", ");
		System.out.print(" P13 " + ": " + Renamed.get("P13") + ", ");
		System.out.print(" P14 " + ": " + Renamed.get("P14") + ", ");
		System.out.print(" P15 " + ": " + Renamed.get("P15") + "] ");
	}
	
	public static void displayFreeList()
	{
		System.out.println("");
		System.out.print("Free List    : ");
		System.out.println(FreeList.toString());
	}
	
	public static void displayValidBitsOfPhysicalReissters()
	{
		System.out.println("");
		System.out.print("Valid Bits    : ");
		System.out.println(validTable.toString());
	}
}
