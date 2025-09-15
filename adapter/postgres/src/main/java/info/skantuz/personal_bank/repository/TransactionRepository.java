package info.skantuz.personal_bank.repository;

import info.skantuz.personal_bank.dto.TransactionDto;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface TransactionRepository extends R2dbcRepository<TransactionDto, UUID>  {
}
