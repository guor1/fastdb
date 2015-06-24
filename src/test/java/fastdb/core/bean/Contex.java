package fastdb.core.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * wdyq_contex
 * 
 * @author guor
 * 
 */
public class Contex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7980715878136339361L;

	private long id;

	private String host;

	private String doctype;

	private String template;

	/**
	 * i:content c:page content t:article r:<div>(.*)</div>
	 */
	private String expression;

	private Timestamp lastoptime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Timestamp getLastoptime() {
		return lastoptime;
	}

	public void setLastoptime(Timestamp lastoptime) {
		this.lastoptime = lastoptime;
	}

	@Override
	public String toString() {
		return "Contex [id=" + id + ", host=" + host + ", doctype=" + doctype + ", template=" + template + ", expression=" + expression + "]";
	}
}
