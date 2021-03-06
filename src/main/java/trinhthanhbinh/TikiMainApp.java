package trinhthanhbinh;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Trinh Thanh Binh (trinhbinh87@gmail.com)
 *
 */
public class TikiMainApp {

	private static final Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		SimpleExcelAppHandler cellHandler = new SimpleExcelAppHandler();
		Map<String, String> cellMap = new HashMap<String, String>();
		Map<String, String> mathCellMap = new HashMap<String, String>();

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int count = 0;
        TikiCell cell = new TikiCell();
        for (int nItr = 0; nItr < 2*n; nItr++) {
            String str = scanner.nextLine();

            if(count == 0) {
            	cell.setName(str.trim());
            	count++;
            }
            else if(count == 1) {
            	cell.setContent(str.trim());
            	
            	if(!AppHelper.isContainLetter(str)) 
            		cellMap.put(cell.getName(), cell.getContent());
            	else 
            		mathCellMap.put(cell.getName(), cell.getContent());
            	
            	count = 0;
            	cell = new TikiCell();
            }

        }

        
        /*
         * Pre-process cells to detect errors
         */
        cellHandler.preProcess(mathCellMap);
        
        /**
         * Print the result
         */
        System.out.println("\nOutput: ");
        System.out.println("---------------------------------------");
        List<String> cellHanldErrors = cellHandler.getErrors();
        if(cellHanldErrors != null && cellHanldErrors.size() > 0) {
        	for (String errorMsg : cellHanldErrors) {
				System.out.println(errorMsg);
			}
        } else {
            // For normal cell which contain number only
            for (String name : cellMap.keySet()) {
                String value = cellMap.get(name).toString();  
                cellHandler.putCell(name, value);
    		}
            // For normal cell which contain Math Formular content
            for (String name : mathCellMap.keySet()) {
                String value = mathCellMap.get(name).toString();  
                TikiCell mathCell = new TikiCell(name, value);
                TikiCell evaluatedCell = cellHandler.evaluateCell(mathCell, cellMap);
                cellHandler.putCell(name, evaluatedCell.getContent());             
    		}
            
        	System.out.println(cellHandler.toString());        	
        }

        scanner.close();
	}
	
	

}
