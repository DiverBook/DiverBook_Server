package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.ApiResponse;
import ada.divercity.diverbook_server.dto.QuestionDto;
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
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getQuestions(@PathVariable Integer count) {

        List<QuestionDto> questions = questionService.getRandomQuestions(count);

        return ResponseEntity.ok(ApiResponse.success(questions));

    }
}
