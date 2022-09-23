package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TransactionDto;
import com.example.demo.model.TransactionDetails;
import com.example.demo.service.TransactionService;


@RestController
public class TransactionController 
{
	@Autowired
	TransactionService rs;
	
	@PutMapping("/transaction")
	public String Transaction(@RequestParam String senderaccholdername,@RequestParam String recieverraccholdername,String amount,String inblacklist)
	{
		return rs.makeTransaction(senderaccholdername,recieverraccholdername,amount,inblacklist);
	}
	
	
	@GetMapping("/getUserTransactions")    //sending transactions to account holder
    public List<TransactionDto> getUserTrans(@RequestParam String sender){
        return rs.getUserTrans(sender);
    }
	
	@GetMapping("/getBlackListTransactions")
    public List<TransactionDetails> getBlacList(){
        return rs.getBlackListDetails("yes");
    }
	
	@GetMapping("/getReceiver")
    public boolean checkReceiver(@RequestParam String receiver) {
        return rs.checkReceiver(receiver);
    }
}
