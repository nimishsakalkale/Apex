import java.util.*;
public class IQ 
{
	static ArrayList<IQEntry> iqList=new ArrayList<IQEntry>();
	
	// when decode will call Enqueue() it will pass below 7 parameters in entry object:-
	//pc,opcode,destRegName,src1(P),src2(P),src3(P),literal.
	
	static void Enqueue(IQEntry entry)
	{
		
		
		// setting the function unit as per opcode
		if(entry.opcode.equals("ADD")||entry.opcode.equals("SUB")||entry.opcode.equals("MOVC")||entry.opcode.equals("MOV")
			||entry.opcode.equals("AND")||entry.opcode.equals("OR")||entry.opcode.equals("EX-OR")||entry.opcode.equals("JUMP")||entry.opcode.equals("BZ")||entry.opcode.equals("BNZ")||entry.opcode.equals("BAL")||entry.opcode.equals("HALT"))
		{
			entry.setFuType("INT");
		}
		else if(entry.opcode.equals("MUL"))
		{
			entry.setFuType("MUL");
		}
		else if(entry.opcode.equals("LOAD")||entry.opcode.equals("STORE"))
		{
			entry.setFuType("MEMORY");
		}							
		iqList.add(entry);
	}
	
	static IQEntry Dequeue(String funcUnit)
	{		
		boolean dequePossible=false;
		IQEntry entry=new IQEntry();
		for(int i=0;i<iqList.size();i++)
		{
			entry=iqList.get(i);
			if(entry.fuType.equals(funcUnit))
			{
				if(entry.isIQEntryValid(entry))
				{
					dequePossible=true;
					break;					
				}
			}
		}
		if(dequePossible)
		{
			IQEntry tmpEntry=entry;
			iqList.remove(entry);
			return tmpEntry;			
		}
		else
		{
		return null;
		}
	}
	
	static void flushIQEntriesAfterBranchTaken(int PC)
	{
		List<IQEntry> tmpIQ = new ArrayList<IQEntry>();
		for(int counter = 0; counter < iqList.size(); counter++)
		{
			IQEntry tmpIQEntry = iqList.get(counter);
			if(tmpIQEntry.getPc() > PC )
			{
				String physRegName = tmpIQEntry.destRegName;
				if(physRegName != null && physRegName != "")
				{
					RegFileClass.setPhysicalRegisterValid(physRegName);
					RegFileClass.AllocatedList.put(physRegName, false);
					RegFileClass.addToFreeList(physRegName);
				
				}
				tmpIQ.add(tmpIQEntry);
			}
		}
		if(!tmpIQ.isEmpty())
		{
			iqList.removeAll(tmpIQ);
		}
		
		ROB.walkBack();
	}
	
	
	
	// this method will receive values from functional unit after execution is completed
		// and result is calculated.
		static void broadcast(String destRegName, int result)
		{
			IQEntry entry;
			for(int i=0;i<IQ.iqList.size();i++)
			{
				entry=IQ.iqList.get(i);
				
				if(entry.src1 != null && entry.src1 != "" && entry.src1.equals(destRegName))
				{
					entry.src1Value=result;				
				}
				if(entry.src2 != null && entry.src2 != "" && entry.src2.equals(destRegName))
				{
					entry.src2Value=result;
				}
				if(entry.src3ForStore != null && entry.src3ForStore != "" && entry.src3ForStore.equals(destRegName))
				{
					entry.src3ForStoreValue=result;				
				}
			}
		}
	
	
	static boolean isEmpty()
	{
		if (iqList.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}					
	}
	static boolean isFull()
	{
		if(iqList.size()<6)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	static void Display()
	{
		
		System.out.println("IQ Size="+iqList.size());
		
		for(int i=0;i<iqList.size();i++)
		{
			System.out.print("PC");
			System.out.print(" "+iqList.get(i).getPc());
			
			System.out.print(",Opcode");
			System.out.print(" "+iqList.get(i).getOpcode());
			
			System.out.print(",Src1");
			System.out.print(" "+iqList.get(i).getSrc1());
			
			System.out.print(",Value");
			System.out.print(" "+iqList.get(i).getSrc1Value(iqList.get(i).getSrc1()));
			
			System.out.print(",Src2");
			System.out.print(" "+iqList.get(i).getSrc2());
			
			System.out.print(",Value");
			System.out.print(" "+iqList.get(i).getSrc2Value(iqList.get(i).getSrc2()));
			
			System.out.print(",Src3ForStore");
			System.out.print(" "+iqList.get(i).getSrc3ForStore());
			
			System.out.print(",Value");
			System.out.print(" "+iqList.get(i).getSrc3ForStoreValue(iqList.get(i).getSrc3ForStore()));
			
			System.out.print(",Literal");
			System.out.print(" "+iqList.get(i).getLiteralValue());
			
			System.out.print(",Dest Reg");
			System.out.print(" "+iqList.get(i).getDestRegName());
			
			System.out.print(",FuType");
			System.out.print(" ["+iqList.get(i).getFuType()+"]");		
			
			System.out.print(",Entry Valid.");
			System.out.println(" "+iqList.get(i).isIQEntryValid(iqList.get(i)));
		
		}	
		
		System.out.println();
	}
	
}
