package com.example.demo.Controller;

import com.example.demo.DTO.NonForkedRepositoryDTO;
import com.example.demo.DTO.PayloadDTO;
import com.example.demo.Service.GithubRepoService;
import com.example.demo.Service.HtmlBuilder;
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
    private final HtmlBuilder htmlBuilder;

    public GithubRepoController(GithubRepoService githubRepoService,HtmlBuilder htmlBuilder) {
        this.githubRepoService = githubRepoService;
        this.Printer = new JsonPrinter();
        this.htmlBuilder = htmlBuilder;
    }

    @PostMapping("/")
    public ResponseEntity<Object> listRepositories(
            @RequestBody PayloadDTO requestPayload,
            @RequestHeader("Accept") String acceptHeader
    ) {
        List<NonForkedRepositoryDTO> nonForkedRepositories = githubRepoService.getNonForkedRepositories(requestPayload.getUsername());

        if (acceptHeader.equals("application/xml")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("status", 406, "Message", "Not Acceptable"));
        } else {
            String responseHtml = htmlBuilder.buildHtmlResponse(nonForkedRepositories);
            Printer.printNonForkedRepositories(nonForkedRepositories);
            return ResponseEntity.ok().body(responseHtml);
        }
    }
}
