package com.example.demo;

import com.example.demo.DTO.PayloadDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${github.api.token}")
	private String githubToken;

	@Test
	void testMyRepositories() throws Exception {
		PayloadDTO requestPayload = new PayloadDTO();
		requestPayload.setUsername("MaksymVyduta");

		String jsonPayload = objectMapper.writeValueAsString(requestPayload);

		mockMvc.perform(MockMvcRequestBuilders.post("/")
						.header("Authorization", "Bearer " + githubToken)
						.header("Accept", "application/json")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonPayload))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


/*	@Test
	void testOctokitListRepositories() throws Exception {
		PayloadDTO requestPayload = new PayloadDTO();
		requestPayload.setUsername("octokit");

		String jsonPayload = objectMapper.writeValueAsString(requestPayload);

		mockMvc.perform(MockMvcRequestBuilders.post("/")
						.header("Authorization", "Bearer " + githubToken)
						.header("Accept", "application/json")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonPayload))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}*/
}
