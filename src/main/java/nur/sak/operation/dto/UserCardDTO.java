package nur.sak.operation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nur.sak.operation.entities.Card;


@Getter
@Setter
@AllArgsConstructor
public class UserCardDTO {
    private Long userId;
    private Card card;
}
