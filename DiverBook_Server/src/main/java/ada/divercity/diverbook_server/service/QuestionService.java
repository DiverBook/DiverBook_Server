package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    public List<QuestionDto> getRandomQuestions(int count);
}
