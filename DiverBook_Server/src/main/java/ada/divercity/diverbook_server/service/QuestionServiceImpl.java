package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.QuestionDto;
import ada.divercity.diverbook_server.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    
    public List<QuestionDto> getRandomQuestions(int count) {
        List<QuestionDto> allQuestions = questionRepository.findAll().stream()
                .map(question -> new QuestionDto(
                        question.getId(),
                        question.getQuestion()
                ))
                .toList();

        if (allQuestions.size() <= count) {
            return allQuestions;
        }

        return new Random()
                .ints(0, allQuestions.size())
                .distinct()
                .limit(count)
                .mapToObj(allQuestions::get)
                .collect(Collectors.toList());
    }
}
