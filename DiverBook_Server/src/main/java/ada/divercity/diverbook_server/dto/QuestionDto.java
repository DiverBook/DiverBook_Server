package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.Question;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private Integer id;
    private String question;
    private String symbol;

    public static QuestionDto fromEntity(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .symbol(question.getSymbol())
                .build();
    }
}
