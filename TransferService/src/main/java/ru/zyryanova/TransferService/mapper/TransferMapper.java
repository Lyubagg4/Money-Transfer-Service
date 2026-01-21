package ru.zyryanova.TransferService.mapper;

import org.mapstruct.Mapper;
import ru.zyryanova.TransferService.entity.Transfer;
import ru.zyryanova.TransferService.entity.TransferDto;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    Transfer toEntity(TransferDto transferDto);
    TransferDto toDto(Transfer transfer);
}
