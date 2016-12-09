import java.util.ArrayList;

public class LSQ 
{
	static ArrayList<LSQEntry> lsqList=new ArrayList<LSQEntry>();
		
	// this method will be called by decode if store instruction is detected.
	// pc should be passed in LSQEntry object.
	public static void LSQEnqueue(LSQEntry entry)
	{
		entry.lsqEntryStatus=false;
		lsqList.add(entry);				
	}
	
	// this method will be called by LSFU3 stage after execution of store instruction.
	public static void addValueInLSQ(long pc,int memoryAddress)
	{
		LSQEntry entry=new LSQEntry();
		for(int i=0;i<lsqList.size();i++)
		{
			entry=lsqList.get(i);
			if(entry.pc==pc)
			{
				entry.memoryAddress=memoryAddress;			
			}
		}
	}
		
	// this method will be called by LSFU3 stage after execution of store instruction.
	public static void updateLSQEntry(long pc, int memoryValue)
	{
		LSQEntry entry=new LSQEntry();
		for(int i=0;i<lsqList.size();i++)
		{
			entry=lsqList.get(i);
			if(entry.pc==pc)
			{
				entry.memoryValue=memoryValue;
				entry.lsqEntryStatus=true;				
			}
		}		
	}
	
	//this method will be called by ROB 
	 static LSQEntry LSQDequeue()
	{
		boolean dequePossible=false;
		LSQEntry entry=new LSQEntry();
		for(int i=0;i<lsqList.size();i++)
		{
			entry=lsqList.get(i);
			//entry=lsqList.get(0);
			if(entry.isLSQEntryValid(entry) && !Utility.isLoadBeforeStore(entry))
			{				
				dequePossible=true;
				break;		
			}	
		}
		if(dequePossible)
		{
			LSQEntry tmpEntry=entry;
			lsqList.remove(entry);
			return tmpEntry;			
		}
		else
		{
		return null;
		}		
	}
		 
	 
	 static void flushLSQEntriesAfterBranchTaken(int PC)
		{
			for(int counter = 0; counter < lsqList.size(); counter++)
			{
				LSQEntry tmpLSQEntry = lsqList.get(counter);
				if(tmpLSQEntry.getPc() > PC )
				{
					String physRegName = tmpLSQEntry.destRegName;
					RegFileClass.setPhysicalRegisterValid(physRegName);
					RegFileClass.AllocatedList.put(physRegName, false);
					RegFileClass.addToFreeList(physRegName);
					lsqList.remove(counter);
				}
			}
			
			ROB.walkBack();
		}
	 
	// this method will receive values from functional unit after execution is completed
		// and result is calculated.
		static void broadcast(String destRegName, int result)
		{
			LSQEntry entry;
			for(int i=0;i<lsqList.size();i++)
			{
				entry=lsqList.get(i);
				
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
	 
	static boolean isFull()
	{
		if(lsqList.size()<4)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	static boolean isEmpty()
	{
		if (lsqList.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}					
	}
	public static void display()
	{
		System.out.println("LSQ Size="+lsqList.size());
		for(int i=0;i<lsqList.size();i++)
		{
			System.out.print("[ PC="+lsqList.get(i).getPc());
			System.out.print(" Opcode="+lsqList.get(i).getOpcode());
			System.out.print(" DestRegName="+lsqList.get(i).getDestRegName());
			System.out.print(" Src1="+lsqList.get(i).getSrc1());
			System.out.print(" Src2"+lsqList.get(i).getSrc2());
			System.out.print(" LSQMemAddrs "+lsqList.get(i).getMemoryAddress());
			System.out.print(" LSQMemValue "+lsqList.get(i).getMemoryValue());
			System.out.print(" LSQEntryStatus "+lsqList.get(i).isLsqEntryStatus()+"]");
		}		
		System.out.println();
	}

}
