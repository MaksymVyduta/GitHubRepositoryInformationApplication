package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
@RestController
class GithubRepoController {

	private final String GITHUB_API_BASE_URL = "https://api.github.com";
	private final String TOKEN = "";

	@GetMapping("/repositories/{username}")
	public ResponseEntity<Object> listRepositories(
			@PathVariable String username,
			@RequestHeader("Accept") String acceptHeader
	) {
		try {
			String url = GITHUB_API_BASE_URL + "/users/" + username + "/repos";
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + TOKEN);

			HttpEntity<String> entity = new HttpEntity<>("", headers);
			ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map[].class);

			List<Map<String, Object>> nonForkRepositories = new ArrayList<>();
			for (Map<String, Object> repo : response.getBody()) {
				if (!(Boolean) repo.get("fork")) {
					nonForkRepositories.add(repo);
				}
			}


			if (acceptHeader.equals("application/xml")) {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("status", 406, "Message", "Not Acceptable"));
			} else {
				return ResponseEntity.ok(nonForkRepositories);
			}
		} catch (HttpClientErrorException.NotFound ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", 404, "Message", "User not found"));
		}
	}
}