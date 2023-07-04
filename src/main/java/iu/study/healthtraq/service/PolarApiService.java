package iu.study.healthtraq.service;

import iu.study.api.polar.ApiClient;
import iu.study.api.polar.ApiException;
import iu.study.api.polar.api.UsersApi;
import iu.study.api.polar.model.Register;
import iu.study.api.polar.model.User;
import iu.study.healthtraq.exceptions.DatabaseException;
import iu.study.healthtraq.models.Participant;
import iu.study.healthtraq.properties.PolarProperties;
import iu.study.healthtraq.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PolarApiService {
    private final PolarProperties polarProperties;
    private final ParticipantRepository participantRepository;

    Logger logger = LoggerFactory.getLogger(PolarApiService.class);

    private static final String POLAR_BASE_URL = "https://flow.polar.com/oauth2/authorization";

    public String getAuthorizationUrl() {
        return POLAR_BASE_URL +
                "?response_type=code" +
                "&client_id=" + polarProperties.getClientId() +
                "&redirect_uri=" + polarProperties.getRedirectUri();
    }

    public Participant getPolarParticipant(long id){
        return participantRepository
                .findById(id)
                .orElseThrow(() -> new DatabaseException("participant", "id", id));
    }

    public void createParticipant(@NotNull User polarUser, String token, String memberId){
        if(polarUser.getPolarUserId() == null){
            logger.error("Returned polar user id is null, participant not saved");
            return;
        }

        Participant participant = Participant.builder()
                .firstName(polarUser.getFirstName())
                .lastName(polarUser.getLastName())
                .polarUserId(Math.toIntExact(polarUser.getPolarUserId()))
                .polarMemberId(memberId)
                .polarToken(token)
                .build();
        participantRepository.save(participant);
    }

    public void registerParticipant(String code) throws ApiException {
        ApiClient apiClient = new ApiClient(
                polarProperties.getRedirectUri(),
                polarProperties.getClientId(),
                polarProperties.getClientSecret(),
                code);
        UsersApi usersApi = new UsersApi(apiClient);

        try {
            String memberId = UUID.randomUUID().toString();
            Register registerRequest = new Register().memberId(memberId);
            User polarUser = usersApi.registerUser(registerRequest);
            createParticipant(polarUser, apiClient.getPolarToken().get(), memberId);
            logger.info("Polar participant added to system");
        }catch(ApiException ex){
            if(ex.getResponseBody() == null || !ex.getResponseBody().contains("user_already_registered"))
                throw ex;

            if(apiClient.getPolarUserId().isEmpty()){
                logger.error("Seems like participant is already registered but user id cant be found");
                return;
            }

            int polarUserId = apiClient.getPolarUserId().get();
            logger.warn("Seems like user is already registered in the system, user id: " + polarUserId);
            if(participantRepository.existsByPolarUserId(polarUserId)){
                logger.info("User already created in system, not doing it again");
            }else{
                User registeredUser = usersApi.getUserInformation(polarUserId);
                createParticipant(registeredUser, apiClient.getPolarToken().get(), registeredUser.getMemberId());
                logger.info("Polar participant added to system");
            }
        }
    }
}
