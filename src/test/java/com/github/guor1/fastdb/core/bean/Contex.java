package com.github.guor1.fastdb.core.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * wdyq_contex
 * 
 * @author guor
 * 
 */
@Entity
@Table(name = "wdyq_contex")
public class Contex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7980715878136339361L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "host", nullable = false, length = 100)
	private String host;

	@Column(name = "doctype", nullable = true, length = 10)
	private String doctype;

	@Column(name = "template", nullable = false, unique = true, length = 255)
	private String template;

	/**
	 * i:content c:page content t:article r:<div>(.*)</div>
	 */
	@Column(name = "expression", nullable = false, length = 255)
	private String expression;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@Version
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
