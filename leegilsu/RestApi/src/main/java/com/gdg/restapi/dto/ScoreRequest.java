package com.gdg.restapi.dto;

import com.gdg.restapi.domain.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public record ScoreRequest(

        @NotBlank(groups = CreateGroup.class, message = "악보명을 작성해주세요.")
        @Pattern(regexp = "^.*\\S.*$", groups = UpdateGroup.class, message = "악보명을 작성해주세요.")
        String name, // 악보 이름

        String composer, // 작곡가

        @Range(min = 0, max = 127, message = "악기 번호는 0 ~ 127 입니다.")
        Integer instrument, // 악기

        List<Note> melody // 멜로디
) {
    public interface CreateGroup extends Default {
    }

    public interface UpdateGroup extends Default {
    }
}
