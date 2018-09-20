package com.chsoft.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.stereotype.Component;

import com.chsoft.fabric.FabricOrderer;
import com.chsoft.fabric.FabricPeer;

@Component  
@Aspect  
public class ClientMethodAction { 
	//指定在具体注释后执行该方法
	@Before( value = "@annotation(com.chsoft.aop.ClientMethod)")
    public void afterMethodXML(JoinPoint joinPoint) throws Exception{  
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();
        
        ClientMethod clientMethod = ms.getMethod().getAnnotation(ClientMethod.class);
        
        Object[] arguments = joinPoint.getArgs();
        
        Object obj = joinPoint.getTarget();
        
        Method getHFClient=obj.getClass().getDeclaredMethod("getHFClient");
        getHFClient.setAccessible(true);
        HFClient client = (HFClient) getHFClient.invoke(obj);
        
        if(clientMethod.peer()!=-1){
        	Field peer = obj.getClass().getDeclaredField("peer");
        	peer.setAccessible(true);
        	FabricPeer argumentPeer = (FabricPeer) arguments[clientMethod.peer()-1];
        	peer.set(obj, client.newPeer(argumentPeer.getPeerName(), argumentPeer.getPeerLocation()));
        }
        
        if(clientMethod.orderer()!=-1){
        	Field orderer = obj.getClass().getDeclaredField("orderer");
        	orderer.setAccessible(true);
        	FabricOrderer argumentOrderer = (FabricOrderer) arguments[clientMethod.orderer()-1];
        	orderer.set(obj, client.newOrderer(argumentOrderer.getOrdererDomainName(), argumentOrderer.getOrdererLocation()));
        }
        
        /*if(clientMethod.channel()!=-1){
        	Field channel = obj.getClass().getDeclaredField("channel");
        	channel.setAccessible(true);
        	String channelName = (String) arguments[clientMethod.channel()-1];
        	channel.set(obj, null);
        }*/
		
    }
	
	//指定在具体注释前执行该方法
	
	@After( value = "@annotation(com.chsoft.aop.ClientMethod)")
    public void beforeMethodXML(JoinPoint joinPoint) {  
        //String opreate = joinPoint.getSignature().getName();  
        System.out.println("我最开始运行啦啊");  
    }
}