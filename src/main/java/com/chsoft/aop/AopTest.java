package com.chsoft.aop;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chsoft.fabric.FabricClient;
import com.chsoft.fabric.FabricUser;
import com.chsoft.localTest.ChannelTest;
import com.chsoft.localTest.E2e_Config;


//import com.chsoft.fabric.FabricClient;
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"classpath*:beans.xml"})
public class AopTest {  
	
	@Resource 
	private FabricClient fabricClient;
	 
	@Resource 
	private ChannelTest channelTest;
	
	@Test  
    public void aopTest() throws Exception{
		E2e_Config config = new E2e_Config();
		fabricClient.init(config.getAdminList().get(0));
		Set<String> queryChannels = fabricClient.queryChannels(config.getAdminList().get(0),config.getPeerList().get(0).get(0));
		System.out.println("aa");
	}  
}  