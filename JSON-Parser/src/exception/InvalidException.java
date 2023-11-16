package exception;
//---------------------------------------------------------------
//Assignment 2 - PartII
//© Anam Ayesha Shaikh
//Written by: Anam Ayesha Shaikh - 40205690
//InvalidException Class - User Defined base Exception Class
//						   Child class of parent Exception
//---------------------------------------------------------------

/*
* User Defined Exception Class for handling CSV2JSON tool(CSV2JSON class) Errors
* */
public class InvalidException extends Exception{

	/*
	    * Method Default Constructor with Error Message
	    * */
	public InvalidException(){
		System.err.println("Error: Input row cannot be parsed due to missing information");
	}
	
	/*
	    * Method Parameterized Constructor with Error Message
	    * @param error message
	    * */
	public InvalidException(String msg){
		super(msg);
	}
	
	/*
	    * Method Parameterized Constructor with Error Message and Throwable object
	    * @param error message and throwable error object
	    * */
	public InvalidException(String msg, Throwable error){
		super(msg, error);
	}
}
