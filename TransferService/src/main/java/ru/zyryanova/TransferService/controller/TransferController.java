package ru.zyryanova.TransferService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zyryanova.TransferService.entity.TransferDto;
import ru.zyryanova.TransferService.repository.TransferRepo;
import ru.zyryanova.TransferService.service.TransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;
    private final TransferRepo transferRepo;

    @Autowired
    public TransferController(TransferService transferService, TransferRepo transferRepo) {
        this.transferService = transferService;
        this.transferRepo = transferRepo;
    }

    @PostMapping
    public void createTransfer(@RequestBody TransferDto transferDto){
        transferService.create(transferDto);
    }
}
