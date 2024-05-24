package it.uniroma3.vestiti.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import it.uniroma3.vestiti.service.CredentialsService;
import it.uniroma3.vestiti.service.NegozioService;



@Controller
public class IndexController {
	
	@Autowired
	private CredentialsService credentialsService;
	
}
