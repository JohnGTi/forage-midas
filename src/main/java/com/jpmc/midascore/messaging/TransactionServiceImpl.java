package com.jpmc.midascore.messaging;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.config.IncentiveConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private DatabaseConduit databaseConduit;

    @Autowired
    private IncentiveConduit incentiveConduit;

    @Override
    @Transactional
    public void onReceive(Transaction transaction) {

        if (transaction == null) { logger.warn("Received null Transaction object"); return; }

        Long senderId = transaction.getSenderId();
        Long recipientId = transaction.getRecipientId();

        float amount = transaction.getAmount();

        // Retrieve the sender and recipient entries.

        Optional<UserRecord> optionalSender = databaseConduit.findUserById(senderId);
        Optional<UserRecord> optionalRecipient = databaseConduit.findUserById(recipientId);

        if (optionalSender.isPresent() && optionalRecipient.isPresent())
        {
            // Sender and user IDs are valid.

            UserRecord sender = optionalSender.get();
            UserRecord recipient = optionalRecipient.get();

            // Is the sender's balance is >= the transaction amount?

            if (sender.getBalance() >= amount)
            {
                // Post the transaction to the Incentives API endpoint.

                Incentive incentive = incentiveConduit.getRestTemplate().postForObject(incentiveConduit.getUrl()
                        , transaction
                        , Incentive.class);

                // The incentive is recorded as part of the transaction.

                final float incentiveAmount = incentive != null ? incentive.getAmount() : 0f;

                transaction.setIncentive(incentiveAmount);

                // Update the balance of the sender and recipient.
                logger.info("{} {} and {} {} transact {}, incentive {}", sender.getName(), sender.getBalance(), recipient.getName(), recipient.getBalance(), amount, incentiveAmount);
                sender.setBalance(sender.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);
                logger.info("{} {} and {} {}", sender.getName(), sender.getBalance(), recipient.getName(), recipient.getBalance());
                // Record the transaction.

                databaseConduit.save(new TransactionRecord(sender, recipient, amount, incentiveAmount));
            }
        }
    }
}
