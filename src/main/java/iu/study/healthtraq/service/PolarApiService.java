package iu.study.healthtraq.service;

import iu.study.api.polar.ApiClient;
import iu.study.api.polar.ApiException;
import iu.study.api.polar.api.UsersApi;
import iu.study.api.polar.model.Register;
import iu.study.api.polar.model.User;
import iu.study.healthtraq.models.Participant;
import iu.study.healthtraq.properties.PolarProperties;
import iu.study.healthtraq.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PolarApiService {
    private final PolarProperties polarProperties;
    private final ParticipantRepository participantRepository;

    private static final String POLAR_BASE_URL = "https://flow.polar.com/oauth2/authorization";

    public String getAuthorizationUrl() {
        return POLAR_BASE_URL +
                "?response_type=code" +
                "&client_id=" + polarProperties.getClientId() +
                "&redirect_uri" + polarProperties.getRedirectUri();
    }

    public void registerParticipant(String code) throws ApiException {
        ApiClient apiClient = new ApiClient(
                polarProperties.getClientId(),
                polarProperties.getClientSecret(),
                code);
        String memberId = UUID.randomUUID().toString();

        UsersApi usersApi = new UsersApi(apiClient);
        Register registerRequest = new Register().memberId(memberId);
        User polarUser = usersApi.registerUser(registerRequest);

        Participant participant = Participant.builder()
                .firstName(polarUser.getFirstName())
                .lastName(polarUser.getLastName())
                .polarUserId(polarUser.getPolarUserId())
                .polarMemberId(memberId)
                .build();
        participantRepository.save(participant);
    }
}
