package com.example.bookstore.controller;

import com.example.bookstore.entity.FeedbackEntity;
import com.example.bookstore.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@AllArgsConstructor
public class FeedbackController
{

    FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity makeFeedback(@RequestBody FeedbackEntity feedback,
                                       @RequestParam Long userId)
    {
        try{
            return ResponseEntity.ok(feedbackService.makeFeedback(feedback,userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }


    @PutMapping
    public ResponseEntity compliteFeedback(@RequestParam Long id)
    {
        try{
            return ResponseEntity.ok(feedbackService.compliteFeedback(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

}
