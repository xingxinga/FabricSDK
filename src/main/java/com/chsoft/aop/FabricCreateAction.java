package com.chsoft.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.stereotype.Component;

import com.chsoft.fabric.FabricClient;
import com.chsoft.fabric.FabricOrderer;
import com.chsoft.fabric.FabricPeer;

@Component  
@Aspect  
public class FabricCreateAction { 
	//指定在具体注释后执行该方法
	@Before( value = "@annotation(com.chsoft.aop.FabricCreate)")
    public void afterMethodXML(JoinPoint joinPoint) throws Exception{  
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        
        FabricCreate fabricCreate = ms.getMethod().getAnnotation(FabricCreate.class);
        
        Object[] arguments = joinPoint.getArgs();
        
        Object obj = joinPoint.getTarget();
        
        Method getHFClient=obj.getClass().getDeclaredMethod("getHFClient");
        
        getHFClient.setAccessible(true);
        
        HFClient client = (HFClient) getHFClient.invoke(obj);
        
        FabricPeer argumentPeer = null;

        FabricOrderer argumentOrderer = null;
        
        Peer peer = null;
        
        Orderer orderer = null;
        
        String channelName = null ;
        
        if(fabricCreate.peer()!=-1){
        	Field peerField = obj.getClass().getDeclaredField("peer");
        	peerField.setAccessible(true);
        	argumentPeer = (FabricPeer) arguments[fabricCreate.peer()-1];
        	peer = client.newPeer(argumentPeer.getPeerName(), argumentPeer.getPeerLocation());
        	peerField.set(obj, peer);
        }
        
        if(fabricCreate.orderer()!=-1){
        	Field ordererField = obj.getClass().getDeclaredField("orderer");
        	ordererField.setAccessible(true);
        	argumentOrderer = (FabricOrderer) arguments[fabricCreate.orderer()-1];
        	orderer = client.newOrderer(argumentOrderer.getOrdererName(), argumentOrderer.getOrdererLocation());
        	ordererField.set(obj, orderer);
        }
        
        if(fabricCreate.channel()!= -1){
    		channelName = (String) arguments[fabricCreate.channel()-1];
            Field channelField = obj.getClass().getDeclaredField("channel");
            channelField.setAccessible(true);
        	Channel channelNow = client.newChannel(channelName);
        	if(peer != null){
        		channelNow.addPeer(peer);
        	}
        	if(orderer != null){
        		channelNow.addOrderer(orderer);
        	}
        	FabricClient.setField(channelNow, "initialized", true);
        	channelField.set(obj, channelNow);
    	}
        
    }
	
	//指定在具体注释前执行该方法
	
	@After( value = "@annotation(com.chsoft.aop.FabricCreate)")
    public void beforeMethodXML(JoinPoint joinPoint) {  
        //String opreate = joinPoint.getSignature().getName();  
    }
}