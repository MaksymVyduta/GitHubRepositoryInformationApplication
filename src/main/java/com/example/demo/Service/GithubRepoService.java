package com.example.demo.Service;

import com.example.demo.DTO.BranchDTO;
import com.example.demo.DTO.CommitDTO;
import com.example.demo.DTO.NonForkedRepositoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GithubRepoService {
    @Value("${github.api.base-url}")
    private String GITHUB_API_BASE_URL;

    @Value("${github.api.token}")
    private String TOKEN;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<NonForkedRepositoryDTO> getNonForkedRepositories(String username) {
        List<NonForkedRepositoryDTO> nonForkedRepositories = new ArrayList<>();

        try {
            String url = GITHUB_API_BASE_URL + "/users/" + username + "/repos";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + TOKEN);

            HttpEntity<String> entity = new HttpEntity<>("", headers);
            ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);
            List<String> repoNames = new ArrayList<>();

            for (Map<String, Object> repo : response.getBody()) {
                if (!(Boolean) repo.get("fork")) {
                    repoNames.add((String) repo.get("full_name"));
                }
            }

            String branchesUrl = GITHUB_API_BASE_URL + "/repos/{owner}/{repo}/branches";

            List<ResponseEntity<Map[]>> branchesResponses = repoNames.stream()
                    .map(repoName -> restTemplate.exchange(
                            branchesUrl.replace("{owner}/{repo}", repoName), HttpMethod.GET, entity, Map[].class))
                    .collect(Collectors.toList());

            int responseIndex = 0;
            for (Map<String, Object> repo : response.getBody()) {
                if (!(Boolean) repo.get("fork")) {
                    String fullName = (String) repo.get("full_name");
                    String ownerLogin = (String) ((Map<String, Object>) repo.get("owner")).get("login");

                    NonForkedRepositoryDTO modifiedRepo = new NonForkedRepositoryDTO(fullName, ownerLogin);

                    ResponseEntity<Map[]> branchesResponse = branchesResponses.get(responseIndex);
                    responseIndex++;

                    for (Map<String, Object> branchInfo : branchesResponse.getBody()) {
                        String branchName = (String) branchInfo.get("name");
                        BranchDTO branch = new BranchDTO();
                        branch.setName(branchName);

                        Map<String, Object> commitInfo = (Map<String, Object>) branchInfo.get("commit");
                        String commitUrl = (String) commitInfo.get("url");
                        String commitSha = (String) commitInfo.get("sha");

                        CommitDTO commit = new CommitDTO();
                        commit.setSha(commitSha);
                        commit.setUrl(commitUrl);

                        ResponseEntity<Map> commitDetailsResponse = restTemplate.exchange(
                                commitUrl, HttpMethod.GET, entity, Map.class);
                        Map<String, Object> commitDetails = commitDetailsResponse.getBody();
                        Map<String, Object> commitData = (Map<String, Object>) commitDetails.get("commit");
                        Map<String, Object> authorInfo = (Map<String, Object>) commitData.get("author");
                        String commitDate = (String) authorInfo.get("date");
                        commit.setDate(commitDate);
                        branch.addCommit(commit);

                        modifiedRepo.addBranch(branch);
                    }

                    nonForkedRepositories.add(modifiedRepo);
                }
            }

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", ex);
        }

        return nonForkedRepositories;
    }
}
