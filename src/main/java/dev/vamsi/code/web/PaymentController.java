package dev.vamsi.code.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.vamsi.code.service.PaymentService;


@RestController
public class PaymentController {
	
	private final PaymentService paymentService;
	
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/submit")
	public String submitPayment(@RequestParam("amount") Double amount) {
		return paymentService.processPayment(amount);
	}
	
}
