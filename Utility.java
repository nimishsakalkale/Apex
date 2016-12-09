
public class Utility 
{
	/*
	function to check whether given string can be parsed into integer or not.
	*/
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}

	//If store is in IQ, then check if load is earlier than that
	public static boolean isLoadBeforeStore(LSQEntry entry){
		boolean isLoadBeforeStore=false;
		if(entry.getOpcode().equalsIgnoreCase("LOAD")){
			for(int i=0;i<LSQ.lsqList.size();i++){
				if(LSQ.lsqList.get(i).getOpcode().equalsIgnoreCase("STORE") && (i < LSQ.lsqList.indexOf(entry))){
					isLoadBeforeStore=true;
					break;
				}
			}
		}
		return isLoadBeforeStore;
	}

}
