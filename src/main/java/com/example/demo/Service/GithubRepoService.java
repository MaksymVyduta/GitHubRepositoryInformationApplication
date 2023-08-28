package com.example.demo.Service;

import com.example.demo.DTO.BranchDTO;
import com.example.demo.DTO.CommitDTO;
import com.example.demo.DTO.NonForkedRepositoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GithubRepoService {
    @Value("${github.api.base-url}")
    private String GITHUB_API_BASE_URL;

    @Value("${github.api.token}")
    private String TOKEN;
    public List<NonForkedRepositoryDTO> getNonForkedRepositories(String username) {
        List<NonForkedRepositoryDTO> nonForkedRepositories = new ArrayList<>();

        try {
            String url = GITHUB_API_BASE_URL + "/users/" + username + "/repos";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + TOKEN);

            HttpEntity<String> entity = new HttpEntity<>("", headers);
            ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);

            for (Map<String, Object> repo : response.getBody()) {
                if (!(Boolean) repo.get("fork")) {
                    String fullName = (String) repo.get("full_name");
                    String ownerLogin = (String) ((Map<String, Object>) repo.get("owner")).get("login");
                    String branchesUrl = ((String) repo.get("branches_url")).replaceAll("\\{.*}", "");

                    NonForkedRepositoryDTO modifiedRepo = new NonForkedRepositoryDTO(fullName, ownerLogin);

                    ResponseEntity<Map[]> branchesResponse = restTemplate.exchange(
                            branchesUrl, HttpMethod.GET, entity, Map[].class);

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
                        Map<String, Object> commitDetails = (Map<String, Object>) restTemplate.getForObject(commitUrl, Map.class);
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
            // Handle user not found exception
        }

        return nonForkedRepositories;
    }
}
