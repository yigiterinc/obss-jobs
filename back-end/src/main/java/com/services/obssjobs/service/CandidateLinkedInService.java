package com.services.obssjobs.service;

import com.services.obssjobs.exception.LinkedInAccessTokenExpiredException;
import com.services.obssjobs.exception.NotFoundException;
import com.services.obssjobs.model.dto.CandidateProfileDto;
import com.services.obssjobs.model.entity.Application;
import com.services.obssjobs.model.entity.Blacklist;
import com.services.obssjobs.model.entity.CandidateLinkedIn;
import com.services.obssjobs.model.entity.LinkedInSkill;
import com.services.obssjobs.model.entity.solr.CandidateLinkedInSummary;
import com.services.obssjobs.repository.CandidateLinkedInRepository;
import com.services.obssjobs.service.solr.CandidateLinkedInSummaryService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CandidateLinkedInService {

    private static final String PROFILE_ENDPOINT = "https://api.linkedin.com/v2/me";
    private static final String IMAGE_ENDPOINT = "https://api.linkedin.com/v2/me?projection=" +
            "(id,profilePicture(displayImage~:playableStreams))&oauth2_access_token=";
    private static final String EMAIL_ENDPOINT = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    private static final String CLIENT_ID = "86uobfdmclzprd";
    private static final String CLIENT_SECRET = "Zqkh05AU1FqI1mZj";
    private static final String REDIRECT_URI = "http://localhost:8080";

    private CandidateLinkedInRepository candidateLinkedInRepository;
    private LinkedInSkillService linkedInSkillService;
    private BlacklistService blacklistService;
    private CandidateLinkedInSummaryService candidateLinkedInSummaryService;

    @Autowired
    public CandidateLinkedInService(CandidateLinkedInRepository candidateLinkedInRepository,
                                    LinkedInSkillService linkedInSkillService,
                                    BlacklistService blacklistService,
                                    CandidateLinkedInSummaryService candidateLinkedInSummaryService) {

        this.candidateLinkedInRepository = candidateLinkedInRepository;
        this.linkedInSkillService = linkedInSkillService;
        this.blacklistService = blacklistService;
        this.candidateLinkedInSummaryService = candidateLinkedInSummaryService;
    }

    public List<String> getSkills(String candidateId) throws NotFoundException {
        Set<LinkedInSkill> linkedInSkillSet = getCandidateLinkedInById(candidateId).getLinkedInSkillSet();
        return linkedInSkillSet.stream().map(LinkedInSkill::getSkill).collect(Collectors.toList());
    }

    public void saveSkills(String candidateId, List<String> skills) throws NotFoundException {
        CandidateLinkedIn candidateLinkedIn = getCandidateLinkedInById(candidateId);

        Set<LinkedInSkill> linkedInSkillSet = linkedInSkillService.getLinkedInSkillSetBySkills(skills);
        candidateLinkedIn.setLinkedInSkillSet(linkedInSkillSet);

        save(candidateLinkedIn);
    }

    public CandidateLinkedIn getCandidateLinkedInById(String candidateId) throws NotFoundException {
        return candidateLinkedInRepository.findById(candidateId).orElseThrow(NotFoundException::new);
    }

    private Optional<CandidateLinkedIn> findByEmail(String email) {
        return candidateLinkedInRepository.findByEmail(email);
    }

    public CandidateProfileDto getCandidateProfileDto(String id) {
        return candidateLinkedInRepository.getCandidateProfileDto(id);
    }

    public String exchangeLinkedInToken(String authorizationCode) throws Exception {

        String accessTokenUri = "https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&code="
                + authorizationCode + "&redirect_uri=" + REDIRECT_URI + "&client_id="
                + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "";

        String accessToken = getAccessToken(accessTokenUri);

        String fullName = getName(accessToken);
        String profilePhotoUrl = getProfilePhotoUrl(accessToken);
        String email = getEmail(accessToken);

        CandidateLinkedIn candidateLinkedIn;

        if (!isCandidateAlreadySaved(email)) {
            candidateLinkedIn = createAndSaveCandidate(fullName, profilePhotoUrl, email);
        } else if (findByEmail(email).isPresent()) {
            candidateLinkedIn = findByEmail(email).get();
        } else {
            throw new NotFoundException();
        }

        return candidateLinkedIn.getId();
    }

    private boolean isCandidateAlreadySaved(String email) {
        return findByEmail(email).isPresent();
    }

    private CandidateLinkedIn createAndSaveCandidate(String fullName, String profilePhotoUrl, String email) {
        CandidateLinkedIn candidateLinkedIn = CandidateLinkedIn.builder()
                .fullName(fullName)
                .profilePhotoUrl(profilePhotoUrl)
                .email(email)
                .build();

        save(candidateLinkedIn);
        return candidateLinkedIn;
    }

    public void save(CandidateLinkedIn candidateLinkedIn) {
        candidateLinkedInRepository.save(candidateLinkedIn);

        saveCandidateLinkedInSummary(candidateLinkedIn, false);
    }

    private String getAccessToken(String accessTokenUri) throws JSONException, LinkedInAccessTokenExpiredException {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String accessTokenRequest = restTemplate.getForObject(accessTokenUri, String.class);
            JSONObject jsonObjOfAccessToken = new JSONObject(accessTokenRequest);

            return jsonObjOfAccessToken.get("access_token").toString();
        } catch (HttpClientErrorException e) {
            throw new LinkedInAccessTokenExpiredException();
        }
    }

    private String getName(String accessToken) throws Exception {
        String jsonString = sendGetWithAuthorizationHeader(PROFILE_ENDPOINT, accessToken);

        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = jsonReader.readObject();

        getProfilePhotoUrl(accessToken);

        // Remove extra double quotes in JSONObject
        String localizedFirstName = jsonObject.get("localizedFirstName").toString()
                .replaceAll("^\"|\"$", "").replaceAll("[ğ]", "g")
                .replaceAll("[İ]", "I");
        String localizedLastName = jsonObject.get("localizedLastName").toString()
                .replaceAll("^\"|\"$", "").replaceAll("[ğ]", "g")
                .replaceAll("[İ]", "I");

        return localizedFirstName + " " + localizedLastName;
    }

    private String getProfilePhotoUrl(String accessToken) throws Exception {
        HttpsURLConnection connection = getHttpsURLConnectionWithAccessTokenConcatenized(IMAGE_ENDPOINT,
                accessToken);

        StringBuilder jsonString = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line);
        }

        br.close();

        return parseProfilePhotoApiResponse(new JSONObject(jsonString.toString()));
    }

    private String parseProfilePhotoApiResponse(JSONObject profilePhotoJson) throws JSONException {

        return profilePhotoJson.getJSONObject("profilePicture")
                .getJSONObject("displayImage~")
                .getJSONArray("elements")
                .getJSONObject(3)
                .getJSONArray("identifiers")
                .getJSONObject(0)
                .getString("identifier");
    }

    private HttpsURLConnection getHttpsURLConnectionWithAccessTokenConcatenized(String url,
                                                                                String accessToken) throws IOException {

        URL urlWithToken = new URL(url + accessToken);
        return (HttpsURLConnection) urlWithToken.openConnection();
    }

    private String sendGetWithAuthorizationHeader(String resourceUrl, String accessToken) throws IOException {
        HttpsURLConnection con = openAuthorizedConnectionTo(resourceUrl, accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder jsonString = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line);
        }

        br.close();

        return jsonString.toString();
    }

    private HttpsURLConnection openAuthorizedConnectionTo(String resourceUrl, String accessToken) throws IOException {
        URL url = new URL(resourceUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("cache-control", "no-cache");
        con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");

        return con;
    }

    private String getEmail(String accessToken) throws Exception {
        String response = sendGetWithAuthorizationHeader(EMAIL_ENDPOINT, accessToken);

        JSONObject responseAsJSON = new JSONObject(response);

        return parseEmailApiResponse(responseAsJSON);
    }

    private String parseEmailApiResponse(JSONObject responseAsJSON) throws JSONException {

        return responseAsJSON.getJSONArray("elements")
                .getJSONObject(0)
                .getJSONObject("handle~")
                .getString("emailAddress");
    }

    public void addCandidateToBlackList(String candidateId, String reason) {
        CandidateLinkedIn candidateLinkedIn = getCandidateLinkedInById(candidateId);
        Blacklist blacklistEntry = Blacklist.builder()
                .candidateLinkedIn(candidateLinkedIn)
                .reason(reason)
                .build();

        declineAllApplications(candidateLinkedIn);

        saveCandidateLinkedInSummary(candidateLinkedIn, true);
        blacklistService.save(blacklistEntry);
    }

    private void saveCandidateLinkedInSummary(CandidateLinkedIn candidateLinkedIn, boolean blacklisted) {
        Set<LinkedInSkill> candidateLinkedInSkillSet = candidateLinkedIn.getLinkedInSkillSet();

        if ((candidateLinkedInSkillSet != null && !blacklisted) && !candidateLinkedInSkillSet.isEmpty()) {
            candidateLinkedIn.getLinkedInSkillSet().stream().forEach(linkedInSkill ->
                    candidateLinkedInSummaryService.save(candidateLinkedIn.getId(),
                            candidateLinkedIn.getFullName(), candidateLinkedIn.getEmail(),
                            linkedInSkill.getSkill(), false));
        } else {
            candidateLinkedInSummaryService.save(candidateLinkedIn.getId(), candidateLinkedIn.getFullName(),
                                                 candidateLinkedIn.getEmail(), "", blacklisted);
        }
    }

    private void declineAllApplications(CandidateLinkedIn candidateLinkedIn) {
        List<Application> candidateApplications = candidateLinkedIn.getApplications();

        for (Application application : candidateApplications) {
            application.setApplicationStatus("Declined");
        }
    }

    public Boolean isBlacklisted(String candidateId) {
        return blacklistService.isBlacklisted(candidateId);
    }
}
