package com.example.demo.Service;

import com.example.demo.DTO.BranchDTO;
import com.example.demo.DTO.CommitDTO;
import com.example.demo.DTO.NonForkedRepositoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HtmlBuilder {

    public String buildHtmlResponse(List<NonForkedRepositoryDTO> repositories) {
        StringBuilder htmlBuilder = new StringBuilder();

        repositories.forEach(repository -> {
            htmlBuilder.append("<p>Repository: ").append(repository.getFull_name())
                    .append(" (Owner: ").append(repository.getOwner_login()).append(")</p>");

            repository.getBranches().forEach(branch -> {
                htmlBuilder.append("<p style='margin-left: 20px;'>Branch: ").append(branch.getName()).append("</p>");

                branch.getCommitsList().stream()
                        .reduce((first, second) -> second)
                        .ifPresent(lastCommit -> {
                            htmlBuilder.append("<p style='margin-left: 40px;'>Last Commit SHA: ").append(lastCommit.getSha()).append("</p>");
                            htmlBuilder.append("<p style='margin-left: 40px;'>Last Commit Date: ").append(lastCommit.getDate()).append("</p>");
                        });
            });
        });

        return htmlBuilder.toString();
    }
}
