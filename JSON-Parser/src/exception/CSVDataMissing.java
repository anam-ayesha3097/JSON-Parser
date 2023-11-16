package exception;
//---------------------------------------------------------------
//Assignment 2 - PartII
//© Anam Ayesha Shaikh
//Written by: Anam Ayesha Shaikh - 40205690
//CSVDataMissing Class - Exception Class for Row Data Value Missing 
//						 Child class of parent InvalidException
//---------------------------------------------------------------

/*
* User Defined Exception Class for Row Data Value Missing
* */
public class CSVDataMissing extends InvalidException {

	/*
	    * Method Default Constructor with Error Message
	    * */
	public CSVDataMissing(){
		super("Missing data");
	}
	
	/*
	    * Method Parameterized Constructor with Error Message
	    * @param error message
	    * */
	public CSVDataMissing(String msg) {
		super(msg);
	}
	
	
	/*
	    * Method Parameterized Constructor with Error Message and Throwable object
	    * @param error message and throwable error object
	    * */
	public CSVDataMissing(String msg, Throwable error) {
		super(msg, error);
	}
	
}
