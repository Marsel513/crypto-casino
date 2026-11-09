package com.crypto.casino.wallet;

import com.crypto.casino.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "wallets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private long balance;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
    }

    public Wallet(User user){
        this.user = user;
    }

    public void credit(long amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public void debit(long amount){
        if (amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (balance < amount){
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance -= amount;
    }

}
