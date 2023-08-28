package com.example.demo.Service;

import com.example.demo.DTO.NonForkedRepositoryDTO;

import java.util.List;

public class JsonPrinter {


    public static void printNonForkedRepositories(List<NonForkedRepositoryDTO> repositories) {
        repositories.forEach(repository -> {
            System.out.println("Repository: " + repository.getFull_name() + " (Owner: " + repository.getOwner_login() + ")");
            repository.getBranches().forEach(branch -> {
                System.out.println("  Branch: " + branch.getName());
                branch.getCommitsList().stream()
                        .reduce((first, second) -> second) // Get the last commit
                        .ifPresent(lastCommit -> {
                            System.out.println("    Last Commit SHA: " + lastCommit.getSha());
                            System.out.println("    Last Commit Date: " + lastCommit.getDate());
                        });
            });
            System.out.println();
        });
    }
}