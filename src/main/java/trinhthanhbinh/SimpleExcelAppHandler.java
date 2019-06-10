package trinhthanhbinh;

import java.util.ArrayList;
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
	}

	/**
	 * @param mathCell: cell which contain math formula content such as "A1 5 * B2 +"
	 * @param normalCellMap: map of cell which contains cell name as key and value is a number
	 * @return an evaluated cell
	 */
	public TikiCell evaluateCell(TikiCell mathCell, Map<String, String> normalCellMap) {
		String mathContent = mathCell.getContent();
		String[] tokens = generateTokens(mathContent, normalCellMap);
		int realValue = evaluateRPN(tokens);
		System.out.println("===> value: " + realValue);
		return new TikiCell(mathCell.getName(), String.valueOf(realValue));
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
	private int evaluateRPN(String[] tokens) {
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
	
	

}
