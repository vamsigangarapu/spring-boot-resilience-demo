package dev.vamsi.code.service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import dev.vamsi.code.PaymentNetworkException;

@Service
public class PaymentService {

	private final Random random = new Random();
	private final AtomicInteger attemptCounter = new AtomicInteger();

	/**
	 * ConcurrencyLimit occurs first, checks for an available concurrent slot or
	 * thread 
	 * Retryable occurs second. i.e., with in each concurrencyLimit there
	 * will be set amount of retries
	 * 
	 * @param amount
	 * @return
	 */
	@Retryable(includes = 
			PaymentNetworkException.class, 
			maxRetries = 4, 
			delay = 1000, 
			jitter = 100, 
			multiplier = 2, 
			maxDelay = 10000)
	@ConcurrencyLimit(2)
	public String processPayment(double amount) {
		attemptCounter.incrementAndGet();
		
		IO.println("Current thread " + Thread.currentThread().threadId());
		IO.println("Saving payment in attempt #" + attemptCounter.get());
		
		if (random.nextBoolean()) {
			IO.println("Simulated Network failure!!! Will retry if attempts are reamining");
			throw new PaymentNetworkException("Network Failure");
		}

		//resetting the counter
		attemptCounter.set(0);
		
		return "payment of $ " + amount + " processed successfully for order Id " + randomOrderIdGenerator();

	}

	String randomOrderIdGenerator() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
	}
}
