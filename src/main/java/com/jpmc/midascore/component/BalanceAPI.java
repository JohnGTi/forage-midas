package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BalanceAPI {

    /**
     * Provides a search function——of User records by ID.
     */
    @Autowired
    private DatabaseConduit databaseConduit;

    /**
     * The relative path of the Balance endpoint.
     */
    private final String resourcePath = "/balance";

    @GetMapping(resourcePath)
    public Balance balance(@RequestParam Long userId) {

        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }

        if (databaseConduit == null) {
            throw new IllegalStateException("databaseConduit is not initialised");
        }

        // Look for a UserRecord for the corresponding userId.

        Optional<UserRecord> userRecordOptional = databaseConduit.findUserById(userId);

        // Return the user's balance, or a balance of zero.

        return new Balance(userRecordOptional.map(UserRecord::getBalance).orElse(0f));
    }
}
