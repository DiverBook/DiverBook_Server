package ada.divercity.diverbook_server.service;

public interface TokenBlackListService {
    public void addTokenToBlackList(String token);
    public boolean isTokenBlackListed(String token);
}
