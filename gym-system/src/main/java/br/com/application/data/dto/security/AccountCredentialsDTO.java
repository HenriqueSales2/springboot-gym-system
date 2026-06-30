package br.com.application.data.dto.security;

import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fullname;
    private String username;
    private String password;

    public AccountCredentialsDTO() {}

    public AccountCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AccountCredentialsDTO(String fullname, String username, String password) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredentialsDTO that = (AccountCredentialsDTO) o;
        return Objects.equals(fullname, that.fullname) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullname, username, password);
    }
}