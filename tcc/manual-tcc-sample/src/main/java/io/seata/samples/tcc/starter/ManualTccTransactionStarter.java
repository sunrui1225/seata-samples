/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.tcc.starter;

import io.seata.samples.tcc.ApplicationKeeper;
import io.seata.samples.tcc.action.impl.TccActionOneImpl;
import io.seata.samples.tcc.action.impl.TccActionTwoImpl;
import io.seata.samples.tcc.service.TccTransactionService;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The type Local tcc transaction starter.
 *
 * @author zhangsen
 */
public class ManualTccTransactionStarter {

    /**
     * The Application context.
     */
    static AbstractApplicationContext applicationContext = null;

    /**
     * The Tcc transaction service.
     */
    static TccTransactionService tccTransactionService = null;

    /**
     * The Tcc action one.
     */
    static TccActionOneImpl tccActionOne = null;

    /**
     * The Tcc action two.
     */
    static TccActionTwoImpl tccActionTwo = null;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[] {"spring/seata-tcc.xml"});

        tccTransactionService = (TccTransactionService)applicationContext.getBean("tccTransactionService");

        tccActionOne = (TccActionOneImpl)applicationContext.getBean("tccActionOne");
        tccActionTwo = (TccActionTwoImpl)applicationContext.getBean("tccActionTwo");

        //分布式事务提交demo
        transactionCommitDemo();
        
        new ApplicationKeeper(applicationContext).keep();
    }

    private static void transactionCommitDemo() throws Exception {
        String txId = tccTransactionService.doTransactionCommit();
        System.out.println(txId);
//        Assert.isTrue(StringUtils.isNotBlank(txId), "事务开启失败");

        Thread.sleep(1000L);

//        Assert.isTrue("T".equals(ResultHolder.getActionOneResult(txId)), "tccActionOne commit failed");
//        Assert.isTrue("T".equals(ResultHolder.getActionTwoResult(txId)), "tccActionTwo commit failed");

        System.out.println("transaction commit demo finish.");
    }

}
