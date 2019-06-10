package trinhthanhbinh;

/**
 * @author Trinh Thanh Binh
 *
 */
public class TikiCell {
	private String name;
	private String content;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public TikiCell() {}
	
	public TikiCell(String name, String content) {
		this.name = name;
		this.content = content;
	}
	
	
	

}
