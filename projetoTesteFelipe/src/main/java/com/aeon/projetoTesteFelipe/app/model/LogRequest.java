package com.aeon.projetoTesteFelipe.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "logRequests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5163441225603971787L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull(message = "Data é uma informação obrigatória.")
    @Column(name = "data_Request", nullable = false)
	private Date data;
	
	@NotBlank(message = "Ip é uma informação obrigatória.")
	@Column(name = "ip", length = 15)
	private String ip;
	
	@NotBlank(message = "Request é uma informação obrigatória.")
	@Column(name = "request", length = 200)
	private String request;
	
	@NotBlank(message = "Status é uma informação obrigatória.")
	@Column(name = "status", length = 3)
	private String status;
	
	@NotBlank(message = "UserAgent é uma informação obrigatória.")
	@Column(name = "userAgent", length = 1000)
	private String userAgent;

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}
