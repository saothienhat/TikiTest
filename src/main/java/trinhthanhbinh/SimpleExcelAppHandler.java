package trinhthanhbinh;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SaoThienHat
 *
 */
public class SimpleExcelAppHandler {
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

	public TikiCell evaluateCell(TikiCell mathCell, Map<String, String> cellMap2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

}
