
public class ROBEntry {
	

	
	public long pc;
	public String opcode;
	public String destPhyRegName;
	public int value;
	//THis means instruction has completed its execution.
	//This field will be set by FU after completing execution.
	public boolean completed;
	public String destArchReg;
	public String exceptionCode;
	public String memAddress;
	public int memAddressValue;
	
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
	public String getDestPhyRegName() {
		return destPhyRegName;
	}
	public void setDestPhyRegName(String destRegName) {
		this.destPhyRegName = destRegName;
	}
	
	
	
	public String getDestArchReg() {
		return destArchReg;
	}
	public void setDestArchReg(String destArchReg) {
		this.destArchReg = destArchReg;
	}
	
	public String getExceptionCode() {
		return exceptionCode;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getMemAddress() {
		return memAddress;
	}
	public void setMemAddress(String memAddress) {
		this.memAddress = memAddress;
	}
	public int getMemAddressValue() {
		return memAddressValue;
	}
	public void setMemAddressValue(int memAddressValue) {
		this.memAddressValue = memAddressValue;
	}
	
	
}
