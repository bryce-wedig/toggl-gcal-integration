package brycewedig.integration.services;

public class GCalService {

    private static final String ENDPOINT_GET_ALL_SECTIONS = "https://api.todoist.com/rest/" + API_VERSION + "/sections";

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public GCalService(OkHttpClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
    }
}
