package exception;
//---------------------------------------------------------------
//Assignment 2 - PartII
//© Anam Ayesha Shaikh
//Written by: Anam Ayesha Shaikh - 40205690
//CSVFileInvalidException Class - Exception Class for Attribute Missing 
//						          Child class of parent InvalidException
//---------------------------------------------------------------

/*
* User Defined Exception Class for Attribute Missing
* */
public class CSVFileInvalidException extends InvalidException{

	/*
	    * Method Default Constructor with Error Message
	    * */
	public CSVFileInvalidException(){
		super("Field Missing");
	}
	
	/*
	    * Method Parameterized Constructor with Error Message
	    * @param error message
	    * */
	public CSVFileInvalidException(String msg){
		super(msg);
	}
	
	/*
	    * Method Parameterized Constructor with Error Message and Throwable object
	    * @param error message and throwable error object
	    * */
	public CSVFileInvalidException(String msg, Throwable error) {
		super(msg, error);
	}
}
