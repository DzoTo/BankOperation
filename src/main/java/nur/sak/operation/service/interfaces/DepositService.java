package nur.sak.operation.service.interfaces;

import nur.sak.operation.dto.CardDepositDTO;
import nur.sak.operation.entities.Deposit;

import java.util.Date;

public interface DepositService {

    Deposit openDepositAcc(CardDepositDTO dto);

    Double checkBalance(Long cardId);

    Double predictionOfFutureDepositBalance(Long initial_money, Integer month);
}
