package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.entity.TokenBlackList;
import ada.divercity.diverbook_server.repository.TokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;

    public void addTokenToBlackList(String token) {
        TokenBlackList newBlackList = TokenBlackList.builder().token(token).build();
        tokenBlackListRepository.save(newBlackList);
    }

    public boolean isTokenBlackListed(String token) {
        return tokenBlackListRepository.findByToken(token).isPresent();
    }
}
