package nur.sak.operation.service.implementation;

import lombok.RequiredArgsConstructor;
import nur.sak.operation.OperationApplication;
import nur.sak.operation.dto.*;
import nur.sak.operation.entities.Card;
import nur.sak.operation.entities.Deposit;
import nur.sak.operation.entities.User;
import nur.sak.operation.repository.CardRepository;
import nur.sak.operation.repository.DepositRepository;
import nur.sak.operation.repository.UserRepository;
import nur.sak.operation.service.interfaces.CardService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final DepositRepository depositRepository;

    private final Log log = LogFactory.getLog(CardServiceImpl.class);

    @Override
    public Double checkBalance(Long userId, Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new IllegalStateException("No such account"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("No such user"));
        if (!user.getCardSet().contains(card)) {
            throw new IllegalStateException("This user doesn't have this kind of card");
        }
        return card.getMainBalance();
    }

//    public void openDeposit(CardDepositDTO dto) {
//        Card card = cardRepository.findById(dto.getCardId()).orElseThrow(() ->
//                new IllegalStateException("No such account"));
//        card.setIsDepositOpen(!card.getIsDepositOpen());
//
//        card.setDeposit(dto.getDeposit());
//        cardRepository.save(card);
//        depositRepository.save(dto.getDeposit());
//    }

    public void addMoneyWithCash(TransferMoneyDTO dto) {
        Card card = operationCard(dto);
        Double new_balance = card.getMainBalance() + dto.getMoney();
        card.setMainBalance(new_balance);
        cardRepository.save(card);
    }

    public Card operationCard(TransferMoneyDTO dto) {
        Card card = cardRepository.findById(dto.getCardId()).orElseThrow(() ->
                new IllegalStateException("No such account"));
        if (!checkConnectionUserWithCard(dto.getUserId(), dto.getCardId()) &&
                !checkConnectionCardWithDeposit(dto.getCardId(), dto.getDepositId())) {
            throw new IllegalStateException("This operation cannot be done");
        }
        return card;
    }

    public Deposit operationDep(TransferMoneyDTO dto) {
        return depositRepository.findById(dto.getDepositId()).orElseThrow(() ->
                new IllegalStateException("No such deposit is found"));
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new IllegalStateException("There is no such a card");
        }
        cardRepository.deleteById(cardId);
    }

    public void updateCard(Long cardId,
                           Long cardNum,
                           String bankName,
                           Double mainBalance,
                           Boolean isDepositOpen) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new IllegalStateException("No such a card"));

        if (cardNum != null && !Objects.equals(cardNum, card.getCardNum())) {
            card.setCardNum(cardNum);
        }

        if (bankName != null && bankName.length() > 0 && !Objects.equals(bankName, card.getBankName())) {
            card.setBankName(bankName);
        }

        if (mainBalance != null && mainBalance >= 0 && !Objects.equals(mainBalance, card.getMainBalance())) {
            card.setMainBalance(mainBalance);
        }

        if (isDepositOpen != null && !Objects.equals(isDepositOpen, card.getIsDepositOpen())) {
            card.setIsDepositOpen(isDepositOpen);
        }

    }


    @Override
    public void transferFromDepositToBalance(TransferMoneyDTO dto) {
        Card card = operationCard(dto);
        Deposit deposit = operationDep(dto);
        if (deposit.getAmountOfMoney() < dto.getMoney()) {
            throw new IllegalStateException("You dont have enough money on your deposit account");
        }
        card.setMainBalance(dto.getMoney() + card.getMainBalance());
        deposit.setAmountOfMoney(deposit.getAmountOfMoney() - dto.getMoney());
        cardRepository.save(card);
        depositRepository.save(deposit);
    }

    @Override
    public void transferFromBalanceToDeposit(TransferMoneyDTO dto) {
        Card card = operationCard(dto);
        Deposit deposit = operationDep(dto);
        if (card.getMainBalance() < dto.getMoney()) {
            throw new IllegalStateException("You dont have enough money on your account");
        }
        card.setMainBalance(card.getMainBalance() - dto.getMoney());
        deposit.setAmountOfMoney(dto.getMoney() + deposit.getAmountOfMoney());
        cardRepository.save(card);
        depositRepository.save(deposit);
    }

    @Override
    public void transferBetweenCardsByPhoneNum(TransferMoneyByPhoneNumDTO dto) {

        /*Указываем наименование банка внутри которого будет выполнена операция перевода средств через номер телефона*/
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() ->
                new IllegalStateException("No such user"));
        if (!userRepository.existsUserByPhoneNum(dto.getPhoneNum())) {
            throw new IllegalStateException("There is no such user with phone number: " + dto.getPhoneNum());
        }

        User user2 = userRepository.findByPhoneNum(dto.getPhoneNum());
        List<Card> list1 = new ArrayList<>(); /*user.getCardSet().size()*/
        List<Card> list2 = new ArrayList<>(); /*user2.getCardSet().size()*/
        for (Card card1 : user.getCardSet()) {

            if (card1.getBankName().equals(dto.getBankName())) {
                list1.add(card1);
            }
        }
        for (Card card2 : user2.getCardSet()) {
            if (card2.getBankName().equals(dto.getBankName())) {
                list2.add(card2);
            }
        }
        if (list1.get(0).getMainBalance() < dto.getMoney()) {
            throw new IllegalStateException("Yoy dont have enough money on yor account");
        }

//        System.out.println("Please write down phoneNumber in order to transfer money to " +
//                "another user with the same bank: ");
        log.info(dto.getPhoneNum());
        log.info("Please write down phoneNumber in order to transfer money to \" +\n" +
                "                \"another user with the same bank:");

        list2.get(0).setMainBalance(dto.getMoney() + list2.get(0).getMainBalance());
        list1.get(0).setMainBalance(list1.get(0).getMainBalance() - dto.getMoney() - 200);

        cardRepository.save(list1.get(0));
        cardRepository.save(list2.get(0));
    }

    public void transferMoneyByCardNum(TransferMoneyByCardNumDTO dto) {//Указываем наименование банка с которого мы хотим переслать деньги
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() ->
                new IllegalStateException("No such user"));
        if (!cardRepository.existsUserByCardNum(dto.getCardNum())) {
            throw new IllegalStateException("There is no such user with phone number: " + dto.getCardNum());
        }
        Card card2 = cardRepository.findByCardNum(dto.getCardNum());
        List<Card> list1 = new ArrayList<>(); /*user.getCardSet().size()*/
        for (Card card1 : user.getCardSet()) {
            if (card1.getBankName().equals(dto.getBankName())) {
                list1.add(card1);
            }
        }

        if (list1.get(0).getMainBalance() < dto.getMoney()) {
            throw new IllegalStateException("Yoy dont have enough money on yor account");
        }
        log.info("Please write down the cardNumber in order to " +
                "transfer money to another user/card: ");
        log.info(dto.getCardNum());
        if (!list1.get(0).getBankName().equals(card2.getBankName())) {
            log.info("You need to pay extra money to transfer to another bank, " +
                    "The amoung of comission is 200tenge");
            list1.get(0).setMainBalance(list1.get(0).getMainBalance() - 200);
        }
        list1.get(0).setMainBalance(list1.get(0).getMainBalance() - dto.getMoney());
        card2.setMainBalance(card2.getMainBalance() + dto.getMoney());

        cardRepository.save(list1.get(0));
        cardRepository.save(card2);
    }

    public void addCardToUser(UserCardDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() ->
                new IllegalStateException("User with id: " + dto.getUserId() + " doesnt exist"));
        user.getCardSet().add(dto.getCard());
        user.setCardSet(user.getCardSet());
        dto.getCard().getUserSet().add(user);
        dto.setCard(dto.getCard());
        cardRepository.save(dto.getCard());
        userRepository.save(user);
    }

    public boolean checkConnectionUserWithCard(Long userId, Long cardid) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("User with id: " + userId + " doesnt exist"));
        Card card1 = cardRepository.findById(cardid).orElseThrow(() ->
                new IllegalStateException("No such account"));

        if (user.getCardSet().contains(card1)) {
            return true;
        }
        return false;
    }

    public boolean checkConnectionCardWithDeposit(Long cardId, Long depositId) {
        Deposit deposit = depositRepository.findById(depositId).orElseThrow(() ->
                new IllegalStateException("No such deposit is found"));
        Card card1 = cardRepository.findById(cardId).orElseThrow(() ->
                new IllegalStateException("No such account"));

        if (card1.getDeposit() == deposit) {
            return true;
        }
        return false;
    }
}
