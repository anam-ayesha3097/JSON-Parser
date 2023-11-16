package csv2json;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
//---------------------------------------------------------------
//Assignment 2 - PartII
//© Anam Ayesha Shaikh
//Written by: Anam Ayesha Shaikh - 40205690
//Log Class - Creating and displaying log messages in default 
//CSV2JSON_logs.log file
//---------------------------------------------------------------

/*
* Creating and maintaining Log Files by default CSV2JSON_logs
* */
public class Logs {

	Logger log = Logger.getLogger("CSV2JSON tool logs");
	FileHandler fh;  
	SimpleFormatter formatter = new SimpleFormatter(); 
	
	Logs(){
		//default log file
		try {
			fh = new FileHandler("CSV2JSON_logs.log");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	Logs(String logFile){
		//User Defined Log File
		try {
			fh = new FileHandler(logFile+".log");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	    * Method to add log messages in logFile
	    * @param message
	    * */
	public void addLogMessage(String msg) {
		log.setUseParentHandlers(false);
		log.addHandler(fh);
		fh.setFormatter(formatter);
		log.info(msg);
	}
	
	/*
	    * Method to close log file fh
	    * */
	public void closeFile() {
		fh.close();
	}
}
