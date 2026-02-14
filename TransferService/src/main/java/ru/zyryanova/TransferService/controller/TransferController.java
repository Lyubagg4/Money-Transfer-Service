package ru.zyryanova.TransferService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransfer(@RequestBody @Valid TransferDto transferDto) throws JsonProcessingException {
        transferService.create(transferDto);
    }
}
