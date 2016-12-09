
//This class represents memory of simulator.

public class MemoryClass 
{

	static int [] memory;
	
	static
	{
		memory = new int[10000];
	}
	
	public static int GetValueFromMemory(int index)
	{
		try
		{
			return memory[index];
		}
		catch(Exception e)
		{
			System.out.println("Excepton found at clock " + Simulator.clock + " while reading memeory with index   " + index );
			return 0;
		}
	}
	
	public static void SetMemoryValue(int index , int value)
	{
		try
		{
			memory[index] = value;
		}
		catch(Exception e)
		{
			System.out.println("Excepton found at clock " + Simulator.clock + " while writing to  memeory with index   " + index );
		}
	}
	
	public static void Display()
	{
		System.out.println();
		System.out.println("First 100 Memory Locations Values");
		for(int counter = 0 ; counter < 100;)
		{
			for(int innerCounter = 0 ; innerCounter < 5 ; innerCounter ++)
			{
				System.out.print("|Index : " + counter + " ,\t Value : " + memory[counter] + "\t");
				counter++;
			}
			System.out.println();
		}
	}
}
