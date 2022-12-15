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
package io.seata.samples.tcc.service;

import io.seata.core.context.RootContext;
import io.seata.manualapi.api.SeataClient;
import io.seata.samples.tcc.action.TccActionTwo;
import io.seata.samples.tcc.action.impl.TccActionOneImpl;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;

/**
 * The type Tcc transaction service.
 *
 * @author zhangsen
 */
public class TccTransactionService {
    

    private TccActionTwo tccActionTwo;

    /**
     * 发起分布式事务
     *
     * @return string string
     */

    public String doTransactionCommit() throws Exception {
        
        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
        try {
            tx.begin(60000, "testBiz");

            SeataClient seataClient = new SeataClient();

            seataClient.init("tcc-sample", "my_test_tx_group");

            TccActionOneImpl tccActionOne = new TccActionOneImpl();
            TccActionOneImpl seataClientProxy  = seataClient.createProxy(tccActionOne);
            boolean result1 = seataClientProxy.prepare(null, 1);
            
            
            //if data negative rollback else commit
            if (result1) {
                tx.commit();
            } else {
                tx.rollback();
            }
        } catch (Exception exx) {
            tx.rollback();
            throw exx;
        }
        return RootContext.getXID();
    }


    /**
     * Sets tcc action two.
     *
     * @param tccActionTwo the tcc action two
     */
    public void setTccActionTwo(TccActionTwo tccActionTwo) {
        this.tccActionTwo = tccActionTwo;
    }
}
