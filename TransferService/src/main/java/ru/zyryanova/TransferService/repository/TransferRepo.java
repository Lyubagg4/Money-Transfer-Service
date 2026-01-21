package ru.zyryanova.TransferService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Repository;
import ru.zyryanova.TransferService.entity.Transfer;

@Repository
public interface TransferRepo extends JpaRepository<Transfer, Integer> {
}
