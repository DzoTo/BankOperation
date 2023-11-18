package nur.sak.operation.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nur.sak.operation.dto.CardDepositDTO;
import nur.sak.operation.entities.Deposit;
import nur.sak.operation.service.implementation.CardServiceImpl;
import nur.sak.operation.service.implementation.DepositServiceImpl;
import nur.sak.operation.service.implementation.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/deposit")
@Getter
@Setter
@RequiredArgsConstructor
public class DepositController {
    private final CardServiceImpl cardService;
    private final UserServiceImpl userService;
    private final DepositServiceImpl depositService;

    @PostMapping("/open")
    public Deposit openDepositAccount(@RequestBody CardDepositDTO dto) {
        return depositService.openDepositAcc(dto);
    }

    @GetMapping("/{cardId}")
    public Double getBalance(@PathVariable("cardId") Long cardId) {
        return depositService.checkBalance(cardId);
    }

    @GetMapping("/prediction/{initial_money}/{month}")
    public Double futureBalance(@PathVariable("initial_money") Long initial_money,
                                @PathVariable("month") Integer month) {
        return depositService.predictionOfFutureDepositBalance(initial_money, month);
    }

    @PutMapping("/add{depositId}/{money}")
    public void addMoneyToDeposit(@PathVariable("depositId") Long depositId,
                                  @PathVariable("money") Double money) {
        depositService.addMoneyWithCash(money, depositId);
    }

    @PutMapping("/tailak/{depositId}")
    public void increaseMoney(@PathVariable("depositId") Long depositId) {
        depositService.balanceAfterSomeDate(depositId);
    }

    @DeleteMapping("/delete{depositId}")
    public void deleteDepos(@PathVariable("depositId") Long depositId) {
        depositService.deleteDepos(depositId);
    }

    @PutMapping("/updateinfo{depositId")
    public void updateInfoData(@PathVariable("depositId") Long depositId,
                               @RequestParam(required = false) String currency,
                               @RequestParam(required = false) Double amountOfMoney,
                               @RequestParam(required = false) Date openAccDate) {
        depositService.updateDeposit(depositId, currency, amountOfMoney, openAccDate);
    }

}
