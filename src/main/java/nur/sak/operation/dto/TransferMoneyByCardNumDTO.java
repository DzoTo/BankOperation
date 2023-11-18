package nur.sak.operation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TransferMoneyByCardNumDTO {
    private Long userId;
    private Long cardNum;
    private String bankName;
    private Double money;
}
