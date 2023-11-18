package nur.sak.operation.service.implementation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nur.sak.operation.dto.CardDepositDTO;
import nur.sak.operation.dto.TransferMoneyDTO;
import nur.sak.operation.entities.Card;
import nur.sak.operation.entities.Deposit;
import nur.sak.operation.repository.CardRepository;
import nur.sak.operation.repository.DepositRepository;
import nur.sak.operation.repository.UserRepository;
import nur.sak.operation.service.interfaces.DepositService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final DepositRepository depositRepository;

    @Override
    public Deposit openDepositAcc(CardDepositDTO dto) {
        Card card = cardRepository.findById(dto.getCardId()).orElseThrow(() ->
                new IllegalStateException("No such account"));
        card.setIsDepositOpen(!card.getIsDepositOpen());

        card.setDeposit(dto.getDeposit());
        cardRepository.save(card);
        return depositRepository.save(dto.getDeposit());
    }

    public void addMoneyWithCash(Double money, Long depositId) {
        Deposit deposit = depositRepository.findById(depositId).orElseThrow(() ->
                new IllegalStateException("No such account"));
        Double new_balance = deposit.getAmountOfMoney() + money;
        deposit.setAmountOfMoney(new_balance);
        depositRepository.save(deposit);
    }

    @Override
    public Double checkBalance(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new IllegalStateException("No such account"));
        if (!card.getIsDepositOpen()) {
            throw new IllegalStateException("This card does not have deposit account");
        }
        Double money = balanceAfterSomeDate(card.getDeposit().getId());
        if (Objects.equals(money, card.getDeposit().getAmountOfMoney())) {
            return card.getDeposit().getAmountOfMoney();
        }
        card.getDeposit().setAmountOfMoney(money);
        depositRepository.save(card.getDeposit());
        return card.getDeposit().getAmountOfMoney();
    }

    @Override
    public Double predictionOfFutureDepositBalance(Long initial_money, Integer month) {
        int years = 0;
        double predicterMoney = initial_money;
        if (month % 12 == 0) {
            years = month / 12;
            for (int i = 0; i < years; i++) {
                predicterMoney = predicterMoney * 1.15;
            }
            return predicterMoney;

        }
        for (int i = 0; i < month; i++) {
            predicterMoney = predicterMoney * 1.0125;
        }
        return predicterMoney;
    }

    public void deleteDepos(Long depositId) {
        if (!depositRepository.existsById(depositId)) {
            throw new IllegalStateException("No such deposit account");
        }
        depositRepository.deleteById(depositId);
    }

    public void updateDeposit(Long id,
                              String currency,
                              Double amountOfMoney,
                              Date openAccDate) {
        Deposit deposit = depositRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("No such deposit account"));
        if (currency != null && currency.length() > 0 && !Objects.equals(currency, deposit.getCurrency())) {
            deposit.setCurrency(currency);
        }

        if (amountOfMoney != null && amountOfMoney > 0 && !Objects.equals(amountOfMoney, deposit.getAmountOfMoney())) {
            deposit.setAmountOfMoney(amountOfMoney);
        }

        if (openAccDate != null && !Objects.equals(openAccDate, deposit.getOpenAccDate())) {
            deposit.setOpenAccDate(openAccDate);
        }
    }


    public Double balanceAfterSomeDate(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId).orElseThrow(() ->
                new IllegalStateException("No such deposit"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deposit.getOpenAccDate());

        Calendar now = Calendar.getInstance();

        Integer monthBetween = calculateMonthBetween(calendar, now);

        return calculateIncreasedMoney(deposit.getAmountOfMoney(), monthBetween, deposit.getInterestRate() / 12);


    }

    public Integer calculateMonthBetween(Calendar startDate, Calendar endDate) {
        Integer startYear = startDate.get(Calendar.YEAR);
        Integer startMonth = startDate.get(Calendar.MONTH);
        Integer endYear = endDate.get(Calendar.YEAR);
        Integer endMonth = endDate.get(Calendar.MONTH);

        return (endYear - startYear) * 12 + (endMonth - startMonth);
    }

    public Double calculateIncreasedMoney(Double initialMoney, Integer month, Double interestRate) {
        Double currentMoney = initialMoney;
        interestRate = 0.0125;
        for (int i = 0; i < month; i++) {
            currentMoney = currentMoney * (1.0000 + interestRate);
        }
        return currentMoney;
    }
}
