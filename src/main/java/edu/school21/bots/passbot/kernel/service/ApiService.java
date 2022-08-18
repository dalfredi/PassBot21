package edu.school21.bots.passbot.kernel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.bots.passbot.dal.models.User;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class ApiService {
	@Value("${user.oauth.clientId}")
	private String credentialId;
	@Value("${user.oauth.clientSecret}")
	private String clientSecret;

	public User requestAccessToken(String name) throws IOException {
		ResponseEntity<String> response;
		User user = new User();

		RestTemplate restTemplate = new RestTemplate();
		String clientId = credentialId + ":" + clientSecret;
		String encodedCredentials = new String(Base64.encodeBase64(clientId.getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);
		String access_token_url = "https://api.intra.42.fr/oauth/token";
		access_token_url += "?code=" + clientId;
		access_token_url += "&grant_type=client_credentials";

		response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.getBody());
		String token = node.path("access_token").asText();

		String url = "https://api.intra.42.fr/v2/users?filter[login]=" + name;
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<>(headers1);

		ResponseEntity<String> peers = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		JsonNode node1 = mapper.readTree(peers.getBody());
		Iterator<Map.Entry<String, JsonNode>> node2 = node1.path(0).fields();
		while (node2.hasNext()) {
			Map.Entry<String, JsonNode> tmp = node2.next();
			if (tmp.getKey().equals("login")) {
				user.setLogin(tmp.getValue().asText());
			}
			if (tmp.getKey().equals("staff?")) {
				user.setRole(tmp.getValue().asText().equals("false") ? "USER" : "ADMIN");
			}
//            System.out.println(tmp.getKey() + " : " + tmp.getValue());
		}
		return user;
	}
}
