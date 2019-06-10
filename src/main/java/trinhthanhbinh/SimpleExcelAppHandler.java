package trinhthanhbinh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author SaoThienHat
 *
 */
public class SimpleExcelAppHandler {
	private final String DEPENDENCY_DETECT_STR = "Circular dependency";
	private List<String> errors;
	private Map<String, String> cellMap;

	public Map<String, String> getCellMap() {
		return cellMap;
	}

	public void setCellMap(Map<String, String> cellMap) {
		this.cellMap = cellMap;
	}
	
	public void putCell(String cellName, String cellContent) {
		this.cellMap.put(cellName, cellContent);
	}

	public SimpleExcelAppHandler() {
		this.cellMap = new HashMap<String, String>();
		this.errors = new ArrayList<String>();
	}

	/**
	 * @param mathCell: cell which contain math formula content such as "A1 5 * B2 +"
	 * @param normalCellMap: map of cell which contains cell name as key and value is a number
	 * @return an evaluated cell
	 */
	public TikiCell evaluateCell(TikiCell mathCell, Map<String, String> normalCellMap) {
		TikiCell cell = new TikiCell();
		try {
			String mathContent = mathCell.getContent();
			String[] tokens = generateTokens(mathContent, normalCellMap);
			if(tokens.length == 0) {
				System.err.println("---> " + mathCell.getName() + " NOT exited");
				return cell;
			}
			
			int realValue = evaluateRPN(tokens);
			System.out.println("===> value: " + realValue);
			cell = new TikiCell(mathCell.getName(), String.valueOf(realValue));			
		} catch (ArithmeticException e) {
			this.getErrors().add("ArithmeticException: / by zero");
		}
		return cell;
	}
	
	
	
	/**
	 * @param mathContent: content of cell which contain math formula content such as "A1 5 * B2 +"
	 * @param normalCellMap
	 * @return 
	 */
	private String[] generateTokens(String mathContent, Map<String, String> normalCellMap) {
		String[] splittedTexts = mathContent.split(" ");
		List<String> list = new ArrayList<String>();
		for (String splittedText : splittedTexts) {
			String existedValue = normalCellMap.get(splittedText.trim());
			String expectedValue = (existedValue != null) ? existedValue : splittedText;
			list.add(expectedValue);
		}
		String[] array = list.toArray(new String[0]);
		return array;
	}

	/**
	 * evaluate reverse polish notation
	 * @param tokens such as ["2", "1", "+", "3", "*"]
	 * @return
	 */
	private int evaluateRPN(String[] tokens) throws ArithmeticException {
		int returnValue = 0;
		String operators = "+-*/";
		Stack<String> stack = new Stack<String>();

		for (String token : tokens) {
			if (!operators.contains(token)) {
				stack.push(token);
			} else {
				int valueOne = Integer.valueOf(stack.pop());
				int valueTwo = Integer.valueOf(stack.pop());
				int index = operators.indexOf(token);
				switch (index) {
				case 0:
					stack.push(String.valueOf(valueOne + valueTwo));
					break;
				case 1:
					stack.push(String.valueOf(valueTwo - valueOne));
					break;
				case 2:
					stack.push(String.valueOf(valueOne * valueTwo));
					break;
				case 3:
					stack.push(String.valueOf(valueTwo / valueOne));
					break;
				}
			}
		}

		returnValue = Integer.valueOf(stack.pop());
		return returnValue;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Map<String, String> finalCellMap = this.getCellMap();
//		List<String> sortedCellByKey = new ArrayList<String>(this.getCellMap().keySet());
//		Collections.sort(sortedCellByKey);
    	for (String name : finalCellMap.keySet()) {
    		String value = finalCellMap.get(name).toString();  
    		sb.append(name + "\n");
    		sb.append(value + "\n");
    	}
    	return sb.toString();
	}
	
	/**
	 * find Circular Dependencies
	 * @return
	 */
	public String findCircularDependencies(Map<String, String> mathCellMap) {
		String result = "";
		boolean isCircularDependencies = false;
		CellGraph graph = new CellGraph(mathCellMap.size());
		
		// Mapping each cell with unique integer value
		final int NOT_FOUND = -1;
		int position = 0;
		Map<String, Integer> cellMap = new HashMap<String, Integer>();
		for (String name : mathCellMap.keySet()) {
			cellMap.put(name, position++);
		}
		
		if(cellMap != null && cellMap.size() < 2) {
			isCircularDependencies = false;
		} else {
			
			for (String cellName : mathCellMap.keySet()) {
				String rawContent = mathCellMap.get(cellName).toString();
				
				String[] splittedTexts = rawContent.split(" ");
				for (String splittedText : splittedTexts) {
					if(AppHelper.isContainLetter(splittedText)) {
						Integer cellPosition = cellMap.get(splittedText);
						Integer currCellPosition = cellMap.get(cellName);
						
						boolean isExistedEdge = graph.isExistedEdge(currCellPosition.intValue(), cellPosition.intValue());
						if(isExistedEdge) {
							isCircularDependencies = true;
							result = DEPENDENCY_DETECT_STR + " between " + cellName + " and " + splittedText + " detected";
						} else {
							if(cellPosition != null && currCellPosition != null) {
								graph.addEdge(currCellPosition.intValue(), cellPosition.intValue()); 
							}
						}
			
					}
				}
				
			}
			
		}

		if(result != null && !result.isEmpty()) this.getErrors().add(result);
		return result;
	}
	

}
