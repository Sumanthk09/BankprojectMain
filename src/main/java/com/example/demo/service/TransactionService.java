package com.example.demo.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TransactionDto;
import com.example.demo.model.CustomerDetails;
import com.example.demo.model.TransactionDetails;
import com.example.demo.repo.CustomerRepo;
import com.example.demo.repo.TransactionRepo;

@Service
public class TransactionService 
{
	@Autowired
	TransactionRepo tr;
	
	@Autowired
	CustomerRepo cr;

	//updates account details after transaction
	public String makeTransaction(String senderaccholdername, String recieverraccholdername, String amount, String inblacklist)
	{
		Date d= new Date(System.currentTimeMillis());
		long now = System.currentTimeMillis();
		Time t=new Time(now);
		if(inblacklist.equals("no"))
		{
		
		//Amountlogic
		CustomerDetails sender=cr.findByAccholdername(senderaccholdername);
		CustomerDetails reciever=cr.findByAccholdername(recieverraccholdername);
		//System.out.println(sender.getBalance()+" "+amount);
		long afterDeductionBalanace=Long.valueOf(sender.getBalance())- Long.valueOf(amount);
		//System.out.println(sender.getBalance()+" "+amount);
		sender.setBalance(String.valueOf(afterDeductionBalanace));
		long afterReceivingAmount=Long.valueOf(reciever.getBalance())+Long.valueOf(amount);
		reciever.setBalance(String.valueOf(afterReceivingAmount));
		
		
		cr.save(sender);
		cr.save(reciever);
		
		//Storing it in transaction repository
		TransactionDetails transaction=new TransactionDetails();
		String randomstring=usingRandomUUID();
		transaction.setTid(randomstring);
		transaction.setTransactionamount(amount);
		transaction.setSender(senderaccholdername);
		transaction.setReceiver(recieverraccholdername);
		transaction.setDate(d);
		transaction.setTime(t);
		transaction.setInblacklist(inblacklist);
		transaction.setStatus("Transaction Successful");
		tr.save(transaction);
		return "success";
		}
		TransactionDetails transaction=new TransactionDetails();
		String randomstring=usingRandomUUID();
		transaction.setTid(randomstring);
		transaction.setTransactionamount(amount);
		transaction.setSender(senderaccholdername);
		transaction.setReceiver(recieverraccholdername);
		transaction.setDate(d);
		transaction.setTime(t);
		transaction.setInblacklist(inblacklist);
		transaction.setStatus("Process...");
		tr.save(transaction);
		return "failure";
		
	}
	
	
	  public String usingRandomUUID()
	  {

		    UUID randomUUID = UUID.randomUUID();

		    return randomUUID.toString().replaceAll("_", "");

	  }
	  
	  //gets transactions of sender
	  public List<TransactionDto> getUserTrans(String sender) {
	        
	        List<TransactionDetails> tdetails = tr.findBySender(sender);
	        List<TransactionDto> tList = new ArrayList<>();
	        if(tdetails.size()!=0)
	        {
	        for(int i=0;i<tdetails.size();i++) 
	        {
	            tList.add(new TransactionDto(tdetails.get(i).getTid(),tdetails.get(i).getTransactionamount(),
	                    tdetails.get(i).getSender(),tdetails.get(i).getReceiver(),tdetails.get(i).getDate(),
	                    tdetails.get(i).getTime(),tdetails.get(i).getStatus())
	            		);
	        }
	        return tList;
	        }
	        return null;
	    }


	public List<TransactionDetails> getBlackListDetails(String blackStatus) 
	{
		 return tr.findByInblacklist(blackStatus);
	}


	//Check receiver
    public boolean checkReceiver(String receiver) {
        CustomerDetails custDetails = cr.findByAccholdername(receiver);
        if(custDetails!=null)
            return true;
        return false;
    }


}
