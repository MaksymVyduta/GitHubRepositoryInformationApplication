package com.example.demo.Controller;

import com.example.demo.DTO.NonForkedRepositoryDTO;
import com.example.demo.DTO.PayloadDTO;
import com.example.demo.Service.GithubRepoService;
import com.example.demo.Service.JsonPrinter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GithubRepoController {
    private final GithubRepoService githubRepoService;
    private final JsonPrinter Printer;

    public GithubRepoController(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
        this.Printer = new JsonPrinter();
    }

    @PostMapping("/repositories")
    public ResponseEntity<Object> listRepositories(
            @RequestBody PayloadDTO requestPayload,
            @RequestHeader("Accept") String acceptHeader
    ) {
        List<NonForkedRepositoryDTO> nonForkedRepositories = githubRepoService.getNonForkedRepositories(requestPayload.getUsername());

        if (acceptHeader.equals("application/xml")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("status", 406, "Message", "Not Acceptable"));
        } else {
            Printer.printNonForkedRepositories(nonForkedRepositories);
            return ResponseEntity.ok().body(nonForkedRepositories);
        }
    }
}
