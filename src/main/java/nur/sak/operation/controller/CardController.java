package nur.sak.operation.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nur.sak.operation.dto.TransferMoneyByCardNumDTO;
import nur.sak.operation.dto.TransferMoneyDTO;
import nur.sak.operation.dto.TransferMoneyByPhoneNumDTO;
import nur.sak.operation.service.implementation.CardServiceImpl;
import nur.sak.operation.service.implementation.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@Getter
@Setter
@RequiredArgsConstructor
public class CardController {
    private final UserServiceImpl userService;
    private final CardServiceImpl cardService;

    @GetMapping("/checkbalance/{userId}/{cardId}")
    public Double checkBalance(@PathVariable("userId") Long id1,
                               @PathVariable("cardId") Long id2) {
        return cardService.checkBalance(id1, id2);
    }

    @PutMapping("/addmoney")
    public void putMoney(@RequestBody TransferMoneyDTO dto) {
        cardService.addMoneyWithCash(dto);
    }

    @PutMapping("/transf_t_balance")
    public void transferToBalance(@RequestBody TransferMoneyDTO dto) {
        cardService.transferFromDepositToBalance(dto);
    }

    @PutMapping("/transf_t_deposit")
    public void transferToDeposit(@RequestBody TransferMoneyDTO dto) {
        cardService.transferFromBalanceToDeposit(dto);
    }

    @PutMapping("/trans_via_phonenumber")
    public void CardToCardInTheSameBank(@RequestBody TransferMoneyByPhoneNumDTO dto) {
        cardService.transferBetweenCardsByPhoneNum(dto);
    }

    @PutMapping("/trans_via_cardnumber")
    public void CardToCardInTheDifferentBank(@RequestBody TransferMoneyByCardNumDTO dto) {
        cardService.transferMoneyByCardNum(dto);
    }

    @DeleteMapping("/delete{cardId}")
    public void removeElement(@PathVariable("cardId") Long cardId) {
        cardService.deleteCard(cardId);
    }

    @PutMapping("/updateinfo{cardId}")
    public void updateAll(@PathVariable("cardId") Long cardId,
                          @RequestParam(required = false) Long cardNum,
                          @RequestParam(required = false) String bankNum,
                          @RequestParam(required = false) Double mainBalance,
                          @RequestParam(required = false) Boolean isDepositOpen) {
        cardService.updateCard(cardId, cardNum, bankNum, mainBalance, isDepositOpen);
    }
}
