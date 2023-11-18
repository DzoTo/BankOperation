package nur.sak.operation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nur.sak.operation.entities.Deposit;

@Getter
@Setter
@RequiredArgsConstructor
public class CardDepositDTO {
    private Long cardId;
    private Deposit deposit;
}
