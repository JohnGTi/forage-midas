package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository)
    {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");

        this.transactionRepository = Objects.requireNonNull(transactionRepository
                , "transactionRepository must not be null");
    }

    public void save(UserRecord userRecord)
    {
        Objects.requireNonNull(userRecord, "userRecord must not be null");
        userRepository.save(userRecord);
    }

    public void save(TransactionRecord transactionRecord)
    {
        Objects.requireNonNull(transactionRecord, "transactionRecord must not be null");
        transactionRepository.save(transactionRecord);
    }

    public Optional<UserRecord> findUserById(Long userId)
    {
        if (userId != null)
        {
            return userRepository.findById(userId);
        }

        return Optional.empty();
    }
}
