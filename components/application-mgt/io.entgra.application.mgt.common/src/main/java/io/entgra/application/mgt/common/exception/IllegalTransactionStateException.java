/* Copyright (c) 2019, Entgra (Pvt) Ltd. (http://www.entgra.io) All Rights Reserved.
 *
 * Entgra (Pvt) Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.entgra.application.mgt.common.exception;

/**
 * Exception thrown due to an issue in database transactions.
 */
public class IllegalTransactionStateException extends RuntimeException {

    private static final long serialVersionUID = -3151279331929070297L;

    public IllegalTransactionStateException(String msg, Exception nestedEx) {
        super(msg, nestedEx);
    }

    public IllegalTransactionStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTransactionStateException(String msg) {
        super(msg);
    }

}