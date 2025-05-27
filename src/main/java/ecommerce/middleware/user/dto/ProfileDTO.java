package ecommerce.middleware.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProfileDTO {
	private Long id;
	private String username;
	private String email;
	private String phone;
	private LocalDate birthday;
	private String cpf;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private Long defaultAddressId;
	private Long authUserId; 

	public ProfileDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate localDate) {
		this.birthday = localDate;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime localDateTime) {
		this.createDate = localDateTime;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime localDateTime) {
		this.updateDate = localDateTime;
	}

	public Long getDefaultAddressId() {
		return defaultAddressId;
	}

	public void setDefaultAddressId(Long defaultAddressId) {
		this.defaultAddressId = defaultAddressId;
	}

	public Long getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(Long authUserId) {
		this.authUserId = authUserId;
	}
	
	
}
