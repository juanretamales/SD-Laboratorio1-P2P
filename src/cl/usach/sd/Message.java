package cl.usach.sd;

/**
 * Clase la cual vamos a utilizar para enviar datos de un Peer a otro
 */
public class Message {
	private String text;
	private Integer type;

	public Message(String string) {
		this.setText(string);
	}
	public Message(String text, Integer type) {
		this.setText(text);
		this.setType(type);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
