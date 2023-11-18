package nur.sak.operation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransferMoneyDTO {
    private Double money;
    private Long cardId;
    private Long userId;
    private Long depositId;
}
