package com.example.demo.DTO;


import java.util.ArrayList;
import java.util.List;

public class BranchDTO {

    private String name;
    private List<CommitDTO> commitsList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommitDTO> getCommitsList() {
        return commitsList;
    }

    public void addCommit(CommitDTO commit) {
        commitsList.add(commit);
    }
}