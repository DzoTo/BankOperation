package nur.sak.operation.service.interfaces;

import nur.sak.operation.dto.TransferMoneyDTO;
import nur.sak.operation.dto.TransferMoneyByPhoneNumDTO;

public interface CardService {

    Double checkBalance(Long userId, Long cardId);

    void transferFromDepositToBalance(TransferMoneyDTO dto);
    void transferFromBalanceToDeposit(TransferMoneyDTO dto);
    void transferBetweenCardsByPhoneNum(TransferMoneyByPhoneNumDTO dto);

}
