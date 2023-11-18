package nur.sak.operation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransferMoneyByPhoneNumDTO {
    private Long userId;
    private Long phoneNum;
    private String bankName;
    private Double money;

}
