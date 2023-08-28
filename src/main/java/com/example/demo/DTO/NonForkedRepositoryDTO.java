package com.example.demo.DTO;

import java.util.ArrayList;
import java.util.List;

public class NonForkedRepositoryDTO {
    private String full_name;
    private String owner_login;

    private List<BranchDTO> branches;

    public NonForkedRepositoryDTO(String full_name, String owner_login) {
        this.full_name = full_name;
        this.owner_login = owner_login;
        this.branches = new ArrayList<>();
    }

    public void addBranch(BranchDTO branch) {
        this.branches.add(branch);
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getOwner_login() {
        return owner_login;
    }

    public void setOwner_login(String owner_login) {
        this.owner_login = owner_login;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }
}
