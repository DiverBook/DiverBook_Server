package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.ApiResponse;
import ada.divercity.diverbook_server.dto.QuestionDto;
import ada.divercity.diverbook_server.exception.CustomException;
import ada.divercity.diverbook_server.exception.ErrorCode;
import ada.divercity.diverbook_server.service.QuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{count}")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getQuestions(@PathVariable String count) {
        if (!count.matches("\\d+")) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            Integer.parseInt(count);
        } catch (Exception e) {
            if (e instanceof CustomException) {
                if (((CustomException) e).getErrorCode() == ErrorCode.INVALID_INPUT_VALUE) {
                    throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
                }
            } else {
                throw e;
            }
        }

        List<QuestionDto> questions = questionService.getRandomQuestions(Integer.parseInt(count));
        return ResponseEntity.ok(ApiResponse.success(questions));
    }
}
