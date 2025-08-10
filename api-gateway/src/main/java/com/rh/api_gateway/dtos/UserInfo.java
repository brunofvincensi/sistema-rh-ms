package com.rh.api_gateway.dtos;

public class UserInfo {

    private String id;
    private String role;
    private String colaboradorId;
    private boolean ativo;

    public UserInfo() {}

    public UserInfo(String id, String role, String colaboradorId, boolean ativo) {
        this.id = id;
        this.role = role;
        this.colaboradorId = colaboradorId;
        this.ativo = ativo;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getColaboradorId() { return colaboradorId; }
    public void setColaboradorId(String colaboradorId) { this.colaboradorId = colaboradorId; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

}
