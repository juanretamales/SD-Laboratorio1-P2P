package cl.usach.sd;

/**
 * Clase la cual vamos a utilizar para enviar datos de un Peer a otro
 */
public class Message {
	private Integer text;
	private Integer type;

	public Message(Integer text) {
		this.setText(text);
	}
	public Message(Integer text, Integer type) {
		this.setText(text);
		this.setType(type);
	}

	public Integer getText() {
		return text;
	}

	public void setText(Integer text) {
		this.text = text;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
