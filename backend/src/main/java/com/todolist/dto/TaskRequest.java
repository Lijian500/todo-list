package com.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Title is required")
    @Length(max = 100, message = "Title can be up to 100 characters")
    private String title;

    @Length(max = 1000, message = "Description can be up to 1000 characters")
    private String description;
    
    private Boolean completed;
}
