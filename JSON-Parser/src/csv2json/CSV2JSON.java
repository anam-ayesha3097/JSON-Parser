package csv2json;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileReader;
import exception.InvalidException;
import exception.CSVFileInvalidException;
import exception.CSVDataMissing;

//---------------------------------------------------------------
//Assignment 2 - PartII
//© Anam Ayesha Shaikh
//Written by: Anam Ayesha Shaikh - 40205690
//CSV2JSON Class - Convert CSV or TXT files to JSON
//---------------------------------------------------------------

/*
* Tool to convert .csv and .txt files to .json
* */


public class CSV2JSON {
	
	static Logs log = new Logs(); //Log Class
	static String fileLoc = "..\\Assignment 2 - Part II\\files\\"; //Files Location(Relative Path)
	
	/*
	    * Method to check if the file exists
	    * @param array of the input files
	    * @exception throws User Defined InvalidException
	    * */
	public static void fileExists(String[] inputFiles )throws InvalidException  {
		File f;
		boolean fileExistsInvalid = false;
		for(int i =0; i < inputFiles.length; i++) {
			f = new File(fileLoc+inputFiles[i]);
			if(!f.exists()) {
				System.err.println("File "+inputFiles[i] +" does not exists to read");
				log.addLogMessage("FileNotFound: "+inputFiles[i] +" does not exists");
				fileExistsInvalid = true;
			}
			f = null;
		}
		
		if(fileExistsInvalid) 
			throw new InvalidException("FileNotFoundException: File does not Exists");
	}
	
	/*
	    * Method to preprocess the rows
	    * @param token object
	    * @return preprocessed row
	    * */
	public static String preprocess(StringTokenizer tokens) {
		int inverted = 0;
		boolean open = false;
		String data = "";
		/*Manually Remove additional ',' from the rows*/
		if(tokens == null)
			return "";
				while(tokens.hasMoreTokens()) {
					String s = tokens.nextToken(); 
					if(s.contains("\""))
					{
						++inverted;
						if(open == false)
						{
							s = s.substring(1, s.length());
							data += s +"";
						}
						else
						{
							s = ":"+s.substring(1, s.length() - 1);
							data += s +",";
						}
						
						open = true;
						
						if(inverted == 2)
						{
							open = false;
							inverted = 0;
						}
					}
					else
					{
						if(inverted == 0)
							data += s + ",";
						else
						{
							s = s.trim();
							data += ":"+s + " ";
						}
					}
				}
		
		return data;
	}
	
	/*
	    * Method to write to JSON file
	    * @param colName array rows printWriter object and a begin flag
	    * */
	public static void printJSONFile(String[] cols, StringTokenizer rows, PrintWriter pw, int begin) {
		String colsName = "", rowValue = "", rowToken = "";
		int i = 0;
		
		if(begin == 0)
		 {
			pw.println("[");
			begin = 1;
		 }
		pw.println("\t {");
		while(rows.hasMoreTokens())
		 {
			colsName = "\""+cols[i]+"\""+":";
			rowToken = rows.nextToken();	
			rowToken = rowToken.replace("***", "");
			boolean isNumeric = rowToken.matches("-?\\d+(\\.\\d+)?"); //Regex for checking numerical values and float
			if(isNumeric)
				rowValue = " "+rowToken+",";
			else
				rowValue = " \""+rowToken.replace(":", ",")+"\",";
			pw.print("\t\t"+colsName);
			pw.print(rowValue);
			pw.println();
			i++;
			
			//If data is missing
			if(rowToken.contains("***"))
			{
				colsName = "\""+cols[i]+"\""+":";
				pw.print("\t\t"+colsName);
				rowValue = "";
				pw.print(rowValue);
				pw.println();
				i++;
			}
		 }
		pw.println("\t},");
	}
	
	/*
	    * Method to read the json file using BufferedReader
	    * @param fileName
	    * @exception throws IOException
	    * */
	public static void readJSONDisplay(String fileName)throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileLoc+fileName));
		String data = "";
		while((data = br.readLine()) != null) {
			System.out.println(data);
		}
		br.close();
	}
	
	/*
	    * Method to convert .csv or .txt files to JSON
	    * @param file name
	    * @exception throws User Defined InvalidException and IOException
	    * here, we are throwing CSVDataMissing and CSVFileInvalidException
	    * */
	public static void convertFile2JSON(String inputFile)throws IOException, InvalidException{
		
		Scanner sc = new Scanner(new FileInputStream(fileLoc+inputFile));
		int indexOfDot = inputFile.indexOf(".");
		String fName = inputFile.substring(0, indexOfDot);
		PrintWriter pw = null;
		
		
		StringTokenizer colName;
		StringTokenizer rows;
		int lines = 0;
		
		//First Line of the File - Column Names		
		String colCheck = sc.nextLine();
		colName = new StringTokenizer(colCheck, ",\t");
		if(colCheck.contains(",,"))
			colCheck = colCheck.replaceAll(",,", ",***,,");
		int totalCols = colName.countTokens();
		String cols[] = new String[colName.countTokens()];
		
		int i = 0;
		while(colName.hasMoreTokens()) {
			cols[i] = colName.nextToken().toString().replaceAll("[[^a-zA-Z0-9' ]]", "");;
			i++;
		}
		colName = null;
		
		String data = "";
		int begin = 0;
		
		//To check if the number of Cols are same as the number of rows
		String rowCheck = "";
		while(sc.hasNextLine()) {
			++lines;
			rowCheck = sc.nextLine();
			
			if(rowCheck.contains(",,"))
				rowCheck = rowCheck.replaceAll(",,", "***,,");
			
			rows = new StringTokenizer(rowCheck, ",\t");
			
			data = preprocess(rows);
			
		
			rows = new StringTokenizer(data, ",\t");
		
		//If the data is complete with complete rows and cols of same length
		if(rows.countTokens() == totalCols) {
			if(begin == 0)
				pw = new PrintWriter(new FileOutputStream(fileLoc+fName+".json"));
			
			printJSONFile(cols, rows, pw, begin);
			begin = 1;
		}
		//If an attribute is missing
		else if(rows.countTokens() > totalCols)
		{
			sc.close();
			if(pw != null)
				pw.close();
			int actualCols = rows.countTokens() - totalCols;
			log.addLogMessage(inputFile +"is Invalid\nMissing Fields: "+totalCols +" detected, "+actualCols +" missing\n" +lines +": "+colCheck +"cannot be converted to JSON: missing data");
			throw new CSVFileInvalidException(inputFile+" is invalid: field is missing\n File is not converted to JSON");
			
		}
		//If a row data missing
		else
		{
			try {
				if(begin == 0)
					pw = new PrintWriter(new FileOutputStream(fileLoc+fName+".json"));
				printJSONFile(cols, rows, pw, begin);
				begin = 1;
				log.addLogMessage(inputFile +": "+lines +"\n"+rowCheck +"Missing");
				
				throw new CSVDataMissing(inputFile +": "+lines +" cannot be converted to JSON: missing data");
			}
			catch(CSVDataMissing e)
			{
				System.out.println(e);
			}
		}
		data = "";
		}
		if(begin == 1)
			pw.println("]");
		begin = 0;
		sc.close();
		pw.close();
	}

	/*
	    * Method Main method to convert CSV2JSON
	    * @param array of the input files
	    * @exception throws User Defined InvalidException and IOException which is handled in the main method
	    * */
	public static void processFilesForValidation(String[] inputFiles) throws IOException, InvalidException {
		fileExists(inputFiles);
		System.err.println("Validated");
		log.addLogMessage("Validated");
		for(int i = 0; i < inputFiles.length; i++)
		{
			convertFile2JSON(inputFiles[i]);
			System.err.println(inputFiles[i] +" Successfuly Converted to JSON");
			log.addLogMessage(inputFiles[i] +" Successfuly Converted to JSON");
		}
		
	}

	public static void main(String[] args)  {
		int choice = 0, chance = 1;
		System.out.println("CSV2JSON Converter Tool\n");
		System.out.println("1. CSV2JSON Files\n2. User CSV files\n3. User JSON file name\n4. Exit");
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			choice = Integer.parseInt(br.readLine());
			while(true) {
			try {
				switch(choice) {
				case 1: {
					try {
						//Converting .csv or .txt default files
						String inputFiles[] = {"Car Rental Record.csv","Car Maintenance Record.txt"};
						System.out.println("CSV2JSON for files : \n" +inputFiles[0] +" and " +inputFiles[1]);
						System.err.println("Validating Files...");
						log.addLogMessage("CSV2JSON for files : \n" +inputFiles[0] +" and " +inputFiles[1]);
						log.addLogMessage("Validating Files...");
						processFilesForValidation(inputFiles);
					}
					catch(InvalidException e) {
						System.out.println(e);
					}
					catch(IOException e) {
						System.out.println(e);
					}
					break;
				}
				
				case 2: {
					try {
						//Converting User Files
						System.out.println("Enter the name of the files you wish to convert to JSON");
						String f1 = br.readLine();
						String f2 = br.readLine();
						System.out.println("CSV2JSON for files : \n" +f1 +" and " +f2);
						System.err.println("Validating Files...");
						String inputFiles[] = {f1, f2};
						log.addLogMessage("CSV2JSON for files : \n" +f1 +" and " +f2);
						log.addLogMessage("Validating Files...");
						processFilesForValidation(inputFiles);
					}
					catch(InvalidException e) {
						System.err.println(e);
					}
					catch(IOException e) {
						System.err.println(e);
					}
					break;
				}
				case 3: {
					System.out.println("Enter the name of the JSON file you want to display");
					String inputFiles[] = new String[1];
					//Giving user two chance to enter file names
					while(chance <= 2) {
						inputFiles[0] = br.readLine();
						try{
							fileExists(inputFiles);
							readJSONDisplay(inputFiles[0]);
							chance++;
							break;
						}
						catch(InvalidException e) {
							System.err.println(e);
							chance++;
							if(chance <= 2)
								System.out.println("Enter the name of the JSON file you want to display");
						}
						catch(IOException e) {
							System.err.println(e);
						}
					}
					break;
				}
				case 4: {
					System.exit(0);
					break;
				}
				default:{
					System.err.println("Enter a valid choice");
					break;
					}
				}
			//To ask user inputs until user exits.	
			System.out.println("CSV2JSON Converter Tool\n");
			System.out.println("1. CSV2JSON Files\n2. User CSV files\n3. User JSON file name\n4. Exit");
			br = new BufferedReader(new InputStreamReader(System.in));
			choice = Integer.parseInt(br.readLine());
			chance = 1;
			}
			catch(Exception e) {
				System.out.println("CSV2JSON Converter Tool\n");
				System.out.println("1. CSV2JSON Files\n2. User CSV files\n3. User JSON file name\n4. Exit");
				br = new BufferedReader(new InputStreamReader(System.in));
				choice = Integer.parseInt(br.readLine());
				chance = 1;
			}
		 }
		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

}
